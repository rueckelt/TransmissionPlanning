
public abstract class Scheduler {
	
	/**
	 * TODO: Different strategies
	 * 
	 */
	
	private NetworkGenerator ng; 
	private TrafficGenerator tg;
	
	private CostFunction cf;
	private long runtime =0;
	private int[][][] schedule_f_t_n;
	
	public Scheduler(NetworkGenerator ng, TrafficGenerator tg){
		this.ng=ng;
		this.tg=tg;
		this.cf=new CostFunction(ng, tg);
	}
	

	/**
	 * 
	 * @param ng
	 * @param tg
	 * @param logfile path
	 * 
	 * calculates the schedule and stores it in internal variable schedule_f_t_n
	 */
	protected abstract void calculateInstance_internal(NetworkGenerator ng, TrafficGenerator tg, String logfile);
	
	
	public int[][][] getSchedule(){
		return schedule_f_t_n;
	}
	
	public int getCost(){
		return cf.costTotal(getSchedule());
	}
	
	public long getRuntimeNs(){
		return runtime;
	}
	
	/**
	 * Method called from outside to start calculation of schedule
	 * TODO: Logging
	 * @param ng
	 * @param tg
	 * @param logfile
	 */
	public void calculateInstance(NetworkGenerator ng, TrafficGenerator tg, String logfile){
		startTimer();
		calculateInstance_internal(ng, tg, logfile);
		stopTimer();
//		log();
	}
	
	private void startTimer(){
		runtime=System.nanoTime();
	}
	
	private void stopTimer(){
		runtime=System.nanoTime()-runtime;
	}
	
}
