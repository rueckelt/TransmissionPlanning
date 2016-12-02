package schedulers;

import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class GreedyOnlineOpppertunisticScheduler extends GreedyOnlineScheduler {

	public GreedyOnlineOpppertunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg) {
		super(ng, tg);
	}
	
	@Override
	protected boolean scheduleDecision(int f, int n, int t) {
//		System.out.println("DECISION_VIO "+calcVio(f,n));
		if(NEW_RATING_ESTIMATOR){
			int calcVio = calcVio(f,n);
			int statefulReward = cs.getStatefulReward(f, t);
			int tp=cs.getTimeMatch(f, t)/getAvMinTp(tg.getFlows().get(f));
			int sum = calcVio+statefulReward+tp;
			//System.out.println("Oppertunistic: calcVio="+calcVio+", statefulR="+statefulReward+", tp="+tp+", timeMatch="+cs.getTimeMatch(f, t)+", sum="+sum+", < limit? ="+schedule_decision_limit);
		return  sum	< schedule_decision_limit;
		}else
			return calcVio(f, n)<schedule_decision_limit;
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return new String("GreedyOnlineOpp_"+schedule_decision_limit+(NEW_RATING_ESTIMATOR?"_H2":"")).replace("-", "m");
	}

}
