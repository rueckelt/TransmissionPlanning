package ToolSet;

import java.util.Arrays;

import schedulingIOModel.CostFunction;
import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;

/**
 * This class provides fast access to cost values
 * It pre-calculates and stores values to arrays
 * This allows constant time lookup without additional mathematical operations
 * @author QZ61P8
 *
 */
public class CostSeparation {
	
	private FlowGenerator tg;
	private NetworkGenerator ng;
	int[][] networkMatch;		//holding two-dimensional flows*nets matrix
	int[][] timeMatch;			//holding two-dimensional flows*timeslots matrix
	int[][] statefulReward; 	//holding state of unused min_throughput reward; flows*timeslots matrix
	int[] flowReward;
	
	CostFunction cf;
	
	public CostSeparation(FlowGenerator tg, NetworkGenerator ng){
		this.ng=ng;
		this.tg=tg;
		cf = new CostFunction(ng, tg);
		
		//pre-calculate and store everything that is fixed and required during scheduling process
		networkMatch=new int[tg.getFlows().size()][ng.getNetworks().size()];
		timeMatch=new int[tg.getFlows().size()][ng.getTimeslots()];
		statefulReward=new int[tg.getFlows().size()][ng.getTimeslots()];
		flowReward = new int[tg.getFlows().size()];
		setNetworkMatch();
		setTimeMatch();
		setFlowReward();
		initStatefulReward();
	}
	
	
	//pre-calculate cost from network choice per flow and per network
	private void setNetworkMatch() {
		int f=0;
		for(Flow flow:tg.getFlows()){
			int n=0;
			for(Network net:ng.getNetworks()){
				double denom = 50;	//default value for average number of tokens when scheduled (estimated)
				if(flow.getTokensMin()>0 && flow.getWindowMin()>0){
					denom=flow.getTokensMin()/flow.getWindowMin();
				}
						
				networkMatch[f][n] = 
				 (int) (flow.getImpUser()*(		
							//latency and jitter match work per time slot. 
							//Therefore we normalize it to the average min tp of the flow				
							(CostFunction.latencyMatch(flow, net)+
							CostFunction.jitterMatch(flow, net)) 
							/denom))
						+cf.monetary(net);
				n++;
			}
			f++;
		}
	}

	
	//pre-calculate cost from time slot choice per flow and per time slot
	private void setTimeMatch() {
		int f=0;
		for(Flow flow:tg.getFlows()){

//			System.out.print("Time Match flow "+f+": ");
			for(int t=0; t<ng.getTimeslots(); t++){
				timeMatch[f][t] = CostFunction.vioTimeLimits(flow, t)*flow.getImpUser();
//				System.out.print(timeMatch[f][t]+", ");
//				if(f==6 && (t==44 || t==45)){
//					System.out.println("time match = "+timeMatch[f][t]);
//				}
			}
//			System.out.println("");
			f++;
			
		}
		
	}

	public int getUnitstatefulReward(int f) {
		Flow flow = tg.getFlows().get(f); 
		return getStatefulRewardStep(flow);
	}
	//calculate potential reward init-state from minTp for scheduling a token of flow f to time slot t
	private void initStatefulReward(){
		int f=0;
		for(Flow flow:tg.getFlows()){
			int reward=0;
			int step = getStatefulRewardStep(flow);	
			for(int t=0; t<ng.getTimeslots(); t++){
				//increment stateful reward for each time when multiple throughput windows cover the time slot
				if(t>=flow.getStartTime() && t<=flow.getStartTime()+flow.getWindowMin()){
					reward+=step;
				}
				//decrement stateful reward for each timeslot when window covers time slot less often
				if(t>=flow.getDeadline()-flow.getWindowMin() && t<=flow.getDeadline()){
					reward-=step;
				}

				statefulReward[f][t] = Math.max(0, reward);
//				System.out.println(reward);
			}
			f++;
			printStatefulReward();
		}
	}
	
	public void printStatefulReward(){
//		System.out.println("stateful_reward = "+Arrays.deepToString(statefulReward));
	}
	
	public int getStatefulRewardStep(Flow flow){
		return cf.getMinTpAmplifier()*flow.getImpThroughputMin()*flow.getImpUser();
	}
	
	private void setFlowReward(){
		int f=0;
		for(Flow flow:tg.getFlows()){
			flowReward[f] = flow.getImpUnsched()*flow.getImpUser()*flow.getImpUser();
			f++;
		}
	}
	
	public void updateStatefulReward(int f, int t, int tokens){
		Flow flow =tg.getFlows().get(f);
		int step= getStatefulRewardStep(flow)*tokens*flow.getWindowMin()/Math.max(1, flow.getTokensMin());
//		if(f==3)System.out.println("cost separation: updatestatefulReward step = "+step+", t="+t+", f="+f+", tokens="+tokens);

		//update each affected time slot
		for(int i=Math.max(0,t-flow.getWindowMin()); i<=Math.min(ng.getTimeslots()-1,t+flow.getWindowMin()); i++){
			statefulReward[f][i]=Math.max(0, statefulReward[f][i]-step);	//no negative values
		}
		
	}

	//getters
	
	/**
	 * 
	 * @param f
	 * @param t
	 * @return the cost if at least one token of flow f is scheduled to time slot t
	 */
	public int getTimeMatch(int f, int t){
		return timeMatch[f][t];
	}
	/**
	 * 
	 * @param f
	 * @param t
	 * @return the reward (neg cost value) of throughput criterion if one token of flow f is scheduled to time slot t
	 *  DONT'T forget to use the update function updateStatefulReward after scheduling of tokens!
	 */
	public int getStatefulReward(int f, int t){
		Flow flow=tg.getFlows().get(f);
		return -statefulReward[f][t]/	Math.max(1,(flow.getWindowMin()*flow.getTokensMin()));
	}
	/**
	 * 
	 * @param f
	 * @param n
	 * @return the mismatch (pos cost value) if one token of flow f is scheduled to network n
	 */
	public int getNetworkMatch(int f, int n){
		if(f>=networkMatch.length || n>=networkMatch[0].length){
			System.err.println("CostSeparation::getNetworkMatch - request exceeded range of matrix");
			return Integer.MAX_VALUE;	//invalid request - return worst case
		}
		return networkMatch[f][n];
	}
	/**
	 * 
	 * @param f
	 * @return the reward (neg cost value) if one token of flow f is scheduled
	 */
	public int getStatelessReward(int f){
		return -flowReward[f];	//the equation contains user^2
	}
	

	public FlowGenerator getTg(){
		return tg;
	}
	public NetworkGenerator getNg(){
		return ng;
	}

}
