package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Controller {

    // fxml imports
    @FXML private TableColumn<TestFile,String> fileCol;
    @FXML private TableColumn<TestFile,String> classCol;
    @FXML private TableColumn<TestFile,String> probCol;
    @FXML private TableView<TestFile> table;
    @FXML private TextField accuracyField;
    @FXML private TextField precisionField;
    @FXML private TextField lowBoundField;
    @FXML private TextField upBoundField;

    // probability maps
    private HashMap<String,Float> PrWH = new HashMap<>();
    private HashMap<String,Float> PrWS = new HashMap<>();
    private HashMap<String,Float> PROBABILITYMAP = new HashMap<>();

    // words lists/sets
    private List<TestFile> testFiles = new ArrayList<>();
    private HashSet<String> bagOfWords = new HashSet<>();

    // files maps
    private HashMap<File, HashSet<String>> spamTrainFilesMap = new HashMap<>();
    private HashMap<File, HashSet<String>> hamTrainFilesMap = new HashMap<>();

    // important values
    private File mainDirectory;
    private float accuracy = 0;
    private float precision = 0;
    private float lB = (float) 0.27;
    private float uB = (float) 0.79;
    private ObservableList<TestFile> data = FXCollections.observableArrayList();

    public void setMainDirectory(File mainDirectory){
        this.mainDirectory = mainDirectory;
        System.out.println(this.mainDirectory.getAbsolutePath());
    }


    public void training(){

        //Lists to store the training files in respective folders
        List<File> allTrainHamFiles = new ArrayList<File>(Arrays.asList(getTrainHamNames()));
        List<File> allTrainSpamFiles = new ArrayList<File>(Arrays.asList(getTrainSpamNames()));

        // TRAINING PHASE
        System.out.println("------------TRAINING-----------");

        // Maps file with a set of words of that file
        spamTrainFilesMap = storeWords(allTrainSpamFiles);
        hamTrainFilesMap = storeWords(allTrainHamFiles);

        List<File> allTrainFiles = new ArrayList<>(allTrainSpamFiles); // List of all the training files ham or spam

        allTrainFiles.addAll(allTrainHamFiles);
        HashMap<File, HashSet<String>> wordsOfFiles = storeWords(allTrainFiles); // makes a map of all files,
                                                                                // with words of each file as a set
        for(Map.Entry<File,HashSet<String>> f : wordsOfFiles.entrySet()){ // iterate all the files, create a bag of words to use
            bagOfWords.addAll(f.getValue()); // for each set of words of a file, add all the words to the bag of words
        }
        System.out.println("Training ham frequency...");
        PrWH = trainHamFreq(bagOfWords); // probability map of word -> prob that file is ham given word
        System.out.println("Training spam frequency...");
        PrWS = trainSpamFreq(bagOfWords); // probability map of word -> prob that file is spam given word


    }
    public void testing(){
        // TESTING
        System.out.println("------------TESTING-----------");
        PROBABILITYMAP.clear();
        System.out.println("Computing probability map...");

        PROBABILITYMAP = PrSW(PrWH,PrWS, bagOfWords); // creates overall probability map that a file is spam given word
        List<File> allTestHamFiles = new ArrayList<File>(Arrays.asList(getTestHamNames()));
        List<File> allTestSpamFiles = new ArrayList<File>(Arrays.asList(getTestSpamNames()));

        try {

            // put all files to be tested into one map (file -> set of words of said file)
            List<File> allTestFiles = new ArrayList<>(allTestSpamFiles);
            allTestFiles.addAll(allTestHamFiles);
            HashMap<File, HashSet<String>> mapOfTestFiles = storeWords(allTestFiles);


            float PrSW;
            for(Map.Entry<File,HashSet<String>> entry : mapOfTestFiles.entrySet()){ // iterate the test files
                float n = 0;
                for(String s : entry.getValue()){ // for every word of the current file
                    if (PROBABILITYMAP.get(s) != null){ //if the probability map has the word
                        PrSW = PROBABILITYMAP.get(s);
                        n = n + (float)(Math.log(1-PrSW) - Math.log(PrSW)); // add to summation

                    }
                }
                float PrSF = (float) (1/(1+Math.pow(Math.E,n))); // calculates the probability of that file being spam
                String parent = entry.getKey().getParent();

                //create table object with file info
                testFiles.add(new TestFile(entry.getKey().getName(),PrSF,parent.substring(parent.lastIndexOf("\\")+1)));
            }


            setAccuracyAndPrecision(testFiles);
//            System.out.println("Accuracy: " + this.accuracy);
//            System.out.println("Precision: " + this.precision);



        } catch (Exception e) {
            e.printStackTrace();
        }
        // set values for tableview
        data.addAll(testFiles);
        table.setItems(data);
        accuracyField.setText(Float.toString(this.accuracy));
        precisionField.setText(Float.toString(this.precision));
        fileCol.setCellValueFactory(new PropertyValueFactory<TestFile,String>("Filename"));
        classCol.setCellValueFactory(new PropertyValueFactory<TestFile,String>("ActualClass"));
        probCol.setCellValueFactory(new PropertyValueFactory<TestFile,String>("SpamProbRounded"));

    }

    private HashMap<String,Float> PrSW (HashMap<String,Float> wH, HashMap<String,Float> wS, HashSet<String> words){
        HashMap<String,Float> prSW = new HashMap<>();
        for(String s :words){ // iterate the bag of words
            if (!(wH.get(s) == 0 && wS.get(s) == 0)){ // if the word is in the spam and ham maps

                float val = wS.get(s)/(wH.get(s) + wS.get(s)); // calculate probabilty for the word
                if((val > 0.01 && val < this.lB) || (val > this.uB && val < 0.99)){ // put it in the proibability map,
                                                                    // only if it probably isnt spam, or if it is spam

                    // lB and uB initially defaulted for first run, user changes values in UI
                    prSW.put(s,val);
                }
            }


        }
        return prSW; // return the map of word -> probability
    }
    private void setAccuracyAndPrecision(List<TestFile> tF){ //from a list of files, calculates precision and accuracy
        List<TestFile> correctSpams = new ArrayList<>();
        List<TestFile> spamGuesses = new ArrayList<>();
        List<TestFile> hamsNotSpam = new ArrayList<>();
        double spamicity = 0.8; // default 'spaminess', best detectors are said to be 0.5, but at very least is 0.8

        for (TestFile f : tF){ // for every file
            if (f.getSpamProbability() >= spamicity){ // if file is spam

                spamGuesses.add(f); // spam guess
                if (f.getActualClass().equalsIgnoreCase("spam")){ // if it was actually a spam file
                    correctSpams.add(f); // correct spam
                }
            }

            if (f.getActualClass().equalsIgnoreCase("ham") && f.getSpamProbability() < spamicity){
                                                                                        // if the file was a correct ham
                hamsNotSpam.add(f);
            }
        }

        //compute accuracy and precision values
        this.accuracy = (float)(correctSpams.size() + hamsNotSpam.size())/ tF.size();
        this.precision = (float)(correctSpams.size())/spamGuesses.size();


    }

    // below 4 functions gets list of files from directory selected by user
    private File [] getTrainSpamNames(){
        File spamFiles = new File(this.mainDirectory.getAbsolutePath()+"\\train\\spam\\");
        return spamFiles.listFiles();
    }
    private File [] getTrainHamNames(){ // makes an array of ham files
        File hamFiles = new File(this.mainDirectory.getAbsolutePath()+"\\train\\ham\\");

        return hamFiles.listFiles();
    }
    private File [] getTestSpamNames(){
        File spamFiles = new File(this.mainDirectory.getAbsolutePath()+"\\test\\spam\\");

        return spamFiles.listFiles();
    }
    private File [] getTestHamNames(){ // makes an array of ham files
        File hamFiles = new File(this.mainDirectory.getAbsolutePath()+"\\test\\ham\\");

        return hamFiles.listFiles();
    }


    private HashMap<File, HashSet<String>> storeWords(List<File> files) { // stores words of a file into map
        HashMap<File, HashSet<String>> filesWords = new HashMap<>();

        for (File f : files) { // for every file
            try {
                HashSet<String> words = new HashSet<>();// set of words of the file
                FileReader fileReader = new FileReader(f);
                BufferedReader buffer = new BufferedReader(fileReader);
                String nextWord = null;
                while ((nextWord = buffer.readLine()) != null) { // read in line by line of the file

                    // format the line, split it into a string list of words, iterate
                    for (String s : nextWord.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+")) {
                        if (s.length() < 100) { // kind of redundant, but just incase some random word with 100+ length sneaks through
                            words.add(s);
                        }
                    }
                }
                filesWords.put(f, words); // map the file to the words of that file
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filesWords;
    }

    private HashMap<String, Float> trainHamFreq(HashSet<String> words) { // Pr(W|H) - probability word is ham
        HashMap<String, Float> freqWordsInHam = new HashMap<>();
        HashMap<String, Float> probability = new HashMap<>();
        for(Map.Entry<File,HashSet<String>> entry : hamTrainFilesMap.entrySet()){ // for every ham training file
            for(String s : words){ // for every word of that file

                if(freqWordsInHam.get(s) == null){ // if word hasnt been accounted for yet add it to map
                    freqWordsInHam.put(s,(float)0);
                }
                if (entry.getValue().contains(s)){//if the ham file contains the word

                    freqWordsInHam.put(s,(freqWordsInHam.get(s)+1)); // increment frequency



                }
            }
        }
        for(String s : freqWordsInHam.keySet()){ // computes P(W|H) stores it in hamProbMap
            probability.put(s,freqWordsInHam.get(s)/ getTrainHamNames().length);
        }
        return probability; // returns map of Pr(W|H)

    }
    private HashMap<String, Float> trainSpamFreq(HashSet<String> words){ // Pr(W|S) - probability word is in spam
        HashMap<String, Float> freqWordsInSpam = new HashMap<>();
        HashMap<String, Float> prob = new HashMap<>();

        for(Map.Entry<File,HashSet<String>> entry : spamTrainFilesMap.entrySet()){ // for every spam training file
            for(String s : words){ // for every word of the file
                if(freqWordsInSpam.get(s) == null){ // if word not accounted for add it
                    freqWordsInSpam.put(s,(float)0);
                }
                if (entry.getValue().contains(s)){ // if found in spam
                    freqWordsInSpam.put(s,(freqWordsInSpam.get(s)+1)); // increment frequency
                }
            }
        }
        for(String s : freqWordsInSpam.keySet()){ // computes P(W|H) stores it in hamProbMap
            prob.put(s,freqWordsInSpam.get(s)/ getTrainSpamNames().length);
        }
        return prob; // returns map of Pr(W|H)

    }


    public void clicked(javafx.event.ActionEvent actionEvent) { // button clicked
        try{
            data.removeAll(data); // reset observable list
            this.accuracy = 0;
            this.precision = 0;

            //get user entered values
            this.lB = Float.parseFloat(lowBoundField.getText());
            this.uB = Float.parseFloat(upBoundField.getText());

            //update the table based on new values
            testing();
            table.refresh();

        }catch(Exception ex){
            ex.getStackTrace();
        }
    }
}
