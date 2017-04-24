package gui1;

import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kolekcje_i_algorytmy.AddedInterface;
import kolekcje_i_algorytmy.AgeInterface;
import kolekcje_i_algorytmy.ConsoleLogger;
import kolekcje_i_algorytmy.Crawler;
import kolekcje_i_algorytmy.ExtractInterface;
import kolekcje_i_algorytmy.IterInterface;
import kolekcje_i_algorytmy.Logger;
import kolekcje_i_algorytmy.MarkInterface;
import kolekcje_i_algorytmy.NotModifiedInterface;
import kolekcje_i_algorytmy.RemovedInterface;
import kolekcje_i_algorytmy.Student;

public class MainGUI extends Application {
	private BorderPane borderPane;
	private static CustomTabPane tabs;
	private CustomMenuBar menu;
	private Scene scene;
	private Label keyLabel;
	private Stage stage;

	@Override
	public void start(Stage stage) {      
		this.stage=stage;
		borderPane = new BorderPane();    
		borderPane.setMinSize(400, 400);

	    tabs=new CustomTabPane();

	    menu=new CustomMenuBar();
	    keyLabel = new Label();
	    borderPane.setTop(menu);
	    borderPane.setCenter(tabs);
	    borderPane.setBottom(keyLabel);

		scene = new Scene(borderPane);
	    scene.setOnKeyPressed((ke) -> {
                keyLabel.setText("Key Pressed: " + ke.getCode());
                if (ke.isControlDown() && ke.getCode().equals(KeyCode.C))
                	System.exit(0);
        });

		this.stage.setTitle("Crawler GUI"); 
	    this.stage.setScene(scene);
	    this.stage.show();
	}
	
	public static synchronized void addStudent(Student student){
		tabs.addStudent(student);
	}
	
	public static synchronized void removeStudent(Student student){
		tabs.removeStudent(student);
	}
	
	public static synchronized void notModifiedStudent(Student student){
		tabs.notModifiedStudent(student);
	}

	public static void main(String[] args){
	    MainGUI gui=new MainGUI();
	    
			Thread crawlLogger=new Thread(new Runnable(){public void run(){
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				final Logger[] loggers=new Logger[]{
						new ConsoleLogger(),
//						new MailLogger("pttMailTest@mail.com","pttMailTest@mail.com","smtp.mail.com","ptt_Mail_Test"),
						new GuiLogger(gui)
				};
				String tmp=new String("http://home.agh.edu.pl/~ggorecki/IS_Java/students.txt");
				Crawler crawl=new Crawler(tmp,0);
				AgeInterface aint=(min,max)->{System.out.println("Age: <"+min+","+max+">");};
				crawl.add(aint);
				MarkInterface mint=(min,max)->{System.out.println("Mark: <"+min+","+max+">");};
				crawl.add(mint);
				ExtractInterface eint=(list,mode)->{
					String m=new String();
					switch(mode){
					case FIRST_NAME:
						m="First Name";
						break;
					case LAST_NAME:
						m="Last Name";
						break;
					case AGE:
						m="Age";
						break;
					case MARK:
						m="Mark";
						break;
					}
					System.out.println("Ordered by "+m+":");
					for (Student x:list){
						System.out.println(x);
					}
				};
				crawl.add(eint);
				IterInterface iint=(iter)->{System.out.println("Iteracja numer: "+iter);};
				crawl.add(iint);
				AddedInterface addint=(s)->{
					for (Logger log:loggers){
							log.log("ADDED",s);	
						
					}
				};
				crawl.add(addint);
				RemovedInterface remint=(s)->{
					for (Logger log:loggers){
							log.log("REMOVED",s);	
						
					}
				};
				crawl.add(remint);
				NotModifiedInterface nonint=(s)->{
					for (Logger log:loggers){
							log.log("NOT MODIFIED",s);	
						
					}
				};
				crawl.add(nonint);
				crawl.run();
			}});
			crawlLogger.start();
			Application.launch(gui.getClass());
			try {
				crawlLogger.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
