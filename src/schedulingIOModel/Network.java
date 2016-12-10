package schedulingIOModel;
import java.io.Serializable;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import ToolSet.RndInt;

/**
 * 
 * @author Tobias Rückelt
 * 
 * 
 *
 */


public class Network implements Serializable, Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3878823537551412162L;
	private int meanChunks=0;	
	private int type =1;		//1 = wifi; 2=cellular; ..?
	private int latency = 0;	//the lower the better. 
	private int jitter = 0;		//the lower the better
	private int cost = 0;
	
	Vector<Integer> capacity = new Vector<Integer>();
	
	/**
	 * initialize a network with #slots token buckets and capacity of "meanchunks"
	 * @param slots
	 * @param meanChunks
	 */
	
	public Network(){
		
	}
	
	public Network(int slots, int meanChunks){
		this.meanChunks=meanChunks;
		for(int i=0;i<slots; i++){
			capacity.add(meanChunks);
		}
	}
	
	//each network gets a unique ID
	//unique identifier enables shrinking of problem and later reconstruction (decomposition heuristic) 
	static final AtomicInteger NEXT_ID = new AtomicInteger(0);
    int id = NEXT_ID.getAndIncrement();

    //set ID externally
    public void setId(int id) {
    	this.id = id;
    }
    public int getId() {
         return id;
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
	
	public void setCapacity(Vector<Integer> capacity){
		this.capacity = capacity;
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

	public static Network getWiFi(int slots, int meanChunks, int delay){
		Network wifi = new Network(slots*1/2, meanChunks);
		wifi.addRampUp(slots/4);
		wifi.addRampDown(slots/4);
		wifi.delay(delay);
		wifi.setType(1);
		wifi.setCost(RndInt.get(1,3));
		wifi.setJitter(RndInt.get(4, 8));
		wifi.setLatency(RndInt.get(1, 4));
		return wifi;
	}
	
	public static Network getCellular(int slots, int meanChunks){
		int type = RndInt.get(2, 4);	//2G, 3G, 4G
		Network cell = new Network(slots, type*meanChunks/2);
		cell.setType(2);	//set cellular
		
		cell.setCost(2+type*RndInt.get(2, 3)/2);	//higher technology, more expensive
		cell.setJitter((5-type)*RndInt.get(1, 3));
		cell.setLatency((5-type)*RndInt.get(3, 5));
		return cell;
	}
	
	
	/**
	 * Model for position uncertainty: (vehicle drove faster or slower)
	 * stretches or shrinks the slots of capacity vector (relating to vehicle speed in comparison to estimated speed)
	 * @param slotIndexes - for each time slot, the index of the corresponding time slot which covers the right value based on speed  
	 * 
	 */
	public void addPositionUncertainty(Vector<Integer> slotIndexes) {
		int len = capacity.size();
//		System.out.println("cap_old="+capacity);
		Vector<Integer> cap_tmp= new Vector<Integer>();
		
		//set capacity values
		for(int t=0;t<len; t++ ){
			if(slotIndexes.get(t)<len){
				cap_tmp.add(capacity.get(slotIndexes.get(t)));
			}
		}
		//fill up if desired length not reached yet (vehicle drove faster in average)
		while(cap_tmp.size()<len){
			cap_tmp.add(capacity.get(capacity.size()-1));	
		}
		capacity=cap_tmp;
//		System.out.println("cap_new="+capacity);
	}
	
	/**
	 * Model for Network Uncertainty:
	 * 1. Variation of range of networks. If a network has a start and an end, these could be varied. Both extended or reduced by random parameter.
	 * 2. Variation of characteristics. Generate random "load". With high load, throughput decreases, latency and jitter increases. vice versa.
	 * 
	 * Fixed characteristics like antenna of the car should already be included in the initial prediction
	 * 
	 * @param w_char weight to change characteristics (throughput, jitter, latency) between [0..1]
	 * @param w_range weight to change network range (WiFi-only)
	 */
	public void addNetworkUncertainty(float w_char, float w_range){
		addRangeUncertainty(w_range);
		addCharacteristicsUncertainty(w_char);		
	}


	/**
	 * change latency, jitter of network and characteristics of each time slot
	 * w_char is the weight for the change
	 * @param w_char
	 */
	private void addCharacteristicsUncertainty(float w_char) {
		//throughput
		int filtered=0;
		float alpha = (float)0.5;
		for(int t=0;t<capacity.size();t++){
			int c = capacity.get(t);
			if(c>0){
				int rnd = (int) ((float)RndInt.getGauss(-c, c)*w_char);
				filtered = (int)((float)filtered*(1-alpha)+rnd*alpha);
//				System.out.print(filtered+", ");
				int newCap = Math.max(0, c+filtered);
				capacity.set(t, newCap);
			}
		}
		
		//latency and jitter
		latency=Math.max(0,(int) (latency+RndInt.getGauss(-5, 5)*w_char));
		jitter =Math.max(0,(int) (jitter+ RndInt.getGauss(-5, 5)*w_char));
		
	}

	private void addRangeUncertainty(float w_range){
		//vary range. This is only applicable at WiFis
				if(type==1){
					int varyRange = (int) (((float)RndInt.getGauss(-1000, 1000))*w_range/1000.0);
//					System.out.println("Vary range by "+varyRange);
					//find slot to insert/remove items in capacity vector
					//TODO: this works only for the generated WiFi throughput characteristics.
					int max = 0;
					int index =0;
					for(int t=0; t<capacity.size(); t++){
						if(max<capacity.get(t)){
							max=capacity.get(t);
							index =t;
						}
					}
					
					if(varyRange>0){
					//extend network range
						
						//insert new items
						for(int i = 0;i<varyRange;i++){
							capacity.add(index, max);
						}
						
						//remove elements at the start and end to make space for new ones
						for(int v=0;v<varyRange;v++){
							if(v<varyRange/2){
								capacity.remove(0);
							}else{
								capacity.remove(capacity.size()-1);
							}
						}

					}else{
					//reduce network range
						varyRange=-varyRange;	//make it positive for easier application
						int varyRange_tmp=0;
						//remove items
						for(int i = 0;i<varyRange;i++){
							if(index<capacity.size()){
								capacity.remove(index);
								varyRange_tmp++;
							}
						}
						
						//add elements at start and end
						for(int v=0;v<varyRange_tmp;v++){
							if(v<varyRange_tmp/2){
								capacity.add(0, capacity.get(0));	//insert first element again
							}else{
								capacity.add(capacity.size()-1, capacity.get(capacity.size()-1));	//insert last element
							}
						}
					}
				}
	}
	
	public float smapeNetwork(Network predicted){
		float sum_tp=0;
		for(int t=0;t<capacity.size(); t++){
			sum_tp+=smape(capacity.get(t), predicted.getCapacity().get(t));
		}
		float smape_tp = sum_tp/capacity.size();
		float smape_lcy = smape(getLatency(), predicted.getLatency());
		float smape_jit = smape(getJitter(), predicted.getJitter());
	
		
		return (float) (0.2*smape_lcy + 0.2*smape_jit + 0.6*smape_tp);
	}
	
	private float smape(int actual, int predicted){
		if(actual==predicted) return 0;
		float smape = ((float)2*Math.abs(predicted-actual))/(Math.abs(predicted)+Math.abs(actual));
//		System.out.println("smape = "+smape);
//		System.out.flush();
		return smape;
		
	}
	
	public Network clone(){  
		try {
			Network n = new Network();
			n.setCapacity(deepCopy(capacity));
			n.jitter=jitter;
			n.cost=cost;
			n.id=id;
			n.latency=latency;
			n.meanChunks=meanChunks;
			n.type=type;
			return n;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
	} 
	
	private Vector<Integer> deepCopy(Vector<Integer> v){
		Vector<Integer> copy= new Vector<Integer>();
		for(int i:v){
			copy.add(new Integer(i));
		}
		return copy;
	}
	
}
