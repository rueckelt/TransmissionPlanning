package schedulingIOModel;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import ToolSet.RndInt;


public class Flow implements Serializable, Cloneable{
	
	//default flow = now special requirements, no chunks
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5197541708853992326L;
	private int chunks=0;
	private int deadline=100000;
	private int startTime=0;
	
	//at least n chunks in t time slots ### lower throughput limit
	private int windowMin=100000;
	private int chunksMin=1;
	//at most n chunks in t time slots (data is not prduced faster/ slow transmitter) ## upper throughput limit
	private int windowMax=1;
	private int chunksMax=100000;
	
	private int reqJitter=10;		//low value if low jitter required
	private int reqLatency=10;		//low value if low latency required
	
	//importance values
	private int impDeadline=0;
	private int impStartTime=0;
	private int impThroughputMin=0;
	private int impThrouthputMax=0;
	private int impUnsched=3;
	private int impLatency=0;
	private int impJitter=0;
	
	private int impUser=10;
	private String flowName="flow";

	//each flow gets a unique ID
	static AtomicInteger NEXT_ID = new AtomicInteger(0);
    int id = NEXT_ID.getAndIncrement();
    
    private int index=-1;	//not set
    public Flow() {
    	////System.out.println(id);
    	setIndex(id);
    }
    //set ID externally; use -1 to create new id
    public void setId(int id) {
    	if(id<0){
    		id=NEXT_ID.getAndIncrement();
    	}else{
    		this.id = id;
    	}	
    }
    
    public void setIndex(int i){
    	index=i;
    }
    public int getIndex(){
    	return index;
    }
    
    public int getId() {
         return id;
    }


	public int getTokens() {
		return chunks;
	}


	public void setTokens(int tokens) {
		this.chunks = tokens;
	}


	public int getDeadline() {
		return deadline;
	}


	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}


	public int getStartTime() {
		return startTime;
	}


	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}


	public int getWindowMin() {
		return windowMin;
	}


	public void setWindowMin(int windowMin) {
		this.windowMin = windowMin;
	}


	public int getTokensMin() {
		return chunksMin;
	}


	public void setTokensMin(int tokensMin) {
		this.chunksMin = tokensMin;
	}


	public int getWindowMax() {
		return windowMax;
	}


	public void setWindowMax(int windowMax) {
		this.windowMax = windowMax;
	}


	public int getTokensMax() {
		return chunksMax;
	}


	public void setTokensMax(int tokensMax) {
		this.chunksMax = tokensMax;
	}


	public int getReqLatency() {
		return reqLatency;
	}


	public void setReqLatency(int reqLatency) {
		this.reqLatency = reqLatency;
	}


	public int getReqJitter() {
		return reqJitter;
	}


	public void setReqJitter(int reqJitter) {
		this.reqJitter = reqJitter;
	}


	public int getImpDeadline() {
		return impDeadline;
	}


	public void setImpDeadline(int impDeadline) {
		this.impDeadline = impDeadline;
	}


	public int getImpStartTime() {
		return impStartTime;
	}


	public void setImpStartTime(int impStartTime) {
		this.impStartTime = impStartTime;
	}


	public int getImpThroughputMin() {
		return impThroughputMin;
	}


	public void setImpThroughputMin(int impThroughputMin) {
		this.impThroughputMin = impThroughputMin;
	}


	public int getImpThroughputMax() {
		return impThrouthputMax;
	}

//	Hard constraint --> does not exist anymore
//	public void setImpThrouthputMax(int impThrouthputMax) {
//		this.impThrouthputMax = impThrouthputMax;
//	}


	public int getImpUnsched() {
		return impUnsched;
	}


	public void setImpUnsched(int impUnsched) {
		this.impUnsched = impUnsched;
	}


	public int getImpLatency() {
		return impLatency;
	}


	public void setImpLatency(int impLatency) {
		this.impLatency = impLatency;
	}


	public int getImpJitter() {
		return impJitter;
	}


	public void setImpJitter(int impJitter) {
		this.impJitter = impJitter;
	}
	
	
	public int getImpUser() {
		return impUser;
	}


	public void setImpUser(int impUser) {
		this.impUser = impUser;
	}

	public boolean isActive(int t){
		return t>=startTime && t<=deadline;
	}

	/**
	 * Creates data flow with high minimum throughput importance
	 * Throughput might be higher (higher voice quality) but importance for scheduling
	 * these additional tokens is lower
	 * start time and deadline are important because its real-time traffic
	 * 
	 * @param startTime
	 * @param deadline
	 * @return
	 */
	public static Flow LiveStram(int startTime, int deadline, int tokens){
		Flow liveStram = new Flow();
		int chunks_per_slot = tokens/(deadline-startTime);
		
		liveStram.setStartTime(startTime);
		liveStram.setDeadline(deadline);
		liveStram.setTokens(tokens);
		
		//lower throughput window
		liveStram.setWindowMin(1);
		liveStram.setTokensMin(chunks_per_slot*3/4);	//should deliver 75% of data to receive full QoS
		liveStram.setImpThroughputMin(25);//50		//should deliver all tokens --> high priority (10? more? what should be max?)
		liveStram.setImpDeadline(10); 			// call is over after deadline --> high prio
		
		liveStram.setReqJitter(4);
		liveStram.setReqLatency(4);			//low latency, low jitter
		
		//upper throughput window
		liveStram.setWindowMax(1);
		liveStram.setTokensMax(chunks_per_slot);		//higher throughput possible: higher voice quality!
		liveStram.setImpStartTime(5);			//data not existent before call --> high prio
		
		liveStram.setImpUnsched(12 + RndInt.get(0, 5));//8		//lower priority for unscheduled than for deadline violation
		liveStram.setImpJitter(4 + RndInt.get(-1, 1)); //8			//jitter and latency are important
		liveStram.setImpLatency(4 + RndInt.get(-1, 1)); //8
		
		liveStram.setImpUser(7 + RndInt.get(-1, 2));//9
		liveStram.setFlowName("Conversational");
		
		return liveStram;
	}
	public static Flow BufferableStream(int startTime, int length, int tokens){
		Flow stream = new Flow();
		int chunks_per_slot=tokens/length;	//stream quality may vary
		
		stream.setStartTime(startTime);
		stream.setDeadline(startTime+length);
		stream.setTokens(tokens);
		
		//relaxed window
		int win_size = 10 + length/RndInt.get(3, 8);		//allowed to be bursty (large window): window is at least 10 slots wide
		stream.setWindowMin(win_size);
		stream.setTokensMin(win_size*chunks_per_slot/2);	//at least one half of the data must pass (low quality video) more data scales quality
		stream.setImpThroughputMin(12+ RndInt.get(0,4));		//straight minimum throughput limit
		stream.setImpDeadline(4+ RndInt.get(-2, 2)); 			
		
		stream.setImpStartTime(1+ RndInt.get(0, 4));			//later start time is ok 	
		
		stream.setImpUnsched(6+ RndInt.get(0,5));//5			//unscheduled chunks may adapt video quality
		
		stream.setImpUser(4+ RndInt.get(0, 2));//7
		stream.setFlowName("BufferableStream");
		
		return stream;
	}
	
	//a burst of interaction
	public static Flow Interactive(int startTime, int tokens){
		Flow userRequest = new Flow();
		int deadline = startTime + RndInt.getGauss(2,25); //interaction "bursts" covering 2-25 time slots
		
		userRequest.setStartTime(startTime);
		userRequest.setDeadline(deadline);
		userRequest.setTokens(tokens);	
		
		userRequest.setImpUnsched(10+ RndInt.get(-3, 3));	//15		//all chunks should be scheduled within the deadline		
		userRequest.setImpUser(8+ RndInt.get(0, 5));//7
		userRequest.setImpDeadline(5+ RndInt.get(-1, 1));
		userRequest.setImpStartTime(5+ RndInt.get(-1, 1));
		userRequest.setImpLatency(2+RndInt.get(0,1));
		
		//we model interactive traffic as burst of interactions that
		//must be transmitted quite steadily
		int win_size=3;
		int chunks_per_win=Math.max(1, win_size*tokens/(deadline-startTime));
		userRequest.setWindowMin(win_size);
		userRequest.setTokensMin(chunks_per_win);	
		userRequest.setImpThroughputMin(25+ RndInt.get(-1, 1));		//soft minimum throughput limit
		userRequest.setWindowMax(win_size);
		userRequest.setTokensMax((int)(chunks_per_win*1.5));
		userRequest.setReqLatency(6+RndInt.get(0,1));
		
		userRequest.setFlowName("Interactive");
		return userRequest;
	}
	
	
	public static Flow Background(int tokens, int deadline){
		int with_deadline=RndInt.get(0,1);		//is 1 for 50% of flows
		Flow background = new Flow();
		background.setTokens(tokens);
		background.setImpUnsched(3+RndInt.get(0, 2) +with_deadline*2);			//chunks should be scheduled with low priority
		
		background.setImpUser(RndInt.get(2, 4)+with_deadline*2);
		
		//set a weak deadline
		background.setDeadline(deadline);
		background.setImpDeadline(2*with_deadline);
		String s = (with_deadline>0)?" (DL)":"";
		background.setFlowName("Background"+s);
		return background;
	}
	
	public Flow clone(){  
		try {
			Flow f = new Flow();
			f.chunks=chunks;
			f.chunksMax=chunksMax;
			f.chunksMin=chunksMin;
			f.deadline=deadline;
			f.id=id;
			f.index=index;
			f.impDeadline=impDeadline;
			f.impJitter=impJitter;
			f.impLatency=impLatency;
			f.impStartTime=impStartTime;
			f.impThroughputMin=impThroughputMin;
			f.impThrouthputMax=impThrouthputMax;
			f.impUnsched=impUnsched;
			f.impUser=impUser;
			f.reqJitter=reqJitter;
			f.reqLatency=reqLatency;
			f.startTime=startTime;
			f.windowMax=windowMax;
			f.windowMin=windowMin;
			f.flowName=flowName;
			return f;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
	}

	public String getFlowName() {
		return flowName;
	}

	private void setFlowName(String flowName) {
		this.flowName = flowName;
	} 

	public String toString(){
		String s = flowName;
		
		s+="\tid: " + id + " index: " + index + " st "+startTime+"\tdl "+deadline+"\ttokens "+chunks+"\twin_min "+windowMin+"\ttok_min "+chunksMin;
		
		return s;
		
		
	}
	
	
	public static void setNextId(AtomicInteger atomInt) {
		NEXT_ID = atomInt;
	} 

		
}
