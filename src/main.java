import java.io.File;
import java.lang.management.GarbageCollectorMXBean;


public class main {

//	final static String DATADIR = "model"+File.separator;	
//	final static String model = "sched_com.mod";
//	final static String dataset1 = "sched_com4.dat";
//	
//	final static String dataset_dyn = "sched_com_dyn.dat";
//	final static String dataset_net = "sched_com_net.dat";
//	final static String dataset_gen = "sched_com_gen.dat";
	
	
	public static void main(String[] args) {

		EvaluationScenarioCreator eval = new EvaluationScenarioCreator();
		eval.evaluate();
		
	}
}
