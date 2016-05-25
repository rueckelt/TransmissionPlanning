function tikz_out_errorbar(filename, data, my_ylabel, legendlabels, style_skip, logscale, zeroline, time0_net1)
%filename for export
%data is matrix with dimensions (scheduler, time, repetitions)
%ylabel is string

%CREATEFIGURE(YMATRIX1, EMATRIX1)
%  YMATRIX1:  errorbar y matrix
%  EMATRIX1:  errorbar e matrix

YMatrix1=mean(data,3)';
EMatrix1=std(data,1,3)';

y_lim_margin=0.1;
%neg=YMatrix1-EMatrix1
[dim1, dim2]=size(YMatrix1);
% Create figure
figure1 = figure('visible', 'on');
linestyles = {'-','--','-.',':'};
linecolors = {'black','magenta','blue','red','green','cyan','yellow'};
%colors = [0,0,0; 0,0,1; 0,1,0; 1,0,0; 0,1,1; 1,0,1; 1,1,0]; %same colors in rgb
% Create axes
if time0_net1==0
    axes1 = axes('Parent',figure1,'YMinorTick','on',...%'YScale','log',...
        'XTickLabel',{'','25','','50','','100','','200','','400',''});
else
    axes1 = axes('Parent',figure1,'YMinorTick','on',...%'YScale','log',...
        'XTickLabel',{'','1','','2','','4','','8','','16',''});
    
end
hold(axes1,'on');

% Create multiple error bars using matrix input to errorbar
errorbar1 = errorbar(YMatrix1,EMatrix1,'LineWidth',1.5);
for i=1:dim2
    set(errorbar1(i),'DisplayName',legendlabels{i},'LineStyle',linestyles{mod(i-1+style_skip,4)+1},...)
        'Color',linecolors{mod(i-1+style_skip,7)+1});
end

if logscale>0
    set(gca,'YScale','log');
end
if zeroline>0
   l=refline([0,0]);
   set(l,'color',[0.9,0.7,0.1]);
   set(l,'DisplayName','Ref Opt');
   y_lim_margin=0.35;    
   %set(gca, 'YTickLabel',num2str(round(100.*get(gca,'YTick'))','%g\%%'));
end

% Create xlabel
if time0_net1==0
    xlabel('# time slots');
else
    xlabel('# networks');
end

% Create ylabel
ylabel(my_ylabel);

%set y-limits of plot to 10% margin
y_lim=[ min(min(YMatrix1-EMatrix1))-0.1*abs(min(min(YMatrix1-EMatrix1))),...
        max(max(YMatrix1+EMatrix1))+y_lim_margin*abs(max(max(YMatrix1+EMatrix1)))];

set(gca,'ylim',y_lim);

% Create legend
legend1 = legend(axes1,'show', 'Location','northwest');
set(gca,...
    'YMinorTick','off',...
    'YGrid','on',...
    'YMinorGrid','off',...
    'YColor',[0.85 0.85 0.85]);

box on;

Caxes = copyobj(gca,gcf);
set(Caxes, 'color', 'none', 'xcolor', 'k', 'xgrid', 'off', 'ycolor','k', 'ygrid','off');
%set(legend1,  );
filename = regexprep(filename, ' ', '_');
matlab2tikz(filename,'width','\figW','height','\figH','showInfo',false);


