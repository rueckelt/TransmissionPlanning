
% 2015-10-17 01:04:50

% my_logs\longTest1\2_0_1\rep_26\priority_log.m
scheduling_duration_us = 4988;

% schedule
schedule_f_t_n(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,2) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,3) = [20, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,4) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 30, 0; 30, 0; 30, 0; 35, 0; 35, 0; 35, 0; 0, 0];

% cost function results
vioSt = [0, 0, 0, 0];
vioDl = [0, 0, 0, 0];
vioNon = [0, 2700, 0, 4248];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0, 0, 0, 0];
vioLcy = [8960, 0, 0, 0];
vioJit = [13440, 0, 0, 0];
cost_vio = 212392;
cost_switches = 400;
cost_ch = 2100;
costTotal = 214892;

% priority
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|5	|5	|5	|	|	|	|	|	|[10]	|	|5	|5	|5	|5	|5	|5	|5	|5	|[20]5	|	|	|	|	|
% F2	|[0]20	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F3	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|30	|30	|[20]30	|35	|35	|35	|	|
% ############### Network 1 ##############
% F0	|[0]	|	|	|	|	|5	|5	|5	|5	|5	|[10]5	|5	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
