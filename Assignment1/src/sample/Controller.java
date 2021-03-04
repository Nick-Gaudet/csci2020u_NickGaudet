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
    private TableColumn<File,String> fileCol;
    @FXML private TableColumn<File,String> hamCol;
    @FXML private TableView<File> table;
    private HashMap<String,Integer> hamMap = new HashMap<>();
    private HashMap<String,Integer> spamMap = new HashMap<>();
    private HashSet<String> hamWords = new HashSet<String>();
    private HashSet<String> spamWords = new HashSet<String>();
    private HashMap<String,Float> wordMap = new HashMap<>();
    private HashMap<String,Float> spamProbMap = new HashMap<>();
    private HashMap<String,Float> hamProbMap = new HashMap<>();
    private HashMap<String,Float> spamWithWords = new HashMap<>();
    private HashMap<String,Float> hamWithWords = new HashMap<>();
    private HashMap<String,Float> probabilityMap = new HashMap<>();
    private HashSet<String> allWords = new HashSet<>();



    public void init(){

        ObservableList<File> data = FXCollections.observableArrayList();
        try {
//            System.out.println(getHamWords(getHamNames()));
            getHamWords(getHamNames());
            getSpamWords(getSpamNames());
//            System.out.println(getHamWords(getHamNames()));
//            System.out.println(getSpamWords(getSpamNames()));


            this.allWords.addAll(getHamWords(getHamNames()));
            this.allWords.addAll(getSpamWords(getSpamNames()));
            this.hamWithWords=trainHamFreq(this.allWords);
            this.spamWithWords=trainSpamFreq(this.allWords);
//            System.out.println(allWords);


            for (String s: this.allWords){ // P(S|W)
                this.probabilityMap.put(s,(this.spamWithWords.get(s)/(this.hamWithWords.get(s) + this.spamWithWords.get(s))));
            }
            for(File f: getHamNames()){
                System.out.println(f.getName() + " ---> " + isFileSpam(f));
            }
            for(File f: getSpamNames()){
                System.out.println(f.getName() + " ---> " + isFileSpam(f));
            }
//            System.out.println("The probability a file is spam = " + probMap(probabilityMap));
//            System.out.println(wordMap);

//            this.hamMap=trainHamFreq();
//            this.spamMap=trainSpamFreq();
//            for(String key: wordMap.keySet()){
//                if (wordMap.get(key) > 1){
//                    System.out.println(key + " = " +wordMap.get(key) );
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        data.addAll(getSpamNames());
        data.addAll(getHamNames());
        table.setItems(data);

        fileCol.setCellValueFactory(new PropertyValueFactory<File,String>("Name"));

    }
    private float isFileSpam(File f){ // passes file, computes spam probability of said file
        HashSet<String> words = new HashSet<String>();
        try{
            FileReader fileReader = new FileReader(f);
            BufferedReader buffer = new BufferedReader(fileReader);
            String nextWord = null;
            while ((nextWord = buffer.readLine()) != null) { // stores all words of the file into a set
                nextWord = nextWord.replaceAll("[^a-zA-Z]+"," ");
                for(String s : nextWord.split(" ")){
                    if (s.length() > 2){
                        words.add(s.toLowerCase());
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        float n = 0;
        for(String s: words){ // compute the probability, summating with each word
            n = (float) (Math.log(1-this.probabilityMap.get(s)) - Math.log(this.probabilityMap.get(s)));
        }
        return (float) (1/(1+Math.pow(Math.E,n)));
    }
    private File [] getSpamNames(){
        File spamFiles = new File(".\\src\\sample\\data\\train\\spam\\");

        return spamFiles.listFiles();
    }
    private File [] getHamNames(){ // makes an array of ham files
        File hamFiles = new File(".\\src\\sample\\data\\train\\ham\\");

        return hamFiles.listFiles();
    }
    private HashSet<String> getHamWords(File [] hamFiles) throws FileNotFoundException { // puts all words of ham files in a set
        try {
            for(int i = 0; i < hamFiles.length; i++){
                this.hamWords.addAll(getWords(hamFiles[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.hamWords;
    }
    private HashSet<String> getSpamWords(File [] spamFiles) throws FileNotFoundException { // puts all words of ham files in a set
        try {
            for(int i = 0; i < spamFiles.length; i++){
                this.spamWords.addAll(getWords(spamFiles[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.spamWords;
    }
    private HashSet<String> getWords(File f){ // gets words from a file
        HashSet<String> words = new HashSet<String>();
        try{
            FileReader fileReader = new FileReader(f);
            BufferedReader buffer = new BufferedReader(fileReader);
            String nextWord = null;
            while ((nextWord = buffer.readLine()) != null) {
                nextWord = nextWord.replaceAll("[^a-zA-Z]+"," ");
//                nextWord = nextWord.toLowerCase();
//                words.addAll(Arrays.asList(nextWord.split(" ")));

                for(String s : nextWord.split(" ")){
//                    s = s.replaceAll("\\d"," ");
                    if (s.length() > 2){
                        words.add(s.toLowerCase());

                    }
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return words;

    }
    private HashMap<String, Float> trainHamFreq(HashSet<String> words) throws FileNotFoundException { // Pr(W|H)
        File [] files = getHamNames();
        for(int i =0; i < files.length; i++){
            int count = 1;
            HashSet<String> strs = getWords(files[i]); // current files words
            for(String s: words){
                if (!strs.contains(s)){
                    continue;
                }
                if (strs.contains(s)){
                    this.wordMap.put(s, (float) count); // map frequency
                    count++;

                }
            }
        }
        this.hamProbMap = this.wordMap;
        for(String s : this.hamProbMap.keySet()){ // computes P(W|H) stores it in hamProbMap
            this.hamProbMap.put(s,this.wordMap.get(s)/ getHamNames().length);
        }

        return this.hamProbMap;

    }
    private HashMap<String, Float> trainSpamFreq(HashSet<String> words) throws FileNotFoundException { // Pr(W|S)
        File [] files = getSpamNames();
        for(int i =0; i < files.length; i++){
            int count = 1;
            HashSet<String> strs = getWords(files[i]); // current files words
            for(String s: words){
                if (!strs.contains(s)){
                    continue;
                }
                if (strs.contains(s)){
                    this.wordMap.put(s, (float) count);
                    count++;
                }
            }
        }
        this.spamProbMap = this.wordMap;
        for(String s : this.spamProbMap.keySet()){ // computes P(W|S) stores it in spamProbMap
            this.spamProbMap.put(s,this.wordMap.get(s)/ getHamNames().length);
        }
        return this.spamProbMap;

    }
    private float probMap(HashMap<String,Float> SW){
        HashMap<String, Float> probabilities = new HashMap<>();
        float n = 0;
        for(String s : SW.keySet()){
            n = (float) (Math.log(1-SW.get(s)) - Math.log(SW.get(s)));
        }

        return (float) (1/(1+Math.pow(Math.E,n)));
    }


}
