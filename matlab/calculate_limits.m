%data(schedulers, flows, time, networks, repetitions)
%lim_x({1..3}, d1, d2)      
function [lim_hi lim_lo] = calculate_limits(data)     
    %calculate limits
    hi=1.1;
    lo=0.9;
    dim_max = max(nof_flows, nof_time, nof_networks);
    lim_hi = zeros(3, dim_max, dim_max);
    lim_lo = zeros(3, dim_max, dim_max);
    
    %flows: 1
    for t=1:nof_time
        for n=1:nof_networks
            tmp= squeeze(data(:,:,t,n));
            lim_hi(1, t,n)=hi*max(tmp(:));
        end
    end
end