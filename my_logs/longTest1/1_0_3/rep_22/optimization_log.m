
% 2015-10-17 01:09:36

% my_logs\longTest1\1_0_3\rep_22\optimization_log.m
scheduling_duration_us = 364200;

% schedule
schedule_f_t_n(:,:,1) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 4; 0, 4, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,2) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 25, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [4590, 0];
vioTpMin = [4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [2800, 0];
vioLcy = [100, 0];
vioJit = [7420, 0];
cost_vio = 149100;
cost_switches = 400;
cost_ch = 218;
costTotal = 149718;

% optimization
% 
% ############### Network 0 ##############
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|4	|4	|4	|4	|4	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]	|	|	|	|	|	|	|	|	|25	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]4	|4	|4	|4	|4	|4	|4	|4	|4	|4	|[20]	|	|	|	|	|
% ############### Network 3 ##############
% ############### Network 4 ##############
% ############### Network 5 ##############
% ############### Network 6 ##############
% ############### Network 7 ##############
% F0	|[0]	|	|	|	|4	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

