package optimization;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

/**
 * only one cplex can run in parallel.
 * @author Tobias
 *
 */
public class CplexSingelton {
	private static IloCplex cplex; 
	
	protected CplexSingelton(){
		//protect constructur
	}
	
	public static IloCplex getInstance(){
		if(cplex==null){
			try {
				cplex = new IloCplex();
			} catch (IloException e) {
				e.printStackTrace();
			}
		}else{
			try {
			cplex.clearCallbacks();
			cplex.clearCuts();
			cplex.clearLazyConstraints();
			cplex.clearModel();
			cplex.clearUserCuts();
			cplex.end();
			} catch (IloException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cplex;
	}
	
	
}
