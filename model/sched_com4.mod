/*********************************************
 * OPL 12.6.0.0 Model
 * Author: Tobias R�ckelt
 * Creation Date: 10.07.2014 at 08:59:12
 *********************************************/

/*
Model
	a number of channels
	a number of requests
	user preferences
	requests shall be mapped on channels over time
	
	
	

A channel is characterised by
	(estimated) available bandwidth per Channel for each point in time
		- time is discretized into slots
		- bandwidth is given as a number (bucketsize) for each timeslot per channel
	(estimated) latency and other??
	cost by bandwidth 




A request is characterised by
	bandwidth restrictions/requirement
		- total required bandwidth		 or 	per chunk+nChunks
	latency restrictions
		- deadline 
		- a maximum and minimum time between first and n'th chunk (start time assigned to first) 
				default transfer have neither max nor min (contrary extreme values)
				bufferable streams have only a max
				constant streams or periodic updates have max and min restrictions
	importance of restrictions
		- weights (1..wMax) for application requests
			explicit comm.requests from user can have a high importance / additional factor in model??
			later on: different weights for min, max, deadline
				e.g. VoIP will not have data available, therefore punish min-violation very hard

	
User profiles declare
	user defined importance to hold request restrictions for each application
	willingness to pay for better connectifity
									
		
Detection of channel switches
	* Streams may be interrupted, therefore one cannot search for events, where chunks are allocated 
	in another channel at next time step.
	* To be able to do this anyway, I introduced "closed_gaps" variable. It is 1 for each channel and time
	where it a) is used  b) if stream is interrupted in last channel used
	* It assumes, that for one stream, only a single channel is used at the same time	
	* punishment of switches can be used to express bad influence of additional control traffic and negative side effects
				

Cost function minimize
	violation of restrictions (positive value) 
		multiplied by application/request importance weight
		multiplied by user profile application importance weight (usually a default value)
	monetary cost for channel use
		Used bandwidth per timeslot multiplied by cost 
		willingness to pay as factor (to decide between cost and restriction violation); exponential?
	switch of channel for a request within schedule
		only change channel if necessary (threshold)

Time/Bandwidth Model		
	* express time in buckets (per channel)
	* every channel has a limited number of slots in each bucket that expresses bandwidth (dynamic!)

	* devide Flows into chunks (not specific but a number?)
	* express latency requirements by maximum duration between chunks
		is this model feasible? these are only interruptions!! Latency of the channel is not covered! --> need additional parameter
		--> latency should be: latency of channel + time between chunks ; prefStartTime should also be part of it



OPEN ISSUES
/*

allow to use channels simultaneously with same Request??	!= closed_gaps			




Open questions: 
	how to model additional control traffic for channel switch? 
	periodic reqeusts can establish new connections instead of keeping it open: 
	how to model preceedence constraint: preferred start time?? 

*/

// using CP;
 using CPLEX;
 
 //constants
 int nImportanceTypes = 7;
 
 int IMP_DEADLINE = 1;
 int IMP_STARTTIME = 2;
 int IMP_TP_MAX = 3; //upper throughput limit
 int IMP_TP_MIN = 4; //lower throughput limit
 int IMP_UNSCHED = 5;
 int IMP_LCY = 6;
 int IMP_JIT = 7;
 
//#######  basic inputs #########
//array sizes
 int nNetworks = ...;
 int nFlows = ...;
 int nInterfaceTypes = ...;	//Anzahl verschiedener Interface-typen; zb (WiFi, LTE, 11p) = 3
 int nTime = ...;

 
range Networks = 1..nNetworks;
range Flows = 1..nFlows;
range Time = 1..nTime;
range InterfaceTypes = 1..nInterfaceTypes;
range ImportanceTypes = 1..nImportanceTypes;

//#######  input values #########

//channel model
int availChunkBuckets[Networks, Time] = ...;
int network_cost[Networks] = ...;				//static cost per channel (change later to more realistic model, high-speed traffic limit?)
int network_lcy[Networks] = ...;
int network_jit[Networks] = ...;

//channel types
int network_type[Networks] = ...;				//each channel has a specific type; e.g. WiFi, LTE..
int nClientInterfaces[InterfaceTypes] = ...;	//the Client has a specific number of interfaces of each type; (WiFi, LTE)


/*
Update in comparison to Backed up:
-- removed chunksizes


*/



//request
	//chunks
//int chunksize[Flows] = ...;
int nChunks[Flows] = ...;
	//timing
int deadline[Flows] = ...;
int prefStartTime[Flows] = ...;
int tpMaxWindow[Flows] = ...;
int tpMaxChunks[Flows] = ...;
int tpMinWindow[Flows] = ...;
int tpMinChunks[Flows] = ...;
int latency[Flows] = ...;
int jitter[Flows] = ...;
	//weights/importance
int hysteresis = ...;	
int importance[ImportanceTypes, Flows] = ...; 

//user profiles
int userImportance[Flows] = ...;
int costImp = ...; //willingness to pay

//#######  output values (decision and expression variables) #########

//Schedule
dvar int+ allocatedChunks[Flows, Time, Networks];	//allocate
dvar int+ non_allocated[Flows];

//dvar int+ cummulatedChunks[Flows, Time];

//violation decision variables
//dvar int+ vioTpMax[Flows, Time];
dvar int+ vioTpMin[Flows, Time];


dexpr int vioLcy[f in Flows] = sum(t in Time, n in Networks) (
						allocatedChunks[f, t, n]*importance[IMP_LCY, f]*			//for allocated chunks
						(network_lcy[n]-latency[f])*(network_lcy[n]-latency[f])*	//latency violation square
						( (network_lcy[n]-latency[f]) >=0)							//sum only if positive
						);

dexpr int vioJit[f in Flows] = sum(t in Time, n in Networks) (					//similar to latency model
						allocatedChunks[f,t,n]*importance[IMP_JIT, f]*
						(network_jit[n]-jitter[f])*(network_jit[n]-jitter[f])*
						( (network_jit[n]-jitter[f]) >=0)
						);


//starttime violoation
dexpr int st_vio[f in Flows] = 
	sum(n in Networks, t in Time)(
		importance[IMP_STARTTIME, f]*	//importance
		(1<=(prefStartTime[f]-t))*		//Start time is violated
		((prefStartTime[f]-t)*(prefStartTime[f]-t))*		//strength of violation (quadratic)
		allocatedChunks[f, t, n]			//linear weight on BW of violation
	);
//deadline violation
dexpr int	dl_vio[f in Flows] = 
	sum(n in Networks, t in Time)(
		importance[IMP_DEADLINE, f]*		//request specific importance
		(1<=(t-deadline[f]))* 				//is violated
		((t-deadline[f])*(t-deadline[f]))*	//quadratic characteristic of punishment concerning violation time
		allocatedChunks[f, t, n]				//linear factor of punishment concerning bandwidth during violation
	);
	
//punishment for non allocated chunks
//dexpr int non_allocated[f in Flows] =
//		nChunks[f] - sum(t in Time, n in Networks)(allocatedChunks[f, t, n]);	//should be zero for all f in Flows	
dexpr int non_allo_vio[f in Flows] = 
		userImportance[f]*
		importance[IMP_UNSCHED, f]*
		non_allocated[f];	


//Throughput violation		
dexpr int vioThroughput[f in Flows] = sum(t in Time) (//(vioTpMax[ f, t]*importance[IMP_TP_MAX,  f])+ //upper tp limit as hard constraint, not cost function
														(vioTpMin[f, t]*importance[IMP_TP_MIN, f]));  


//punish switches slightly to avoid ping-pong (and to model control traffic for switch)
dvar int closed_gaps[Flows, Time, Networks] in 0..1;
dexpr int cost_switch = sum(f in Flows, t in 1..nTime-1, n in Networks)(
				closed_gaps[f, t, n]!=closed_gaps[f, t+1, n]
			)*hysteresis;
			
dexpr int cost_violation = sum(f in Flows)((st_vio[f]+dl_vio[f]+non_allo_vio[f]+vioThroughput[f]+vioLcy[f]+vioJit[f])*userImportance[f]);
dexpr int cost_ch = sum(f in Flows, t in Time, n in Networks)(allocatedChunks[f, t, n]*network_cost[n]);

dexpr int cost_total = cost_violation+cost_ch*costImp+cost_switch;



//#######  model #########

minimize cost_total;
subject to{
	//allocate resources
	
	//allocate resources
	forall (f in Flows)( 
		sum (t in Time, n in Networks)allocatedChunks[f,t,n] + non_allocated[f] == nChunks[f] 
	);
		
	//do not overuse available resources
	forall (t in Time, n in Networks)(
	  	sum (f in Flows)allocatedChunks[f, t, n] <= availChunkBuckets[n, t]
	);
	
	//do not allow parallel channel use for single request
	//forall (f in Flows, t in Time, n1 in Networks, n2 in Networks)(
	//	(n1!=n2)	<=	((allocatedChunks[f,t,n1]==0) + (allocatedChunks[f,t,n2]==0))
	//);
	
		
	//do not use more Networks of the same type at the same time than correspondent network interfaces available
	forall (t in Time, it in InterfaceTypes)(
		sum(n in Networks)(		//sum channels of type it	
			(( sum (f in Flows) allocatedChunks[f,t,n]) >= 1)  //channel is used and
			* (network_type[n]==it)		//channel is of type "it"	
		)
		 <= nClientInterfaces[it]
	);	///sum of used interfaces must be smaller/equal than available interfaces						
	
	
	
	
	/********************************************************************/  
	//puhish channel switches (ping pong avoidance)
	forall (f in Flows, t in Time)(
		sum (n in Networks)(closed_gaps[f, t, n])==1		//no gaps: in each timeslot sum is one; so channel selection takes place even if no traffic is scheduled
	);
	forall (f in Flows, t in Time, n in Networks)(
		closed_gaps[f, t, n]>=(allocatedChunks[f, t, n]!=0) //force equality of scheduled chunks to channel selection
	);
	
	//first, calculate cummulated chunks
	/*forall(f in Flows)( //first timeslot is 0+first
		cummulatedChunks[f, 1] == sum(n in Networks)allocatedChunks[f, 1, n]
	);
	forall(f in Flows, t in 2..nTime)( //other timeslots is sum of previous cummulated plus currently allocated chunks
		cummulatedChunks[f, t] == cummulatedChunks[f, t-1]+sum(n in Networks)allocatedChunks[f, t, n]
	);*/
		
	//upper throughput limit: commented out vioTpMax --> upper throughput limit is a hard constraint now
	/*forall(f in Flows, t1 in Time)(
		(t1>=prefStartTime[f]+tpMaxWindow[f])*(t1<=deadline[f])*(t1==tpMaxWindow[f])<=(cummulatedChunks[f, t1]<=tpMaxChunks[f])//+vioTpMax[f, t1])
		//for first timeslot: the number of commulated chunks for a request in t1 must be less than the minimum
	);*/	
	//time span equal to window and within startTime/Deadline limits ===implies==> number of chunks in window less or equal to max+vio
	forall(f in Flows, t0 in Time, t1 in Time)(
		(t0>=prefStartTime[f])*(t1<=deadline[f])*(t1-t0==tpMaxWindow[f]-1)<=
		(sum(n in Networks, t in t0..t1)allocatedChunks[f,t,n]//cummulatedChunks[f, t1]-cummulatedChunks[f, t0]
		<=tpMaxChunks[f])//+vioTpMax[f, t1])
		//in a certain timespan, there must be less than tpMaxChunks chunks
	);
	
	//lower throughput limit
	/*forall(f in Flows, t1 in Time)(
		(t1>=prefStartTime[f]+tpMinWindow[f])*(t1<=deadline[f])*(t1==tpMinWindow[f])<=(cummulatedChunks[f, t1]>=tpMinChunks[f]-vioTpMin[f, t1])
	);*/
	//time span equal to window and within startTime/Deadline limits ===implies==> number of chunks in window more or equal to min-vio
	forall (f in Flows){
		forall (t1 in tpMinWindow[f]..nTime)(	//to and t1 are lower/upper limits of troughput window
		(t1-tpMinWindow[f]>=prefStartTime[f])*(t1<=deadline[f])<=(//cummulatedChunks[f, t1]-cummulatedChunks[f, t0]
		(sum(n in Networks, t in t1-tpMinWindow[f]..t1)allocatedChunks[f,t,n]) >= (tpMinChunks[f]-vioTpMin[f,t1]) )
		//in a certain timespan, there must be at least tpMinChunks chunks
	);

}


 