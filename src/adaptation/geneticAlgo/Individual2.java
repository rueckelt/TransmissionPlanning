package adaptation.geneticAlgo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import schedulers.FitnessEvaluator;
import schedulers.HeuristicScheduler;
import schedulingIOModel.Flow;
import schedulingIOModel.Network;

public class Individual2 {

	private Vector<Network> activeNetworks = new Vector<Network>();
	private Map<Flow,Network> assignment = new HashMap<Flow,Network>();
	private long fitness_cache= Long.MAX_VALUE;	//worst
	
	//initial assignment
	public Individual2(Vector<Network> activeNetworks, Map<Flow,Network> assignment){
		this.activeNetworks=activeNetworks;
		this.assignment=assignment;
	}
	
	//creates a copy
	public Individual2(final Individual2 ind) {
		this.activeNetworks.addAll(ind.getActiveNetworks());
		for(Flow flow : ind.assignment.keySet()){
			this.assignment.put(flow, ind.getAssignment().get(flow));
		}
//		this.assignment.putAll(ind.getAssignment());
	}
	
	/**
	 * @param mutation_rate default=1; higher leads to more mutation.
	 * @return a new individual which is mutated with a certain probability
	 */
	public Individual2 mutate(double mutation_rate){
		return mutate(mutation_rate, false);
	}
	/**
	 * 
	 * @param mutation_rate default=1; higher leads to more mutation
	 * @return a new individual which is mutated with a certain probability. 
	 * repeated mutation tries if enforceChange is true
	 */
	public Individual2 mutate(double mutation_rate, boolean enforceChange){
		Individual2 mutated = new Individual2(this);
		Set<Network> mutationTargets = new HashSet<Network>();
		
		int tries=1;
		if(enforceChange) tries =20;
		
		do{
			for(Flow f : assignment.keySet()){
				boolean doChange = Math.random() < (mutation_rate / assignment.keySet().size());
				if(doChange){
					Network net = getRandomNetwork();
					if(net != assignment.get(f)){	//if it is really a change.
						if(net!=null) mutationTargets.add(net);
						mutated.assignment.put(f, net);	//update assignment with mutation
					}
				}
			}
			//after random mutation, guarantee that constraints are satisfied
			//we check only interface limit
			mutated.enforceConstraints(mutationTargets);
			tries--;
		}
		while(tries>0 && equals(mutated));
		//repeat as long as repeated is not different
//		System.out.println("mutation from: "+this.toString()+ "   to    "+mutated.toString());
		return mutated;
	}
	
	/**
	 * @return a random network from the active ones
	 */
	private Network getRandomNetwork(){
		int rndIndex = (int)(Math.random()*(activeNetworks.size()+1))-1;
		if(rndIndex<0) return null;
		return activeNetworks.get(rndIndex);
	}
	
	//TODO this does not cover multiple interfaces of same technology
	/**
	 * ensure that no more networks are used of same technology than interfaces
	 * of this technology are available. Currently just 1 of each technology!
	 * @param mutationTargets
	 */
	private void enforceConstraints(Set<Network> mutationTargets){
		for(Network net:mutationTargets){
			for(Flow f: assignment.keySet()){
				if(assignment.get(f)!=null &&
						assignment.get(f).getType() == net.getType()){
					assignment.put(f, net);
				}
			}
		}
	}
	
	
	public Vector<Network> getActiveNetworks() {
		return activeNetworks;
	}
	public Map<Flow,Network> getAssignment() {
		return assignment;
	}

	/**
	 * checks for equal assignment
	 */
	public boolean equals(Object o){
		if(! (o instanceof Individual2)) return false;
		Individual2 ind= (Individual2) o;
		return assignment.equals(ind.getAssignment());
	}
	
	public int getSize(){
		return assignment.size();
	}

	public int getFitness(HeuristicScheduler scheduler, int t) {
		FitnessEvaluator fe = new FitnessEvaluator(scheduler);
		
		int fitness = fe.getFitness(assignment, t);	//the smaller the better
		
		return fitness;
		
	}
	
	public long getFitnessCache(HeuristicScheduler scheduler, int t){
		if(fitness_cache==Long.MAX_VALUE){
			fitness_cache=getFitness(scheduler,t);
		}
		return fitness_cache;
	}
	
	public String toString(){
		String s= "Individual: ";
		for(Flow f : assignment.keySet()){
			s+="f:"+f.getId()+"->";
			if(assignment.get(f)!=null)
				s+=assignment.get(f).getId();
			else
				s+="null";
			s+=";  ";
		}
		return s;
	}

}
