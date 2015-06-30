

public class main {

//	final static String DATADIR = "model"+File.separator;	
//	final static String model = "sched_com.mod";
//	final static String dataset1 = "sched_com4.dat";
//	
//	final static String dataset_dyn = "sched_com_dyn.dat";
//	final static String dataset_net = "sched_com_net.dat";
//	final static String dataset_gen = "sched_com_gen.dat";
	
	
	public static void main(String[] args) {
		
		int t=2;
		int r=4;
		int n=3;
		int rep=2;

		EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,r,n,rep);
		eval.evaluate();

//		String model = "sched_com.mod";
//		new ModelExecutor(model).testLog();
		
	}
}
