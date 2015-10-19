
% 2015-10-17 01:04:25

% my_logs\longTest1\1_0_1\rep_0\priorityMatch_log.m
scheduling_duration_us = 3242;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,2) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 8; 0, 17; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [250, 0];
vioNon = [560, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0, 0];
vioLcy = [10500, 0];
vioJit = [14080, 0];
cost_vio = 203120;
cost_switches = 400;
cost_ch = 720;
costTotal = 204240;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|5	|5	|	|	|	|[10]	|	|	|	|	|	|	|5	|5	|5	|[20]5	|5	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|5	|5	|5	|[10]5	|5	|5	|5	|5	|5	|5	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]	|	|	|	|	|	|	|8	|17	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

