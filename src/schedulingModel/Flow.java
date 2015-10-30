package schedulingModel;

import java.io.Serializable;

import toolSet.RndInt;

public class Flow implements Serializable {

	// default flow = now special requirements, no chunks

	/**
	 * 
	 */
	private static final long serialVersionUID = 5197541708853992326L;

	public enum FlowType {
		IPCALL, BUFFERABLESTREAM, USERREQUEST, UPDATE
	}

	private FlowType flowType;
	private int chunks = 0;
	private int chunksPerSlot = 1;

	private int deadline = 100000;
	private int startTime = 0;

	// at least n chunks in t time slots ### lower throughput limit
	private int windowMin = 100000;
	private int chunksMin = 0;
	// at most n chunks in t time slots (data is not produced faster/ slow
	// transmitter) ## upper throughput limit
	private int windowMax = 1;
	private int chunksMax = 100000;

	private int reqJitter = 10; // low value if low jitter required [~1000ms]
	private int reqLatency = 15; // low value if low latency required [~225ms]

	// importance values
	private int impDeadline = 1;
	private int impStartTime = 1;
	private int impThroughputMin = 1;
	private int impThrouthputMax = 1;
	private int impUnsched = 5;
	private int impLatency = 1;
	private int impJitter = 1;

	private int impUser = 1;

	/**
	 * 
	 * @return The number of chunks the application have to send
	 */
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

	public FlowType getFlowType() {
		return this.flowType;
	}

	public void setFlowType(FlowType flowType) {
		this.flowType = flowType;
	}

	public int getChunksPerSlot() {
		return chunksPerSlot;
	}

	public void setChunksPerSlot(int chunksPerSlot) {
		this.chunksPerSlot = chunksPerSlot;
	}

	/**
	 * 
	 * @param startTime
	 *            in time slots
	 * @param deadline
	 *            in time slots
	 * @return flow object represents an ip call from start time till deadline
	 */
	public static Flow IPCall(int startTime, int deadline) {
		Flow IPCall = new Flow();

		// 1 chunk ~ 1 Byte
		// Measurments: ~3,4 kByte/s = 3400 Byte/s --> 340Byte/slot
		// Twice because of up- and downstream
		IPCall.setChunksPerSlot(340 * 2);

		IPCall.setFlowType(FlowType.IPCALL);

		IPCall.setStartTime(startTime);
		IPCall.setDeadline(deadline);
		IPCall.setChunks((deadline - startTime) * IPCall.getChunksPerSlot());
		IPCall.setWindowMin(1);
		IPCall.setWindowMax(1);
		IPCall.setChunksMin(IPCall.getChunksPerSlot() + RndInt.get(-10,0));
		// should not deliver more than 340 + rndInt
		IPCall.setChunksMax(IPCall.getChunksPerSlot() + RndInt.get(0,10));

		// require jitter ~ 10 ms --> 3 points (9ms)
		IPCall.setReqJitter(3);
		// ITU-T G.114 recommends a maximum of a 150 ms one-way latency
		IPCall.setReqLatency(5);

		// call is over after deadline
		IPCall.setImpDeadline(10);
		// should deliver all the same count of chunks
		IPCall.setImpThroughputMin(10);
		IPCall.setImpThrouthputMax(10);
		// data not existent before call
		IPCall.setImpStartTime(10);
		// lower priority for unscheduled than for deadline violation
		IPCall.setImpUnsched(7);
		// Jitter and latency have high priority
		// Latency > 250ms can be odd for user
		// Jitter > 50ms can result in loss of packets and in false order
		IPCall.setImpJitter(8);
		IPCall.setImpLatency(8);

		IPCall.setImpUser(7);

		return IPCall;
	}

	/**
	 * 
	 * @param startTime
	 *            in time slots
	 * @param duration
	 *            in time slots
	 * @return flow object represents a stream from start time till duration
	 *         ends
	 */
	public static Flow BufferableStream(int startTime, int duration) {
		Flow stream = new Flow();

		// Measurments: 99 pakets/s; 972Byte;
		// 9.9 pakets/slot * 972 Byte = 9622.8 Byte/slot
		stream.setChunksPerSlot(9623);

		stream.setFlowType(FlowType.BUFFERABLESTREAM);

		stream.setStartTime(startTime);
		stream.setDeadline(startTime + duration);
		stream.setChunks(duration * stream.getChunksPerSlot());
		
		// very little chunks to send, because there is only a request for  a song
		// after the request there will only be send the ack's for confirming
		
		// relaxed window
		stream.setWindowMin(15 + RndInt.get(-5, 5));
		stream.setChunksMin(stream.getChunksPerSlot() + RndInt.get(-300, 0));
		stream.setChunksMax(stream.getChunksPerSlot() + RndInt.get(0, 300));

		// ~ 49ms
		stream.setReqJitter(7);
		// ~ 216ms
		stream.setReqLatency(6);

		// soft minimum throughput limit; allowed to be bursty (large window)
		stream.setImpThroughputMin(5);
		stream.setImpDeadline(7);
		// later start time is ok
		stream.setImpStartTime(4);
		// unscheduled chunks are ok for long streams and short scheduling
		// duration
		stream.setImpUnsched(6);
		stream.setImpUser(7);

		// Pakets have to be in the right order rather than beeing as fast as
		// possible at the destination
		// De-jitter buffer against high jitter causes high latency
		// user waits rather for some seconds and stream plays well all the time
		stream.setImpJitter(6);
		stream.setImpLatency(4);

		return stream;
	}

	/**
	 * 
	 * @param startTime
	 *            in time slots [100ms]
	 * @param duration
	 *            in time slots [100ms]
	 * @return Flow object created with the given values and the presets for an
	 *         user request
	 */
	public static Flow UserRequest(int startTime, int duration) {
		Flow userRequest = new Flow();
		// early deadline, depends on request size
		int deadline = startTime + duration;
		
		// 500kBps = 50kB/slot ~ 50000B/slot
		userRequest.setChunksPerSlot(50000);

		userRequest.setFlowType(FlowType.USERREQUEST);

		userRequest.setStartTime(startTime);
		userRequest.setDeadline(deadline);
		userRequest.setChunks(duration * userRequest.getChunksPerSlot());

		// all chunks must be scheduled
		userRequest.setImpUnsched(10);
		userRequest.setImpUser(7);
		userRequest.setImpDeadline(8);

		// Jitter and latency are unimportant

		return userRequest;
	}

	/**
	 * 
	 * @param startTime
	 *            in time slots
	 * @param duration
	 *            duration in time slots the update wants to send data
	 * @return flow object represents an update with size of the given number of
	 *         chunks
	 */
	public static Flow Update(int startTime, int duration) {
		// TODO maybe add a start time
		Flow update = new Flow();

		update.setStartTime(startTime);
		// 1000kB/s = 100kB/slot = 100000B/slot
		update.setChunksPerSlot(100000);
		update.setFlowType(FlowType.UPDATE);
		update.setChunks(duration * update.getChunksPerSlot());

		// chunks should be scheduled with low priority
		update.setImpUnsched(2);
		update.setImpUser(2);
		return update;
	}
}