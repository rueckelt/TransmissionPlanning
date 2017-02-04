%values are in the form: 
%(varnames, schedulers, flows, time, networks, %repetitions)

%get schedulers from scheduler_logs (read from parameter_file.m)

function [values, avail] = readValuesFromFiles( in_folder, varnames, f_max, t_max, n_max, load_max, mon_max, select, rep_max, scheduler_logs )
%READVALUESFROMFILES Summary of this function goes here
%   Detailed explanation goes here
    [~, nof_schedulers] =size(scheduler_logs); 
    [~, nof_values] = size(varnames);
   
    %define path and counter variable with select parameter
    if select==1    %vary flows
        path_prefix = [in_folder filesep];
        path_suffix = [ '_' num2str(t_max) '_' num2str(n_max) '_' num2str(load_max) '_' num2str(mon_max)];
        counter = 0:f_max;
    elseif select==2    %vary time
        path_prefix = [in_folder filesep num2str(f_max) '_' ];
        path_suffix = [ '_' num2str(n_max) '_' num2str(load_max) '_' num2str(mon_max)];
        counter = 0:t_max;
            
    elseif select==3    %vary networksf_max
        path_prefix = [in_folder filesep num2str(f_max) '_' num2str(t_max) '_'  ];
        path_suffix = ['_' num2str(load_max) '_' num2str(mon_max)];
        counter = 0:n_max;
    
    elseif select==4    %vary traffic load
        path_prefix = [in_folder filesep num2str(f_max) '_' num2str(t_max) '_' num2str(n_max) '_' ];
        path_suffix = ['_' num2str(mon_max)];
        counter = 1:load_max;
                
    elseif select ==5   %vary monetary cost weight
        path_prefix = [in_folder filesep num2str(f_max) '_' num2str(t_max) '_' num2str(n_max) '_' num2str(load_max) '_' ];
        path_suffix = [];
        counter = 1:mon_max;
                    
    end;
    
    [~, len]=size(counter);
    values = zeros(nof_values, nof_schedulers,len, rep_max);
    avail = zeros(nof_values, nof_schedulers,len, rep_max);

    
    [nof_values, nof_schedulers, f_max, t_max, n_max,rep_max]
    state='start reading values from log data'
       
    i=1;    %index for counter. since counters start differently from 0 or 1
    for c=counter
        for rep = 1:rep_max
            
           in_path = [path_prefix num2str(c) path_suffix ...
                filesep num2str(rep-1) filesep]
           if exist(in_path,'dir')==7
               addpath(in_path);    %make path accessible

                for s=1:nof_schedulers
                   fname = [in_path scheduler_logs{s}];
                   if exist(fname, 'file') == 2 %skip non-existing files
                       run(fname);  %run script to get values
                       %store values to matrix[value scheduler, var, repetition]
                       for val=1:nof_values    %skip non-existing values
                           try

                                values(val,s,i,rep) = eval(varnames{val});
                                avail(val,s,i,rep) = 1;

                           catch
    %                                    values(val, s,f,t,n,rep) = 0;
    %                                    ['failed to read: ' fname ': ' varnames{val}]
                           end
                       end
                   else
                       err=['file not found' fname]
                   end
                   clearvars -except in_path path_prefix path_suffix i values avail varnames counter c rep t_max n_max f_max rep_max scheduler_logs nof_values nof_schedulers in_folder
                end
               rmpath(in_path);
           end
        end 
    i=i+1;
    end
end

