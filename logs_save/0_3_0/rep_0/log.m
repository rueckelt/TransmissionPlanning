% t_n_i
% time:
generate_model = 42027;
duration_to_solve_model_us = 230;
create_model = 100;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0, 0, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0];
% non_allocated
non_allocated = [75];
% dl_vio
dl_vio = [0];
% st_vio
st_vio = [0];
% vioThroughput
vioThroughput = [0];
% non_allo_vio
non_allo_vio = [4200];
% nChunks
nChunks = [200];
% prefStartTime
prefStartTime = [0];
% deadline
deadline = [40];
% availBW
availBW = [24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 48, 48, 48, 48, 48, 24, 0, 0, 0, 0, 0, 0; 0, 26, 52, 78, 78, 78, 78, 78, 78, 52, 26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 16, 32, 48, 48, 48, 48, 48, 48, 32, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 74, 74, 74, 74, 74, 37, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 48, 48, 48, 48, 48, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 20, 40, 61, 61, 61, 61, 61, 61, 40, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 39, 79, 79, 79, 79, 79, 39, 0, 0, 0, 0, 0, 0, 0, 0];

