package schedulingIOModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import ToolSet.PersistentStore;
import ToolSet.RndInt;

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
	
	private final float ALLOWED_ERROR_OFFSET = (float) 0.02;	//part of error/uncertainty model. 
	
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
				int duration = RndInt.get(5, 5+2*(int)Math.round(Math.sqrt(time))); //availablity at least 10 slots + extra (depends on sqrt of time)
				int delay = RndInt.get(duration/10, time-duration-1);
				int tokens = RndInt.get(20,100);
				addNetwork(Network.getWiFi(duration, tokens , delay));
			}
		}
	}
	
	/**
	 * error: gives the SMAPE error which uncertainty should produce. 
	 * 
	 * strength_charactieristics - modifies throughput (capacity), latency, jitter. [0..1]
	 * strength_range - modifies range of wifi networks. 
	 */
	public void addNetworkUncertainty(float error){
		Vector<Network> backup = cloneNetworks(networks);
		
		float strength_charactieristics=error; 
		
		float adapt=1;
		do{
			strength_charactieristics=(float) (strength_charactieristics*(0.5+0.5*adapt)); 
			float strength_range=(float) (strength_charactieristics*getTimeslots()/15);

			networks=cloneNetworks(backup);	//reset networks for each new try
			for(Network net: networks){
				net.addNetworkUncertainty(strength_charactieristics, strength_range);
			}
			adapt = Math.max((float)0.00001, error/getNetworkError(backup));
			//System.out.println("NetGen124: adapt = "+adapt+", strength = "+strength_charactieristics);
		}while(adapt<(1-ALLOWED_ERROR_OFFSET) || 
				adapt>(1+ALLOWED_ERROR_OFFSET));	
	}
	
	private float getNetworkError(Vector<Network> predicted){
		float sum=0;
		for(int n=0; n<networks.size(); n++){
			Network act=networks.get(n);
			Network pred = predicted.get(n);
			sum+=act.smapeNetwork(pred);
//			System.out.println("smape of n="+n+" is "+act.smapeNetwork(pred));
		}
		float result = sum/networks.size();
		//System.out.println("result is "+result);
		return result;
	}
	
	/**
	 * Vehicle driving model. With certain probability, the car drives faster/slower than expected.
	 * For the model, this means that network slots are present longer/shorter.
	 * Therefore we shrink or extend slots
	 * @param strength - influences the distribution of the error. should be between 0 [no error] and 1 [huge error]
	 * @param offset - gives an offset to the mean of the distribution. Car drives faster/slower in average. Use 0 for NO offset; range [0..1]
	 */
	public void addMovementUncertainty(float error){

		//take motorway model for <0.2. Urban leads to higher orders whenever the car stops at least once. stops convergence.
		//else, select randomly.
		boolean motorwayModel=error<0.2 || RndInt.get(0, 1)>0;
//		motorwayModel=false;
//		System.out.println("isMotorway "+motorwayModel);
		float strength=error;
		Vector<Integer> slotChange;
		
		//adapt parameters for uncertainty automatically if the resulting error was out of bounds.
		float adapt=1;
		int counter = 1000;
		do{
			if(counter<0){
				motorwayModel=RndInt.get(0, 1)>0;
			}else{
				counter--;
			}
			
			strength=(float) ((float) strength*(0.7+0.3*adapt));
			float offset=(float) (0.2*strength);
			slotChange = calculateSlotChange(strength, offset, motorwayModel);
//			System.out.println(slotChange);
			adapt = Math.max((float)0.00001,Math.min(2, error/getPositionError(slotChange)));
//			System.out.println("Pos Error is "+getPositionError(slotChange)+", adapt ="+adapt+", strength = "+strength);
		}	
		while(adapt<(1-ALLOWED_ERROR_OFFSET) || 
				adapt>(1+ALLOWED_ERROR_OFFSET));	
		System.out.println("NetworkGenerator: addMovementUncertainty("+error+") ="+slotChange);
		for(Network net: networks){
			net.addPositionUncertainty(slotChange);
		}
	}
	
	/**
	 * SMAPE (Symmetrical mean absolute percentage error) of position. Each shift contributes to the error with 2/T.
	 * @param slotChange
	 */
	public float getPositionError(Vector<Integer> slotChange){
		int sum_change=0;
		
		int prev_value=0;
		for(int value:slotChange){
			sum_change+= 2*Math.abs(prev_value+1-value);
			prev_value = value;
		}
//		System.out.println(sum_change);
		return ((float)sum_change)/getTimeslots();
	}
	
	/**
	 * Vehicle driving model - faster or slower (offset) and random behavior which averages out (strength); 
	 * as well as distributions for motorway and urban
	 * 
	 * @param strength		scales the sigma for the distribution
	 * @param offset		[0..1] select 1 for no offset
	 * @param motorwayModel	set true for motorway, false for urban distribution model
	 * 
	 * motorway model covers normal distribution of speed deviation. Target speed is held for 1-15 time slots, low alpha for low pass filter of speed
	 * urban model has tighter distribution. Target speed is held for 1-10 time slots. High alpha for low pass filter leads to fast reaction and high dynamics
	 * in addition, target speed is 0 with probability of 10%
	 * 
	 */
	private Vector<Integer> calculateSlotChange(float strength, float offset, boolean motorwayModel){
		// make offset a random parameter in range +/-offset
		float rndFloat = (float)RndInt.getGauss(-(int)(1000000*offset), (int)(1000000*offset))/1000000;
		float rndOffset = 1+(float)rndFloat;
//		System.out.println("rndfloat= "+rndFloat+"  rndOffset= "+rndOffset +"   neg_int "+(-(int)(1000*offset)));
		
		//generate speed characteristic over time slots.
		Vector<Integer> slotChange = new Vector<Integer>();
		//motorway: we assume gaussian distribution for deviation from expected speed on trip segment

		int index = 0;
		
		float alpha=0;
		float sum = 1;

		int keep=0;				//counter. keep target speed for n time steps
		float target_speed=1;	//relative value. 1 means equal to expected speed. <1 means slower by this factor
		float speed=1;
		for(int t = 0; t<getTimeslots(); t++ ){
			//set new target speed randomly with scenario-dependent distribution and random duration
			if(keep==0){
				if(motorwayModel){
//					target_speed= (float)RndInt.getGauss(0, 1000, (int)(500*offset), (int)(250*strength))/500;		//fixed offset
					target_speed= (float)RndInt.getGauss(0, 100000, (int)(50000*rndOffset), (int)(25000*strength))/50000;		//random offset
					alpha=(float) 0.2;	//smoother acceleration on motorway
					keep = RndInt.get(1, 15); 
					//System.out.println("\nNew target speed at slot "+t+" is "+target_speed);
				}else{
					//TODO: how to create random numbers with own distribution??
//					target_speed= (float)RndInt.getGauss(0, 1000, (int)(750*offset), (int)(250*strength))/675;	//tighter distribution around target speed
					target_speed= (float)RndInt.getGauss(0, 100000, (int)(75000*rndOffset), (int)(25000*strength))/67500;	//tighter distribution around target speed
					//in addition, stops are more common in city
					if(RndInt.get(0,9)==1){
						target_speed=0;
					}
					alpha=(float) 0.35;	//lots of acceleration in city
					keep = RndInt.get(1, 10);	//faster target speed changes in city 
					//System.out.println("\nNew target speed at slot "+t+" is "+target_speed);
				}
			}
			//simple low pass to approach target speed smoothly (digressive behavior)
			speed = (1-alpha)*speed + alpha*target_speed;
			keep--;
			sum=sum+speed-1;	//adds a value if car is faster; substracts if car is slower.

//			System.out.print(speed+"("+sum+"), ");
			//correct: car too slow. Take same time slot again.
			do{
				if(sum<0.5){
					sum=sum+1;
				}else	
				//correct: car too fast. Skip one time slot.
				if(sum>1.5){
					index=index+2;
					sum=sum-1;
				}
				else{	//as expected. Take next time slot
					index++;
				}
				slotChange.add(index);
			}while(sum<0.5 || sum > 1.5);
		}
			
		
		return slotChange;
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
	private Vector<Network> cloneNetworks(Vector<Network> toClone){
		Vector<Network> nets = new Vector<Network>();
		for(Network net:toClone){
			nets.add(net.clone());
		}
		return nets;
	}
}
