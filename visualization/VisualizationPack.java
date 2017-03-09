package visualization;

import java.util.Vector;

import schedulers.Scheduler;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.FlowGenerator;



/**
 * 
 * @author DennisHanslik@web.de
 *
 * Networks, traffic and schedulers are needed in different classes and without VisualizationPack we would need
 * to transmit all of them a couple of times. To keep it easy, we now only need to transmit the VisualizationPack-Object
 * 
 */
public class VisualizationPack {

	private NetworkGenerator nets;
	private FlowGenerator traff;
	private Scheduler sched0=null;
	private Scheduler sched1=null;
	private Scheduler sched2=null;
	private Scheduler sched3=null;
	private int[][][] sp = null;

	public VisualizationPack(NetworkGenerator ng, FlowGenerator tg, int[][][] schedule) {
		nets = ng;
		traff = tg;
		setSp(schedule);
	}
	
	public VisualizationPack(NetworkGenerator ng, FlowGenerator tg, Scheduler s0, Scheduler s1, Scheduler s2, Scheduler s3){
		nets = ng;
		traff = tg;
		sched0 = s0;
		sched1 = s1;
		sched2 = s2;
		sched3 = s3;
	}
	
	public VisualizationPack(NetworkGenerator ng, FlowGenerator tg, Vector<Scheduler> sched_vec){
		nets=ng;
		traff=tg;
		
		if(sched_vec!=null && !sched_vec.isEmpty()){
			sched0=sched_vec.get(0);
			if(sched_vec.size()>1){
				sched1=sched_vec.get(1);
			}
			if(sched_vec.size()>2){
				sched2=sched_vec.get(2);
			}
			if(sched_vec.size()>3){
				sched3=sched_vec.get(3);
			}
		}
	}

	public VisualizationPack(NetworkGenerator ng, FlowGenerator tg, Scheduler s0, Scheduler s1, Scheduler s2){
		this(ng, tg, s0, s1, s2, null);
	}

	public VisualizationPack(NetworkGenerator ng, FlowGenerator tg, Scheduler s0, Scheduler s1){
		this(ng, tg, s0, s1, null, null);
	}

	public VisualizationPack(NetworkGenerator ng, FlowGenerator tg, Scheduler s0){
		this(ng, tg, s0, null, null, null);
	}

	public NetworkGenerator getNets() { return nets; }
	public FlowGenerator getTraffic() { return traff; }	
	public Scheduler getScheduler0() { return sched0; }
	public Scheduler getScheduler1() { return sched1; }
	public Scheduler getScheduler2() { return sched2; }
	public Scheduler getScheduler3() { return sched3; }

	public int[][][] getSp() {
		return sp;
	}

	public void setSp(int[][][] sp) {
		this.sp = sp;
	}
}
