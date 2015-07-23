import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.BooleanParam;
import ilog.opl.IloCustomOplDataSource;
import ilog.opl.IloOplDataElements;
import ilog.opl.IloOplDataHandler;
import ilog.opl.IloOplDataSource;
import ilog.opl.IloOplErrorHandler;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplModelSource;
import ilog.opl.IloOplSettings;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Execute IBM CPLEX model
 * 
 * @author QZ61P8
 *
 */


public class ModelExecutor {

	private final boolean PRESOLVE = true;
	
	IloOplFactory oplF;
	IloOplErrorHandler errHandler;
	IloOplModelSource modelSource;
		
	IloCplex cplex;
	
	IloOplModel opl_model;
	IloOplSettings settings;
	IloOplModelDefinition definition;
	IloOplDataElements dataElements;
	
	IloCustomOplDataSource dataSource;
	IloOplDataHandler dataHandler;
		
	String[] log_scenario = {"nTime", "nChannels", "nRequests"};
	
	String[] logparam = {"allocatedChunks","non_allocated",
							"dl_vio", "st_vio","vioLcy", "vioJit","vioTpMax","vioTpMin",
							"st_vio","dl_vio","cost_switch","cost_ch",
							"vioThroughput", "non_allo_vio", "nChunks", 
							"prefStartTime", "deadline", "availChunkBuckets",
							"closed_gaps"};
	int[] dim = {3, 1,  
				 1, 1,1,1,2,2,
				 1,1,0,0,
				1, 1, 1,
				1, 1, 2,
				3};
	
	long time =0;
	HashMap<String, Integer> timeMap; 

	
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
			cplex.setParam(IloCplex.BooleanParam.PreInd, PRESOLVE);
		} catch (IloException e) {
			System.out.println("ERROR_INIT");
			e.printStackTrace();
		}
		opl_model = oplF.createOplModel(definition, cplex);
	
	}
	
	public IloOplModel getModel(){
		return opl_model;
	}
	
	public void initializeData(String datasource_file){
	//	load data
		timeMap = new HashMap<String, Integer>();
	
		time=System.nanoTime();
		IloOplDataSource dataSource = oplF.createOplDataSource(datasource_file);
		opl_model = oplF.createOplModel(definition, cplex);
		opl_model.addDataSource(dataSource);
		time = System.nanoTime()-time;
		timeMap.put("create_model", (int) (time/1000000));
//		System.out.println("create_model: "+time/1000000);
		
		
		
	//	generate
		time=System.nanoTime();
		opl_model.generate();
	
		time = System.nanoTime()-time;
	}

	
	/**
	 * used
	 * @param dataSource
	 * @return
	 */
	public boolean execute(String datasource_file, String logfile) {
//		load data
		timeMap = new HashMap<String, Integer>();

		time=System.nanoTime();
		IloOplDataSource dataSource = oplF.createOplDataSource(datasource_file);
		opl_model = oplF.createOplModel(definition, cplex);
		opl_model.addDataSource(dataSource);
		time = System.nanoTime()-time;
		timeMap.put("create_model", (int) (time/1000000));
//		System.out.println("create_model: "+time/1000000);
		System.out.println("USE DATASOURCE: "+datasource_file);
//		generate
		time=System.nanoTime();
		opl_model.generate();

		time = System.nanoTime()-time;

		dataElements = opl_model.makeDataElements();
		
		
		timeMap.put("generate_model", (int) (time/1000000));
//		System.out.println("generate_model: "+time/1000000);
		
//		solve
		boolean feasible = false;
		try {
			time = System.nanoTime();	
			feasible = cplex.solve();
			time = System.nanoTime()-time;
			timeMap.put("duration_to_solve_model_us", (int) (time/1000000));
//			System.out.println("duration_to_solve_model_us: "+time/1000000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log(logfile);
//		dataSource.end();
//		opl_model.end();
		return feasible;
	}
	
	public int[][][] getSchedule_f_t_n(){
		return ModelAccess.getArray3(opl_model, "allocatedChunks");
	}
	
	/**
	 * logging for evaluation
	 */
	public void log(String filename){
		//log timing
		String log="% t_n_i\n% time:\n";
		for(String s:timeMap.keySet()){
			log+=s+" = "+timeMap.get(s)+";\n";
		}
		//log violation
//		try {
//			System.out.println("OBJECTIVE=" +cplex.getObjValue());
//		} catch (IloException e1) {
//			e1.printStackTrace();
//		}
		
		PrintWriter pw=null;
		try {
			pw = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
//		String path= "t_n_i_";
		for(int i =0;i<logparam.length;i++){
			log+="% "+logparam[i]+"\n";
			
			switch(dim[i]){

			case 0:		log+=logValue(logparam[i],ModelAccess.getValue(opl_model, logparam[i])); break;
			case 1: 	log+=logDim1(logparam[i],ModelAccess.getArray(opl_model, logparam[i])); break;
			case 2: 	log+=logDim2(logparam[i],ModelAccess.getArray2(opl_model, logparam[i])); break;
			case 3: 	log+=logDim3(logparam[i],ModelAccess.getArray3(opl_model, logparam[i])); break;
			}
		}
		
		pw.println(log);
		pw.flush();
		pw.close();
		
	}
	
	public void testLog(){
		int[] d1={1,2,3};
		int[][] d2 = {d1, d1, d1, d1};
		int[][][] d3 = {d2, d2, d2};
		
		System.out.println(logValue("a", 5));
		System.out.println(logDim1("b", d1));
		System.out.println(logDim2("c", d2));
		System.out.println(logDim3("d", d3));
	}
	
	/**
	 * Log outputs for matlab input
	 * @param name
	 * @param value
	 * @return
	 */
	private String logValue(String name, int value){
		 return name+" = "+value+";\n";
	}
	private String logDim1(String name, int[] value){
		return name+" = "+Arrays.toString(value)+";\n";
	}
	private String logDim2(String name, int[][] value){
		String s = name +" = [";
		for (int i = 0; i < value.length; i++) {
			if(i>0){
				s+="; ";
			}
			for (int j = 0; j < value[0].length; j++) {
				if(j>0){
					s+=", ";
				}
				s+=value[i][j];
			}
		}
		return s+"];\n";
	}
	private String logDim3(String name, int[][][] value){
		String s="";
		for (int i = 0; i < value.length; i++) {
			String name2=name+"(:,:,"+(i+1)+")";
			s+=logDim2(name2, value[i]);
		}
		return s;
	}


}
