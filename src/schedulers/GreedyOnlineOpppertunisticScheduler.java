package schedulers;

import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class GreedyOnlineOpppertunisticScheduler extends GreedyOnlineScheduler {

	public GreedyOnlineOpppertunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg) {
		super(ng, tg);
	}
	
	protected boolean scheduleDecision(int f, int n, int t) {
//		System.out.println("DECISION_VIO "+calcVio(flow, ng.getNetworks().get(n)));
		if(NEW_RATING_ESTIMATOR){
		return  (
					calcVio(f, n)+
					cs.getStatefulReward(f, t)+
					cs.getTimeMatch(f, t)/getAvMinTp(tg.getFlows().get(f))
				)
				< schedule_decision_limit;
		}else
			return calcVio(f, n)<schedule_decision_limit;
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return new String("GreedyOnlineOpp_"+schedule_decision_limit+(NEW_RATING_ESTIMATOR?"_H2":"")).replace("-", "m");
	}

}
