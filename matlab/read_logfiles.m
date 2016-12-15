%% Import data from text file.
% Author: Halis Altug, Version: 1.0, Date: 18.10.2015
% CHANGED TO READ_FILES FUNCTION: Tobias Rueckelt, 10.6.2016

%returns a matrix: (time_slots*repetitions) x rounds
function [output] = read_logfiles(input_folder)
%% Scenario configuration

output=[];
file = 'GA_conv.txt';

rep=0;
filename = [input_folder filesep num2str(rep) filesep file];

%% Measurement data read-in for all repetitions, as long as they exist
while exist(filename, 'file') == 2
    read_matrix=csvread(filename)
    output=vertcat(output, read_matrix);
    
    rep=rep+1;
    filename = [input_folder filesep num2str(rep) filesep file]
end
 
