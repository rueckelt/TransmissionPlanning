
/**
 * is not used
 * might be used in the future for data I/O
 * 
 * Not compartible with latest version of scheduler
 * 
 * @author QZ61P8
 *
 */
public class ShedDataIO {

	private int nChannels = 2;
	private int nRequests = 4;
	private int nInterfaceTypes = 2;
	private int nTime = 21;

	private int nImportanceTypes = 4;
//	final int IMP_DEADLINE = 1;
//	final int IMP_PREFSTART = 2;
//	final int IMP_MIN_TIME_CHUNKS = 3;
//	final int IMP_MAX_TIME_CHUNKS = 4;

	private int[][] availBW = new int[nChannels][nTime];
	private int[] channel_cost = new int[nChannels];
	private int[] nClientInterfaces = new int[nInterfaceTypes];
	

	// request spec
	private int[] chunksize = new int[nRequests];
	private int[] nChunks = new int[nRequests];
	private int[] deadline = new int[nRequests];
	private int[] prefStartTime = new int[nRequests];
	private int[] minTimeBetweenChunks = new int[nRequests];
	private int[] maxTimeBetweenChunks = new int[nRequests];
	

	private int[] stretch_max = new int[nRequests];
	private int[] compress_min = new int[nRequests];
	
	private int[][] importance = new int[nImportanceTypes][nRequests];
	private int[] userImportance = new int[nRequests];
	private int userWTP = 2;

	
	public void setExample(){
		nChannels = 2;
		nRequests = 4;
		nInterfaceTypes = 2;
		nTime = 30;
		
		int[] availBW_cellular 	= new int[]{2, 2, 1, 2, 2, 1, 1, 1, 3, 2, 2, 2, 1, 2, 2, 1, 1, 1, 3, 2, 2, 0, 0 ,0, 0, 0 ,0, 0, 0 ,0};
		int[] availBW_wifi 		= new int[]{0, 0, 3, 4, 5, 4, 3, 1, 0, 0 ,0, 0, 3, 4, 5, 4, 3, 1, 0, 0 ,0, 0, 0 ,0, 0, 0 ,0, 0, 0 ,0};	
		availBW = new int[][] 
			{	availBW_cellular,
				availBW_wifi
			};
		channel_cost = new int[] {5, 2};
		nClientInterfaces = new int[] {1, 2};
		
		chunksize = new int[] {1, 1, 2, 2};
		nChunks = new int[] {21, 4, 10, 8};
		deadline = new int[] {20, 5, 7, 10};
		prefStartTime = new int[] {0, 2, 0, 0};
		
		minTimeBetweenChunks = new int[] {1, 0, 1, 0};
		stretch_max = new int[] {2, 1, 4, 1};
		maxTimeBetweenChunks = new int[] {2, 1000, 4, 1000};
		compress_min = new int[] {1, 1, 1, 1};
		
		importance = new int[][] {
						{10, 5, 6, 1},	//deadline
						{1, 1, 1, 1},
						{10, 1, 1, 1},
						{10, 1, 8, 1}
					};
		
		userImportance = new int[] {5, 5, 3, 2};
		userWTP = 2;
		
	}
	
	public String getDataString(){
		return new DataStringBuilder()
		.add("nChannels", nChannels)		//Anzahl der Werte(Arraylaengen)
		.add("nRequests", nRequests)
		.add("nClientInterfaces", nClientInterfaces)
		.add("nTime", nTime)
		.add("availBW", availBW)			//Arrays
		.add("", nInterfaceTypes)
		.add("channel_cost", channel_cost)
		.add("chunksize", chunksize)
		.add("nChunks", nChunks)
		.add("deadline", deadline)
		.add("prefStartTime", prefStartTime)
		.add("minTimeBetweenChunks", minTimeBetweenChunks)
		.add("stretch_max", stretch_max)
		.add("maxTimeBetweenChunks", maxTimeBetweenChunks)
		.add("compress_min", compress_min)
		.add("importance", importance)
		.add("userImportance", userImportance)
		.add("userWTP", userWTP)
		.toString();
	}
	
	
	public int getnChannels() {
		return nChannels;
	}
	public void setnChannels(int nChannels) {
		this.nChannels = nChannels;
	}
	public int getnRequests() {
		return nRequests;
	}
	public void setnRequests(int nRequests) {
		this.nRequests = nRequests;
	}
	public int getnTime() {
		return nTime;
	}
	public void setnTime(int nTime) {
		this.nTime = nTime;
	}
	public int getnImportanceTypes() {
		return nImportanceTypes;
	}
	public void setnImportanceTypes(int nImportanceTypes) {
		this.nImportanceTypes = nImportanceTypes;
	}
	public int[][] getAvailBW() {
		return availBW;
	}
	public void setAvailBW(int[][] availBW) {
		this.availBW = availBW;
	}
	public int[] getChannel_cost() {
		return channel_cost;
	}
	public void setChannel_cost(int[] channel_cost) {
		this.channel_cost = channel_cost;
	}
	public int[] getnChunks() {
		return nChunks;
	}
	public void setnChunks(int[] nChunks) {
		this.nChunks = nChunks;
	}
	public int[] getDeadline() {
		return deadline;
	}
	public void setDeadline(int[] deadline) {
		this.deadline = deadline;
	}
	public int[] getPrefStartTime() {
		return prefStartTime;
	}
	public void setPrefStartTime(int[] prefStartTime) {
		this.prefStartTime = prefStartTime;
	}
	public int[] getMinTimeBetweenChunks() {
		return minTimeBetweenChunks;
	}
	public void setMinTimeBetweenChunks(int[] minTimeBetweenChunks) {
		this.minTimeBetweenChunks = minTimeBetweenChunks;
	}
	public int[] getMaxTimeBetweenChunks() {
		return maxTimeBetweenChunks;
	}
	public void setMaxTimeBetweenChunks(int[] maxTimeBetweenChunks) {
		this.maxTimeBetweenChunks = maxTimeBetweenChunks;
	}
	public int[][] getImportance() {
		return importance;
	}
	public void setImportance(int[][] importance) {
		this.importance = importance;
	}
	public int[] getUserImportance() {
		return userImportance;
	}
	public void setUserImportance(int[] userImportance) {
		this.userImportance = userImportance;
	}
	public int getUserWTP() {
		return userWTP;
	}
	public void setUserWTP(int userWTP) {
		this.userWTP = userWTP;
	}

	public int[] getnClientInterfaces() {
		return nClientInterfaces;
	}

	public void setnClientInterfaces(int[] nClientInterfaces) {
		this.nClientInterfaces = nClientInterfaces;
	}

}
