function [ x ] = getXVector( s_max, SHARE, i_max )
%GETXVECTOR Summary of this function goes here
%   Detailed explanation goes here
% 
% s_max=3;
% SHARE=0.5;
% 
% i_max=2;
x=ones(i_max*s_max,1);

for s=0:i_max-1
    if s_max>1
        for i=0:s_max-1
            x(s_max*s+i+1)=(2^s)*(1-SHARE/2 +i*SHARE/(s_max-1));
        end
    else
        x(s+1)=2^s;
    end 
end

end

