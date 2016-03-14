
% 2016-03-02 15:20:48

% my_logs\longTest1\1_0_0\rep_5\greedyFill_log.m
scheduling_duration_us = 545;

% schedule
schedule_f_t_n(:,:,1) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
schedule_f_t_n(:,:,2) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];

% cost function results
vioSt = [0, 0];
vioDl = [0, 0];
vioNon = [12400, 4500];
vioTpMin = [4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
vioTp = [9600, 0];
vioLcy = [0, 0];
vioJit = [0, 0];
cost_vio = 265000;
cost_switches = 0;
cost_ch = 0;
costTotal = 265000;

% greedyFill
% 
% ############### Network 0 ##############

