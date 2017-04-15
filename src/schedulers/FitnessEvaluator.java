package schedulers;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;

public class FitnessEvaluator extends HeuristicScheduler {
	
	HeuristicScheduler s;

	public FitnessEvaluator(HeuristicScheduler s) {
		super(s.getNetworkGenerator(), s.getFlowGenerator());
		this.s =s;
	}

	public FitnessEvaluator(NetworkGenerator ng, FlowGenerator tg,
			NetworkGenerator ngPred, FlowGenerator tgPred,
			String longTermSchedule_logfile) {
		super(ng, tg, ngPred, tgPred, longTermSchedule_logfile);
		// TODO Auto-generated constructor stub
	}
	
	public int getFitness(Map<Flow,Network> assignments, int t){
		cs=s.getCostSeparation().clone();
		setTempSchedule(deepCopy(s.getTempSchedule()));
		allocated_f_t=deepCopy(s.allocated_f_t);
		prefix_allocated_f_t=deepCopy(s.prefix_allocated_f_t);
		remaining_tokens_f = Arrays.copyOf(s.remaining_tokens_f, s.remaining_tokens_f.length);
		
		//change hysteresis for decision. does not have to pay back in one time step, but in 5.
		int hys_bu = ng.getHysteresis();	
		ng.setHysteresis(hys_bu/5);
		
		List<Integer> flowOrder =sortByFlowCriticality();
		
		for(int f_index: flowOrder){
			Flow flow = tg.getFlows().get(f_index);
			if(assignments.get(flow)!=null){	//if flow active and a network assigned
				if(oppScheduleDecision(flow.getIndex(), assignments.get(flow).getId(), t)){		//apply adaptation heuristic
					allocate(f_index, t, assignments.get(flow).getId(), flow.getTokensMax());
				}
			}
		}
		//set hysteresis back to normal
		ng.setHysteresis(hys_bu);
		return cf.costTotal(getTempSchedule());
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	
	private int[][] deepCopy(int[][] original) {
	    if (original == null) {
	        return null;
	    }

	    final int[][] result = new int[original.length][];
	    for (int i = 0; i < original.length; i++) {
	        result[i] = Arrays.copyOf(original[i], original[i].length);
	    }
	    return result;
	}
	private int[][][] deepCopy(int[][][] original) {
	    if (original == null) {
	        return null;
	    }

	    final int[][][] result = new int[original.length][original[0].length][];
	    for (int i = 0; i < original.length; i++) {
	    	for(int j=0; j<original[i].length;j++)
	    		result[i][j] = Arrays.copyOf(original[i][j], original[i][j].length);
	    }
	    return result;
	}
}
