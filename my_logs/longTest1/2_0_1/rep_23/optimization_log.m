
% 2015-10-17 01:04:48

% my_logs\longTest1\2_0_1\rep_23\optimization_log.m
scheduling_duration_us = 177222;

% schedule
schedule_f_t_n(:,:,1) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 4; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,2) = [0, 0; 0, 0; 0, 0; 0, 0; 25, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,3) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 25; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,4) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 33, 0; 33, 0; 33, 0; 33, 0; 33, 0; 33, 0; 33, 0; 33, 0; 33, 0; 33, 0];

% cost function results
vioSt = [0, 0, 0, 990];
vioDl = [0, 0, 0, 198];
vioNon = [11484, 0, 0, 9240];
vioTpMin = [4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [6800, 0, 0, 0];
vioLcy = [0, 0, 0, 0];
vioJit = [3500, 0, 0, 0];
cost_vio = 323048;
cost_switches = 0;
cost_ch = 4419;
costTotal = 327467;

% optimization
% 
% ############### Network 0 ##############
% F1	|[0]	|	|	|	|25	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F3	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|33	|33	|33	|33	|33	|[20]33	|33	|33	|33	|33	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|4	|4	|[10]4	|4	|4	|4	|4	|	|	|	|	|	|[20]	|	|	|	|	|
% F2	|[0]	|	|	|	|	|	|	|	|	|	|[10]25	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

