package gui1;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class CustomMenuBar extends MenuBar {

	Menu menuProgram;
	Menu menuAbout;
	Label menuAboutLabel;
	Alert info;
	
	MenuItem close;
	
	CustomMenuBar(){
		menuProgram=new Menu("Program");
		close=new MenuItem("Close Ctrl+C");
		close.setOnAction((t) -> {
			System.exit(0);
		});
		menuProgram.getItems().add(close);
		menuAbout=new Menu();
		
		info = new Alert(AlertType.INFORMATION);
		info.setTitle("About");
		info.setHeaderText(null);
		info.setContentText("Bartłomiej Uchacz");

		menuAboutLabel=new Label("About");
		menuAboutLabel.setOnMouseClicked((t)->{
			info.showAndWait();
		});
		menuAbout.setGraphic(menuAboutLabel);
		
		this.getMenus().addAll(menuProgram,menuAbout);
		
	}
}
