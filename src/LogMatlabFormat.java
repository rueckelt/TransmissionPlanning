import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;


public class LogMatlabFormat {
	String log="";
	
	
	/**
	 * Log outputs for matlab input
	 * @param name
	 * @param value
	 * @return
	 */
	
	public void log(String name, int value){
		log+=logValue(name, value);
	}	
	public void log(String name, int[] value){
		log+=logValue(name, value);
	}	
	public void log(String name, int[][] value){
		log+=logValue(name, value);
	}	
	public void log(String name, int[][][] value){
		log+=logValue(name, value);
	}
	public void comment(String comment){
		log+="\n% "+comment.replaceAll("\n", "\n% ")+"\n";
	}
		
	public void writeLog(String filename){
		PrintWriter pw=null;
		try {
			pw = new PrintWriter(filename);
			pw.println(log);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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

}
