import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {
	
	public static void main(String[] args) {
		boolean decomp = false;
		int f=4;
		int n=4;
		int t=4;
		int rep=30;
		String logpath= "my_logs"+File.separator+"longTest1";
		
//		for(int rep = 4; rep<=rep1; rep++){
			EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
//			eval.evaluateAll();
			eval.calculateInstance_t_n_i(t, n, f, rep, logpath, false ,true, decomp);	//recalc
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath, true ,false, decomp);	//overwrite
//			System.gc();
//		}
		
		//eval.evaluate();	//calculate each instance from 0_0_0 till t_n_f

		
	}
}
