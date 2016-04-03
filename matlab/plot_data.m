%out folder is basic output folder

%data is (varnames (=extime, cost,  throughput, ..), schedulers, flows, time, networks, %repetitions)

function [] = plot_data(out_folder, data, vartypes, schedulers)

[nof_vartypes, nof_schedulers, nof_flows, nof_time, nof_networks, nof_repetitions] = size(data);
    %what to plot?
    % use tikz_out(out_folder, data, plot_var_ftn, my_xlabel, my_ylabel, bound_hi, bound_lo, logscale)
        %out folder should contain subfolder for scheduler and subfolder for
        %extime/cost (type)
        %data is in dimensions [flows, time, networks, repetitions]
        %plot var ftn decides which variable (ftn?) is varied for plotting
        %logscale is applied if >0
       

    logscale=1;    
    %select data
    for v=1:nof_vartypes
        tmp = squeeze(data(v,:,:,:,:,:));
        [bound_hi, bound_lo] = calculate_bounds(tmp);
        nof_schedulers
        for s=1:nof_schedulers
            %create path and labels
            %path
            path = [out_folder filesep schedulers{s} filesep vartypes{v}] 
            mkdir(path);
            %path and ylabel = execution time / realative ... total cost /throughput/ latancy... 
            
            my_ylabel = [];
            if(v==1)
                if(s==1)
                    my_ylabel = vartypes{v};    %only execution time
                end
            else
                if(s==2)
                    my_ylabel = [vartypes{v} '/ opt ' vartypes{v}] ;
                end
            end
            % + scheduler for path
            %xlabel = scheduler (?)
            my_xlabel = schedulers{s};
            
            data_sqeezed = squeeze(data(v, s, :,:,:,:));

            %vary flows in plots
            tikz_out(path,data_sqeezed , 1, my_xlabel, my_ylabel, bound_hi, bound_lo, logscale);
        end
    end

end