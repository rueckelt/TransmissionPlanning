
% 2015-10-17 01:06:26

% my_logs\longTest1\2_0_2\rep_12\priority_log.m
scheduling_duration_us = 13131;

% schedule
schedule_f_t_n(:,:,1) = [6, 0, 0, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 6, 0; 0, 0, 0, 6; 0, 0, 0, 6; 0, 0, 0, 6; 0, 0, 0, 6; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 6, 0, 0, 0; 0, 0, 0, 0];
schedule_f_t_n(:,:,2) = [0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];
schedule_f_t_n(:,:,3) = [22, 0, 0, 0; 10, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];
schedule_f_t_n(:,:,4) = [0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 22, 0, 0, 0; 22, 0, 0, 0; 22, 0, 0, 0; 22, 0, 0, 0; 22, 0, 0, 0; 22, 0, 0, 0; 0, 0, 0, 0];

% cost function results
vioSt = [0, 0, 0, 0];
vioDl = [0, 0, 0, 0];
vioNon = [0, 2700, 0, 7980];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0, 0, 0, 0];
vioLcy = [17760, 0, 0, 0];
vioJit = [13020, 0, 0, 0];
cost_vio = 333120;
cost_switches = 600;
cost_ch = 1760;
costTotal = 335480;

% priority
% 
% ############### Network 0 ##############
% F0	|[0]6	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|6	|6	|6	|6	|6	|[20]6	|6	|6	|6	|	|
% F2	|[0]22	|10	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F3	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|22	|22	|[20]22	|22	|22	|22	|	|
% ############### Network 1 ##############
% ############### Network 2 ##############
% F0	|[0]	|6	|6	|6	|6	|6	|6	|6	|6	|6	|[10]6	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 3 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|6	|6	|6	|6	|	|	|	|	|	|[20]	|	|	|	|	|

