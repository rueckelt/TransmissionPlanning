
% 2015-10-17 01:08:48

% my_logs\longTest1\0_0_3\rep_16\optimization_log.m
scheduling_duration_us = 166193;

% schedule
schedule_f_t_n(:,:,1) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [2000];
vioLcy = [0];
vioJit = [10000];
cost_vio = 120000;
cost_switches = 200;
cost_ch = 320;
costTotal = 120520;

% optimization
% 
% ############### Network 0 ##############
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|5	|5	|5	|5	|5	|5	|[10]5	|5	|5	|5	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 2 ##############
% ############### Network 3 ##############
% ############### Network 4 ##############
% ############### Network 5 ##############
% ############### Network 6 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|5	|5	|5	|5	|5	|5	|[20]	|	|	|	|	|
% ############### Network 7 ##############
