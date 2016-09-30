import java.io.File;
import java.util.Vector;

import schedulers.GreedyScheduler;
import schedulers.Scheduler;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import visualization.Plot;
import visualization.VisualizationPack;
import ToolSet.EvaluationScenarioCreator;
import ToolSet.RndInt;




public class main {
	
	public static void main(String[] args) {
		boolean decomp = false;
		//logfile is logpath/f_t_n/rep..
//		int f=2;
//		int t=1;
//		int n=2;
//		int rep=3;
		int f=4;
		int t=4;
		int n=3;//5;
		int rep=30;
//		String logpath= "my_logs"+File.separator+"short_test";
//		String logpath= "my_logs"+File.separator+"test";

		String logpath= "my_logs"+File.separator+"tester";

		
//		for(int rep1 = 0; rep1<=rep; rep1++){
		
		
		//if overwrite, then delete everything in folder, create scenario new and calculate
		//if recalc, then keep generated scenario, keep all files and recalculate only specified schedules; overwrite their files
		//if nothing of the two, create new scenario if none available; calculate schedules if no logs available for scheduler.
		
			EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
//			eval.recalc();
//			eval.visualize();
//			eval.overwrite();
//			eval.evaluateAll();
			eval.evaluateTimeVariation();
//			eval.evaluateNetworkVariation();
//			eval.evaluateTop();
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, false ,true, decomp);	//recalc
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, true ,false, decomp);	//overwrite
			eval.parallel(4);
			eval.start();
//		}
		
		//testing uncertainty models
//		int timeslots=80;
//	
//		FlowGenerator fg= new FlowGenerator(timeslots, 8);
//		fg.addUncertainty((float)0.2, (float)0.3, timeslots);	//probAddCancel, probContinue, timesteps
//	
//		NetworkGenerator ng = new NetworkGenerator(4,80);
//		ng.addPositionUncertainty((float) 0.3,(float)0.1, false);
//		ng.addNetworkUncertainty((float)0.3, 5);		//param1: change characteristics (tp, lcy, jit) --> [0..1]; param 2: change range
		
//		for(Network net: ng.getNetworks()){
//			System.out.println(net.toString());
//
//			System.out.println("\nnet lcy / jit="+net.getLatency()+" / "+net.getJitter());
//		}
//		
//		
//		for(Network net: ng.getNetworks()){
//			System.out.println(net.toString());
//			System.out.println("\nnet lcy / jit="+net.getLatency()+" / "+net.getJitter());
//		}
			
		System.out.println("done");
	}
}
