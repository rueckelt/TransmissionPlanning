package adaptation.executor;

import java.util.List;

import adaptation.utils.Printer;

public class RRB {
	public void RRBAllocate(int cap, List<Integer> restData, int t, int n, int fNum) {
		
	}
	
	public void rrb(int cap, int[] minTp, int[] executed, int fNum){
		int rest = cap;
//		while (rest > 0) {
		if (fNum == 0 || cap <= 0 || minTp.length == 0) return;
		int average = cap / fNum;		//share of the network capacity
		////System.out.println("before: rest: " + rest + " - fNum: " + fNum);
		
		//for each flow do
		for (int f = 0; f < minTp.length && rest > 0; f++) {
			int sent = 0;
			// when cap < fNum, average will be 0, but there are still data to be sent
			if (average == 0 && minTp[f] > 0) {
				sent = 1;
			} else {
				int min = minTp[f];		//minimum throughput requirement of flow
				if (min > average) {	//if it wants more than its average share, send average
					sent = average;
				} else {
					sent = min;			//else, send mintp
				}
			}
			executed[f] += sent;
			minTp[f] -= sent;
			rest -= sent;
		}	
		//}
		if (rest > 0) rrb(rest, minTp, executed, fNum);
		System.out.println("rest: " + rest + " - fNum: " + fNum);
		Printer.printInt(minTp);
		Printer.printInt(executed);
		System.out.println("\n_______________");
	}
	public static void main(String[] args) {
		int[] minTp = {10, 5, 1, 10, 15, 15, 15};
		int[] executed = new int[minTp.length];
		int cap = 24;
		RRB x = new RRB();		
		x.rrb(cap, minTp, executed, minTp.length);
		System.out.println("\n****************");
		Printer.printInt("final minTp: ", minTp);
		Printer.printInt("final exec: ", executed);
	}
}
