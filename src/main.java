import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {

	
	public static void main(String[] args) {
		boolean decomp = false;
//		int n=0;//3_0_2 soll
//		int f=2;
//		int t=3;
		int f=6;
		int n=6;
		int t=3;
		int rep=30;
		String logpath= "my_logs"+File.separator+"longTest1";
		
//		for(int rep = 0; rep<=30; rep++){
			EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
			eval.evaluateAll();
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath, false ,false, decomp);	//recalc
//
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath, false ,true, decomp);	//recalc
//			eval.calculateInstance_t_n_i(t, n, f, rep, logpath, true ,false, decomp);	//overwrite
//			System.gc();
//		}
		
		//eval.evaluate();	//calculate each instance from 0_0_0 till t_n_f

		
	}
}
