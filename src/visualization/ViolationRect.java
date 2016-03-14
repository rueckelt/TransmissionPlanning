package visualization;

import java.awt.Rectangle;
import java.util.Vector;


/**
 * 
 * @author DennisHanslik@web.de
 * 
 * For the tooltips we need to know where to show the violations. This class stores all rectangles (screen coordinates)
 * where we found any violation. So we only have to check if actual mouse position is in one of the rectangles.
 *
 */
public class ViolationRect {
	Vector <Rectangle> rect = new Vector <Rectangle>();
	Vector <String> violation = new Vector <String>();

	public ViolationRect() {
		
	}

	/**
	 * Store new violation.
	 * 
	 * @param r		Rectangle with screen coordinates of the chunk which caused a violation.
	 * @param s		What to print in a tooltip
	 */
	public void add (Rectangle r, String s) {
		rect.add(r);
		violation.add(s);
	}
	
	/**
	 * Get one of the stored rectangles 
	 * 
	 * @param pos	Position of target rectangle in our list.
	 * @return		Rectangle at target position or an empty rectangle, if pos is out of bounds.
	 */
	public Rectangle getRect(int pos) {
		if (pos < rect.size() && pos >= 0)
			return rect.get(pos);
		
		return new Rectangle();
	}
	
	/**
	 * Get the text of the violation.
	 * 
	 * @param pos	Position of target violation in our list.
	 * @return		Violation-String at target position or an empty string, if pos is out of bounds.
	 */
	public String getViolation(int pos) {
		if (pos < violation.size() && pos >= 0)
			return violation.get(pos);
		
		return "";
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
