package ToolSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import schedulingIOModel.NetworkGenerator;

/**
 * this class keeps track of on many interfaces of a certain type have been used in one time slot during scheduling
 * it is used to satisfy the constrained limit of available network interfaces of each type
 * @author QZ61P8
 *
 */
public class InterfaceLimit {
	
	//dimension 1: interface type
	//dimension 2: time slot
	//value: nof used interfaces of this type in this time slot
	int[][] used_interfaces;
	//key is time slot
	//value is a vector of used networks in this time slot
	Map<Integer, Vector<Integer>> usedNetworks = new HashMap<Integer, Vector<Integer>>();
	
	
	NetworkGenerator ng;
	public InterfaceLimit(NetworkGenerator ng){
		this.ng=ng;
		//initialize matrix with zeros: all unused; will be filled during scheduling
		used_interfaces = new int[ng.getNofInterfaceTypes()][ng.getTimeslots()];
	}
	
	/**
	 * network is usable if
	 * (1) net already in list or
	 * (2) net not in list but interface is unused
	 * @param netID
	 * @param timeslot
	 * @return
	 */
	public boolean isUsable(int netID, int timeslot){
		//if(true)return true;	//switch check off
		int ifType=ng.getNetworks().get(netID).getType()-1;
		//values in limits?
		if(ifType>=ng.getNofInterfaceTypes() || timeslot>=ng.getTimeslots()){
//			System.err.println("InterfaceLimit.java: Scheduler checks interface availability for invalid interface type or time slot");
			return false;
		}

		//(1) network already in use - then remaining resources may be used
		Vector<Integer> nets = usedNetworks.get(timeslot);
		if(nets!=null && nets.contains(netID)){
			return true;
		}
		//(2) interface is free
//		System.out.println("InterfaceLimit::isUsable(net "+netID+", time "+timeslot+");;   #used IF is "+used_interfaces[ifType][timeslot]);
		return used_interfaces[ifType][timeslot]<ng.getNofInterfacesOfType(ifType);
	}
	
	public boolean useNetwork(int netID, int timeslot){
		int ifType=ng.getNetworks().get(netID).getType()-1;
		boolean usable=isUsable(netID, timeslot);
		if (usable){
			used_interfaces[ifType][timeslot]++;

			Vector<Integer> nets = usedNetworks.get(timeslot);
			//if empty map, then initialize list
			if(nets==null){
				nets=new Vector<Integer>();
				usedNetworks.put(timeslot, nets);
			}
			nets.add(netID);
			
		}
		return usable;
	}
	

}
