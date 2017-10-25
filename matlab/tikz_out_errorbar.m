function tikz_out_errorbar(filename, data, my_ylabel, my_xlabel, my_xTickLabels, legendlabels, style_skip, logscale, zeroline)
%data(schedulers, x-axis var, repetitions)

%filename for export
%data is matrix with dimensions (scheduler, time, repetitions)
%ylabel is string

%CREATEFIGURE(YMATRIX1, EMATRIX1)
%  YMATRIX1:  errorbar y matrix
%  EMATRIX1:  errorbar e matrix

y_lim_margin=0.15;

p = 25;
%neg=YMatrix1-EMatrix1
% Create figure
figure1 = figure('visible', 'on');
linestyles = {'-','--',':','-','--',':','-','-.'};
linecolors = {'black','cyan','magenta','green','red','blue','black','yellow', 'cyan','magenta','green','red','blue'};
%linecolors = {'black','cyan','magenta','green','red','yellow'};
%colors = [0,0,0; 0,0,1; 0,1,0; 0,1,1; 1,0,0; 1,0,1; 1,1,0]; %same colors in rgb
% Create axes

    %A=size(data);

    YMatrix1=median(data, 3)';%mean(data,3)';
    EMatrix1bot=-(prctile(data, p,3)' -YMatrix1 );%std(data,1,3)';
    EMatrix1top=-(YMatrix1-prctile(data, 100-p,3)' );%std(data,1,3)'
  
    
    [dim1, dim2]=size(YMatrix1);
    x=repmat(1:dim1, dim2, 1)'  ;
%        
% %     %sort out non-available
%     if(~isnan(avail))
%         for d1 = 1:dim1
%            for d2 = 1:dim2
%                'next'
%               tmp=data(d1,d2)
%               tmp=tmp(avail(d2,d1)>0)
%               
%               YMatrix1(d1,d2)=median(tmp);%mean(data,3)';
%               EMatrix1bot(d1,d2)=-(prctile(tmp, p) -YMatrix1(d1,d2) );%std(data,1,3)';
%               EMatrix1top=-(YMatrix1(d1,d2)-prctile(tmp, 100-p) );%std(data,1,3)'
%            end
%         end
%     end
    
    
    
    axes1 = axes('Parent',figure1,'YMinorTick','on',...%'YScale','log',...
        'XTickLabel',my_xTickLabels);%{'','25','','50','','100','','200','','400',''});

hold(axes1,'on');

% Create multiple error bars using matrix input to errorbar

%size_y=size(YMatrix1)
%sizeEb=size(EMatrix1bot)
%sizeEt= size(EMatrix1top)
%size_x = size(x)

errorbar1 = errorbar(x,YMatrix1,EMatrix1bot, EMatrix1top,'LineWidth',1.5);
%errorbar1(1)
for i=1:dim2
    set(errorbar1(i),'DisplayName',legendlabels{i},'LineStyle',linestyles{mod(i-1+style_skip,8)+1},...)
        'Color',linecolors{mod(i-1+style_skip,13)+1});
end

if logscale>0
    set(gca,'YScale','log');
end
if zeroline>0
   l=refline([0,0]);
   set(l,'color',[0.9,0.7,0.1]);
   set(l,'DisplayName','Ref Opt');
   %y_lim_margin=0.35;    
   %set(gca, 'YTickLabel',num2str(round(100.*get(gca,'YTick'))','%g\%%'));
end

% Create xlabel
xlabel(my_xlabel); %'Time slots');

% Create ylabel
ylabel(my_ylabel);

%set y-limits of plot to 10% margin
y_lim=[ min(min(YMatrix1-EMatrix1bot))-y_lim_margin*abs(min(min(YMatrix1-EMatrix1bot))),...
        max(max(YMatrix1+EMatrix1top))+y_lim_margin*abs(max(max(YMatrix1+EMatrix1top)))];
if ~isnan(y_lim)
    %set(gca,'ylim',y_lim);
end

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


