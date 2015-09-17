%matlab eval for scheduling

%stores results.mat file in in_folder for extracted log matrices
%delete this file to read logs from raw files

in_folder = 'my_logs\test2';% 'logs_time';
out_folder = 'my_logs\test2\graphs_comp';

%get paramters from file
parameter_file=[in_folder '\parameters_log.m'];
if exist(parameter_file, 'file') ==2
   run(parameter_file);
end

%read matrix file if available, else create matrix file from raw log files
results=[in_folder '\results.mat'];
if exist(results, 'file')
    load(results);
else
    tic
    [duration_us, cost] = readValuesFromFiles( in_folder, max_time, max_nets, max_flows, max_rep, scheduler_logs );
    toc
    save(results, 'cost', 'duration_us');
end


%draw boxplots
mkdir(out_folder);
ymin = 0;
ymax = 40;
%p=size(duration_us)
plotCompare(out_folder, duration_us);

%plot for each scheduler individually
% for s=1:size(scheduler_logs)
%     %str_value is 'cost' or 'solve duration"
%     %scale = 'solve duration in s' or 'cost function value'
%     sched=strtrim(scheduler_logs(s,:));
%     
%     s_cost=squeeze(cost(s,:,:,:,:));
%     plotExecutionTime(out_folder, 'Cost', 'cost function value',sched, cost)
% 
%     s_time=squeeze(duration_us(s,:,:,:,:));
%     plotExecutionTime(out_folder, 'Solve duration', 'solve duration in s',sched, s_time)
% end


clearvars




