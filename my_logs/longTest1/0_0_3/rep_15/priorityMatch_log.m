
% 2015-10-17 01:08:47

% my_logs\longTest1\0_0_3\rep_15\priorityMatch_log.m
scheduling_duration_us = 12559;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 0, 0, 0, 0, 6, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 2, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 4, 4, 0, 0, 0];
vioTp = [1400];
vioLcy = [3328];
vioJit = [9780];
cost_vio = 159588;
cost_switches = 800;
cost_ch = 560;
costTotal = 160948;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|6	|2	|	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|6	|6	|	|	|	|[20]	|	|	|	|	|
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|	|	|	|	|6	|6	|[10]6	|6	|6	|6	|6	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 3 ##############
% ############### Network 4 ##############
% ############### Network 5 ##############
% ############### Network 6 ##############
% F0	|[0]	|	|	|6	|6	|6	|6	|6	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 7 ##############

