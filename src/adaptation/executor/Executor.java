package adaptation.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ToolSet.JsonLogger;
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
	private int[][][] executedPlan;
	// strategy...
	
	public Executor(int[][][] sp, NetworkGenerator ngVar, FlowGenerator fgVar) {
		setSchedulePlan(sp);
		setNg(ngVar);
		setFg(fgVar);
//		int flowLen = Math.max(fgVar.getFlows().size(), sp.length);
//		int netLen = Math.max(ngVar.getNetworks().size(), sp[0][0].length);
		setExecutedPlan(new int[fgVar.getFlows().size()][ngVar.getTimeslots()][ngVar.getNetworks().size()]);	
	}
	public void run(boolean plotBool) {
		int total = 0;
		ArrayList<Integer> restData = new ArrayList<Integer>();
		HashMap<Integer, ArrayList<Integer>> paused = new HashMap<>();
		// new flows are neglected in Executor
		List<Flow> flows = getFg().getFlows();
		////////System.out.println("sp.length: " + schedulePlan.length + " - f.size(): " + flows.size());
		for (int f = 0; f < schedulePlan.length ; f++) {
			if (f >= getFg().getFlows().size())  {
				restData.add(0);			
			} else {
				restData.add(getFg().getFlows().get(f).getTokens());
			}
		}
		
		if (flows.size() > schedulePlan.length) {
			//if paused, record the starttime and flow-Id
			////System.out.println("schedule Plan length: " + schedulePlan.length);
			for (int f = schedulePlan.length; f < flows.size(); f++) {
				Flow tmp = flows.get(f);
				////////System.out.println("hello f : " + f);
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
		////System.out.println("*********************");
		for(int t = 0; t < schedulePlan[0].length; t++) {
		//	for (ArrayList<Integer> p : paused.get) {
			if (paused.containsKey(t)) {
				////System.out.println(" t : " + t + " - " + paused.get(t).toString());
			}
		}
		////System.out.println("*********************");

		
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

				int len = Math.min(schedulePlan.length, getFg().getFlows().size());
				for (int f = 0; f < len; f++) {					//restData.add(getFg().getFlows().get(f).getTokens());
					if (schedulePlan[f][t][n] != 0) {
						sumNetTp += schedulePlan[f][t][n];
						fNum++;
					}
				}
				int[] dataToSent = new int[fNum];
				int[] dataId = new int[fNum];
				int[] executed = new int[fNum];
				int dataIdItr = 0;
				for (int f = 0; f < len; f++) {
					if (schedulePlan[f][t][n] != 0) {
						dataToSent[dataIdItr] = schedulePlan[f][t][n];
						dataId[dataIdItr] = f;
						dataIdItr++;
					}
				}
		
				if (sumNetTp <= cap) {
					for (int f = 0; f < len; f++) {
						getExecutedPlan()[f][t][n] = Math.min(schedulePlan[f][t][n], restData.get(f));
						restData.set(f, Math.max(restData.get(f) - getExecutedPlan()[f][t][n], 0));
					}
				} else {
					int rest = cap;
					RRB rrb = new RRB();
					rrb.rrb(cap, dataToSent, executed, fNum);
					for (int f = 0; f < dataToSent.length; f++) {
						int fId = dataId[f];
						getExecutedPlan()[fId][t][n] = executed[f];
						restData.set(fId, Math.max(restData.get(f) - getExecutedPlan()[f][t][n], 0));
					}
				}
			}
		}

		////////System.out.println("planed tp: " + Simulation.calcTP(schedulePlan).toString());
		////////System.out.println("real tp: " + Simulation.calcTP(getExecutedPlan()).toString());
		////////System.out.println("total: " + total);
		
		if (plotBool) {
			Plot plot2 = new Plot(new VisualizationPack(ng, fg, getExecutedPlan()));
		}
		
	}
	/*
	public void run() {
		int total = 0;
		ArrayList<Integer> restData = new ArrayList<Integer>();
		for (int f = 0; f < schedulePlan.length ; f++) {
			if (f >= getFg().getFlows().size())  {
				restData.add(0);
			} else {
				////////System.out.println("f: " + f);
				restData.add(getFg().getFlows().get(f).getTokens());
			}
		}
		for (int t = 0; t < schedulePlan[0].length; t++) {
			for (int n = 0; n < schedulePlan[0][0].length; n++) {
				int cap = 0;
				if (n > getNg().getNetworks().size()) {
					cap = 0;
				} else {
					cap = getNg().getNetworks().get(n).getCapacity().get(t);
				}
				total += cap;
				int sumNetTp = 0;
				int fNum = 0;

				int len = Math.min(schedulePlan.length, getFg().getFlows().size());
				for (int f = 0; f < len; f++) {
					sumNetTp += schedulePlan[f][t][n];
					//restData.add(getFg().getFlows().get(f).getTokens());
					if (schedulePlan[f][t][n] != 0) {
						fNum++;
					}
				}
				////////System.out.println("sumNet: " + sumNetTp + " " + "cap: " + cap);
				if (sumNetTp <= cap) {
					//int len = Math.min(schedulePlan.length, getFg().getFlows().size());
					for (int f = 0; f < len; f++) {
						getExecutedPlan()[f][t][n] = Math.min(schedulePlan[f][t][n], restData.get(f));
						restData.set(f, Math.max(restData.get(f) - getExecutedPlan()[f][t][n], 0));
					}
				} else {
					//1400
					int rest = cap;
					for (int f = 0; f < schedulePlan.length ; f++) {
						if (schedulePlan[f][t][n] != 0 && schedulePlan[f][t][n] <= (cap / fNum)) {
							////////System.out.println("f: " + f + " restData.size(): " + restData.size());
							if (f < fg.getFlows().size()) {
								getExecutedPlan()[f][t][n] = Math.min(schedulePlan[f][t][n], restData.get(f));
								restData.set(f, Math.max(restData.get(f) - getExecutedPlan()[f][t][n], 0));
								rest -= getExecutedPlan()[f][t][n];
								fNum--;
							}

						}
					}
					int average = fNum == 0? 0 : rest / fNum;
					for (int f = 0; f < schedulePlan.length && f < getFg().getFlows().size() && fNum > 0; f++) {
						if (schedulePlan[f][t][n] != 0) {
							if (schedulePlan[f][t][n] > average) {
							
								getExecutedPlan()[f][t][n] = Math.min(average, restData.get(f));
								restData.set(f, Math.max(restData.get(f) - getExecutedPlan()[f][t][n], 0));
								rest -= getExecutedPlan()[f][t][n];
								fNum--;
							average = fNum == 0? 0 : rest / fNum;
							} else {
								getExecutedPlan()[f][t][n] = Math.min(schedulePlan[f][t][n], restData.get(f));
								restData.set(f, Math.max(restData.get(f) - getExecutedPlan()[f][t][n], 0));
								rest -= getExecutedPlan()[f][t][n];
								fNum--;
								average = fNum == 0? 0 : rest / fNum;
							}
						}
					}
					
				}
			}
		}

		////////System.out.println("planed tp: " + Simulation.calcTP(schedulePlan).toString());
		////////System.out.println("real tp: " + Simulation.calcTP(getExecutedPlan()).toString());
		////////System.out.println("total: " + total);
		Plot plot2 = new Plot(new VisualizationPack(ng, fg, getExecutedPlan()));
		
	}
	*/
	public void roundRobin() {
		
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

	public int[][][] getExecutedPlan() {
		return executedPlan;
	}

	public void setExecutedPlan(int[][][] executedPlan) {
		this.executedPlan = executedPlan;
	}
	
	public void equalAllocate(int cap, List<Integer> restData, int t, int n, int fNum) {
		int rest = cap;
		for (int f = 0; f < schedulePlan.length ; f++) {
			if (schedulePlan[f][t][n] != 0 && schedulePlan[f][t][n] <= (cap / fNum)) {
				////////System.out.println("f: " + f + " restData.size(): " + restData.size());
				if (f < fg.getFlows().size()) {
					getExecutedPlan()[f][t][n] = Math.min(schedulePlan[f][t][n], restData.get(f));
					restData.set(f, Math.max(restData.get(f) - getExecutedPlan()[f][t][n], 0));
					rest -= getExecutedPlan()[f][t][n];
					fNum--;
				}

			}
		}
		int average = fNum == 0? 0 : rest / fNum;
		for (int f = 0; f < schedulePlan.length && f < getFg().getFlows().size() && fNum > 0; f++) {
			if (schedulePlan[f][t][n] != 0 && schedulePlan[f][t][n] > average) {
				getExecutedPlan()[f][t][n] = Math.min(average, restData.get(f));
				restData.set(f, Math.max(restData.get(f) - getExecutedPlan()[f][t][n], 0));
				rest -= getExecutedPlan()[f][t][n];
				fNum--;
				average = fNum == 0? 0 : rest / fNum;
			} 
		}
	}
	

	
	public class Pair implements Comparable<Pair> {
	    public final int index;
	    public final int value;

	    public Pair(int index, int value) {
	        this.index = index;
	        this.value = value;
	    }

	    @Override
	    public int compareTo(Pair other) {
	        //multiplied to -1 as the author need descending sort order
	        return -1 * Integer.valueOf(this.value).compareTo(other.value);
	    }
	}
	

}
