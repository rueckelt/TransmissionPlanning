
% 2015-10-17 01:04:22

% my_logs\longTest1\0_0_1\rep_17\optimization_log.m
scheduling_duration_us = 60238;

% schedule
schedule_f_t_n(:,:,1) = [4, 0; 4, 0; 4, 0; 4, 0; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [3200];
vioJit = [3400];
cost_vio = 52800;
cost_switches = 400;
cost_ch = 400;
costTotal = 53600;

% optimization
% 
% ############### Network 0 ##############
% F0	|[0]4	|4	|4	|4	|	|	|	|	|	|	|[10]	|	|	|	|4	|4	|4	|4	|4	|4	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|4	|4	|4	|4	|4	|4	|[10]4	|4	|4	|4	|	|	|	|	|	|	|[20]	|	|	|	|	|

