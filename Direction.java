
/*
 * this enum represents the orientation of the puzzle piece in terms of integers. This class is used to access the specific side direction when 
 * looking at a puzzle piece. For example one would look at what Side type exists at a specific direction on the piece. This is useful when the 
 * piece has been rotated multiple times; it keeps track of which side type is in which location.
 */
public enum Direction {
TOP(0), BOTTOM(2), LEFT(3), RIGHT(1);
	private int orientation;
	/*
	 * This constructor sets the value of an enum to the given value
	 * parameters: int value that you want to set the enum to
	 */
	Direction(int value){
		this.orientation = value;
	}
	/*
	  * returns the integer representation given to the direction.
	  * parameters: none
	  * returns: integer value of the direction you are trying to find.
	  */
	public int getValue(){
		return orientation;
	}
}
