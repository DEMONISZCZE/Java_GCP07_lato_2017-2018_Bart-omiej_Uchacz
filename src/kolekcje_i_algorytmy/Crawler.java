
package kolekcje_i_algorytmy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


interface RInterface {
    void handled (Student x);
}
interface NInterface {
    void handled (Student x);
}
interface AInterface {
    void handled (Student x);
}
interface ExtractInterface {
	void handled(List<Student> list, OrderMode mode);
}
interface MarkInterface {
    void handled(double min, double max);
}
interface IterInterface {
    void handled(int iteration);
}
interface AgeInterface {
    void handled(int min, int max);
}

enum ExtremumMode
{
    MAX,
    MIN
}

enum OrderMode
{
    MARK,
    FIRST_NAME,
    LAST_NAME,
    AGE
}
class CrawlerException extends Exception
{
    public CrawlerException()
    {
        System.err.print("Nie skonfigurowany lub błędnie skonfigurowany adres.");
    }
}
public class Crawler {

	private int iteration=0;
	private int maxIter;
	private String path;
	private List<AInterface> addedList=new LinkedList();
	private List<IterInterface> iterList=new LinkedList();
	private List<RInterface> removedList=new LinkedList();
	private List<NInterface> notModifiedList=new LinkedList();
	private List<ExtractInterface> extractList=new LinkedList();
	private List<AgeInterface> ageList=new LinkedList();
	private List<MarkInterface> markList=new LinkedList();
	
	private Set<Student> studentsPrev=new HashSet();
	private List<Student> studentsList=null;

	public boolean add(RInterface e) {
		return removedList.add(e);
	}
	public boolean add(NInterface e) {return notModifiedList.add(e);}
	public boolean add(AInterface e) {
		return addedList.add(e);
	}
	public boolean add(IterInterface arg0) {
		return iterList.add(arg0);
	}
	public boolean add(ExtractInterface arg0) {
		return extractList.add(arg0);
	}
	public boolean add(AgeInterface arg0) {
		return ageList.add(arg0);
	}
	public boolean add(MarkInterface e) {
		return markList.add(e);
	}
	
	Crawler(String path,int maxIter){
		this.path=path;
		this.maxIter=maxIter;
	}
	private List<Student> extractStudents(OrderMode mode){
		switch(mode){
		case AGE:
			Collections.sort(studentsList,(left,right)-> Integer.compare(left.getAge(), right.getAge()));
			break;
		case MARK:
			Collections.sort(studentsList,(left,right)->Double.compare(left.getMark(), right.getMark()));
			break;
		case FIRST_NAME:
			Collections.sort(studentsList,(left,right)->{
				if (left.getFirstName()!=null){
					return left.getFirstName().compareTo(right.getFirstName());
				}
				return 0;
			});
			break;
		case LAST_NAME:
			Collections.sort(studentsList,(left,right)->{
				if (left.getLastName()!=null){
					return left.getLastName().compareTo(right.getLastName());
				}
				return 0;
			});
			break;
		}

		return studentsList;
	}
	private double extractMark(ExtremumMode mode){
		double ret=studentsList.get(0).getMark();
		for (Student x:studentsList){
			switch (mode){
			case MIN:
				if (x.getMark()<ret)
					ret=x.getMark();
				break;
			case MAX:
				if (x.getMark()>ret)
					ret=x.getMark();
			}
		}
		return ret;
	}
	private int extractAge(ExtremumMode mode){
		int ret=studentsList.get(0).getAge();
		for (Student x:studentsList){
			switch (mode){
			case MIN:
				if (x.getAge()<ret)
					ret=x.getAge();
				break;
			case MAX:
				if (x.getAge()>ret)
					ret=x.getAge();
			}
		}
		return ret;
	}

	private void notModified(Student x){
		for (NInterface i:notModifiedList){
			i.handled(x);
		}
	}

	private void added(Student x){
		for (AInterface i:addedList){
			i.handled(x);
		}		
	}
	private void removed(Student x){
		for (RInterface i:removedList){
			i.handled(x);
		}		
	}

	
	public void run() throws CrawlerException{
	while (true){
		if (path==null)
			throw new CrawlerException();

        if (path.contains(".txt")) {
            try {
                studentsList = StudentsParser.parse(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (path.contains("http://")) {
            try {
                studentsList = StudentsParser.parse(new URL(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		for (Student x:studentsList){
			if (studentsPrev.contains(x)){
				notModified(x);
			} else {
				added(x);
			}
		}
		for (Student x:studentsPrev){
			if (!studentsList.contains(x)){
				removed(x);
			}
		}
		for (IterInterface x:iterList)
			x.handled(iteration);
		for (ExtractInterface x:extractList)
			x.handled(extractStudents(OrderMode.MARK),OrderMode.MARK);
		for (AgeInterface x:ageList)
			x.handled(extractAge(ExtremumMode.MIN), extractAge(ExtremumMode.MAX));
		for (MarkInterface x:markList)
			x.handled(extractMark(ExtremumMode.MIN), extractMark(ExtremumMode.MAX));
		
		studentsPrev=new HashSet();
		for (Student x:studentsList)
			studentsPrev.add(x);
		try{
			Thread.sleep(10000);
		} catch (InterruptedException e){e.printStackTrace();}

		if (iteration==maxIter)
			break;
        iteration+=1;
	}
	}
	
	public static void main(String[] args){
		
		final Logger[] loggers=new Logger[]{
				new ConsoleLogger(),
				new MailLogger("javatestxd@gmail.com","javatestxd@gmail.com","smtp.gmail.com","ciastka8")
		};
		String source=new String("E:\\Java\\Projekty\\Java_GCP07_lato_2017-2018_Bart-omiej_Uchacz\\students.txt");
		Crawler crawl=new Crawler(source, 1);
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
		AInterface addint=(s)->{
			for (Logger log:loggers){
				log.log("ADDED",s);
			}
		};
		crawl.add(addint);
		RInterface remint=(s)->{
			for (Logger log:loggers){
				log.log("REMOVED",s);
			}
		};
		crawl.add(remint);
		NInterface nonint=(s)->{
			for (Logger log:loggers){
				log.log("NOT MODIFIED",s);
			}
		};
		crawl.add(nonint);
		try{
			crawl.run();
		} catch (CrawlerException e){e.printStackTrace();}
	}
	
	
}
