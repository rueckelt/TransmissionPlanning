
% 2015-10-17 01:09:35

% my_logs\longTest1\1_0_3\rep_21\optimization_log.m
scheduling_duration_us = 373177;

% schedule
schedule_f_t_n(:,:,1) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,2) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 25, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [1232, 0];
vioTpMin = [4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [1600, 0];
vioLcy = [0, 0];
vioJit = [9128, 0];
cost_vio = 131560;
cost_switches = 200;
cost_ch = 210;
costTotal = 131970;

% optimization
% 
% ############### Network 0 ##############
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|4	|4	|4	|4	|4	|[20]4	|	|	|	|	|
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|4	|4	|4	|4	|4	|4	|[10]4	|4	|4	|4	|4	|	|	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|25	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 3 ##############
% ############### Network 4 ##############
% ############### Network 5 ##############
% ############### Network 6 ##############
% ############### Network 7 ##############

