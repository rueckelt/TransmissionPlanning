%out folder is basic output folder

%data is (varnames (=extime, cost,  throughput, ..), schedulers, flows, time, networks, %repetitions)

function [] = plot_data3(out_folder, data, avail, vartypes, schedulers)

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
    scale_s = {'Opt' 'Gre' 'GreOn' 'Rnd'};
    
    
    addpath('matlab2tikz');  
    legendlabels2={'Opt','Gre','GreOn','Rnd'};
    legendlabels=vartypes(3:7);
    %path
    path = [out_folder];
    if exist(path, 'dir')==0
        mkdir(path);
    end
    
    check_this=squeeze(data(3,:,5,1,4,:))
    data_rel=relative_to_opt(data);
    check_this2=squeeze(data_rel(3,:,5,1,4,:))
  %  nof_schedulers
                %select data
  for f=1:nof_flows
     for n = 1:nof_networks
         %compareing plots
        for v=1:nof_vartypes
            avail_sq= squeeze(avail(v,:,f,:,n,:));

            if sum(avail_sq(:))==nof_repetitions*nof_time*nof_schedulers
                data_sq = squeeze(data(v,:,f,:,n,:));

                %create path and labels

                filename = [out_folder filesep 'vary_t__f_' num2str(scale_f(f)) ...
                    '_n_' num2str(scale_n(n)) '__' vartypes{v} '.tikz']
                
                my_ylabel=vartypes{v};
                tikz_out_errorbar(filename, data_sq, my_ylabel,legendlabels2, 1,0);
            end
        end

         %detail plots
         
         for s=2:3
                 data_rel_sq=squeeze(data_rel(3:end,s,f,:,n,:));
                 avail_sq=squeeze(avail(3:end,s,f,:,n,:));
             if sum(avail_sq(:))>0
                 ['plot for n:' num2str(n) ' f:' num2str(f)]
                 squeezed_data=squeeze(data_rel_sq(s,:,:))
                 filename = [out_folder filesep 'vary_t__f_' num2str(scale_f(f)) ...
                '_n_' num2str(scale_n(n)) '__detail_' schedulers{s} '.tikz']

                my_ylabel=[schedulers{s} ' detail cost relative to optimal schedule']
                tikz_out_errorbar(filename, data_rel_sq, my_ylabel,legendlabels, 0,1);
             end
         end
      end
  end
   %opt cost pie chart
figure;
 for s=1:nof_schedulers-1  
     for t=1:nof_time
         pie_title=['detail cost share optimal schedule, #time slots: ' num2str(scale_t(t))];
        subplot(nof_schedulers, nof_time, s*nof_time+t);
        title(pie_title);
        pie_data=mean(squeeze(data(3:end,s,nof_flows,t,nof_networks,:)),2);
        pie1=pie(pie_data);
        if s*t==1
            legend(vartypes(3:end));
        end
     end
 end
end
