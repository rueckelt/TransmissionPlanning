
% 2015-10-17 01:05:42

% my_logs\longTest1\0_0_2\rep_10\priorityMatch_log.m
scheduling_duration_us = 6442;

% schedule
schedule_f_t_n(:,:,1) = [5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 0, 0, 0, 5; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [50];
vioNon = [2232];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [16765];
vioJit = [12840];
cost_vio = 286983;
cost_switches = 600;
cost_ch = 820;
costTotal = 288403;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|5	|5	|5	|5	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|5	|5	|5	|5	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]5	|5	|5	|5	|5	|5	|5	|5	|5	|5	|[20]5	|	|	|	|	|
% ############### Network 2 ##############
% ############### Network 3 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|5	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

