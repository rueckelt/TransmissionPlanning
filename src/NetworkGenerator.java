import ilog.opl.IloOplModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.InterfaceAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

/**
 * Firstly creates Network objects defining capacity to each time slot
 * Secondly converts it to network part of the dat-file 
 * @author QZ61P8
 *
 */


public class NetworkGenerator {
	

	private Vector<Network> networks = new Vector<Network>();
	private String content;
	
	public NetworkGenerator(int nofNetworks, int time){
		addNetworks(nofNetworks, time);
	}
	
	private void addNetworks(int nofNetworks, int time){
		Random r = new Random();
		r.setSeed(System.nanoTime());
		
		//initialize automatically
		for (int i=0; i<nofNetworks;i++){
			if(i%10==0){
				networks.add(Network.getCellular(time, 20+r.nextInt(20)));	//cellular available all the time; consant rate (bad model)
			}else{
				int duration = 10+r.nextInt((int)Math.round(Math.sqrt(time))); //availablity at least 10 slots + extra (depends on sqrt of time)
				int delay = r.nextInt(time-duration);
				networks.add(Network.getWiFi(duration, 30 + r.nextInt(50), delay));
			}
		}
	}
	
	
	private int getNofInterfaceTypes(){
		int interfaceTypes=0;
		for(Network network: networks){
			if(network.getType()>interfaceTypes){
				interfaceTypes = network.getType();
			}
		}
		return interfaceTypes;
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
			clientInterfaces+="1";
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
			//set size
			network.setSlots(size);
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setNetworkData(IloOplModel model) {
		//init arrays
		int n=networks.size();	//networks
		int time=getNofTimeSlots();	//time slots
		
		int[][] availBW = new int[n][time];
		int[] type = new int[n];
		int[] cost = new int[n];
		int[] latency = new int[n];
		int[] jitter = new int[n];
		
		//fill arrays
		int i =0;
		for(Network net: networks){
			for(int t=0; t<time; t++){
				if(t<net.capacity.size()){
					availBW[i][t]=net.capacity.get(t);
				}else{
					availBW[i][t]=0;
				}
			}
			System.out.println("availBW mod:\n"+Arrays.deepToString(availBW));
			type[i]=net.getType();
			cost[i]=net.getCost();
			latency[i]=net.getLatency();
			jitter[i]=net.getJitter();
			
			i++;
		}
		
		//set data
		ModelAccess.set(model, "availBW", availBW);
		ModelAccess.set(model, "channel_cost", cost);
		ModelAccess.set(model, "channel_lcy", latency);
		ModelAccess.set(model, "channel_jit", jitter);
		ModelAccess.set(model, "ChannelType", type);
		ModelAccess.set(model, "nInterfaceTypes", getNofInterfaceTypes());
		
	}
	
	
	
	
}
