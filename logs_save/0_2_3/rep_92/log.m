% t_n_i
% time:
generate_model = 18220;
duration_to_solve_model_us = 1762;
create_model = 55;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0];
allocatedChunks(:,:,2) = [0, 0, 0, 0; 0, 0, 0, 0; 27, 0, 0, 0; 8, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];
allocatedChunks(:,:,3) = [0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 12; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];
allocatedChunks(:,:,4) = [0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 68, 0; 0, 0, 0, 0; 0, 0, 68, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 31, 0; 0, 0, 42, 0; 0, 0, 16, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];
allocatedChunks(:,:,5) = [0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 35; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];
allocatedChunks(:,:,6) = [5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0];
allocatedChunks(:,:,7) = [0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 35; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];
allocatedChunks(:,:,8) = [0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 12, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0; 0, 0, 0, 0];
% non_allocated
non_allocated = [25, 0, 0, 0, 0, 30, 0, 0];
% dl_vio
dl_vio = [0, 0, 0, 0, 0, 0, 0, 0];
% st_vio
st_vio = [0, 0, 0, 0, 0, 50750, 0, 0];
% vioThroughput
vioThroughput = [0, 0, 0, 0, 0, 0, 0, 0];
% non_allo_vio
non_allo_vio = [1400, 0, 0, 0, 0, 1680, 0, 0];
% nChunks
nChunks = [150, 35, 12, 225, 35, 155, 35, 12];
% prefStartTime
prefStartTime = [0, 3, 0, 9, 12, 15, 18, 0];
% deadline
deadline = [30, 4, 100000, 24, 13, 46, 19, 100000];
% availBW
availBW = [37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37; 0, 0, 0, 0, 0, 0, 0, 0, 15, 31, 31, 31, 31, 31, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 26, 52, 78, 78, 78, 78, 78, 78, 52, 26, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 26, 52, 78, 78, 78, 78, 78, 78, 78, 52, 26, 0, 0, 0, 0, 0, 0, 0, 0, 0];

