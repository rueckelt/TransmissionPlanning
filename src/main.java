import java.io.File;



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
		
		int t=3;
		int n=3;
		int i=4;
		int rep=3;
		
		EvaluationScenarioCreator eval = new EvaluationScenarioCreator(t,n,i,rep);

//		eval.calculateInstance_t_n_i(0, 0, 0, 0, "logs2"+File.separator, false, true);
		eval.evaluate();
//		
		String instance_path = "logs2\\0_0_0\\rep_0\\";
		TrafficGenerator tg = TrafficGenerator.loadTrafficGenerator(instance_path);
		System.out.println(tg.getFlows().get(0).getChunks());
		NetworkGenerator ng = NetworkGenerator.loadNetworkGenerator(instance_path);
		System.out.println(ng.getHysteresis());
		
		 
//		String model = "sched_com.mod";
//		new ModelExecutor(model).testLog();
		
	}
}
