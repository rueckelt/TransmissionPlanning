
% 2015-10-17 01:04:28

% my_logs\longTest1\1_0_1\rep_8\priorityMatch_log.m
scheduling_duration_us = 4224;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0];
schedule_f_t_n(:,:,2) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 5; 0, 15; 0, 5; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [50, 0];
vioNon = [2849, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0, 0];
vioLcy = [12250, 0];
vioJit = [8275, 0];
cost_vio = 257664;
cost_switches = 400;
cost_ch = 1070;
costTotal = 259134;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|5	|5	|5	|	|	|[10]	|	|	|	|	|	|	|	|	|5	|[20]5	|5	|5	|5	|5	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|5	|5	|[10]5	|5	|5	|5	|5	|5	|5	|5	|5	|	|[20]	|	|	|	|	|
% F1	|[0]	|	|	|	|	|	|	|	|5	|15	|[10]5	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

