%out folder is basic output folder

%data is (varnames (=extime, cost,  throughput, ..), schedulers, flows, time, networks, %repetitions)

function [] = plot_data2(out_folder, data, avail, vartypes, schedulers)

[nof_vartypes, nof_schedulers, nof_flows, nof_time, nof_networks, nof_repetitions] = size(data);
    %what to plot?
    % use tikz_out(out_folder, data, plot_var_ftn, my_xlabel, my_ylabel, bound_hi, bound_lo, logscale)
        %out folder should contain subfolder for scheduler and subfolder for
        %extime/cost (type)
        %data is in dimensions [flows, time, networks, repetitions]
        %plot var ftn decides which variable (ftn?) is varied for plotting
        %logscale is applied if >0
       
    logscale=1;  
    
    addpath('matlab2tikz');  
    timeslots=[25,50,100,200,400,800,1600];

  %  nof_schedulers
    for t=1:nof_time
    %for t=nof_time:nof_time
                %select data
        for v=1:nof_vartypes
            tmp = squeeze(data(v,:,:,:,:,:));
            
            if v<=2
                [bound_hi, bound_lo] = calculate_bounds(tmp);
            else
                [bound_hi, bound_lo] = calculate_bounds(tmp([2:3],:,:,:,:));
            end
            %create path and labels
            %path
            path = [out_folder filesep num2str(timeslots(t)) filesep vartypes{v}] ;

            if exist(path, 'dir')==0
                mkdir(path);
            end
            %path and ylabel = execution time / realative ... total cost /throughput/ latancy... 
            
            % + scheduler for path
            %xlabel = scheduler (?)

            data_sqeezed = squeeze(data(v, :, :,t,:,:));
            avail_squeezed = squeeze(avail(v, :, :,t,:,:));
            
            my_ylabel = [];
            if(v==1)
            %v==1 is total cost: show a plot which compares all schedulers
                my_xlabel = num2str(timeslots(t)); % show name of scheduler below graph
                if(t==1)
                    %show label only for first plot
                    my_ylabel = vartypes{v}; 
                end    
                %vary flows(1), time(2) and networks(3) in plots %(4)
                %schedulers
                tikz_out(path,data_sqeezed , avail_squeezed, 4, ...%vary schedulers
                 my_xlabel, my_ylabel, bound_hi, bound_lo, logscale);
            else if v==2
            %case of duration plot: compare all schedulers
                    my_xlabel = num2str(timeslots(t)); % show name of scheduler below graph
                    if(t==1)
                        %show label only for first plot
                        my_ylabel = [vartypes{v} '/ opt ' vartypes{v}] ; %attrib / opt attrib (relative)
                    end
                    %vary flows(1), time(2) and networks(3) in plots
                    % for vary=1:3
                    tikz_out(path,data_sqeezed , avail_squeezed, 4, ...%vary schedulers
                     my_xlabel, my_ylabel, bound_hi, bound_lo, logscale);
                else
                %case of other variables: use detailed plot
                    my_xlabel = vartypes{v};    %use vartype name as xlabel
                    if v==3 %v=3: startime+deadline is first plot; show label here only
                        my_ylabel='violation relative to optimum';
                    end
                    tikz_out_detail(path, data_sqeezed, avail_squeezed, 2, ...
                        my_xlabel, my_ylabel,  bound_hi, bound_lo, logscale);
                end
            end        
        end      
        %barplot: bar(data(scheduler, mean_data for variable)
        %plot_data = squeeze(1
        %state=['plotting done by ' num2str(100*(1+(t-1)+(v-1)*(nof_schedulers))/((nof_vartypes)*(nof_schedulers))) '%']

    end

end