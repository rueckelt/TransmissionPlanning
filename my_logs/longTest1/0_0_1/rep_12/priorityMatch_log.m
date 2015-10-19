
% 2015-10-17 01:04:21

% my_logs\longTest1\0_0_1\rep_12\priorityMatch_log.m
scheduling_duration_us = 2011;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 2, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 0; 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [1800];
vioLcy = [5376];
vioJit = [12360];
cost_vio = 175824;
cost_switches = 200;
cost_ch = 494;
costTotal = 176518;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|5	|5	|5	|2	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|5	|5	|5	|5	|5	|5	|5	|[20]5	|5	|5	|	|	|

