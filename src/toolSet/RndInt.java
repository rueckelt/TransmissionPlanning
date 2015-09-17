package toolSet;

import java.util.Random;

public class RndInt {
	/**
	 * 
	 * @param min
	 * @param max
	 * @return random int between min and max (including border values)
	 */
	public static int get(int min, int max){
		return new Random().nextInt(max-min+1)+min;
	}
	
}
