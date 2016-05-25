import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {
	
	public static void main(String[] args) {
		boolean decomp = false;
//		int f=2;
//		int t=1;
//		int n=2;
//		int rep=3;
		int f=4;
		int t=1;
		int n=4;
		int rep=10;
//		String logpath= "my_logs"+File.separator+"short_test";
//		String logpath= "my_logs"+File.separator+"test";

		String logpath= "my_logs"+File.separator+"eval_4_4_3_c15";
		
//		for(int rep1 = 0; rep1<=rep; rep1++){
			EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
//			eval.recalc();
//			eval.visualize();
//			eval.overwrite();
//			eval.evaluateAll();
//			eval.evaluateTimeVariation();
			eval.evaluateNetworkVariation();
//			eval.evaluateTop();
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, false ,true, decomp);	//recalc
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, true ,false, decomp);	//overwrite
			eval.parallel(1);
			eval.start();
//		}
		

		System.out.println("done");
	}
}
