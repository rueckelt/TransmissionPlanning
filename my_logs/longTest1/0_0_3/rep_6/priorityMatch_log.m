
% 2015-10-17 01:08:38

% my_logs\longTest1\0_0_3\rep_6\priorityMatch_log.m
scheduling_duration_us = 8151;

% schedule
schedule_f_t_n(:,:,1) = [4, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 0, 0];
vioTp = [2800];
vioLcy = [448];
vioJit = [10880];
cost_vio = 155408;
cost_switches = 400;
cost_ch = 284;
costTotal = 156092;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]4	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|6	|[10]6	|6	|6	|6	|6	|6	|6	|6	|6	|	|[20]	|	|	|	|	|
% ############### Network 2 ##############
% ############### Network 3 ##############
% ############### Network 4 ##############
% F0	|[0]	|	|	|	|	|6	|6	|6	|6	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 5 ##############
% ############### Network 6 ##############
% ############### Network 7 ##############

