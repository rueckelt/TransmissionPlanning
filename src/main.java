import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {

	
	public static void main(String[] args) {
		boolean decomp = false;
		int t=1;
		int n=2;
		int f=2;
		int rep=2;
		String logpath= "my_logs"+File.separator+"priomatch";
		
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
