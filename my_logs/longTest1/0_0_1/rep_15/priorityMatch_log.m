
% 2015-10-17 01:04:22

% my_logs\longTest1\0_0_1\rep_15\priorityMatch_log.m
scheduling_duration_us = 2370;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0];

% cost function results
vioSt = [0];
vioDl = [50];
vioNon = [7630];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [29400];
vioJit = [21400];
cost_vio = 584800;
cost_switches = 400;
cost_ch = 875;
costTotal = 586075;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|	|	|	|	|	|	|	|[10]	|	|	|5	|5	|5	|5	|5	|5	|5	|[20]5	|5	|5	|5	|5	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|5	|5	|5	|5	|5	|5	|5	|[10]5	|5	|5	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

