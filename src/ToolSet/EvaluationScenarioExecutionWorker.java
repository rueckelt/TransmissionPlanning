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
	NetworkGenerator ng;
	FlowGenerator fg;
	private boolean TEST_COST_FUNCTION;
	private boolean VISUALIZE; 
	private String folder;  
	private boolean recalc;
	private float uncertaintyLevel = (float)0.2;

	//overwrite means: Create new scenario (ng+tg) even if it exists in this folder and calculate all schedules
	//recalc means: Keep ng+tg but recalculate all schedules
	
	//change
	public EvaluationScenarioExecutionWorker(NetworkGenerator ng, FlowGenerator fg, boolean tEST_COST_FUNCTION, 
			boolean vISUALIZE, String folder, boolean recalc){
		this.ng=ng;
		this.fg=fg;
		TEST_COST_FUNCTION = tEST_COST_FUNCTION;
		VISUALIZE = vISUALIZE;
		this.folder=folder;
		this.recalc=recalc;

	}
	
	public Boolean call() throws Exception {
		try{


			
			
		if (TEST_COST_FUNCTION) {
			// run optimization and compare results to results of the
			// cost function
			System.out.println("TEST_COST_FUNCTION active");
			OptimizationScheduler sched = new OptimizationScheduler(ng,
					fg);
			sched.testCostFunction();
			sched.calculateInstance(folder, recalc); //or when overwrite
		} else {

			
			boolean first=true;
			int c_opt=0;
			Vector<Scheduler> scheds= EvaluationScenarioCreator.initSchedulers(ng, fg);
			for (Scheduler scheduler : scheds) {
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = formatter.format(Calendar.getInstance().getTime());
				System.out.println(" "+scheduler.getType() + ", starting at "+date);

				scheduler.calculateInstance(folder, recalc); //or when overwrite

			}

			if(VISUALIZE){
				Plot plot = new Plot(new VisualizationPack(ng, fg, scheds));
			}

		}

		return true;
	
	}catch(Exception e){
		e.printStackTrace();
		return false;
	}
	}
	
}