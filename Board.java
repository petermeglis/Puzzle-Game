/**
 * The purpose of this class is to create a board object for a game to be played on
 * The class includes basic functions like adding, removing, and getting piece
 * The object is constructed by a row and col, or a size variable and is then represented as a 2D Array
 *  
 * @author 	Stelios and Sid
 *
 */
public class Board {

	//This Piece Array holds the arrangement of the pieces on the board
	private Piece[][] piece;
	//number of rows that board has
	private int numRows;
	//number of columns the board has
	private int numCols;

	//Constructs a Board based on dimensions
	public Board(int numRows, int numCols) {
		piece = new Piece[numRows][numCols];
		this.numRows = numRows;
		this.numCols = numCols;
	}

	//Constructs a square Board based on a side length
	public Board(int size) {
		this(size, size);
	}

	//Returns the piece at the specified location, or null
	//if empty
	public Piece getPiece(int row, int col) {
		if(isValid(row, col))
			return piece[row][col];
		return null;
	}
	//Determines whether a location exists in the board
	public boolean isValid(int row, int col) {
		return ((row > -1 && row < piece.length) && (col > -1 && col < piece[0].length));
	}

	//Places a piece at a specified location, returning the
	//piece that is replaced (or null if no piece)
	public Piece setPiece(Piece piece, int row, int col) {
		Piece temp = null; 
		if(isValid(row, col)){
			temp = this.piece[row][col];
			this.piece[row][col] = piece;
		}
		return temp;

	}

	//Determines whether a piece is located at the 
		//specified location
	public boolean isOccupied(int row, int col) {
		if(isValid(row, col)) {
			if(piece[row][col] != null)
				return true;
		}
		return false;
	}

	//Removes all pieces from the board
	public void clear() {
		for(int i = 0; i < piece.length; i++) {
			for(int j = 0; j < piece[i].length; j++) {
				piece[i][j] = null;
			}

		}
	}


	//Removes and returns the piece at the specified loc
	public Piece removePiece(int row, int col) {
		return setPiece(null, row, col);
	}
	
	/**
	 * @return the numRows
	 */
	public int getRows() {
		return numRows;
	}

	/**
	 * @return the numCols
	 */
	public int getCols() {
		return numCols;
	}
	// toString to print out the board object. A piece on the board is represented as a 1
	// An empty space is represented as a 0
	public String toString() {
		for(int i = 0; i < piece.length; i++) {
			for(int j = 0; j < piece[i].length; j++) {
				if(piece[i][j] != null) {
					System.out.print(1 + " ");
				}
				else {
					System.out.print(0 + " ");
				}
			}
			System.out.println();
		}
//		for(Piece[] p: piece){
//			for(Piece p2 : p){
//				System.out.println(p);
//			}
//		}
		return "";
	}


	/*
	 * Tests methods and constructors of this class.
	 */
	public static void main(String[] args) {
		Piece a = new Piece(Side.CLUB_IN, Side.CLUB_OUT, Side.HEART_IN, Side.HEART_OUT);
		Board b = new Board(4);
		b.setPiece(a, 3, 2);
		b.setPiece(a, 2, 2);
		b.setPiece(a, 1, 2);
		System.out.println(b.getPiece(2,2));
	
		



	}

	

}



