% t_n_i
% time:
generate_model = 7015;
duration_to_solve_model_us = 483;
create_model = 38;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 0, 0, 5, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 5, 0, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,2) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 35, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
% non_allocated
non_allocated = [70, 0];
% dl_vio
dl_vio = [0, 0];
% st_vio
st_vio = [0, 0];
% vioThroughput
vioThroughput = [0, 0];
% non_allo_vio
non_allo_vio = [3920, 0];
% nChunks
nChunks = [195, 35];
% prefStartTime
prefStartTime = [0, 12];
% deadline
deadline = [39, 13];
% availBW
availBW = [33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 60, 60, 60, 60, 60, 30, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 25, 38, 38, 38, 38, 38, 38, 25, 12, 0, 0, 0, 0; 0, 0, 14, 29, 44, 44, 44, 44, 44, 44, 44, 29, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 38, 76, 76, 76, 76, 76, 38, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 23, 35, 35, 35, 35, 35, 35, 23, 11, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 32, 49, 49, 49, 49, 49, 49, 32, 16, 0, 0; 0, 0, 0, 20, 40, 40, 40, 40, 40, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

