
% 2015-10-17 01:05:57

% my_logs\longTest1\1_0_2\rep_9\optimization_log.m
scheduling_duration_us = 154413;

% schedule
schedule_f_t_n(:,:,1) = [0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 0, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 3, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];
schedule_f_t_n(:,:,2) = [0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 23; 0, 0, 0, 2; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [0, 0];
vioTpMin = [5, 5, 5, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [2200, 0];
vioLcy = [4752, 0];
vioJit = [528, 0];
cost_vio = 74800;
cost_switches = 0;
cost_ch = 842;
costTotal = 75642;

% optimization
% 
% ############### Network 0 ##############
% F0	|[0]	|	|	|5	|5	|5	|	|5	|5	|5	|[10]5	|5	|5	|5	|5	|3	|5	|5	|5	|5	|[20]5	|5	|	|	|	|
% ############### Network 1 ##############
% ############### Network 2 ##############
% ############### Network 3 ##############
% F1	|[0]	|	|	|	|	|	|	|	|	|	|[10]23	|2	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

