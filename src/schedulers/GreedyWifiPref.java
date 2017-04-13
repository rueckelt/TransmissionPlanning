package schedulers;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;

public class GreedyWifiPref extends GreedyScheduler {

	public GreedyWifiPref(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
	}
	
	protected Vector<Integer> sortNetworkIDs(final int f){
		//create sorted list
		Vector<Integer> netIDs = new Vector<>();
		for(int n = 0; n<ng_tmp.getNetworks().size(); n++){		//use ng_tmp: remaining capacity of networks
			netIDs.add(n);
		}
		
		//comparator which compares network types. WiFi preferred (cell=1, wifi=2)
		Collections.sort(netIDs, new Comparator<Integer>(){
			@Override
			public int compare(Integer arg0, Integer arg1) {
				Network net0 = ng_tmp.getNetworks().get(arg0);
				Network net1 = ng_tmp.getNetworks().get(arg1);
				int result=net0.getType()-net1.getType();
				return result;
			}
		}
		);
//		Flow flow = tg.getFlows().get(f);		
//		System.out.println("network sort for Flow "+flow.getId()+" = "+netIDs);
		return netIDs;
		
	}
	
	public boolean scheduleDecision(int f, int n, int t) {
		return true;
	}
	
	public String getType(){
		return "DWO"; //delayed wifi offloading
	}

}
