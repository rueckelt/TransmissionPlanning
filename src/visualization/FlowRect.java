package visualization;

import java.awt.Rectangle;
import java.util.Vector;

/**
 * 
 * @author DennisHanslik@web.de
 * 
 * For the tooltips, we need to know where all chunks are printed on the screen. So we store all chunks
 * (their pos, capacity and timeslot) for one flow.
 * To store multiple flows, we use a vector of FlowRect in PlotGraph. 
 *
 */
public class FlowRect {
	Vector <Rectangle> rect = new Vector <Rectangle>();		// Position of chunks on the screen
	Vector <Integer> capacity = new Vector <Integer>();		// capacity of each chunk-rectangle
	Vector <Integer> timeslot = new Vector <Integer>();		// timeslot for each chunk-rectangle
	
	
	public FlowRect() {
		
	}
	
	/**
	 * Add new chunk
	 * 
	 * @param r		Position of chunk on screen
	 * @param c		Capacity of current chunk
	 * @param t		Current timeslot
	 */
	public void add(Rectangle r, int c, int t) {
		rect.add(r);
		capacity.add(c);
		timeslot.add(t);
	}
	
	/**
	 * Get screen coordinates for chunk on target position in list
	 * 
	 * @param pos		Position of target chunk in our list 
	 * @return			Screen Coordinates of chunk at target position, or empty rectangle if pos is out of bounds.
	 */
	public Rectangle getRect(int pos) {
		if (pos < rect.size() && pos >= 0)
			return rect.get(pos);
		
		return new Rectangle();
	}
	
	/**
	 * Get capacity of target chunk.
	 * 
	 * @param pos		Position of target chunk in our list.
	 * @return			Capacity of target chunk, or 0 if pos is out of bounds.
	 */
	public int getCapacity (int pos) {
		if (pos < capacity.size() && pos >= 0)
			return capacity.get(pos);

		return 0;
	}
	
	/**
	 * Get timeslot of target chunk.
	 * 
	 * @param pos		Position of target chunk in our list.
	 * @return			Timeslot of target chunk, or 0 if pos is out of bounds.
	 */
	public int getTimeslot (int pos) {
		if (pos < timeslot.size() && pos >= 0)
			return timeslot.get(pos);

		return 0;
	}
	
	/**
	 * Get size of our list.
	 * 
	 * @return		Number of elements in list.
	 */
	public int size(){
		return rect.size();
	}
}
