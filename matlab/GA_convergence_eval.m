%MoVeNet Eval, Tobias Rueckelt, 10.6.2016

state='initialize parameters'
addpath('matlab2tikz');

%Parameter
force_read_data=1;
input_folder = '../converge0.0/'
output_folder='tikz0.0';

percentiles = [68.3, 95.4, 99.7, 100];    %which percentiles should be ploted?

mkdir(output_folder);
data_file = 'GA_convergence.mat';

%##################################################
state='gather data'

if force_read_data <1 && exist(data_file, 'file')==2 %read values from file if no force to reread and available
    load(data_file);
else
    %read data from csv files
    GA_conv = read_logfiles(input_folder);
    save(data_file, 'GA_conv');
end
GA_conv=-GA_conv;


%##################################################
state='calculate values'

[iterations, rounds]=size(GA_conv);
[~,p_size]=size(percentiles);

%create names for lines in plot
plot_names=cell(1,p_size);
for i=1:p_size
    plot_names{i}=[num2str(percentiles(i)) '%'];
end


%determine values for each iteration (=each time slot in each repetition)
round_of_percentiles=zeros(iterations, p_size);
for i=1:iterations
    col=1;
    %look up all percentile indexes (=rounds)
    for p=percentiles
        round_of_percentiles(i,col)=getPercentileIndex(GA_conv(i,:), p);
        col=col+1;
    end
end


%##################################################
state='plot'
size(round_of_percentiles)
o_filename=[output_folder filesep 'GA_convergence.tikz'];

cdfPrint(o_filename, 'GA Rounds', round_of_percentiles, plot_names);
%cdfPrint( output_name, my_xlabel, data, index1, prot_names)

state='done'


