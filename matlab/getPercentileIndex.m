
function index = getPercentileIndex(X,p)
    mini=min(X);
    maxi=max(X);

    limit= mini + (maxi-mini)*p/100;
    [~,x_len]=size(X);
    
    index=1;
    while(X(index)<limit && index < x_len)
        index=index+1;
    end
end
