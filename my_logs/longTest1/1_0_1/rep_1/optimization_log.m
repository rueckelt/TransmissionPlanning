
% 2015-10-17 01:04:26

% my_logs\longTest1\1_0_1\rep_1\optimization_log.m
scheduling_duration_us = 127510;

% schedule
schedule_f_t_n(:,:,1) = [4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 4, 0; 4, 0; 4, 0; 0, 0];
schedule_f_t_n(:,:,2) = [1, 0; 1, 0; 1, 0; 1, 0; 1, 0; 1, 0; 1, 0; 14, 0; 1, 0; 1, 0; 1, 0; 1, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [3402, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0, 0];
vioLcy = [7488, 0];
vioJit = [4872, 0];
cost_vio = 141858;
cost_switches = 400;
cost_ch = 768;
costTotal = 143026;

% optimization
% 
% ############### Network 0 ##############
% F0	|[0]4	|4	|4	|4	|4	|4	|4	|4	|4	|4	|[10]4	|	|	|	|	|	|	|	|	|	|[20]	|4	|4	|4	|	|
% F1	|[0]1	|1	|1	|1	|1	|1	|1	|14	|1	|1	|[10]1	|1	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|4	|4	|4	|4	|4	|4	|4	|4	|4	|[20]4	|	|	|	|	|

