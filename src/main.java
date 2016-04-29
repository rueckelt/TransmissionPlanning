import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {
	
	public static void main(String[] args) {
		boolean decomp = false;
		int f=2;
		int t=1;
		int n=2;
		int rep=1;
//		String logpath= "my_logs"+File.separator+"short_test";
//		String logpath= "my_logs"+File.separator+"test";
		String logpath= "my_logs"+File.separator+"test_lcyjit";
		
//		for(int rep1 = 0; rep1<=rep; rep1++){
			EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
//			eval.recalc();
//			eval.visualize();
			eval.overwrite();
//			eval.evaluateAll();
//			eval.evaluateTimeVariation();
			eval.evaluateTop();
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, false ,true, decomp);	//recalc
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, true ,false, decomp);	//overwrite
//			eval.parallel(14);
			eval.start();
//		}
		

		System.out.println("done");
	}
}
