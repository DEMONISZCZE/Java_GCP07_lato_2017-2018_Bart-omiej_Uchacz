
package kolekcje_i_algorytmy;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Crawler extends Thread {

	private int iteration=0;
	private int maxIter;
	private String path;
	private List<AddedInterface> addedList=new LinkedList<AddedInterface>();
	private List<IterInterface> iterList=new LinkedList<IterInterface>();
	private List<RemovedInterface> removedList=new LinkedList<RemovedInterface>();
	private List<NotModifiedInterface> notModifiedList=new LinkedList<NotModifiedInterface>();
	private List<ExtractInterface> extractList=new LinkedList<ExtractInterface>();
	private List<AgeInterface> ageList=new LinkedList<AgeInterface>();
	private List<MarkInterface> markList=new LinkedList<MarkInterface>();

	private Set<Student> studentsPrev=new HashSet<Student>();
	private List<Student> studentsList=null;
	
	private boolean isRunning;
	
	public synchronized void postCancel(){
		isRunning=false;
	}

	public boolean add(RemovedInterface e) {
		return removedList.add(e);
	}
	public boolean add(NotModifiedInterface e) {
		return notModifiedList.add(e);
	}
	public boolean add(AddedInterface e) {
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

	public boolean remove(AddedInterface arg0) {
		return addedList.remove(arg0);
	}
	public boolean remove(RemovedInterface arg0) {
		return removedList.remove(arg0);
	}
	public boolean remove(NotModifiedInterface arg0) {
		return notModifiedList.remove(arg0);
	}
	public boolean remove(IterInterface arg0) {
		return iterList.remove(arg0);
	}
	public boolean remove(ExtractInterface arg0) {
		return extractList.remove(arg0);
	}
	public boolean remove(AgeInterface arg0) {
		return ageList.remove(arg0);
	}
	public boolean remove(MarkInterface arg0) {
		return iterList.remove(arg0);
	}
	public int getMaxIter() {
		return maxIter;
	}
	public void setMaxIter(int maxIter) {
		this.maxIter = maxIter;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public Crawler(String path,int maxIter){
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
		for (NotModifiedInterface i:notModifiedList){
			i.handled(x);
		}
	}

	private void added(Student x){
		for (AddedInterface i:addedList){
			i.handled(x);
		}		
	}
	private void removed(Student x){
		for (RemovedInterface i:removedList){
			i.handled(x);
		}		
	}
	@Override
	public void run() {
		isRunning=true;
		while (isRunning){
			try{
				if (path==null)
					throw new CrawlerException("Nie zdefiniowana sciezka path");
				try{
					studentsList = StudentsParser.parse( new URL(path));
				} catch (IOException e) {e.printStackTrace();}
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

				studentsPrev=new HashSet<Student>();
				for (Student x:studentsList)
					studentsPrev.add(x);
				try{
					Thread.sleep(10000);
				} catch (InterruptedException e){e.printStackTrace();}
				iteration+=1;
				if (iteration==maxIter)
					break;
			} catch (CrawlerException e){
				e.printStackTrace();
				isRunning=false;
			}
		}
	}

	public static void main(String[] args){

		final Logger[] loggers=new Logger[]{
				new ConsoleLogger(),
				new MailLogger("javatestxd@gmail.com","javatestxd@gmail.com","smtp.gmail.com","ciastka8")
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
	}


}
