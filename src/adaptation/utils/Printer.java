package adaptation.utils;

public class Printer {
	public static void print(double[] d) {
		System.out.print("[");
		for (int i = 0; i < d.length; i++) {
			if (i != d.length - 1) {
				System.out.print(d[i] + ", ");
			} else {
				System.out.print(d[i]);
			}
		}
		System.out.print("]");
		////////System.out.println("");		
	}
	
	public static void printInt(int[] d) {
		System.out.print("[");
		for (int i = 0; i < d.length; i++) {
			if (i != d.length - 1) {
				System.out.print(d[i] + ", ");
			} else {
				System.out.print(d[i]);
			}
		}
		System.out.print("]");
		//System.out.println("");		
	}
	public static String printInt(int[] d, boolean str) {
		StringBuffer sb = new StringBuffer("[");
		System.out.print("[");
		for (int i = 0; i < d.length; i++) {
			if (i != d.length - 1) {
				System.out.print(d[i] + ", ");
				sb.append(d[i]);
				sb.append(',');
			} else {
				System.out.print(d[i]);
				sb.append(d[i]);
			}
		}
		System.out.print("]");
		sb.append(']');
		return sb.toString();
	//	////System.out.println("");		
	}
	public static void printInt(String msg, int[] d) {
		////System.out.println(msg);
		System.out.print("[");
		for (int i = 0; i < d.length; i++) {
			if (i != d.length - 1) {
				System.out.print(d[i] + ", ");
			} else {
				System.out.print(d[i]);
			}
		}
		System.out.print("]");
		////System.out.println("");		
	}
}
