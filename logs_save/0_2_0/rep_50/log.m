% t_n_i
% time:
generate_model = 2381;
duration_to_solve_model_us = 142;
create_model = 16;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 5, 0; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0];
% non_allocated
non_allocated = [125];
% dl_vio
dl_vio = [0];
% st_vio
st_vio = [0];
% vioThroughput
vioThroughput = [0];
% non_allo_vio
non_allo_vio = [7000];
% nChunks
nChunks = [250];
% prefStartTime
prefStartTime = [0];
% deadline
deadline = [50];
% availBW
availBW = [37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37; 0, 31, 63, 63, 63, 63, 63, 31, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 11, 22, 34, 34, 34, 34, 34, 34, 22, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 0, 25, 51, 77, 77, 77, 77, 77, 77, 51, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

