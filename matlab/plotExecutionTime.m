function [ ] = plotExecutionTime(out_folder, str_value, scale, sched, time)
%PLOTEXECUTIONTIME Summary of this function goes here
%   Detailed explanation goes here

%str_value is 'cost' or 'solve duration"
%scale = 'solve duration in s' or 'cost function value'
%time(f, n, t, rep)

[f_max,t_max,n_max, r_max]=size(time);

%vary networks
nets=ones(n_max,1);
for n=0:n_max-1
   nets(n+1)=2^n;
end

%solve duration against number of networks
for t=1:t_max
    t1=25*2^(t-1);
    for f=1:f_max
        f1=2^(f-1);
        values=squeeze(time(f,:,t,:))'/1000;    %f,t
        
        fig = figure('visible', 'off');
        hold on;
      %  axis([1 2^(n_max-1) 0 20000]);
        boxplot(values, nets);
  %      ylim([ymin,ymax])
        xlabel('number of networks');
        ylabel(scale);
        title([str_value ' over number of networks, time slots = ' num2str(t1) ', applications = ' num2str(f1)]);
        hold off;
        filename = [out_folder '\nets__t_' num2str(t1) '__app_' num2str(f1) '__' scale '__' sched '.png'];
        saveas(fig,filename);
    end
end


%vary applications
apps=ones(f_max,1);
for f=0:f_max-1
   apps(f+1)=2^f;
end

%solve duration against number of applications
% for t=1:t_max
%     t1=25*2^(t-1)
%     for n=1:n_max
%         n1=2^(n-1);
%         values=squeeze(time(:,n,t,:))'/1000;    %n,t
%         
%         fig = figure('visible', 'off');
%         hold on;
%         %axis([1 2^(f_max-1) 0 20000]);
%         size(values)
%         values
%         f_max
%         boxplot(values, apps);
%       %  ylim([ymin,ymax])
%         xlabel('number of applications');
%         ylabel(scale);
%         title([str_value ' over number of applications, time slots = ' num2str(t1) ', networks = ' num2str(n1)]);
%         hold off;
%         filename = [out_folder '\apps__t_' num2str(t1) '__net_' num2str(n1) '__' scale '__' sched '.png'];
%         saveas(fig,filename);
%     end
% end


%vary time slots
ts=ones(t_max,1);
for f=0:t_max-1
   ts(f+1)=25*2^f;
end

%solve duration against number of time slots
for f=1:f_max
    f1=2^(f-1);
    for n=1:n_max
        n1=2^(n-1);
        values=squeeze(time(f,n,:,:))'/1000;    %n,f
        
        fig = figure('visible', 'off');
        hold on;
        %axis([1 2^(f_max-1) 0 20000]);
        boxplot(values, ts);
     %   ylim([ymin,ymax])
        xlabel('number of time slots');
        ylabel(scale);
        title([str_value ' over number of time slots, applications = ' num2str(f1) ', networks = ' num2str(n1)]);
        hold off;
        filename = [out_folder '\tslots__apps_' num2str(f1) '__net_' num2str(n1) '__' scale '__' sched '.png'];
        saveas(fig,filename);
    end
end

end

