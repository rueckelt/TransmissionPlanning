
% 2017-01-10 20:59:25

% my_logs/vary_time/3_0_3_2_2/16/GreedyOnline_log.m
scheduling_duration_us = 6980;

% schedule
schedule_f_t_n(:,:,1) = [0, 0, 0, 0, 25, 0, 0, 0; 0, 0, 0, 0, 39, 0, 0, 0; 0, 0, 0, 0, 30, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,2) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 47, 0, 0, 0; 0, 0, 0, 0, 47, 0, 0, 0; 0, 0, 0, 0, 47, 0, 0, 0; 0, 0, 0, 0, 47, 0, 0, 0; 0, 45, 0, 0, 0, 0, 0, 0; 0, 2, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,3) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 8, 0, 0, 0; 0, 0, 0, 0, 8, 0, 0, 0; 0, 0, 0, 0, 8, 0, 0, 0; 0, 8, 0, 0, 0, 0, 0, 0; 0, 8, 0, 0, 0, 0, 0, 0; 0, 8, 0, 0, 0, 0, 0, 0; 0, 8, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,4) = [0, 0, 0, 0, 22, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,5) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 43, 0, 0; 0, 0, 0, 0, 0, 43, 0, 0; 0, 0, 0, 0, 0, 0, 13, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,6) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 9, 0, 0, 0; 0, 0, 0, 0, 39, 0, 0, 0; 0, 22, 0, 0, 0, 0, 0, 0; 0, 53, 0, 0, 0, 0, 0, 0; 0, 53, 0, 0, 0, 0, 0, 0; 0, 53, 0, 0, 0, 0, 0, 0; 0, 16, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,7) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 0, 0, 4, 0, 0, 0, 0; 0, 0, 0, 4, 0, 0, 0, 0; 0, 0, 0, 0, 0, 4, 0, 0; 0, 0, 0, 0, 0, 4, 0, 0; 0, 0, 0, 0, 0, 0, 4, 0; 0, 0, 0, 0, 0, 0, 4, 0; 0, 0, 0, 0, 0, 4, 0, 0; 0, 0, 0, 0, 0, 4, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 0, 0, 4, 0, 0, 0; 0, 0, 0, 0, 4, 0, 0, 0; 0, 0, 0, 0, 4, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
schedule_f_t_n(:,:,8) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 17, 0; 0, 0, 0, 0, 0, 6, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];

% cost function results
vioSt = [0, 0, 0, 0, 0, 0, 0, 0];
vioDl = [0, 0, 0, 0, 0, 0, 0, 0];
vioNon = [0, 0, 252, 0, 0, 0, 468, 0];
vioTpMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0];
vioTp = [0, 0, 0, 800, 0, 0, 0, 865];
vioLcy = [0, 0, 768, 0, 0, 0, 576, 0];
vioJit = [0, 0, 144, 0, 0, 0, 415, 0];
cost_vio = 31822;
cost_switches = 11000;
cost_ch = 39600;
costTotal = 82422;

% GreedyOnline
% 
% ############### Network 0 ##############
% cap	|[0]68	|68	|68	|68	|68	|68	|68	|68	|68	|68	|[10]68	|68	|68	|68	|68	|68	|68	|68	|68	|68	|[20]68	|68	|68	|68	|68	|
% ############### Network 1 ##############
% cap	|[0]0	|0	|0	|0	|30	|61	|61	|61	|61	|30	|[10]0	|0	|0	|0	|0	|0	|0	|0	|0	|0	|[20]0	|0	|0	|0	|0	|
% F1	|[0]	|	|	|	|	|	|	|	|45	|2	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F2	|[0]	|	|	|	|8	|8	|8	|8	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F5	|[0]	|	|	|	|22	|53	|53	|53	|16	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F6	|[0]	|	|	|	|	|	|	|	|	|4	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 2 ##############
% cap	|[0]0	|0	|0	|0	|0	|0	|0	|0	|0	|0	|[10]0	|0	|20	|41	|41	|41	|41	|41	|20	|0	|[20]0	|0	|0	|0	|0	|
% F6	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|4	|	|[20]	|	|	|	|	|
% ############### Network 3 ##############
% cap	|[0]0	|0	|0	|0	|0	|0	|36	|72	|72	|72	|[10]72	|36	|0	|0	|0	|0	|0	|0	|0	|0	|[20]0	|0	|0	|0	|0	|
% F6	|[0]	|	|	|	|	|	|	|	|	|	|[10]4	|4	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 4 ##############
% cap	|[0]47	|47	|47	|47	|47	|47	|47	|47	|47	|47	|[10]47	|47	|47	|47	|47	|47	|47	|47	|47	|47	|[20]47	|47	|47	|47	|47	|
% F0	|[0]25	|39	|30	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F1	|[0]	|	|	|	|47	|47	|47	|47	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F2	|[0]	|8	|8	|8	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F3	|[0]22	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F5	|[0]	|	|9	|39	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F6	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|	|	|	|4	|[20]4	|4	|	|	|	|
% ############### Network 5 ##############
% cap	|[0]0	|0	|0	|0	|0	|0	|0	|0	|15	|31	|[10]47	|47	|47	|47	|47	|47	|31	|15	|0	|0	|[20]0	|0	|0	|0	|0	|
% F4	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|43	|43	|	|	|	|	|	|	|[20]	|	|	|	|	|
% F6	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|4	|4	|	|	|4	|4	|	|	|[20]	|	|	|	|	|
% F7	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|	|6	|	|	|	|[20]	|	|	|	|	|
% ############### Network 6 ##############
% cap	|[0]0	|0	|0	|0	|0	|0	|0	|0	|0	|0	|[10]0	|0	|0	|0	|21	|21	|0	|0	|0	|0	|[20]0	|0	|0	|0	|0	|
% F4	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|13	|	|	|	|	|	|[20]	|	|	|	|	|
% F6	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|4	|4	|	|	|	|	|[20]	|	|	|	|	|
% F7	|[0]	|	|	|	|	|	|	|	|	|	|[10]	|	|	|	|	|17	|	|	|	|	|[20]	|	|	|	|	|
% ############### Network 7 ##############
% cap	|[0]0	|0	|0	|0	|0	|12	|25	|25	|25	|25	|[10]12	|0	|0	|0	|0	|0	|0	|0	|0	|0	|[20]0	|0	|0	|0	|0	|

% Flow Drop Rate:
% Flow 0: 0%, type: BufferableStream, st 0, dl 12, UserImp 6, ImpUnsch 11, ImpLcy 0, ImpJit 0, ImpTp 13, Tokens 94
% Flow 1: 0%, type: Background, st 0, dl 18, UserImp 3, ImpUnsch 4, ImpLcy 0, ImpJit 0, ImpTp 0, Tokens 235
% Flow 2: 6%, type: Conversational, st 1, dl 8, UserImp 7, ImpUnsch 12, ImpLcy 4, ImpJit 4, ImpTp 25, Tokens 59
% Flow 3: 0%, type: Interactive, st 0, dl 8, UserImp 10, ImpUnsch 12, ImpLcy 0, ImpJit 0, ImpTp 24, Tokens 22
% Flow 4: 0%, type: BufferableStream, st 12, dl 24, UserImp 4, ImpUnsch 9, ImpLcy 0, ImpJit 0, ImpTp 12, Tokens 99
% Flow 5: 0%, type: Background (DL), st 0, dl 13, UserImp 6, ImpUnsch 5, ImpLcy 0, ImpJit 0, ImpTp 0, Tokens 245
% Flow 6: 11%, type: Conversational, st 9, dl 22, UserImp 6, ImpUnsch 13, ImpLcy 3, ImpJit 5, ImpTp 25, Tokens 58
% Flow 7: 0%, type: Interactive, st 15, dl 24, UserImp 8, ImpUnsch 8, ImpLcy 0, ImpJit 0, ImpTp 26, Tokens 23
% 

