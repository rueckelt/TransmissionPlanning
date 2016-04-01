%matlab eval for scheduling

%stores results.mat file in in_folder for extracted log matrices
%delete this file to read logs from raw files

in_folder = '..\my_logs\longTest1';% 'logs_time';
out_folder = 'graphics'% '..\my_logs\longTest1\graphs_tikz';

%get paramters from file
parameter_file=[in_folder '\parameters_log.m'];
if exist(parameter_file, 'file') ==2
   run(parameter_file);
end
state = 'read param done'
%read matrix file if available, else create matrix file from raw log files
results=[in_folder '\results.mat'];
valuenames = {'cost', 'duration', 'time_limits', ...
    'throughput', 'unscheduled', 'latency_jitter', 'monetary_cost'};
% if exist(results, 'file')
%     load(results);
% else
    valuestrings = {'costTotal', 'scheduling_duration_us', 'sum(vioSt)+sum(vioDl)', ...
        'sum(vioTp)', 'sum(vioNon)', 'sum(vioLcy)+sum(vioJit)', 'cost_ch'};
    tic
    out = readValuesFromFiles( in_folder, valuestrings, max_time, max_nets, max_flows, max_rep, scheduler_logs );
%     [cost, duration_us, vioTp] = out;
    toc
    save(results, 'cost', 'duration_us');
% end

state='gathered data'

[type_max, f_max , t_max, n_max,rep_max]=size(cost);
%calculate how good heuristics have been in comparison to optimization
cost_rel2=ones(type_max+1,f_max,t_max,n_max, rep_max);    %relative to optimization
 cost_relx = cost(2,:,:,:,:)./cost(4,:,:,:,:);
for f=1:f_max 
   for n = 1:n_max 
    for t = 1:t_max
       for rep=1:rep_max
           for type = 1:type_max
%                 cost_rel(type,f,t,n, rep) = cost(type,f,t,n, rep)/cost(1,f,t,n, rep);
                cost_rel2(type,f,t,n, rep) = cost(type,f,t,n, rep)/cost(1,f,t,n, rep);
           end
           cost_rel2(type_max+1,f,t,n,rep) = cost_relx(1,f,t,n,rep);
       end
    end
   end
end

tikz = false;

state = 'calc relative cost done'
%cost_mean=cost_mean(2:type_max,:,:,:);
%draw boxplots
mkdir(out_folder);

%plotCompare(out_folder, duration_us, 0,tikz);  
state = 'plot duration done'

%plot cost2 includes all values from plot cost!
% %plotCompare(out_folder, cost_rel, 1,tikz);
% state = 'plot cost done'

plotCompare(out_folder, cost_rel2, 2, tikz);
state = 'plot cost2 done, finished'
clearvars

%The functions you mention return H=0 when a test cannot reject the hypothesis of a normal distribution.
%Kolmogorow-Smirnow-Test kstest
%Anderson-Darling-Test adtest
%Lilliefors-Test [h,p,k,c] = lillietest()

