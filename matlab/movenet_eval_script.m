%MoVeNet Eval, Tobias Rueckelt, 10.6.2016

state='initialize parameters'
addpath('C:/Data/Tools/matlab/matlab2tikz');

%Parameter
force_read_data = 0;
skip=0;
N_sim_runs=100;
N_apps = 4;
output_folder='tikz';

mkdir(output_folder);
data_file = 'movenet_data.mat';

%Vars
prot_names={'MIPv6', 'MoVeNet', 'MoVeNet-MH'};
prot_folders={...
    ['frankfurt_urban_mipv6' filesep 'output' filesep '1_to_1' filesep],...
    ['frankfurt_urban' filesep 'output' filesep '1_to_1' filesep],...
    ['frankfurt_urban_multi_radio' filesep 'output' filesep '1_to_1' filesep]};

[~,n_prot]=size(prot_names);

%HO_delay and rtt 
HO_delay=cell(N_apps,n_prot);
rtt=cell(N_apps,n_prot);
packet_times=cell(N_apps,n_prot);

%##################################################
state='gather data'

if force_read_data <1 && exist(data_file, 'file') %read values from file if no force to reread and available
    load(data_file);
else
    %read data from csv files
    for prot=1:n_prot
        [HO_delay_tmp, rtt_tmp, packet_times_tmp]=read_logfiles(N_sim_runs, prot_folders{prot});
        for a=1:N_apps
           rtt{a,prot}=rtt_tmp{a};
           HO_delay{a,prot}=HO_delay_tmp{a};
           packet_times{a,prot}=packet_times_tmp{a};
        end
    end
    save(data_file, 'rtt', 'HO_delay');
end


%##################################################
state='calculate and plot'
if(true)
app_name = {'TCP CN->MA', 'TCP MA->CN', 'UDP CN->MA', 'UDP MA->CN'};
%rtt
for app=1:N_apps
    out_name=[output_folder filesep 'rtt_' num2str(app) '.tikz'];
    cdfPrint(out_name,[app_name{app} ': RTT/s'], rtt, app, prot_names);
end
%Handover delay
for app=1:N_apps
    out_name=[output_folder filesep 'HO_' num2str(app) '.tikz'];
    cdfPrint(out_name,[app_name{app} ': HO delay/s'], HO_delay, app, prot_names);
end
%packet numbers
for app=1:N_apps
    
end
end

%##################################################
state='calculate median values'

if(false)
%comparison while piggybacking: TCP + UDP MA->CN
 
HO_delay_m=[HO_delay{1,2}, HO_delay{2,2},HO_delay{4,2}];
HO_delay_mh=[HO_delay{1,3}, HO_delay{2,3},HO_delay{4,3}];
HO_delay_mip=[HO_delay{1,1}, HO_delay{2,1},HO_delay{4,1}];

median_mip=median(HO_delay_mip)
median_m=median(HO_delay_m)
median_mh=median(HO_delay_mh)

factor_m=median_mip/median_m
factor_mh=median_mip/median_mh


%comparison without piggbigacking UDP CN->MA
a=3;
app_name{a}
HO_delay_m_np=HO_delay{a,2};
HO_delay_mh_np=HO_delay{a,3};
HO_delay_mip_np=HO_delay{a,1};

median_mip_np=median(HO_delay_mip_np)
median_m_np=median(HO_delay_m_np)
median_mh_np=median(HO_delay_mh_np)

factor_m_np=median_mip_np/median_m_np
factor_mh_np=median_mip_np/median_mh_np

%make-before-break performance gain
median_m_all = median([HO_delay_m HO_delay_m_np])
median_mh_all = median([HO_delay_mh HO_delay_mh_np])
factor_m_mh = median_m_all/median_mh_all
end
    
rtt_m=[rtt{2,2},rtt{3,2},rtt{4,2}]; 
rtt_mh=[rtt{2,3},rtt{3,3},rtt{4,3}];
rtt_mip=[rtt{2,1},rtt{3,1}];  

median_m=median(rtt_m)
mean_m=mean(rtt_m)
median_mh=median(rtt_mh)
mean_mh=mean(rtt_mh)
median_mip=median(rtt_mip)
mean_mip=mean(rtt_mip)
    
state='done'
