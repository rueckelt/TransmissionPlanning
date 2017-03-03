package ToolSet;

//import javax.json;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;



public class JsonLogger {
	//public int array[][][];
	public void printArray(JsonArray ja) {
		
	}
    public static void main(String[] args) throws IOException {    
    	/*
    	printArray(json2Array("./Output.json"));
    	List<String> paths = new ArrayList<String>();
    	String path1 = new String("./output-478.json");
    	String path2 = new String("./output-486.json");
    	paths.add(path1);
    	paths.add(path2);
    	array2Json(json2Array(paths), "./merge.json");*/
    	String path11 = new String("./output-478.json");
    	String path21 = new String("./output-486.json");
    	List<String> paths1 = new ArrayList<String>();
    	paths1.add(path11);
    	paths1.add(path21);
    	array2Json(json2Array(paths1), "./real.json");


    	
    	
 //   	array2Json(json2Array("./sp.json"));

    }
	// write array to json file
	public static void array2Json(int array[][][], String path) {
        Gson gson = new Gson();

        JsonObject root = new JsonObject();
        JsonObject config = new JsonObject();
        config.addProperty("flowNum", gson.toJson(array.length));
        config.addProperty("timeSlotNum", array[0].length);
        config.addProperty("netNum", array[0][0].length);
        root.add("config", config);
        
        JsonArray spArray = new JsonArray();        

        for (int i = 0; i < array.length; i++) {
            JsonObject sp = new JsonObject();
            JsonArray planArray = new JsonArray();
        	for (int j = 0; j < array[0].length; j++) {
        		for (int k = 0; k < array[0][0].length; k++) {
        	        JsonObject plan = new JsonObject();
        			if (array[i][j][k] != 0) {
        				plan.addProperty("timeSlot", j+1);
        				plan.addProperty("network", k+1);
        				plan.addProperty("cap", array[i][j][k]);
        				planArray.add(plan);
        			}
        			
        		}
        	}
			sp.addProperty("flowId", i+1);
        	sp.add("plan", planArray);
        	spArray.add(sp);
        	
        }
        root.add("schedulePlan", spArray);
        ////////System.out.println(root.toString());
        try {
			writer(root, path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writer(JsonObject root, String path) throws IOException {
		Gson gson = new Gson();
        Writer writer = new FileWriter(path);
        gson.toJson(root, writer);
        writer.close();
	}
	public static int[][][] json2Array(String path) {
		 
        Gson gson = new Gson();
        try {
			JsonReader reader = new JsonReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        JsonObject jsonObject = new JsonObject();
        
    	int array[][][] = null;
        	// TODO catch json syntax error
        	// TODO parse the json according to some template so that the hard coding can be avoided.
    	try{
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(path));
            jsonObject = jsonElement.getAsJsonObject();
            JsonObject config = jsonObject.get("config").getAsJsonObject();
            int flowNum = config.get("flowNum").getAsInt();
            int netNum = config.get("netNum").getAsInt();
            int timeSlotNum = config.get("timeSlotNum").getAsInt();
            array = new int[flowNum][timeSlotNum][netNum];
            ////////System.out.println("flowNum: " + flowNum + " netNum: " + netNum + " timeSlotNum: " + timeSlotNum);
            JsonArray f1 = (JsonArray) jsonObject.get("schedulePlan");
            for (JsonElement p : f1) {
            	if (p.isJsonObject()) {
            		JsonObject plan = p.getAsJsonObject();
            		int flowId = plan.get("flowId").getAsInt();
            		//////////System.out.println("flowId: " + plan.get("flowId"));
            		JsonArray timecap = plan.get("plan").getAsJsonArray();
            		for (JsonElement tc: timecap) {
    					if (tc.isJsonObject()) {
    						JsonObject tcObj = tc.getAsJsonObject();
    						int timeSlot = tcObj.get("timeSlot").getAsInt();
    						int network = tcObj.get("network").getAsInt();
    						int cap = tcObj.get("cap").getAsInt();
    						array[flowId-1][timeSlot-1][network-1] = cap;
    	//					////////System.out.println("timeslot: " + timeSlot + ", " + "network: " + network + ", " + "cap: " + cap);
    					} 
            		}
            	}
            }
            
        } catch (FileNotFoundException e) {
        }
    	return array;
	}

	public static int[][] json2NetArray(String path) {
		 
        Gson gson = new Gson();
        try {
			JsonReader reader = new JsonReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        JsonObject jsonObject = new JsonObject();
        
    	int array[][] = null;
        	// TODO catch json syntax error
        	// TODO parse the json according to some template so that the hard coding can be avoided.
    	try{
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(path));
            jsonObject = jsonElement.getAsJsonObject();
            JsonObject config = jsonObject.get("config").getAsJsonObject();
            int flowNum = config.get("flowNum").getAsInt();
            int netNum = config.get("netNum").getAsInt();
            int timeSlotNum = config.get("timeSlotNum").getAsInt();
            array = new int[netNum][timeSlotNum];
            //////////System.out.println("flowNum: " + flowNum + " netNum: " + netNum + " timeSlotNum: " + timeSlotNum);
            JsonArray f1 = (JsonArray) jsonObject.get("schedulePlan");
            for (JsonElement p : f1) {
            	if (p.isJsonObject()) {
            		JsonObject plan = p.getAsJsonObject();
            		int flowId = plan.get("flowId").getAsInt();
            		////////System.out.println("flowId: " + plan.get("flowId"));
            		JsonArray timecap = plan.get("plan").getAsJsonArray();
            		for (JsonElement tc: timecap) {
    					if (tc.isJsonObject()) {
    						JsonObject tcObj = tc.getAsJsonObject();
    						int timeSlot = tcObj.get("timeSlot").getAsInt();
    						int network = tcObj.get("network").getAsInt();
    						int cap = tcObj.get("cap").getAsInt();
    						array[network-1][timeSlot-1] += cap;
    						////////System.out.println("timeslot: " + timeSlot + ", " + "network: " + network + ", " + "cap: " + cap);
    					} 
            		}
            	}
            }
            
        } catch (FileNotFoundException e) {
        }
    	return array;
	}
	
	public static int[][][] json2Array(List<String> paths)  {
		 
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
    	int array[][][] = null;
    	int flowMax = 0, netMax = 0, timeSlotMax = 0;
    	int flowNum = 0, netNum = 0, timeSlotNum = 0;

		// get the biggest flowNum, timeslotNum, netNum
		for (String path : paths) {
	        try {
				JsonReader reader = new JsonReader(new FileReader(path));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	       	try{
	            JsonParser parser = new JsonParser();
	            JsonElement jsonElement = parser.parse(new FileReader(path));
	            jsonObject = jsonElement.getAsJsonObject();
	            JsonObject config = jsonObject.get("config").getAsJsonObject();
	            flowNum = config.get("flowNum").getAsInt();
	            netNum = config.get("netNum").getAsInt();
	            timeSlotNum = config.get("timeSlotNum").getAsInt();
	            flowMax = Math.max(flowMax, flowNum);
	            netMax = Math.max(netMax, netNum);
	            timeSlotMax = Math.max(timeSlotMax, timeSlotNum);

	       	} catch (FileNotFoundException e) {
	        }
		}
        array = new int[flowMax][timeSlotMax][netMax];
        ////////System.out.println("flowNum: " + flowMax + " netNum: " + netMax + " timeSlotNum: " + timeSlotMax);
		
		for (String path : paths) {
		  	try{
	            JsonParser parser = new JsonParser();
	            JsonElement jsonElement = parser.parse(new FileReader(path));
	            jsonObject = jsonElement.getAsJsonObject();
		  	
		    	JsonArray f1 = (JsonArray) jsonObject.get("schedulePlan");
				for (JsonElement p : f1) {
					if (p.isJsonObject()) {
						JsonObject plan = p.getAsJsonObject();
						int flowId = plan.get("flowId").getAsInt();
						////////System.out.println("flowId: " + plan.get("flowId"));
						JsonArray timecap = plan.get("plan").getAsJsonArray();
						for (JsonElement tc: timecap) {
							if (tc.isJsonObject()) {
								JsonObject tcObj = tc.getAsJsonObject();
								int timeSlot = tcObj.get("timeSlot").getAsInt();
								int network = tcObj.get("network").getAsInt();
								int cap = tcObj.get("cap").getAsInt();
								array[flowId-1][timeSlot-1][network-1] = cap;
								////////System.out.println("timeslot: " + timeSlot + ", " + "network: " + network + ", " + "cap: " + cap);
							} 
						}
					}
				}
		   } catch (FileNotFoundException e) {
		   }
			
		}

        
        	// TODO catch json syntax error
        	// TODO parse the json according to some template so that the hard coding can be avoided.

    	return array;
	}
	
    public static void printArray(int array[][][]) {
    	for (int i = 0; i < array.length; i++) {
 
    		////////System.out.println("f: " + i);
    		for (int j = 0; j < array[i].length; j++) {
    			System.out.print("t: " + j);
    			for (int k = 0; k < array[i][j].length; k++) {
    				System.out.print("n: " + k + " -> " + array[i][j][k] + " / ");	
    			}
    			////////System.out.println("");
    		}
    	}
    }
    
    public static void printArray(int array[][]) {
    	/*
    	for (int i = 0; i < array.length; i++) {
    		////////System.out.println("network: " + (i+1));
    		for (int j = 0; j < array[i].length; j++) {
    			System.out.print("timeslot: " + (j+1) + " - Capacity: " + array[i][j]);
    			////////System.out.println("");
    		}
    	}
    	*/
    	for (int i = 0; i < array.length; i++) {
    		////////System.out.println("network: " + (i+1));
			////////System.out.println("timeslot: ");
    		for (int j = 0; j < array[i].length; j++) {
    			////////System.out.println(j);
    		}
			////////System.out.println("Network: ");
    		for (int j = 0; j < array[i].length; j++) {
    			////////System.out.println(j);
    		}
    	}
    }
}

