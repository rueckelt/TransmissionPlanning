
public class CostFunction_Backup {
	
	private NetworkGenerator ng;
	private TrafficGenerator tg;
	
	public CostFunction_Backup(NetworkGenerator ng, TrafficGenerator tg){
		this.ng=ng;
		this.tg=tg;
	}
	
	
	/**
	 * 
	 * @param schedule
	 * @return
	 * 
	 * TODO: throughput violation: for each flow: summing array over complete time. compare differences for specified window sizes 
	 */
	public int calculateCost(int[][][] schedule){
		int networks = schedule[0][0].length;
		int timeslots = schedule[0].length;
		int flows = schedule.length;
		
		System.out.println("t:"+timeslots+"\nn:"+networks+"\nf:"+flows);
		
		//cost elements
		int cost = 0, cost_lcy=0, cost_jit=0, cost_mon=0, cost_non=0, 
			cost_switches=0, cost_startTime=0, cost_deadline=0, cost_throughput=0;
		int non_alloc[] = new int[flows]; 
		
		//loops 
		for(int f = 0; f<flows; f++){
			Flow flow = tg.getFlows().get(f);
			int[] cummulated = new int[timeslots]; 
			
			for(int n = 0; n<networks; n++){
				Network net = ng.getNetworks().get(n);
				//cost non alloc
				non_alloc[f] = flow.getChunks();
				
				for (int t = 0; t < timeslots; t++) {
					//latency violation
					if(net.getLatency()>flow.getReqLatency()){
						cost_lcy+= Math.pow(net.getLatency()-flow.getReqLatency(),2)*schedule[f][t][n]*flow.getImpLatency()*flow.getImpUser();
					}
					//jitter violation
					if(net.getJitter()>flow.getReqJitter()){
						cost_jit+= Math.pow(net.getJitter()-flow.getReqJitter(),2)*schedule[f][t][n]*flow.getImpJitter()*flow.getImpUser();
					}
					//non allocated chunks
					non_alloc[f] -= schedule[f][t][n];		//subtract scheduled chunks to get unscheduled after loop
					
					//monetary cost
					cost_mon += schedule[f][t][n]*net.getCost();
					
					//start time violation
					if(t<flow.getStartTime()){
						cost_startTime += schedule[f][t][n]*flow.getImpStartTime()*Math.pow(flow.getStartTime()-t, 2)*flow.getImpUser();
					}
					
					//deadline violation
					if(t>flow.getDeadline()){
						cost_deadline += schedule[f][t][n]*flow.getImpDeadline()*Math.pow(t-flow.getDeadline(), 2)*flow.getImpUser();
					}
					
					//throughput
					for(int t0 = t; t0<timeslots; t0++){
						cummulated[t0]+=schedule[f][t][n];
					}
					
				} //end for timeslots
			} //end for networks
			System.out.println("non_alloc "+f+": "+ non_alloc[f]*flow.getImpUnsched()*flow.getImpUser());
			cost_non += non_alloc[f]*flow.getImpUnsched()*flow.getImpUser()*flow.getImpUser(); //two times user importance
		
			//switches	: is counted for each connection / no gain from complete shift in model
			int current_net = -1;
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
			
			//throughput window violations check
			//maximum
			int t0=0;
			for(int t=flow.getWindowMax(); t<timeslots; t++){
				int tp = cummulated[t]-cummulated[t0];	//get chunks in window
				if(tp>flow.getChunksMax()){				//if there are too much
					cost_throughput +=tp-flow.getChunksMax()*flow.getImpThroughputMax()*flow.getImpUser();
				}
				t0++;
			}
			//minimum throughput
			t0=0;
			for(int t=flow.getWindowMin(); t<timeslots; t++){
				int tp = cummulated[t]-cummulated[t0];	//get chunks in window
				if(tp<flow.getChunksMin()){				//if there are too much
					cost_throughput +=flow.getChunksMin()-tp*flow.getImpThroughputMax()*flow.getImpUser();
				}
				t0++;
			}
		
		
		
		
		} //end for flows
		
		
		
		
		cost = cost_lcy+cost_jit+ cost_deadline + cost_startTime + cost_mon+cost_non+ cost_switches + cost_throughput;
		
		System.out.println("lcy "+cost_lcy+"\njit "+cost_jit+"\ndl "+cost_deadline+"\nst "+cost_startTime+"\nmon "+
				cost_mon+"\nnon "+cost_non+"\n sw "+cost_switches+"\ntp "+cost_throughput);
		
		return cost;
	}

}
