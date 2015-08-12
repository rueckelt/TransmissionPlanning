package schedulingIOModel;
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
	private TrafficGenerator tg;
	LogMatlabFormat logger = null;
	
	public CostFunction(NetworkGenerator ng, TrafficGenerator tg){
		this.ng=ng;
		this.tg=tg;
	}
	
	public CostFunction(NetworkGenerator ng, TrafficGenerator tg, LogMatlabFormat logger){
		this.ng=ng;
		this.tg=tg;
		this.logger=logger;
	}
	
	///////////////////// VIOLATIONS /////////////////////

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
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			for(int n = 0; n<networks; n++){
				Network net = ng.getNetworks().get(n);
				for (int t = 0; t < timeslots; t++) {
					if(net.getLatency()>flow.getReqLatency()){
						vioLcy[f]+= Math.pow(net.getLatency()-flow.getReqLatency(),2)*schedule[f][t][n]*flow.getImpLatency();
					}
				}
			}
		}
		check(vioLcy,"vioLcy");
		if(logger!=null){
			logger.log("vioLcy", vioLcy);
		}
		return vioLcy;
	}
	
	public int[] vioJit(int[][][] schedule){
		int flows = schedule.length;
		int timeslots = schedule[0].length;
		int networks = schedule[0][0].length;
		
		int[] vioJit = new int[flows];
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			for(int n = 0; n<networks; n++){
				Network net = ng.getNetworks().get(n);
				for (int t = 0; t < timeslots; t++) {
					if(net.getJitter()>flow.getReqJitter()){
						vioJit[f]+= Math.pow(net.getJitter()-flow.getReqJitter(),2)
								*schedule[f][t][n]*flow.getImpJitter();
					}
				}
			}
		}
		check(vioJit, "vioJit");
		if(logger!=null){
			logger.log("vioJit", vioJit);
		}
		return vioJit;
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
					if((t+1)<flow.getStartTime()){
						vioSt[f]+= Math.pow(flow.getStartTime()-(t+1),2)*schedule[f][t][n]*flow.getImpStartTime();
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
						vioDl[f]+= Math.pow((t+1)-flow.getDeadline(),2)*schedule[f][t][n]*flow.getImpDeadline();
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
	private int[][] cummulated_f_t(int[][][] schedule){
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
			for (int t = 0; t < timeslots; t++) {
				vioNon[f]= (flow.getChunks()-cummulated_f_t[f][timeslots-1])* flow.getImpUnsched()*flow.getImpUser();
			}
		}
		check(vioNon, "non_allo_vio");
		if(logger!=null){
			logger.log("vioNon", vioNon);
		}
		return vioNon;
	}
	
	public int[] vioTp(int[][] cummulated_f_t){
		int flows = cummulated_f_t.length;
		int timeslots = cummulated_f_t[0].length;
		
		int[] vioTp = new int[flows];
		int[][] vioTpMax = vioTpMax(cummulated_f_t);
		int[][] vioTpMin = vioTpMin(cummulated_f_t);
		for(int f = 0; f<flows; f++){
			for(int t=0; t<timeslots; t++){
				vioTp[f]=	vioTpMax[f][t]*tg.getFlows().get(f).getImpThroughputMax()+
							vioTpMin[f][t]*tg.getFlows().get(f).getImpThroughputMin();
			}
		}
		check(vioTp, "vioThroughput");
		if(logger!=null){
			logger.log("vioTp", vioTp);
		}
		return vioTp;
	}
	
	/**
	 * todo: first case --> nothing to subtract!
	 * @param cummulated_f_t
	 * @return
	 */
	public int[][] vioTpMax(int[][] cummulated_f_t){
		int flows = cummulated_f_t.length;
		int timeslots = cummulated_f_t[0].length;
		
		int[][] vioTpMax = new int[flows][timeslots];
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			//throughput window violations check
			//maximum throughput
			int t0=0;
			int subtract = 0;
			for(int t=flow.getWindowMax()-1; t<timeslots; t++){
				if(t0>=flow.getStartTime() && t<=flow.getDeadline()-1){
					
					int tp = cummulated_f_t[f][t]-subtract;	//get chunks in window
					if(tp>flow.getChunksMax()){				//if there are too much
						int vio=tp-flow.getChunksMax();
						vioTpMax[f][t] +=vio;
					}
				}
				subtract=cummulated_f_t[f][t0];			//first step: nothing to subtract; then cummulated[t0]
				t0++;
			}
		}
		check(vioTpMax, "vioTpMax");
		if(logger!=null){
			logger.log("vioTpMax", vioTpMax);
		}
		return vioTpMax;
	}
	
	public int[][] vioTpMin(int[][] cummulated_f_t){
		int flows = cummulated_f_t.length;
		int timeslots = cummulated_f_t[0].length;
		int[][] vioTpMin = new int[flows][timeslots];
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			//minimum throughput
			//search only within window between startTime and deadline
			int t0=0;
			int subtract = 0;
			for(int t=flow.getWindowMin()-1; t<timeslots; t++){
				if(t0>=flow.getStartTime() && t<=flow.getDeadline()-1){
					int tp =  cummulated_f_t[f][t]- subtract;	//get chunks in window
					if(tp<flow.getChunksMin()){				//if there are too much
						int vio=flow.getChunksMin()-tp;
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
	 * @return Latency violations for each flow (without user pref)
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
							+vioNon(cummulated_f_t)[f]+vioTp(cummulated_f_t)[f]
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
	
	//this is only dummy for overwriting in extending test classes and should be left empty
	private void check(int value, String variable){
		
	}
	private void check(int[] value, String variable){
		
	}
	private void check(int[][] value, String variable){
		
	}

	/**
	 * calculates total cost without feedback; if written to log
	 * @param schedule
	 */
	public void calculate(int[][][] schedule) {
		costTotal(schedule);		
	}
}
