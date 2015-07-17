
public class CostFunction {
	
	private NetworkGenerator ng;
	private TrafficGenerator tg;
	
	public CostFunction(NetworkGenerator ng, TrafficGenerator tg){
		this.ng=ng;
		this.tg=tg;
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
					if(net.getLatency()>flow.getReqLatency()){
						vioJit[f]+= Math.pow(net.getJitter()-flow.getReqJitter(),2)*schedule[f][t][n]*flow.getImpJitter();
					}
				}
			}
		}
		check(vioJit, "vioJit");
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
			for (int t = 0; t < timeslots; t++) {
				vioNon[f]= (flow.getChunks()-cummulated_f_t[f][timeslots-1])* flow.getImpUnsched()*flow.getImpUser();
			}
		}
		check(vioNon, "non_allo_vio");
		return vioNon;
	}
	
	public int[] vioTp(int[][] cummulated_f_t){
		int flows = cummulated_f_t.length;
		int timeslots = cummulated_f_t[0].length;
		
		int[] vioTp = new int[flows];
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			//throughput window violations check
			//maximum throughput
			int t0=0;
			System.out.println("wmax="+flow.getWindowMax());
			for(int t=flow.getWindowMax(); t<timeslots; t++){
				int tp = cummulated_f_t[f][t]-cummulated_f_t[f][t0];	//get chunks in window
				if(tp>flow.getChunksMax()){				//if there are too much
					int tmp=flow.getChunksMin()-tp*flow.getImpThroughputMax()*flow.getImpUser();
					System.out.println("max: f, t0, t, tp, tmp:"+f+", "+t0+", "+t+", "+tp+", "+tmp);
					vioTp[f] +=tp-flow.getChunksMax()*flow.getImpThroughputMax();
				}
				t0++;
			}
			//minimum throughput
			t0=0;
			System.out.println("wmin="+flow.getWindowMin());
			for(int t=flow.getWindowMin(); t<timeslots; t++){
				int tp =  cummulated_f_t[f][t]- cummulated_f_t[f][t0];	//get chunks in window
				if(tp<flow.getChunksMin()){				//if there are too much
					int tmp=flow.getChunksMin()-tp*flow.getImpThroughputMax()*flow.getImpUser();
					System.out.println("min: f, t0, t, tp, tmp:"+f+", "+t0+", "+t+", "+tp+", "+tmp);
					vioTp[f] +=flow.getChunksMin()-tp*flow.getImpThroughputMax()*flow.getImpUser();
				}
				t0++;
			}
		}
		check(vioTp, "vioThroughput");
		return vioTp;
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
		return cost_vio;
	}
	
	public int costSwitches(int[][][] schedule){
		int flows = schedule.length;
		int timeslots = schedule[0].length;
		int networks = schedule[0][0].length;
		
		//switches	: is counted for each connection / no gain from complete shift in model
		int cost_switches=0;
		int current_net = -1;
		for(int f = 0; f<flows; f++){
			for(int t=0; t<timeslots; t++){
				for(int n = 0; n<networks; n++){
					if(schedule[f][t][n]>0 && current_net!=n ){
						//do not count first use
						if(current_net>=0){
							cost_switches++;
						}
						current_net=n;
					}
				}
			}
		}
		check(cost_switches, "cost_switch");
		return cost_switches;
	}
	
	public int costTotal(int[][][] schedule){
		return costViolation(schedule)+costSwitches(schedule)+costMon(schedule);
	}
	
	//this is only dummy for overwriting in generalization and should be left empty
	public void check(int value, String variable){
		
	}
	public void check(int[] value, String variable){
		
	}
	public void check(int[][] value, String variable){
		
	}
}
