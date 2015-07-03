%matlab eval for scheduling

read_log=1;
%read out logfiles
if read_log
    clearvars -except read_log
else
    clearvars -except time read_log
end
   
t_max=1;
n_max=3;
i_max=4;
rep_max=100;

in_folder = 'logs_save';% 'logs_time';
out_folder = 'graphs_save';

if read_log
    time = zeros(t_max, n_max, i_max, rep_max);
    for t = 0:t_max-1
        for n = 0:n_max-1
           for i=0:i_max-1
               for rep=0:rep_max-1
                   in_path = [in_folder '\' num2str(t) '_' num2str(n) '_' num2str(i) '\rep_' num2str(rep) '\']
                   addpath(in_path);
                   fname = [in_path 'log.m'];
                   if exist(fname, 'file') == 2
                       run(fname);
                       if exist('duration_to_solve_model_us', 'var')
                           duration = generate_model + duration_to_solve_model_us;
                           %collect time data of all log files
                           time(t+1,n+1,i+1,rep+1)=duration;
                       end
                   end 
                   rmpath(in_path);

                  % time(rep+1)=duration;
                    clearvars -except time t n i rep t_max n_max i_max rep_max in_folder out_folder
               end
           end 
        end
    end
end


%draw boxplots
mkdir(out_folder);
ymin = 0;
ymax = 11;


%vary networks
nets=ones(n_max,1);
for n=0:n_max-1
   nets(n+1)=2^n;
end


for t=1:t_max
    t1=25*2^(t-1);
    for i=1:i_max
        i1=2^(i-1);
        values=squeeze(time(t,:,i,:))'/1000;
        
        fig = figure('visible', 'off');
        hold on;
      %  axis([1 2^(n_max-1) 0 20000]);
        boxplot(values, nets);
        ylim([ymin,ymax])
        xlabel('number of networks');
        ylabel('solve duration in s');
        title(['Solve duration over number of networks, time slots = ' num2str(t1) ', applications = ' num2str(i1)]);
        hold off;
        filename = [out_folder '\nets__t_' num2str(t1) '__app_' num2str(i1) '.png'];
        saveas(fig,filename);
    end
end


%vary applications
apps=ones(i_max,1);
for i=0:i_max-1
   apps(i+1)=2^i;
end


for t=1:t_max
    t1=25*2^(t-1);
    for n=1:n_max
        n1=2^(n-1);
        values=squeeze(time(t,n,:,:))'/1000;
        
        fig = figure('visible', 'off');
        hold on;
        %axis([1 2^(i_max-1) 0 20000]);
        boxplot(values, apps);
        ylim([ymin,ymax])
        xlabel('number of applications');
        ylabel('solve duration in s');
        title(['Solve duration over number of applications, time slots = ' num2str(t1) ', networks = ' num2str(n1)]);
        hold off;
        filename = [out_folder '\apps__t_' num2str(t1) '__net_' num2str(n1) '.png'];
        saveas(fig,filename);
    end
end


%vary time slots
ts=ones(t_max,1);
for i=0:t_max-1
   ts(i+1)=25*2^i;
end


for i=1:i_max
    i1=2^(i-1);
    for n=1:n_max
        n1=2^(n-1);
        values=squeeze(time(:,n,i,:))'/1000;
        
        fig = figure('visible', 'off');
        hold on;
        %axis([1 2^(i_max-1) 0 20000]);
        boxplot(values, ts);
        ylim([ymin,ymax])
        xlabel('number of time slots');
        ylabel('solve duration in s');
        title(['Solve duration over number of time slots, applications = ' num2str(i1) ', networks = ' num2str(n1)]);
        hold off;
        filename = [out_folder '\tslots__apps_' num2str(i1) '__net_' num2str(n1) '.png'];
        saveas(fig,filename);
    end
end


clearvars -except time





