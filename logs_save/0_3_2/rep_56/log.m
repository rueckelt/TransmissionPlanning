% t_n_i
% time:
generate_model = 13574;
duration_to_solve_model_us = 2292;
create_model = 56;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 0, 0, 5; 0, 0, 0, 0, 0, 5, 0, 0; 0, 0, 0, 0, 0, 5, 0, 0; 0, 0, 0, 0, 0, 5, 0, 0; 0, 0, 0, 0, 0, 5, 0, 0; 0, 0, 0, 0, 0, 5, 0, 0; 0, 0, 0, 0, 0, 5, 0, 0; 0, 0, 0, 0, 0, 5, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 0, 5, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0; 5, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,2) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 31; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 4; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,3) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 12, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0];
allocatedChunks(:,:,4) = [0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 47, 0, 0; 0, 0, 0, 0, 0, 62, 0, 0; 0, 0, 0, 0, 0, 39, 0, 0; 0, 36, 0, 0, 0, 0, 0, 0; 37, 0, 0, 0, 0, 0, 0, 0; 37, 0, 0, 0, 0, 0, 0, 0; 32, 0, 0, 0, 0, 0, 0, 0; 32, 0, 0, 0, 0, 0, 0, 0; 32, 0, 0, 0, 0, 0, 0, 0; 32, 0, 0, 0, 0, 0, 0, 0; 32, 0, 0, 0, 0, 0, 0, 0; 32, 0, 0, 0, 0, 0, 0, 0];
% non_allocated
non_allocated = [25, 0, 0, 0];
% dl_vio
dl_vio = [0, 0, 0, 0];
% st_vio
st_vio = [0, 0, 0, 3004];
% vioThroughput
vioThroughput = [0, 0, 0, 0];
% non_allo_vio
non_allo_vio = [1400, 0, 0, 0];
% nChunks
nChunks = [150, 35, 12, 450];
% prefStartTime
prefStartTime = [0, 6, 0, 18];
% deadline
deadline = [30, 7, 100000, 48];
% availBW
availBW = [37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37; 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 27, 41, 41, 41, 41, 41, 41, 27, 13, 0, 0, 0, 0, 0, 0; 0, 0, 0, 21, 43, 65, 65, 65, 65, 65, 65, 43, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 24, 49, 74, 74, 74, 74, 74, 74, 49, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 29, 58, 58, 58, 58, 58, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 22, 44, 67, 67, 67, 67, 67, 67, 67, 44, 22, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 16, 33, 33, 33, 33, 33, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 18, 36, 54, 54, 54, 54, 54, 54, 36, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

