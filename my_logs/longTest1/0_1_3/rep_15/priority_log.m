
% 2015-10-17 01:43:04

% my_logs\longTest1\0_1_3\rep_15\priority_log.m
scheduling_duration_us = 51498;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 4, 0, 0, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 0];
vioTp = [2800];
vioLcy = [12670];
vioJit = [26292];
cost_vio = 459382;
cost_switches = 800;
cost_ch = 1180;
costTotal = 461362;

% priority
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[10]6	|6	|6	|6	|6	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|6	|6	|[30]6	|4	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 1 ##############
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|6	|6	|6	|6	|6	|6	|6	|6	|[40]6	|6	|	|	|	|	|	|	|	|	|
% ############### Network 3 ##############
% ############### Network 4 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|6	|6	|6	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 5 ##############
% ############### Network 6 ##############
% ############### Network 7 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|6	|6	|6	|6	|6	|[20]6	|6	|6	|6	|6	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
