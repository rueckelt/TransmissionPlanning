package adaptation.tabusearch;
import java.util.ArrayList;
import java.util.List; 
 
import org.apache.commons.collections4.IteratorUtils; 
 
/**
 * Default implementation of the Tabu Search algorithm 
 * @author Alex Ferreira 
 * 
 */ 
public class TabuSearch { 
  
 private TabuList tabuList; 
 private StopCondition stopCondition; 
 private BestNeighborSolutionLocator solutionLocator; 
 private static ArrayList<ArrayList<Double>> convergeList;
 private static ArrayList<ArrayList<List<Solution>>> neighList;
  
 /**
  * Construct a {@link TabuSearch} object 
  * @param tabuList the tabu list used in the algorithm to handle tabus 
  * @param stopCondition the algorithm stop condition 
  * @param solutionLocator the best neightbor solution locator to be used in each algortithm iteration 
  */ 
 public TabuSearch(TabuList tabuList, StopCondition stopCondition, BestNeighborSolutionLocator solutionLocator) { 
  this.tabuList = tabuList; 
  this.stopCondition = stopCondition; 
  this.solutionLocator = solutionLocator; 
 } 
  
 /**
  * Execute the algorithm to perform a minimization. 
  * @param initialSolution the start point of the algorithm 
  * @return the best solution found in the given conditions 
  */ 
 public Solution run(Solution initialSolution) { 
  Solution bestSolution = initialSolution; 
  Solution currentSolution = initialSolution; 
   
  Integer currentIteration = 0; 
  ArrayList<Double> converge = new ArrayList<Double>();
  ArrayList<List<Solution>> neigborhoodList = new ArrayList<>();
  while (!stopCondition.mustStop(++currentIteration, bestSolution)) { 
   List<Solution> candidateNeighbors = currentSolution.getNeighbors(); 
   neigborhoodList.add(candidateNeighbors);
   if (candidateNeighbors.size() == 0) return bestSolution;
   List<Solution> solutionsInTabu = IteratorUtils.toList(tabuList.iterator()); 
    
   Solution bestNeighborFound = solutionLocator.findBestNeighbor(candidateNeighbors, solutionsInTabu); 
   if (bestNeighborFound.getValue() < bestSolution.getValue()) { 
    bestSolution = bestNeighborFound; 
   } 
   converge.add(bestSolution.getValue());
   tabuList.add(currentSolution); 
   currentSolution = bestNeighborFound; 
    
   tabuList.updateSize(currentIteration, bestSolution); 
  } 
  getNeighList().add(neigborhoodList);
  getConvergeList().add(converge);
   
  return bestSolution; 
 }

public static ArrayList<ArrayList<Double>> getConvergeList() {
	return convergeList;
}

public static void setConvergeList(ArrayList<ArrayList<Double>> convergeList) {
	TabuSearch.convergeList = convergeList;
}

public static ArrayList<ArrayList<List<Solution>>> getNeighList() {
	return neighList;
}

public static void setNeighList(ArrayList<ArrayList<List<Solution>>> neighList) {
	TabuSearch.neighList = neighList;
}
}
  