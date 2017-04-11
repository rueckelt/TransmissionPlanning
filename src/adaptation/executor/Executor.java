package adaptation.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ToolSet.JsonLogger;
import schedulers.Scheduler;
import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import visualization.Plot;
import visualization.VisualizationPack;

public class Executor {
	private int[][][] schedulePlan;
	private NetworkGenerator ng;
	private FlowGenerator fg;
//	private int[][][] executedPlan;
	private Scheduler scheduler;
	// strategy...
	
	public Executor(int[][][] sp, NetworkGenerator ngVar, FlowGenerator fgVar, Scheduler scheduler) {
		setSchedulePlan(sp);
		setNg(ngVar);
		setFg(fgVar);
//		int flowLen = Math.max(fgVar.getFlows().size(), sp.length);
//		int netLen = Math.max(ngVar.getNetworks().size(), sp[0][0].length);
//		setExecutedPlan(new int[fgVar.getFlows().size()][ngVar.getTimeslots()][ngVar.getNetworks().size()]);	
		this.scheduler=scheduler;
	}
	public void run(boolean plotBool) {
		
		ArrayList<Integer> restData = new ArrayList<Integer>();
		HashMap<Integer, ArrayList<Integer>> paused = new HashMap<>();
		// new flows are neglected in Executor
		List<Flow> flows = getFg().getFlows();
		
	//set rest=absolute number of tokens for each flow
		for (int f = 0; f < schedulePlan.length ; f++) {
			if (f >= flows.size())  {	//this is a new flow. do not allocate tokens of it. rest=0
				restData.add(0);			
			} else {
				restData.add(flows.get(f).getTokens());
			}
		}
	
	//if there exist new flows in the plan	
		if (flows.size() > schedulePlan.length) {
			//if paused, record the starttime and flow-Id
			////System.out.println("schedule Plan length: " + schedulePlan.length);
			
			//for each new added flow
			for (int f = schedulePlan.length; f < flows.size(); f++) {
				Flow tmp = flows.get(f);
				
				//for each paused, store start time
				if (tmp.getId() <= schedulePlan.length && tmp.getId() < tmp.getIndex()) {
					////System.out.println("paused flow: f: " + f + " - fid: " + tmp.getId() + " - st: " + tmp.getStartTime());
					if (paused.containsKey(tmp.getStartTime())) {
						paused.get(tmp.getStartTime()).add(f);
					} else {
						ArrayList<Integer> pausedFlow = new ArrayList();
						pausedFlow.add(f);
						paused.put(tmp.getStartTime(), pausedFlow);
					}
				}
			}
		}

	//1. add data of the paused flow to the actual	
		for (int t = 0; t < schedulePlan[0].length; t++) {
			////System.out.println("t: " + t + " - " + "restData before paused flow: " + restData.toString());
			if (paused.containsKey(t)) {
				////////System.out.println("t: " + t + " - " + "restData before paused flow: " + restData.toString());
				for (Integer pausedFlow : paused.get(t)) {
					int id = flows.get(pausedFlow).getId();
					int restDataPf = flows.get(pausedFlow).getTokens();
					if (id < restData.size()) {
						restData.set(id, restData.get(id) + restDataPf);
					}
				}
				////System.out.println("t: " + t + " - " + "restData after paused flow: " + restData.toString());
			}
		
		
			for (int n = 0; n < schedulePlan[0][0].length && n < getNg().getNetworks().size(); n++) {
				int cap = 0;
				// new network neglected
				if (n >= getNg().getNetworks().size()) {
					cap = 0;
				} else {
					cap = getNg().getNetworks().get(n).getCapacity().get(t);
				}
				
//				total += cap;
				int sumNetTp = 0; 
				int fNum = 0;

				
				
				int num_of_flows = Math.min(schedulePlan.length, getFg().getFlows().size());
				
				//count number of active flows in this time slot and network
				for (int f = 0; f < num_of_flows; f++) {					//restData.add(getFg().getFlows().get(f).getTokens());
					if (schedulePlan[f][t][n] != 0) {
						sumNetTp += schedulePlan[f][t][n];
						fNum++;
					}
				}
				
				//assign data of active flows to arrays
				int[] dataToSent = new int[fNum];
				int[] dataId = new int[fNum];
				int[] executed = new int[fNum];
				int dataIdItr = 0;
				for (int f = 0; f < num_of_flows; f++) {
					if (schedulePlan[f][t][n] != 0) {
						dataToSent[dataIdItr] = schedulePlan[f][t][n];
						dataId[dataIdItr] = f;
						dataIdItr++;
					}
				}
				//if amount of data scheduled smaller equal current network capacity, 
				//then assign to executed, limited by the maximum flow capacity
				if (sumNetTp <= cap) {
					for (int f = 0; f < num_of_flows; f++) {
						int allocated = scheduler.allocate(f,t,n,Math.min(schedulePlan[f][t][n], restData.get(f)));
						restData.set(f, Math.max(restData.get(f) - allocated, 0));
					}
				//if there is more data than available space in the network
				//then assign in round robin fashion. also schedule data of paused ones
				} else {
					RRB rrb = new RRB();
					rrb.rrb(cap, dataToSent, executed, fNum);
					
					for (int f = 0; f < dataToSent.length; f++) {
						int fId = dataId[f];
						int allocated = scheduler.allocate(f,t,n, executed[f]);						
						restData.set(fId, Math.max(restData.get(f) - allocated, 0));
					}	
				}
			}
		}
		
		if (plotBool) {
//			Plot plot2 = new Plot(new VisualizationPack(ng, fg, getExecutedPlan()));
		}
		
	}

	public int[][][] getSchedulePlan() {
		return schedulePlan;
	}

	public void setSchedulePlan(int[][][] schedulePlan) {
		this.schedulePlan = schedulePlan;
	}

	public NetworkGenerator getNg() {
		return ng;
	}

	public void setNg(NetworkGenerator ng) {
		this.ng = ng;
	}

	public FlowGenerator getFg() {
		return fg;
	}

	public void setFg(FlowGenerator fg) {
		this.fg = fg;
	}


}
