
% 2015-10-17 01:44:05

% my_logs\longTest1\0_1_3\rep_28\priorityMatch_log.m
scheduling_duration_us = 78148;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 0];
vioTp = [2500];
vioLcy = [15600];
vioJit = [20580];
cost_vio = 386800;
cost_switches = 1200;
cost_ch = 1068;
costTotal = 389068;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|6	|6	|6	|6	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]6	|6	|6	|6	|6	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|6	|6	|6	|6	|6	|6	|[40]6	|6	|6	|6	|	|	|	|	|	|	|
% ############### Network 3 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|6	|6	|6	|6	|6	|[20]6	|6	|6	|6	|6	|6	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 4 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|6	|	|	|	|	|	|
% ############### Network 5 ##############
% ############### Network 6 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|6	|6	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 7 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|6	|6	|6	|6	|[30]6	|6	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|

