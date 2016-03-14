
% 2015-10-17 01:43:38

% my_logs\longTest1\0_1_3\rep_23\priorityMatch_log.m
scheduling_duration_us = 81594;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 2, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0];
vioTp = [4800];
vioLcy = [22022];
vioJit = [20052];
cost_vio = 468740;
cost_switches = 800;
cost_ch = 1034;
costTotal = 470574;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|6	|6	|6	|6	|6	|2	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|6	|6	|6	|6	|6	|6	|[30]6	|6	|6	|6	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 2 ##############
% ############### Network 3 ##############
% ############### Network 4 ##############
% ############### Network 5 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|6	|6	|6	|6	|[20]6	|6	|6	|6	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
% ############### Network 6 ##############
% ############### Network 7 ##############
% F0	|[0]	|	|	|	|	|6	|6	|6	|6	|6	|[10]6	|6	|6	|6	|6	|6	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
