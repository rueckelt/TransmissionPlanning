Transmission Planning Framework by Tobias Rückelt v0.2
==============================================

Problem to solve:
==Transmission Planning== 
Mobile clients can use different wireless networks to transmit data. When moving, availability of these networks changes over time because networks might not be reachable everywhere or connection quality changes. Knowing the track of the mobile client, e.g. from vehicle navigation system, the a network prediction over time can be made.
Moreover, applications have individual requirements on data transmissions. Those requirements include network aspects, like latency and throughput, and also time aspects, like deadlines. To satisfy those requirements, appropriate network and transmission time for each data transmission should be chosen. This is the goal of scheduling.

==Adaptation==
Transmission Planning is often based on predicted inputs. The framework can analyze the impact of prediction errors on the transmission performance.
In additon, an adaptation algorithm modifies the transmission as initially planned according to recognized environmental changes. It uses an opportunistic algorithm as base.
The approach can maintain major share of the benefits from transmission planning approaches.

Papers are attached in the repository.

==========================================	

Model:
Time is devided in time slots.
Networks are characterized by their latency, jitter, cost for use, throughput. Throughput is given as capapacity per time slot.
Data Flows are data transmissions of applications. They are characterized by their amount of data and requirements for latency, jitter, start time, deadline, throughput. Amount of data is abstracted to a number of tokens, which each represent a certain fixed of data.
To create a schedule, tokens of data flows are assigned to Networks and time slots. Assignment per network and time slot is limited by network capacity. 
Matching of requirements is checked by comparing the characteristics of networks to which a token is assigned to the requirements corresponding to a data flow. Same holds for time.

Schedule quality is rated by data flow requirement satisfaction. Therefore we model requirement satisfaction as "forces" and bipartite requirements in two kinds: 
- attracting forces, which endorse allocation of tokens to any time slot or network. These are:
	* minimum throughput requirement
	* punishment for unscheduled (unallocated) tokens of data flows
- repelling forces, which push tokens away from networks and time slots which violate requirements. If requirements match, repelling force between a data flow and a network is (near) zero.
	* latency, jitter violation between data flow and network
	* start time and deadline violation for allocated tokens of a data flow
	* cost for network use
	* flow migration cost when a data flow switches the network (handover overhead/ ping pong avoidance)

	The sum of forces of a schedule defines a kind of tension. A schedule with a high tension is usually bad, because requirements of data flows must be violated.
	We calculate this tension in a cost function for schedules.

	
==========================================	

Schedulers:
For evaluation:
	- Optimization:
		Minimizes the cost function using IBM CPLEX solver. Finds optimal schedule in exponential time (slow!). Is used as reference for quality assessment, providing upper quality bound.
		IBM library oplall.jar is required.
		
	- Random:
		Assigns tokens of data flows between start time and deadline to a random network. We let it run X times and average schedule quality and execution time. It is used as reference for quality assessment, providing a quality bound that should be lower than any reasonable scheduler result. We don't want any scheduler which is worse than random.

Derived from related work:	
	- Network Selection: Prioritize data flows according to their attracting forces. For each time slot (one after another), allocate data to the *best matching* currltly available network.

	- Opportunistic Network Selection: Extends Network Selection by the option to pause transmission, whenever an estimated benefit via the currently available networks is rated too low (repelling forces > attracting forces).

	- Delayed WiFi Offloading: Considers predicted networks within a certain planning time horizon. It uses a WiFi-preferred strategy and selects the transmission time (within the planning time horizon and data flow deadline) in a way that as much data as possible is transmitted via WiFi networks. Considers no further network-matching.

Own approach:
	Joint Transmission Planning with time-network selection: Plans transmission to the best-matching networks within a planning time horizon. Uses an opportunistic component to delay data transfer beyond the planning time horizon.


==========================================	

Setup:
- Import project into Eclipse
- Install IBM OPL Studio and add to PATH to enable Optimization: 
	#download from https://www.dropbox.com/sh/pnerehn57fgsuy3/AABDZm5qTmiTjzPTUBNckKEYa?dl=0

	**Windows: install and add to Path Variable 
	[..your_path..]\CPLEX_Studio126\opl\bin\x86-64_win
	
	**Linux:
	chmod a+x cplex_studio126.linux-x86-64.bin
	sudo ./cplex_studio126.linux-x86-64.bin
	#add to Path: /opt/ibm/ILOG/CPLEX_Studio126/opl/bin/x86-64_linux

		#did not work for me.. therefore add to java runtime environment:

		#Right-click on project/Properties/Run-Debug Settings/
		#choose main(1) and click edit
		#select Environment / New..
		#Name: LD_LIBRARY_PATH
		#Value: /opt/ibm/ILOG/CPLEX_Studio126/opl/bin/x86-64_linux


