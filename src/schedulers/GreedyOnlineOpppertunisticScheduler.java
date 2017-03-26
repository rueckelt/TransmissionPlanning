package schedulers;

import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class GreedyOnlineOpppertunisticScheduler extends GreedyOnlineScheduler {

	public GreedyOnlineOpppertunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg) {
		super(ng, tg);
	}
	

	public GreedyOnlineOpppertunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg, NetworkGenerator ng_pred, FlowGenerator tg_pred, double alpha, String longTermSpPath, String name_extention) {
		this(ng,tg,ng_pred,tg_pred,alpha,-1,longTermSpPath, name_extention);
	}
	
	public GreedyOnlineOpppertunisticScheduler(NetworkGenerator ng,
			FlowGenerator tg, NetworkGenerator ng_pred, FlowGenerator tg_pred, double alpha, int window, String longTermSpPath, String name_extention) {
		this(ng, tg);
		this.tgPred=tg_pred;
		this.ngPred=ng_pred;
		this.TIME_IMPAIRING_WEIGHT=alpha;	//if !=0 this activates time impairing. requires other schedule.
		this.WINDOW=window;
		spLogPath=longTermSpPath;
		this.name_extention="_"+name_extention+"_";
	}

	private String name_extention="";
	
	@Override
	public boolean scheduleDecision(int f, int n, int t) {
//		schedule_decision_limit=1000;
		return oppScheduleDecision(f, n, t);
	}
	
	protected void calculateInstance_internal(String logfile) {
		if(spLogPath!=null) loadLongTermSp(spLogPath);
		super.calculateInstance_internal(logfile);
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return new String("GreedyOnlineOpp_"+schedule_decision_limit+
				name_extention+"_alpha_"+TIME_IMPAIRING_WEIGHT+"_w_"+WINDOW).replace("-", "m").replace('.', '_');	
				//matlab does not support minus or dots in file names
	}

}
