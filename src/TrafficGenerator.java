import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;


public class TrafficGenerator {
	
	private Vector<Flow> flows = new Vector<Flow>();
	private PrintWriter pw;
	
	
	public TrafficGenerator(){
		addTraffic();
	}
	
	private void addTraffic(){
		flows.add(Flow.IPCall(18, 90));
		flows.add(Flow.Update(250));
		addBrowsing(2, 150);
	}
	
	private void addBrowsing(int startTime, int endTime){
		int duration = endTime-startTime;
		int max_request_size = 15;
		int requests = 2*duration/max_request_size;
		
		
		Random rnd = new Random();
		rnd.setSeed(System.nanoTime());

		for(int i=0; i<requests; i++){
			int start = rnd.nextInt(duration);
			int chunks = rnd.nextInt(max_request_size);
			flows.add(Flow.UserRequest(startTime+start, chunks));		
		}
	}
	

	public void writeOutput(String source, String dest){
		//read source
		String content="";
		try {
			content = new Scanner(new File(source)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
		//build arrays
		String chunks="";
		String deadline = "";
		String startTime = "";
		String windowMax = "";
		String throughputMax = "";
		String windowMin = "";
		String throughputMin = "";
		String reqLatency = "";
		String reqJitter = "";
		
		String impDeadline = "";
		String impStartTime = "";
		String impThroughputMax = "";
		String impThroughputMin = "";
		String impUnscheduled = "";
		String impLatency = "";
		String impJitter = "";
		
		String impUser = "";
		
		boolean first = true;
		for (Flow flow : flows) {
			//list separators: omit before first list element
			if(first){
				first = false;
			}else{
				chunks+=", ";
				deadline+=", ";
				startTime+=", ";
				windowMax+=", ";
				throughputMax+=", ";
				windowMin+=", ";
				throughputMin+=", ";
				reqLatency+=", ";
				reqJitter+=", ";
				
				impDeadline+=", ";
				impStartTime+=", ";
				impThroughputMax+=", ";
				impThroughputMin+=", ";
				impUnscheduled+=", ";
				impLatency+=", ";
				impJitter+=", ";

				impUser+=", ";
			}
			//add list items
			chunks+=flow.getChunks();
			deadline+=flow.getDeadline();
			startTime+=flow.getStartTime();
			windowMax+=flow.getWindowMax();
			throughputMax+=flow.getChunksMax();
			windowMin+=flow.getWindowMin();
			throughputMin+=flow.getChunksMin();
			reqLatency+=flow.getReqLatency();
			reqJitter+=flow.getReqJitter();
			
			impDeadline+=flow.getImpDeadline();
			impStartTime+=flow.getImpStartTime();
			impThroughputMax+=flow.getImpThrouthputMax();
			impThroughputMin+=flow.getImpThroughputMin();
			impUnscheduled+=flow.getImpUnsched();
			impLatency+=flow.getImpLatency();
			impJitter+=flow.getImpJitter();

			impUser+=flow.getImpUser();
		}
		
		content=content.replace("[chunks]", chunks);
		content=content.replace("[deadline]", deadline);
		content=content.replace("[startTime]", startTime);
		content=content.replace("[windowMax]", windowMax);
		content=content.replace("[throughputMax]", throughputMax);
		content=content.replace("[windowMin]", windowMin);
		content=content.replace("[throughputMax]", throughputMax);
		content=content.replace("[throughputMin]", throughputMin);
		content=content.replace("[reqLatency]", reqLatency);
		content=content.replace("[reqJitter]", reqJitter);
		
		content=content.replace("[impDeadline]", impDeadline);
		content=content.replace("[impStartTime]", impStartTime);
		content=content.replace("[impThroughputMax]", impThroughputMax);
		content=content.replace("[impThroughputMin]", impThroughputMin);
		content=content.replace("[impUnscheduled]", impUnscheduled);
		content=content.replace("[impLatency]", impLatency);
		content=content.replace("[impJitter]", impJitter);
		
		content=content.replace("[impUser]", impUser);
		
		content=content.replace("[nRequests]", ""+flows.size());

		content= content.replace("[trafficGenTime]", new Date().toString());
		
		System.out.println(content);
		//write file
		try {
			pw = new PrintWriter(dest);
			pw.println(content);
			pw.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
