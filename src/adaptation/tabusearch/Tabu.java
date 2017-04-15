package adaptation.tabusearch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import adaptation.geneticAlgo.Individual;
import adaptation.geneticAlgo.Individual2;
import schedulers.HeuristicScheduler;
import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;


/**
 * 
 * @author Tobias Rueckelt
 * 
 * how does tabuSearch work?
 * 
 * 1. from an anchor element, look up the fittest neighbor (similar solution) in 
 * 		its neighborhood (limited); skip neighbors which are in the tabuList.
 * 2. set fittest as next anchor element. 
 * 		add fittest to tabuList. This avoids exploration from same element again
 * 		and clean tabu list if it its size exceeds a limit (not implemented so far)
 * 4. if fittest better than "result", update result = fittest
 * 5. continue with 1. if stop criteria not satisfied
 *
 */

public class Tabu extends HeuristicScheduler{

	private final int MAX_ROUNDS= 10;
	private final int neighborhoodSize= 10;
	
	private Set<Individual2> tabuList = new HashSet<Individual2>();
	
	public Tabu(NetworkGenerator ng, FlowGenerator tg, NetworkGenerator ngPred,
			FlowGenerator tgPred, String longTermSchedule_logfile, String typeExt) {
		super(ng, tg, ngPred, tgPred, longTermSchedule_logfile);
		this.typeExt=typeExt;
	}
	
	
	protected void calculateInstance_internal(String logfile) {
		super.calculateInstance_internal(logfile);	//initialize long term plan and cost separation.
		
		for (int t = 0; t<ng.getTimeslots(); t++){
			//1. load network assignment from plan
			Individual2 initial = new Individual2(getActiveNetworks(t), getPlannedAssignment(t));
			
			//2. do taboSearch
			Individual2 fittest = tabuRun(initial, t);
			
			allocateIndividual(fittest, t);
		}
	}
	
	
	
	private Individual2 tabuRun(Individual2 initial, int t){
		if(initial.getSize()<=0) return initial;	//skip for empty ones
		
		Individual2 fittest = initial;
		
		//used as initial individual for next neighborhood exploration
		Individual2 anchor = initial; 	
		
		int round=100;
		int change = 10;
		
		while(anchor!=null && round>0 && change>0){
		//create n neighbors and get fittest
			Vector<Individual2> neighbors = getNeighbors(anchor);
		//getFittest in neighborhood and set as anchor for next exploration. add to tabu list.
			anchor = getFittest(neighbors, t);
			tabuList.add(anchor);
		//update result 
			if(anchor!=null && 
					anchor.getFitnessCache(this, t) < fittest.getFitnessCache(this, t)){
				fittest=anchor;
				change=10;
				System.out.println("Tabu: tabuRun: t="+t+", fitness has changed in round "+round+" to "+fittest.getFitnessCache(this, t));
			}
		//4. clean tabuList if full
			
			round--;
			change--;
		}
		
		
		return fittest;
	}
	
	private void allocateIndividual(Individual2 ind, int t){
		List<Integer> flowOrder =sortByFlowCriticality();
		for(int f_index: flowOrder){
			Flow flow = tg.getFlows().get(f_index);
			if(ind.getAssignment().get(flow)!=null){ //if flow active and a network assigned
				allocate(f_index, t, ind.getAssignment().get(flow).getId(), flow.getTokensMax());
			}
		}
	}


	/**
	 * @param anchor	element to search neighbors from
	 * @return n neighbors where n is the size of the anchor individual (number of active flows)
	 */
	private Vector<Individual2> getNeighbors(Individual2 anchor){
		Vector<Individual2> neighbors = new Vector<Individual2>();
		
		while(neighbors.size()<anchor.getSize()){
			Individual2 neighbor = anchor.mutate(1, true);	//TODO adapt rate??
			if(!tabuList.contains(neighbor)){
				neighbors.add(neighbor);
			}
		}
		return neighbors;
	}
	
	
	private Individual2 getFittest(Vector<Individual2> neighbors, int t) {
		Individual2 fittest = null;
		for(Individual2 neighbor: neighbors){
			if(fittest==null || 
					neighbor.getFitnessCache(this, t) < fittest.getFitnessCache(this, t)){
				
				fittest=neighbor;
			}
		}
		return fittest;
	}

	

	private boolean stopCriteriaNotSatisfied(int round) {
		if(round>0) return true;
		
		return false;
	}


	/**
	 * @param t
	 * @return Vector of all networks of ng which are active in times slot t
	 */
	private Vector<Network> getActiveNetworks(int t){
		Vector<Network> activeNet = new Vector<Network>();
		for (Network net : ng.getNetworks()){
			if(net.isActive(t))
				activeNet.add(net);
		}
		return activeNet;
	}
	
	/**
	 * @param t
	 * @return Vector of all flows of fg which are active in times slot t
	 */
	private Vector<Flow> getActiveFlows(int t){
		Vector<Flow> activeFlows = new Vector<Flow>();
		for (Flow flow : tg.getFlows()){
			if(flow.isActive(t))
				activeFlows.add(flow);
		}
		return activeFlows;
	}
	
	/**
	 * extracts the assignment from the long-term plan
	 * and cleans invalid assignments. add new flows with network null
	 * @param t time slot
	 * @return map of assignments Flow -> Network
	 */
	private Map<Flow,Network> getPlannedAssignment(int t){
		Map<Flow,Network> assignment = new HashMap<Flow,Network>();
		//check only valid assignment: for active flows and active networks
		for(Flow flow : getActiveFlows(t)){
			for(Network net : getActiveNetworks(t)){
				if(flow.getId()<tgPred.getFlows().size()){//if flow is not new 
					if(longTermSP_f_t_n[flow.getId()][t][net.getId()]>0){	//and data assigned
						assignment.put(flow, net);	//then add assignmentW
					}else{
						assignment.put(flow, null);
					}
				}else{
					assignment.put(flow, null);	//add new flows to assignment list without network
				}
			}
		}
		
		return assignment;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "AdaTabu";
	}
	
}

