% t_n_i
% time:
generate_model = 28325;
duration_to_solve_model_us = 10045;
create_model = 97;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,2) = [0, 0, 0, 0, 0, 0, 0, 0; 4, 0, 0, 0, 0, 0, 0, 0; 0, 0, 7, 0, 0, 0, 0, 0; 0, 0, 24, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,3) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 12, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,4) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 63, 0; 0, 0, 0, 0, 0, 0, 36, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 63, 0; 0, 0, 0, 0, 0, 0, 63, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,5) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 4, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 24, 0, 0, 0, 0, 0; 0, 0, 7, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,6) = [5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 0, 0, 0, 0, 0, 0, 5, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,7) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 35, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,8) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 12, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
% non_allocated
non_allocated = [35, 0, 0, 0, 0, 35, 0, 0];
% dl_vio
dl_vio = [0, 0, 0, 0, 0, 0, 0, 0];
% st_vio
st_vio = [0, 0, 0, 0, 0, 50750, 0, 0];
% vioThroughput
vioThroughput = [0, 0, 0, 0, 0, 0, 0, 0];
% non_allo_vio
non_allo_vio = [1960, 0, 0, 0, 0, 1960, 0, 0];
% nChunks
nChunks = [160, 35, 12, 225, 35, 160, 35, 12];
% prefStartTime
prefStartTime = [0, 3, 0, 9, 12, 15, 18, 0];
% deadline
deadline = [32, 4, 100000, 24, 13, 47, 19, 100000];
% availBW
availBW = [25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25; 0, 0, 0, 0, 0, 26, 52, 52, 52, 52, 52, 26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 17, 34, 52, 52, 52, 52, 52, 52, 34, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 17, 35, 35, 35, 35, 35, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 12, 24, 36, 36, 36, 36, 36, 36, 24, 12, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 18, 37, 56, 56, 56, 56, 56, 56, 37, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 48, 73, 73, 73, 73, 73, 73, 48, 24, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 22, 44, 66, 66, 66, 66, 66, 66, 66, 44, 22, 0, 0, 0, 0, 0, 0];

