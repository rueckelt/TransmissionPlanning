function relative = calculateRelative(values_absolutes, values_ref)

[type_max, f_max , t_max, n_max,rep_max]=size(values_absolute);
%calculate how good heuristics have been in comparison to optimization
relative=ones(type_max+1,f_max,t_max,n_max, rep_max);    %relative to optimization + 1 for relative to random

%relative to random
 relative_rnd = values_absolute(2,:,:,:,:)./values_absolute(4,:,:,:,:);
 
 
 
 %relative to optimum
for f=1:f_max 
   for n = 1:n_max 
    for t = 1:t_max
       for rep=1:rep_max
           for type = 1:type_max
%               relative to optimal
                relative(type,f,t,n, rep) = values_absolute(type,f,t,n, rep)/values_absolute(1,f,t,n, rep);
           end
           relative(type_max+1,f,t,n,rep) = relative_rnd(1,f,t,n,rep);  %add relative to random
       end
    end
   end
end