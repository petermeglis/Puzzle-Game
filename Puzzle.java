/**
 * The purpose of this class is to create a puzzle game
 * The class consists of a board an an Array for used pieces
 * The class consists of basic functions, like adding and removing pieces
 * It also consists of the games rules and extras like a solver
 * @author 	Stelios and Sid 
 */
import java.util.ArrayList;
import java.util.List;

public class Puzzle {
	//The Board stores the arrangement of the Pieces for the puzzle
	private Board board;
	//The ArrayList holds the unused pieces in that are needed to solve the puzzle.
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	

	public Puzzle(int rows, int cols, List<Piece> pieceList) {
		board = new Board(rows, cols);
		if (pieceList instanceof ArrayList) {
			for (Piece p : pieceList) {
				pieces.add(p);
			}

		}
	}

	// Creates a square puzzle with the given list of pieces
	public Puzzle(int size, List<Piece> pieces) {
		this(size, size, pieces);
	}

	// Determines whether the puzzle has been completed
	public boolean isSolved() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				if (board.getPiece(i, j) == null || !doesFit(board.getPiece(i, j), i, j)) 
					return false;
			}
		}
		return true;
	}
	//Determines whether a piece will fit at the specified
		//location
	public boolean doesFit(Piece piece, int row, int col) {
		boolean a = true, b = true, c = true, d = true;
		if (!board.isValid(row, col)) {
			
			return false;
		} else {
			if (board.isOccupied(row - 1, col)) {
				a = (piece.getSide(Direction.TOP).getValue() == (board.getPiece(row - 1, col).getSide(Direction.BOTTOM).getValue() * -1));
				
			}
			if (board.isOccupied(row + 1, col)) {
				b = (piece.getSide(Direction.BOTTOM)
						.getValue() == (board.getPiece(row + 1, col).getSide(Direction.TOP).getValue() * -1));
			
			}
			if (board.isOccupied(row, col - 1)) {
				c = (piece.getSide(Direction.LEFT)
						.getValue() == (board.getPiece(row, col - 1).getSide(Direction.RIGHT).getValue() * -1));
			
			}
			if (board.isOccupied(row, col + 1)) {
				d = (piece.getSide(Direction.RIGHT)
						.getValue() == (board.getPiece(row, col + 1).getSide(Direction.LEFT).getValue() * -1));
			
			}
			
			return a && b && c && d;
		}
	}
	//Clears the board and puts all pieces back into the piece list
	public void reset() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				if (board.getPiece(i, j) != null) {
					pieces.add(board.removePiece(i, j));
				}

			}
		}

	}
	//Clears the board and puts all pieces back into the piece list
	public Piece removePiece(int row, int col) {
		Piece p = board.removePiece(row, col);
		if(p != null)
			pieces.add(p);
		return p;
	}

	// returns a List of any pieces not already on the board
	public List<Piece> getUnused() {
		return pieces;
	}

	// returns the number of rows
	public int getRows() {
		return board.getRows();
	}

	// returns the number of columns
	public int getCols() {
		return board.getCols();
	}
	//returns the Piece at the specified location
	public Piece getPiece(int row, int col) {
		return board.getPiece(row, col);
	}
	//replaces a Piece at the given location and returns the old Piece in the unused ArrayList
	public void setPiece(Piece piece, int row, int col) {
		board.setPiece(piece, row, col);
		pieces.remove(piece);
	}

	
	//Fills the puzzle with the correct solution

	public void solve() {
		reset();
		solve(0, 0 ,0);
	}
	// private overloaded method that takes in an index, row, and col 
		//so the method can traverse through the board
	private void solve(int index, int row, int col) {
		if(pieces.size() <= 0) return;
		while(index < pieces.size()){	
			int j = 0;
			while(j < 4){
				if(doesFit(pieces.get(index),row,col)){
					int newRow = row, newCol = col;
					setPiece(pieces.get(index), row, col);
					if(!board.isValid(row, col + 1)) {	
						newRow  = row + 1;
						newCol = 0;
					}
					else {	newCol = col + 1;}
					solve(0,newRow,newCol);
					if(isSolved())
						break;
					removePiece(row,col);
				}
				pieces.get(index).rotateClockwise();
				j++;
			}
			index++;
		}
	}

	
	// toString to print out a puzzle object. 
	// both the board and ArrayList of unused pieces get printed
	public String toString() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				if (board.getPiece(i, j) != null) {
					System.out.print(1 + " ");
				} else {
					System.out.print(0 + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
		// System.out.println(pieces);
		return "";
	}

	public static void main(String[] args) {
		ArrayList<Piece> x = new ArrayList<Piece>();
		

		/*
		 * These tests represent different boards that can exist to test the multiple methods. The different tests include puzzles that have the same number of
		 * pieces, puzzles of different sizes, and puzzles with a given solution that require rotations to solve.
		 * The last test is the puzzle that was given to us in the project sheet. There are also puzzles that have no solution to test what happens.
		 */
		// y.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// //y.add(new Piece(Side.CLUB_OUT, Side.CLUB_OUT,Side.CLUB_IN,
		// Side.CLUB_IN));
		// y.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// y.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// Piece fitPiece = new Piece(Side.CLUB_OUT, Side.CLUB_OUT,Side.CLUB_IN,
		// Side.CLUB_IN);
		// Puzzle jigsaw = new Puzzle(2,2,y);
		// jigsaw.setPiece(y.get(0), 0, 0);
		// jigsaw.setPiece(y.get(1), 1, 0);
		// jigsaw.setPiece(y.get(2), 1, 1);
		// System.out.println(jigsaw.doesFit(fitPiece,0,1));
		// System.out.println(fitPiece);
		// fitPiece.rotateClockwise();
		// fitPiece.rotateClockwise();
		// System.out.println(fitPiece);
		// System.out.println(jigsaw.doesFit(fitPiece,0,1));

		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.HEART_OUT, Side.HEART_IN,
		// Side.HEART_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.HEART_OUT, Side.HEART_IN,
		// Side.HEART_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.HEART_OUT, Side.HEART_IN,
		// Side.HEART_OUT));

		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		// Side.CLUB_OUT));
		// x.add(new Piece(Side.CLUB_OUT, Side.CLUB_OUT,Side.CLUB_IN,
		// Side.CLUB_IN));

		//
		// Piece p1 = new Piece(Side.HEART_IN,Side.SPADE_OUT,Side.SPADE_OUT,Side.HEART_OUT);
		//				p1.rotateCounterClockwise();
		//				Piece p2 = new
		//						Piece(Side.CLUB_OUT,Side.HEART_OUT,Side.DIAMOND_OUT,Side.DIAMOND_OUT);
		//				p2.rotateClockwise();
		//				p2.rotateClockwise();
		//				Piece p3 = new
		//						Piece(Side.SPADE_IN,Side.CLUB_OUT,Side.DIAMOND_IN,Side.DIAMOND_OUT);
		//				p3.rotateClockwise();
		//				p3.rotateClockwise();
		//				Piece p4 = new
		//						Piece(Side.CLUB_IN,Side.CLUB_IN,Side.SPADE_OUT,Side.SPADE_OUT);
		//				Piece p5 = new
		//						Piece(Side.SPADE_OUT,Side.HEART_OUT,Side.DIAMOND_OUT,Side.DIAMOND_IN);
		//				Piece p6 = new
		//						Piece(Side.DIAMOND_IN,Side.SPADE_OUT,Side.SPADE_IN,Side.SPADE_IN);
		//				Piece p9 = new
		//						Piece(Side.SPADE_OUT,Side.HEART_IN,Side.HEART_IN,Side.DIAMOND_IN);
		//				Piece p7 = new
		//						Piece(Side.DIAMOND_OUT,Side.CLUB_IN,Side.SPADE_OUT,Side.CLUB_OUT);
		//				p7.rotateCounterClockwise();
		//		
		//				Piece p8 = new
		//							Piece(Side.SPADE_IN,Side.DIAMOND_OUT,Side.CLUB_OUT,Side.CLUB_OUT);

		x.add(new Piece(Side.CLUB_OUT, Side.HEART_OUT, Side.DIAMOND_IN, Side.CLUB_IN));
		x.add(new Piece(Side.SPADE_OUT, Side.DIAMOND_OUT, Side.SPADE_IN, Side.HEART_IN));
		x.add(new Piece(Side.HEART_OUT, Side.SPADE_OUT, Side.SPADE_IN, Side.CLUB_IN));
		x.add(new Piece(Side.HEART_OUT, Side.DIAMOND_OUT, Side.CLUB_IN, Side.CLUB_IN));
		x.add(new Piece(Side.SPADE_OUT, Side.SPADE_OUT, Side.HEART_IN, Side.CLUB_IN));
		x.add(new Piece(Side.HEART_OUT, Side.DIAMOND_OUT, Side.DIAMOND_IN, Side.HEART_IN));
		x.add(new Piece(Side.SPADE_OUT, Side.DIAMOND_OUT, Side.HEART_IN, Side.DIAMOND_IN));
		x.add(new Piece(Side.CLUB_OUT, Side.HEART_OUT, Side.SPADE_IN, Side.HEART_IN));
		x.add(new Piece(Side.DIAMOND_OUT, Side.CLUB_OUT, Side.CLUB_IN, Side.DIAMOND_IN));

		//				x.add(p1);
		//				x.add(p2);
		//				x.add(p4);
		//				x.add(p8);
		//				x.add(p9);
		//				x.add(p6);
		//				x.add(p5);
		//				x.add(p3);
		//				x.add(p7);

		//				 x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		//				 Side.CLUB_OUT));
		//				 x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		//				 Side.CLUB_OUT));
		//				 x.add(new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		//				 Side.CLUB_OUT));
		//				Piece piece = new Piece(Side.CLUB_IN, Side.CLUB_IN,Side.CLUB_OUT,
		//				 Side.CLUB_OUT);

		// p4.rotateClockwise();
		Puzzle p = new Puzzle(3, 3, x);
		p.solve();
		System.out.println(p);

		//		 p.setPiece(x.get(0), 0, 0);
		//		 p.setPiece(x.get(1), 0, 1);
		//		 p.setPiece(x.get(2), 1, 0);
		// p.setPiece(x.get(3), 1, 1);
		//		 System.out.println(p.doesFit(piece,1,1));
		// System.out.println(p.doesFit(x.get(0),0,0));

	}
}
