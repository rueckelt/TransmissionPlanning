import java.io.Serializable;
import java.util.Vector;

/**
 * 
 * @author Tobias Rückelt
 * 
 * 
 *
 */


public class Network implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3878823537551412162L;
	private int meanChunks=0;	
	private int type =1;		//1 = wifi; 2=cellular; ..?
	private int latency = 0;
	private int jitter = 0;
	private int cost = 0;
	
	Vector<Integer> capacity = new Vector<Integer>();
	
	public Network(int slots, int meanChunks){
		this.meanChunks=meanChunks;
		for(int i=0;i<slots; i++){
			capacity.add(meanChunks);
		}
	}
	
	public void addRampUp(int slots){
		for(int i=1; i<=slots; i++){
			capacity.add(0, meanChunks*(slots-i)/slots);
		}
	}
	
	public void addRampDown(int slots){
		for(int i=1; i<=slots; i++){
			capacity.add(capacity.size(), meanChunks*(slots-i)/slots);
		}
	}
	
	public void delay(int slots){
		for(int i=0;i<slots;i++){
			capacity.add(0, 0);
		}
	}
	
	public int getSlots(){
		return capacity.size();
	}
	
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

	public static Network getWiFi(int slots, int meanChunks, int delay){
		Network wifi = new Network(slots*1/2, meanChunks);
		wifi.addRampUp(slots/4);
		wifi.addRampDown(slots/4);
		wifi.delay(delay);
		wifi.setType(1);
		wifi.setCost(3);
		wifi.setJitter(8);
		wifi.setLatency(2);
		return wifi;
	}
	
	public static Network getCellular(int slots, int meanChunks){
		Network cell = new Network(slots, meanChunks);
		cell.setType(2);
		cell.setCost(10);
		cell.setJitter(6);
		cell.setLatency(8);
		return cell;
	}
	
	
	
}
