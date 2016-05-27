package schedulingIOModel;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import ToolSet.PersistentStore;
import ToolSet.RndInt;
import optimization.ModelAccess;

/**
 * Firstly creates Network objects defining capacity to each time slot
 * Secondly converts it to network part of the dat-file 
 * @author QZ61P8
 *
 */


public class NetworkGenerator implements Serializable, Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4527605239252256091L;
	public static final String NG_NAME = "NetworkGenerator";
	private Vector<Network> networks = new Vector<Network>();
	
	private int hysteresis = 500;
	private int cost_imp = 15;	//importance of monetary cost for network use
	//one interface of each type. Only first two are used 
	private int[] interfacesOftype = {1,1};	//default: (index: 0 = NONE, 1 = #wifi, 2 = #mobile network) 
	
	public NetworkGenerator(){
	}
	
	public NetworkGenerator(int nofNetworks, int time){
		addNetworks(nofNetworks, time);
	}
	
	public static NetworkGenerator loadNetworkGenerator(String path){
		NetworkGenerator ng=null;
		try{
			if(new File(path+NG_NAME).exists()){
				ng= (NetworkGenerator) PersistentStore.loadObject(path+NG_NAME);
//				ng.setNofInterfacesOfType(new int[]{1,1});
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ng;
	}
	
	public int getTimeslots(){
		return networks.get(0).getSlots();
	}
	
	//write this object to file
	public void writeObject(String dest){
		PersistentStore.storeObject(dest+NG_NAME, this);
	}
	
	
	public Vector<Network> getNetworks(){
		return networks;
	}
	
	public void addNetwork(Network network){
		networks.add(network);
		//align number of time slots of all networks
		int size = getNofTimeSlots();
		for (Network net : networks) {
			net.setSlots(size);
		}
	}
	
	private void addNetworks(int nofNetworks, int time){
		Random r = new Random();
		r.setSeed(System.nanoTime());
		
		//initialize automatically
		for (int i=0; i<nofNetworks;i++){
			if(i%4==0){
				addNetwork(Network.getCellular(time, RndInt.get(20, 60)));	//cellular available all the time; consant rate (bad model)
			}else{
				int duration = RndInt.get(5, 5+(int)Math.round(Math.sqrt(time))); //availablity at least 10 slots + extra (depends on sqrt of time)
				int delay = RndInt.get(0, time-duration-1);
				int tokens = RndInt.get(20,100);
				addNetwork(Network.getWiFi(duration, tokens , delay));
			}
		}
	}
	
	
	public int getNofInterfaceTypes(){
		int interfaceTypes=0;	
		for(Network network: networks){
			if(network.getType()>interfaceTypes){
				interfaceTypes = network.getType();
			}
		}
		return interfaceTypes;
	}
	
	public int[] getNofInterfacesByType(){
		int[] intByType= new int[getNofInterfaceTypes()];
		
		for(Network network: networks){
			intByType[network.getType()-1]++;	//indexing needs -1, because client interface numbers start at 1
		}
		return intByType;
	}
	
	private int getNofTimeSlots(){
		int slots=0;
		for(Network network: networks){
			if(network.capacity.size()>slots){
				slots = network.capacity.size();
			}
		}
		return slots;
	}
	

	public void writeOutput(String source, String dest){

		String content="";
		//read source
		try {
			content = new Scanner(new File(source)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//get max size
		int size = getNofTimeSlots();
		int interfaceTypes = getNofInterfaceTypes();
		
		content=content.replace("[nChannels]", ""+networks.size());
		content=content.replace("[nTime]", ""+size);
		content=content.replace("[interfaceTypes]", ""+interfaceTypes);
		content=content.replace("[hysteresis]", ""+hysteresis);
		content=content.replace("[costImp]", ""+cost_imp);
		
		//one interface of each type
		boolean first=true;
		String clientInterfaces = "";
		for(int i=0;i<interfaceTypes;i++){
			if(first){
				first = false;
			}else{
				clientInterfaces+=", ";
			}
			//add list item
			clientInterfaces+=""+interfacesOftype[i];
		}
		
		content=content.replace("[clientInterfaces]", clientInterfaces);
		
		//set equal size
		//and print networks
		String availBW="";
		String type = "";
		String cost = "";
		String latency = "";
		String jitter = "";
		
		first = true;
		for (Network network : networks) {
			//list separators: omit before first list element
			if(first){
				first = false;
			}else{
				availBW+=",\n";
				type+=", ";
				cost+=", ";
				latency+=", ";
				jitter+=", ";
			}
			//add list items
			
			availBW+="\t"+network.toString();
			type+=network.getType();
			cost+=network.getCost();
			latency+=network.getLatency();
			jitter+=network.getJitter();
		}
		
		content=content.replace("[availBW]", availBW);
		content=content.replace("[type]", type);
		content=content.replace("[cost]", cost);
		content=content.replace("[latency]", latency);
		content=content.replace("[jitter]", jitter);
		
		content= content.replace("[networkGenTime]", new Date().toString());
		
		//write file
		try {
			PrintWriter pw = new PrintWriter(dest);
			pw.println(content);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//It did not work to insert data directly into the model. Therefore use method via file write and read above

//	public void setNetworkData(IloOplModel model, IloOplFactory fac) {
//		//init arrays
//		int n=networks.size();	//networks
//		int time=getNofTimeSlots();	//time slots
//		
//		int[][] availBW = new int[n][time];
//		int[] type = new int[n];
//		int[] cost = new int[n];
//		int[] latency = new int[n];
//		int[] jitter = new int[n];
//		
//		//fill arrays
//		int n0 =0;
//		for(Network net: networks){
//			for(int t=0; t<time; t++){
//				if(t<net.capacity.size()){
//					availBW[n0][t]=net.capacity.get(t);
//				}else{
//					availBW[n0][t]=0;
//				}
//			}
//			type[n0]=net.getType();
//			cost[n0]=net.getCost();
//			latency[n0]=net.getLatency();
//			jitter[n0]=net.getJitter();
//			
//			n0++;
//		}
//		
//		//set data
//		ModelAccess.set(fac, model, "availBW", availBW);
//		ModelAccess.set(fac, model, "channel_cost", cost);
//		ModelAccess.set(fac, model, "channel_lcy", latency);
//		ModelAccess.set(fac, model, "channel_jit", jitter);
//		ModelAccess.set(fac, model, "ChannelType", type);
//		ModelAccess.set(fac, model, "nInterfaceTypes", getNofInterfaceTypes());
//		
//	}
	
	public void setNofInterfacesOfType(int[] interfaces){
		interfacesOftype=interfaces;
	}
	
	public int[] getNofInterfacesOfType(){
		return interfacesOftype;
	}
	
	public int getNofInterfacesOfType(int type){
		if(type>=interfacesOftype.length)
			return 0;
		else
			return interfacesOftype[type];
	}
	
	public void setHysteresis(int value){
		hysteresis = value;
	}	
	
	public int getHysteresis(){
		return hysteresis;
	}
	
	public void setCostImportance(int value){
		cost_imp=value;
	}
	
	public int getCostImportance(){
		return cost_imp;
	}
	public NetworkGenerator clone(){  
		try {
			NetworkGenerator ng_clone = new NetworkGenerator();
			ng_clone.setHysteresis(getHysteresis());
			ng_clone.setCostImportance(getCostImportance());
			for(Network n: getNetworks()){
				ng_clone.addNetwork(n.clone());
			}
			return ng_clone;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
	} 
}
