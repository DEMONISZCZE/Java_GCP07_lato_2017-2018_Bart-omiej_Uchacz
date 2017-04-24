package gui1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import kolekcje_i_algorytmy.Student;

public class CustomTableView extends AnchorPane {
	
	private final ObservableList<Student> studentsList;
	private TableView<Student> tableView;
	private TableColumn<Student, String> mark;
	private TableColumn<Student, String> firstName;
	private TableColumn<Student, String> lastName;
	private TableColumn<Student, String> age;
	
	CustomTableView(){
		studentsList=FXCollections.observableArrayList();
		tableView=new TableView<Student>(studentsList);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		mark = new TableColumn<Student, String>("Mark");
		mark.setMinWidth(50);
		mark.setCellValueFactory(new PropertyValueFactory<Student,String>("mark"));
		
		firstName=new TableColumn<Student, String>("First Name");
		firstName.setMinWidth(200);
		firstName.setCellValueFactory(new PropertyValueFactory<Student,String>("firstName"));
		
		lastName=new TableColumn<Student, String>("Last Name");
		lastName.setMinWidth(200);
		lastName.setCellValueFactory(new PropertyValueFactory<Student,String>("lastName"));

		age=new TableColumn<Student, String>("Age");
		age.setMinWidth(50);
		age.setCellValueFactory(new PropertyValueFactory<Student,String>("age"));

		tableView.getColumns().addAll(mark,firstName,lastName,age);
		this.getChildren().add(tableView);
	}
	
	public void addStudent(Student student){
		studentsList.add(student);
	}
	public void removeStudent(Student student){
		studentsList.remove(student);
	}

}
