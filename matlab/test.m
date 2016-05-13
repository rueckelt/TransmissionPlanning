A=squeeze(rel_data(1,:,5,:,4,:));

    figure();
for s=1:4
    hold on;
    B=squeeze(A(s,:,:));
    errorbar(mean(B,2),std(B,0,2));
end
 set(gca,'xscale','log');
 legend('Opt','Gre','GreOl','Rnd');
 