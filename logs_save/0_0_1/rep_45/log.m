% t_n_i
% time:
generate_model = 286;
duration_to_solve_model_us = 30;
create_model = 4;
% allocatedChunks
allocatedChunks(:,:,1) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5];
allocatedChunks(:,:,2) = [0; 0; 0; 0; 5; 0; 0; 0; 15; 15; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
% non_allocated
non_allocated = [25, 0];
% dl_vio
dl_vio = [0, 0];
% st_vio
st_vio = [0, 0];
% vioThroughput
vioThroughput = [0, 0];
% non_allo_vio
non_allo_vio = [1400, 0];
% nChunks
nChunks = [150, 35];
% prefStartTime
prefStartTime = [0, 12];
% deadline
deadline = [30, 13];
% availBW
availBW = [20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20];

