% t_n_i
% time:
generate_model = 8223;
duration_to_solve_model_us = 600;
create_model = 30;
% allocatedChunks
allocatedChunks(:,:,1) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0];
allocatedChunks(:,:,2) = [0, 0; 17, 0; 1, 0; 17, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
allocatedChunks(:,:,3) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 12, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
allocatedChunks(:,:,4) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 16, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 39; 0, 39; 0, 39; 0, 39; 0, 39; 0, 14; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
allocatedChunks(:,:,5) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 17, 0; 0, 0; 1, 0; 17, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
allocatedChunks(:,:,6) = [5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 0, 5; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0; 5, 0];
allocatedChunks(:,:,7) = [17, 0; 0, 0; 4, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 14; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0];
allocatedChunks(:,:,8) = [0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 0, 0; 12, 0];
% non_allocated
non_allocated = [30, 0, 0, 0, 0, 25, 0, 0];
% dl_vio
dl_vio = [0, 0, 0, 0, 0, 0, 0, 0];
% st_vio
st_vio = [0, 0, 0, 0, 0, 50750, 0, 0];
% vioThroughput
vioThroughput = [0, 0, 0, 0, 0, 0, 0, 0];
% non_allo_vio
non_allo_vio = [1680, 0, 0, 0, 0, 1400, 0, 0];
% nChunks
nChunks = [155, 35, 12, 225, 35, 150, 35, 12];
% prefStartTime
prefStartTime = [0, 3, 0, 9, 12, 15, 18, 0];
% deadline
deadline = [31, 4, 100000, 24, 13, 45, 19, 100000];
% availBW
availBW = [27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27; 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 49, 49, 49, 49, 49, 24, 0, 0, 0, 0, 0, 0];

