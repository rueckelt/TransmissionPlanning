
% 2015-10-17 01:04:26

% my_logs\longTest1\1_0_1\rep_2\priorityMatch_log.m
scheduling_duration_us = 2538;

% schedule
schedule_f_t_n(:,:,1) = [6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 4, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,2) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 18; 0, 7; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [0, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5, 5, 5, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [1600, 0];
vioLcy = [5580, 0];
vioJit = [24800, 0];
cost_vio = 255840;
cost_switches = 0;
cost_ch = 1662;
costTotal = 257502;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[10]6	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[20]4	|	|	|	|	|
% ############### Network 1 ##############
% F1	|[0]	|	|	|	|	|	|	|	|18	|7	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|

