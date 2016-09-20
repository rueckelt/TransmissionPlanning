import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {
	
	public static void main(String[] args) {
		boolean decomp = false;
//		int f=2;
//		int t=1;
//		int n=2;
//		int rep=3;
		int f=2;
		int t=2;
		int n=2;//5;
		int rep=1;
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
//			eval.evaluateTimeVariation();
//			eval.evaluateNetworkVariation();
//			eval.evaluateTop();
			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, false ,true, decomp);	//recalc
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, true ,false, decomp);	//overwrite
			eval.parallel(1);
			eval.start();
//		}
		

		System.out.println("done");
	}
}
