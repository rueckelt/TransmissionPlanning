s_max=3;
SHARE=0.5;

n_max=2;
nets=ones(n_max,1);

for n=0:n_max-1
    if s_max>1
        for i=0:s_max-1
            nets(s_max*n+i+1)=(2^n)*(1-SHARE/2 +i*SHARE/(s_max-1));
        end
    else
        nets(n+1)=2^n;
    end 
end
nets