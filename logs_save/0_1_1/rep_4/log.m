% t_n_i
% time:
generate_model = 1800;
duration_to_solve_model_us = 147;
create_model = 12;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0];
allocatedChunks(:,:,2) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 5, 0; 0, 0; 0, 0; 30, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
% non_allocated
non_allocated = [75, 0];
% dl_vio
dl_vio = [0, 0];
% st_vio
st_vio = [0, 0];
% vioThroughput
vioThroughput = [0, 0];
% non_allo_vio
non_allo_vio = [4200, 0];
% nChunks
nChunks = [200, 35];
% prefStartTime
prefStartTime = [0, 12];
% deadline
deadline = [40, 13];
% availBW
availBW = [35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 65, 65, 65, 65, 65, 32, 0, 0, 0, 0, 0];

