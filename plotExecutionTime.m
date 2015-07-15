function [ ] = plotExecutionTime(out_folder, time, ymin,ymax, t_max, n_max, i_max )
%PLOTEXECUTIONTIME Summary of this function goes here
%   Detailed explanation goes here

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
  %      ylim([ymin,ymax])
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
      %  ylim([ymin,ymax])
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
     %   ylim([ymin,ymax])
        xlabel('number of time slots');
        ylabel('solve duration in s');
        title(['Solve duration over number of time slots, applications = ' num2str(i1) ', networks = ' num2str(n1)]);
        hold off;
        filename = [out_folder '\tslots__apps_' num2str(i1) '__net_' num2str(n1) '.png'];
        saveas(fig,filename);
    end
end

end

