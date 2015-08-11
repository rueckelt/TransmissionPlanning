import java.util.Arrays;
import java.util.Vector;


public abstract class Scheduler {
	
	/**
	 * TODO: Different strategies
	 * 
	 */
	
	protected NetworkGenerator ng; 
	protected TrafficGenerator tg;
	
	private CostFunction cf;
	private long runtime =0;
	private int[][][] schedule_f_t_n;
	
	public Scheduler(NetworkGenerator ng, TrafficGenerator tg){
		this.ng=ng;
		this.tg=tg;
		this.cf=new CostFunction(ng, tg);
		schedule_f_t_n=getEmptySchedule();
	}
	

	/**
	 * 
	 * @param ng
	 * @param tg
	 * @param logfile path
	 * 
	 * calculates the schedule and stores it in internal variable schedule_f_t_n
	 */
	protected abstract int[][][] calculateInstance_internal(String logfile);
	
	protected int[][][] getEmptySchedule(){
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
	public void calculateInstance(String logfile){
		int[][][] schedule_temp;
		startTimer();
		schedule_temp = calculateInstance_internal(logfile);
		stopTimer();
		
		if(verificationOfConstraints(schedule_temp)){
			schedule_f_t_n=schedule_temp;
		}else{
			System.err.println("Scheduler: calculated schedule violates constraints: "+logfile);
		}
//TODO		log();
	}
	
	/**
	 * 
		1. Do not overuse resources
		2. Do not exceed available number of Link Interfaces
		3. Do not allow parallel channel use for same flow
		
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
					System.err.println("Net use error: "+network_use+ " > "+networks.get(n).getCapacity().get(t));
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
		
		return true;
	}
	
	private void startTimer(){
		runtime=System.nanoTime();
	}
	
	private void stopTimer(){
		runtime=System.nanoTime()-runtime;
	}
	
}
