%matlab eval for scheduling

%stores results.mat file in in_folder for extracted log matrices
%delete this file to read logs from raw files

%in_folder = '..\my_logs\jakob';% 
%in_folder = '..\my_logs\vary_time';% 

in_folder = ['..' filesep 'my_logs' filesep 'vary_time'];% 
%in_folder = ['..' filesep 'my_logs' filesep 'vary_load'];% 
%in_folder = '..\my_logs\vary_nets';% 
%in_folder = '..\my_logs\vary_flows';% 
out_folder = [in_folder filesep 'eval'];
select = 2; %select data to read from files: 1=flows, 2=time, 3=net, 4=load, 5=monetary
force_read_data = 1;

%get paramters from file
parameter_file=[in_folder filesep 'parameters_log.m'];
if exist(parameter_file, 'file') ==2    %compare to 2 == is a file?
   run(parameter_file);
end
state = 'read param done'
%read matrix file if available, else create matrix file from raw log files
data_file=[in_folder filesep 'results.mat'];
avail_file=[in_folder filesep 'avail.mat'];
valuenames = {'violation cost', 'execution duration', 'time limits',...
     'min. throughput','unscheduled tokens', 'latency jitter','monetary cost'};
% valuenames = {'cost', 'duration', 'time_limits', ...
%     'throughput', 'unscheduled', 'latency_jitter', 'monetary_cost'};

if force_read_data <1 && exist(data_file, 'file') %read values from file if no force to reread and available
    %load(data_file);
    %load(avail_file);
else
    %read data from simulation output files
    valuestrings = {'costTotal', 'scheduling_duration_us', 'sum(vioSt)+sum(vioDl)', ...
        'sum(vioTp)', 'sum(vioNon)', 'sum(vioLcy)+sum(vioJit)', 'cost_ch'};
    tic
    [raw_values, avail] = readValuesFromFiles1D( in_folder, valuestrings,...
        max_flows, max_time, max_nets, load, w_monetary, select, max_rep, scheduler_logs);
    toc
    save(data_file, 'raw_values');
    save(avail_file, 'avail');
end
raw_values(2,:,:,:)=raw_values(2,:,:,:)./(1000*1000);     %time: �s to seconds
state='gathered data'
%calculate relative
% %plot
%rel_data = relative_to_opt(raw_values);
%save('rel_data.mat', 'rel_data');


%plot_data3(out_folder, raw_values, avail, valuenames, schedulers);  %vary time
plot_data4(out_folder, raw_values, avail, valuenames, schedulers, select);  %vary networks
%plot_data5(out_folder, raw_values, avail, valuenames, schedulers);  %vary schedulers
 
state='done'


%The functions you mention return H=0 when a test cannot reject the hypothesis of a normal distribution.
%Kolmogorow-Smirnow-Test kstest
%Anderson-Darling-Test adtest
%Lilliefors-Test [h,p,k,c] = lillietest()

