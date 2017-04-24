package gui1;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import kolekcje_i_algorytmy.Student;

public class CustomTabPane extends AnchorPane{
	private CustomListView logList=new CustomListView();
	private CustomTableView studentsList=new CustomTableView();
	private CustomBarChart histList=new CustomBarChart();
	
	TabPane tabs;
	Tab students;
	Tab log;
	Tab hist;
	
	CustomTabPane(){
		 tabs = new TabPane();
		 
		 students=new Tab();
		 students.setText("Students");
		 students.setContent(studentsList);
		 
		 log = new Tab();
		 log.setText("Log");
		 log.setContent(logList);
		 
		 hist=new Tab();
		 hist.setText("Histogram");
		 hist.setContent(histList);
		 
		 tabs.getTabs().add(students);
		 tabs.getTabs().add(log);
		 tabs.getTabs().add(hist);
		 this.getChildren().add(tabs);
	}
	
	public void addStudent(Student student){
		studentsList.addStudent(student);
		logList.addedStudent(student);
		histList.addMark(student.getMark());
	}
	
	public void removeStudent(Student student){
		studentsList.removeStudent(student);
		logList.removedStudent(student);
		histList.removeMark(student.getMark());
	}
	
	public void notModifiedStudent(Student student){
		logList.notModifiedStudent(student);
	}


}
