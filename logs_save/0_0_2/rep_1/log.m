% t_n_i
% time:
generate_model = 725;
duration_to_solve_model_us = 55;
create_model = 8;
% allocatedChunks
allocatedChunks(:,:,1) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5];
allocatedChunks(:,:,2) = [0; 0; 0; 4; 31; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,3) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 12; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,4) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 31; 31; 31; 31; 31; 31; 31; 31; 31; 31; 31; 31];
% non_allocated
non_allocated = [50, 0, 0, 78];
% dl_vio
dl_vio = [0, 0, 0, 0];
% st_vio
st_vio = [0, 0, 0, 1860];
% vioThroughput
vioThroughput = [0, 0, 0, 0];
% non_allo_vio
non_allo_vio = [2800, 0, 0, 3276];
% nChunks
nChunks = [175, 35, 12, 450];
% prefStartTime
prefStartTime = [0, 6, 0, 18];
% deadline
deadline = [35, 7, 100000, 48];
% availBW
availBW = [36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36];

