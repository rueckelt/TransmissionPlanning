
% 2015-10-17 01:43:09

% my_logs\longTest1\0_1_3\rep_16\priorityMatch_log.m
scheduling_duration_us = 59797;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 3, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [300];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [4200];
vioLcy = [14112];
vioJit = [24024];
cost_vio = 383724;
cost_switches = 800;
cost_ch = 1164;
costTotal = 385688;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|6	|6	|6	|6	|6	|[30]3	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 1 ##############
% ############### Network 2 ##############
% ############### Network 3 ##############
% ############### Network 4 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|6	|[40]6	|	|	|	|	|	|	|	|	|	|
% ############### Network 5 ##############
% ############### Network 6 ##############
% F0	|[0]	|	|	|	|	|6	|6	|6	|6	|6	|[10]6	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 7 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[20]6	|6	|6	|6	|6	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
