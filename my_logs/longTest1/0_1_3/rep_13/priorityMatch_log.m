
% 2015-10-17 01:42:57

% my_logs\longTest1\0_1_3\rep_13\priorityMatch_log.m
scheduling_duration_us = 60960;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 1, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [300];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 4, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [3900];
vioLcy = [4144];
vioJit = [21336];
cost_vio = 326480;
cost_switches = 800;
cost_ch = 693;
costTotal = 327973;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|6	|1	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|6	|6	|6	|6	|6	|6	|6	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|6	|6	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 3 ##############
% ############### Network 4 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|6	|6	|[30]6	|6	|6	|6	|6	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 5 ##############
% ############### Network 6 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|6	|[20]6	|6	|6	|6	|6	|6	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 7 ##############

