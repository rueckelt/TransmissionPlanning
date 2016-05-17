package schedulers;

import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class GreedyOnlineOpppertunisticScheduler extends GreedyOnlineScheduler {

	public GreedyOnlineOpppertunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg) {
		super(ng, tg);
	}
	
	protected boolean scheduleDecision(Flow flow, int n) {
		return calcVio(flow, ng.getNetworks().get(n))<schedule_decision_limit;
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return new String("GreedyOnlineOpp_"+schedule_decision_limit).replace("-", "m");
	}

}
