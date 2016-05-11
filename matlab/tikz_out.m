%out folder should contain subfolder for scheduler and subfolder for
%extime/cost (type)

%data is in dimensions [flows, time, networks, repetitions]

%plot var ftn decides which variable (ftn?) is varied for plotting

%logscale is applied if >0

function [] = tikz_out(out_folder, data, avail, plot_var_ftn, my_xlabel, my_ylabel, bound_hi, bound_lo, logscale)
 
    [f_max, t_max, n_max, r_max] = size(data);
    scale_f = [1 2 4 8 16 32 64];
    scale_n = [1 2 4 8 16 32 64];
    scale_t = [25 50 100 200 400 800 1600];
    scale_s = {'Opt' 'Gre' 'GreOl' 'Rnd'};
    %arrange data
    if(plot_var_ftn==1) %vary flows for plot
        data = permute(data, [2 3 1 4]);
        avail = permute(avail, [2 3 1 4]);
        x_axis = scale_f;
        d1_scale = scale_t; 
        d2_scale = scale_n;  
        f_parts = {'vary_f__t_', '__n_', '.tikz'}; 
    end
    if(plot_var_ftn==2) %vary time for plot
        data = permute(data, [1 3 2 4]);
        avail = permute(avail, [1 3 2 4]);
        x_axis = scale_t;
        d1_scale = scale_f; 
        d2_scale = scale_n; 
        f_parts = {'vary_t__f_', '__n_', '.tikz'}; 
    end
    if(plot_var_ftn==3) %vary networks for plot
        x_axis = scale_n;
        d1_scale = scale_f; 
        d2_scale = scale_t; 
        f_parts = {'vary_n__f_', '__t_', '.tikz'}; 
    end
    if(plot_var_ftn==4) %t is fixed, vary schedulers
        'plot4_sched'
        data = permute(data, [2 3 1 4]);    %former second dimension now on first
        avail = permute(avail, [2 3 1 4]);
       x_axis = scale_s;
       d1_scale = scale_f;
       d2_scale = scale_n;
       f_parts = {'vary_s__f_', '__n_', '.tikz'}; 
    end
    
    [dim1, dim2, dim3, ~] = size(data); 
    %plot
   % fig = figure('visible', 'off');  
    
    for d1=1:dim1
        for d2=1:dim2
            filename = [out_folder filesep f_parts{1} num2str(d1_scale(d1))...
                        f_parts{2}  num2str(d2_scale(d2)) f_parts{3}];
            figure('Name',filename, 'visible', 'off');
           % f=figure('Name',filename);
            data_squeezed=squeeze(data(d1,d2,:,:))';    %fix two dimensions
            avail_squeezed = squeeze(avail(d1,d2,:,:))';

            if sum(avail_squeezed(:))>0
                if plot_var_ftn<4
                    boxplot(data_squeezed, x_axis(1:dim3));     %plot data and add x-axis dim
                else
                    ['plot ' filename]
                    boxplot(data_squeezed,  x_axis);
                  %  set(gca,'XTickLabel', scale_s);
                end;
                
                xlabel(my_xlabel);
                ylim=[bound_lo(plot_var_ftn,d1,d2) bound_hi(plot_var_ftn,d1,d2)+0.00001];
                set(gca,'YLim',ylim); 

                grid on
                if logscale>0
                    set(gca,'yscale','log');
                end
                if ~isempty(my_ylabel)
                    ylabel(my_ylabel);  %set ylabel and numbers only for first
                    %y_tick = get(gca, 'YTick') %get YTick from labeled graph
                else
    %                 y_tick = get(gca, 'YTick') %get YTick from labeled graph
                     set(gca,'YTickLabel',[]); %remove y-axis numbers for others
    %                 set(gca, 'YTick', y_tick); %apply old YTick
                end

               matlab2tikz(filename,'width','\figW','height','\figH','showInfo',false);
               %the library adds a line that destroys YTick representation: YTick=
               %this line must be deleted / commented out

                fid  = fopen(filename,'r');
                f=fread(fid,'*char')';
                fclose(fid);
                f = regexprep(f,'ytick={','%ytick={');
                %rotate xtick labels and adjust xlabel spacing
                f = regexprep(f, 'xmajorgrids','x label style={at={(axis description cs:0.5,-0.09)},anchor=north},\nxticklabel style={rotate=90},\nxmajorgrids,');
                 fid  = fopen(filename,'w');
                fprintf(fid,'%s',f);
                fclose(fid);
            end

        end           
     end
end