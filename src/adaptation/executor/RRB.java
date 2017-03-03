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
		int average = cap / fNum;
		////System.out.println("before: rest: " + rest + " - fNum: " + fNum);
		for (int i = 0; i < minTp.length && rest > 0; i++) {
			int sent = 0;
			// when cap < fNum, average will be 0, but there are still data to be sent
			if (average == 0 && minTp[i] > 0) {
				sent = 1;
			} else {
				int min = minTp[i];
				if (min > average) {
					sent = average;
				} else {
					sent = min;
				}
			}
			executed[i] += sent;
			minTp[i] -= sent;
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
