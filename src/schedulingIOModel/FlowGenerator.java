package schedulingIOModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import ToolSet.PersistentStore;
import ToolSet.RndInt;


public class FlowGenerator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8184271622466363691L;
	public static final String TG_NAME = "TrafficGenerator";
	private Vector<Flow> flows = new Vector<Flow>();

	private final float ALLOWED_ERROR_OFFSET = (float) 0.05;	//part of error/uncertainty model
	private int data_amount = 5;	//amount of data: usually between 1 and 10. can be higher.
	
	public FlowGenerator(){
		Flow.setNextId(new AtomicInteger(0));
	}
	
	public FlowGenerator(int duration, int requests){
		Flow.setNextId(new AtomicInteger(0));
		addTraffic(duration,requests);
	}
	
	public static FlowGenerator loadTrafficGenerator(String path){
		FlowGenerator tg=null;
		Flow.setNextId(new AtomicInteger(0));
		
		try{
			if(new File(path+TG_NAME).exists()){
				tg= (FlowGenerator) PersistentStore.loadObject(path+TG_NAME);
				tg.setFlowIndices();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tg;
	}

	public void writeObject(String dest){
		PersistentStore.storeObject(dest+TG_NAME, this);
	}
	
	public Vector<Flow> getFlows(){
		return flows;
	}
	
	public void addFlow(Flow flow){
		flows.add(flow);
		setFlowIndices();
	}
	
	public void setFlowIndices(){
		int i=0;
		for(Flow f :flows){
			f.setIndex(i);
			i++;
		}
	}
	
	public void setDataAmount(int amount){
		data_amount=amount;
	}
	public int getDataAmount(){
		return data_amount;
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
		addBufferable(duration);
		addBackground(duration);
		addLive(duration);
		addInteractive(duration);
	}
	
	private int getOverallTokens(int duration){
		return data_amount*duration*(15+RndInt.get(0,30))/10; //random amount of overall traffic, but values 45 and 30 have no deeper reason
	}
	private void addBufferable(int duration){
		int tokens_bufferable 	= (int) (getOverallTokens(duration)*0.25);
		//bufferable
		int buf_length=duration/RndInt.get(2, 3);	//scale duration from half length to 20% length
		int buf_starttime=RndInt.get(1, duration-buf_length)-1;		//random start time in a way that it can finish before end
		addFlow(Flow.BufferableStream(buf_starttime, buf_length, tokens_bufferable));
	}
	private void addBackground(int duration){
		int tokens_background 	= (int) (getOverallTokens(duration)*0.55);
		//background 
		int back_deadline=RndInt.get(duration/2, duration-1);	//deadline not in first half
		addFlow(Flow.Background(tokens_background, back_deadline));
	}
	private void addLive(int duration){
		int tokens_live 		= (int) (getOverallTokens(duration)*0.15);
		//liveStream
		int live_start_time= RndInt.get(0,duration/2);
		int deadline= RndInt.get(live_start_time*3/2+1, duration-1);
		addFlow(Flow.LiveStram(live_start_time, deadline, tokens_live));	
	}
	private void addInteractive(int duration){		
		int tokens_interactive 	= (int) (getOverallTokens(duration)*0.05);
		//interactive burst
		int interactive_start_time=RndInt.get(0, duration-10);
		addFlow(Flow.Interactive(interactive_start_time, tokens_interactive));
	}
	
	
	
//	private void addTraffic_old(int duration, int requests){
//		Random r=new Random();
//		r.setSeed(System.nanoTime());
//		int tmp=duration/requests;
//		int m=6;
//		for(int i=0;i<requests;i++){
//			if(i%m==0|| i%m==4){					//Stream 0
//				addFlow(Flow.BufferableStream(i*tmp, tmp*6));
////				System.out.println("ADD FLOW ("+i+"/"+requests+"): BufStream");
//			}else if(i%m==3 || i%m==5){		//4..10		Browsing
//				addFlow(Flow.UserRequest(i*tmp, (int)Math.round(20+Math.sqrt(duration))));
////				System.out.println("ADD FLOW ("+i+"/"+requests+"): UserRequest "+(int)Math.round(30+Math.sqrt(duration)));
//			}else if(i%m==1 ){		//3		Download
//				addFlow(Flow.Update(duration));
////				System.out.println("ADD FLOW ("+i+"/"+requests+"): Update");
//			}else{			//0		VoIP
//				addFlow(Flow.IPCall(i*tmp, r.nextInt(tmp)+20+i*tmp)); 	//todo
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
			addFlow(Flow.Interactive(startTime+start, chunks));		
		}
	}
	
	
	
	
	/**
	 * Uncertainty model: Uncertainty comes from user interaction
	 * flows can be canceled ; flows can be added. 
	 * a pause/continue of a flow is modeled as flow canceled and added later with "same" characteristics
	 */
	public void addUncertainty(float error, int timesteps){
		Vector<Flow> backup = cloneFlows(flows);
		
		float probAddCancel=error;
		float adapt =1;
		do{
			Flow.setNextId(new AtomicInteger(0));	
			probAddCancel = (float) (probAddCancel*(0.85+0.15*adapt));
			float probContinue = (float) (0.3);//*probAddCancel);
			
			flows = cloneFlows(backup);		//reset flows in each iteration without prior success
			
			addFlows(probAddCancel, timesteps);
//			float probCancel = 1/(1+probAddCancel);		//should result in equal amount of flows
			cancelFlows(probAddCancel, probContinue, timesteps);
			
			float act_error = new UncertaintyErrorCalculation().getFlowUncertaintyError(backup, flows);
			
			
			adapt = Math.min(2, error/act_error);	//upper limit for adaption step. Uncertainty may have huge random influence which lead to giant (unwanted) adaption.
			
			
//			System.out.println("act_error = "+act_error+", adapt = "+adapt+", probAddCancel = "+ probAddCancel);
		}while(adapt<(1-ALLOWED_ERROR_OFFSET) || 
				adapt>(1+ALLOWED_ERROR_OFFSET));
	}

	//cancel flows with certain probability 
	private void cancelFlows(float probCancel, float probContinue, int timesteps) {
		//do not iterate over list that is modified; add later
		Vector<Flow> toContinue = new Vector<Flow>();
		Vector<Flow> toRemove = new Vector<Flow>();
		for(Flow flow : flows){
			//cancel flow during transmission with certain probability
			float rndCancel = (float)RndInt.get(0, 1000)/1000;
			if(rndCancel<probCancel){
//				System.out.println("Cancel if "+rndCancel+"<"+probCancel);
				
				//cancel flow completely with certain probability (20%)
				if(RndInt.get(0,9)<2){
					//toRemove.add(flow);
					flow.setTokens(0); // if we remove the flow from the list, the executor cannot follow the original schedule plan with rowindex
				}else{
//					System.out.println("##canceled "+flow+";; ");
					//determine slot for cancellation during running transmission
					int cancelSlot = RndInt.get(flow.getStartTime()+1, flow.getDeadline());//(int)(RndInt.getGauss(flow.getStartTime()+1, flow.getDeadline()));
					int cutLength = flow.getDeadline()-cancelSlot;
	
//					System.out.print(" Continue..");
					//set new flow properties
					int newAmountOfTokens=flow.getTokens()*(cancelSlot-flow.getStartTime())/(flow.getDeadline()-flow.getStartTime());
					int remainingTokens= flow.getTokens()-newAmountOfTokens;	
					
					flow.setTokens(newAmountOfTokens);
					flow.setDeadline(cancelSlot);
	
//					System.out.print(" Continue..");
					
					//continue flow with certain probability
					float rndCont = ((float)RndInt.get(0, 1000)/1000);
//					System.out.println("Continue if "+rndCont+"<"+probContinue);
					if(rndCont<probContinue){
						//flow will be continued.
						int startTime = (int)RndInt.get(cancelSlot+2, timesteps-1);
						int deadline = Math.max(startTime+cutLength-1, timesteps-1);
	
						Flow contFlow = flow.clone();
						//contFlow.setId(-1);	//generate new id for cloned flow
						contFlow.setStartTime(startTime);
						contFlow.setDeadline(deadline);
						contFlow.setTokens(remainingTokens);
						toContinue.add(contFlow);
					}
				}
			}
		}

		flows.removeAll(toRemove);
//		System.out.println("######## after cancel of \n"+toRemove);
//		for(Flow f: flows){
//			System.out.println(f);
//		}
		flows.addAll(toContinue);
		setFlowIndices();
//		System.out.println("######## after continue \n"+toContinue);
//		for(Flow f: flows){
//			System.out.println(f);
//		}
//		System.out.println("finished cancelling");
	}
	
	private void addFlows(float probAdd, int duration) {

		int tries=flows.size();
		for(int i=0; i<tries; i++){
			float rndAdd = (float)RndInt.get(0, 1000)/1000;
//			System.out.println("Add if "+rndAdd+"<"+probAdd);
			if(rndAdd<probAdd){
				
				int category = RndInt.getGauss(1, 4, 3, 3);		//gauss between 1 and 4. Center is at 3, std-deviation is 1
//				System.out.println("add category "+ category);
				//add interactive and life stream with higher probability
				if(category==1){
					addLive(duration);
				}else if(category==2){
					addBufferable(duration);
				}else if(category==3){
					addInteractive(duration);
				}else if(category==4){
					addBackground(duration);
				}
			}
		}
//		System.out.println("finished adding flows");
	}
	
	public void writeOutput(String source, String dest){
				
		//read source
		String content="";
		try {
			content = new Scanner(new File(source)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchElementException e){
			System.err.println("FlowGenerator writeOutput (Opt) line 320: could not write file.\nsource: "+source +"\ndest: "+dest);
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
			chunks+=flow.getTokens();
			deadline+=flow.getDeadline();
			startTime+=flow.getStartTime();
			windowMax+=flow.getWindowMax();
			throughputMax+=flow.getTokensMax();
			windowMin+=flow.getWindowMin();
			throughputMin+=flow.getTokensMin();
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
		content=content.replace("[tpMinAmp]", ""+new CostFunction(null, null).getMinTpAmplifier());		//added tpMinAmplification 3.8.16, TR
		
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
	
	private Vector<Flow> cloneFlows(Vector<Flow> toClone){
		Vector<Flow> cloned_flows = new Vector<Flow>();
		for(Flow flow:toClone){
			cloned_flows.add(flow.clone());
		}
		return cloned_flows;
	}

}
