
% 2015-10-17 01:09:45

% my_logs\longTest1\1_0_3\rep_28\priority_log.m
scheduling_duration_us = 11981;

% schedule
schedule_f_t_n(:,:,1) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 0, 4, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,2) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [0, 2700];
vioTpMin = [5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5, 5, 5, 5, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [3600, 0];
vioLcy = [0, 0];
vioJit = [12754, 0];
cost_vio = 179740;
cost_switches = 400;
cost_ch = 176;
costTotal = 180316;

% priority
% 
% ############### Network 0 ##############
% ############### Network 1 ##############
% ############### Network 2 ##############
% ############### Network 3 ##############
% ############### Network 4 ##############
% F0	|[0]	|	|	|6	|6	|6	|6	|6	|6	|6	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 5 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]6	|6	|6	|6	|6	|6	|6	|	|	|	|[20]	|	|	|	|	|
% ############### Network 6 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|4	|	|	|[20]	|	|	|	|	|
% ############### Network 7 ##############

