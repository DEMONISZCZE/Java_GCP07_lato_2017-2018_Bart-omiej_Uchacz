package gui1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import kolekcje_i_algorytmy.Student;

public class CustomListView extends AnchorPane{
	
	private ObservableList<String> logList = FXCollections.observableArrayList();
	private ListView<String> listView;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	CustomListView(){
		 listView = new ListView<String>(logList);
		 listView.setMinWidth(562);
		 this.getChildren().add(listView);		 
	}
	public void addedStudent(Student student){
		logList.add(dateFormat.format(new Date())+":  ADDED:  "+student);
	}
	public void removedStudent(Student student){
		logList.add(dateFormat.format(new Date())+":  REMOVED:  "+student);
	}
	public void notModifiedStudent(Student student){
		logList.add(dateFormat.format(new Date())+":  NOT MODIFIED:  "+student);
	}
	
}
