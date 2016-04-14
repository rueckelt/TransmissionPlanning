package schedulingIOModel;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.sampled.ReverbType;

import schedulers.PriorityScheduler;
import schedulers.Scheduler;
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
	private int chunksMin=0;
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
	private int impUnsched=5;
	private int impLatency=0;
	private int impJitter=0;
	
	private int impUser=1;

	//each flow gets a unique ID
	static final AtomicInteger NEXT_ID = new AtomicInteger(0);
    int id = NEXT_ID.getAndIncrement();

    //set ID externally
    public void setId(int id) {
    	this.id = id;
    }
    
    public int getId() {
         return id;
    }


	public int getChunks() {
		return chunks;
	}


	public void setChunks(int chunks) {
		this.chunks = chunks;
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


	public int getChunksMin() {
		return chunksMin;
	}


	public void setChunksMin(int chunksMin) {
		this.chunksMin = chunksMin;
	}


	public int getWindowMax() {
		return windowMax;
	}


	public void setWindowMax(int windowMax) {
		this.windowMax = windowMax;
	}


	public int getChunksMax() {
		return chunksMax;
	}


	public void setChunksMax(int chunksMax) {
		this.chunksMax = chunksMax;
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
	public static Flow IPCall(int startTime, int deadline){
		Flow IPCall = new Flow();
		int chunks_per_slot = 10 + RndInt.get(-2, 2);
		
		IPCall.setStartTime(startTime);
		IPCall.setDeadline(deadline);
		IPCall.setChunks((deadline-startTime)*chunks_per_slot);
		
		IPCall.setWindowMin(1);
		IPCall.setChunksMin(5 + RndInt.get(-1, 0));
		IPCall.setImpThroughputMin(50);		//should deliver all 5 --> high priority (10? more? what should be max?)
		IPCall.setImpDeadline(10); 			// call is over after deadline --> high prio
		
		IPCall.setReqJitter(4);
		IPCall.setReqLatency(4);			//low latency, low jitter
		
		IPCall.setWindowMax(1);
		IPCall.setChunksMax(chunks_per_slot);		//higher thourthput possible: higher voice quality!
//		IPCall.setImpThrouthputMax(10000);	//cannot deliver more than 5 --> blocking high priority
		IPCall.setImpStartTime(10);			//data not existent before call --> high prio
		
		IPCall.setImpUnsched(4 + RndInt.get(-1, 2));		//lower priority for unscheduled than for deadline violation
		IPCall.setImpJitter(6 + RndInt.get(-1, 1));			//jitter and latency are important
		IPCall.setImpLatency(6 + RndInt.get(-1, 1));
		
		IPCall.setImpUser(9 + RndInt.get(-1, 2));
		
		return IPCall;
	}
	public static Flow BufferableStream(int startTime, int length){
		Flow stream = new Flow();
		int chunks_per_slot=15 + RndInt.get(-5, 5);	//stream quality may vary
		
		stream.setStartTime(startTime);
		stream.setDeadline(startTime+length);
		stream.setChunks(length*chunks_per_slot);
		
		//relaxed window
		int min_win_size = 10 + length/RndInt.get(3, 8);				//allowed to be bursty (large window)
		stream.setWindowMin(min_win_size);
		stream.setChunksMin(min_win_size*chunks_per_slot/2);	//at least half of the data must pass (low quality video) more data scales quality
		stream.setImpThroughputMin(7+ RndInt.get(-1, 1));		// soft minimum throughput limit
		stream.setImpDeadline(7+ RndInt.get(-2, 2)); 			
		
		stream.setImpStartTime(2+ RndInt.get(-1, 4));			//later start time is ok 	
		
		stream.setImpUnsched(5+ RndInt.get(-1, 1));			//unscheduled chunks may adapt video quality
		
		stream.setImpUser(7+ RndInt.get(-3, 2));
		
		return stream;
	}
	
	public static Flow UserRequest(int startTime, int chunks){
		Flow userRequest = new Flow();
		int deadline = startTime + chunks/(20+ RndInt.get(-5, 5));	//early deadline, depends on request size
		
		userRequest.setStartTime(startTime);
		userRequest.setDeadline(deadline);
		userRequest.setChunks(chunks);	
		
		userRequest.setImpUnsched(15+ RndInt.get(-3, 3));			//all chunks must be scheduled		
		userRequest.setImpUser(7+ RndInt.get(-1, 3));
		userRequest.setImpDeadline(8+ RndInt.get(-1, 1));
		userRequest.setImpStartTime(8+ RndInt.get(-1, 1));
		
		return userRequest;
	}
	
	
	public static Flow Update(int chunks){
		Flow update = new Flow();
		update.setChunks(chunks+ RndInt.get(0, 20));
		update.setImpUnsched(4+ RndInt.get(-2, 1));			//chunks should be scheduled with low priority
		
		update.setImpUser(2+ RndInt.get(-1, 1));
		return update;
	}
	
	public Flow clone(){  
		try {
			Flow f = new Flow();
			f.chunks=chunks;
			f.chunksMax=chunksMax;
			f.chunksMin=chunksMin;
			f.deadline=deadline;
			f.id=id;
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
			return f;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
	} 


		
}
