
% 2015-10-17 01:05:38

% my_logs\longTest1\0_0_2\rep_2\priorityMatch_log.m
scheduling_duration_us = 5993;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 0, 6; 0, 0, 0, 6; 0, 0, 0, 6; 0, 0, 0, 6; 0, 0, 0, 6; 0, 0, 0, 6; 0, 0, 0, 6; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [60];
vioNon = [6426];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [18522];
vioJit = [16608];
cost_vio = 374544;
cost_switches = 600;
cost_ch = 774;
costTotal = 375918;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|6	|6	|6	|6	|
% ############### Network 1 ##############
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|	|6	|6	|6	|6	|6	|[10]6	|6	|6	|6	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 3 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|6	|6	|6	|6	|6	|6	|[20]6	|	|	|	|	|

