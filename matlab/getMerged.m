function [ y ] = getMerged( v )
%GETMERGED Summary of this function goes here
%   Detailed explanation goes here

[s_max, k_max, r_max]=size(v);

y=ones(s_max*k_max, r_max);

for s=0:s_max-1
    for k=0:k_max-1
        y(k_max*k+s+1,:)=v(s+1,k+1,:);
    end
end

