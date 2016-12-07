%out folder is basic output folder

%data is (varnames (=extime, cost,  throughput, ..), schedulers, flows, time, networks, %repetitions)

%####################################################
% this file varies number of networks on x axis
%####################################################
function [] = plot_data4(out_folder, data, avail, vartypes, schedulers)

[nof_vartypes, nof_schedulers, nof_flows, nof_time, nof_networks, nof_repetitions] = size(data);
    %what to plot?
    % use tikz_out(out_folder, data, plot_var_ftn, my_xlabel, my_ylabel, bound_hi, bound_lo, logscale)
        %out folder should contain subfolder for scheduler and subfolder for
        %extime/cost (type)
        %data is in dimensions [flows, time, networks, repetitions]
        %plot var ftn decides which variable (ftn?) is varied for plotting
        %logscale is applied if >0

    logscale=1;  
    
    scale_f = [1 2 4 8 16 32 64];
    scale_n = [1 2 4 8 16 32 64];
    scale_t = [25 50 100 200 400 800 1600];
    scale_s = schedulers; %{'Opt' 'TS' 'ONS' 'NS' 'Rnd'};
   
    addpath('matlab2tikz'); 
    
    legendlabels2=scale_s;
    legendlabels=vartypes(3:nof_vartypes);
    
    %path
    path = [out_folder];
    if exist(path, 'dir')==0
        mkdir(path);
    end
    
    %check_this=squeeze(data(3,:,5,1,4,:))
   % data_rel=relative_to_opt(data);
    %check_this2=squeeze(data_rel(3,:,5,1,4,:))
  %  nof_schedulers
                %select data
  for f=1:nof_flows
     for t = 1:nof_time
         %compareing plots
        for v=1:nof_vartypes
            avail_sq= squeeze(avail(v,:,f,t,:,:));
            
            if v<2 && sum(avail_sq(:))>0%==nof_repetitions*nof_networks*nof_schedulers
                
                %squeezed data dimensions: scheduler, networks, repetitions
                data_sq = squeeze(data(v,:,f,t,:,:));

                %create path and labels

                filename = [out_folder filesep 'vary_n__f_' num2str(scale_f(f)) ...
                    '_t_' num2str(scale_t(t)) '__' vartypes{v} '.tikz']
                
                labels = {'cost function value','execution time in s'};
                my_ylabel=labels{v};
%                tikz_out_errorbar(filename, data_sq, my_ylabel,legendlabels2,0, 1,0,1);
                
                %Normalized Rating score (NRS)
                %  v_rnd - v_x
                % -------------
                % v_rnd - v_opt
                data_score = data_sq(2:end-1,:,:);
                for s=1:nof_schedulers-2
                    data_score(s,:,:) = (data_sq(end,:,:)-data_sq(s+1,:,:)) ./ (data_sq(end,:,:)-data_sq(1,:,:));
                end
                
                
                %potential of scheduling in last line 
                %Scheduler Rating Score (NRS)
                % v_rnd,t0 - v_opt,t0
                %---------------------
                % v_rnd,ti - v_opt,ti
               % data_score(end,:,:) = 
                
                filename = [out_folder filesep 'vary_n__f_' num2str(scale_f(f)) ...
                    '_t_' num2str(scale_t(t)) '__' vartypes{v} '_rel.tikz'];

                my_ylabel='Normalized Rating Score (NRS)';
                tikz_out_errorbar(filename, data_score, my_ylabel,scale_s(2:end-1),1, 0,0,1);
            end
        end

         %detail plots
         for s=2:(nof_schedulers-1)
             %init
             %Relative Detail Score (RDS) of criterion v
             %detail cost
            % v_x - v_opt   detail cost difference
            %-------------
            % c_x - c_opt   total cost difference
             detail_cost_share=squeeze(data(3:end, s, f,t,:,:));
             for v=1:nof_vartypes-2
                 detail_cost_share(v,:,:) = (squeeze(detail_cost_share(v,:,:))-squeeze(data(v+2,1,f,t,:,:))) ./ ...
                                            (squeeze(data(1,s,f,t,:,:))-squeeze(data(1,1,f,t,:,:)));
             end
             %detail_cost_share
             %get relevant data for plot
             %data_rel_sq=squeeze(data_rel(3:end,s,f,:,n,:));    %3:end means for all detail variables of the cost function
             avail_sq=squeeze(avail(3:end,s,f,t,:,:));
             
             %plot only if data is available
             if sum(avail_sq(:))==nof_repetitions*nof_time*nof_schedulers%>0
                 %['plot for n:' num2str(n) ' f:' num2str(f)]
                 %squeezed_data=squeeze(data_rel_sq(s,:,:));
                 filename = [out_folder filesep 'vary_n__f_' num2str(scale_f(f)) ...
                '_t_' num2str(scale_t(t)) '__detail_' schedulers{s} '.tikz']

               % my_ylabel=[schedulers{s} ' detail cost relative to optimal schedule'];
               % tikz_out_errorbar(filename, data_rel_sq, my_ylabel,legendlabels, 0,1);
                my_ylabel = ['Relative Detail Score RDS(' scale_s{s} ')'];
                tikz_out_errorbar(filename, detail_cost_share, my_ylabel,legendlabels,0, 0,1,1);
             end
         end
      end
  end
  
 %do t-test for number of networks 
 %for n=1:nof_networks
 %    nets=scale_n(n)
 %   [h,p]=ttest2(data_score(1,n,:), data_score(3,n,:))
 %end
 
 
   %opt cost pie chart
% figure;
%  for s=1:nof_schedulers-1  
%      for t=1:nof_time
%          pie_title=['detail cost share optimal schedule, #time slots: ' num2str(scale_t(t))];
%         subplot(nof_schedulers, nof_time, s*nof_time+t);
%         title(pie_title);
%         pie_data=mean(squeeze(data(3:end,s,nof_flows,t,nof_networks,:)),2);
%         pie1=pie(pie_data);
%         if s*t==1
%             legend(vartypes(3:end));
%         end
%      end
%  end
end
