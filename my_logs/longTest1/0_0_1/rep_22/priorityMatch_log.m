
% 2015-10-17 01:04:23

% my_logs\longTest1\0_0_1\rep_22\priorityMatch_log.m
scheduling_duration_us = 2602;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [20160];
vioJit = [13440];
cost_vio = 302400;
cost_switches = 400;
cost_ch = 850;
costTotal = 303650;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|	|	|	|	|	|	|[10]	|	|	|	|5	|5	|5	|5	|5	|5	|[20]5	|5	|5	|5	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|5	|5	|5	|5	|5	|5	|[10]5	|5	|5	|5	|	|	|	|	|	|	|[20]	|	|	|	|	|

