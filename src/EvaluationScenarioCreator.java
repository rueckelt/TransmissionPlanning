import ilog.opl.IloOplModel;

import java.io.File;


public class EvaluationScenarioCreator {
	
	private final boolean TEST_COST_FUNCTION = true;
	private boolean LOG_OVERWRITE = true;
	private boolean RECALC= false;
	private final String LOG;
	
	public EvaluationScenarioCreator(int time, int nets, int apps, int repetitions, String logpath){
		REPETITIONS=repetitions;
		MAX_TIME=time;
		MAX_APPS=apps;
		MAX_NETS=nets;
		LOG=logpath+File.separator;
	}
	
	private final int REPETITIONS;
	private final int MAX_TIME;
	private final int MAX_APPS;
	private final int MAX_NETS;
	
	private NetworkGenerator ng;
	private TrafficGenerator tg;

	
	public void evaluate(){

		//read model		
//		me = new ModelExecutor(DATADIR+model);
		
		//data sizes of..
		//(i) time
		for(int t=0;t<MAX_TIME;t++){
			//(ii) number of networks
			for(int net=0;net<MAX_NETS;net++){	
				//(iii) number of flows/requests
				for(int req=0;req<MAX_APPS;req++){
					//repetitions of optimization
					for(int rep=0; rep<REPETITIONS;rep++){
						calculateInstance_t_n_i(t, net, req, rep, LOG, LOG_OVERWRITE, RECALC);
					}
					
				}
			}
		}
	
	System.out.println("###############   DONE  ##################");
	}

	public void calculateInstance_t_n_i(int t, int n, int f, int rep, String folder, boolean overwrite, boolean recalc){
		int time = 25*pow(2,t);
		int nets = pow(2,n);
		int flows = pow(2,f);
		String folder_out = folder+t+"_"+n+"_"+f+File.separator;
		calculateInstance(time, nets, flows, rep, folder_out, overwrite, recalc);
		
	}
	public void calculateInstance(int time, int nets, int flows, int rep, String folder, boolean overwrite, boolean recalc) {
		String path=folder+"rep_"+ rep+File.separator;
		//skip if folder exists
		if(!new File(path).exists() || overwrite || recalc){
			new File(path).mkdirs();

			if(!recalc){
				ng=new NetworkGenerator(nets, time);	//add network input data
				ng.writeObject(path);
				tg = new TrafficGenerator(time, flows);		//add application traffic input data
				tg.writeObject(path); 
			}else{
				System.out.println(path);
				ng=NetworkGenerator.loadNetworkGenerator(path);
				tg=TrafficGenerator.loadTrafficGenerator(path);
			}
			String logfile=path+"log.m";
			
			Scheduler optimization = new OptimizationScheduler(ng, tg);
			optimization.calculateInstance(logfile);
			

//			me.execute(path+dataset_gen, logfile);
//			
//			CostFunction cf;
//			
//			if(ng!=null && tg!=null){
//				if(TEST_COST_FUNCTION){
//					cf = new TestCostFunction(ng, tg, me.getModel());
//				}else{
//					cf = new CostFunction(ng, tg);
//				}
//				int cost = cf.costTotal(me.getSchedule_f_t_n());
//			}else{
//				System.err.println("################## TG OR NG IS NULL ###################");
//			}
		}
	}

	private int pow(int v, int exp){
		return (int)Math.round(Math.pow(v, exp));
	}
	
}
