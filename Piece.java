/**
 * The purpose of this class is to construct a piece object to be placed on a board
 * The piece is given values for each one of its 4 sides
 * The class includes basic functions like rotating the piece and getters for its sides and orientation 
 * @author Stelios and Sid
 *
 */
public class Piece {

	//Array of sides maintains the orientation of the Side types with repect to each other and holds the four sides that makes the puzzle Piece
	private Side[] sides = new Side[4];
	//the orientation stores how many 90 degree turns from the assigned value the Piece is at.
	private int orientation;
/*
 * This constructor creates a Piece by assigning the four sides of the piece different Side types from the enum list.
 * Parameters: Side top, Side right, Side bottom, Side left are the four side types which will be assigned to the direction as given by their name.
 * Returns: none
 */
	public Piece(Side top, Side right, Side bottom, Side left) {
		sides[0] = top;
		sides[1] = right;
		sides[2] = bottom;
		sides[3] = left;

	}
	//rotates piece 90 degrees clockwise
	public void rotateClockwise() {
		Side temp = sides[sides.length - 1];

		for(int i = sides.length - 1; i > 0; i--) {
			sides[i] = sides[i - 1];
		}
		sides[0] = temp;
		orientation = (orientation + 1) % 4;
	}
	//rotates piece 90 degrees counter-clockwise
	public void rotateCounterClockwise(){		
		Side temp = sides[0];
		for(int i = 0; i < sides.length - 1 ; i++ ){
			sides[i] = sides[i + 1];
		}
		sides[sides.length - 1] = temp;
		orientation = (orientation + 3) % 4;
	}
	//returns the chosen side
	public Side getSide(Direction direction) {
		return sides[direction.getValue()];
	}
	// Returns number of 90 degree clockwise turns from original state
	public int getOrientation() {
		return orientation;
	}
	// toString to print out a piece object. The method prints out each side and its orientation
	public String toString () {
		for(Side b: sides) 
			System.out.println(b + " ");
			System.out.println("Orientation = " + orientation);
		
		return "";
	}

	/*
	 * Tests methods and constructors of this class.
	 */
	public static void main(String[] args) {
		
		Piece a = new Piece(Side.CLUB_IN, Side.CLUB_OUT, Side.HEART_IN, Side.HEART_OUT);
		System.out.println(a);
		System.out.println("The top is" + a.getSide(Direction.TOP));
		System.out.println("The bottom is" + a.getSide(Direction.BOTTOM));
		System.out.println("The right is" + a.getSide(Direction.RIGHT));
		System.out.println("The left is" + a.getSide(Direction.LEFT));
//		a.rotateCounterClockwise();
//		a.rotateCounterClockwise();
		
		System.out.println(a);
		System.out.println("The top is" + a.getSide(Direction.TOP));
		System.out.println("The bottom is" + a.getSide(Direction.BOTTOM));
		System.out.println("The right is" + a.getSide(Direction.RIGHT));
		System.out.println("The left is" + a.getSide(Direction.LEFT));

	}

}
