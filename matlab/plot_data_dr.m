%out folder is basic output folder

%data is (varnames (=extime, cost,  throughput, ..), schedulers, flows, time, networks, %repetitions)

%####################################################
% this file varies number of networks on x axis
%####################################################
function [] = plot_data4(out_folder, data, avail, vartypes, schedulers, select)

[nof_vartypes, nof_schedulers, nof_count, nof_repetitions] = size(data);
    %what to plot?
    % use tikz_out(out_folder, data, plot_var_ftn, my_xlabel, my_ylabel, bound_hi, bound_lo, logscale)
        %out folder should contain subfolder for scheduler and subfolder for
        %extime/cost (type)
        %data is in dimensions [flows, time, networks, repetitions]
        %plot var ftn decides which variable (ftn?) is varied for plotting
        %logscale is applied if >0

    scale_s = schedulers; %{'Opt' 'TS' 'ONS' 'NS' 'Rnd'};
    i_start=0;
    
    if(select==1)
        i_start=2;
        my_xlabel = 'Data flows';
        fname_part = 'flows';
        my_xTickLabels = {'','4','','8','','16',''};
    elseif(select==2)
        my_xlabel = 'Time Slots';
        fname_part = 'time';
        my_xTickLabels = {'','25','50','100','200','400',''};
    elseif(select==3)
        my_xlabel = 'Networks';
        fname_part = 'networks';
        my_xTickLabels = {'','1','2','4','8','16','32',''};
    elseif(select==4)
        my_xlabel = 'Data traffic Load';
        fname_part = 'traffic_load';
        my_xTickLabels = {'','low','','medium','','high',''};
    elseif(select==5)
        my_xlabel = 'Monetary cost weight';
        fname_part = 'cost_weight';
        my_xTickLabels = {'','zero','','low','','medium','','high',''};
    elseif(select==6)
        my_xlabel = 'Movement prediction error';
        fname_part = 'pe_move';
        my_xTickLabels = {'','0.0','0.1','0.2','0.3','0.4','0.5',''};
    elseif(select==7)
        my_xlabel = 'Flow prediction error';
        fname_part = 'pe_flow';
        my_xTickLabels = {'','0.0','0.1','0.2','0.3','0.4','0.5',''};
    elseif(select==8)
        my_xlabel = 'Network prediction error';
        fname_part = 'pe_net';
        my_xTickLabels = {'','0.0','0.1','0.2','0.3','0.4','0.5',''};
    elseif(select==9)
        my_xlabel = 'Combined prediction error';
        fname_part = 'pe_comb';
        my_xTickLabels = {'','0.0','0.1','0.2','0.3','0.4','0.5',''};        
    end
    
   
    addpath('matlab2tikz'); 
    
    legendlabels2=scale_s;
    legendlabels=vartypes(3:nof_vartypes);
    
    %path
    path = [out_folder];
    if exist(path, 'dir')==0
        mkdir(path);
    end
    

  %select data: drop rate
 v=8
    avail_sq= squeeze(avail(v,:,:,:));  %vartype, scheduler, vary_param, rep

    if sum(avail_sq(:))>0 %==nof_repetitions*nof_networks*nof_schedulers

        %squeezed data dimensions: scheduler, networks, repetitions
        data_sq = squeeze(data(v,:,:,:))./1000000;

        %create path and labels
        filename = [out_folder filesep 'vary_' fname_part '_' vartypes{v} '.tikz']

        my_ylabel= 'Drop and long-term delay rate';
        
        %plot absolutes
        tikz_out_errorbar(filename, data_sq, my_ylabel,my_xlabel, my_xTickLabels, legendlabels2,0, 0,0);

        
%         %RELATIVE
%         data_sq_rel = data_sq(2:end,:,:);
%         for s=(2:nof_schedulers)
%             data_sq_rel(s-1,:,:) = data_sq(s,:,:)./data_sq(1,:,:);
%         end
%         data_sq_rel
%         %create path and labels
%         filename = [out_folder filesep 'vary_rel_' fname_part '_' vartypes{v} '.tikz']
% 
%         my_ylabel= 'Relative drop and long-term delay rate';
%         
%         %plot absolutes
%         tikz_out_errorbar(filename, data_sq_rel, my_ylabel,my_xlabel, my_xTickLabels, legendlabels2(2:end),0, 0,0);
        
    end


end
