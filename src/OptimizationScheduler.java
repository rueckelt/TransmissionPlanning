import java.io.File;


public class OptimizationScheduler extends Scheduler {

	
	final static String DATADIR = "model"+File.separator;	
	final static String LOG = "logs2"+File.separator;	
	final static String model = "sched_com2.mod";
//	final static String model = "split_sched_com.mod";
	final static String dataset1 = "sched_com4.dat";
	
	final static String dataset_dyn = "sched_com_dyn.dat";
	final static String dataset_net = "sched_com_net.dat";
	final static String dataset_gen = "sched_com_gen.dat";
	
	
	
	public OptimizationScheduler(NetworkGenerator ng, TrafficGenerator tg) {
		super(ng, tg);
	}

	@Override
	protected int[][][] calculateInstance_internal(String logfile) {
		String dataset_path = logfile+"__"+dataset_gen;
		ng.writeOutput(DATADIR+dataset_dyn, DATADIR+dataset_net);		//write the file for ILP
		tg.writeOutput(DATADIR+dataset_net, dataset_path);			//write the file for ILP
		
		ModelExecutor me = new ModelExecutor(DATADIR+model);
		me.execute(dataset_path, logfile);
		
		return me.getSchedule_f_t_n();
	}

}
