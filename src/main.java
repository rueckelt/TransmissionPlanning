

public class main {

//	final static String DATADIR = "model"+File.separator;	
//	final static String model = "sched_com.mod";
//	final static String dataset1 = "sched_com4.dat";
//	
//	final static String dataset_dyn = "sched_com_dyn.dat";
//	final static String dataset_net = "sched_com_net.dat";
//	final static String dataset_gen = "sched_com_gen.dat";
	
	
	public static void main(String[] args) {
		
//		TestCostFunction tcf = new TestCostFunction();
//		tcf.runTests();
		
		int t=4;
		int n=4;
		int i=4;
		int rep=1;
		
		EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,i,rep);
		eval.evaluate();
		 
//		String model = "sched_com.mod";
//		new ModelExecutor(model).testLog();
		
	}
}
