
% 2015-10-17 01:03:58

% my_logs\longTest1\3_0_0\rep_5\optimization_log.m
scheduling_duration_us = 296704;

% schedule
schedule_f_t_n(:,:,1) = [5; 5; 5; 5; 5; 5; 0; 0; 0; 5; 0; 5; 5; 5; 5; 5; 5; 5; 5; 5; 0; 0; 0; 0; 0];
schedule_f_t_n(:,:,2) = [0; 25; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
schedule_f_t_n(:,:,3) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
schedule_f_t_n(:,:,4) = [0; 0; 0; 0; 0; 0; 0; 0; 39; 34; 39; 34; 34; 0; 34; 30; 0; 0; 28; 0; 0; 0; 0; 0; 0];
schedule_f_t_n(:,:,5) = [0; 0; 0; 0; 0; 0; 0; 25; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
schedule_f_t_n(:,:,6) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 4; 4; 4; 4; 4; 4; 4; 4; 4; 0];
schedule_f_t_n(:,:,7) = [0; 0; 0; 0; 25; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
schedule_f_t_n(:,:,8) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 30; 0; 0; 0; 0; 0; 0; 0; 0];

% cost function results
vioSt = [0, 0, 0, 0, 0, 0, 0, 0];
vioDl = [0, 0, 0, 0, 0, 0, 0, 0];
vioNon = [0, 0, 120, 0, 0, 3520, 0, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 5, 5, 5, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [2000, 0, 0, 0, 0, 400, 0, 0];
vioLcy = [10240, 0, 0, 0, 0, 2880, 0, 0];
vioJit = [4320, 0, 0, 0, 0, 1620, 0, 0];
cost_vio = 249920;
cost_switches = 0;
cost_ch = 3451;
costTotal = 253371;

% optimization
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|5	|	|	|	|5	|[10]	|5	|5	|5	|5	|5	|5	|5	|5	|5	|[20]	|	|	|	|	|
% F1	|[0]	|25	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F3	|[0]	|	|	|	|	|	|	|	|39	|34	|[10]39	|34	|34	|	|34	|30	|	|	|28	|	|[20]	|	|	|	|	|
% F4	|[0]	|	|	|	|	|	|	|25	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F5	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|4	|4	|4	|4	|4	|[20]4	|4	|4	|4	|	|
% F6	|[0]	|	|	|	|25	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F7	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|30	|	|	|	|[20]	|	|	|	|	|

