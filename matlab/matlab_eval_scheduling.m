%matlab eval for scheduling

%stores results.mat file in in_folder for extracted log matrices
%delete this file to read logs from raw files

in_folder = '..\my_logs\eval_4_4_3';% 'logs_time';
out_folder = [in_folder filesep 'tikz'];
force_read_data = 0;

%get paramters from file
parameter_file=[in_folder filesep 'parameters_log.m'];
if exist(parameter_file, 'file') ==2    %compare to 2 == is a file?
   run(parameter_file);
   max_flows=max_flows+1;
   max_nets=max_nets+1;
   max_time=max_time+1;
end
state = 'read param done'
%read matrix file if available, else create matrix file from raw log files
data_file=[in_folder filesep 'results.mat'];
avail_file=[in_folder filesep 'avail.mat'];
valuenames = {'cost', 'duration', 'time_limits', ...
    'throughput', 'unscheduled', 'latency_jitter', 'monetary_cost'};

if force_read_data <1 && exist(data_file, 'file') %read values from file if no force to reread and available
    load(data_file);
    load(avail_file);
else
    %read data from simulation output files
    valuestrings = {'costTotal', 'scheduling_duration_us', 'sum(vioSt)+sum(vioDl)', ...
        'sum(vioTp)', 'sum(vioNon)', 'sum(vioLcy)+sum(vioJit)', 'cost_ch'};
    tic
    [raw_values avail] = readValuesFromFiles( in_folder, valuestrings, max_time, max_nets, max_flows, max_rep, scheduler_logs );
    toc
    save(data_file, 'raw_values');
    save(avail_file, 'avail');
end

state='gathered data'

%calculate relative
rel_data = relative_to_opt(raw_values);
%plot
plot_data(out_folder, rel_data, avail, valuenames, schedulers);







% 
% [type_max, f_max , t_max, n_max,rep_max]=size(cost);
% %calculate how good heuristics have been in comparison to optimization
% cost_rel2=ones(type_max+1,f_max,t_max,n_max, rep_max);    %relative to optimization
%  cost_relx = cost(2,:,:,:,:)./cost(4,:,:,:,:);
% for f=1:f_max 
%    for n = 1:n_max 
%     for t = 1:t_max
%        for rep=1:rep_max
%            for type = 1:type_max
% %                 cost_rel(type,f,t,n, rep) = cost(type,f,t,n, rep)/cost(1,f,t,n, rep);
%                 cost_rel2(type,f,t,n, rep) = cost(type,f,t,n, rep)/cost(1,f,t,n, rep);
%            end
%            cost_rel2(type_max+1,f,t,n,rep) = cost_relx(1,f,t,n,rep);
%        end
%     end
%    end
% end
% 
% tikz = false;
% 
% state = 'calc relative cost done'
% %cost_mean=cost_mean(2:type_max,:,:,:);
% %draw boxplots
% mkdir(out_folder);
% 
% %plotCompare(out_folder, duration_us, 0,tikz);  
% state = 'plot duration done'
% 
% %plot cost2 includes all values from plot cost!
% % %plotCompare(out_folder, cost_rel, 1,tikz);
% % state = 'plot cost done'
% 
% plotCompare(out_folder, cost_rel2, 2, tikz);
% state = 'plot cost2 done, finished'
% clearvars

%The functions you mention return H=0 when a test cannot reject the hypothesis of a normal distribution.
%Kolmogorow-Smirnow-Test kstest
%Anderson-Darling-Test adtest
%Lilliefors-Test [h,p,k,c] = lillietest()

