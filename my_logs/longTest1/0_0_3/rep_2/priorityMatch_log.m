
% 2015-10-17 01:08:33

% my_logs\longTest1\0_0_3\rep_2\priorityMatch_log.m
scheduling_duration_us = 12457;

% schedule
schedule_f_t_n(:,:,1) = [5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0];
vioDl = [50];
vioNon = [539];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0];
vioLcy = [6300];
vioJit = [12635];
cost_vio = 214764;
cost_switches = 800;
cost_ch = 490;
costTotal = 216054;

% priorityMatch
% 
% ############### Network 0 ##############
% F0	|[0]5	|5	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|5	|5	|5	|
% ############### Network 1 ##############
% ############### Network 2 ##############
% F0	|[0]	|	|	|	|	|	|5	|5	|5	|5	|[10]5	|5	|5	|5	|5	|5	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 3 ##############
% ############### Network 4 ##############
% ############### Network 5 ##############
% ############### Network 6 ##############
% F0	|[0]	|	|5	|5	|5	|5	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 7 ##############
% F0	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|5	|5	|5	|5	|[20]5	|5	|	|	|	|
