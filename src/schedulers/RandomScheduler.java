package schedulers;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import schedulingIOModel.Flow;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.TrafficGenerator;


public class RandomScheduler extends Scheduler{
	
	
	/**
	 * Behavior:
	 * 
	 * 1. shuffle flows and networks
	 * 2. randomize time slot scheduling order
	 * 
	 * schedule_f_t_n
	 */

	public RandomScheduler(NetworkGenerator ng, TrafficGenerator tg) {
		super(ng, tg);
	}

	@Override
	protected int[][][] calculateInstance_internal(String logfile) {
		int[][][] schedule = getEmptySchedule();
		Vector<Flow> flows = tg.getFlows();
		Vector<Network> networks = ng.getNetworks();
		
//		1. shuffle flows
		Vector<Integer> flowOrder= new Vector<Integer>();
		for(int f=0; f<ng.getTimeslots(); f++){
			flowOrder.add(f);
		}
		
		for(int f=0; f<ng.getTimeslots(); f++){
			//select random flow
			int f0= flowOrder.get(f);
			Flow flow = flows.get(f0);
			Random rd = new Random();
			
			int flowSt= flow.getStartTime();
			int flowDl= flow.getDeadline();
			
			//select random network
			int n0= rd.nextInt(ng.getNetworks().size());
			ng.getNetworks().get(n0);
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		/*/create sorted vector with time slots which can be shuffeled later
		Vector<Integer> timeSlotOrder= new Vector<Integer>();
		Vector<Vector<Integer>> flowTimeSlotOrder = new Vector<Vector<Integer>>();
		for(int t=0; t<ng.getTimeslots(); t++){
			timeSlotOrder.add(t);
		}
		
//		2. randomize time slot scheduling order
		for(int f= 0; f<tg.getFlows().size(); f++){
			Collections.shuffle(timeSlotOrder);
			flowTimeSlotOrder.add((Vector<Integer>) timeSlotOrder.clone());
		}
				
		//############ START OF ALGORITHM #############
		for(int t=0; t<ng.getTimeslots(); t++){
			for(Flow flow: flows){
				
			}
		}	
		*/
		return schedule;
	}
	

}
