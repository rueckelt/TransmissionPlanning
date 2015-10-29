package toolSet;
import java.io.File;
import java.util.Vector;

import javax.swing.text.SimpleAttributeSet;

import io.SimulationInputGenerator;
import schedulers.OptimizationScheduler;
import schedulers.PriorityScheduler;
import schedulers.RandomScheduler;
import schedulers.Scheduler;
import schedulingModel.FlowGenerator;
import schedulingModel.NetworkGenerator;


public class EvaluationScenarioCreator {
	
	private boolean LOG_OVERWRITE = true;				//overwrites last simulation including network generator and traffic generator	
	private boolean RECALC= false;						//recalculates results, loading network and traffic generators from previous run
	private final boolean TEST_COST_FUNCTION = false;	//tests cost function implemented in java, comparing all results to optimization output

	
	private final int REPETITIONS;
	private final int MAX_TIME;
	private final int MAX_FLOWS;
	private final int MAX_NETS;
	private final String LOG;
	
	public EvaluationScenarioCreator(int time, int nets, int apps, int repetitions, String logpath){
		REPETITIONS=repetitions;
		MAX_TIME=time;
		MAX_FLOWS=apps;
		MAX_NETS=nets;
		LOG=logpath+File.separator;
		
		//evaluate();
	}

	/**
	 * get list of all schedulers which shall calculate a schedule
	 * @param ng
	 * @param tg
	 * @return list of schedulers
	 */
	private Vector<Scheduler> initSchedulers(NetworkGenerator ng, FlowGenerator tg){
		Vector<Scheduler> schedulers = new Vector<Scheduler>();
		//schedulers.add(new RandomScheduler(ng, tg, 500));	//500 random runs of this scheduler. Returns average duration and cost
		//schedulers.add(new PriorityScheduler(ng, tg));
		schedulers.add(new OptimizationScheduler(ng, tg));
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
						calculateInstance_t_n_f(t, net, req, rep, LOG, LOG_OVERWRITE, RECALC);
					}
					
				}
			}
		}
	
	System.out.println("###############   DONE   ###############");
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
	public void calculateInstance_t_n_f(int t, int n, int f, int rep, String folder, boolean overwrite, boolean recalc){
		int time = 25*pow(2,t);
		int nets = pow(2,n);
		int flows = pow(2,f);
		String folder_out = folder+f+"_"+t+"_"+n+File.separator;
		calculateInstance(time, nets, flows, rep, folder_out, overwrite, recalc);
		
	}
	
	public void calculateInstance(int time, int nets, int flows, int rep, String folder, boolean overwrite, boolean recalc) {
		NetworkGenerator networkGenerator;
		FlowGenerator flowGenerator;
		
		String path=folder+"rep_"+ rep+File.separator;
		//skip if folder exists
		if(!new File(path).exists() || overwrite || recalc){
			new File(path).mkdirs();
			System.out.println(recalc);
			if(!recalc){
				System.out.println("Creating Networks and Flows..");
				networkGenerator = new NetworkGenerator(nets, time);		//add network input data
				networkGenerator.writeObject(path);
				flowGenerator = new FlowGenerator(time, flows);		//add application traffic input data
				flowGenerator.writeObject(path); 
			}else{
				System.out.println("Loading stored Networks and Flows..");
				System.out.println(path);
				networkGenerator = NetworkGenerator.loadNetworkGenerator(path);
				flowGenerator = FlowGenerator.loadTrafficGenerator(path);
			}
			

			if(TEST_COST_FUNCTION){
//				run optimization and compare results to results of the cost function
				OptimizationScheduler sched = new OptimizationScheduler(networkGenerator, flowGenerator);
				sched.testCostFunction(networkGenerator, flowGenerator);
				sched.calculateInstance(path);
			}else{
				//default case:
				//run instance for each scheduler which is created in method "initSchedulers(ng,tg)" above
				for(Scheduler scheduler: initSchedulers(networkGenerator, flowGenerator)){
					scheduler.calculateInstance(path);
				}
			}
			
		}
	}

	private int pow(int v, int exp){
		return (int)Math.round(Math.pow(v, exp));
	}
	
}
