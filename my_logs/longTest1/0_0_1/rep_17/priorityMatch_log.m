
% 2015-10-17 01:04:22

% my_logs\longTest1\0_0_1\rep_17\priorityMatch_log.m
scheduling_duration_us = 1759;

% schedule
schedule_f_t_n(:,:,1) = [6, 0; 6, 0; 6, 0; 2, 0; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0];
vioTp = [2600];
vioLcy = [1600];
vioJit = [4900];
cost_vio = 72800;
cost_switches = 200;
cost_ch = 320;
costTotal = 73320;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|2	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|6	|6	|6	|6	|6	|6	|[10]6	|6	|6	|6	|	|	|	|	|	|	|[20]	|	|	|	|	|

