
% 2015-10-17 01:09:18

% my_logs\longTest1\1_0_3\rep_9\priority_log.m
scheduling_duration_us = 17190;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0, 0, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 6, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 6, 0, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 6, 0, 0, 0, 0, 0, 0, 0; 2, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,2) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [0, 2800];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 5, 5, 5, 5, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [2300, 0];
vioLcy = [7888, 0];
vioJit = [18736, 0];
cost_vio = 337764;
cost_switches = 1000;
cost_ch = 422;
costTotal = 339186;

% priority
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|6	|2	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|6	|6	|6	|6	|6	|6	|6	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]6	|6	|6	|6	|6	|6	|6	|	|	|	|[20]	|	|	|	|	|
% ############### Network 3 ##############
% F0	|[0]	|	|6	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 4 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|6	|	|	|[20]	|	|	|	|	|
% ############### Network 5 ##############
% ############### Network 6 ##############
% ############### Network 7 ##############
