package schedulers;
import java.io.File;

import optimization.ModelExecutor;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.TestCostFunction;
import schedulingIOModel.TrafficGenerator;


public class OptimizationScheduler extends Scheduler {

	
	final static String MODELDIR = "model"+File.separator;	
	final static String model = "sched_com.mod";
//	final static String model = "split_sched_com.mod";
//	final static String dataset1 = "sched_com4.dat";
	
	final static String dataset_dyn = "sched_com_dyn.dat";		//with place holders for flows and networks
	final static String dataset_net = "sched_com_net.dat";		//is created: with place holders for flows only
	final static String dataset_gen = "sched_com_gen.dat";		//is created: without place holders
	
	private boolean testCF=false;

	@Override
	public String getType() {
		return "optimization";
	}
	
	public OptimizationScheduler(NetworkGenerator ng, TrafficGenerator tg) {
		super(ng, tg);
	}

	@Override
	protected void calculateInstance_internal(String logpath) {

		String dataset_path = logpath+dataset_gen;
		writeDatFile(logpath);
				
		ModelExecutor me = new ModelExecutor(MODELDIR+model);
		me.execute(dataset_path, logpath);
		
		//does not use the "allocate" function, but set schedule from optimization directly
		setTempSchedule(me.getSchedule_f_t_n());
		
		if(testCF){
			TestCostFunction cf = new TestCostFunction(ng, tg, me.getModel());
			cf.costTotal(getSchedule());
		}
		me.end();
	}
	
	public void writeDatFile(String logpath){
		//System.out.println("2 "+logpath);
		new File(logpath).mkdirs();
		String dataset_path = logpath+dataset_gen;
		ng.writeOutput(MODELDIR+dataset_dyn, MODELDIR+dataset_net);		//write the file for ILP
		tg.writeOutput(MODELDIR+dataset_net, dataset_path);			//write the file for ILP
	}
	
	public void testCostFunction(NetworkGenerator ng, TrafficGenerator tg){
		this.ng=ng;
		this.tg=tg;
		this.testCF=true;
	}

}
