/**
 * 
 */
package schedulers;

import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

/**
 * @author tobias.rueckelt
 *
 */
public class HeuristicAllocator extends HeuristicScheduler {

	/**
	 * @param ng
	 * @param tg
	 */
	public HeuristicAllocator(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param ng
	 * @param tg
	 * @param ngPred
	 * @param tgPred
	 * @param longTermSchedule_logfile
	 */
	public HeuristicAllocator(NetworkGenerator ng, FlowGenerator tg,
			NetworkGenerator ngPred, FlowGenerator tgPred,
			String longTermSchedule_logfile) {
		super(ng, tg, ngPred, tgPred, longTermSchedule_logfile);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see schedulers.Scheduler#calculateInstance_internal(java.lang.String)
	 */
	@Override
	protected void calculateInstance_internal(String logfile) {
		//initialize 


	}

	/* (non-Javadoc)
	 * @see schedulers.Scheduler#getType()
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
