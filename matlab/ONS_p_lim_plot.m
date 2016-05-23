%script for ONS parameter analysis

m=squeeze(data(1,2:end-1,1,1,1,:))';
x=-65:5;
p=plot(x,m)
set(p(1), 'Color', 'black', 'LineWidth', 1.5);
xlabel('p_{lim}')
ylabel('ONS cost value function value')
ylabel('ONS cost function value')
