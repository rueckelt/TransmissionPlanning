import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;


public class PersistentStore {

	public static void storeObject(String filename, Object o){
		String serializedObject = "";

		//http://stackoverflow.com/questions/8887197/reliably-convert-any-object-to-string-and-then-back-again
		 // serialize the object
		 try {
		     ByteArrayOutputStream bo = new ByteArrayOutputStream();
		     ObjectOutputStream so = new ObjectOutputStream(bo);
		     so.writeObject(o);
		     so.flush();
		     bo.flush();
		     serializedObject = bo.toString();
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		 //end of snippet
		 
		//write file
		try {
			PrintWriter pw = new PrintWriter(filename);
			pw.println(serializedObject);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
		     e.printStackTrace();
		}
		 

	}
	
	public static Object loadObject(String filename){
		//read from file: http://abhinandanmk.blogspot.de/2012/05/java-how-to-read-complete-text-file.html
		String serializedObject="";
		try {
			serializedObject = new Scanner(new File(filename)).useDelimiter("\\A").next();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		};
		
		
		Object o= null;
		//http://stackoverflow.com/questions/8887197/reliably-convert-any-object-to-string-and-then-back-again
		// deserialize the object
		try {
		     byte b[] = serializedObject.getBytes(); 
		     ByteArrayInputStream bi = new ByteArrayInputStream(b);
		     ObjectInputStream si = new ObjectInputStream(bi);
		     o = si.readObject();
		 } catch (Exception e) {
		     System.out.println(e);
		 }
		 //end of snippet
		return o;
	}

}
