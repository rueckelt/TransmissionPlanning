import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {
	
	public static void main(String[] args) {
		boolean decomp = false;
		int f=2;
		int t=3;
		int n=2;
		int rep=10;
//		String logpath= "my_logs"+File.separator+"short_test";
//		String logpath= "my_logs"+File.separator+"test";
		String logpath= "my_logs"+File.separator+"longTest1";
		
//		for(int rep1 = 0; rep1<=rep; rep1++){
			EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
//			eval.recalc();
//			eval.visualize();
//			eval.overwrite();
//			eval.evaluateAll();
			eval.evaluateTop();
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, false ,true, decomp);	//recalc
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, true ,false, decomp);	//overwrite
//			System.gc();
//		}
		

		System.out.println("done");
	}
}
