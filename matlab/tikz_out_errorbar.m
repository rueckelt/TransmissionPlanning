function tikz_out_errorbar(filename, data, my_ylabel, legendlabels, logscale, zeroline)
%filename for export
%data is matrix with dimensions (scheduler, time, repetitions)
%ylabel is string

%CREATEFIGURE(YMATRIX1, EMATRIX1)
%  YMATRIX1:  errorbar y matrix
%  EMATRIX1:  errorbar e matrix

YMatrix1=mean(data,3)';
EMatrix1=std(data,1,3)';

%neg=YMatrix1-EMatrix1
[dim1, dim2]=size(YMatrix1)
% Create figure
figure1 = figure('visible', 'on');
linestyles = {'-','--','-.',':'};
linecolors = {'black','blue','red','green','cyan','magenta','yellow'};
colors = [0,0,0; 0,0,1; 0,1,0; 1,0,0; 0,1,1; 1,0,1; 1,1,0]; %same colors in rgb
% Create axes
axes1 = axes('Parent',figure1,'YMinorTick','on',...%'YScale','log',...
    'XTickLabel',{'','25','','50','','100','','200','','400',''});
hold(axes1,'on');

% Create multiple error bars using matrix input to errorbar
errorbar1 = errorbar(YMatrix1,EMatrix1,'LineWidth',1.5);
for i=1:dim2
    set(errorbar1(i),'DisplayName',legendlabels{i},'LineStyle',linestyles{mod(i-1,4)+1},...)
        'Color',linecolors{mod(i-1,7)+1});
end

if logscale>0
    set(gca,'YScale','log');
end
if zeroline>0
   l=refline([0,1]);
   set(l,'color',[0.8,0.8,0.8]);
   set(l,'DisplayName','Ref Opt');
   %set(errorbar1(dim2+1),'DisplayName','Opt reference');%,'LineStyle','-',...
        %'Color',[0.8,0.8,0.8]);
end

% Create xlabel
xlabel('# time slots');

% Create ylabel
ylabel(my_ylabel);

% Create legend
legend1 = legend(axes1,'show', 'Location','northwest');

%set(legend1,  );

matlab2tikz(filename,'width','\figW','height','\figH','showInfo',false);


