
% 2015-10-17 01:04:31

% my_logs\longTest1\1_0_1\rep_16\optimization_log.m
scheduling_duration_us = 90361;

% schedule
schedule_f_t_n(:,:,1) = [4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 4, 0; 0, 0; 0, 0];
schedule_f_t_n(:,:,2) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 18; 0, 7; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [1863, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [0, 0];
vioLcy = [11776, 0];
vioJit = [2208, 0];
cost_vio = 142623;
cost_switches = 0;
cost_ch = 1062;
costTotal = 143685;

% optimization
% 
% ############### Network 0 ##############
% F0	|[0]4	|4	|4	|4	|4	|4	|4	|4	|4	|4	|[10]4	|4	|4	|4	|4	|4	|4	|4	|4	|4	|[20]4	|4	|4	|	|	|
% ############### Network 1 ##############
% F1	|[0]	|	|	|	|	|	|	|	|	|18	|[10]7	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
