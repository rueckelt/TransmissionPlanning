%matlab eval for scheduling

t_max=1;
n_max=1;
i_max=1;
rep_max=100;

t=0;

%time = zeros(t_max,n_max,i_max,rep_max);

for t = 0:t_max-1
    for n = 0:n_max-1
       for i=0:i_max-1
           for rep=0:rep_max-1
               path = ['logs\' num2str(t) '_' num2str(n) '_' num2str(i) '\rep_' num2str(rep) '\']
               addpath(path);
               fname = [path 'log.m'];
               run(fname);
               rmpath(path);
               
               duration = generate_model + duration_to_solve_model_us;
               %collect time data of all log files
               %time(t+1,n+1,i+1,rep+1)=duration;
               time(rep+1)=duration;
                clearvars -except time t n i rep t_max n_max i_max rep_max
           end
       end 
    end
end
    


boxplot(time);










