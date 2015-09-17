import java.io.File;

import ToolSet.EvaluationScenarioCreator;




public class main {

	
	public static void main(String[] args) {
		
		int t=3;
		int n=4;
		int f=3;
		int rep=1;
		String logpath= "my_logs"+File.separator+"t0";
		
		EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,f,rep,logpath);
		eval.calculateInstance_t_n_i(t, n, f, rep, logpath, true, false);	//calculate only this instance of the schedule
		
		//eval.evaluate();	//calculate each instance from 0_0_0 till t_n_f

		
	}
}
