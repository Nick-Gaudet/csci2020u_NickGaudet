package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Controller {

    @FXML
    private TableColumn<TestFile,String> fileCol;
    @FXML private TableColumn<TestFile,String> classCol;
    @FXML private TableColumn<TestFile,String> probCol;
    @FXML private TableView<TestFile> table;
    private HashMap<String,Float> PrWH = new HashMap<>();
    private HashMap<String,Float> PrWS = new HashMap<>();
    private List<TestFile> testFiles = new ArrayList<>();
    private HashMap<File, HashSet<String>> wordsOfFiles = new HashMap<>();

    private List<File> allTrainHamFiles = new ArrayList<File>(Arrays.asList(getTrainHamNames()));
    private List<File> allTrainSpamFiles = new ArrayList<File>(Arrays.asList(getTrainSpamNames()));
    private List<File> allTestHamFiles = new ArrayList<File>(Arrays.asList(getTestHamNames()));
    private List<File> allTestSpamFiles = new ArrayList<File>(Arrays.asList(getTestSpamNames()));
    private HashMap<File, HashSet<String>> spamTrainFilesMap = new HashMap<>();
    private HashMap<File, HashSet<String>> hamTrainFilesMap = new HashMap<>();
    private HashMap<File, HashSet<String>> spamTestFilesMap = new HashMap<>();
    private HashMap<File, HashSet<String>> hamTestFilesMap = new HashMap<>();
    private HashMap<File, HashSet<String>> mapOfTestFiles = new HashMap<>();

    private float accuracy = 0;
    private float precision = 0;

    public void init(){

        ObservableList<TestFile> data = FXCollections.observableArrayList();
        try {
//            System.out.println(getHamWords(getHamNames()));
//            getHamWords(getHamNames());
//            getSpamWords(getSpamNames());
//            System.out.println(getHamWords(getHamNames()));
//            System.out.println(getSpamWords(getSpamNames()));

            // TRAINING PHASE
            System.out.println("------------TRAINING-----------");

            //Store ham files with words of each file
            spamTrainFilesMap = storeWords(allTrainSpamFiles);
            hamTrainFilesMap = storeWords(allTrainHamFiles);

            List<File> allTrainFiles = new ArrayList<>(allTrainSpamFiles);

            allTrainFiles.addAll(allTrainHamFiles);
            wordsOfFiles = storeWords(allTrainFiles);
            HashSet<String> bagOfWords = new HashSet<>();
            for(Map.Entry<File,HashSet<String>> f : wordsOfFiles.entrySet()){
                bagOfWords.addAll(f.getValue());
//                wordsOfFiles.get(f);
            }
            System.out.println("Length of BagofWords: " + bagOfWords.size());

            PrWH = trainHamFreq(bagOfWords);
            PrWS = trainSpamFreq(bagOfWords);
            HashMap<String,Float> PROBABILITYMAP = PrSW(PrWH,PrWS, bagOfWords);
            System.out.println(PROBABILITYMAP);
            // NEED MAP OF ALL SPAM/HAM TEST AS MAP<FILE,SET(BAGOFWORDS)> iterate it per word calcualting prob based on above

            spamTestFilesMap = storeWords(allTestSpamFiles);
            hamTestFilesMap = storeWords(allTestHamFiles);
            List<File> allTestFiles = new ArrayList<>(allTestSpamFiles);
            allTestFiles.addAll(allTestHamFiles);
            mapOfTestFiles = storeWords(allTestFiles);
            float PrSW;
            for(Map.Entry<File,HashSet<String>> entry : mapOfTestFiles.entrySet()){
                float n = 0;
                for(String s : entry.getValue()){
                    if (PROBABILITYMAP.get(s) != null){ //if the probability map has the word
                        PrSW = PROBABILITYMAP.get(s);
                        n = n + (float)(Math.log(1-PrSW) - Math.log(PrSW));

                    }
//                    else if (PROBABILITYMAP.get(s) == null){
//                        PrSW = (float)0.5;
//                        n = n + (float)(Math.log(1-PrSW) - Math.log(PrSW));
//
//                    }

                }
                float PrSF = (float) (1/(1+Math.pow(Math.E,n)));
                String parent = entry.getKey().getParent();
                testFiles.add(new TestFile(entry.getKey().getName(),PrSF,parent.substring(parent.lastIndexOf("\\")+1)));
            }

            // TESTING
            System.out.println("------------TESTING-----------");

            setAccuracyAndPrecision(testFiles);
            System.out.println("Accuracy: " + this.accuracy);
            System.out.println("Precision: " + this.precision);



        } catch (Exception e) {
            e.printStackTrace();
        }

        data.addAll(testFiles);
        table.setItems(data);

        fileCol.setCellValueFactory(new PropertyValueFactory<TestFile,String>("Filename"));
        classCol.setCellValueFactory(new PropertyValueFactory<TestFile,String>("ActualClass"));
        probCol.setCellValueFactory(new PropertyValueFactory<TestFile,String>("SpamProbRounded"));

    }
    private HashMap<String,Float> PrSW (HashMap<String,Float> wH, HashMap<String,Float> wS, HashSet<String> words){
        HashMap<String,Float> prSW = new HashMap<>();
        for(String s :words){
            if (!(wH.get(s) == 0 && wS.get(s) == 0)){
                float val = wS.get(s)/(wH.get(s) + wS.get(s));
                if((val > 0.01 && val < 0.27) || (val > 0.79 && val < 0.99)){ // put it in the proibability map,
                                                                    // only if it probably isnt spam, or if it is spam
                    prSW.put(s,val);
                }
//                if(val > 0.01 && val < 0.99){
//                    prSW.put(s,val);
//
//                }
            }


        }
        return prSW;
    }
    private void setAccuracyAndPrecision(List<TestFile> tF){
        List<TestFile> correctSpams = new ArrayList<>();
        List<TestFile> spamGuesses = new ArrayList<>();
        List<TestFile> hamsNotSpam = new ArrayList<>();
        double spamicity = 0.8;
        for (TestFile f : tF){
            if (f.getSpamProbability() >= spamicity){
                spamGuesses.add(f);
                if (f.getActualClass().equalsIgnoreCase("spam")){
                    correctSpams.add(f);
                }
            }

            if (f.getActualClass().equalsIgnoreCase("ham") && f.getSpamProbability() < spamicity){
                hamsNotSpam.add(f);
            }
        }

        this.accuracy = (float)(correctSpams.size() + hamsNotSpam.size())/ tF.size();
        this.precision = (float)(correctSpams.size())/spamGuesses.size();


    }

    private File [] getTrainSpamNames(){
        File spamFiles = new File(".\\src\\sample\\data\\train\\spam\\");
        return spamFiles.listFiles();
    }
    private File [] getTrainHamNames(){ // makes an array of ham files
        File hamFiles = new File(".\\src\\sample\\data\\train\\ham\\");

        return hamFiles.listFiles();
    }
    private File [] getTestSpamNames(){
        File spamFiles = new File(".\\src\\sample\\data\\test\\spam\\");

        return spamFiles.listFiles();
    }
    private File [] getTestHamNames(){ // makes an array of ham files
        File hamFiles = new File(".\\src\\sample\\data\\test\\ham\\");

        return hamFiles.listFiles();
    }


    private HashMap<File, HashSet<String>> storeWords(List<File> files) {
        HashMap<File, HashSet<String>> filesWords = new HashMap<>();

        for (File f : files) {
            try {
                HashSet<String> words = new HashSet<>();
                FileReader fileReader = new FileReader(f);
                BufferedReader buffer = new BufferedReader(fileReader);
                String nextWord = null;
                while ((nextWord = buffer.readLine()) != null) {
//                    nextWord = nextWord.replaceAll("[^a-zA-Z]+", " ");
                    for (String s : nextWord.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+")) {
                        if (s.length() > 2 && s.length() < 100) {
                            words.add(s);
                        }
                    }
                }
                filesWords.put(f, words);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filesWords;
    }

    private HashMap<String, Float> trainHamFreq(HashSet<String> words) throws FileNotFoundException { // Pr(W|H)
        HashMap<String, Float> freqWordsInHam = new HashMap<>();
        HashMap<String, Float> prob = new HashMap<>();
        File [] files = getTrainHamNames();
        for(Map.Entry<File,HashSet<String>> entry : hamTrainFilesMap.entrySet()){
            for(String s : words){
                if(freqWordsInHam.get(s) == null){
                    freqWordsInHam.put(s,(float)0);
                }
                if (entry.getValue().contains(s)){//if the file contains the word

                    freqWordsInHam.put(s,(freqWordsInHam.get(s)+1));



                }
            }
        }
        System.out.println(freqWordsInHam.size());
        for(String s : freqWordsInHam.keySet()){ // computes P(W|H) stores it in hamProbMap
            prob.put(s,freqWordsInHam.get(s)/ getTrainHamNames().length);
        }
        return prob; // returns map of Pr(W|H)

    }
    private HashMap<String, Float> trainSpamFreq(HashSet<String> words) throws FileNotFoundException { // Pr(W|S)
        HashMap<String, Float> freqWordsInSpam = new HashMap<>();
        HashMap<String, Float> prob = new HashMap<>();

        for(Map.Entry<File,HashSet<String>> entry : spamTrainFilesMap.entrySet()){
            for(String s : words){
                if(freqWordsInSpam.get(s) == null){
                    freqWordsInSpam.put(s,(float)0);
                }
                if (entry.getValue().contains(s)){
                    freqWordsInSpam.put(s,(freqWordsInSpam.get(s)+1));
                }
            }
        }
        System.out.println(freqWordsInSpam.size());

        for(String s : freqWordsInSpam.keySet()){ // computes P(W|H) stores it in hamProbMap
            prob.put(s,freqWordsInSpam.get(s)/ getTrainSpamNames().length);
        }
        return prob; // returns map of Pr(W|H)

    }



}
