
% 2015-10-17 01:03:43

% my_logs\longTest1\1_0_0\rep_27\optimization_log.m
scheduling_duration_us = 77029;

% schedule
schedule_f_t_n(:,:,1) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 0];
schedule_f_t_n(:,:,2) = [0; 0; 0; 0; 0; 1; 0; 24; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [3234, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0, 0];
vioLcy = [8640, 0];
vioJit = [6480, 0];
cost_vio = 201894;
cost_switches = 0;
cost_ch = 1740;
costTotal = 203634;

% optimization
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|5	|5	|5	|5	|5	|[10]5	|5	|5	|5	|5	|5	|5	|5	|5	|5	|[20]5	|5	|5	|5	|	|
% F1	|[0]	|	|	|	|	|1	|	|24	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
