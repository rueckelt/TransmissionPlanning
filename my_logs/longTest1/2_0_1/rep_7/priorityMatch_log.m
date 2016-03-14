
% 2015-10-17 01:04:39

% my_logs\longTest1\2_0_1\rep_7\priorityMatch_log.m
scheduling_duration_us = 6367;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0];
schedule_f_t_n(:,:,2) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 18; 0, 7; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,3) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 29; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,4) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 17, 0; 17, 0; 17, 0; 17, 0; 17, 0; 17, 0; 17, 0; 17, 0; 17, 0];

% cost function results
vioSt = [0, 0, 0, 102];
vioDl = [250, 368, 0, 153];
vioNon = [936, 0, 0, 7595];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0, 0, 0, 0];
vioLcy = [9600, 0, 0, 0];
vioJit = [18725, 0, 0, 0];
cost_vio = 293982;
cost_switches = 400;
cost_ch = 1804;
costTotal = 296186;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|5	|	|	|	|	|[10]	|	|	|	|	|	|5	|5	|5	|5	|[20]5	|5	|5	|5	|5	|
% F3	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|17	|17	|17	|17	|[20]17	|17	|17	|17	|17	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|5	|5	|5	|5	|[10]5	|5	|5	|5	|5	|5	|	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]	|	|	|	|	|	|18	|7	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F2	|[0]	|	|	|	|	|	|	|29	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
