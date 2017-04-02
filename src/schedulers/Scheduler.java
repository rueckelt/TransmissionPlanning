package schedulers;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import schedulingIOModel.CostFunction;
import schedulingIOModel.Flow;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.FlowGenerator;
import ToolSet.InterfaceLimit;
import ToolSet.LogMatlabFormat;


public abstract class Scheduler {
	final boolean DEBUG = false;
	/**
	 * TODO: Different strategies
	 * 
	 */
	
	protected NetworkGenerator ng; 
	protected FlowGenerator tg;
	protected LogMatlabFormat logger;
	protected InterfaceLimit interfaceLimit;	//ensures interface constraint during scheduling with allocate(..)
	
	protected CostFunction cf;
	private long runtime =0;
	private int[][][] schedule_f_t_n;			//holds verified schedules only
	private int[][][] schedule_f_t_n_temp;		//may be used during calculation of a schedule
	protected int[][] allocated_f_t;
	protected int[][] prefix_allocated_f_t;
	
	
	public Scheduler(NetworkGenerator ng, FlowGenerator tg){

		boundFlowDeadlines(tg, ng);
		this.ng=ng;
		this.tg=tg;
		 
		schedule_f_t_n=getEmptySchedule();
		logger = new LogMatlabFormat();	
		if(tg!=null && ng!=null){
			this.tg.setFlowIndices();
			interfaceLimit = new InterfaceLimit(ng);
			allocated_f_t=new int[tg.getFlows().size()*2][ng.getTimeslots()];
			prefix_allocated_f_t=new int[tg.getFlows().size()*2][ng.getTimeslots()];
			cf=new CostFunction(ng, tg, logger);
		}
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
	public void setSchedule(int[][][] schedule_f_t_n){
		this.schedule_f_t_n = schedule_f_t_n;
		this.schedule_f_t_n_temp = schedule_f_t_n;
	}
	
	public CostFunction getCostFunction(){
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
	public void calculateInstance(String path, boolean recalc){
		if((!new File(getLogfileName(path)).exists())||recalc){
			tg.setFlowIndices();
			initTempSchedule();
			startTimer();
			calculateInstance_internal(path);	//result is stored to schedule_f_t_n_temp
			long duration = stopTimer();
		//		Runtime rt = Runtime.getRuntime();
		//		System.out.println("TotalMemory of JVM: "+(rt.totalMemory()-rt.freeMemory())/(1024*1024));
			schedule_f_t_n=schedule_f_t_n_temp;	//store to schedule_f_t_n if constraints hold
			
			logInstance(path, duration);

//			if(DEBUG) System.err.println(getType()+": \t"
//			//+ cf.costTotal(schedule_f_t_n)); //
//			+cf.getStat(schedule_f_t_n));

		}else{
//			System.out.println(getLogfileName(path)+" is already calculated. Load result from file for visualization or other postprocessing.");
			schedule_f_t_n = LogMatlabFormat.load3DFromLogfile("schedule_f_t_n", getLogfileName(path));
			schedule_f_t_n_temp=schedule_f_t_n;
//			System.out.println(showSchedule(schedule_f_t_n));
			
			long duration = LogMatlabFormat.loadValueFromLogfile("scheduling_duration_us", getLogfileName(path));

			cf.calculate(schedule_f_t_n);	//calculate cost function parts from schedule
			//redo log
//			if(duration>0)
//				logInstance(path, duration);
			

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
		
		logFlowDrop();
		
		//finish logging and write to file
		logger.writeLog(getLogfileName(path));
	}
	
	//per million
	private int getDropRate(){
		int sum_tokens = 0;
		int sum_scheduled=0;
		for(int f=0;f<tg.getFlows().size();f++){
			sum_tokens+=tg.getFlows().get(f).getTokens();
			for(int n=0;n<ng.getNetworks().size();n++){
				for(int t=0;t<ng.getTimeslots();t++){
					sum_scheduled+=schedule_f_t_n_temp[f][t][n];
				}
			}
		}
		if(sum_tokens==0)return 0;	//avoid division by zero
		double drop_rate=1000000.0*((double)1.0-((double)sum_scheduled/(double)sum_tokens));
//		System.out.println("DR: scheduled/tokens = dr \t"+sum_scheduled+" / "+sum_tokens+" = "+((double)1.0-((double)sum_scheduled/(double)sum_tokens)));
		return (int)drop_rate;
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
						network_used_by_type[networks.get(n).getType()-1]++; //Do not exceed available number of used Link Interfaces (2)
						network_used[n]=true;
					}
				}
				//check overuse of resources (1)
				if(network_use>networks.get(n).getCapacity().get(t)){
					if(DEBUG) System.err.println("Scheduler::constraintCheck - Net use error: "+network_use+ " > "+networks.get(n).getCapacity().get(t)+", network "+n+" time "+t);
					return false;	//violation if used capacity of network in this time slot is larger than available capacity in this time slot
				}
			}
			//check: Do not exceed available number of Link Interfaces (2)
			for (int i = 0; i < network_used_by_type.length; i++) {
				if(network_used_by_type[i]>ng.getNofInterfacesByType()[i]){ //check if sum is larger than available interfaces
					if(DEBUG) System.err.println("Scheduler::constraintCheck - Parallel link interface error : "+ network_used_by_type[i] +" > "+ng.getNofInterfacesByType()[i]);
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
							if(DEBUG) System.err.println("Scheduler::constraintCheck - Parallel channel use for same flow: "+f +" in time slot " + t);
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
			int chunksMax = flow.getTokensMax();
			for(int t=0;t<(ng.getTimeslots()-winSize); t++){
				int chunkSum=0;	//sum of chunks in this window
				int t_max = Math.min(t+winSize, ng.getTimeslots());
				for(int tw=t;tw<t_max;tw++){
					for(int n=0; n<ng.getNetworks().size();n++){
						chunkSum+=schedule_f_t_n[f][tw][n];
					}
				}
				if(chunkSum>chunksMax){
					if(DEBUG) System.err.println("Scheduler::constraintCheck - Upper throughput limit exceeded for flow: "+f +" in time window " + t+" to " + t_max +" by "+ (chunkSum-chunksMax));
					return false;	//violation of upper tp limit
				}
			}
		}
		
		//do not schedule more tokens than available
		for(int f=0;f<flows.size();f++){
			int sum = 0;
			for (int n=0; n<networks.size();n++){
				for(int t=0; t<ng.getTimeslots();t++){
					sum+=schedule_f_t_n[f][t][n];
				}
			}
			if(sum>flows.get(f).getTokens()){
				if(DEBUG) System.err.println("Scheduler::constraintCheck - More tokens scheduled than available for flow: "+f);
				return false;	//violation of flow tokens
				
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
	 * @param tokens to allocate
	 * @return number of allocated chunks
	 */
	protected int allocate(int flow, int time, int network, int tokens){
		if(!boundsValid(flow, time, network))return 0;
		if(!interfaceLimit.isUsable(network, time))return 0;
//		System.out.println("interface_limit_ok");
//		System.out.println("Scheduler::allocate. validity checks passed");
		//calculate remaining capacity of network in this slot
		int remaining_net_cap=getRemainingNetCap(network, time);
//		int s[][][]=schedule_f_t_n_temp.clone();
		int scheduled = Math.min(tokens, remaining_net_cap);
//		System.out.println("Scheduler::allocate. f,t,n "+flow+","+time+","+network+" tokens: "+tokens+
//				"; remaining cap = "+remaining_net_cap+"; scheduled = "+scheduled);

		if(scheduled>0){
			schedule_f_t_n_temp[flow][time][network]+=scheduled;
			//any constraint violated? use if ok, else revert
			if(verificationOfConstraints(schedule_f_t_n_temp)){
//				System.out.println("CORRECT PLAN: "+verificationOfConstraints(schedule_f_t_n_temp));
				interfaceLimit.useNetwork(network, time);
				int f_id= tg.getFlows().get(flow).getId();	
				allocated_f_t[f_id][time]+=scheduled;
				updatePrefixAllocated(Math.min(ng.getTimeslots()-1, ng.getTimeslots()), flow);
//				if(flow==0)System.out.println("prefix_alloc "+Arrays.deepToString(prefix_allocated_f_t));
				return scheduled;
			}else{
				schedule_f_t_n_temp[flow][time][network]-=scheduled;	//undo: remove tokens from plan if it gets invalid
				if(DEBUG) System.err.println("Scheduler::allocate. constraint check NOT passed");
				return 0;
			}
		}
		return 0;
		
	}
	
	/**
	 * 
	 * @param network ID
	 * @param time slot
	 * @return remaining capacity of network in this time slot in currently calculated schedule
	 */
	protected int getRemainingNetCap(int network,int time){
		if(!boundsValid(0,time,network))return 0;
		int remaining_net_cap =ng.getNetworks().get(network).getCapacity().get(time);
		if(remaining_net_cap<=0)return 0;
		//System.out.println("-------------remaining cap bounds are valid;; net / time "+network+" / "+time+"  cap ="+remaining_net_cap);
		for(int f = 0; f<tg.getFlows().size();f++){
			remaining_net_cap-=schedule_f_t_n_temp[f][time][network];
		}
//		System.out.println("-------------remaining cap bounds are valid;; net cap ="+remaining_net_cap);
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
			//print capacity
			s+="\ncap\t|";
			Vector<Integer> cap = ng.getNetworks().get(n).getCapacity();
			for(int t=0; t<ng.getTimeslots();t++){
				if(t%10==0){
					s+="["+t+"]";
				}
				s+=cap.get(t)+"\t|";
			}
				
			//print each flow which uses the network
			for(int f=0; f<tg.getFlows().size();f++){
				boolean used=false;
				//does the flow use the network?
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

	public void logFlowDrop(){

		logger.comment("Drop rate in parts per million. (=Percent with four digits right of comma)");
		logger.log("drop_rate", getDropRate());
		
		String s = "Flow Drop Rate:\n";
		for(int f =0; f<tg.getFlows().size(); f++){
			Flow flow = tg.getFlows().get(f);
			s+="Flow "+f+": "+getFlowDrop(f)+"%, type: "+flow.getFlowName()
					+", st "+flow.getStartTime()
					+", dl "+flow.getDeadline()
					+", UserImp "+flow.getImpUser()
					+", ImpUnsch "+flow.getImpUnsched()
					+", ImpLcy " +flow.getImpLatency()
					+", ImpJit "+flow.getImpJitter()
					+", ImpTp "+flow.getImpThroughputMin()
					+", Tokens "+flow.getTokens()
					+"\n";
					
		}
		logger.comment(s);
	}
	
	public int getFlowDrop(int f){
		Flow flow = tg.getFlows().get(f);
			int sched_flow_tokens=0;
			
			for(int n = 0; n<ng.getNetworks().size(); n++){
				for(int t = 0; t<ng.getTimeslots(); t++){
					sched_flow_tokens+=schedule_f_t_n[f][t][n];
				}
			}
			if(flow.getTokens()>0)
				return (100-100*sched_flow_tokens/flow.getTokens());
			else
				return 0;
	}
	
	protected void updatePrefixAllocated(int t_max, int f){
		int f_id = tg.getFlows().get(f).getId();

		prefix_allocated_f_t[f_id][0]=allocated_f_t[f_id][0];
		for(int t=1; t<=t_max; t++){
			prefix_allocated_f_t[f_id][t]=prefix_allocated_f_t[f_id][t-1]+allocated_f_t[f_id][t];
		}
	}
	
	public NetworkGenerator getNetworkGenerator(){
		return ng;
	}

	public FlowGenerator getFlowGenerator() {
		return tg;
	}
}
