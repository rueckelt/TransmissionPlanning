
% 2015-10-17 01:05:21

% my_logs\longTest1\3_0_1\rep_20\priorityMatch_log.m
scheduling_duration_us = 26020;

% schedule
schedule_f_t_n(:,:,1) = [6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 3, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 6; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,2) = [0, 0; 23, 0; 2, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,3) = [0, 0; 0, 0; 1, 0; 0, 0; 19, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,4) = [0, 0; 0, 0; 6, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 17; 0, 40; 0, 40; 0, 40; 0, 40; 0, 40; 0, 17; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,5) = [24, 0; 1, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,6) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0; 6, 0];
schedule_f_t_n(:,:,7) = [0, 0; 0, 0; 0, 0; 24, 0; 1, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,8) = [0, 0; 0, 0; 15, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0, 0, 0, 216, 0, 0, 0, 0];
vioDl = [60, 0, 0, 0, 0, 60, 0, 0];
vioNon = [0, 0, 0, 0, 0, 3696, 0, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 88, 48, 8, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [2200, 0, 0, 720, 0, 0, 0, 0];
vioLcy = [18144, 0, 0, 0, 0, 11880, 0, 0];
vioJit = [12600, 0, 0, 0, 0, 462, 0, 0];
cost_vio = 496508;
cost_switches = 400;
cost_ch = 4013;
costTotal = 500921;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]6	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[10]3	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]	|23	|2	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F2	|[0]	|	|1	|	|19	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F3	|[0]	|	|6	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F4	|[0]24	|1	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F5	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|6	|6	|6	|6	|6	|6	|[20]6	|6	|6	|6	|6	|
% F6	|[0]	|	|	|24	|1	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F7	|[0]	|	|15	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|6	|6	|6	|6	|6	|[20]6	|6	|	|	|	|
% F3	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|17	|40	|40	|40	|40	|[20]40	|17	|	|	|	|

