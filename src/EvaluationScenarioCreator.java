import ilog.opl.IloOplModel;

import java.io.File;


public class EvaluationScenarioCreator {
	
	public EvaluationScenarioCreator(int time, int nets, int apps, int repetitions){
		REPETITIONS=repetitions;
		MAX_TIME=time;
		MAX_APPS=apps;
		MAX_NETS=nets;
	}
	
	private boolean log_overwrite = true;
	private final int REPETITIONS;
	private final int MAX_TIME;
	private final int MAX_APPS;
	private final int MAX_NETS;
	
	final static String DATADIR = "model"+File.separator;	
	final static String LOG = "logs2"+File.separator;	
	final static String model = "sched_com.mod";
//	final static String model = "split_sched_com.mod";
	final static String dataset1 = "sched_com4.dat";
	
	final static String dataset_dyn = "sched_com_dyn.dat";
	final static String dataset_net = "sched_com_net.dat";
	final static String dataset_gen = "sched_com_gen.dat";

	private ModelExecutor me;
	
	
	private NetworkGenerator ng;
	private TrafficGenerator tg;

	
	public void evaluate(){

		//read model		
		me = new ModelExecutor(DATADIR+model);
		
		//data sizes of..
		//(i) time
		for(int t=0;t<MAX_TIME;t++){
			//(ii) number of networks
			for(int net=0;net<MAX_NETS;net++){	
				int time = 25*pow(2,t);
				int nets = pow(2,net);
				
				//(iii) number of flows/requests
				for(int req=0;req<MAX_APPS;req++){
					int reqs = pow(2,req);
					String folder = LOG+t+"_"+net+"_"+req+File.separator;
					//new File(folder).mkdirs();

					//repetitions of optimization
					for(int rep=0; rep<REPETITIONS;rep++){
						String path=folder+"rep_"+ rep+File.separator;
//						//skip if folder exists
						if(!new File(path).exists() || log_overwrite){
							new File(path).mkdirs();

							ng=new NetworkGenerator(nets, time);	//add network input data
							ng.writeOutput(DATADIR+dataset_dyn, DATADIR+dataset_net);		//write the file
							
							tg = new TrafficGenerator(time, reqs);		//add application traffic input data
							tg.writeOutput(DATADIR+dataset_net, path+dataset_gen);			//write the file

							String logfile=path+"log.m";

							me.execute(path+dataset_gen, logfile);
							
							CostFunction cf = //new CostFunction(ng, tg);
											new TestCostFunction(ng, tg, me.getModel());
							int cost = cf.costTotal(me.getSchedule_f_t_n());
							System.out.println("COST_FUNCTION: "+cost);
						}
					}
					
				}
			}
		}
	
	System.out.println("###############   DONE  ##################");
	}

	private int pow(int v, int exp){
		return (int)Math.round(Math.pow(v, exp));
	}
	
}
