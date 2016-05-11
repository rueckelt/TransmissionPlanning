
%data(vartypes, schedulers, flow, time, networks, repetitions) 
function [data_rel] = relative_to_opt(data)
    
    [nof_vartypes, nof_schedulers, nof_flows, nof_time, nof_networks, nof_repetitions] = size(data);
    data_rel = data;
   % data=data+1;
    %sum_opt=sum(data(:,1,:,:,:,:),1)
    for v=2:nof_vartypes %skip first: execution time
        for s=1:nof_schedulers %skip first: do not norm opt to itself (would be 1 for every value)
            data_rel(v,s,:,:,:,:)=data(v,s,:,:,:,:)./data(v,1,:,:,:,:);  %elementweise division: Normierung auf opt
           % data_rel(v,s,:,:,:,:)=data(v,s,:,:,:,:)./sum(data(:,1,:,:,:,:),1);  %elementweise division: Normierung auf opt
        end
    end
end