package schedulers;
import java.util.Arrays;
import java.util.Vector;

import schedulingIOModel.CostFunction;
import schedulingIOModel.Flow;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.FlowGenerator;
import ToolSet.LogMatlabFormat;


public abstract class Scheduler {
	
	/**
	 * TODO: Different strategies
	 * 
	 */
	
	protected NetworkGenerator ng; 
	protected FlowGenerator tg;
	protected LogMatlabFormat logger;
	
	protected CostFunction cf;
	private long runtime =0;
	private int[][][] schedule_f_t_n;			//holds verified schedules only
	private int[][][] schedule_f_t_n_temp;		//may be used during calculation of a schedule
	
	
	public Scheduler(NetworkGenerator ng, FlowGenerator tg){
		boundFlowDeadlines(tg, ng);
		this.ng=ng;
		this.tg=tg;
		schedule_f_t_n=getEmptySchedule();
		logger = new LogMatlabFormat();
		cf=new CostFunction(ng, tg, logger);
	}
	
	//set limit for Deadlines of flows to scheduling length
	private void boundFlowDeadlines(FlowGenerator tg, NetworkGenerator ng) {
		if(tg!=null && ng!=null)
		for(Flow f: tg.getFlows()){
			if(!(f.getDeadline()<ng.getTimeslots())){
				f.setDeadline(ng.getTimeslots()-1);
			}
		}
	}

	/**
	 * 
	 * @param ng
	 * @param tg
	 * @param logfile path
	 * 
	 * calculates the schedule and stores it in internal variable schedule_f_t_n_temp
	 */
	protected abstract void calculateInstance_internal(String logfile);
	
	public abstract String getType();
	
	public String getLogfileName(String logpath){
		return logpath+getType()+"_log.m";
	}
	
	protected int[][][] getEmptySchedule(){
		if(tg==null || ng==null) return null;
		return new int[tg.getFlows().size()][ng.getNetworks().get(0).getSlots()][ng.getNetworks().size()];
	}
	
	public int[][][] getSchedule(){
		return schedule_f_t_n;
	}
	
	CostFunction getCostFunction(){
		return cf;
	}
	
	public int getCost(){
		return cf.costTotal(getSchedule());
	}
	
	public long getRuntimeNs(){
		return runtime;
	}

	
	/**
	 * Method called from outside to start calculation of schedule
	 * 
	 * @param ng
	 * @param tg
	 * @param logfile
	 */
	public void calculateInstance(String path){
		initTempSchedule();
		startTimer();
		calculateInstance_internal(path);	//result is stored to schedule_f_t_n_temp
		long duration = stopTimer();
		//Runtime rt = Runtime.getRuntime();
		//System.out.println("TotalMemory of JVM: "+(rt.totalMemory()-rt.freeMemory())/(1024*1024));
		
		//check if schedule holds required constraints
		if(verificationOfConstraints(schedule_f_t_n_temp)){
			schedule_f_t_n=schedule_f_t_n_temp;	//store to schedule_f_t_n if constraints hold
			
			logInstance(path, duration);

		}else{
			System.err.println(getType()+" Scheduler: calculated schedule violates constraints: "+path);
		}
	}

	/**
	 * evaluate cost of schedule_f_t_n and log results
	 * @param path
	 * @param duration
	 */
	protected void logInstance(String path, long duration) {
		//write log file for schedule including solve duration, schedule, cost function 
		logger.comment(getLogfileName(path));
		logger.log("scheduling_duration_us", duration/1000);
		
		logger.comment("schedule");
		logger.log("schedule_f_t_n", schedule_f_t_n_temp);
		
		
		//add cost function results to log file
		logger.comment("cost function results");
		cf=new CostFunction(ng, tg, logger);
		cf.calculate(schedule_f_t_n_temp);	//writes log
		
		logger.comment(showSchedule(schedule_f_t_n_temp));
		
		//finish logging and write to file
		logger.writeLog(getLogfileName(path));
	}
	
	

	/**
	 * 
		1. Do not overuse resources (network capacity limit)
		2. Do not exceed available number of Link Interfaces (interface limit)
		3. Do not allow parallel channel use for same flow
		4. Do not exceed upper throughput limit
		
	 * @return true if constraints hold
	 */
	
	private boolean verificationOfConstraints(int[][][] schedule_f_t_n){

		Vector<Network> networks = ng.getNetworks();
		Vector<Flow> flows = tg.getFlows();
		int time = ng.getTimeslots();
		
		for(int t=0; t<time;t++){
			//constraint 1+2 (per time slot)
			int[] network_used_by_type = new int[ng.getNofInterfaceTypes()]; //Do not exceed available number of Link Interfaces (2)
			boolean[] network_used = new boolean[ng.getNetworks().size()];
			for(int n=0;n<networks.size();n++){
				
				int network_use=0;	//overuse of resources (1)
				
				for(int f=0;f<flows.size();f++){
					network_use+=schedule_f_t_n[f][t][n];	//overuse of resources (1)
					
					if(!network_used[n]){	//do not count parallel use of the same network (2)
						network_used_by_type[networks.get(n).getType()]++; //Do not exceed available number of used Link Interfaces (2)
						network_used[n]=true;
					}
				}
				//check overuse of resources (1)
				if(network_use>networks.get(n).getCapacity().get(t)){
					System.err.println("Net use error: "+network_use+ " > "+networks.get(n).getCapacity().get(t)+", network "+n+" time "+t);
					return false;	//violation if used capacity of network in this time slot is larger than available capacity in this time slot
				}
			}
			//check: Do not exceed available number of Link Interfaces (2)
			for (int i = 0; i < network_used_by_type.length; i++) {
				if(network_used_by_type[i]>ng.getNofInterfacesByType()[i]){ //check if sum is larger than available interfaces
					System.err.println("Parallel link interface error : "+ network_used_by_type[i] +" > "+ng.getNofInterfacesByType()[i]);
					System.out.println(Arrays.toString(ng.getNofInterfacesByType()));
					return false;
				}
			}
			
			//check :Do not allow parallel channel use for same flow (3)
			for(int f=0;f<flows.size();f++){
				boolean flow_is_scheduled=false;
				for(int n=0;n<networks.size();n++){
					if(schedule_f_t_n[f][t][n]>0){
						if(flow_is_scheduled==false){
							flow_is_scheduled=true;	//set true if flow is scheduled in this time slot
						}else{
							System.err.println("Parallel channel use for same flow: "+f +" in time slot " + t);
							return false;	//violation if flow scheduled to a second network
						}
					}
				}
			}
		}
		//do not exceed upper throughput limit (4)
		for(int f=0;f<flows.size();f++){
			Flow flow = tg.getFlows().get(f);
			int winSize = flow.getWindowMax();
			int chunksMax = flow.getChunksMax();
			for(int t=0;t<(ng.getTimeslots()-winSize); t++){
				int chunkSum=0;	//sum of chunks in this window
				for(int tw=t;tw<t+winSize;tw++){
					for(int n=0; n<ng.getNetworks().size();n++){
						chunkSum+=schedule_f_t_n[f][tw][n];
					}
				}
				if(chunkSum>chunksMax){
					System.err.println("Upper throughput limit exceeded for flow: "+f +" in time window " + t+" to " + t+winSize +" by "+ (chunkSum-chunksMax));
//					return false;	//violation of upper tp limit
				}
			}
		}
		
		return true;
	}
	
	private void startTimer(){
		runtime=System.nanoTime();
	}
	
	private long stopTimer(){
		long my_runtime=System.nanoTime()-runtime;
		runtime=0;
		return my_runtime;
	}
	
	//#################### Allocation Support ###############
	
	protected void initTempSchedule(){
		schedule_f_t_n_temp=getEmptySchedule();
	}
	
	protected int[][][] getTempSchedule(){
		return schedule_f_t_n_temp;
	}
	protected void setTempSchedule(int[][][] schedule){
		schedule_f_t_n_temp=schedule;
	}
	
	/**
	 * 
	 * @param flow ID
	 * @param time slot
	 * @param network ID
	 * @param chunks to allocate
	 * @return number of allocated chunks
	 */
	protected int allocate(int flow, int time, int network, int chunks){
		if(!boundsValid(flow, time, network))return 0;

		//calculate remaining capacity of network in this slot
		int remaining_net_cap=getRemainingNetCap(network, time);
		int s[][][]=schedule_f_t_n_temp;
		int scheduled = chunks;
		
		//schedule as much as possible
		if(chunks>remaining_net_cap){
			s[flow][time][network]+=remaining_net_cap;
			scheduled= remaining_net_cap;
		}else{
			s[flow][time][network]+=chunks;
		}
		//any constraint violated? use if ok, else revert
		if(verificationOfConstraints(s)){
			schedule_f_t_n_temp=s;
			return scheduled;
		}else{
			return 0;
		}
		
		//calculate remaining capacity of network in this slot
//		int remaining_net_cap=getRemainingNetCap(network, time);
//		if(chunks>remaining_net_cap){
//			schedule_f_t_n_temp[flow][time][network]+=remaining_net_cap;
//			return remaining_net_cap;
//		}else{
//			schedule_f_t_n_temp[flow][time][network]+=chunks;
//			return chunks;
//		}
	}
	
	/**
	 * 
	 * @param network ID
	 * @param time slot
	 * @return remaining capacity of network in this time slot in currently calculated schedule
	 */
	protected int getRemainingNetCap(int network,int time){
		if(!boundsValid(0,time,network))return 0;
		
		int remaining_net_cap =ng.getNetworks().get(network).getCapacity().get(time);;		
		for(int f = 0; f<tg.getFlows().size();f++){
			remaining_net_cap-=schedule_f_t_n_temp[f][time][network];
		}
		return remaining_net_cap;
	}
	
	/**
	 * checks if values for flow index, time slot index and network index are in bounds
	 * @param f
	 * @param t
	 * @param n
	 * @return
	 */
	protected boolean boundsValid(int f, int t, int n){
		if(f<tg.getFlows().size() && t<ng.getTimeslots() && n<ng.getNetworks().size()){
			return true;
		}else{
			return false;
		}
	}
	
	
	public String showSchedule(int[][][] schedule_f_t_n){
		String s=getType()+"\n";
		for(int n=0; n<ng.getNetworks().size();n++){
			s+="\n############### Network "+n+" ##############";
			for(int f=0; f<tg.getFlows().size();f++){
				boolean used=false;
				for(int t=0; t<ng.getTimeslots();t++){
					if(schedule_f_t_n[f][t][n]>0){
						used=true;
						break;
					}
				}
				if(used){
					s+="\nF"+f+"\t|";
					for(int t=0; t<ng.getTimeslots();t++){
						if(t%10==0){
							s+="["+t+"]";
						}
						if(schedule_f_t_n[f][t][n]>0){
							s+=schedule_f_t_n[f][t][n];
						}
						s+="\t|";
						
					}
				}
			}
		}
		return s;
	}
	
	
	public int getFlowCounter() {
		return tg.getFlows().size();
	}
	
	public Flow getFlow(int pos) {
		return tg.getFlows().get(pos);
	}
	
}
