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

	* devide Requests into chunks (not specific but a number?)
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
 int IMP_STRETCH = 3; //min time between chunks
 int IMP_COMPRESS = 4; //max time between chunks
 int IMP_UNSCHED = 5;
 int IMP_LCY = 6;
 int IMP_JIT = 7;
 
//#######  basic inputs #########
//array sizes
 int nChannels = ...;
 int nRequests = ...;
 int nInterfaceTypes = ...;	//Anzahl verschiedener Interface-typen; zb (WiFi, LTE, 11p) = 3
 int nTime = ...;

 
range Channels = 1..nChannels;
range Requests = 1..nRequests;
range Time = 1..nTime;
range InterfaceTypes = 1..nInterfaceTypes;
range ImportanceTypes = 1..nImportanceTypes;

//#######  input values #########

//channel model
int availBW[Channels, Time] = ...;
int channel_cost[Channels] = ...;				//static cost per channel (change later to more realistic model, high-speed traffic limit?)
int channel_lcy[Channels] = ...;
int channel_jit[Channels] = ...;

//channel types
int ChannelType[Channels] = ...;				//each channel has a specific type; e.g. WiFi, LTE..
int nClientInterfaces[InterfaceTypes] = ...;	//the Client has a specific number of interfaces of each type; (WiFi, LTE)


/*
Update in comparison to Backed up:
-- removed chunksizes


*/



//request
	//chunks
int nChunks[Requests] = ...;
	//timing
int deadline[Requests] = ...;
int prefStartTime[Requests] = ...;
int minTimeBetweenChunks[Requests] = ...;
int stretch_max[Requests] = ...;
int maxTimeBetweenChunks[Requests] = ...;
int compress_min[Requests] = ...;
int latency[Requests] = ...;
int jitter[Requests] = ...;
	//weights/importance
int importance[ImportanceTypes, Requests] = ...; 

//user profiles
int userImportance[Requests] = ...;
int userWTP = ...; //willingness to pay

//#######  output values (decision and expression variables) #########

//Schedule
dvar int+ allocatedChunks[Requests, Time];	//allocate
dvar int+ selectedChannel[Requests, Time, Channels] in 0..1; 	//channel selection decoupled from 
dvar int+ non_allocated[Requests];
dvar int+ cummulatedChunks[Requests, Time];

//violation decision variables
dvar int+ vioStretch[Requests, Time];
dvar int+ vioCompress[Requests, Time];


dexpr int vioLcy[r in Requests] = sum(t in Time, c in Channels) (
						allocatedChunks[r, t]*importance[IMP_LCY, r]*selectedChannel[r,t,c]*			//for allocated chunks
						(channel_lcy[c]-latency[r])*(channel_lcy[c]-latency[r])*	//latency violation square
						( (channel_lcy[c]-latency[r]) >=0)							//sum only if positive
						);

dexpr int vioJit[r in Requests] = sum(t in Time, c in Channels) (					//similar to latency model
						allocatedChunks[r,t]*importance[IMP_JIT, r]*selectedChannel[r,t,c]*
						(channel_jit[c]-jitter[r])*(channel_jit[c]-jitter[r])*
						( (channel_jit[c]-jitter[r]) >=0)
						);


//starttime violoation
dexpr int st_vio[r in Requests] = 
	sum(t in Time)(
		importance[IMP_STARTTIME, r]*	//importance
		(1<=(prefStartTime[r]-t))*		//Start time is violated
		((prefStartTime[r]-t)*(prefStartTime[r]-t))*		//strength of violation (quadratic)
		allocatedChunks[r, t]			//linear weight on BW of violation
	);
//deadline violation
dexpr int	dl_vio[r in Requests] = 
	sum(t in Time)(
		importance[IMP_DEADLINE, r]*		//request specific importance
		(1<=(t-deadline[r]))* 				//is violated
		((t-deadline[r])*(t-deadline[r]))*	//quadratic characteristic of punishment concerning violation time
		allocatedChunks[r, t]				//linear factor of punishment concerning bandwidth during violation
	);
	
//punishment for non allocated chunks
//dexpr int non_allocated[r in Requests] =
//		nChunks[r] - sum(t in Time, c in Channels)(allocatedChunks[r, t, c]);	//should be zero for all r in Requests	
dexpr int non_allo_vio[r in Requests] = 
		userImportance[r]*
		importance[IMP_UNSCHED, r]*
		non_allocated[r];	


//Throughput violation		
dexpr int vioThroughput[r in Requests] = sum(t in Time) ((vioStretch[ r, t]*importance[IMP_STRETCH,  r])
														+(vioCompress[r, t]*importance[IMP_COMPRESS, r]));  


//punish switches slightly to avoid ping-pong (and to model control traffic for switch)
dexpr int cost_switch = sum(r in Requests, t in 1..nTime-1, c in Channels)(
				selectedChannel[r, t, c]!=selectedChannel[r, t+1, c]
			);
			
dexpr int cost_violation = sum(r in Requests)((st_vio[r]+dl_vio[r]+non_allo_vio[r]+vioThroughput[r]+vioLcy[r]+vioJit[r])*userImportance[r]);
dexpr int cost_ch = sum(r in Requests, t in Time, c in Channels)(allocatedChunks[r, t]*channel_cost[c]);

dexpr int cost_total = cost_violation+cost_ch+cost_switch;



//#######  model #########

minimize cost_total;
subject to{
	//allocate resources
	
	//allocate resources
	forall (r in Requests)( 
		sum (t in Time)allocatedChunks[r,t] + non_allocated[r] == nChunks[r] 
	);
	
	forall (r in Requests, t in Time, c in Channels)(
		selectedChannel[r,t,c] >= (allocatedChunks[r,t] !=0)
	);
		
	//do not overuse available resources
	forall (t in Time, c in Channels)(
	  	sum (r in Requests)allocatedChunks[r, t]*selectedChannel[r,t,c] <= availBW[c, t]
	);
	
	//for each request, there is always one channel selected
	forall (r in Requests, t in Time)(
		sum (c in Channels) selectedChannel[r,t,c]==1
	);//if channels are different (c1 not used or c2 not used by request in time slot)
	
	/********************************************************************/  
	
	//do not use more Channels of the same type at the same time than correspondent network interfaces available
	forall (t in Time, it in InterfaceTypes)(
		sum(c in Channels)(		//sum channels of type it	
			( sum (r in Requests) selectedChannel[r,t,c])  //channel is used and
			* (ChannelType[c]==it)		//channel is of type "it"	
		)
		 <= nClientInterfaces[it]
	);	///sum of used interfaces must be smaller/equal than available interfaces						
	
	
		//first, calculate cummulated chunks
	forall(r in Requests)( //first timeslot is 0+first
		cummulatedChunks[r, 1] == sum(c in Channels)allocatedChunks[r, 1]*selectedChannel[r,1,c]
	);
	forall(r in Requests, t in 2..nTime)( //other timeslots is sum of previous cummulated plus currently allocated chunks
		cummulatedChunks[r, t] == cummulatedChunks[r, t-1]+sum(c in Channels)allocatedChunks[r, t]*selectedChannel[r,t,c]
	);
		
	//stretch
	forall(r in Requests, t1 in Time)(
		(t1==minTimeBetweenChunks[r])<=(cummulatedChunks[r, t1]<=stretch_max[r]+vioStretch[r, t1])
		//for first timeslot: the number of commulated chunks for a request in t1 must be less than the minimum
	);
	forall(r in Requests, t0 in Time, t1 in Time)(
		(t1-t0==minTimeBetweenChunks[r])<=(cummulatedChunks[r, t1]-cummulatedChunks[r, t0]<=stretch_max[r]+vioStretch[r, t1])
		//in a certain timespan, there must be less than stretch_max chunks
	);
		//compress
	forall(r in Requests, t1 in Time)(
		(t1==maxTimeBetweenChunks[r])<=(cummulatedChunks[r, t1]>=compress_min[r]-vioCompress[r, t1])
	);
	forall(r in Requests, t0 in Time, t1 in Time)(
		(t1-t0==maxTimeBetweenChunks[r])<=(cummulatedChunks[r, t1]-cummulatedChunks[r, t0]>=compress_min[r]-vioCompress[r,t1] )
		//in a certain timespan, there must be at least compress_min chunks
	);

}


 