package schedulingIOModel;
import java.io.Serializable;
import java.util.Vector;

import toolSet.RndInt;

/**
 * 
 * @author Tobias Rückelt
 * 
 */

public class Network implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3878823537551412162L;
	
	public enum NetworkType {
		NONE , WIFI , CELLULAR
	}
	
	private NetworkType networkType = NetworkType.WIFI;
	private int type =1;		//1 = wifi; 2=cellular; ..?
	
	private int meanChunks=0;
	private int latency = 0;
	private int jitter = 0;
	private int cost = 0;
	
	Vector<Integer> capacity = new Vector<Integer>();
	
	/**
	 * initialize a network with #slots token buckets and capacity of "meanchunks"
	 * @param slots
	 * @param meanChunks
	 */
	public Network(int slots, int meanChunks){
		this.meanChunks=meanChunks;
		for(int i=0;i<slots; i++){
			capacity.add(meanChunks);
		}
	}
	
	/**
	 * add ramp of n slots as first slots of bucket configuration
	 * @param slots
	 */
	public void addRampUp(int slots){
		for(int i=1; i<=slots; i++){
			capacity.add(0, meanChunks*(slots-i)/slots);
		}
	}
	
	/**
	 * add a ramp with size of n slots at the end of the available buckets
	 * @param slots
	 */
	public void addRampDown(int slots){
		for(int i=1; i<=slots; i++){
			capacity.add(capacity.size(), meanChunks*(slots-i)/slots);
		}
	}
	
	/**
	 * add n empty slots before current configuration
	 * @param slots
	 */
	public void delay(int slots){
		for(int i=0;i<slots;i++){
			capacity.add(0, 0);
		}
	}
	
	public Vector<Integer> getCapacity(){
		return capacity;
	}
	
	public int getSlots(){
		return capacity.size();
	}
	
	/**
	 * resize network duration by adding empty buckets at the end or removing the last ones
	 * @param size
	 */
	public void setSlots(int size){
		//too long
		while(capacity.size()>size){
			capacity.remove(capacity.size()-1);
		}
		//too short
		while(capacity.size()<size){
			capacity.add(capacity.size(), 0);
		}
	}
	
	public String toString(){
		String s = "[";
		boolean first = true;
		for (Integer slot : capacity) {
			if(first){
				first = false;
			}else{
				s+=", ";
			}
			s+=slot;
		}		
		return s+"]";
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
	public int getLatency() {
		return latency;
	}

	public void setLatency(int latency) {
		this.latency = latency;
	}

	public int getJitter() {
		return jitter;
	}

	public void setJitter(int jitter) {
		this.jitter = jitter;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public NetworkType getNetworkType(){
		return this.networkType;
	}
	
	public void setNetworkType(NetworkType type){
		this.networkType = type;
	}

	public static Network getWiFi(int slots, int throughput, int startTime){
		Network wifi = new Network(slots*1/2, throughput);
		wifi.addRampUp(slots/4);
		wifi.addRampDown(slots/4);
		wifi.delay(startTime + RndInt.get(-1, 1));
		wifi.setType(1);
		wifi.setNetworkType(NetworkType.WIFI);
		wifi.setCost(3 + RndInt.get(-1, 1));
		wifi.setJitter(8 + RndInt.get(-1, 1));
		wifi.setLatency(2 + RndInt.get(-1, 2));
		return wifi;
	}

	public static Network getCellular(int slots, int throughput){
		Network cell = new Network(slots, throughput);
		cell.setType(2);
		cell.setNetworkType(NetworkType.CELLULAR);
		cell.setCost(10  + RndInt.get(-3, 3));
		cell.setJitter(6 + RndInt.get(-2, 2));
		cell.setLatency(8 + RndInt.get(-2, 2));
		return cell;
	}
}
