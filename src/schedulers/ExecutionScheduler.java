package schedulers;

import adaptation.executor.Executor;
import ToolSet.JsonLogger;
import ToolSet.LogMatlabFormat;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class ExecutionScheduler extends Scheduler {
	private Executor exe;
	private String nameExtention="";
	String logFilePath;
	public ExecutionScheduler(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
	}
	public ExecutionScheduler(NetworkGenerator ng, FlowGenerator tg, String logFilePath) {
		this(ng, tg);
		this.logFilePath=logFilePath;
	}
	public ExecutionScheduler(NetworkGenerator ng, FlowGenerator tg, String logFilePath, String nameExtention) {
		this(ng, tg, logFilePath);
		this.nameExtention=nameExtention;
	}
	
	@Override
	protected void calculateInstance_internal(String logfile) {
//		System.out.println(logfile+", ; load result from \t"+logFilePath);
		int[][][] sp = LogMatlabFormat.load3DFromLogfile("schedule_f_t_n", logFilePath);//JsonLogger.json2Array(logFilePath);
		exe = new Executor(sp, ng, tg);
		
		exe.run(false);
		setSchedule(exe.getExecutedPlan());
	}

	@Override
	public String getType() {
		return "Exec"+nameExtention;
	}
	

}
