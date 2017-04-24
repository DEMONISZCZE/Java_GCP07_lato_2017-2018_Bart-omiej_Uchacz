package kolekcje_i_algorytmy;

public class ConsoleLogger implements Logger {
	public void log(String status, Student student){
		System.out.println(status+" : "+student);
	}
}
