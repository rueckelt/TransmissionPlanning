
% 2015-10-17 01:04:19

% my_logs\longTest1\0_0_1\rep_2\priorityMatch_log.m
scheduling_duration_us = 2599;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0];

% cost function results
vioSt = [0];
vioDl = [250];
vioNon = [910];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [10830];
vioJit = [22380];
cost_vio = 343700;
cost_switches = 400;
cost_ch = 1020;
costTotal = 345120;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|	|	|	|	|	|[10]	|	|	|	|	|	|5	|5	|5	|5	|[20]5	|5	|5	|5	|5	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|5	|5	|5	|5	|5	|[10]5	|5	|5	|5	|5	|5	|	|	|	|	|[20]	|	|	|	|	|

