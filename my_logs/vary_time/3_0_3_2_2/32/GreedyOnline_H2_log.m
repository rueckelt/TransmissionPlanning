
% 2017-01-10 21:30:03

% my_logs/vary_time/3_0_3_2_2/32/GreedyOnline_H2_log.m
scheduling_duration_us = 7742;

% schedule
schedule_f_t_n(:,:,1) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 31, 0, 0, 0, 0, 0; 0, 0, 0, 0, 57, 0, 0, 0; 0, 0, 9, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,2) = [0, 0, 0, 0, 57, 0, 0, 0; 0, 0, 0, 0, 54, 0, 0, 0; 0, 0, 0, 0, 0, 0, 21, 0; 0, 0, 38, 0, 0, 0, 0, 0; 0, 0, 75, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,3) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 3, 0, 0, 0; 0, 0, 0, 0, 0, 0, 3, 0; 0, 0, 3, 0, 0, 0, 0, 0; 0, 0, 3, 0, 0, 0, 0, 0; 0, 0, 3, 0, 0, 0, 0, 0; 0, 0, 3, 0, 0, 0, 0, 0; 0, 0, 3, 0, 0, 0, 0, 0; 0, 0, 3, 0, 0, 0, 0, 0; 0, 0, 0, 0, 3, 0, 0, 0; 0, 0, 0, 0, 0, 3, 0, 0; 0, 0, 0, 0, 0, 3, 0, 0; 0, 0, 0, 0, 3, 0, 0, 0; 0, 0, 0, 0, 0, 3, 0, 0; 0, 0, 0, 0, 0, 3, 0, 0; 0, 0, 0, 0, 0, 0, 0, 3; 0, 0, 0, 0, 0, 0, 0, 3; 0, 0, 0, 0, 0, 0, 0, 3; 0, 0, 0, 0, 0, 0, 0, 3; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,4) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 11, 0, 0; 0, 0, 0, 0, 0, 12, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,5) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 73, 0, 0, 0, 0, 0; 0, 0, 20, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,6) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 57, 0, 0, 0; 0, 0, 0, 0, 57, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 79, 0, 0, 0, 0, 0; 0, 0, 48, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,7) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 6, 0, 0, 0, 0, 0; 0, 0, 0, 0, 6, 0, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 4, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 6; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,8) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 19, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0, 0, 0, 0, 0, 0, 0, 0];
vioDl = [0, 0, 0, 0, 0, 0, 0, 0];
vioNon = [0, 0, 936, 0, 0, 0, 810, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0];
vioTp = [0, 0, 0, 1730, 0, 0, 0, 1557];
vioLcy = [0, 0, 1089, 0, 0, 0, 484, 0];
vioJit = [0, 0, 575, 0, 0, 0, 180, 0];
cost_vio = 66580;
cost_switches = 15000;
cost_ch = 32820;
costTotal = 114400;

% GreedyOnline_H2
% 
% ############### Network 0 ##############
% cap	|[0]68	|68	|68	|68	|68	|68	|68	|68	|68	|68	|[10]68	|68	|68	|68	|68	|68	|68	|68	|68	|68	|[20]68	|68	|68	|68	|68	|
% ############### Network 1 ##############
% cap	|[0]0	|0	|0	|0	|0	|0	|0	|0	|0	|33	|[10]66	|100	|100	|100	|100	|100	|100	|66	|33	|0	|[20]0	|0	|0	|0	|0	|
% ############### Network 2 ##############
% cap	|[0]0	|0	|0	|41	|82	|82	|82	|82	|41	|0	|[10]0	|0	|0	|0	|0	|0	|0	|0	|0	|0	|[20]0	|0	|0	|0	|0	|
% F0	|[0]	|	|	|	|	|	|31	|	|9	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]	|	|	|38	|75	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F2	|[0]	|	|	|3	|3	|3	|3	|3	|3	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F4	|[0]	|	|	|	|	|	|	|73	|20	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F5	|[0]	|	|	|	|4	|79	|48	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F6	|[0]	|	|	|	|	|	|	|6	|6	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 3 ##############
% cap	|[0]0	|0	|0	|0	|0	|0	|0	|0	|0	|0	|[10]0	|0	|0	|77	|77	|77	|0	|0	|0	|0	|[20]0	|0	|0	|0	|0	|
% ############### Network 4 ##############
% cap	|[0]57	|57	|57	|57	|57	|57	|57	|57	|57	|57	|[10]57	|57	|57	|57	|57	|57	|57	|57	|57	|57	|[20]57	|57	|57	|57	|57	|
% F0	|[0]	|	|	|	|	|	|	|57	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]57	|54	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F2	|[0]	|3	|	|	|	|	|	|	|	|3	|[10]	|	|3	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F5	|[0]	|	|57	|57	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F6	|[0]	|	|	|	|	|	|	|	|	|6	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 5 ##############
% cap	|[0]0	|0	|0	|0	|0	|0	|0	|0	|0	|11	|[10]23	|23	|23	|23	|11	|0	|0	|0	|0	|0	|[20]0	|0	|0	|0	|0	|
% F2	|[0]	|	|	|	|	|	|	|	|	|	|[10]3	|3	|	|3	|3	|	|	|	|	|	|[20]	|	|	|	|	|
% F3	|[0]	|	|	|	|	|	|	|	|	|11	|[10]12	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F6	|[0]	|	|	|	|	|	|	|	|	|	|[10]6	|6	|4	|6	|6	|	|	|	|	|	|[20]	|	|	|	|	|
% F7	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|19	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 6 ##############
% cap	|[0]0	|0	|24	|49	|49	|49	|49	|49	|24	|0	|[10]0	|0	|0	|0	|0	|0	|0	|0	|0	|0	|[20]0	|0	|0	|0	|0	|
% F1	|[0]	|	|21	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F2	|[0]	|	|3	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 7 ##############
% cap	|[0]0	|0	|0	|0	|0	|0	|0	|0	|0	|0	|[10]0	|0	|27	|54	|54	|54	|54	|54	|27	|0	|[20]0	|0	|0	|0	|0	|
% F2	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|3	|3	|3	|3	|	|[20]	|	|	|	|	|
% F6	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|6	|6	|6	|	|	|[20]	|	|	|	|	|

% Flow Drop Rate:
% Flow 0: 0%, type: BufferableStream, st 6, dl 18, UserImp 4, ImpUnsch 6, ImpLcy 0, ImpJit 0, ImpTp 14, Tokens 97
% Flow 1: 0%, type: Background (DL), st 0, dl 18, UserImp 6, ImpUnsch 7, ImpLcy 0, ImpJit 0, ImpTp 0, Tokens 245
% Flow 2: 20%, type: Conversational, st 1, dl 19, UserImp 6, ImpUnsch 12, ImpLcy 3, ImpJit 5, ImpTp 25, Tokens 67
% Flow 3: 0%, type: Interactive, st 9, dl 23, UserImp 11, ImpUnsch 13, ImpLcy 0, ImpJit 0, ImpTp 26, Tokens 23
% Flow 4: 0%, type: BufferableStream, st 7, dl 15, UserImp 5, ImpUnsch 10, ImpLcy 0, ImpJit 0, ImpTp 14, Tokens 93
% Flow 5: 0%, type: Background (DL), st 0, dl 17, UserImp 5, ImpUnsch 7, ImpLcy 0, ImpJit 0, ImpTp 0, Tokens 245
% Flow 6: 9%, type: Conversational, st 7, dl 18, UserImp 9, ImpUnsch 15, ImpLcy 4, ImpJit 4, ImpTp 25, Tokens 70
% Flow 7: 0%, type: Interactive, st 12, dl 24, UserImp 12, ImpUnsch 11, ImpLcy 0, ImpJit 0, ImpTp 26, Tokens 19
% 

