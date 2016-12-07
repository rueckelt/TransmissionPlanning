package schedulingIOModel;

import java.util.HashMap;
import java.util.Vector;

public class UncertaintyErrorCalculation {

	public float getFlowUncertaintyError(Vector<Flow> predicted, Vector<Flow> actual){
		//1. Collect all Flows and merge information. Required if flow was canceled or split.
			HashMap<Integer, Integer> act_nof_tokens = new HashMap<Integer,Integer>();
			HashMap<Integer, Integer> act_deadline = new HashMap<Integer, Integer>();
			
			for(Flow f: actual){
				//combine tokens of continued flows
				if(act_nof_tokens.containsKey(f.getId())){
					//is continued flow, second part.
					act_nof_tokens.put(f.getId(), act_nof_tokens.get(f.getId())+f.getTokens());		//add tokens

//					System.out.println("f"+f.getId()+"is paused and continued");
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
			}
		//2. calcualte SMAPE of tokens and deadlines (separately)

			HashMap<Integer, Float> smape_tokens = new HashMap<Integer, Float>();
			HashMap<Integer, Float> smape_deadlines = new HashMap<Integer, Float>();
			
			//we start with predicted and then go for the other in the hash map.
			for(Flow pred:predicted){
				//tokens
//				System.out.print("token    smape of f"+pred.getId() +" is ");
				if(act_nof_tokens.containsKey(pred.getId())){
					smape_tokens.put(pred.getId(),getSmape(pred.getTokens(), act_nof_tokens.get(pred.getId())));
				}else{
					smape_tokens.put(pred.getId(),getSmape(pred.getTokens(), 0)); //must be canceled completely. 
				}
				//deadline
//				System.out.print("deadline smape of f"+pred.getId()+" is ");
				if(act_deadline.containsKey(pred.getId())){
					smape_deadlines.put(pred.getId(),getSmape(pred.getDeadline(), act_deadline.get(pred.getId())));
				}else{
					smape_deadlines.put(pred.getId(),(float) 0);	//must be canceled completely. 
				}
			}
			
			//for all unseen flows (new ones that have not been predicted) add smape and deadline from 'actual' list.
			for(int f_id : act_nof_tokens.keySet()){
				if(!smape_tokens.containsKey(f_id)){
					int tokens = act_nof_tokens.get(f_id);
//					System.out.print("token smape of f"+f_id +" is ");
					smape_tokens.put(f_id, getSmape(0, tokens));
				}
			}	
			for(int f_id : act_deadline.keySet()){
				if(!smape_deadlines.containsKey(f_id)){
//					System.out.print("deadline smape of f"+f_id +" is ");
					smape_deadlines.put(f_id, getSmape(0, 1));	//new added flow
				}
			}
			
					
		//3. smape_flow calculation
			HashMap<Integer, Float> smape_flow = new HashMap<Integer, Float>();
			float alpha = (float) 0.5;
			
			//calculate flow restrictiveness (from greedy heuristic) to weight flows.
			
			float sum_criticalities = 0;
			for(int f_id : smape_tokens.keySet()){
				//get flow first with actual tokens + deadline to put it into the function then
				Flow flow=null;
				//get from actual
				for(Flow f_temp:actual){
					if(f_temp.getId()==f_id){
						flow=f_temp.clone();
						flow.setTokens(act_nof_tokens.get(f_id));
						flow.setDeadline(act_deadline.get(f_id));
						break;
					}
				}
				if(flow==null){
					//if canceled, get from predicted
					for(Flow f_temp:predicted){
						if(f_temp.getId()==f_id){
							flow=f_temp;
							break;
						}
					}
				}
				
				//put into function
				float criticality =calculateFlowCriticality(flow);
				sum_criticalities+=criticality;
				
				float dl=(float)0;
				if(smape_deadlines.containsKey(f_id)){
					dl=smape_deadlines.get(f_id);
				}
				smape_flow.put(f_id, criticality*smape_tokens.get(f_id)*alpha + dl*(1-alpha));
			}
			
		//4. get final SMAPE error
			
			float sum_smape=0;
			for(int f_id: smape_flow.keySet()){
				sum_smape += smape_flow.get(f_id);	//summation could be integrated above for performance gain
//				System.out.println("flow smape of f "+f_id+" is "+smape_flow.get(f_id));
			}
			
			float smape = sum_smape/sum_criticalities;
//			System.out.println("result = "+smape+", sum_smape="+sum_smape+", #flows="+smape_flow.size()+", sum_crit="+sum_criticalities);
			return smape;
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
	
		private float calculateFlowCriticality(Flow f){
			if(true)return 1;		//equal weight for all flows
			
			float c= f.getImpUser()*(
					//attracting factors: important because cost rises fast if not scheduled
					f.getTokensMin()/f.getWindowMin() *f.getImpThroughputMin()	//average number of min tokens to schedule
					+f.getImpUnsched())*f.getTokens()/(f.getDeadline()-f.getStartTime())	//average number of tokens to schedule
					
					//repelling factors: important because cost rises fast if wrongly scheduled
					+f.getReqJitter()*f.getImpJitter()
					+f.getReqLatency()*f.getImpLatency()
					;
			c=(float) Math.log10(c);
//			System.out.println("flow criticality of f "+f.getId()+" is "+c+", type is "+f.getFlowName());
			return c;
			
		}
	
}
