package ToolSet;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import schedulers.OptimizationScheduler;
import schedulers.PriorityScheduler;
import schedulers.Scheduler;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;
import visualization.Plot;
import visualization.VisualizationPack;

public class EvaluationScenarioExecutionWorker implements Callable<Boolean>{
	private boolean TEST_COST_FUNCTION;
	private boolean VISUALIZE;
	private int time, nets, flows, rep; 
	private String folder; 
	private boolean overwrite; 
	private boolean recalc;
	private float uncertaintyLevel = (float)0.4;

	//overwrite means: Create new scenario (ng+tg) even if it exists in this folder and calculate all schedules
	//recalc means: Keep ng+tg but recalculate all schedules
	
	
	public EvaluationScenarioExecutionWorker(boolean tEST_COST_FUNCTION, 
			boolean vISUALIZE, int time, int nets, int flows, int rep, 
			String folder, boolean overwrite, boolean recalc){
		
		TEST_COST_FUNCTION = tEST_COST_FUNCTION;
		VISUALIZE = vISUALIZE;
		this.time=time;
		this.nets=nets;
		this.flows=flows;
		this.rep=rep;
		this.folder=folder;
		this.overwrite=overwrite;
		this.recalc=recalc;

	}
	
	public Boolean call() throws Exception {
		try{
		NetworkGenerator ng;
		FlowGenerator tg;
		
		String path=folder+"rep_"+ rep+File.separator;
		//skip if folder exists
//		if(!new File(path).exists() || overwrite || recalc){
			System.out.println(path);
			new File(path).mkdirs();
//			System.out.println(recalc);
			if(overwrite){	//overwrite ng+tg even if existing
//				System.out.println("Creating Networks and Flows..");
				ng=new NetworkGenerator(nets, time);	//add network input data
				ng.addNetworkUncertainty(uncertaintyLevel);
				ng.addPositionUncertainty(uncertaintyLevel);
				ng.writeObject(path);
				tg = new FlowGenerator(time, flows);		//add application traffic input data
				tg.addUncertainty(uncertaintyLevel, ng.getTimeslots());
				tg.writeObject(path); 
			}else{
				//try to load ng and tg configurations from files (recalc or not existing yet)
				ng=NetworkGenerator.loadNetworkGenerator(path);
				tg=FlowGenerator.loadTrafficGenerator(path);
				
				//create if not existing; if one is missing calculate all schedules new (recalc=true)!
				if(ng==null){
					ng=new NetworkGenerator(nets, time);	//add network input data
					ng.addNetworkUncertainty(uncertaintyLevel);
					ng.addPositionUncertainty(uncertaintyLevel);
					ng.writeObject(path);
					recalc=true;
				}
				if(tg==null){
					tg = new FlowGenerator(time, flows);		//add application traffic input data
					tg.addUncertainty(uncertaintyLevel, ng.getTimeslots());
					tg.writeObject(path);
					recalc=true;
				}
				tg.setFlowIndices();	
			}
			
			

//			if(decomposition_heuristic){
//				//TODO test output
//				OptimizationScheduler o = new OptimizationScheduler(ng, tg);
//				o.writeDatFile(path);
//				
//				Decomposer d = new Decomposer(tg, ng);
//				d.decompose();
//				
//			} else {
				if (TEST_COST_FUNCTION) {
					// run optimization and compare results to results of the
					// cost function
					System.out.println("TEST_COST_FUNCTION active");
					OptimizationScheduler sched = new OptimizationScheduler(ng,
							tg);
					sched.testCostFunction();
					sched.calculateInstance(path, recalc||overwrite);
				} else {

					
					boolean first=true;
					int c_opt=0;
					Vector<Scheduler> scheds= EvaluationScenarioCreator.initSchedulers(ng, tg);
					for (Scheduler scheduler : scheds) {
						Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String date = formatter.format(Calendar.getInstance().getTime());
						System.out.println(" "+scheduler.getType() + ", starting at "+date);

						scheduler.calculateInstance(path, recalc||overwrite);
						
						//debug: check if optimization has lowest cost
//						if(first){
//							c_opt=scheduler.getCost();
//							first=false;
//						}else{
//							if(scheduler.getCost()<c_opt){
//								System.err.println("FIRST(OPT?) STILL NOT BEST");
//							}
//						}

					}

					if(VISUALIZE){
						Plot plot = new Plot(new VisualizationPack(ng, tg, scheds));
					}

				}
//			}
//		}
		return true;
	
	}catch(Exception e){
		e.printStackTrace();
		return false;
	}
	}
	
}