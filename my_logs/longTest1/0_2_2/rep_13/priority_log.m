
% 2015-10-17 13:07:04

% my_logs\longTest1\0_2_2\rep_13\priority_log.m
scheduling_duration_us = 62424;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 0, 0, 0, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [4000];
vioLcy = [27000];
vioJit = [10080];
cost_vio = 328640;
cost_switches = 200;
cost_ch = 1680;
costTotal = 330520;

% priority
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[10]6	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[20]6	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|[50]	|	|	|	|	|	|	|	|	|	|[60]	|	|	|	|	|	|	|	|	|	|[70]	|	|	|	|	|	|	|	|	|	|[80]	|	|	|	|	|	|	|	|	|	|[90]	|	|	|	|	|	|	|	|	|	|
% ############### Network 1 ##############
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[40]6	|	|	|	|	|	|	|	|	|	|[50]	|	|	|	|	|	|	|	|	|	|[60]	|	|	|	|	|	|	|	|	|	|[70]	|	|	|	|	|	|	|	|	|	|[80]	|	|	|	|	|	|	|	|	|	|[90]	|	|	|	|	|	|	|	|	|	|
% ############### Network 3 ##############
