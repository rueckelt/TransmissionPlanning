import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {
	
	public static void main(String[] args) {
		boolean decomp = false;
		int f=3;
		int n=2;
		int t=2;
		int rep=30;
		String logpath= "my_logs"+File.separator+"longTest_tp";
		
//		for(int rep1 = 0; rep1<=rep; rep1++){
			EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
//			eval.evaluateAll();
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, false ,true, decomp);	//recalc
			eval.calculateInstance_t_n_i(t, n, f, rep, logpath+File.separator, true ,false, decomp);	//overwrite
//			System.gc();
//		}
		
		//eval.evaluate();	//calculate each instance from 0_0_0 till t_n_f

		System.out.println("done");
	}
}
