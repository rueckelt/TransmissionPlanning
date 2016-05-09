%calculate plotting bounds

%data(schedulers, flows, time, networks, repetitions)
%bound_x({1..3}, d1, d2)      
function [bound_hi, bound_lo] = calculate_bounds(data)  

    [nof_schedulers, nof_flows, nof_time, nof_networks, nof_repetitions] = size(data);
    %calculate bounds
    hi=1.1;
    lo=0.9;
    dim_max = max([nof_flows, nof_time, nof_networks]);
    bound_hi = zeros(4, dim_max, dim_max);
    bound_lo = zeros(4, dim_max, dim_max);
    
    %flows: 1
    for t=1:nof_time
        for n=1:nof_networks
            tmp= squeeze(data(:,:,t,n,:));
            bound_hi(1, t,n)=hi*max(tmp(:));
            if bound_hi(1, t,n)>0 
                bound_lo(1, t,n)=lo*min(tmp(tmp(:)>0));
            end
        end
    end
    
        %time: 2
    for f=1:nof_flows
        for n=1:nof_networks
            tmp= squeeze(data(:,f,:,n,:));
            bound_hi(2, f,n)=hi*max(tmp(:));
            if bound_hi(2, f,n)>0 
                bound_lo(2, f,n)=lo*min(tmp(tmp(:)>0));
            end
        end
    end
    
        %net: 3
    for t=1:nof_time
        for f=1:nof_flows
            tmp= squeeze(data(:,f,t,:,:));
            bound_hi(3, f,t)=hi*max(tmp(:));
            if bound_hi(3, f,t)>0 
                bound_lo(3, f,t)=lo*min(tmp(tmp(:)>0));
            end
        end
    end
    
    %sched: 4
    bound_hi(4,:,:)=bound_hi(2,:,:);
    bound_lo(4,:,:)=bound_lo(2,:,:);
end