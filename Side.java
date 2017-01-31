/*
 * $telio$ Papoutsaki$, $iddharth $alunkhe
 * This enum represents the different sides of the puzzle as a numerical value. The sides that stick out are represented with positive numbers.
 * The sides that cut in are represented with negative numbers. 
 * The absolute value of the numbers range from 1 to 4 to represent the 4 different side types(Spade,Diamond,Heart,Spade).
 * 
 */
public enum Side {
	CLUB_IN(1) ,
	CLUB_OUT(-1),
	SPADE_IN(2),
	SPADE_OUT(-2),
	HEART_IN(3) ,
	HEART_OUT(-3) ,
	DIAMOND_IN(4),
	DIAMOND_OUT(-4);
	private int side;
/*
 * This constructor sets the value of an enum to the given value
 * parameters: int value that you want to set the enum to
 */
	 Side(int value){
		this.side = value;
	}
	 /*
	  * returns the integer representation given to the side type.
	  * parameters: none
	  * returns: integer value of the side type you are trying to find.
	  */
	public int getValue(){
		return side;
	}
}
