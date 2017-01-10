function [ output_args ] = cdfPrint( output_name, my_xlabel, data, prot_names)
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
    
    MAX_ELEMENTS=1000;
    [~,n_prot]=size(data);
    
    linestyles = {'-','--',':','-.'};
    linecolors = {'black','magenta','blue','red','green','cyan','yellow'};

    fig=figure;%('visible','off');
    hold on
    grid on
    
    %reduce the number of values if too large
    for prot=1:n_prot
%        if ~isempty(data{index1,prot})
            %reduce data: otherwise matlab runs into OUT OF MEMORY error
            %while plotting or creating tiks
%             data_sort=data{index1,prot};
%             data_sort=sort(data_sort);
%             [~,len]=size(data_sort);
%             k=ceil(len/MAX_ELEMENTS);
%  
%             data_reduced=zeros(1,MAX_ELEMENTS); %init with zeros
%             j=1;
%             for i=1:len
%                 if mod(i,k)==0
%                     data_reduced(j)=data_sort(i);
%                     j=j+1;
%                 end
%             end
            %data_reduced=data_reduced(data_reduced~=0); %delete zeros
                
%            ['reduced for ' output_name]
            h = cdfplot(data(:,prot)); 
            hold on
          %  meanvalue=mean(data{index1, prot});
%            line([0.5 0.5], meanvalue);
             set(h,'LineStyle',linestyles{mod(prot-1,4)+1},...
                 'Color',linecolors{mod(prot-1,7)+1},...
                 'LineWidth',1.25);
%        end
    end
    
   % l=refline(0,0.5);
%    set(l,'color',[0.9,0.7,0.1]);


    xlabel(my_xlabel);
    ylabel('Cumulative Distribution');
    legend(prot_names{1}, prot_names{2}, prot_names{3}, prot_names{4});
    xlim([0 50]);
   % axes1 = axes('Parent',fig,'XScale','log');
    set(gca,'XMinorTick', 'off');
   % set(gca,'xscale','log', 'XMinorTick', 'off');
    title([]);
    
    matlab2tikz(output_name,'width','\figW','height','\figH','showInfo',false);
end


