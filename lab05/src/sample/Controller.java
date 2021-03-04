package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;


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


}
