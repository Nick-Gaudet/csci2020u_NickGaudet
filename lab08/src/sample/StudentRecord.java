package sample;

public class StudentRecord {
    private String letterGrade,ID;
    private float finalGrade,midterm,assignments,finalExam;
    public StudentRecord(String ID, float midterm, float assignments, float finalExam){
        this.ID = ID;
        this.midterm = midterm;
        this.assignments = assignments;
        this.finalExam = finalExam;
    }

    public String getLetterGrade() {
        if (80 <= this.getFinalGrade() && this.getFinalGrade() <= 100){
            this.letterGrade = "A";
        }
        if (70 <= this.getFinalGrade() && this.getFinalGrade() < 80){
            this.letterGrade = "B";
        }
        if (60 <= this.getFinalGrade() && this.getFinalGrade() < 70){
            this.letterGrade = "C";
        }
        if (50 <= this.getFinalGrade() && this.getFinalGrade() < 60){
            this.letterGrade = "D";
        }
        if (this.getFinalGrade() < 50){
            this.letterGrade = "F";
        }
        return letterGrade;
    }

    public String getID() {
        return ID;
    }

    public float getFinalGrade() {
        return (float) ((this.getAssignments()*0.20) + (this.getMidterm()*0.30) + (this.getFinalExam()*0.50));
    }

    public float getMidterm() {
        return midterm;
    }

    public float getAssignments() {
        return assignments;
    }

    public float getFinalExam() {
        return finalExam;
    }
}
