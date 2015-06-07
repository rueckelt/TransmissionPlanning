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

import java.io.FileNotFoundException;
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
		
	String[] log_scenario = {"nTime", "nChannels", "nRequests"};
	
	String[] logparam = {"allocatedChunks","non_allocated","cost_total", 
						"cost_violation", "dl_vio", "st_vio",
							"vioThroughput", "non_allo_vio", "nChunks", 
							"prefStartTime", "deadline", "availBW"};
	int[] dim = {3, 1, 0, 
				0, 1, 1,
				1, 1, 1,
				1, 1, 2};
	
	long executionTime=0;
	long genTime =0;
	private boolean firstInit=true;

	
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
	
	public IloOplModel getModel(){
		return opl_model;
	}
	
	public long getExecutionTimeNs(){
		return executionTime;
	}
	
	public void initializeData(String datasource_file){
		IloOplDataSource dataSource = oplF.createOplDataSource(datasource_file);
//		load data
		opl_model.addDataSource(dataSource);
//		generate
		genTime=System.nanoTime();
		opl_model.generate();			//must this be after new data arrived??????
		genTime=System.nanoTime()-genTime;
		System.out.println("#### GEN TIME [ns] ##### "+genTime);
	}
	
		
	
	/**
	 * used
	 * @param dataSource
	 * @return
	 */
	public boolean execute(String datasource_file) {
//		opl_model.end();
		//################ this data input does not work! ################
//		load data
		IloOplDataSource dataSource = oplF.createOplDataSource(datasource_file);
		opl_model.addDataSource(dataSource);
//		generate
		genTime=System.nanoTime();
		opl_model.generate();
		genTime=System.nanoTime()-genTime;
		System.out.println("#### GEN TIME [ns] ##### "+genTime);
		//################################################################
//		solve
		boolean feasible = false;
		try {
			long nano = System.nanoTime();	
			feasible = cplex.solve();
			nano = System.nanoTime()-nano;
			System.out.println("######### solver time: "+nano/1000+" µs");

//			if(feasible){
//				opl_model.postProcess();				
//			} else{
////				ParameterSet ps= new ParameterSet(IloCplex__ParameterSet = new IloCplex__ParameterSet(cplex.()));
////				opl.getSolutionGetter().
//				System.out.println("NOT_FEASIBLE");
//
//			}
//			System.out.println("**********************************************STATUS:\n"+cplex.getStatus());

//			System.out.println("VALUE: "+cplex.getObjValue());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR_EXEC");
			e.printStackTrace();
		}
		dataSource.end();
		return feasible;
	}
	
	/**
	 * used to print selected output to console
	 */
	public void printOutput(){

		System.out.println("cost_ch:\n"+ModelAccess.getValue(opl_model, "cost_total"));
		System.out.println("cost_ch:\n"+ModelAccess.getValue(opl_model, "cost_ch"));
		System.out.println("dl_vio:\n"+Arrays.toString(ModelAccess.getArray(opl_model, "dl_vio")));
		System.out.println("importance:\n"+Arrays.deepToString(ModelAccess.getArray2(opl_model, "importance")));
		System.out.println("allocatedChunks:\n"+Arrays.deepToString(ModelAccess.getArray3(opl_model, "allocatedBW")).replaceAll("],", "]\n"));
		System.out.println("unsched:\n"+Arrays.toString(ModelAccess.getArray(opl_model, "non_allocated")));
	}

	
	/**
	 * logging for evaluation
	 */
	public void log(String filename){
		String log="% t_n_i\n% execution time\n"+executionTime+"\n";
		
		PrintWriter pw=null;
		try {
			pw = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String path= "t_n_i_";
		for(int i =0;i<logparam.length;i++){
			log+="% "+logparam[i]+"\n";
			
			switch(dim[i]){
			case 0:		log+=ModelAccess.getValue(opl_model, logparam[i])+"\n";break;
			case 1:		log+=Arrays.toString(ModelAccess.getArray(opl_model, logparam[i])).replace("[", "").replace("]", "")+"\n";break;
			case 2:		//log+=Arrays.deepToString(ModelAccess.getArray2(opl_model, logparam[i])).replace("[", "").replace("],", ";]\n").replace("]", "")+"\n";
			log+=Arrays.deepToString(ModelAccess.getArray2(opl_model, logparam[i])).replace("[", "").replace("],", ";]\n").replace("]", "")+"\n";
			break;
			case 3: 	//log+=Arrays.deepToString(ModelAccess.getArray3(opl_model, logparam[i])).replace("[", "").replace("],", ";\n").replace("]", "")+"\n";break;
				log+=Arrays.deepToString(ModelAccess.getArray3(opl_model, logparam[i])).replace("],", "]\n")+"\n";break;
			}
		}
		
		pw.println(log);
		pw.flush();
		
	}


}
