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
import java.util.Scanner;
import java.util.Vector;

import schedulers.Scheduler;

public class LogMatlabFormat {

	private String log;
	private List<String> logged_variables;

	public LogMatlabFormat() {
		initLog();
	}

	/**
	 * Log outputs for matlab input
	 * 
	 * Writes to log if variable does not exist yet. Would lead to (redundant)
	 * matlab overwrite elsewise
	 * 
	 * @param name
	 * @param value
	 * @return
	 */

	public void log(String name, long value) {
		if (!logged_variables.contains(name)) {
			log += logValue(name, value);
			logged_variables.add(name);
		}
	}

	public void log(String name, float value) {
		if (!logged_variables.contains(name)) {
			log += logValue(name, value);
			logged_variables.add(name);
		}
	}

	public void log(String name, int value) {
		if (!logged_variables.contains(name)) {
			log += logValue(name, value);
			logged_variables.add(name);
		}
	}

	public void log(String name, int[] value) {
		if (!logged_variables.contains(name)) {
			log += logValue(name, value);
			logged_variables.add(name);
		}
	}

	public void log(String name, int[][] value) {
		if (!logged_variables.contains(name)) {
			log += logValue(name, value);
			logged_variables.add(name);
		}
	}

	public void log(String name, int[][][] value) {
		if (!logged_variables.contains(name)) {
			log += logValue(name, value);
			logged_variables.add(name);
		}
	}

	public void comment(String comment) {
		log += "\n% " + comment.replaceAll("\n", "\n% ") + "\n";
	}

	public void logSchedulers(Vector<Scheduler> schedulers) {
		int i = 0;
		log += "\nscheduler_logs= {";
		String log_tmp = "\nschedulers= {";
		for (Scheduler scheduler : schedulers) {
			if (i > 0) { // no leading comma at first of list
				log += ",";
				log_tmp += ",";
			}
			log += "'" + scheduler.getLogfileName("") + "'";
			log_tmp += "'" + scheduler.getType() + "'";
			i++;
		}
		log += "};";
		log += log_tmp + "};\n";
	}

	public void writeLog(String filename) {
		PrintWriter pw = null;
		// check/create path
		String path = filename.substring(0,
				filename.lastIndexOf(File.separator));
		// System.out.println(path);
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}

		// write log file
		try {
			pw = new PrintWriter(filename);
			pw.println(log);
			pw.flush();
			pw.close();

			// clean log
			initLog();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String logValue(String name, long value) {
		return name + " = " + value + ";\n";
	}

	private String logValue(String name, float value) {
		return name + " = " + value + ";\n";
	}

	private String logValue(String name, int value) {
		return name + " = " + value + ";\n";
	}

	private String logValue(String name, int[] value) {
		return name + " = " + Arrays.toString(value) + ";\n";
	}

	private String logValue(String name, int[][] value) {
		String s = name + " = [";
		for (int i = 0; i < value.length; i++) {
			if (i > 0) {
				s += "; ";
			}
			for (int j = 0; j < value[0].length; j++) {
				if (j > 0) {
					s += ", ";
				}
				s += value[i][j];
			}
		}
		return s + "];\n";
	}

	private String logValue(String name, int[][][] value) {
		String s = "";
		for (int i = 0; i < value.length; i++) {
			String name2 = name + "(:,:," + (i + 1) + ")";
			s += logValue(name2, value[i]);
		}
		return s;
	}

	private void initLog() {
		logged_variables = new LinkedList<String>();
		log = "";

		// start with date
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = formatter.format(Calendar.getInstance().getTime());
		comment(date);
	}

	public static long loadValueFromLogfile(String varName, String logfileName){
		long v = 0;
		
		//read file to string
				Scanner scanner = null;
				String content="";
				try {
					scanner = new Scanner(new File(logfileName)).useDelimiter("\\Z");
					content = scanner.next();
				} catch (FileNotFoundException e) {
					System.err.println("LogMatlabFormat::load3DFromLogfile: reading logged result failed. File not found: "+logfileName);
					e.printStackTrace();
				} finally{
					scanner.close();
				}
				
				//parse lines
				content=content.trim();
				String lines[]=content.split("\n");
				
				for(String line : lines){
//					e.g. scheduling_duration_us = 152508;
					if(line.contains(varName)){
						String valueStr = line.substring(line.indexOf('=')+1, line.indexOf(';'));
						valueStr=valueStr.trim();
						try{
							v=Long.parseLong(valueStr);
							return v;
						}catch(Exception e){
							System.err.println("LobMatlabFormat: loadValue from File: invalid parseInt:"+valueStr);
							e.printStackTrace();
							break;
						}
					}
				}
		
		return v;
	}
	
	public static int[][][] load3DFromLogfile(String varName, String logfileName) {

		int values[][][] = null;
		
		//read file to string
		Scanner scanner = null;
		String content="";
		try {
			scanner = new Scanner(new File(logfileName)).useDelimiter("\\Z");
			content = scanner.next();
		} catch (FileNotFoundException e) {
			System.err.println("LogMatlabFormat::load3DFromLogfile: reading logged result failed. File not found: "+logfileName);
			e.printStackTrace();
		} finally{
			scanner.close();
		}
		
		//parse lines
		content=content.trim();
		String lines[]=content.split("\n");

		Vector<String> valid_lines = new Vector<String>();	//lines containing the variable
		for (String line:lines){
			line=line.trim();
			//identify right lines; each line is a network
			if(line.startsWith(varName)){

				line=line.substring(line.indexOf("[")+1, line.indexOf("]"));
				valid_lines.add(line);
			}
		}
		
		//get values from lines
		
		int f=0;
		for (String line :valid_lines){
//			System.out.println(line);
			String split2d[] = line.split(";");		//split time slots
			//for each time slot
			int t=0;
			for(String s2d:split2d){
				s2d=s2d.trim();
				String v1d[]=s2d.split(",");
				int n=0;
				for(String v:v1d){					//split networks
					//initialize output array with correct size once
					if(values==null){
						values= new int[valid_lines.size()][split2d.length][v1d.length];	//f_t_n
					}
					//write values to array
					v=v.trim();
					try{
						values[f][t][n]=Integer.parseInt(v);;
					}catch(Exception e){
						System.err.println("LobMatlabFormat: load3D from File: invalid parseInt:"+v);
						e.printStackTrace();
						break;
					}
					n++;
				}
				t++;
			}
			f++;	//next line in log file is new flow
		}
		return values;
	}
}
