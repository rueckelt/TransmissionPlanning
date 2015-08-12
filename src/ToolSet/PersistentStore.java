package ToolSet;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class PersistentStore {

	public static void storeObject(String filename, Object o){
		//http://www.tutorialspoint.com/java/java_serialization.htm
		 // serialize the object
		 try {
			 FileOutputStream fileOut = new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(o);
	         out.close();
	         fileOut.close();
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		 //end of snippet
	}
	
	public static Object loadObject(String filename){
		Object o= null;
		//http://stackoverflow.com/questions/8887197/reliably-convert-any-object-to-string-and-then-back-again
		// deserialize the object
		try {
			 FileInputStream fileIn = new FileInputStream(filename);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         o = in.readObject();
	         in.close();
	         fileIn.close();
		 } catch (Exception e) {
		     System.out.println(e);
		 }
		 //end of snippet
		return o;
	}

}
