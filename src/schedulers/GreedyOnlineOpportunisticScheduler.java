package schedulers;

import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class GreedyOnlineOpportunisticScheduler extends GreedyOnlineScheduler {

	public GreedyOnlineOpportunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg) {
		super(ng, tg);
	}
	

	public GreedyOnlineOpportunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg, NetworkGenerator ng_pred, FlowGenerator tg_pred, double alpha, String longTermSpPath, String name_extention) {
		this(ng,tg,ng_pred,tg_pred,alpha,-1,longTermSpPath, name_extention);
	}
	
	public GreedyOnlineOpportunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg, NetworkGenerator ng_pred, FlowGenerator tg_pred, double alpha, int window, String longTermSpPath, String name_extention) {
		this(ng, tg);
		this.tgPred=tg_pred;
		this.ngPred=ng_pred;
		this.TIME_IMPAIRING_WEIGHT=alpha;	//if !=0 this activates time impairing. requires other schedule.
		this.WINDOW=window;
		spLogPath=longTermSpPath;
		this.typeExt="_"+name_extention;
	}
	
	@Override
	public boolean scheduleDecision(int f, int n, int t) {
//		schedule_decision_limit=1000;
		return oppScheduleDecision(f, n, t);
	}


	@Override
	public String getType() {
		return new String("ONS"+getTypeExt()).replace("-", "m").replace('.', '_');	

	}

}
