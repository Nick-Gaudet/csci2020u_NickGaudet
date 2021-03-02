package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;



public class Controller {


    @FXML private TableColumn<StudentRecord, String> sidCol;
    @FXML private TableColumn<StudentRecord, String> assignCol;
    @FXML private TableColumn<StudentRecord,String> midCol;
    @FXML private TableColumn<StudentRecord,String> examCol;
    @FXML private TableColumn<StudentRecord,String> markCol;
    @FXML private TableColumn<StudentRecord,String> gradeCol;
    @FXML private TableView<StudentRecord> table;

    public static ObservableList<StudentRecord> data = DataSource.getAllMarks();

    @FXML
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
}
