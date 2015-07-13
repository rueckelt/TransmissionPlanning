

public class main {

//	final static String DATADIR = "model"+File.separator;	
//	final static String model = "sched_com.mod";
//	final static String dataset1 = "sched_com4.dat";
//	
//	final static String dataset_dyn = "sched_com_dyn.dat";
//	final static String dataset_net = "sched_com_net.dat";
//	final static String dataset_gen = "sched_com_gen.dat";
	
	
	public static void main(String[] args) {
		
		int t=5;
		int n=5;
		int i=5;
		int rep=100;

//		int t = Integer.parseInt(args[0]);
//		int n = Integer.parseInt(args[1]);
//		int i = Integer.parseInt(args[2]);
//		int rep = Integer.parseInt(args[3]);
		
		EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,i,rep);
		eval.evaluate();

//		String model = "sched_com.mod";
//		new ModelExecutor(model).testLog();
		
	}
}
