
% 2015-10-17 01:23:22

% my_logs\longTest1\0_1_1\rep_13\priority_log.m
scheduling_duration_us = 10205;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 0];

% cost function results
vioSt = [0];
vioDl = [0];
vioNon = [3850];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [41040];
vioJit = [19600];
cost_vio = 709390;
cost_switches = 400;
cost_ch = 1875;
costTotal = 711665;

% priority
% 
% ############### Network 0 ##############
% F0	|[0]5	|	|	|	|	|	|	|	|	|	|[10]	|	|5	|5	|5	|5	|5	|5	|5	|5	|[20]5	|5	|5	|5	|5	|5	|5	|5	|5	|5	|[30]5	|5	|5	|5	|5	|5	|5	|5	|5	|5	|[40]5	|5	|5	|5	|5	|5	|5	|5	|5	|	|
% ############### Network 1 ##############
% F0	|[0]	|5	|5	|5	|5	|5	|5	|5	|5	|5	|[10]5	|5	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|	|	|	|	|	|[30]	|	|	|	|	|	|	|	|	|	|[40]	|	|	|	|	|	|	|	|	|	|
