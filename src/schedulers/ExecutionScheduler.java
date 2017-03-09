package schedulers;

import adaptation.executor.Executor;
import ToolSet.JsonLogger;
import ToolSet.LogMatlabFormat;
import schedulingIOModel.FlowGenerator;
import schedulingIOModel.NetworkGenerator;

public class ExecutionScheduler extends Scheduler {
	private Executor exe;
	String logFilePath;
	public ExecutionScheduler(NetworkGenerator ng, FlowGenerator tg) {
		super(ng, tg);
	}
	public ExecutionScheduler(NetworkGenerator ng, FlowGenerator tg, String logFilePath) {
		super(ng, tg);
		this.logFilePath=logFilePath;
	}
	@Override
	protected void calculateInstance_internal(String logfile) {
		System.out.println(logfile+", ; \t"+logFilePath);
		int[][][] sp = LogMatlabFormat.load3DFromLogfile("schedule_f_t_n", logFilePath);//JsonLogger.json2Array(logFilePath);
		exe = new Executor(sp, ng, tg);
		
		exe.run(false);
		setSchedule(exe.getExecutedPlan());
	}

	@Override
	public String getType() {
		return "Exec";
	}
	

}
