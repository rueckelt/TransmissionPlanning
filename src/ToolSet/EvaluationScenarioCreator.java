package ToolSet;
import java.io.File;
import java.util.Vector;

import schedulers.OptimizationScheduler;
import schedulers.PriorityScheduler;
import schedulers.RandomScheduler;
import schedulers.Scheduler;
import schedulingIOModel.CostFunction;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.TestCostFunction;
import schedulingIOModel.TrafficGenerator;


public class EvaluationScenarioCreator {
	
	private boolean LOG_OVERWRITE = false;				//overwrites last simulation including network generator and traffic generator	
	private boolean RECALC= true;						//recalculates results, loading network and traffic generators from previous run
	private final boolean TEST_COST_FUNCTION = false;	//tests cost function implemented in java, comparing all results to optimization output

	
	public EvaluationScenarioCreator(int time, int nets, int apps, int repetitions, String logpath){
		REPETITIONS=repetitions;
		MAX_TIME=time;
		MAX_FLOWS=apps;
		MAX_NETS=nets;
		LOG=logpath+File.separator;
		
		//evaluate();
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
	private Vector<Scheduler> initSchedulers(NetworkGenerator ng, TrafficGenerator tg){
		Vector<Scheduler> schedulers = new Vector<Scheduler>();
		//schedulers.add(new OptimizationScheduler(ng, tg));
		schedulers.add(new RandomScheduler(ng, tg, 500));	//500 random runs of this scheduler. Returns average duration and cost
		schedulers.add(new PriorityScheduler(ng, tg));
		return schedulers;
	}
	
	private void writeScenarioLog(){
		
		//write scenario parameters log
		LogMatlabFormat logger = new LogMatlabFormat();
		logger.comment(LOG);
		logger.log("max_time", MAX_TIME);
		logger.log("max_flows", MAX_FLOWS);
		logger.log("max_nets", MAX_NETS);
		logger.log("max_rep", REPETITIONS);
		logger.logSchedulers(initSchedulers(null, null));	//save which schedulers are available for evaluation
		
		logger.writeLog(LOG+"parameters_log.m");
	}
	
	
	public void evaluateAll(){
		//paramter log
		writeScenarioLog();
		
		//data sizes of..
		//(i) time
		for(int t=0;t<MAX_TIME;t++){
			//(ii) number of networks
			for(int net=0;net<MAX_NETS;net++){	
				//(iii) number of flows/requests
				for(int req=0;req<MAX_FLOWS;req++){
					//repetitions of optimization
					for(int rep=0; rep<REPETITIONS;rep++){
						calculateInstance_t_n_i(t, net, req, rep, LOG, LOG_OVERWRITE, RECALC);
					}
					
				}
			}
		}
	
	System.out.println("###############   DONE  ##################");
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
	public void calculateInstance_t_n_i(int t, int n, int f, int rep, String folder, boolean overwrite, boolean recalc){
		int time = 25*pow(2,t);
		int nets = pow(2,n);
		int flows = pow(2,f);
		String folder_out = folder+f+"_"+t+"_"+n+File.separator;
		calculateInstance(time, nets, flows, rep, folder_out, overwrite, recalc);
		
	}
	
	
	public void calculateInstance(int time, int nets, int flows, int rep, String folder, boolean overwrite, boolean recalc) {
		NetworkGenerator ng;
		TrafficGenerator tg;
		
		String path=folder+"rep_"+ rep+File.separator;
		//skip if folder exists
		if(!new File(path).exists() || overwrite || recalc){
			new File(path).mkdirs();
			System.out.println(recalc);
			if(!recalc){
				System.out.println("Creating Networks and Flows..");
				ng=new NetworkGenerator(nets, time);	//add network input data
				ng.writeObject(path);
				tg = new TrafficGenerator(time, flows);		//add application traffic input data
				tg.writeObject(path); 
			}else{
				System.out.println("Loading stored Networks and Flows..");
				System.out.println(path);
				ng=NetworkGenerator.loadNetworkGenerator(path);
				tg=TrafficGenerator.loadTrafficGenerator(path);
			}
			

			if(TEST_COST_FUNCTION){
//				run optimization and compare results to results of the cost function
				OptimizationScheduler sched = new OptimizationScheduler(ng, tg);
				sched.testCostFunction(ng, tg);
				sched.calculateInstance(path);
			}else{
				//default case:
				//run instance for each scheduler which is created in method "initSchedulers(ng,tg)" above
				for(Scheduler scheduler: initSchedulers(ng, tg)){
					scheduler.calculateInstance(path);
				}
			}
			
		}
	}

	private int pow(int v, int exp){
		return (int)Math.round(Math.pow(v, exp));
	}
	
}
