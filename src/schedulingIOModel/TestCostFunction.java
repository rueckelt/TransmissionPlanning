package schedulingIOModel;
import ilog.opl.IloOplModel;

import java.util.Arrays;

import optimization.ModelAccess;


public class TestCostFunction extends CostFunction {

	private IloOplModel model;
	
	public TestCostFunction(NetworkGenerator ng, TrafficGenerator tg, IloOplModel model) {
		super(ng, tg);
		this.model=model;
	}
	
	
	protected void check(int value, String variable){
		int v = ModelAccess.getValue(model, variable);
		if(v!=value && v>=0){	//model access sometimes gives errors "expression overflow" here.. don't know why!
			System.err.println(variable + " model != cost-function:"+v+" != "+value);
			System.exit(0);
		}
	}
	protected void check(int[] value, String variable){
		int[] v = ModelAccess.getArray(model, variable);
		if(variable.equals("vioThroughput")){
			System.out.println("vioTP\nopt:"+Arrays.toString(v)+"\n"+Arrays.toString(value));
		}
		if(!Arrays.equals(v, value)){
			System.err.println(variable + " model != cost-function:\n"+Arrays.toString(v)+"\n"+Arrays.toString(value));
			System.exit(0);
		}
	}
	protected void check(int[][] value, String variable){
		int[][] v = ModelAccess.getArray2(model, variable);
		if(!Arrays.deepEquals(v, value)){
			System.err.println(variable + " model != cost-function:\n"+Arrays.deepToString(v)+"\n"+Arrays.deepToString(value));
			System.exit(0);
		}
	}
	
}
