%values are in the form: 
%(varnames, schedulers, flows, time, networks, %repetitions)

%get schedulers from scheduler_logs (read from parameter_file.m)

function [values, avail] = readValuesFromFiles( in_folder, varnames, t_max, n_max, f_max, rep_max, scheduler_logs, max_only )
%READVALUESFROMFILES Summary of this function goes here
%   Detailed explanation goes here
    [len, nof_schedulers] =size(scheduler_logs);  %length is unused
    [len, nof_values] = size(varnames);
   
    %reduce matrix size to avoid out of memory
    if max_only>0
        values = zeros(nof_values, nof_schedulers, 1, 1, 1,rep_max);
        avail = zeros(nof_values, nof_schedulers, 1, 1, 1,rep_max);
        f_start=f_max;
        t_start=t_max;
        n_start=n_max;
    else
        values = zeros(nof_values, nof_schedulers, f_max, t_max, n_max,rep_max);
        avail = zeros(nof_values, nof_schedulers, f_max, t_max, n_max,rep_max);
        f_start=1;
        t_start=1;
        n_start=1;
    end
    
    [nof_values, nof_schedulers, f_max, t_max, n_max,rep_max]
    state='start reading values from log data'
    for f=f_start:f_max
        for t = t_start:t_max
            for n = n_start:n_max
               for rep=1:rep_max
                   in_path = [in_folder filesep  num2str(f-1) '_' num2str(t-1) '_' ...
                       num2str(n-1) filesep 'rep_' num2str(rep-1) filesep]
                   if exist(in_path,'dir')==7
                       addpath(in_path);    %make path accessible

                        for s=1:nof_schedulers
                           fname = [in_path scheduler_logs{s}];
                           if exist(fname, 'file') == 2 %skip non-existing files
                               run(fname);  %run script to get values
                               %store values to matrix[scheduler, flow,timeslot, network, repetition]
                               for val=1:nof_values    %skip non-existing values
                                   try
                                       if max_only>0
                                            values(val,s,1,1,1,rep) = eval(varnames{val});
                                            avail(val,s,1,1,1,rep) = 1;
                                       else
                                            values(val,s,f,t,n,rep) = eval(varnames{val});
                                            avail(val,s,f,t,n,rep) = 1;
                                       end
                                   catch
    %                                    values(val, s,f,t,n,rep) = 0;
    %                                    ['failed to read: ' fname ': ' varnames{val}]
                                   end
                               end
                           else
                               err=['file not found' fname]
                           end
                           clearvars -except in_path values avail max_only n_start f_start t_start varnames t n f s rep t_max n_max f_max rep_max scheduler_logs nof_values nof_schedulers in_folder out_folder
                        end
                       rmpath(in_path);
                   end
               end 
            end
        end
     end
end

