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
		return calcVio(flow, ng.getNetworks().get(n))<0;
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "GreedyOnlineOpp";
	}

}
