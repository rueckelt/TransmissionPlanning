
% 2015-10-17 01:03:37

% my_logs\longTest1\0_0_0\rep_23\priorityMatch_log.m
scheduling_duration_us = 1320;

% schedule
schedule_f_t_n(:,:,1) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5];

% cost function results
vioSt = [0];
vioDl = [50];
vioNon = [1200];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [15625];
vioJit = [9000];
cost_vio = 258750;
cost_switches = 0;
cost_ch = 1250;
costTotal = 260000;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|5	|5	|5	|5	|5	|[10]5	|5	|5	|5	|5	|5	|5	|5	|5	|5	|[20]5	|5	|5	|5	|5	|

