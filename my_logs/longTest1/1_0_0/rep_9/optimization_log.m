
% 2015-10-17 01:03:40

% my_logs\longTest1\1_0_0\rep_9\optimization_log.m
scheduling_duration_us = 70080;

% schedule
schedule_f_t_n(:,:,1) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
schedule_f_t_n(:,:,2) = [0; 0; 0; 0; 0; 0; 0; 24; 1; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [9600, 0];
vioTpMin = [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [12000, 0];
vioLcy = [0, 0];
vioJit = [0, 0];
cost_vio = 216000;
cost_switches = 0;
cost_ch = 200;
costTotal = 216200;

% optimization
% 
% ############### Network 0 ##############
% F1	|[0]	|	|	|	|	|	|	|24	|1	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

