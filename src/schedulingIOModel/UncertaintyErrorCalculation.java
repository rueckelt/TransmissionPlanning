package schedulingIOModel;

import java.util.HashMap;
import java.util.Vector;

import schedulers.DummyScheduler;
import schedulers.HeuristicScheduler;

public class UncertaintyErrorCalculation {
	
	private final double alpha = 0.5;
	private int nof_timeslots = 0;

	private HashMap<Integer, Integer> act_nof_tokens = new HashMap<Integer,Integer>();
	private HashMap<Integer, Integer> act_deadline = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> act_starttime = new HashMap<Integer, Integer>();

	private HashMap<Integer, Integer> pred_deadline = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> pred_starttime = new HashMap<Integer, Integer>();
	

	private HashMap<Integer, Float> smape_tokens = new HashMap<Integer, Float>();
	private HashMap<Integer, Float> smape_deadlines = new HashMap<Integer, Float>();
	private HashMap<Integer, Float> smape_starttimes = new HashMap<Integer, Float>();
	

	private HashMap<Integer, Float> smape_flow = new HashMap<Integer, Float>();
	
	public UncertaintyErrorCalculation(Vector<Flow> predicted, Vector<Flow> actual, int timeslots){
		//1. Collect all Flows and merge information. Required if flow was canceled or split.
		nof_timeslots=timeslots;
		for(Flow f: actual){
			//combine tokens of continued flows
			if(act_nof_tokens.containsKey(f.getId())){
				//is continued flow, second part.
				act_nof_tokens.put(f.getId(), act_nof_tokens.get(f.getId())+f.getTokens());		//add tokens

//				System.out.println("f"+f.getId()+"is paused and continued");
			}else{
				act_nof_tokens.put(f.getId(), f.getTokens());	
			}
			//combine deadline of continued flows.
			if(act_deadline.containsKey(f.getId())){
				//is continued flow, second part.
				act_deadline.put(f.getId(), Math.max(act_deadline.get(f.getId()), f.getDeadline()));		//add tokens
			}else{
				act_deadline.put(f.getId(), f.getDeadline());	
			}
			//combine start time of continued flows: earlier
			if(act_starttime.containsKey(f.getId())){
				//is continued flow, second part.
				act_starttime.put(f.getId(), Math.min(act_starttime.get(f.getId()), f.getStartTime()));		//add tokens
			}else{
				act_starttime.put(f.getId(), f.getStartTime());	
			}
		}
	//2. calcualte SMAPE of tokens and deadlines (separately)
		
		//we start with predicted and then go for the other in the hash map.
		for(Flow pred:predicted){
			//tokens
//			System.out.print("token    smape of f"+pred.getId() +" is ");
			if(act_nof_tokens.containsKey(pred.getId())){
				smape_tokens.put(pred.getId(),getSmape(pred.getTokens(), act_nof_tokens.get(pred.getId())));
			}else{
				smape_tokens.put(pred.getId(),getSmape(pred.getTokens(), 0)); //must be canceled completely. 
			}
			//deadline
			pred_deadline.put(pred.getId(), pred.getDeadline());
//			System.out.print("deadline smape of f"+pred.getId()+" is ");
			if(act_deadline.containsKey(pred.getId())){
				smape_deadlines.put(pred.getId(),getSmape(pred.getDeadline(), act_deadline.get(pred.getId())));
			}else{
				smape_deadlines.put(pred.getId(),(float) 0);	//must be canceled completely. 
			}
			//starttime
			pred_starttime.put(pred.getId(), pred.getStartTime());
//			System.out.print("deadline smape of f"+pred.getId()+" is ");
			if(act_starttime.containsKey(pred.getId())){
				smape_starttimes.put(pred.getId(),getSmape(pred.getStartTime(), act_starttime.get(pred.getId())));
			}else{
				smape_starttimes.put(pred.getId(),(float) 0);	//must be canceled completely. 
			}
		}
		
		//for all unseen flows (new ones that have not been predicted) add smape and deadline from 'actual' list.
		for(int f_id : act_nof_tokens.keySet()){
			if(!smape_tokens.containsKey(f_id)){
				int tokens = act_nof_tokens.get(f_id);
//				System.out.print("token smape of f"+f_id +" is ");
				smape_tokens.put(f_id, getSmape(0, tokens));
			}
		}	
		for(int f_id : act_deadline.keySet()){
			if(!smape_deadlines.containsKey(f_id)){
//				System.out.print("deadline smape of f"+f_id +" is ");
				smape_deadlines.put(f_id, getSmape(0, 1));	//new added flow
			}
		}
		for(int f_id : act_starttime.keySet()){
			if(!smape_starttimes.containsKey(f_id)){
//				System.out.print("deadline smape of f"+f_id +" is ");
				smape_starttimes.put(f_id, getSmape(0, 1));	//new added flow
			}
		}
		
				
	//3. smape_flow calculation
		float alpha = (float) 0.5;
		
		for(int f_id : smape_tokens.keySet()){
			
			float dl=(float)0;
			float st=(float)0;
			if(smape_deadlines.containsKey(f_id)){
				dl=smape_deadlines.get(f_id);
			}
			if(smape_starttimes.containsKey(f_id)){
				st=smape_starttimes.get(f_id);
			}
			smape_flow.put(f_id, smape_tokens.get(f_id)*alpha + dl*(1-alpha)/2 +st*(1-alpha)/2);
		}
		
		
	//4. get smape for each time slot
		
		int size = smape_tokens.size();
		for (int f_id : smape_tokens.keySet()){

			//flow was added
			if(!pred_starttime.containsKey(f_id)&& act_starttime.containsKey(f_id)){
				for(int t=act_starttime.get(f_id); t<=act_deadline.get(f_id); t++){
					addSmape(t, 2.0/size);
				}
			}else
			//flow was deleted
			if(pred_starttime.containsKey(f_id)&& !act_starttime.containsKey(f_id)){
				for(int t=pred_starttime.get(f_id); t<=pred_deadline.get(f_id); t++){
					addSmape(t, 2.0/size);
				}
			}else
			//flow starts earlier or later
			if(pred_starttime.get(f_id)!=act_starttime.get(f_id)){
				for(int t= Math.min(pred_starttime.get(f_id),act_starttime.get(f_id)); 
						t<=Math.max(pred_starttime.get(f_id),act_starttime.get(f_id)); t++){
					addSmape(t, 2.0/size);	//without start time
				}
			}else			
			//flow ends earlier or later
			if(pred_deadline.get(f_id)!=act_deadline.get(f_id)){
				for(int t= Math.min(pred_deadline.get(f_id),act_deadline.get(f_id)); 
						t<=Math.max(pred_deadline.get(f_id),act_deadline.get(f_id)); t++){
					addSmape(t, 2.0/size);	//without deadline
				}
			}
			
		}
		
		
		
	}
	HashMap<Integer, Double> smape_t= new HashMap<Integer, Double>();
	private void addSmape(int t, double value){
		
		if(!smape_t.containsKey(t)){
			smape_t.put(t,0.0);
		}
		smape_t.put(t, smape_t.get(t)+value);
	}

	public float getFlowUncertaintyError(){
		return getFlowUncertaintyError(0, nof_timeslots);
	}
	
	public float getFlowUncertaintyError(int t_start, int t_end){
		double sum =0.0;
		for(int t:smape_t.keySet()){
			if(t>=t_start && t<=t_end){
				sum+=smape_t.get(t);
			}
		}
		if(sum==0) return 0;
		return (float) sum/(t_end-t_start+1);
	}
	
	public float getFlowUncertaintyError2(int t_start, int t_end){
		
			
		//4. get final SMAPE error of active flows in time range
			
			float sum_smape=0;
			int flowCount = 0;
			for(int f_id: smape_flow.keySet()){
				int i;
				if(t_end>=0) 
					i=0;
				if(t_end<t_start ||	//no proper time specified. return for all slots	
					
					//flow deleted. should be active count during planned active time	
					( flowDeletedButShouldBeActive(f_id, t_start, t_end)||		

					//flow active within the time frame
					(flowIsOrShouldBeActiveWithinTimeFrame(f_id, t_start, t_end) &&
						 (
								 		
							// window not in unchanged mid area without covering start time or deadline shifts
							(!inUnchangedMidArea(f_id, t_start, t_end)	)
					     )	
					))){
					sum_smape += smape_flow.get(f_id);	//summation could be integrated above for performance gain
					flowCount++;
//					if(t_end>0)System.out.println("smape in t = "+t_end+" of f "+f_id+" is "+smape_flow.get(f_id));
				}
			}

			
			float smape = 0;
			if(flowCount>0){
				smape = sum_smape/flowCount;	
			}
				
//			System.out.println("result = "+smape+", sum_smape="+sum_smape+", #flows="+smape_flow.size()+", sum_crit="+sum_criticalities);
			return smape;
		}
	
	private boolean flowDeletedButShouldBeActive(int f_id, int t_start, int t_end){		//start time error for every time slot
		return (!act_starttime.keySet().contains(f_id) &&	//flow not in actual set
				t_end>=act_starttime.get(f_id) && t_start<=act_deadline.get(f_id));
	}
	
	private boolean flowIsOrShouldBeActiveWithinTimeFrame(int f_id, int t_start, int t_end){
		if(act_starttime.containsKey(f_id) && pred_starttime.containsKey(f_id))
			return (t_end	 >=Math.min(act_starttime.get(f_id), pred_starttime.get(f_id)) &&	
					 t_start <=Math.max(act_deadline.get(f_id), pred_deadline.get(f_id)));	
		else if(act_starttime.containsKey(f_id))
			return (t_end	 >=act_starttime.get(f_id) &&t_start <=act_deadline.get(f_id));
		else if(pred_starttime.containsKey(f_id)){
			return (t_end	 >=pred_starttime.get(f_id) &&t_start <=pred_deadline.get(f_id));
		}else
			return false;
			
	}
	
	private boolean isNewFlow(int f_id){
		return (!pred_starttime.keySet().contains(f_id)) && act_starttime.keySet().contains(f_id);		//
	}
	
	private boolean inUnchangedMidArea(int f_id, int t_start, int t_end){
		if(act_starttime.containsKey(f_id) && pred_starttime.containsKey(f_id))
			return (
					t_start>Math.max(act_starttime.get(f_id), pred_starttime.get(f_id))) &&
					t_end <Math.min(act_deadline.get(f_id),  pred_deadline.get(f_id));
		else
			return false;
	}
	
		private float getSmape(int pred, int act){
			if(pred==act){
//				System.out.println(""+0);
				return 0;	//avoid the 0,0 case
			}
			float smape = 2*(float)Math.abs(pred-act)/(float)(Math.abs(pred)+Math.abs(act));
//			System.out.println(smape);
			return smape;
		}
	
}
