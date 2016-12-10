package schedulingIOModel;
import schedulers.PriorityScheduler;
import schedulers.Scheduler;
import ToolSet.LogMatlabFormat;


/***
 * 
 * @author Tobias Rueckelt
 * 
 * Cost function reimplements the cost function of the optimization model
 * It shall be used to compare quality different approaches
 * Low values are good
 *
 */

public class CostFunction {
	
	private NetworkGenerator ng;
	private FlowGenerator tg;
	private int minTpAmplifier = 20;
	private static int LcyJitAmplifier = 1;	// this is testing only. MUST BE = 1 for eval!  is not in opt. therefore modify ImpJit/Lcy.
	protected LogMatlabFormat logger = null;
	
	public CostFunction(NetworkGenerator ng, FlowGenerator tg){
		this.ng=ng;
		this.tg=tg;
	}
	
	public CostFunction(NetworkGenerator ng, FlowGenerator tg, LogMatlabFormat logger){
		this.ng=ng;
		this.tg=tg;
		this.logger=logger;
	}
	
	///////////////// MATCH FUNCTIONS ////////////////
	/**
	 * latency match of net to flow; 0 for match; else strength of violation
	 * @param flow
	 * @param net
	 * @return latency match according to cost function
	 */
	public static int latencyMatch(Flow flow, Network net){
		if(net.getLatency()>flow.getReqLatency()){
			//System.out.println("lcy "+(Math.pow(net.getLatency()-flow.getReqLatency(),2)*flow.getImpLatency()));
			return (int) (Math.pow(net.getLatency()-flow.getReqLatency(),2)*flow.getImpLatency())*LcyJitAmplifier;
		}else{
			return 0;
		}
	}
	/**
	 * jitter match of net to flow; 0 for match; else strength of violation
	 * @param flow
	 * @param net
	 * @return jitter match according to cost function
	 */
	public static int jitterMatch(Flow flow, Network net){
		if(net.getJitter()>flow.getReqJitter()){
			return (int) (Math.pow(net.getJitter()-flow.getReqJitter(),2)*flow.getImpJitter())*LcyJitAmplifier;
		}else{
			return 0;
		}
	}

	public int monetary(Network net){
		return ng.getCostImportance()*net.getCost();
	}
	
	public static int vioTimeLimits(Flow flow, int t){
		return (int) ( 
				Math.pow(Math.max(t-flow.getDeadline(),0), 2)*flow.getImpDeadline()+
				Math.pow(Math.max(flow.getStartTime()-t,0), 2)*flow.getImpStartTime()
				);
	}
	

	///////////////////// VIOLATIONS /////////////////////
	
	public int vioLcy_f_t_n(int[][][] schedule, int f, int t, int n){
		Flow flow = tg.getFlows().get(f);
		Network net = ng.getNetworks().get(n);
		if(schedule[f][t][n]>0){
			return latencyMatch(flow, net);
		}else{
			return 0;
		}

	}
	/**
	 * 
	 * @param schedule[f][t][n]
	 * @return Latency violations for each flow (without user pref)
	 */
	public int[] vioLcy(int[][][] schedule){
		int flows = schedule.length;
		int timeslots = schedule[0].length;
		int networks = schedule[0][0].length;
		
		int[] vioLcy = new int[flows];
		for(int n = 0; n<networks; n++){
			for (int t = 0; t < timeslots; t++) {
				for(int f = 0; f<flows; f++){
					vioLcy[f]+= vioLcy_f_t_n(schedule, f, t, n);
				}
			}
		}
		check(vioLcy,"vioLcy");
		if(logger!=null){
			logger.log("vioLcy", vioLcy);
		}
		return vioLcy;
	}

	public int vioJit_f_t_n(int[][][] schedule, int f, int t, int n){
		Flow flow = tg.getFlows().get(f);
		Network net = ng.getNetworks().get(n);
		if(schedule[f][t][n]>0){
			return jitterMatch(flow, net);
		}else{
			return 0;
		}
	}
	
	public int[] vioJit(int[][][] schedule){
		int flows = schedule.length;
		int timeslots = schedule[0].length;
		int networks = schedule[0][0].length;
		
		int[] vioJit = new int[flows];
		for(int n = 0; n<networks; n++){
			for (int t = 0; t < timeslots; t++) {
				for(int f = 0; f<flows; f++){
					vioJit[f]+= vioJit_f_t_n(schedule, f, t, n);
				}
			}
		}

		check(vioJit, "vioJit");
		if(logger!=null){
			logger.log("vioJit", vioJit);
		}
		return vioJit;
	}
	
	
	public int vioSt_f_t_n(int[][][] schedule, int f, int t, int n){
		Flow flow = tg.getFlows().get(f);
		return (int) Math.pow(flow.getStartTime()-(t+1),2)*schedule[f][t][n]*flow.getImpStartTime();
	}
	
	/**
	 * 
	 * @param schedule_f_t_n
	 * @return start time violation per flow (without user imp)
	 * 
	 * use (t+1) for comparison, because java arrays start at 0 but cplex arrays start with 1
	 */
	public int[] vioSt(int[][][] schedule){
		int flows = schedule.length;
		int timeslots = schedule[0].length;
		int networks = schedule[0][0].length;
		
		int[] vioSt = new int[flows];
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			for(int n = 0; n<networks; n++){
				for (int t = 0; t < timeslots; t++) {
					if((t+1)<flow.getStartTime()){		//start time and deadline are in CPLEX index (starting at 1) we therefore compare with modified time index (t+1)
//						vioSt[f]+= Math.pow(flow.getStartTime()-(t+1),2)*schedule[f][t][n]*flow.getImpStartTime();
						vioSt[f]+= vioSt_f_t_n(schedule, f, t, n);
					}
				}
			}
		}
		check(vioSt, "st_vio");
		if(logger!=null){
			logger.log("vioSt", vioSt);
		}
		return vioSt;
	}
	
	
	public int vioDl_f_t_n(int[][][] schedule, int f, int t, int n){
		Flow flow = tg.getFlows().get(f);
		return (int) Math.pow((t+1)-flow.getDeadline(),2)*schedule[f][t][n]*flow.getImpDeadline();
	}
	/**
	 * 
	 * @param schedule_f_t_n
	 * @return deadline violation per flow (without user imp)
	 * 
	 * use (t+1) for comparison, because java arrays start at 0 but cplex arrays start with 1
	 */
	public int[] vioDl(int[][][] schedule){
		int flows = schedule.length;
		int timeslots = schedule[0].length;
		int networks = schedule[0][0].length;
		
		int[] vioDl = new int[flows];
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			for(int n = 0; n<networks; n++){
				for (int t = 0; t < timeslots; t++) {
					if((t+1)>flow.getDeadline()){
//						vioDl[f]+= Math.pow((t+1)-flow.getDeadline(),2)*schedule[f][t][n]*flow.getImpDeadline();
						vioDl[f]+= vioDl_f_t_n(schedule, f, t, n);
					}
				}
			}
		}
		check(vioDl, "dl_vio");
		if(logger!=null){
			logger.log("vioDl", vioDl);
		}
		return vioDl;
	}
	
	/**
	 * 
	 * @param schedule[f][t][n]
	 * @return cummulated chunks for flows and timeslots
	 */
	public int[][] cummulated_f_t(int[][][] schedule){
		int flows = schedule.length;
		int timeslots = schedule[0].length;
		int networks = schedule[0][0].length;
		
		int[][] cummulated = new int[flows][timeslots];
		for(int f = 0; f<flows; f++){
			for(int n = 0; n<networks; n++){
				for (int t = 0; t < timeslots; t++) {
					for(int t0 = t; t0<timeslots; t0++){
						cummulated[f][t0]+=schedule[f][t][n];
					}
				}
			}
		}
		check(cummulated, "cummulatedChunks");
		return cummulated;
	}
	
	
	/**
	 * 
	 * @param cummulated_f_t
	 * @return returns violation from non allocated chunks; one time user imp multiplied (must be twice)
	 */
	public int[] vioNon(int[][] cummulated_f_t){
		int flows = cummulated_f_t.length;
		int timeslots = cummulated_f_t[0].length;
		
		int[] vioNon = new int[flows];
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
				vioNon[f]= (flow.getTokens()-cummulated_f_t[f][timeslots-1])* 
				flow.getImpUnsched()* flow.getImpUser();

		}
//		System.out.println("vioNon: "+Arrays.toString(vioNon));
		check(vioNon, "non_allo_vio");
		if(logger!=null){
			logger.log("vioNon", vioNon);
		}
		return vioNon;
	}

	public int vioTp_f_t(int[][] vioTpMin, int f, int t){
		Flow flow= tg.getFlows().get(f);
		return vioTp_weight(vioTpMin[f][t], flow);
		//return vioTpMin[f][t]*flow.getImpThroughputMin()/(flow.getChunksMin()*flow.getWindowMin());
	}
	public int vioTp_weight(int vioTpMin, Flow flow){
		
		if(flow.getTokensMin()*flow.getWindowMin()<=0){
			return 0;
		}
		return Math.round(((float)(vioTpMin*flow.getImpThroughputMin()*minTpAmplifier))/(flow.getTokensMin()*flow.getWindowMin()));
	}
	
	public int[] vioTp(int[][] cummulated_f_t){
		int flows = cummulated_f_t.length;
		int timeslots = cummulated_f_t[0].length;
		
		int[] vioTp = new int[flows];
		//int[][] vioTpMax = vioTpMax(cummulated_f_t);
		int[][] vioTpMin = vioTpMin(cummulated_f_t);
		for(int f = 0; f<flows; f++){
			for(int t=0; t<timeslots; t++){
				/*				vioTp[f]+=	//vioTpMax[f][t]*tg.getFlows().get(f).getImpThroughputMax()+
				vioTpMin[f][t]*tg.getFlows().get(f).getImpThroughputMin();
			*/
				vioTp[f]+= vioTp_f_t(vioTpMin, f, t);
			}
		}
//		System.out.println("vio: "+Arrays.toString(vioTp));
		check(vioTp, "vioThroughput");
		if(logger!=null){
			logger.log("vioTp", vioTp);
		}
		return vioTp;
	}
	
//	/**
//	 * todo: first case --> nothing to subtract!
//	 * @param cummulated_f_t
//	 * @return
//	 */
//	public int[][] vioTpMax(int[][] cummulated_f_t){
//		int flows = cummulated_f_t.length;
//		int timeslots = cummulated_f_t[0].length;
//		
//		int[][] vioTpMax = new int[flows][timeslots];
//		for(int f = 0; f<flows; f++){
//			Flow flow = tg.getFlows().get(f);
//			//throughput window violations check
//			//maximum throughput
//			int t0=0;
//			int subtract = 0;
//			for(int t=flow.getWindowMax()-1; t<timeslots; t++){
//				if(t0>=flow.getStartTime() && t<=flow.getDeadline()-1){
//					
//					int tp = cummulated_f_t[f][t]-subtract;	//get chunks in window
//					if(tp>flow.getChunksMax()){				//if there are too much
//						int vio=tp-flow.getChunksMax();
//						vioTpMax[f][t] +=vio;
//					}
//				}
//				subtract=cummulated_f_t[f][t0];			//first step: nothing to subtract; then cummulated[t0]
//				t0++;
//			}
//		}
//		check(vioTpMax, "vioTpMax");
//		if(logger!=null){
//			logger.log("vioTpMax", vioTpMax);
//		}
//		return vioTpMax;
//	}
//	
	public int[][] vioTpMin(int[][] cummulated_f_t){
		int flows = cummulated_f_t.length;
		int timeslots = cummulated_f_t[0].length;
		int[][] vioTpMin = new int[flows][timeslots];
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			//minimum throughput
			//search only within window between startTime and deadline
			int t0=0;	//lower bound of window
			int subtract = 0;
			for(int t=flow.getWindowMin()-1; t<timeslots; t++){		// t is upper bound of window (minus 1 for cplex index starting at 1)
				if(t0>=flow.getStartTime() && t<=flow.getDeadline()-1){		//TODO -1 after startime added	
					int tp =  cummulated_f_t[f][t]- subtract;	//get chunks in window
					if(tp<flow.getTokensMin()){				//if there are too many tokens/chunks scheduled
						int vio=flow.getTokensMin()-tp;
						vioTpMin[f][t] +=vio;
					}
				}
				subtract=cummulated_f_t[f][t0];			//first step: nothing to subtract; then cummulated[t0]
				t0++;
			}
		}
		check(vioTpMin, "vioTpMin");
		if(logger!=null){
			logger.log("vioTpMin", vioTpMin);
		}
		return vioTpMin;
	}
	
	
	////////////////////// COST FUNCTIONS /////////////////////

	/**
	 * 
	 * @param schedule[f][t][n]
	 * @return monetary cost for schedule
	 */
	public int costMon(int[][][] schedule){
		int flows = schedule.length;
		int timeslots = schedule[0].length;
		int networks = schedule[0][0].length;
		
		int costMon = 0;
		for(int f = 0; f<flows; f++){
			for(int n = 0; n<networks; n++){
				Network net = ng.getNetworks().get(n);
				for (int t = 0; t < timeslots; t++) {
					costMon+= schedule[f][t][n] * net.getCost();
				}
			}
		}
		check(costMon,"cost_ch");
		costMon*=ng.getCostImportance();	//multiply with cost importance
		if(logger!=null){
			logger.log("cost_ch", costMon);
		}
		return costMon;
	}
	
	public int costViolation(int[][][] schedule){
		int flows = schedule.length;
		
		int cost_vio =0;
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			
			int[][] cummulated_f_t = cummulated_f_t(schedule);
			
			cost_vio+=	(	vioSt(schedule)[f]+vioDl(schedule)[f]
							+vioNon(cummulated_f_t)[f]+vioTp(cummulated_f_t)[f]		//impUser is squared for vioNon
							+vioLcy(schedule)[f]+vioJit(schedule)[f]
						) * flow.getImpUser();
		}
		check(cost_vio, "cost_violation");
		if(logger!=null){
			logger.log("cost_vio", cost_vio);
		}
		return cost_vio;
	}
	
	public int costSwitches(int[][][] schedule){
		int flows = schedule.length;
		int timeslots = schedule[0].length;
		int networks = schedule[0][0].length;
		
		//switches	: is counted for each connection / no gain from complete shift in model
		int cost_switches=0;
		for(int f = 0; f<flows; f++){
			int current_net = -1;
			for(int t=0; t<timeslots; t++){
				for(int n = 0; n<networks; n++){
					if(schedule[f][t][n]>0 && current_net!=n ){
						//do not count first use
						if(current_net>=0){
							cost_switches+=2;	
								//in optimization, switches are counted per network. Here switches instances are counted.
								//because always two networks are influenced from a switch, we add 2
						}
						current_net=n;
					}
				}
			}
		}
		cost_switches*=ng.getHysteresis();
		check(cost_switches, "cost_switch");
		if(logger!=null){
			logger.log("cost_switches", cost_switches);
		}
		return cost_switches;
	}
	
	public int costTotal(int[][][] schedule){
		int costTotal = costViolation(schedule)+costSwitches(schedule)+costMon(schedule);
		if(logger!=null){
			logger.log("costTotal", costTotal);
		}
		return costTotal;
	}
	
	public String getStat(int[][][] schedule){
		String stat="Cost Deviation Statistics: \n";
		int total = costTotal(schedule);
		stat+="total = "+total;
		
		int mon = costMon(schedule);
		int vio = costViolation(schedule);
		int switches= costSwitches(schedule);
		stat+="\nmon: "+100*mon/total+"%, vio: "+100*vio/total+"%, switches"+100*switches/total+"%";
		int timeLimits=0;
		int nonsched=0;
		int tp = 0;
		int lcyJit=0;
		for(int f = 0; f<schedule.length; f++){
			int impUser = tg.getFlows().get(f).getImpUser();
			int[][] cummulated_f_t = cummulated_f_t(schedule);
			
			timeLimits += (vioSt(schedule)[f]+vioDl(schedule)[f])*impUser;
			nonsched += vioNon(cummulated_f_t)[f]*impUser;
			tp += vioTp(cummulated_f_t)[f]*impUser;
			lcyJit += vioLcy(schedule)[f]+vioJit(schedule)[f]*impUser;
		}
		 
		stat+="\ntime_limits: "+100*timeLimits/total+"%, nonsched: "+100*nonsched/total+"%, tp: "+100*tp/total+"%, lcyJit"+100*lcyJit/total+"%";
		
		return stat;
	}
	
	//this is only dummy for overwriting in extending test classes and should be left empty
	protected void check(int value, String variable){
		
	}
	protected void check(int[] value, String variable){
		
	}
	protected void check(int[][] value, String variable){
		
	}

	/**
	 * calculates total cost without feedback; if written to log
	 * @param schedule
	 */
	public void calculate(int[][][] schedule) {
		costTotal(schedule);		
	}
	
	/**
	 * criticality is worst case schedule cost (flow NOT scheduled) 
	 * @param f flow
	 * @param ng available networks
	 * @return criticality
	 */
	public static int calculateFlowCriticality(Flow f, NetworkGenerator ng){
		//calculate violation if flow is NOT scheduled (worst case)
		//TODO: and subtract violation is flow is scheduled alone (best case)
		FlowGenerator tg_temp= new FlowGenerator();
		tg_temp.addFlow(f);
		CostFunction cf = new CostFunction(ng, tg_temp);
		Scheduler s = getScheduler(tg_temp, ng);
		//get cost with empty schedule (worst case, flow is unscheduled)
		int cost_wc = cf.costViolation(s.getSchedule());
		//cost_wc*=f.getImpUser();
		//System.out.println("criticality:cost of flow "+getId()+" worst: "+cost_wc);
		return cost_wc;//-cost_bc;
	}

	private static Scheduler getScheduler(FlowGenerator tg, NetworkGenerator ng){
		return new PriorityScheduler(ng, tg);	//this could be a dummy scheduler.. only need empty schedule from it
	}

	public int getMinTpAmplifier() {
		return minTpAmplifier;
	}

}
