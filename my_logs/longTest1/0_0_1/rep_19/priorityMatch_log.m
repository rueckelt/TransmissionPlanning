
% 2015-10-17 01:04:23

% my_logs\longTest1\0_0_1\rep_19\priorityMatch_log.m
scheduling_duration_us = 2293;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [25480];
vioJit = [24160];
cost_vio = 546040;
cost_switches = 400;
cost_ch = 760;
costTotal = 547200;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|5	|	|	|	|	|[10]	|	|	|	|	|	|	|5	|5	|5	|[20]5	|5	|5	|5	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|5	|5	|5	|5	|[10]5	|5	|5	|5	|5	|5	|5	|	|	|	|[20]	|	|	|	|	|

