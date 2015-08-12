package ToolSet;
import java.util.Arrays;


/**
 * Creates String Arrays that can be used for input of the scheduler instead of sched_com.dat 
 * 
 * @author QZ61P8
 *
 */

public class DataStringBuilder {

	String ds="";
	
	private final String EQ = "=";
	private final String SEP = ";\n";
	
	public DataStringBuilder add(String name, int value){
		ds+=name+EQ+value+SEP;
		return this;
	}
	
	public DataStringBuilder add(String name, int[] value){
		ds+=name+EQ+Arrays.toString(value)+SEP;
		return this;
	}
	
	public DataStringBuilder add(String name, int[][] value){
		ds+=name+EQ+Arrays.deepToString(value)+SEP;
		return this;
	}
	
	public DataStringBuilder add(String name, int[][][] value){
		ds+=name+EQ+Arrays.deepToString(value)+SEP;
		return this;
	}
	
	public String toString(){
		return ds;
	}
	
}
