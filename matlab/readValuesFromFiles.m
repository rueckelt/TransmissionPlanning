%values are in the form: 
%(varnames, schedulers, flows, time, networks, %repetitions)

%get schedulers from scheduler_logs (read from parameter_file.m)

function values = readValuesFromFiles( in_folder, varnames, t_max, n_max, f_max, rep_max, scheduler_logs )
%READVALUESFROMFILES Summary of this function goes here
%   Detailed explanation goes here
    [nof_schedulers, len] =size(scheduler_logs);  %length is unused
    [x nof_values] = (size(varnames))
    [nof_values, nof_schedulers, f_max, t_max, n_max,rep_max]
    values = zeros(nof_values, nof_schedulers, f_max, t_max, n_max,rep_max);
    schedulers = [];
    
%     duration_us = zeros(nof_schedulers, f_max, t_max, n_max,rep_max);
%     cost = zeros(nof_schedulers, f_max, t_max, n_max,rep_max);
%     out = zeros(nof_schedulers, f_max, t_max, n_max,rep_max);
%     duration_us(1,1,1,1,1)
    for s=1:nof_schedulers
        for f=1:f_max
            for t = 1:t_max
                for n = 1:n_max
                   for rep=1:rep_max
                       in_path = [in_folder '\' num2str(f-1) '_' num2str(t-1) '_' num2str(n-1) '\rep_' num2str(rep-1) '\'];
                       addpath(in_path);    %make path accessible
                       %optimization
                       sched=scheduler_logs(s,:);
                       fname = [in_path strtrim(sched)]  %array starts at index 1
                       if exist(fname, 'file') == 2 %does exist
                           run(fname);  %run script to get values
                           %store values to matrix[scheduler, flow,
                           %timeslot, network, repetition]
                           for(val=1:nof_values)
                               values(val, s,f,t,n,rep);
                           end
%                            duration_us(s,f,t,n,rep)  =   scheduling_duration_us;  
%                            cost(s,f,t,n,rep)         =   costTotal;
%                            out(s,f,t,n,rep)  = sum(eval('vioTp'));
                       end 
                       rmpath(in_path);
                       clearvars -except values t n f s rep t_max n_max f_max rep_max scheduler_logs nof_values nof_schedulers in_folder out_folder
                   end
               end 
            end
        end
     end
end

