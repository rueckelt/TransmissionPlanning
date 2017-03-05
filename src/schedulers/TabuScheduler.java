package schedulers;

import adaptation.tabusearch.BasicNeighborSolutionLocator;
import adaptation.tabusearch.IterationsStopCondition;
import adaptation.tabusearch.Solution;
import adaptation.tabusearch.StaticTabuList;
import adaptation.tabusearch.TabuSearch;
import adaptation.utils.Combination;
import adaptation.utils.Config;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class TabuScheduler extends Scheduler {

	public TabuScheduler(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
	}

	@Override
	protected void calculateInstance_internal(String logfile) {
		TabuSearch ts = setupTS(tg.getFlows().size(), 100);
		Config config = new Config(ng, tg);
		
		
//		Combination initialSolution = new Combination(config.getInitGenes()):
//		
//		ts.run(initialSolution);

	}
	
	 public TabuSearch setupTS(Integer tabuListSize, Integer iterations) { 
		 return new TabuSearch(new StaticTabuList(tabuListSize), new IterationsStopCondition(iterations), new BasicNeighborSolutionLocator()); 
	 } 

	@Override
	public String getType() {
		return "Tabu";
	}

}
