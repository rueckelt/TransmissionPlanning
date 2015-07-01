% t_n_i
% time:
generate_model = 8073;
duration_to_solve_model_us = 382;
create_model = 64;
% allocatedChunks
allocatedChunks(:,:,1) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5];
allocatedChunks(:,:,2) = [10; 10; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,3) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 10; 2; 0; 0; 0];
allocatedChunks(:,:,4) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,5) = [0; 0; 5; 0; 10; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,6) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5];
allocatedChunks(:,:,7) = [0; 0; 5; 10; 0; 10; 10; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,8) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 4; 8; 0; 0];
allocatedChunks(:,:,9) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,10) = [0; 0; 0; 0; 0; 0; 0; 10; 10; 10; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,11) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5];
allocatedChunks(:,:,12) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 10; 10; 10; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,13) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 2; 10; 0];
allocatedChunks(:,:,14) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 5; 10; 10; 10; 0; 0; 0; 0; 0];
allocatedChunks(:,:,15) = [0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 0; 10; 10; 10; 5; 0; 0; 0; 0; 0; 0; 0; 0];
allocatedChunks(:,:,16) = [5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5; 5];
% non_allocated
non_allocated = [25, 15, 0, 75, 20, 25, 0, 0, 75, 5, 25, 5, 0, 40, 0, 25];
% dl_vio
dl_vio = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 80, 0, 350, 240, 0];
% st_vio
st_vio = [0, 0, 0, 0, 0, 1500, 0, 0, 0, 0, 14250, 0, 0, 0, 0, 50750];
% vioThroughput
vioThroughput = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
% non_allo_vio
non_allo_vio = [1400, 1575, 0, 3150, 2100, 1400, 0, 0, 3150, 525, 1400, 525, 0, 1680, 0, 1400];
% nChunks
nChunks = [150, 35, 12, 75, 35, 150, 35, 12, 75, 35, 150, 35, 12, 75, 35, 150];
% prefStartTime
prefStartTime = [0, 1, 0, 3, 4, 5, 6, 0, 8, 9, 10, 11, 0, 13, 14, 15];
% deadline
deadline = [30, 2, 100000, 8, 5, 35, 7, 100000, 13, 10, 40, 12, 100000, 18, 15, 45];
% availBW
availBW = [30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30];

