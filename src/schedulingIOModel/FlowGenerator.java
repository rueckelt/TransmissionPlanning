package schedulingIOModel;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import optimization.ModelAccess;
import toolSet.PersistentStore;


public class FlowGenerator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8184271622466363691L;
	public static final String TG_NAME = "FlowGenerator";
	private Vector<Flow> flows = new Vector<Flow>();
	
	public FlowGenerator(){
		
	}
	public FlowGenerator(int duration, int requests){
		addTestTraffic(duration,requests);
	}
	
	public static FlowGenerator loadTrafficGenerator(String path){
		FlowGenerator tg=null;
		try{
			tg= (FlowGenerator) PersistentStore.loadObject(path+TG_NAME);
		}catch(Exception e){
			e.printStackTrace();
		}
		return tg;
	}
	
	//write this object to file
	public void writeObject(String dest){
		PersistentStore.storeObject(dest+TG_NAME, this);
	}
	
	public Vector<Flow> getFlows(){
		return flows;
	}
	
	public void addFlow(Flow flow){
		flows.add(flow);
	}
	
	//duration in timeslots!
	private void addTraffic(int duration, int requests){
		
		for(int i = 0; i < requests; ++i){
			//TODO generate traffic
		}
	}
	
	private void addTestTraffic(int duration, int requests){
		Random r=new Random();
		r.setSeed(System.nanoTime());
		int tmp=duration/requests;
		for(int i=0;i<requests;i++){
			if(i%5==0){			//0		VoIP
				flows.add(Flow.IPCall(i*tmp, r.nextInt(tmp)+20+i*tmp)); 	//todo
				//System.out.println("ADD FLOW ("+i+"/"+requests+"): IPCall");
			}else if(i%5==1 || i%5==4){		//4..10		Browsing
				flows.add(Flow.UserRequest(i*tmp, (int)Math.round(20+Math.sqrt(duration))));
				//				System.out.println("ADD FLOW ("+i+"/"+requests+"): UserRequest "+(int)Math.round(30+Math.sqrt(duration)));
			}else if(i%5==2){		//3		Download
				flows.add(Flow.Update(i * tmp, duration/2));
				//System.out.println("ADD FLOW ("+i+"/"+requests+"): Update");
			}else{					//Stream
				flows.add(Flow.BufferableStream(i*tmp, tmp*5));
				//System.out.println("ADD FLOW ("+i+"/"+requests+"): BufStream");
			}
		}
	}
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param requests	if < 1, then calculate number of requests automatically
	 */
	private void addBrowsing(int startTime, int endTime, int requests){
		int duration = endTime-startTime;
		int max_request_size = 40;
		if(requests<1){
			requests = 2*duration/max_request_size;
		}		
		
		Random rnd = new Random();
		rnd.setSeed(System.nanoTime());

		for(int i=0; i<requests; i++){
			int start = rnd.nextInt(duration);
			int chunks = rnd.nextInt(max_request_size);
			flows.add(Flow.UserRequest(startTime+start, chunks));		
		}
	}
	

	@SuppressWarnings("resource")
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
			impThroughputMax+=flow.getImpThroughputMax();
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
		
		//write file
		try {
			PrintWriter pw = new PrintWriter(dest);
			pw.println(content);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setTrafficData(IloOplModel model, IloOplFactory fac) {
		//initialize arrays
		int[] chunks = new int[flows.size()];
		int[] deadline = new int[flows.size()];
		int[] startTime = new int[flows.size()];
		int[] windowMax = new int[flows.size()];
		int[] throughputMax = new int[flows.size()];
		int[] windowMin = new int[flows.size()];
		int[] throughputMin = new int[flows.size()];
		int[] reqLatency = new int[flows.size()];
		int[] reqJitter = new int[flows.size()];
		
		int[][] importance = new int[7][flows.size()];

		int[] impUser = new int[flows.size()];
		//nRequests does not change..does it?
		
		//set values of arrays
		int i=0;
		for(Flow flow:flows){
			chunks[i]		=flow.getChunks();
			deadline[i]		=flow.getDeadline();
			startTime[i]	=flow.getStartTime();
			windowMax[i]	=flow.getWindowMax();
			throughputMax[i]=flow.getChunksMax();
			windowMin[i]	=flow.getWindowMin();
			throughputMin[i]=flow.getChunksMin();
			reqLatency[i]	=flow.getReqLatency();
			reqJitter[i]	=flow.getReqJitter();
			
			importance[0][i]	=	flow.getImpDeadline();
			importance[1][i]	=	flow.getImpStartTime();
			importance[2][i]	=	flow.getImpThroughputMin();
			importance[3][i]	=	flow.getImpThroughputMax();
			importance[4][i]	=	flow.getImpUnsched();
			importance[5][i]	=	flow.getImpLatency();
			importance[6][i]	=	flow.getImpJitter();
			
			impUser[i]		=		flow.getImpUser();
			
			i++;
		}
		
		//set values in Model
		ModelAccess.set(fac, model, "nChunks", chunks);
		ModelAccess.set(fac, model, "deadline", deadline);
		ModelAccess.set(fac, model, "prefStartTime", startTime);
		ModelAccess.set(fac, model, "minTimeBetweenChunks", windowMax);
		ModelAccess.set(fac, model, "stretch_max", throughputMax);
		ModelAccess.set(fac, model, "maxTimeBetweenChunks", windowMin);
		ModelAccess.set(fac, model, "compress_min", throughputMin);
		ModelAccess.set(fac, model, "latency", reqLatency);
		ModelAccess.set(fac, model, "jitter", reqJitter);
		
		ModelAccess.set(fac, model, "importance", importance);
		ModelAccess.set(fac, model, "userImportance", impUser);
		
	}
	
	
}
