import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {
	
	public static void main(String[] args) {
		boolean decomp = false;
		int f=2;
		int t=2;
		int n=2;
		int rep=20;
//		String logpath= "my_logs"+File.separator+"short_test";
//		String logpath= "my_logs"+File.separator+"test";
		String logpath= "my_logs"+File.separator+"longTest3";
		
//		for(int rep1 = 0; rep1<=rep; rep1++){
			EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
//			eval.recalc();
//			eval.visualize();
			eval.overwrite();
//			eval.evaluateAll();

//			eval.evaluateTop();
			eval.evaluateTimeVariation();
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, false ,true, decomp);	//recalc
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, true ,false, decomp);	//overwrite
			eval.parallel(4);
			eval.start();
//		}
		

		System.out.println("done");
	}
}
