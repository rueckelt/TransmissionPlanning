package schedulers;
import java.io.File;

import optimization.ModelExecutor;
import schedulingIOModel.NetworkGenerator;
import schedulingIOModel.TestCostFunction;
import schedulingIOModel.FlowGenerator;


public class OptimizationScheduler extends Scheduler {

	
	final static String MODELDIR = "model"+File.separator;	
	final static String model = "sched_com.mod";
//	final static String model = "split_sched_com.mod";
	final static String dataset1 = "sched_com4.dat";
	
	final static String dataset_dyn = "sched_com_dyn.dat";		//with place holders for flows and networks
	final static String dataset_net = "sched_com_net.dat";		//is created: with place holders for flows only
	final static String dataset_gen = "sched_com_gen.dat";		//is created: without place holders
	
	private boolean testCF=false;

	@Override
	public String getType() {
		return "optimization";
	}
	
	public OptimizationScheduler(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
	}

	@Override
	protected void calculateInstance_internal(String logpath) {
		String dataset_path = logpath+dataset_gen;
		System.out.println(logpath);
		System.out.println(MODELDIR+dataset_dyn);
		ng.writeOutput(MODELDIR+dataset_dyn, MODELDIR+dataset_net);		//write the file for ILP
		tg.writeOutput(MODELDIR+dataset_net, dataset_path);				//write the file for ILP
		
		ModelExecutor me = new ModelExecutor(MODELDIR+model);
		System.out.println("Start model executor.");
		me.execute(dataset_path, logpath);
		System.out.println("End model executor.");
		//does not use the "allocate" function, but set schedule from optimization directly
		setTempSchedule(me.getSchedule_f_t_n());
		
		if(testCF){
			TestCostFunction cf = new TestCostFunction(ng, tg, me.getModel());
			cf.costTotal(getSchedule());
		}
	}
	
	public void testCostFunction(NetworkGenerator ng, FlowGenerator tg){
		this.ng=ng;
		this.tg=tg;
		this.testCF=true;
	}

}
