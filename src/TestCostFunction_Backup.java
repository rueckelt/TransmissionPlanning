import java.io.File;


public class TestCostFunction_Backup {
	
	public TestCostFunction_Backup(ModelExecutor me, NetworkGenerator ng, TrafficGenerator tg){
	}

	
	public boolean runTests(){
		latency_jitter_cost();
		
		return false;
	}
	


	final static String DATADIR = "model"+File.separator;	
	final static String LOG = "logs2"+File.separator;	
	final static String model = "sched_com.mod";
//	final static String model = "split_sched_com.mod";
	final static String dataset1 = "sched_com4.dat";
	
	final static String dataset_dyn = "sched_com_dyn.dat";
	final static String dataset_net = "sched_com_net.dat";
	final static String dataset_gen = "sched_com_gen.dat";
	
	

	
	public boolean latency_jitter_cost(){
		int t=5; int cap=2;
		//init network
		NetworkGenerator ng = new NetworkGenerator();
		Network net = new Network(t, cap);
				net.setCost(2);
				net.setJitter(2);
				net.setLatency(2);
				
		ng.addNetwork(net);
		ng.writeOutput(DATADIR+dataset_dyn, DATADIR+dataset_net);
		
		//init flow
		TrafficGenerator tg = new TrafficGenerator();
		Flow flow = new Flow();
			flow.setChunks(t*cap);	//capacity fits to network		
			flow.setChunksMax(cap);
			flow.setChunksMin(cap);
			flow.setWindowMax(1);
			flow.setWindowMin(1);
			flow.setImpThroughputMin(100);
			flow.setImpThrouthputMax(100);
			
			flow.setReqJitter(1);		//difference is 1
			flow.setReqLatency(1);
			flow.setImpJitter(1);
			flow.setImpLatency(1);
			flow.setImpUser(1);
		tg.addFlow(flow);
		tg.writeOutput(DATADIR+dataset_net, dataset_gen);			//write the file
		
		ModelExecutor me = new ModelExecutor(DATADIR+model);
		String logpath = "test_log"+File.separator;
		new File(logpath).mkdirs();
		me.execute(dataset_gen, logpath+"logfile.log");
		
		CostFunction cf= new CostFunction(ng, tg);
//		System.out.println(cf.calculateCost(me.getSchedule_f_t_n()));
			
		return false;
	}
}
