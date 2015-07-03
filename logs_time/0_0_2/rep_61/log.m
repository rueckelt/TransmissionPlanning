% t_n_i
% time:
generate_model = 900;
duration_to_solve_model_us = 57;
create_model = 12;
% allocatedChunks
allocatedChunks(:,:,1) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5];
allocatedChunks(:,:,2) = [0; 0; 0; 5; 30; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,3) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 12; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,4) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 30; 30; 30; 30; 30; 30; 30; 30; 30; 30; 30; 30];
% non_allocated
non_allocated = [30, 0, 0, 90];
% dl_vio
dl_vio = [0, 0, 0, 0];
% st_vio
st_vio = [0, 0, 0, 1800];
% vioThroughput
vioThroughput = [0, 0, 0, 0];
% non_allo_vio
non_allo_vio = [1680, 0, 0, 3780];
% nChunks
nChunks = [155, 35, 12, 450];
% prefStartTime
prefStartTime = [0, 6, 0, 18];
% deadline
deadline = [31, 7, 100000, 48];
% availBW
availBW = [35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35];

