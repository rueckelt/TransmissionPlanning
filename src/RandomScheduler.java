import java.util.Collections;
import java.util.Vector;


public class RandomScheduler extends Scheduler{

	public RandomScheduler(NetworkGenerator ng, TrafficGenerator tg) {
		super(ng, tg);
	}

	@Override
	protected int[][][] calculateInstance_internal(String logfile) {
		int[][][] schedule = getEmptySchedule();
		Vector<Flow> flows = tg.getFlows();
		Vector<Network> networks = ng.getNetworks();
		
		Collections.shuffle(flows);
		Collections.shuffle(networks);
		
		
		//############ START OF ALGORITHM #############
		
		
		
		
		return schedule;
	}
	

}
