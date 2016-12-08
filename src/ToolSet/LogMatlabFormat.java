package ToolSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import schedulers.Scheduler;


public class LogMatlabFormat {

	private String log;
	private List<String> logged_variables;
	
	
	public LogMatlabFormat(){
		initLog();
	}
	
	
	/**
	 * Log outputs for matlab input
	 * 
	 * Writes to log if variable does not exist yet. Would lead to (redundant) matlab overwrite elsewise
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	
	public void log(String name, long value){
		if(!logged_variables.contains(name)){
			log+=logValue(name, value);
			logged_variables.add(name);
		}
	}	
	public void log(String name, float value){
		if(!logged_variables.contains(name)){
			log+=logValue(name, value);
			logged_variables.add(name);
		}
	}	
	public void log(String name, int value){
		if(!logged_variables.contains(name)){
			log+=logValue(name, value);
			logged_variables.add(name);
		}
	}	
	public void log(String name, int[] value){
		if(!logged_variables.contains(name)){
			log+=logValue(name, value);
			logged_variables.add(name);
		}
	}	
	public void log(String name, int[][] value){
		if(!logged_variables.contains(name)){
			log+=logValue(name, value);
			logged_variables.add(name);
		}
	}	
	public void log(String name, int[][][] value){
		if(!logged_variables.contains(name)){
			log+=logValue(name, value);
			logged_variables.add(name);
		}
	}
	public void comment(String comment){
		log+="\n% "+comment.replaceAll("\n", "\n% ")+"\n";
	}
	
	public void logSchedulers(Vector<Scheduler> schedulers){
		int i=0;
		log+="\nscheduler_logs= {";
		String log_tmp = "\nschedulers= {";
		for(Scheduler scheduler:schedulers){
			if(i>0) {	//no leading comma at first of list
				log+=",";
				log_tmp+=",";
			}
			log+="'"+scheduler.getLogfileName("")+"'";
			log_tmp+="'"+scheduler.getType()+"'";
			i++;
		}
		log+="};";
		log+=log_tmp+"};\n";
	}
		
	public void writeLog(String filename){
		PrintWriter pw=null;
		//check/create path
		String path = filename.substring(0, filename.lastIndexOf(File.separator));
//		System.out.println(path);
		if(!new File(path).exists()){
			new File(path).mkdirs();
		}
		
		//write log file
		try {
			pw = new PrintWriter(filename);
			pw.println(log);
			pw.flush();
			pw.close();
			
			//clean log
			initLog();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private String logValue(String name, long value){
		 return name+" = "+value+";\n";
	}
	private String logValue(String name, float value){
		 return name+" = "+value+";\n";
	}
	private String logValue(String name, int value){
		 return name+" = "+value+";\n";
	}
	private String logValue(String name, int[] value){
		return name+" = "+Arrays.toString(value)+";\n";
	}
	private String logValue(String name, int[][] value){
		String s = name +" = [";
		for (int i = 0; i < value.length; i++) {
			if(i>0){
				s+="; ";
			}
			for (int j = 0; j < value[0].length; j++) {
				if(j>0){
					s+=", ";
				}
				s+=value[i][j];
			}
		}
		return s+"];\n";
	}
	private String logValue(String name, int[][][] value){
		String s="";
		for (int i = 0; i < value.length; i++) {
			String name2=name+"(:,:,"+(i+1)+")";
			s+=logValue(name2, value[i]);
		}
		return s;
	}
	
	private void initLog(){
		logged_variables = new LinkedList<String>();
		log="";
		
		//start with date
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = formatter.format(Calendar.getInstance().getTime());
		comment(date);
	}

}
