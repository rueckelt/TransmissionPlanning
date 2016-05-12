package ToolSet;
import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import schedulers.GreedyOnlineOpppertunisticScheduler;
import schedulers.GreedyOnlineScheduler;
import schedulers.GreedyScheduler;
import schedulers.OptimizationScheduler;
import schedulers.PriorityScheduler;
import schedulers.RandomScheduler;
import schedulers.Scheduler;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;
import visualization.Plot;
import visualization.VisualizationPack;


public class EvaluationScenarioCreator {
	
	private boolean LOG_OVERWRITE = false;				//overwrites last simulation including network generator and traffic generator	
	private boolean RECALC= false;						//recalculates results, loading network and traffic generators from previous run
	private final boolean TEST_COST_FUNCTION = false;	//tests cost function implemented in java, comparing all results to optimization output
	private boolean VISUALIZE = false;	//tests cost function implemented in java, comparing all results to optimization output
	private int PARALLEL=1;
	
	List<Callable<Boolean>> taskList = new ArrayList<Callable<Boolean>>();
	
	public EvaluationScenarioCreator(int time, int nets, int apps, int repetitions, String logpath){
		REPETITIONS=repetitions;
		MAX_TIME=time;
		MAX_FLOWS=apps;
		MAX_NETS=nets;
		LOG=logpath+File.separator;
		
		//evaluate();
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
	
	public void start(){
		ExecutorService executor = Executors.newFixedThreadPool(PARALLEL);
		try {
			executor.invokeAll(taskList);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		executor.shutdown();
	}
	
	
	private final int REPETITIONS;
	private final int MAX_TIME;
	private final int MAX_FLOWS;
	private final int MAX_NETS;
	private final String LOG;

	/**
	 * get list of all schedulers which shall calculate a schedule
	 * @param ng
	 * @param tg
	 * @return list of schedulers
	 */
	public static Vector<Scheduler> initSchedulers(NetworkGenerator ng, FlowGenerator tg){
		Vector<Scheduler> schedulers = new Vector<Scheduler>();
//		schedulers.add(new OptimizationScheduler(ng, tg));	
//		schedulers.add(new PriorityScheduler(ng, tg));
//		schedulers.add(new GreedyScheduler(ng, tg));
		schedulers.add(new GreedyOnlineOpppertunisticScheduler(ng, tg));
		schedulers.add(new GreedyOnlineScheduler(ng, tg));
//		schedulers.add(new RandomScheduler(ng, tg, 200));	//200 random runs of this scheduler. Returns average duration and cost
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
		logger.log("evaluate_max_only", evaluateMax);			//is 1 if simulation did not ran from 0 to max, but only max
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
						calculateInstance_t_n_i(t, net, req, rep, LOG, LOG_OVERWRITE, RECALC, false);	//false=decomposition heuristic TODO
					}
					
				}
			}
		}

		System.out.println("###############  TASK CREATION DONE  ##################");
	}
	
	public void evaluateTop(){
		//paramter log
		writeScenarioLog(1);

		//for(int rep=0; rep<REPETITIONS;rep++){
			//calculateInstance_t_n_i(MAX_TIME, MAX_NETS, MAX_FLOWS, rep, LOG, LOG_OVERWRITE, RECALC, false);	//false=decomposition heuristic TODO
		//}
		calculateInstance_t_n_i(MAX_TIME, MAX_NETS, MAX_FLOWS, REPETITIONS, LOG, LOG_OVERWRITE, RECALC, false);	//false=decomposition heuristic TODO

		System.out.println("###############  TASK CREATION DONE  ##################");
	}
	
	public void evaluateTimeVariation(){
		//paramter log
		writeScenarioLog(1);
		for(int t= 0; t<=MAX_TIME; t++){
			for(int rep=0; rep<REPETITIONS;rep++){
				calculateInstance_t_n_i(t, MAX_NETS, MAX_FLOWS, rep, LOG, LOG_OVERWRITE, RECALC, false);	//false=decomposition heuristic TODO
			}
		}
		
	
	System.out.println("###############  TASK CREATION DONE  ##################");
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
	
	
	public void calculateInstance_t_n_i(int t, int n, int f, int rep, String folder, boolean overwrite, boolean recalc, boolean decomposition_heuristic){
		int time = 25*pow(2,t);
		int nets = pow(2,n);
		int flows = pow(2,f);
		String folder_out = folder+f+"_"+t+"_"+n+File.separator;
//		calculateInstance(time, nets, flows, rep, folder_out, overwrite, recalc, decomposition_heuristic);
		
		EvaluationScenarioExecutionWorker worker = new 
				EvaluationScenarioExecutionWorker(TEST_COST_FUNCTION, VISUALIZE, time, nets, flows, rep, folder_out, overwrite, recalc);
		taskList.add(worker);
		
	}
	
	
	private int pow(int v, int exp){
		return (int)Math.round(Math.pow(v, exp));
	}
	
}
