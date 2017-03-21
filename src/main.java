import java.io.File;
import java.util.Vector;

import schedulers.GreedyScheduler;
import schedulers.Scheduler;
import schedulingIOModel.Flow;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.Network;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.UncertaintyErrorCalculation;
import visualization.Plot;
import visualization.VisualizationPack;
import ToolSet.EvaluationScenarioCreator;
import ToolSet.LogMatlabFormat;
import ToolSet.RndInt;




public class main {
	
	public static void main(String[] args) {


		int f=3;	//2^f		default: f=3 (8 flows) 			//for vary flows use f=6
		int t=2;	//25*2^t	default: t=2 (100 time slots)	//for vary time use t=4
		int n=3;	//2^n		default: n=3 (8 networks)		//for vary networks use n=6

		//load			default	= 2;	(1=low, 2=medium, 3=high)
		//cost weight 	default = 2; 	(1=low, 2=medium, 3=high)
		int rep=50;
		
//		String logpath= "my_logs"+File.separator+"short_test";
//		String logpath= "my_logs"+File.separator+"test";

		//String logpath= "my_logs"+File.separator+"tester";
//		String logpath= "my_logs"+File.separator+"eval_4_4_3_c15";

	
//		String logpath= "my_logs"+File.separator+"test_exec_ga";

//		String logpath= "my_logs"+File.separator+"vary_flows"; f=4;//f=5;
//		String logpath= "my_logs"+File.separator+"vary_time"; t=4;
//		String logpath= "my_logs"+File.separator+"vary_nets"; n=5;
//		String logpath= "my_logs"+File.separator+"vary_load"; 
//		String logpath= "my_logs"+File.separator+"vary_cost";
//		String logpath= "my_logs"+File.separator+"vary_time_test"; t=0;
		 
//		String logpath= "my_logs"+File.separator+"vary_pe_move";
//		String logpath= "my_logs"+File.separator+"vary_pe_net";
//		String logpath= "my_logs"+File.separator+"vary_pe_flow";
//		String logpath= "my_logs"+File.separator+"vary_pe_comb";

		String logpath= "my_logs"+File.separator+"vary_pe_all2";

//		String logpath= "test_GA";
		
		
		//if overwrite, then delete everything in folder, create scenario new and calculate
		//if recalc, then keep generated scenario, keep all files and recalculate only specified schedules; overwrite their files
		//if nothing of the two, create new scenario if none available; calculate schedules if no logs available for scheduler.

			EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
//			eval.visualize();
//			eval.recalc();
//			eval.addUncertainty((float)0.3, (float)0.3, (float)0.3);
//			eval.addUncertainty((float)0.5, (float)0.0, (float)0.0);	//move
//			eval.addUncertainty((float)0.0, (float)0.5, (float)0.0);	//net
//			eval.addUncertainty((float)0.0, (float)0.0, (float)0.5);	//flow
//			eval.addUncertainty((float)0.5, (float)0.5, (float)0.5);	//combined
//			eval.evaluateTop();
//			eval.overwrite();	//overwrite does not work in current state. EvalScneario creator needs update. Delete logs or use other path instead!
//			eval.evaluateAll();
//			eval.evaluateTimeVariation();	
//			eval.evaluateNetworkVariation();

//			eval.evaluateFlowVariation();
//			eval.evaluateMonetaryWeight();
//			eval.evaluateTrafficLoad();
			eval.evaluateThisInstance();
			eval.parallel(1);
			eval.start(6*24);

		


//		//testing uncertainty models
//		int timeslots=50;
//		float unc=(float)0.3;
//
//		FlowGenerator fg= new FlowGenerator(timeslots, 4);
//
//		for(Flow f1 : fg.getFlows()){
//			f1.toString();
//		}
//		FlowGenerator fg2=fg.clone();
//		fg2.addUncertainty((float) unc, timeslots);
//
//		for(Flow f1 : fg.getFlows()){
//			System.out.println(f1.toString());
//		}
//		UncertaintyErrorCalculation ec = new UncertaintyErrorCalculation(fg.getFlows(), fg2.getFlows(), timeslots);
//		System.out.println("changed-------------");
//		for(Flow f1 : fg2.getFlows()){
//			System.out.println(f1.toString());
//		}
//		float error = ec.getFlowUncertaintyError();
//		System.out.println("error "+error);
//		
//		double sum =0;
//		for(int t0=0; t0<timeslots; t0++){
//
//			float error2 = ec.getFlowUncertaintyError(t0, t0);
//			float error_3 = ec.getFlowUncertaintyError(t0-9, t0);
//			System.out.println("============error t "+t0+"+1 is "+error2+",\terr +9 ="+error_3);
//			sum+=error2;
//		}
//		System.out.println("average error = " +sum/timeslots+ ", comp to "+unc);
		
//		fg.addUncertainty((float)0.2, (float)0.3, timeslots);	//probAddCancel, probContinue, timesteps
//
//		
//		NetworkGenerator ng = new NetworkGenerator(8,timeslots);
//		
//		ng.addNetworkUncertainty((float) 0.4);
//		ng.addPositionUncertainty((float) 0.2);
		
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
