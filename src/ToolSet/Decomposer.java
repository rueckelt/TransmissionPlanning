package ToolSet;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import schedulingIOModel.CostFunction;
import schedulingIOModel.Flow;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.FlowGenerator;

public class Decomposer {
	
	private FlowGenerator tg;
	private NetworkGenerator ng;
	
	public Decomposer(FlowGenerator tg, NetworkGenerator ng){
		this.ng=ng;
		this.tg=tg;
	}
	
	//split problem at start times and deadlines of flows. schedule homogeneous flow set
	
	
	public void decompose(){
		boundFlowDeadlines(tg, ng);
		//split
		List<Integer> splitPoints = getSplitPoints(tg);
		List<FlowGenerator> tg_timeFrames = splitFlows(tg, splitPoints);
		List<NetworkGenerator> ng_timeFrames = splitNetworks(ng,  splitPoints);		
		final List<Integer> criticalityList = getCriticalityList(tg_timeFrames, ng_timeFrames);
		
		List<Integer> tf_order = new LinkedList<Integer>();		//order of time frames
		for(int i=0; i<tg_timeFrames.size();i++){
			tf_order.add(i);
		}
		
		//sort time frames according to criticality
		Collections.sort(tf_order, new Comparator<Integer>(){
			@Override
			public int compare(Integer i1, Integer i2) {
				return criticalityList.get(i2)-criticalityList.get(i1);
			}
		});
		
		final List<Integer>  sp = splitPoints;
//		TODO test output only
		System.out.println("decreasing criticality:");
		for(int i:tf_order){
			System.out.println("index "+i+" value "+criticalityList.get(i));// + " size "+ (sp.get(i+1)-sp.get(i)) );
		}
		
		 
	}
	
	private void boundFlowDeadlines(FlowGenerator tg2, NetworkGenerator ng2) {
		//leads to error at deadlines out of range. Therefore normalize them!
		for(Flow f: tg.getFlows()){
			if(!(f.getDeadline()<ng.getTimeslots())){
				f.setDeadline(ng.getTimeslots()-1);
			}
		}
	}


	//##################### 1. get splitPoints ########################
	private List<Integer> getSplitPoints(final FlowGenerator tg){
		List<Integer> splitPoints = new LinkedList<Integer>();
		splitPoints.add(ng.getTimeslots()-1);
		for(Flow f: tg.getFlows()){
			addSplitPoint(f.getStartTime()-1, splitPoints);	//include start time slot
			addSplitPoint(f.getDeadline() , splitPoints);	//include end time slot
		}
		Collections.sort(splitPoints);
		System.out.println("Split points:"+splitPoints);
		return splitPoints;
	}
	//add splitPoint to vector if in range and not yet in set
	private void addSplitPoint(int t, List<Integer> splitPoints){
		if(t>0&& t<ng.getTimeslots() && !splitPoints.contains(t)){	//in range and not yet in set
			splitPoints.add(t);
		}
	}
	
	//##################### 2. Decomposition ########################
	private List<FlowGenerator> splitFlows(final FlowGenerator tg, final List<Integer> splitPoints){
		List<FlowGenerator> timeFrames = new LinkedList<FlowGenerator>();
		int start=0;	//init; first possible time slot
		for(int end: splitPoints){		//for each time frame (time between two split points) add relevant flows
			int duration = end-start+1;	//both are included, therefore +1
			System.out.println("start "+start+" end "+end);
			FlowGenerator tg_timeFrame = new FlowGenerator();
			for(Flow f: tg.getFlows()){
				if(f.getStartTime()<=start && f.getDeadline() >= end){		//is flow in range of time frame?
					Flow f_decomposed = f.clone();		//TODO: check if ID is same!
					//set start time and end time of decomposed flow
					f_decomposed.setStartTime(0);
					f_decomposed.setDeadline(duration);
					//set chunks of decomposed flow
					int chunks_split = f.getTokens()*(duration)/(f.getDeadline()-f.getStartTime()+1);	//dreisatz; include deadline => +1
					f_decomposed.setTokens(chunks_split);
					//add decomposed flow to time frame
					tg_timeFrame.addFlow(f_decomposed);
				}
			}
			timeFrames.add(tg_timeFrame);
			start = end+1;	//next time frame; non-overlapping
		}
		return timeFrames;
	}
	
	//do only use networks which are available in time frame 
	private List<NetworkGenerator> splitNetworks(NetworkGenerator ng, List<Integer> splitPoints){
		List<NetworkGenerator> timeFrames = new LinkedList<NetworkGenerator>();
		int start=0;	//init; first possible time slot
		for(int end: splitPoints){		//for each time frame (time between two split points) add relevant flows
			NetworkGenerator ng_timeFrame = new NetworkGenerator();
			for(Network n : ng.getNetworks()){
				//get capacity for relevant time frame
				Vector<Integer> capacity = new Vector<Integer>();
				capacity.addAll(n.getCapacity().subList(start, end+1));	//include end in subList
				//sum up capacity to see if network is available in time frame
				int sum_capacity = 0;
				for (Integer cap : capacity) {
					sum_capacity+= cap;
				}
				if(sum_capacity>0){ //network is available in time frame?
					Network n0 = n.clone();		//TODO: check if ID is equal!
					n0.setCapacity(capacity);
					ng_timeFrame.addNetwork(n0);	//add
				}
			}
			timeFrames.add(ng_timeFrame);
			start = end+1;	//next time frame; non-overlapping
		}
		return timeFrames;
	}
	

	

	//##################### 3. criticality of time frames ########################
	/**
	 * 
	 * @param flow_timeFrames
	 * @param net_timeFrames
	 * @return list of timeFrame criticalities
	 */
	private List<Integer> getCriticalityList(List<FlowGenerator> flow_timeFrames, List<NetworkGenerator> net_timeFrames){
		List<Integer> criticalityList = new LinkedList<Integer>();
		for(int i =0;i<flow_timeFrames.size();i++){
			if(!flow_timeFrames.get(i).getFlows().isEmpty() && !net_timeFrames.get(i).getNetworks().isEmpty()){
				System.out.println("#### Analyze time frame "+i);
				criticalityList.add(getTimeFrameCriticality(flow_timeFrames.get(i), net_timeFrames.get(i)));
			}else{
				System.out.println("#### Skip time frame "+i);
				criticalityList.add(-1);
			}
		}
		return criticalityList;
	}
	
	private int getTimeFrameCriticality(FlowGenerator tg, NetworkGenerator ng){
		int criticality =0;
		for(Flow f: tg.getFlows()){
			criticality+=CostFunction.calculateFlowCriticality(f, ng);
		}
		return criticality;
	}
	

	private List<Integer[][][]>  scheduleTimeFrames(List<FlowGenerator> flow_timeFrames, List<NetworkGenerator> net_timeFrames, List<Integer>tf_order){
		List<Integer[][][]> schedules = new LinkedList<Integer[][][]>();
		
		
		return schedules;
	}
}
