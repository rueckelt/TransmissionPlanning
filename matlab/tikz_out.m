%out folder should contain subfolder for scheduler and subfolder for
%extime/cost (type)

%data is in dimensions [flows, time, networks, repetitions]

%plot var ftn decides which variable (ftn?) is varied for plotting

%logscale is applied if >0

function [] = tikz_out(out_folder, data, plot_var_ftn, my_xlabel, my_ylabel, bound_hi, bound_lo, logscale)

    scale_f = [1 2 4 8 16 32 64];
    scale_n = [1 2 4 8 16 32 64];
    scale_t = [25 50 100 200 400 800 1600];
    
    %arrange data
    if(plot_var_ftn==1) %vary flows for plot
        data = permute(data, [2 3 1 4]);
        x_axis = scale_f;
        d1_scale = scale_t; 
        d2_scale = scale_n;  
        f_parts = {'vary_f__t_', '__n_', '.tikz'}; 
    end
    if(plot_var_ftn==2) %vary time for plot
        data = permute(data, [1 3 2 4]);
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
    
    %plot
    fig = figure('visible', 'off'); 
    [dim1 dim2, dim3, rep] = size(data);   
    
    for d1=1:dim1
        for d2=1:dim2
            fixed_f_t=squeeze(data(d1,d2,:,:))';
            boxplot(fixed_f_t, x_axis(1:dim3));
            xlabel(my_xlabel);
            ylim=[bound_lo(plot_var_ftn,d1,d2) bound_hi(plot_var_ftn,d1,d2)+0.00001];
            set(gca,'YLim',ylim); 
            if logscale>0
                set(gca,'yscale','log');
            end
            grid on
            if ~isempty(my_ylabel)
                ylabel(my_ylabel);  %set ylabel only for first
            end

           % filename = [out_folder '\' name '__nets_comp__t_' num2str(d1) '__app_' num2str(d1) '__' num2str(type) labels{type}(1:3) '.tikz'];
           filename = [out_folder filesep f_parts{1} num2str(d1_scale(d1))...
                        f_parts{2}  num2str(d2_scale(d2)) f_parts{3}]
           matlab2tikz(filename,'width','\figW','height','\figH','showInfo',false);
        end
     end
end