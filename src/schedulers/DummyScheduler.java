package schedulers;

import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class DummyScheduler extends Scheduler {

	public DummyScheduler(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
	}

	@Override
	protected void calculateInstance_internal(String logfile) {
		// do nothing
	}

	@Override
	public String getType() {
		return "empty";
	}

}
