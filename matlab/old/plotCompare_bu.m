function [ ] = plotCompare_bu(out_folder, data, isCost, tikz)
%PLOTEXECUTIONTIME Summary of this function goes here
%   Detailed explanation goes here

%str_value is 'cost' or 'solve duration"
%scale = 'solve duration in s' or 'cost function value'
%time(f, n, t, rep)

% plotCompare(out_folder, duration_us, 'extime', 'execution time 2^x ms');
% plotCompare(out_folder, cost_rel, 'cost', 'relative cost');

hi=1.1;
low= 0.9;

    my_ylabel2= 'cost random / cost greedy';
if isCost==2
    my_ylabel='cost / optimal cost';
    name = 'cost2';
    skip_no=2;
    skip=[1 3];
 labels={'optimization','random/opt','priority/opt','greedy/opt','random/greedy'};
elseif isCost==1
    my_ylabel='cost / optimal cost';
    name = 'cost';
    skip_no=2;
    skip=[1 3];
 labels={'optimization','random','priority','greedy','random/greedy'};
elseif isCost ==0
    my_ylabel='execution time in s';
    name='extime';
    skip_no=1;
    skip=[3];
    data = (data/1000000);   %us to s
 labels={'optimization','random','priority','greedy','random/greedy'};
end
    
 [type_max, f_max , t_max, n_max,~]=size(data);
 
% [one skip_no] = size(skip)


x=[1 2 4 8 16 32 64];

%vary networks
for f=1:f_max
    for t=1:t_max
        fig = figure('visible', 'off');
        sched_m=squeeze(data(:,f,t,:,:));

         t1=25*2^(t-1);
         f1=2^(f-1);
        max_sched = hi*max(sched_m(:));
        min_sched = low*min(sched_m(:));
        ylim=[min_sched max_sched];
        plot_no=1;
         for type=1:type_max
            if(ismember(skip, type)<1)  % if type is not in skip list
               %printoutvar=['draw graph ' num2str(plot_no)]
              if ~tikz
               subplot(1,type_max-skip_no,plot_no);
              end
                fixed_f_t=squeeze(data(type,f,t,:,:))';
                boxplot(fixed_f_t, x(1:n_max));
                xlabel(labels{type});
                set(gca,'YLim',ylim); 
                if isCost<1
                    set(gca,'yscale','log');
                end
                grid on
                if plot_no==1
                    ylabel(my_ylabel);  %set ylabel only for first
                end
                if type==5
                    ylabel(my_ylabel2);
                end
                plot_no=plot_no+1;
                if tikz
                    filename = [out_folder '\' name '__nets_comp__t_' num2str(t1) '__app_' num2str(f1) '__' num2str(type) labels{type}(1:3) '.tikz'];
                    matlab2tikz(filename,'width','\figW','height','\figH','showInfo',false);
                end
            end
         end
         if ~tikz
            filename = [out_folder '\' name '__nets_comp__t_' num2str(t1) '__app_' num2str(f1) '.png'];
           % title(['Execution time: variation of number of networks, ' num2str(t1) ' time slots ', num2str(f1) ' flows']);
            saveas(fig,filename);
         end

    end
end

% vary applications
for n=1:n_max
    for t=1:t_max
        fig = figure('visible', 'off');     
        sched_m=squeeze(data(:,:,t,n,:));  %linear for cost
        max_sched = hi*max(sched_m(:));
        min_sched = low*min(sched_m(:));
        ylim=[min_sched max_sched];
        plot_no=1;
        
         t1=25*2^(t-1);
         n1=2^(n-1);
         for type=1:type_max
            if(ismember(skip, type)<1)  % if type is not in skip list
               if ~tikz
                subplot(1,type_max-skip_no,plot_no);
               end
                fixed_f_t=squeeze(data(type,:,t,n,:))';
                boxplot(fixed_f_t, x(1:f_max));
                xlabel(labels{type});
                set(gca,'YLim',ylim);
                if isCost<1
                    set(gca,'yscale','log');
                end
                grid on
                if plot_no==1
                    ylabel(my_ylabel);
                end
                if type==5
                    ylabel(my_ylabel2);
                end
                plot_no=plot_no+1;
                if tikz
                    filename = [out_folder '\' name '__apps__t_' num2str(t1) '__net_' num2str(n1)  '__' num2str(type) labels{type}(1:3) '.tikz'];
                    matlab2tikz(filename,'width','\figW','height','\figH','showInfo',false);
                end
            end
         end
         if ~tikz
             filename = [out_folder '\' name '__apps__t_' num2str(t1) '__net_' num2str(n1) '.png'];
             saveas(fig,filename);
         end
    end
end

 
% %vary time slots
ts=ones(t_max,1);
for f=0:t_max-1
   ts(f+1)=25*2^f;
end

for n=1:n_max
    for f=1:f_max
        fig = figure('visible', 'off');

        sched_m=squeeze(data(:,f,:,n,:));  %sched_m is only for scaling of non-opt plots

        max_sched = hi*max(sched_m(:));
        min_sched = low*min(sched_m(:));
        ylim=[min_sched max_sched];
        
         f1=2^(f-1);
         n1=2^(n-1);
        plot_no=1;
         for type=1:type_max
            if(ismember(skip, type)<1)  % if type is not in skip list
                if ~tikz
                    subplot(1,type_max-skip_no,plot_no);
                end
                fixed_f_t=squeeze(data(type,f,:,n,:))';
                boxplot(fixed_f_t, ts(1:t_max));
                xlabel(labels{type});
                set(gca,'YLim',ylim);
                if isCost<1
                    set(gca,'yscale','log');
                end
                grid on
                if plot_no==1
                    ylabel(my_ylabel);
                end
                if type==5
                    ylabel(my_ylabel2);
                end
                plot_no=plot_no+1;
                if tikz
                    filename = [out_folder '\' name '__time__flows_' num2str(f1) '__net_' num2str(n1)  '__' num2str(type) labels{type}(1:3) '.tikz'];
                    matlab2tikz(filename,'width','\figW','height','\figH','showInfo',false);
                end
            end
         end
         if ~tikz
             filename = [out_folder '\' name '__time__flows_' num2str(f1) '__net_' num2str(n1) '.png'];
             saveas(fig,filename);
         end
    end
end

end


