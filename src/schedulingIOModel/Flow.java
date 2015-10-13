package schedulingIOModel;
import java.io.Serializable;

import toolSet.RndInt;


public class Flow implements Serializable{
	
	//default flow = now special requirements, no chunks
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5197541708853992326L;
	
	public enum FlowType{
		IPCALL, BUFFERABLESTREAM, USERREQUEST, UPDATE
	}
	
	private FlowType flowType;
	private int chunks=0;
	private int deadline=100000;
	private int startTime=0;
	
	//at least n chunks in t time slots ### lower throughput limit
	private int windowMin=100000;
	private int chunksMin=0;
	//at most n chunks in t time slots (data is not produced faster/ slow transmitter) ## upper throughput limit
	private int windowMax=1;
	private int chunksMax=100000;
	
	private int reqJitter=10;		//low value if low jitter required [ms]
	private int reqLatency=10;		//low value if low latency required [ms]
	
	//importance values
	private int impDeadline=1;
	private int impStartTime=1;
	private int impThroughputMin=1;
	private int impThrouthputMax=1;
	private int impUnsched=5;
	private int impLatency=1;
	private int impJitter=1;
	
	private int impUser=1;

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

	public void setImpThrouthputMax(int impThrouthputMax) {
		this.impThrouthputMax = impThrouthputMax;
	}

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
	
	public FlowType getFlowType(){
		return this.flowType;
	}
	
	public void setFlowType(FlowType flowType){
		this.flowType = flowType;
	}

	
	public static Flow IPCall(int startTime, int deadline){
		Flow IPCall = new Flow();
		int chunks_per_slot = 1;
		
		IPCall.setFlowType(FlowType.IPCALL);
		
		IPCall.setStartTime(startTime);
		IPCall.setDeadline(deadline);
		IPCall.setChunks((deadline-startTime)*chunks_per_slot);
		
		IPCall.setWindowMin(1);
		IPCall.setChunksMin(5 + RndInt.get(-1, 0));
		IPCall.setImpThroughputMin(10);						//should deliver all 5 --> high priority (10? more? what should be max?)
		IPCall.setImpDeadline(10); 							// call is over after deadline --> high prio
		
		IPCall.setReqJitter(2);
		IPCall.setReqLatency(2);							//low latency, low jitter
		
		IPCall.setWindowMax(1);
		IPCall.setChunksMax(5 + RndInt.get(0, 1));
		IPCall.setImpThrouthputMax(10000);					//cannot deliver more than 5 --> blocking high priority
		IPCall.setImpStartTime(10);							//data not existent before call --> high prio
		
		IPCall.setImpUnsched(8 + RndInt.get(-1, 1));		//lower priority for unscheduled than for deadline violation
		IPCall.setImpJitter(8 + RndInt.get(-1, 2));			//jitter and latency are important
		IPCall.setImpLatency(8 + RndInt.get(-2, 1));
		
		IPCall.setImpUser(7 + RndInt.get(-1, 2));
		
		return IPCall;
	}
	
	public static Flow BufferableStream(int startTime, int duration){
		Flow stream = new Flow();
		int chunks_per_slot=1;
		
		stream.setFlowType(FlowType.BUFFERABLESTREAM);
		
		stream.setStartTime(startTime);
		stream.setDeadline(startTime+duration);
		stream.setChunks(duration*chunks_per_slot);
		
		//relaxed window
		stream.setWindowMin(15+ RndInt.get(-5, 5));
		stream.setChunksMin(15*chunks_per_slot);
		stream.setImpThroughputMin(5+ RndInt.get(-1, 1));		// soft minimum throughput limit; allowed to be bursty (large window)
		stream.setImpDeadline(7+ RndInt.get(-2, 2)); 			
		
		stream.setImpStartTime(2+ RndInt.get(-1, 4));			//later start time is ok 	
		stream.setImpUnsched(6+ RndInt.get(-1, 1));			//unscheduled chunks are ok for long streams and short scheduling duration
		stream.setImpUser(7+ RndInt.get(-3, 2));
		
		// Pakets have to be in the right order rather than beeing as fast as possible at the destination
		stream.setImpJitter(8);
		stream.setImpLatency(4);
		
		return stream;
	}
	
	/**
	 * 
	 * @param startTime in time slots [100ms]
	 * @param duration in time slots [100ms]
	 * @return Flow object created with the given values and the presets for an user request
	 */
	public static Flow UserRequest(int startTime, int duration){
		Flow userRequest = new Flow();
		int deadline = startTime + duration;					//early deadline, depends on request size
		int chunks_per_slot=3 + RndInt.get(-1, 1);
		
		userRequest.setFlowType(FlowType.USERREQUEST);
		
		userRequest.setStartTime(startTime);
		userRequest.setDeadline(deadline);
		userRequest.setChunks(duration * chunks_per_slot);	
		
		userRequest.setImpUnsched(15+ RndInt.get(-3, 3));		//all chunks must be scheduled		
		userRequest.setImpUser(7+ RndInt.get(-1, 3));
		userRequest.setImpDeadline(8+ RndInt.get(-1, 1));
		
		return userRequest;
	}
	
	public static Flow Update(int chunks){
		Flow update = new Flow();
		
		update.setFlowType(FlowType.UPDATE);
		
		update.setChunks(chunks);
		update.setImpUnsched(4+ RndInt.get(-2, 1));			//chunks should be scheduled with low priority
		
		update.setImpUser(2+ RndInt.get(-1, 1));
		return update;
	}	
}