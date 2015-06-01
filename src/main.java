import java.io.File;
import java.lang.management.GarbageCollectorMXBean;


public class main {

	final static String DATADIR = "model"+File.separator;	
	final static String model = "sched_com.mod";
	final static String dataset1 = "sched_com4.dat";
	
	final static String dataset_dyn = "sched_com_dyn.dat";
	final static String dataset_net = "sched_com_net.dat";
	final static String dataset_gen = "sched_com_gen.dat";
	
	
	public static void main(String[] args) {

		ModelExecutor me = new ModelExecutor(DATADIR+model);		
		
		new NetworkGenerator().writeOutput(DATADIR+dataset_dyn, DATADIR+dataset_net);	//add network input data
		new TrafficGenerator().writeOutput(DATADIR+dataset_net, DATADIR+dataset_gen);	//add application traffic input data

		for(int i =0; i<50;i++){
			me.execute(DATADIR+		dataset1);
			me.execute(DATADIR+		dataset_gen);
		
		}
		System.out.println("Mean in ns = "+ me.getMedianNs());
		me.printOutput();
	}

}
