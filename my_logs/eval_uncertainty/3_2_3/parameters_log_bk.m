
% 2016-12-10 13:41:30

% my_logs/eval_uncertainty/
max_time = 5;
max_flows = 1;
max_nets = 1;
max_rep = 30;
move_uncertainty = 0.5;
net_uncertainty = 0.5;
flow_uncertainty = 0.5;

scheduler_logs= {'Optimization_log.m','Greedy_0_H2_log.m','GreedyOnlineOpp_0_H2_log.m','GreedyOnline_H2_log.m','true_adapted.m', 'executed.m', 'Random_log.m'};
schedulers= {'Opt','TP','ONS','NS','GA', 'Exe', 'Rnd'};

