package schedulers;
import java.util.Collections;
import java.util.Vector;

import ToolSet.RndInt;
import schedulingIOModel.CostFunction;
import schedulingIOModel.Flow;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.FlowGenerator;


public class RandomScheduler extends Scheduler{
	

	@Override
	public String getType() {
		return "Random";
	}
	
	/**
	 * 
	 * Selects a network randomly and keeps it as long as any data can be scheduled to it
	 * 
	 * Behavior:
	 * 
	 * 1. Take random flow and 
	 * 		1.1 calculate average maximum throughput for flow = chunks to schedule in each time slot
	 * 		1.2 Start allocating at flow start time; till flow deadline or all chunks allocated
	 * 			- select random network
	 * 			- if bucket available for time slot: allocate max(bucket size, flow_TP_max)
	 * 			- go to next time step; 
	 * 		
	 * 
	 * schedule_f_t_n
	 */

	public RandomScheduler(NetworkGenerator ng, FlowGenerator tg, int runs) {
		super(ng, tg);
		RUNS = runs;
		per_run_cost = new int[RUNS];
		per_run_duration = new int[RUNS];
	}
	
	private final int RUNS;
	private int c;	//final averaged cost

	private int run_counter = 0;
	private long sum_duration=0;
	private long sum_cost=0;
	
	private int[] per_run_cost;
	private int[] per_run_duration;
	
	/**
	 * runs instance multiple (RUNS) times
	 */
	@Override
	public void calculateInstance(String path, boolean recalc){
		for(run_counter=RUNS-1; run_counter>= 0; run_counter--){
			super.calculateInstance(path, recalc);
			//System.out.println("calc_run "+run_counter);
		}
	}
	
	/**
	 * log only once and calculate mean values for execution duration and schedule quality
	 */
	@Override
	protected void logInstance(String path, long duration) {
		sum_duration+=duration;
		int cost = new CostFunction(ng, tg).costTotal(getSchedule());
		sum_cost+=(long)cost;
//				System.out.println(cost);
		per_run_cost[run_counter]=cost;
		per_run_duration[run_counter]=(int)(duration/1000);
		
		//write log if all runs finished
		if(run_counter<=0){
			
		//write log file for schedule including solve duration, schedule, cost function 
		logger.comment(getLogfileName(path));
		logger.log("scheduling_duration_us", (int)(sum_duration/(RUNS*1000)));

		logger.comment("schedule");
		logger.log("schedule_f_t_n", getSchedule());
		
		//add cost function results to log file
		logger.comment("cost function results");
		logger.log("costTotal", (int)(sum_cost/RUNS));
		c=(int) (sum_cost/RUNS);
		
		//runs
		logger.comment("per run statistics");
		logger.log("per_run_cost", per_run_cost);
		logger.log("per_run_duration_us", per_run_duration);
		
		//finish logging and write to file
		logger.writeLog(getLogfileName(path));
		}
		
	}
	
	@Override
	protected void calculateInstance_internal(String logfile) {
//		1. shuffle flows
		Vector<Integer> flowOrder= new Vector<Integer>();
		for(int f=0; f<tg.getFlows().size(); f++){
			flowOrder.add(f);
		}
		Collections.shuffle(flowOrder);
		
//		2. shuffle networks
		Vector<Integer> netOrder= new Vector<Integer>();
		for(int n=0; n<ng.getNetworks().size(); n++){
			netOrder.add(n);
		}	//shuffeling is done later
		
		//for each flow
		for(int f=0; f<tg.getFlows().size(); f++){
			//select next of randomized flows
			int f0= flowOrder.get(f);
			Flow flow = tg.getFlows().get(f0);
			//get flow parameters
			int flowSt= flow.getStartTime();
			int flowDl= flow.getDeadline();
			int flowTpMax = (int) Math.floor(flow.getTokensMax()/flow.getWindowMax());
			
			//allocate chunks
			int nonAlloChunks = flow.getTokens();

			for(int t=flowSt; t<flowDl && nonAlloChunks>0; t++){	//stop if all chunks allocated or deadline reached
				//shuffle networks for each flow individually
				Collections.shuffle(netOrder);
				int n=0;	
				int steps=0;	//number of networks for which current allocation failed
				int allocated=0;	//is zero if previous allocation not successful (e.g. no network resources free)
				//next step if allocation possible or after nof_net^2 steps 
				while(allocated<1 && steps<(ng.getNetworks().size())){	//stop next allocation if all networks have been tried
						
					int n0=netOrder.get(n);		//get network id from randomized vector
					//allocate chunks
					if(flowTpMax>nonAlloChunks){
						allocated=allocate(f0, t, n0, nonAlloChunks);		//do not allocate more chunks than required
					}else{
						allocated=allocate(f0, t, n0, flowTpMax);	//allocate as much as possible
					}
					nonAlloChunks-=allocated;	//remove chunks from allocated

					
					if(allocated<1){	//if allocation failed, select new network for next step
						n=(n+1)%ng.getNetworks().size();	//select next of randomized networks
						steps++;									//count number of networks for which allocation failed
					}else{
						if(RndInt.get(0, 4)==0){	//20 percent chance of shuffeling again
							Collections.shuffle(netOrder);
						}
						steps=0; //tokens scheduled: keep network with 80% probability, reset counter
							
					}
					
				}
			}
			
		}
	}

	@Override
	public int getCost(){
		return c;
	}
	

}
