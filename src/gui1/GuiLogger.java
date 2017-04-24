package gui1;

//import gui2.MainGUI2;
import javafx.application.Platform;
import kolekcje_i_algorytmy.Logger;
import kolekcje_i_algorytmy.Student;

public class GuiLogger implements Logger {
	MainGUI gui;
	//MainGUI2 gui2;
	public GuiLogger(MainGUI gui) {
		this.gui = gui;
		//this.gui2=null;
	}
	//public GuiLogger(MainGUI2 gui2){
		//this.gui2=gui2;
		//this.gui=null;
	//}
	@Override
	public void log(String status, Student student) {
		if (gui!=null){
			Platform.runLater(new Runnable(){public void run(){
				switch (status){

				case "ADDED":
					MainGUI.addStudent(student);
					break;
				case "REMOVED":
					MainGUI.removeStudent(student);
					break;
				case "NOT MODIFIED":
					MainGUI.notModifiedStudent(student);
					break;
				}	
			}});

		}
		/*if (gui2!=null){
			Platform.runLater(new Runnable(){public void run(){
				switch (status){
				case "ADDED":
					MainGUI2.addStudent(student);
					break;
				case "REMOVED":
					MainGUI2.removeStudent(student);
					break;
				case "NOT MODIFIED":
					MainGUI2.notModifiedStudent(student);
					break;
				}*/
			}};
