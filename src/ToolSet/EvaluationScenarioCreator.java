package ToolSet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import schedulers.AdaptationScheduler;
import schedulers.DummyScheduler;
import schedulers.ExecutionScheduler;
import schedulers.GreedyScheduler;
import schedulers.GreedyOnlineOpppertunisticScheduler;
import schedulers.GreedyOnlineScheduler;
import schedulers.OptimizationScheduler;
import schedulers.RandomScheduler;
import schedulers.Scheduler;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;


public class EvaluationScenarioCreator {
	
	private boolean LOG_OVERWRITE = false;				//overwrites last simulation including network generator and traffic generator	
	private boolean RECALC= false;						//recalculates results, loading network and traffic generators from previous run
	private final boolean TEST_COST_FUNCTION = false;	//tests cost function implemented in java, comparing all results to optimization output
	private boolean VISUALIZE = false;	//tests cost function implemented in java, comparing all results to optimization output
	private int PARALLEL=1;
	
	private final int REPETITIONS;
	private final int MAX_TIME;
	private final int MAX_FLOWS;
	private final int MAX_NETS;
	private final String LOG;
	private static String log_run_path="";
	
	private float NET_UNCERTAINTY=(float)0.0;
	private float MOVE_UNCERTAINTY=(float)0.0;
	private float FLOW_UNCERTAINTY=(float)0.0;
	
	
	private int DATA_AMOUNT=2;		//1 = low, 2= medium, 3= high
	private int MONETARY_WEIGHT=2;	//1 = low, 2= medium, 3= high
	
	
	List<Callable<Boolean>> taskList = new ArrayList<Callable<Boolean>>();
	
	public EvaluationScenarioCreator(int time, int nets, int apps, int repetitions, String logpath){
		REPETITIONS=repetitions;
		MAX_TIME=time;
		MAX_FLOWS=apps;
		MAX_NETS=nets;
		LOG=logpath+File.separator;		
		//evaluate();
	}


	public void addUncertainty(float move, float net, float flow){
		NET_UNCERTAINTY=net;
		MOVE_UNCERTAINTY=move;
		FLOW_UNCERTAINTY=flow;
	}

	/*
	 * reads out generated simulation scenarios and recalculates results
	 */
	public void recalc(){
		RECALC=true;
	}
	/*
	 * reads out generated simulation scenarios and recalculates results
	 */
	public void parallel(int parallel_workers){
		PARALLEL=parallel_workers;
	}
	/*
	 * overwrites generated simulation scenarios and results
	 */
	public void overwrite(){
		LOG_OVERWRITE=true;
	}
	
	public void visualize(){
		VISUALIZE=true;
	}
	
	public void start(int timeout_hours){
		ExecutorService executor = Executors.newFixedThreadPool(Math.max(1, PARALLEL));
		try {
			executor.invokeAll(taskList,timeout_hours, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		executor.shutdown();
	}
	

	/**
	 * get list of all schedulers which shall calculate a schedule
	 * @param ng
	 * @param fg
	 * @return list of schedulers
	 */
	public Vector<Scheduler> initSchedulers(NetworkGenerator ng, FlowGenerator fg){

		Vector<Scheduler> schedulers = new Vector<Scheduler>();	


		boolean newRating = false;
//		schedulers.add(new OptimizationScheduler(ng, fg));
//		
//		schedulers.add(new GreedyScheduler(ng, fg).newRating(newRating));		
//		schedulers.add(new GreedyOnlineOpppertunisticScheduler(ng, fg).newRating(newRating));
//
		newRating=true;

//		schedulers.add(new GreedyOnlineScheduler(ng, fg).newRating(newRating));
		schedulers.add(new GreedyOnlineOpppertunisticScheduler(ng, fg).newRating(newRating));
		Scheduler gs = new GreedyScheduler(ng, fg).newRating(newRating);
		schedulers.add(gs);				

		//execution of schedule from erroneous prediction
		String path = gs.getLogfileName(log_run_path);
//		schedulers.add(new ExecutionScheduler(ng, fg, path));
		
		//Adaptation of schedule under prediction errors
		FlowGenerator fgPred = getFlowGenerator(log_run_path, false, 0, 0, 0);
		schedulers.add(new AdaptationScheduler(ng, fg, fgPred, path));
		
//		schedulers.add(((GreedyScheduler) new GreedyScheduler(ng, fg).setScheduleDecisionLimit(5)).newRating(newRating));
//		schedulers.add(((GreedyScheduler) new GreedyScheduler(ng, fg).setScheduleDecisionLimit(8)).newRating(newRating));
//		schedulers.add(((GreedyScheduler) new GreedyScheduler(ng, fg).setScheduleDecisionLimit(10)).newRating(newRating));
//		schedulers.add(((GreedyScheduler) new GreedyScheduler(ng, fg).setScheduleDecisionLimit(15)).newRating(newRating));
//		schedulers.add(((GreedyScheduler) new GreedyScheduler(ng, fg).setScheduleDecisionLimit(20)).newRating(newRating));
		
//		schedulers.add(new RandomScheduler(ng, fg, 100));	//100 random runs of this scheduler. Returns average duration and cost
		
	return schedulers;
	}

	
	private void writeScenarioLog(int evaluateMax){
		
		//write scenario parameters log
		LogMatlabFormat logger = new LogMatlabFormat();
		logger.comment(LOG);
		logger.log("max_time", MAX_TIME);
		logger.log("max_flows", MAX_FLOWS);
		logger.log("max_nets", MAX_NETS);
		logger.log("max_rep", REPETITIONS);
		logger.log("load", DATA_AMOUNT);
		logger.log("w_monetary", MONETARY_WEIGHT);
		logger.log("move_uncertainty", MOVE_UNCERTAINTY);
		logger.log("net_uncertainty", NET_UNCERTAINTY);
		logger.log("flow_uncertainty", FLOW_UNCERTAINTY);
		
		//logger.log("evaluate_max_only", evaluateMax);			//is 1 if simulation did not ran from 0 to max, but only max
		logger.logSchedulers(initSchedulers(null, null));	//save which schedulers are available for evaluation
		
		logger.writeLog(LOG+"parameters_log.m");
	}
	
	
	public void evaluateAll(){
		//paramter log
		writeScenarioLog(0);
		
		//data sizes of..
		//(i) time
		for(int t=0;t<=MAX_TIME;t++){
			//(ii) number of networks
			for(int net=0;net<=MAX_NETS;net++){	
				//(iii) number of flows/requests
				for(int req=0;req<=MAX_FLOWS;req++){
					//repetitions of optimization
					for(int rep=0; rep<REPETITIONS;rep++){
						evaluateUncertainty(t, net, req, rep);
					}
					
				}
			}
		}
		System.out.println("###############  TASK CREATION DONE  ##################");
	}
	
	public void evaluateThisInstance(){
		//paramter log
//		DATA_AMOUNT=3;

		evaluateUncertainty(MAX_TIME, MAX_NETS, MAX_FLOWS, REPETITIONS-1);
		
		System.out.println("###############  TASK CREATION DONE  ##################");
	}
	
	public void evaluateTop(){
		//paramter log
		writeScenarioLog(1);

		for(int rep=0; rep<REPETITIONS;rep++){
//		int rep=2;
			evaluateUncertainty(MAX_TIME, MAX_NETS, MAX_FLOWS, rep);
		}
		
		System.out.println("###############  TASK CREATION DONE  ##################");
	}
	
	public void evaluateTimeVariation(){
		//paramter log
		writeScenarioLog(1);
		for(int t= 0; t<=MAX_TIME; t++){
			for(int rep=0; rep<REPETITIONS;rep++){
				evaluateUncertainty(t,  MAX_NETS, MAX_FLOWS, rep);
			}
		}
	System.out.println("###############  TASK CREATION DONE  ##################");
	}
	
	public void evaluateFlowVariation(){
		//paramter log
		writeScenarioLog(1);
		for(int f= 2; f<=MAX_FLOWS; f++){		//start with 2 = 2² = 4 flows to be able to use "add4" for traffic share
			for(int rep=0; rep<REPETITIONS;rep++){
				evaluateUncertainty(MAX_TIME,  MAX_NETS, f, rep);
			}
		}
	System.out.println("###############  TASK CREATION DONE  ##################");
	}
	
	public void evaluateNetworkVariation(){
		//paramter log
		writeScenarioLog(1);
		for(int n= 0; n<=MAX_NETS; n++){
			for(int rep=0; rep<REPETITIONS;rep++){
				evaluateUncertainty(MAX_TIME, n, MAX_FLOWS, rep);
			}
		}
	System.out.println("###############  TASK CREATION DONE  ##################");
	}
	
	public void evaluateTrafficLoad(){
		//paramter log
		DATA_AMOUNT=3;	//set max for log
		writeScenarioLog(1);
		for(int d= 1; d<=3; d++){
			DATA_AMOUNT=d;
			for(int rep=0; rep<REPETITIONS;rep++){
				evaluateUncertainty(MAX_TIME, MAX_NETS, MAX_FLOWS, rep);
			}
		}
	System.out.println("###############  TASK CREATION DONE  ##################");
	}
	
	public void evaluateMonetaryWeight(){
		//paramter log
		writeScenarioLog(1);
		for(int m= 0; m<=3; m++){
			MONETARY_WEIGHT=m;
			for(int rep=0; rep<REPETITIONS;rep++){
				evaluateUncertainty(MAX_TIME, MAX_NETS, MAX_FLOWS, rep);
			}
		}
	System.out.println("###############  TASK CREATION DONE  ##################");
	}

	public void evaluateUncertainty(int t, int n, int f, int rep){
		calculateInstance_t_n_i(t, n, f, rep, LOG, LOG_OVERWRITE, RECALC, 0, 0, 0);		//no uncertainty
	
		for(float move_unc=(float)0.1; move_unc<=MOVE_UNCERTAINTY; move_unc+=(float)0.1){
			calculateInstance_t_n_i(t, n, f, rep, LOG, LOG_OVERWRITE, RECALC, 0, move_unc, 0);
		}
		for(float net_unc=(float)0.1; net_unc<=NET_UNCERTAINTY; net_unc+=(float)0.1){
			calculateInstance_t_n_i(t, n, f, rep, LOG, LOG_OVERWRITE, RECALC, net_unc, 0, 0);
		}
		for(float flow_unc=(float)0.1; flow_unc<=FLOW_UNCERTAINTY; flow_unc+=(float)0.1){
			calculateInstance_t_n_i(t, n, f, rep, LOG, LOG_OVERWRITE, RECALC, 0, 0, flow_unc);	
		}
		if(FLOW_UNCERTAINTY>0 && MOVE_UNCERTAINTY>0 && FLOW_UNCERTAINTY>0){
			for(float unc=(float)0.1; unc<=FLOW_UNCERTAINTY; unc+=(float)0.1){
				calculateInstance_t_n_i(t, n, f, rep, LOG, LOG_OVERWRITE, RECALC, unc, unc, unc);
			}
		}
	}

	/**
	 * calculates maximum values for time slots, networks, and flows from indexes
	 * @param t
	 * @param n
	 * @param f
	 * @param rep
	 * @param folder
	 * @param overwrite
	 * @param recalc
	 */
	
	
	public void calculateInstance_t_n_i(int t, int n, int f, int rep, String folder, boolean overwrite, 
			boolean recalc, float netUncertainty, float movementUncertainty, float flowUncertainty){
		int time = 25*pow(2,t);
		int nets = pow(2,n);
		int flows = pow(2,f);
		String folder_out = folder+f+"_"+t+"_"+n+"_"+DATA_AMOUNT+"_"+MONETARY_WEIGHT+File.separator+rep+File.separator;
		log_run_path = folder_out;
//		calculateInstance(time, nets, flows, rep, folder_out, overwrite, recalc, decomposition_heuristic);

		NetworkGenerator ng = getNetworkGenerator(folder_out, overwrite, nets, time, netUncertainty, movementUncertainty);	//do not change order of ng and fg! There's a bad dependence for optimization
		ng.setCostImportance(Math.max(0,(MONETARY_WEIGHT-1)*10+5));	//0, 5, 15, 25
		FlowGenerator fg = getFlowGenerator(folder_out, flowUncertainty>0 && overwrite, flows, time, flowUncertainty);
		
		if(netUncertainty>0 && movementUncertainty>0 && flowUncertainty>0){
			folder_out+="comb_"+movementUncertainty+File.separator;
		}else if(netUncertainty>0){
			folder_out+="net_"+netUncertainty+File.separator;
		}else if(movementUncertainty>0){
			folder_out+="move_"+movementUncertainty+File.separator;
		}else if(flowUncertainty>0){
			folder_out+="flow_"+flowUncertainty+File.separator;
		}
		
		Vector<Scheduler> schedulers = initSchedulers(ng, fg);
		
		EvaluationScenarioExecutionWorker worker = new 
				EvaluationScenarioExecutionWorker(ng, fg, TEST_COST_FUNCTION, VISUALIZE, folder_out, overwrite||recalc, schedulers);
		taskList.add(worker);
		
	}
	
		
	public NetworkGenerator getNetworkGenerator(String folder, boolean overwrite, int nets, int time, 
			float netUncertainty, float movementUncertainty){
		String path=folder;
		if(movementUncertainty>0 && netUncertainty>0){
			path+="comb_"+movementUncertainty+File.separator;
		}else if(movementUncertainty>0){
			path+="move_"+movementUncertainty+File.separator;
		}else if(netUncertainty>0){
			path+="net_"+netUncertainty+File.separator;
		}

//		System.out.println(path);
		new File(path).mkdirs();

		//try to load ng configurations from file
		NetworkGenerator ng= NetworkGenerator.loadNetworkGenerator(path);

		//if file does not exist, create scenario (or overwrite if desired)
		if(ng == null || overwrite){	//overwrite ng even if existing
//				System.out.println("Creating Networks and Flows..");
			if(netUncertainty>0 && movementUncertainty>0){
				//if both are different from zero, add movement uncertainty first. Load from movement uncertainty.
				ng=getNetworkGenerator(folder, false, nets, time, 0, movementUncertainty);
				ng.addNetworkUncertainty(netUncertainty);
			}else if(movementUncertainty>0){
				//if one of the two is equal zero, then load NG from file where both are zero and modify
				ng=getNetworkGenerator(folder, false, nets, time, 0, 0);
				ng.addMovementUncertainty(movementUncertainty);
			}else if(netUncertainty>0){
				//if one of the two is equal zero, then load NG from file where both are zero and modify
				ng=getNetworkGenerator(folder, false, nets, time, 0, 0);
				ng.addNetworkUncertainty(netUncertainty);
			}else{
				ng=new NetworkGenerator(nets, time);
			}
			
			ng.writeObject(path);
		}

		return ng;		
	}
	
	public FlowGenerator getFlowGenerator(String folder, boolean overwrite, int flows, int time,
			float flowUncertainty){
		String path=folder;
		if(flowUncertainty>0){
			path+="flow_"+flowUncertainty+File.separator;
		}
		
		new File(path).mkdirs();

		//try to load ng configurations from file
		FlowGenerator fg= FlowGenerator.loadTrafficGenerator(path);

		//if file does not exist, create scenario (or overwrite if desired)
		if(fg == null || overwrite){	//overwrite ng even if existing
//				System.out.println("Creating Networks and Flows..");
			if(flowUncertainty>0){
				fg=getFlowGenerator(folder, false, flows, time, 0);	//get initial flowGenerator without uncertainty
				//add uncertainty
				fg.addUncertainty(flowUncertainty, time);
			}else{
				fg=new FlowGenerator(time, flows, DATA_AMOUNT);
			}

//			System.out.println("write FlowGen to: "+path);	
			fg.writeObject(path);
		}
		
		return fg;
	}
	
	
	private int pow(int v, int exp){
		return (int)Math.round(Math.pow(v, exp));
	}
	
}
