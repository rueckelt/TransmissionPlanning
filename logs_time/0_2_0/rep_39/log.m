% t_n_i
% time:
generate_model = 1500;
duration_to_solve_model_us = 180;
create_model = 19;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 0, 5; 0, 0, 5, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 0, 5, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0; 5, 0, 0, 0];
% non_allocated
non_allocated = [60];
% dl_vio
dl_vio = [0];
% st_vio
st_vio = [0];
% vioThroughput
vioThroughput = [0];
% non_allo_vio
non_allo_vio = [3360];
% nChunks
nChunks = [185];
% prefStartTime
prefStartTime = [0];
% deadline
deadline = [37];
% availBW
availBW = [35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 22, 34, 34, 34, 34, 34, 34, 22, 11, 0, 0, 0, 0, 0; 0, 0, 0, 0, 0, 0, 0, 0, 23, 46, 70, 70, 70, 70, 70, 70, 46, 23, 0, 0, 0, 0, 0, 0, 0; 0, 0, 0, 38, 76, 76, 76, 76, 76, 38, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

