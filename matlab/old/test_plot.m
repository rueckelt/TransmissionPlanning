%example code: YTick changes on removing YTickLabel
    
    %create input data
    dim1 =20;
    dim2 =3;
    dim3 = 3;
    data = rand(dim1, dim2, dim3)*100+1000;
    ylim=[0.9*min(data(:)) 1.1*max(data(:))];

    my_ylabel = 'y_label';

    %create [dim3] plots
    for d3=1:dim3
       % subplot(1,3,d3);
        f=figure;
        plot_data=squeeze(data(:,:,d3));
        boxplot(plot_data, 1:dim2);     %plot data and add x-axis dim
       % xlabel(my_xlabel);
        set(f,'YLim',ylim); 

        grid on
        set(f,'yscale','log');
        if d3==1 %~isempty(my_ylabel)
            ylabel(my_ylabel);  %set ylabel and numbers only for first
           % y_tick = get(f, 'YTick'); %get YTick from labeled graph
        else
%           y_tick = get(gca, 'YTick') %get YTick from labeled graph
          %  set(gca,'YTickLabel',[]); %remove y-axis numbers for others
%           set(gca, 'YTick', y_tick); %apply old YTick
        end
%    filename = [out_folder filesep f_parts{1} num2str(d1_scale(d1))...
%                 f_parts{2}  num2str(d2_scale(d2)) f_parts{3}];
%    matlab2tikz(filename,'width','\figW','height','\figH','showInfo',false);
   end