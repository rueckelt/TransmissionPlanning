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

import ToolSet.PersistentStore;
import ToolSet.RndInt;
import optimization.ModelAccess;


public class FlowGenerator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8184271622466363691L;
	public static final String TG_NAME = "TrafficGenerator";
	private Vector<Flow> flows = new Vector<Flow>();
	
	public FlowGenerator(){
		
	}
	public FlowGenerator(int duration, int requests){
		addTraffic(duration,requests);
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
	
	
	/*
	 * Realistic traffic shaping:
	 * 
	 * 
	 * http://www.cisco.com/c/dam/en/us/solutions/service-provider/vni-service-adoption-forecast/index.html?CAMPAIGN=Mobile+VNI+2016&COUNTRY_SITE=us&POSITION=Press+Release&REFERRING_SITE=PR&CREATIVE=PR+to+infographic
	 * Cisco report:
	 * Video 			2015: 55%		2020: 75%
	 * Overall Traffic	2015: 495MB		2020: 3.3GB / Month		
	 * 
	 * Sandvine 2014 Fixed line:
	 * https://www.sandvine.com/downloads/general/global-internet-phenomena/2014/1h-2014-global-internet-phenomena-report.pdf
	 * On-demand video: ~45%
	 * Browsing ~10%
	 * Downlods: ~10%
	 * 
	 * Sandvine 2014 Mobile:
	 * On-demand video: ~25%
	 * Browsing: 20%
	 * 
	 * ###################################
	 * OUR MODEL???
	 * 55% bufferable stream /e.g. video-on-demand or music-on-demand (about average for today)
	 * 25% background / e.g. app updates, updates of high precision maps, periodic delay-tolerant traffic (average today is <10%, but we expect vehicles to have higher maintenance traffic for highly autonomous driving)
	 * 15% liveStream  / e.g. live transmission, skype (not used very often; today's average, usually low data rates, usually heavy tailed and plannable)
	 * 5% interactive /e.g. browsing (used much but usually low data rates; long times without interaction; usually not plannable!!!)
	 * 
	 * bufferable and download provide potentials to optimize in time
	 * 
	 */
	
	//bad method; does only work for flow count multiple of 4. But otherwise the traffic share is not possible
	//too lazy to implement rest; so parameter 
	private void addTraffic(int duration, int flows){
		for(int i=0;i<flows/4;i++){
			add4(duration);
		}
	}
	
	private void add4(int duration){
		int overall_tokens = duration*(15+RndInt.get(0,30));	//random amount of overall traffic, but values 45 and 30 have no deeper reason
		System.out.println("tokens:"+overall_tokens);
		int tokens_bufferable 	= (int) (overall_tokens*0.25);
		int tokens_background 	= (int) (overall_tokens*0.55);
		int tokens_live 		= (int) (overall_tokens*0.15);
		int tokens_interactive 	= (int) (overall_tokens*0.5);
		
		//bufferable
		int buf_length=duration/RndInt.get(2, 3);	//scale duration from half length to 20% length
		int buf_starttime=RndInt.get(1, duration-buf_length)-1;		//random start time in a way that it can finish before end
		flows.add(Flow.BufferableStream(buf_starttime, buf_length, tokens_bufferable));
		
		//background 
		int back_deadline=RndInt.get(duration*2/3, duration-1);	//deadline not in first half
		flows.add(Flow.Background(tokens_background, back_deadline));
		
		//liveStream
		int live_start_time= RndInt.get(0,duration/2);
		int deadline= RndInt.get(live_start_time*3/2, duration-1);
		flows.add(Flow.LiveStram(live_start_time, deadline, tokens_live));
		
		//interactive
		int interactive_start_time=RndInt.get(0, duration-10);
		flows.add(Flow.Interactive(interactive_start_time, tokens_interactive));
		
	}
	
	
	
//	private void addTraffic_old(int duration, int requests){
//		Random r=new Random();
//		r.setSeed(System.nanoTime());
//		int tmp=duration/requests;
//		int m=6;
//		for(int i=0;i<requests;i++){
//			if(i%m==0|| i%m==4){					//Stream 0
//				flows.add(Flow.BufferableStream(i*tmp, tmp*6));
////				System.out.println("ADD FLOW ("+i+"/"+requests+"): BufStream");
//			}else if(i%m==3 || i%m==5){		//4..10		Browsing
//				flows.add(Flow.UserRequest(i*tmp, (int)Math.round(20+Math.sqrt(duration))));
////				System.out.println("ADD FLOW ("+i+"/"+requests+"): UserRequest "+(int)Math.round(30+Math.sqrt(duration)));
//			}else if(i%m==1 ){		//3		Download
//				flows.add(Flow.Update(duration));
////				System.out.println("ADD FLOW ("+i+"/"+requests+"): Update");
//			}else{			//0		VoIP
//				flows.add(Flow.IPCall(i*tmp, r.nextInt(tmp)+20+i*tmp)); 	//todo
////				System.out.println("ADD FLOW ("+i+"/"+requests+"): IPCall");
//			}
//		}
//	}
	
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
			flows.add(Flow.Interactive(startTime+start, chunks));		
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
//			impThroughputMax+=flow.getImpThroughputMax();
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
