
% 2015-10-17 01:04:19

% my_logs\longTest1\0_0_1\rep_3\priorityMatch_log.m
scheduling_duration_us = 1842;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 0, 0, 0, 0, 0];
vioTp = [1600];
vioLcy = [6750];
vioJit = [8960];
cost_vio = 173100;
cost_switches = 400;
cost_ch = 655;
costTotal = 174155;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|	|	|	|	|	|	|	|[10]5	|5	|5	|5	|5	|5	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|5	|5	|5	|5	|5	|5	|5	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

