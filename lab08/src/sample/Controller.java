package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;


public class Controller {


    @FXML private TableColumn<StudentRecord, String> sidCol;
    @FXML private TableColumn<StudentRecord, String> assignCol;
    @FXML private TableColumn<StudentRecord,String> midCol;
    @FXML private TableColumn<StudentRecord,String> examCol;
    @FXML private TableColumn<StudentRecord,String> markCol;
    @FXML private TableColumn<StudentRecord,String> gradeCol;
    @FXML private TableView<StudentRecord> table;

    @FXML private TextField sidText;
    @FXML private TextField midText;
    @FXML private TextField finalText;
    @FXML private TextField assignText;
    @FXML private Button addButton;

    public static ObservableList<StudentRecord> data = DataSource.getAllMarks();
    private String currentFileName = "tableData.csv";


    public void init(){
        table.setEditable(true);
        table.setItems(data);
        sidCol.setCellValueFactory(new PropertyValueFactory<StudentRecord,String>("ID"));
        assignCol.setCellValueFactory(new PropertyValueFactory<StudentRecord,String>("Assignments"));
        midCol.setCellValueFactory(new PropertyValueFactory<StudentRecord,String>("Midterm"));
        examCol.setCellValueFactory(new PropertyValueFactory<StudentRecord,String>("FinalExam"));
        markCol.setCellValueFactory(new PropertyValueFactory<StudentRecord,String>("FinalGrade"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<StudentRecord,String>("LetterGrade"));
    }
    @FXML
    public void clicked(ActionEvent e){
        try{
            StudentRecord student = new StudentRecord(sidText.getText(), Float.parseFloat(midText.getText()),
                    Float.parseFloat(assignText.getText()),Float.parseFloat(finalText.getText()));
            data.add(student);
        }catch(Exception a){
            System.out.println("Invalid parameters!");
        }

    }

    public void newClicked(ActionEvent e) {
        data.clear();
    }

    public void load(){
        try {
            BufferedReader csvRead = new BufferedReader(new FileReader(currentFileName));
            csvRead.readLine(); //reads first line ignoring column titles
            String line;
            while((line = csvRead.readLine()) != null){
                String [] lineData = line.split(",");
                StudentRecord student = new StudentRecord(lineData[0], Float.parseFloat(lineData[1]),
                        Float.parseFloat(lineData[2]),Float.parseFloat(lineData[3]));
                data.add(student);            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(){
        File CSV = new File(this.currentFileName);
        try{
            FileWriter writeCsv = new FileWriter(CSV);
            writeCsv.append("SID,");
            writeCsv.append("Midterm,");
            writeCsv.append("Assignments,");
            writeCsv.append("FinalExam\n");

            for (StudentRecord s : this.data){
                writeCsv.append(s.getID() + "," + s.getMidterm()
                        + "," + s.getAssignments() + "," + s.getFinalExam());
                writeCsv.append("\n");
            }
            writeCsv.flush();
            writeCsv.close();


        }catch(IOException er){
            er.printStackTrace();
        }
    }
    public void openClicked(ActionEvent e) {
        data.clear();
        Stage thisStage = (Stage) table.getScene().getWindow();
        FileChooser fChoose = new FileChooser();
        fChoose.setTitle("Load File");
        fChoose.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        File selectedFile = fChoose.showOpenDialog(thisStage);
        this.currentFileName = selectedFile.getName();
        load();
    }

    public void saveClicked(ActionEvent e) {
        save();
    }

    public void saveAsClicked(ActionEvent e) {
        Stage thisStage = (Stage) table.getScene().getWindow();
        FileChooser fChoose = new FileChooser();
        fChoose.setTitle("Save File");
        fChoose.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        File selectedFile = fChoose.showSaveDialog(thisStage);
        this.currentFileName = selectedFile.getName();
        save();

    }

    public void exitClicked(ActionEvent e) {
        Stage thisStage = (Stage) table.getScene().getWindow();
        thisStage.close();
    }
}

