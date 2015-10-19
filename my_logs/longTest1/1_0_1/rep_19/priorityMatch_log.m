
% 2015-10-17 01:04:32

% my_logs\longTest1\1_0_1\rep_19\priorityMatch_log.m
scheduling_duration_us = 2971;

% schedule
schedule_f_t_n(:,:,1) = [6, 0; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,2) = [0, 0; 0, 4; 0, 15; 0, 6; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [0, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [3000, 0];
vioLcy = [6624, 0];
vioJit = [18144, 0];
cost_vio = 305448;
cost_switches = 400;
cost_ch = 506;
costTotal = 306354;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|	|	|	|	|	|	|	|	|	|[10]	|6	|6	|6	|6	|6	|6	|6	|	|	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[10]6	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]	|4	|15	|6	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

