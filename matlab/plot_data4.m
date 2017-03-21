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
    
    if(select==1)
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
    

  %select data
for v=1:nof_vartypes
    avail_sq= squeeze(avail(v,:,:,:));  %vartype, scheduler, vary_param, rep

    if v<=2 && sum(avail_sq(:))>0%==nof_repetitions*nof_networks*nof_schedulers

        %squeezed data dimensions: scheduler, networks, repetitions
        data_sq = squeeze(data(v,:,:,:));

        %create path and labels
        filename = [out_folder filesep 'vary_' fname_part '_' vartypes{v} '.tikz']

        labels = {'Cost function value','Execution time in s'};
        my_ylabel=labels{v};
        
        %plot absolutes
        tikz_out_errorbar(filename, data_sq, my_ylabel,my_xlabel, my_xTickLabels, legendlabels2,0, 1,0);
       % tikz_out_errorbar(filename, data, my_ylabel, legendlabels, style_skip, logscale, zeroline)

        %Normalized Rating score (NRS)
        %  v_rnd - v_x
        % -------------
        % v_rnd - v_opt
          
        %calculate NRS
        data_score = data_sq(1:end-1,:,:);  %cut off opt and random
        for s=2:nof_schedulers-1  
            data_score(s,:,:) = (data_sq(end,:,:)-data_sq(s,:,:)) ./ (data_sq(end,:,:)-data_sq(1,:,:));
        end
        %Relative Optimization Potential (ROP)
        % v_rnd - v_opt
        % -------------
        %     v_rnd
        data_score(1,:,:) = (data_sq(end,:,:)-data_sq(1,:,:)) ./ (data_sq(end,:,:));
  
        data_score(isnan(data_score))=0;
       %plot NRS
        filename = [out_folder filesep 'vary_' fname_part '_NRS_' vartypes{v} '.tikz'];

        my_ylabel={sprintf('Normalized Rating Score (NRS) and\nRelative Optimization Potential (ROP)')};
        legend_names = scale_s(1:end-1)
        legend_names(1) = {'ROP'};
        %tikz_out_errorbar(filename, data_score, my_ylabel,my_xlabel, my_xTickLabels, scale_s(2:end-1),1, 0,0);
        tikz_out_errorbar(filename, data_score, my_ylabel,my_xlabel, my_xTickLabels, legend_names,0, 0,0);
       if(v==1) 
            nrs_jtp = squeeze(data_score(3,:,:))';   %3 = jtp, 2= ons, 1 = ns. 
            nrs_ons = squeeze(data_score(2,:,:))';   %3 = jtp, 2= ons, 1 = ns.  
            nrs_ns = squeeze(data_score(1,:,:))';   %3 = jtp, 2= ons, 1 = ns.
            [h p] =ttest2(nrs_jtp, nrs_ons);
            labels{v}
            h
            p
            gain_ons = median(nrs_jtp)-median(nrs_ons)
            gain_ns  = median(nrs_jtp)-median(nrs_ns)
       end
    end
end

if 1==1
     %detail plots
     for s=2:(nof_schedulers-1)
         %Relative Detail Score (RDS) of criterion v
         %detail cost
        % v_x - v_opt   detail cost difference
        %-------------
        % c_x - c_opt   total cost difference
         detail_cost_share=squeeze(data(3:end, s, :,:));
         for v=1:nof_vartypes-2
             detail_cost_share(v,:,:) = (squeeze(detail_cost_share(v,:,:))-squeeze(data(v+2,1,:,:))) ./ ...
                                        (squeeze(data(1,s,:,:))-squeeze(data(1,1,:,:)));
         end
        detail_cost_share(isnan(detail_cost_share))=0;
         avail_sq=squeeze(avail(3:end,s,:,:));

         %plot only if data is available
         if sum(avail_sq(:))>0
             %['plot for n:' num2str(n) ' f:' num2str(f)]
             %squeezed_data=squeeze(data_rel_sq(s,:,:));
             filename = [out_folder filesep 'vary_' fname_part '_RDS__' schedulers{s} '.tikz']

           % my_ylabel=[schedulers{s} ' detail cost relative to optimal schedule'];
           % tikz_out_errorbar(filename, data_rel_sq, my_ylabel,legendlabels, 0,1);
            my_ylabel = ['Relative Detail Score RDS(' scale_s{s} ')'];
            tikz_out_errorbar(filename, detail_cost_share, my_ylabel,my_xlabel, my_xTickLabels, legendlabels,0, 0,1);
         end
     end
end
  
 
end
