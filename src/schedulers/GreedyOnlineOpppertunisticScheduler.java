package schedulers;

import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class GreedyOnlineOpppertunisticScheduler extends GreedyOnlineScheduler {

	public GreedyOnlineOpppertunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg) {
		super(ng, tg);
	}
	
	@Override
	protected boolean scheduleDecision(int f, int n, int t) {
//		schedule_decision_limit=1000;
		return oppScheduleDecision(f, n, t);
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return new String("GreedyOnlineOpp_"+schedule_decision_limit+(NEW_RATING_ESTIMATOR?"_H2":"")).replace("-", "m");
	}

}
