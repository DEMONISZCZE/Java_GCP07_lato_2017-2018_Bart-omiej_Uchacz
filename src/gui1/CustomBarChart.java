package gui1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.AnchorPane;

public class CustomBarChart extends AnchorPane{
	
	private final ObservableList<Data<String,Integer>> marks;
	private CategoryAxis xAxis;
	private NumberAxis yAxis;
	private BarChart barChart;
	private XYChart.Series data;
	
	
	CustomBarChart(){
		marks=FXCollections.observableArrayList();
		marks.addAll(
				new Data<String,Integer>(Double.toString(3.0),0),
				new Data<String,Integer>(Double.toString(3.5),0),
				new Data<String,Integer>(Double.toString(4.0),0),
				new Data<String,Integer>(Double.toString(4.5),0),
				new Data<String,Integer>(Double.toString(5.0),0));
		xAxis=new CategoryAxis();
		xAxis.setLabel("Mark");
		
		yAxis=new NumberAxis();
		yAxis.setLabel("Count");
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0.0);
		yAxis.setUpperBound(10.0);
		yAxis.setTickUnit(1.0);
		yAxis.setMinorTickVisible(false);
		data=new XYChart.Series<String,Integer>(marks);
		
		barChart=new BarChart(xAxis, yAxis);
		barChart.setTitle("Distribution of marks");
		barChart.getData().add(data);
		barChart.setLegendVisible(false);
				
		this.getChildren().add(barChart);
	}
	
	public void addMark(double mark){
		for(Data<String,Integer> d:marks){
			if (d.getXValue().equals(Double.toString(mark))){
				d.setYValue(d.getYValue()+1);
				if (yAxis.getUpperBound()==(double)d.getYValue()){
					yAxis.setUpperBound(yAxis.getUpperBound()+1.0);
				}
				break;
			}
		}
		
	}
	
	public void removeMark(double mark){
		for (Data<String,Integer> d:marks){
			if (d.getXValue().equals(Double.toString(mark))){
				if (d.getYValue()>0){
					d.setYValue(d.getYValue()-1);
				}
				if (yAxis.getUpperBound()>10.0){
					yAxis.setUpperBound(yAxis.getUpperBound()-1.0);
				}
				break;
			}
		}
	}
	

}
