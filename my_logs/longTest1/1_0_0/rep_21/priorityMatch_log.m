
% 2015-10-17 01:03:42

% my_logs\longTest1\1_0_0\rep_21\priorityMatch_log.m
scheduling_duration_us = 1410;

% schedule
schedule_f_t_n(:,:,1) = [6; 6; 6; 6; 6; 6; 6; 6; 6; 6; 6; 6; 6; 2; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
schedule_f_t_n(:,:,2) = [25; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [0, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [3300, 0];
vioLcy = [16000, 0];
vioJit = [14000, 0];
cost_vio = 333000;
cost_switches = 0;
cost_ch = 735;
costTotal = 333735;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[10]6	|6	|6	|2	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]25	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
