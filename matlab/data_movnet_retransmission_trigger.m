i=0.067*13

x_w= [28:i:38, 69.3:i:84.7, 101:i:140.1, ...
    155.8:i:191, 226.6:i:289, 319.1:i:403];
x_wo=[28:i:38, 70.2:i:84.7, 114.4:i:140.1, ...
    173.3:i:191, 257.9:i:289, 353.5:i:403];

[~,l_w] = size(x_w)
y_w = (1:l_w)*13;
[~,l_wo] = size(x_wo)
y_wo= (1:l_wo)*13;

x_w = x_w+rand(1,l_w)*(i/2);
x_wo = x_wo+rand(1,l_wo)*(i/2);

figure1 = figure

hold on
fig_w=plot(x_w, y_w);
set(fig_w,'DisplayName','MoVeNet retransmission trigger','LineStyle','-',...)
        'Color','green','LineWidth',1.5);
fig_wo=plot(x_wo, y_wo);
set(fig_wo,'DisplayName','TCP retransmission trigger only','LineStyle',':',...)
        'Color','black','LineWidth',1.5);

    % Create xlabel
xlabel('Simulation time in s'); %'Time slots');

% Create ylabel
ylabel('# transmitted packets');
    
    
% Create legend
%  axes1 = axes('YMinorTick','on');

% hold(axes1,'on');
% 
 legend1 = legend('Location','northwest');
% set(gca,...
%     'YMinorTick','off',...
%     'YGrid','on',...
%     'YMinorGrid','off',...
%     'YColor',[0.85 0.85 0.85]);
% 
% box on;
% 
% Caxes = copyobj(gca,gcf);
% set(Caxes, 'color', 'none', 'xcolor', 'k', 'xgrid', 'off', 'ycolor','k', 'ygrid','off');
%set(legend1,  );

matlab2tikz('movenet_retransmission.tikz','width','\figW','height','\figH','showInfo',false);
    
