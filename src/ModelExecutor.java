import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import ilog.opl.IloOplDataSource;
import ilog.opl.IloOplElement;
import ilog.opl.IloOplErrorHandler;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplModelSource;
import ilog.opl.IloOplSettings;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

/**
 * Execute IBM CPLEX model
 * 
 * @author QZ61P8
 *
 */


public class ModelExecutor {

	IloOplFactory oplF;
	IloOplErrorHandler errHandler;
	IloOplModelSource modelSource;
		
	IloCplex cplex;
	IloOplModel opl_model;
	IloOplSettings settings;
	IloOplModelDefinition definition;
	IloOplDataSource dataSource;
	
	ShedDataIO dataIO = new ShedDataIO();
	
	String[] logparam = {"allocatedChunks","non_allocated","cost_total", "cost_violation", "dl_vio", "st_vio", "vioThroughput", "non_allo_vio"};
	int[] dim = {3, 1, 1, 1, 1, 1, 1, 1};
	
	
	//Statistics
	Vector<Long> store = new Vector<Long>();
	
	public ModelExecutor(String model){
		
		IloOplFactory.setDebugMode(false);
		oplF = new IloOplFactory();
		
//		Create model
		errHandler = oplF.createOplErrorHandler(System.out);
		settings = oplF.createOplSettings(errHandler);
		modelSource = oplF.createOplModelSource(model);
		definition = oplF.createOplModelDefinition(modelSource, settings);
		try {
			
			cplex = oplF.createCplex();
		} catch (IloException e) {
			System.out.println("ERROR_INIT");
			e.printStackTrace();
		}
		opl_model = oplF.createOplModel(definition, cplex);

	}
	/**
	 * unused
	 * @return
	 */
	public boolean executeExample(){
		dataIO.setExample();
		return execute();
	}
	/**
	 * unused
	 */
	public boolean execute(){
		return execute(oplF.createOplDataSourceFromString(dataIO.getDataString(), "data"));
	}
	/**
	 * currently used
	 * @param datasource_file
	 * @return
	 */
	public boolean execute(String datasource_file){
		return execute(oplF.createOplDataSource(datasource_file));
	}
	
	public void resetStore(){
		store= new Vector<Long>();
	}
	
	public long getMedianNs(){
		long sum = 0;
		for (Long long1 : store) {
			sum+=long1;
		}
		return sum/store.size();
	}
	
	/**
	 * used
	 * @param dataSource
	 * @return
	 */
	public boolean execute(IloOplDataSource dataSource) {
//		load dataset
		
		
		long nanos = System.nanoTime();
		opl_model.addDataSource(dataSource);
		System.out.println("time to add data source:"+(System.nanoTime()-nanos));
		
//		generate
		opl_model.generate();
		boolean feasible = false;
		try {
			long nano = System.nanoTime();
			long time = System.currentTimeMillis();		
			feasible = cplex.solve();
			time = System.currentTimeMillis()-time;
			nano = System.nanoTime()-nano;
			System.out.println("######### solver time: "+(System.currentTimeMillis()-time)+" ms\t"+nano+" ns");
			
			store.add(nano);
			
			if(feasible){
				opl_model.postProcess();				
			} else{
//				ParameterSet ps= new ParameterSet(IloCplex__ParameterSet = new IloCplex__ParameterSet(cplex.()));
//				opl.getSolutionGetter().
				System.out.println("NOT_FEASIBLE");

			}
//			System.out.println("**********************************************STATUS:\n"+cplex.getStatus());

//			System.out.println("VALUE: "+cplex.getObjValue());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR_EXEC");
			e.printStackTrace();
		}
		
		return feasible;
	}
	
	/**
	 * used to print selected output to console
	 */
	public void printOutput(){
		
		System.out.println("cost_ch:\n"+getValue("cost_ch"));
		System.out.println("dl_vio:\n"+Arrays.toString(getArray("dl_vio")));
		System.out.println("importance:\n"+Arrays.deepToString(getArray2("importance")));
		System.out.println("allocatedChunks:\n"+Arrays.deepToString(getArray3("allocatedBW")).replaceAll("],", "]\n"));
		System.out.println("unsched:\n"+Arrays.toString(getArray("non_allocated")));
	}
	
	public void log(){
		String log="Logfile of OPL Model Executor";
		
		
		PrintWriter pw = new PrintWriter("log/opt_log "+new Date().toString());
		pw.println(log);
		pw.flush();
		
	}

	
	/**
	 * get Values from solution parameters
	 */
	public int getValue(String name){
		IloOplElement el= opl_model.getElement(name);
		return el.asInt();
	}
	public int[] getArray(String name){
		int[] array = null;
		 try {	
			IloOplElement el= opl_model.getElement(name);	
			int dim1=el.asIntMap().getSize();
			array = new int[dim1];
			for(int i = 1; i<dim1; i++){
					array[i-1]= el.asIntMap().get(i);
			 }
		} catch (Exception e1) {
			System.err.println(name +" is not a 1-dim IntMap");
			e1.printStackTrace();
			
		}
		return array;
	}
	
	public int[][] getArray2(String name){
		int[][] map = null;
		try{
			IloOplElement el= opl_model.getElement(name);
			int dim1=el.asIntMap().getSize();
			int dim2=el.asIntMap().getSub(1).getSize();
			
			map = new int[dim1][dim2];
			for(int i = 1; i<=dim1; i++){
				for(int j= 1; j<=dim2; j++){
					map[i-1][j-1]= el.asIntMap().getSub(i).get(j);
				}
			}
		} catch (Exception e1) {
			System.err.println(name +" is not a 2-dim IntMap");
			e1.printStackTrace();
			
		}
		return map;
	}
	
	public int[][][] getArray3(String name){
		int[][][] map = null;
		try{
			IloOplElement el= opl_model.getElement(name);
			int dim1=el.asIntMap().getSize();
			int dim2=el.asIntMap().getSub(1).getSize();
			int dim3=el.asIntMap().getSub(1).getSub(1).getSize();
			map = new int[dim1][dim2][dim3];
			
			for(int i = 1; i<=dim1; i++){	
				for(int j= 1; j<=dim2; j++){
					for(int k= 1; k<=dim3; k++){
						map[i-1][j-1][k-1]= el.asIntMap().getSub(i).getSub(j).get(k);
					}
				}
			}
		} catch (Exception e1) {
			System.err.println(name +" is not a 3-dim IntMap");
			e1.printStackTrace();
			
		}
		return map;
	}

}
