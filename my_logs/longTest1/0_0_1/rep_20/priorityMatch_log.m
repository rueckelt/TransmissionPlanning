
% 2015-10-17 01:04:23

% my_logs\longTest1\0_0_1\rep_20\priorityMatch_log.m
scheduling_duration_us = 2227;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 0];

% cost function results
vioSt = [0];
vioDl = [250];
vioNon = [1080];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [15120];
vioJit = [20440];
cost_vio = 368900;
cost_switches = 400;
cost_ch = 990;
costTotal = 370290;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|	|	|	|	|	|[10]	|	|	|	|	|5	|5	|5	|5	|5	|[20]5	|5	|5	|5	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|5	|5	|5	|5	|5	|[10]5	|5	|5	|5	|5	|	|	|	|	|	|[20]	|	|	|	|	|

