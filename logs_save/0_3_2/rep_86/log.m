% t_n_i
% time:
generate_model = 13862;
duration_to_solve_model_us = 1179;
create_model = 59;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0, 0, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 5, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 5, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 0, 0, 0, 5, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,2) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 35, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,3) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 4, 0, 0, 0, 0, 0, 0; 0, 8, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,4) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 72, 0, 0, 0; 0, 35, 0, 0, 0, 0, 0, 0; 0, 35, 0, 0, 0, 0, 0, 0; 0, 35, 0, 0, 0, 0, 0, 0; 26, 0, 0, 0, 0, 0, 0, 0; 26, 0, 0, 0, 0, 0, 0, 0; 21, 0, 0, 0, 0, 0, 0, 0; 21, 0, 0, 0, 0, 0, 0, 0; 21, 0, 0, 0, 0, 0, 0, 0; 21, 0, 0, 0, 0, 0, 0, 0; 21, 0, 0, 0, 0, 0, 0, 0; 21, 0, 0, 0, 0, 0, 0, 0];
% non_allocated
non_allocated = [45, 0, 0, 95];
% dl_vio
dl_vio = [0, 0, 0, 0];
% st_vio
st_vio = [0, 0, 0, 3284];
% vioThroughput
vioThroughput = [0, 0, 0, 0];
% non_allo_vio
non_allo_vio = [2520, 0, 0, 3990];
% nChunks
nChunks = [170, 35, 12, 450];
% prefStartTime
prefStartTime = [0, 6, 0, 18];
% deadline
deadline = [34, 7, 100000, 48];
% availBW
availBW = [26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26; 0, 0, 0, 0, 0, 0, 0, 0, 13, 26, 40, 40, 40, 40, 40, 40, 40, 26, 13, 0, 0, 0, 0, 0, 0; 0, 21, 43, 65, 65, 65, 65, 65, 65, 43, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 31, 63, 63, 63, 63, 63, 31, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 38, 77, 77, 77, 77, 77, 38, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 12, 24, 37, 37, 37, 37, 37, 37, 37, 24, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 13, 26, 40, 40, 40, 40, 40, 40, 26, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 12, 24, 36, 36, 36, 36, 36, 36, 24, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

