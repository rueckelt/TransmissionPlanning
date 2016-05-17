
% 2016-05-17 07:05:41

% my_logs/eval_4_4_3_c/
max_time = 3;
max_flows = 4;
max_nets = 3;
max_rep = 30;
evaluate_max_only = 1;

scheduler_logs= {'Greedy_m3000_log.m','Greedy_m2900_log.m','Greedy_m2800_log.m','Greedy_m2700_log.m','Greedy_m2600_log.m','Greedy_m2500_log.m','Greedy_m2400_log.m','Greedy_m2300_log.m','Greedy_m2200_log.m','Greedy_m2100_log.m','Greedy_m2000_log.m','Greedy_m1900_log.m','Greedy_m1800_log.m','Greedy_m1700_log.m','Greedy_m1600_log.m','Greedy_m1500_log.m','Greedy_m1400_log.m','Greedy_m1300_log.m','Greedy_m1200_log.m','Greedy_m1100_log.m','Greedy_m1000_log.m','Greedy_m900_log.m','Greedy_m800_log.m','Greedy_m700_log.m','Greedy_m600_log.m','Greedy_m500_log.m','Greedy_m400_log.m','Greedy_m300_log.m','Greedy_m200_log.m','Greedy_m100_log.m','Greedy_0_log.m','Greedy_100_log.m','Greedy_200_log.m','Greedy_300_log.m','Greedy_400_log.m','Greedy_500_log.m','Greedy_600_log.m','Greedy_700_log.m','Greedy_800_log.m','Greedy_900_log.m','Greedy_1000_log.m','Greedy_1100_log.m','Greedy_1200_log.m','Greedy_1300_log.m','Greedy_1400_log.m','Greedy_1500_log.m','Greedy_1600_log.m','Greedy_1700_log.m','Greedy_1800_log.m','Greedy_1900_log.m','Greedy_2000_log.m','GreedyOnlineOpp_m65000_log.m','GreedyOnlineOpp_m64000_log.m','GreedyOnlineOpp_m63000_log.m','GreedyOnlineOpp_m62000_log.m','GreedyOnlineOpp_m61000_log.m','GreedyOnlineOpp_m60000_log.m','GreedyOnlineOpp_m59000_log.m','GreedyOnlineOpp_m58000_log.m','GreedyOnlineOpp_m57000_log.m','GreedyOnlineOpp_m56000_log.m','GreedyOnlineOpp_m55000_log.m','GreedyOnlineOpp_m54000_log.m','GreedyOnlineOpp_m53000_log.m','GreedyOnlineOpp_m52000_log.m','GreedyOnlineOpp_m51000_log.m','GreedyOnlineOpp_m50000_log.m','GreedyOnlineOpp_m49000_log.m','GreedyOnlineOpp_m48000_log.m','GreedyOnlineOpp_m47000_log.m','GreedyOnlineOpp_m46000_log.m','GreedyOnlineOpp_m45000_log.m','GreedyOnlineOpp_m44000_log.m','GreedyOnlineOpp_m43000_log.m','GreedyOnlineOpp_m42000_log.m','GreedyOnlineOpp_m41000_log.m','GreedyOnlineOpp_m40000_log.m','GreedyOnlineOpp_m39000_log.m','GreedyOnlineOpp_m38000_log.m','GreedyOnlineOpp_m37000_log.m','GreedyOnlineOpp_m36000_log.m','GreedyOnlineOpp_m35000_log.m','GreedyOnlineOpp_m34000_log.m','GreedyOnlineOpp_m33000_log.m','GreedyOnlineOpp_m32000_log.m','GreedyOnlineOpp_m31000_log.m','GreedyOnlineOpp_m30000_log.m','GreedyOnlineOpp_m29000_log.m','GreedyOnlineOpp_m28000_log.m','GreedyOnlineOpp_m27000_log.m','GreedyOnlineOpp_m26000_log.m','GreedyOnlineOpp_m25000_log.m','GreedyOnlineOpp_m24000_log.m','GreedyOnlineOpp_m23000_log.m','GreedyOnlineOpp_m22000_log.m','GreedyOnlineOpp_m21000_log.m','GreedyOnlineOpp_m20000_log.m','GreedyOnlineOpp_m19000_log.m','GreedyOnlineOpp_m18000_log.m','GreedyOnlineOpp_m17000_log.m','GreedyOnlineOpp_m16000_log.m','GreedyOnlineOpp_m15000_log.m','GreedyOnlineOpp_m14000_log.m','GreedyOnlineOpp_m13000_log.m','GreedyOnlineOpp_m12000_log.m','GreedyOnlineOpp_m11000_log.m','GreedyOnlineOpp_m10000_log.m','GreedyOnlineOpp_m9000_log.m','GreedyOnlineOpp_m8000_log.m','GreedyOnlineOpp_m7000_log.m','GreedyOnlineOpp_m6000_log.m','GreedyOnlineOpp_m5000_log.m','GreedyOnlineOpp_m4000_log.m','GreedyOnlineOpp_m3000_log.m','GreedyOnlineOpp_m2000_log.m','GreedyOnlineOpp_m1000_log.m','GreedyOnlineOpp_0_log.m','GreedyOnlineOpp_1000_log.m','GreedyOnlineOpp_2000_log.m','GreedyOnlineOpp_3000_log.m','GreedyOnlineOpp_4000_log.m','GreedyOnlineOpp_5000_log.m'};
schedulers= {'Greedy_m3000','Greedy_m2900','Greedy_m2800','Greedy_m2700','Greedy_m2600','Greedy_m2500','Greedy_m2400','Greedy_m2300','Greedy_m2200','Greedy_m2100','Greedy_m2000','Greedy_m1900','Greedy_m1800','Greedy_m1700','Greedy_m1600','Greedy_m1500','Greedy_m1400','Greedy_m1300','Greedy_m1200','Greedy_m1100','Greedy_m1000','Greedy_m900','Greedy_m800','Greedy_m700','Greedy_m600','Greedy_m500','Greedy_m400','Greedy_m300','Greedy_m200','Greedy_m100','Greedy_0','Greedy_100','Greedy_200','Greedy_300','Greedy_400','Greedy_500','Greedy_600','Greedy_700','Greedy_800','Greedy_900','Greedy_1000','Greedy_1100','Greedy_1200','Greedy_1300','Greedy_1400','Greedy_1500','Greedy_1600','Greedy_1700','Greedy_1800','Greedy_1900','Greedy_2000','GreedyOnlineOpp_m65000','GreedyOnlineOpp_m64000','GreedyOnlineOpp_m63000','GreedyOnlineOpp_m62000','GreedyOnlineOpp_m61000','GreedyOnlineOpp_m60000','GreedyOnlineOpp_m59000','GreedyOnlineOpp_m58000','GreedyOnlineOpp_m57000','GreedyOnlineOpp_m56000','GreedyOnlineOpp_m55000','GreedyOnlineOpp_m54000','GreedyOnlineOpp_m53000','GreedyOnlineOpp_m52000','GreedyOnlineOpp_m51000','GreedyOnlineOpp_m50000','GreedyOnlineOpp_m49000','GreedyOnlineOpp_m48000','GreedyOnlineOpp_m47000','GreedyOnlineOpp_m46000','GreedyOnlineOpp_m45000','GreedyOnlineOpp_m44000','GreedyOnlineOpp_m43000','GreedyOnlineOpp_m42000','GreedyOnlineOpp_m41000','GreedyOnlineOpp_m40000','GreedyOnlineOpp_m39000','GreedyOnlineOpp_m38000','GreedyOnlineOpp_m37000','GreedyOnlineOpp_m36000','GreedyOnlineOpp_m35000','GreedyOnlineOpp_m34000','GreedyOnlineOpp_m33000','GreedyOnlineOpp_m32000','GreedyOnlineOpp_m31000','GreedyOnlineOpp_m30000','GreedyOnlineOpp_m29000','GreedyOnlineOpp_m28000','GreedyOnlineOpp_m27000','GreedyOnlineOpp_m26000','GreedyOnlineOpp_m25000','GreedyOnlineOpp_m24000','GreedyOnlineOpp_m23000','GreedyOnlineOpp_m22000','GreedyOnlineOpp_m21000','GreedyOnlineOpp_m20000','GreedyOnlineOpp_m19000','GreedyOnlineOpp_m18000','GreedyOnlineOpp_m17000','GreedyOnlineOpp_m16000','GreedyOnlineOpp_m15000','GreedyOnlineOpp_m14000','GreedyOnlineOpp_m13000','GreedyOnlineOpp_m12000','GreedyOnlineOpp_m11000','GreedyOnlineOpp_m10000','GreedyOnlineOpp_m9000','GreedyOnlineOpp_m8000','GreedyOnlineOpp_m7000','GreedyOnlineOpp_m6000','GreedyOnlineOpp_m5000','GreedyOnlineOpp_m4000','GreedyOnlineOpp_m3000','GreedyOnlineOpp_m2000','GreedyOnlineOpp_m1000','GreedyOnlineOpp_0','GreedyOnlineOpp_1000','GreedyOnlineOpp_2000','GreedyOnlineOpp_3000','GreedyOnlineOpp_4000','GreedyOnlineOpp_5000'};

