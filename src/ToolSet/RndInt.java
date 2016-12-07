package ToolSet;

import java.util.Random;

public class RndInt {
	/**
	 * 
	 * @param min
	 * @param max
	 * @return random int between min and max (including border values)
	 */
	public static int get(int min, int max){
		if(min>=max){
			return min;
		}
		return new Random().nextInt(max-min+1)+min;
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @return Gaussian distributed integer with mean (min+max)/2 and deviation (max-min)/3
	 */
	public static int getGauss(int min, int max){
		return getGauss(min, max, (max+min)/2, (max-min)/3);
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @param mean
	 * @param deviation
	 * @return Gaussian distributed random number between min and max with mean value and deviation
	 */
	public static int getGauss(int min, int max, int mean, int deviation){
		int r=Integer.MIN_VALUE;
		//
		if(max<=min){
			System.out.println("Error: gaussian random not applicable for min>max");
			return min;
		}
		//repeat random if result is of range [min,max] to cut without changing the distribution
		int i=1000;
		while(r>max||r<min||i<=0){
			r= (int)(new Random().nextGaussian()*deviation)+ mean;
			i--;
		}

		if(i<2){
			System.err.println("Random Int getGauss: did not succeed for 1000 runs to return a proper result between (min, max):("+min+", "+max+"). Returned "+r);
		}
		return r;
	}
	
}
