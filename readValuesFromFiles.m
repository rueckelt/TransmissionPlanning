function [ gen_time solve_time ] = readValuesFromFiles( in_folder, t_max, n_max, i_max, rep_max )
%READVALUESFROMFILES Summary of this function goes here
%   Detailed explanation goes here
    gen_time = zeros(t_max, n_max, i_max, rep_max);
    solve_time = zeros(t_max, n_max, i_max, rep_max);
    for t = 0:t_max-1
        for n = 0:n_max-1
           for i=0:i_max-1
               for rep=0:rep_max-1
                   in_path = [in_folder '\' num2str(t) '_' num2str(n) '_' num2str(i) '\rep_' num2str(rep) '\']
                   addpath(in_path);
                   fname = [in_path 'log.m'];
                   if exist(fname, 'file') == 2
                       run(fname);
                       if exist('duration_to_solve_model_us', 'var')
                          % duration_gen = generate_model + duration_to_solve_model_us;
                           %collect time data of all log files
                            gen_time(t+1,n+1,i+1,rep+1)=generate_model;
                            solve_time(t+1,n+1,i+1,rep+1)=duration_to_solve_model_us;
                       end
                       
                   end 
                   rmpath(in_path);

                  % time(rep+1)=duration;
                   clearvars -except gen_time solve_time t n i rep t_max n_max i_max rep_max in_folder out_folder
               end
           end 
        end
    end

end

