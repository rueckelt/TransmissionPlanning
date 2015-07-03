% t_n_i
% time:
generate_model = 846;
duration_to_solve_model_us = 55;
create_model = 9;
% allocatedChunks
allocatedChunks(:,:,1) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5];
allocatedChunks(:,:,2) = [0; 0; 0; 10; 25; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,3) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 12; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,4) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 25; 25; 25; 25; 25; 25; 25; 25; 25; 25; 25; 25];
% non_allocated
non_allocated = [35, 0, 0, 150];
% dl_vio
dl_vio = [0, 0, 0, 0];
% st_vio
st_vio = [0, 0, 0, 1500];
% vioThroughput
vioThroughput = [0, 0, 0, 0];
% non_allo_vio
non_allo_vio = [1960, 0, 0, 6300];
% nChunks
nChunks = [160, 35, 12, 450];
% prefStartTime
prefStartTime = [0, 6, 0, 18];
% deadline
deadline = [32, 7, 100000, 48];
% availBW
availBW = [30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30];

