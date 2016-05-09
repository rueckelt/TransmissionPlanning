%values are in the form: 
%(varnames, schedulers, flows, time, networks, %repetitions)

%get schedulers from scheduler_logs (read from parameter_file.m)

function [values, avail] = readValuesFromFiles( in_folder, varnames, t_max, n_max, f_max, rep_max, scheduler_logs )
%READVALUESFROMFILES Summary of this function goes here
%   Detailed explanation goes here
    [len, nof_schedulers] =size(scheduler_logs);  %length is unused
    [len, nof_values] = size(varnames);
    [nof_values, nof_schedulers, f_max, t_max, n_max,rep_max]
    values = zeros(nof_values, nof_schedulers, f_max, t_max, n_max,rep_max);
    avail = zeros(nof_values, nof_schedulers, f_max, t_max, n_max,rep_max);

    for f=1:f_max
        for t = 1:t_max
            for n = 1:n_max
               for rep=1:rep_max
                   in_path = [in_folder filesep  num2str(f-1) '_' num2str(t-1) '_' ...
                       num2str(n-1) filesep 'rep_' num2str(rep-1) filesep];
                   if exist(in_path,'dir')==7
                       addpath(in_path);    %make path accessible

                        for s=1:nof_schedulers
                           fname = [in_path scheduler_logs{s}]; 
                           if exist(fname, 'file') == 2 %skip non-existing files
                               run(fname);  %run script to get values
                               %store values to matrix[scheduler, flow,timeslot, network, repetition]
                               for val=1:nof_values    %skip non-existing values
                                   try
                                        values(val,s,f,t,n,rep) = eval(varnames{val});
                                        avail(val,s,f,t,n,rep) = 1;
                                   catch
    %                                    values(val, s,f,t,n,rep) = 0;
    %                                    ['failed to read: ' fname ': ' varnames{val}]
                                   end
                               end
                           else
                              % err='file not found'
                           end
                           clearvars -except in_path values avail varnames t n f s rep t_max n_max f_max rep_max scheduler_logs nof_values nof_schedulers in_folder out_folder
                        end
                       rmpath(in_path);
                   end
               end 
            end
        end
     end
end

