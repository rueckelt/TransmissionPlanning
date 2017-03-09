package adaptation.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Hasher {
	private int[] arr;
	
	public Hasher(int len) {
		arr = new int[len];
	}
	
	public Hasher(int[] arr) {
		this.arr = arr.clone();
	}
	
	public static HashMap<Integer, List<Integer>> listToMap (int[] arr) {
		HashMap<Integer, List<Integer>> hm = new HashMap<Integer, List<Integer>>();
		for (int i = 0; i < arr.length; i++) {
			if (hm.containsKey(arr[i])) {
				hm.get(arr[i]).add(i);
			} else {
				List<Integer> list = new LinkedList<Integer>();
				list.add(i);
				hm.put(arr[i], list);
			}
		}
		return hm;
	}
	
//	public static int[] mapToList (HashMap<Integer, List<Integer>> map) {
//	    Iterator it = map.entrySet().iterator();
//	    int sumNum = 0;
//	    while (it.hasNext()) {
//	        HashMap.Entry<Integer, List<Integer>> pair = (HashMap.Entry)it.next();
//	        List<Integer> list = (List<Integer>) pair.getValue();
//	        sumNum += list.size();
//	    }
//	    // ////////System.out.println(sumNum);
//	    int[] result = new int[sumNum];
//	    
//	    Iterator it2 = map.entrySet().iterator();
//	    while (it2.hasNext()) {
//	        HashMap.Entry pair = (HashMap.Entry)it2.next();
//	        List<Integer> list = (List<Integer>) pair.getValue();
//	        for (int i = 0; i < list.size(); i++) {
//	        	result[list.get(i)] = (int) pair.getKey();
//	        }
//	    }
//	    
//	    //Printer.printInt(result);
//	    return result;   
//	}
	
	public static void main(String[] args) {
		int[] comb = {1, 2, 2, 3, 3};
		////////System.out.println(Hasher.listToMap(comb).toString());
		HashMap<Integer, List<Integer>> map = Hasher.listToMap(comb);
		////////System.out.println(map.toString());
		List<Integer> net2 = map.get(2);
		Random rand = new Random();
	    int randomFlowIndex = rand.nextInt(net2.size());
	    int randomFlow = net2.get(randomFlowIndex);
	    ////////System.out.println("randomFlow: " + randomFlow);
	    int randomNet = rand.nextInt(3 - 1) + 1;
	    ////////System.out.println("randomNet: " + randomNet);
	    comb[randomFlow] = randomNet;
	    //Printer.printInt(comb);

	    
	    
	    


		//int[] x = Hasher.mapToList(map);

	}

}
