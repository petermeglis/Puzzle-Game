

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JComponent;

/**
 * BoardComponent.java
 * 
 * A BoardComponent is a visual representation of a Puzzle
 * object. It can hold PieceComponents, and it displays them
 * when they are placed onto the BoardComponent. A BoardComponent
 * can also be solved, where all the PieceComponents are 
 * automatically placed in a solved state.
 * 
 * The HolderPanel is needed in the constructor to access 
 * the HolderPanel when removing PieceComponents from the 
 * BoardComponent.
 * 
 * @author Ajay Suresh and Peter Meglis
 * 6 May 2016
 */
public class BoardComponent extends JComponent {

	private Puzzle puzzle;
	private HolderPanel holderPanel;
	private PieceComponent[][] pieces = new PieceComponent[3][3];


	/*
	 * Constructs a BoardComponent object based on a Puzzle object,
	 * and the HolderPanel from Display.java. 
	 */
	public BoardComponent(Puzzle puzzle, HolderPanel holderPanel){
		this.puzzle = puzzle;
		this.holderPanel = holderPanel;
	}

	/*
	 * Paints the BoardComponent from the given Graphics object. The 
	 * BoardComponent looks like a 3x3 grid.
	 */
	public void paintComponent(Graphics g){
		int width = this.getWidth();
		int height = this.getHeight();

		int leftX = width / 3;
		int rightX = 2 * width / 3;
		int topY = height / 3;
		int bottomY = 2 * height / 3;

		Graphics2D g2 = (Graphics2D) g; 

		g2.drawRect(0,0,width-1, height-1);

		// drawing horizontal lines
		g2.drawLine(0,topY, width,topY);
		g2.drawLine(0,bottomY,width,bottomY);

		// drawing vertical lines
		g2.drawLine(leftX,0,leftX,height);
		g2.drawLine(rightX,0,rightX,height);
	}

	/*
	 * Checks if a PieceComponent can fit onto the BoardComponent at 
	 * a certain row and column.
	 * 
	 * Parameters:
	 * 		PieceComponent piece = the PieceComponent to check if it can fit
	 * 		int row = the row number of the space checked
	 * 		int col = the column number of the space checked
	 */
	public boolean doesFit(PieceComponent pieceComp, int row, int col) {
		return ((row > -1 && col > -1) 
				&& puzzle.doesFit(pieceComp.getPiece(), row, col) 
				&& pieces[row][col] == null);
	}

	/*
	 * Sets a PieceComponent onto the BoardComponent at a given row and column, 
	 * and also sets its Piece onto the Puzzle object.
	 * 
	 * Parameters:
	 * 		PieceComponent pieceComp = the PieceComponent to set
	 * 		int row = the row number to set the PieceComponent
	 * 		int col = the column number to set the PieceComponent
	 * 
	 * Returns:
	 * 		PieceComponent; the PieceComponent getting placed
	 */
	public PieceComponent setPieceComp(PieceComponent pieceComp, int row, int col) {
		puzzle.setPiece(pieceComp.getPiece(), row, col);
		pieces[row][col] = pieceComp;
		return pieceComp;
	}

	/*
	 * Gets the PieceComponent at a given row and column.
	 * 
	 * Parameters:
	 * 		int row = the row number
	 * 		int col = the col number
	 * 
	 * Returns:
	 * 		PieceComponent; the PieceComponent at that location
	 */
	public PieceComponent getPieceComp(int row, int col) {
		return pieces[row][col];
	}

	/*
	 * Removes a PieceComponent from the BoardComponent at 
	 * the given row and column.
	 * 
	 * Parameters:
	 * 		int row = the row number
	 * 		int col = the column number
	 * 
	 * Returns:
	 * 		PieceComponent; the PieceComponent that gets removed
	 */
	public PieceComponent removePieceComp(int row, int col) {
		puzzle.removePiece(row, col);
		PieceComponent tempPiece = pieces[row][col];
		pieces[row][col] = null;
		return tempPiece;
	}

	/*
	 * Resets the Puzzle object, and takes all the PieceComponents 
	 * from the BoardComponent, and simply adds them to the HolderPanel.
	 */
	public void reset() {
		puzzle.reset();
		for (int row = 0; row < pieces.length; row++) {
			for (int col = 0; col < pieces[0].length; col++) {
				PieceComponent pieceComp = getPieceComp(row,col);
				if (pieceComp != null) {
					Container c = pieceComp.getParent();
					c.remove(pieces[row][col]);
					pieceComp.scaleImage(121);
					removePieceComp(row,col);
					holderPanel.addToPanel(pieceComp);
					c.repaint();
				}
			}
		}
	}

	/*
	 * Checks if the Puzzle has been solved.
	 * 
	 * Returns:
	 * 		boolean; true if it is solved, false if it isn't solved yet
	 */
	public boolean isSolved() {
		return puzzle.isSolved();
	}
	
	/*
	 * Solves the puzzle, putting all the correct PieceComponents onto
	 * the BoardComponent.
	 */
	public void solve() {
		List<PieceComponent> pieceCompList = Display.pieceComps;
		puzzle.solve();

		for (int row = 0; row < pieces.length; row++) {
			for (int col = 0; col < pieces[0].length; col++) {
				boolean fits = false;	
				for (int i = 0; !fits && i < pieceCompList.size(); i++) {
					PieceComponent pieceComp = pieceCompList.get(i);
					if (areEqual(puzzle.getPiece(row, col),pieceComp)){
						setPieceComp(pieceComp, row, col);
						holderPanel.removeFromPanel(pieceComp);
						fits = true;
					}
				}
			}
		}
	}
	
	/*
	 * Checks to see if a PieceComponent's Piece is the same as another Piece.
	 * 
	 * Parameters:
	 * 		Piece piece = the Piece object to check
	 * 		PieceComponent pieceComp = the PieceComponent that holds 
	 * 			a Piece object to check
	 */
	public static boolean areEqual(Piece piece, PieceComponent pieceComp) {
		boolean areEqual = false;
		if (pieceComp.getPiece().equals(piece)) {
			areEqual = true;
			for (int i = pieceComp.getOrientation(); i < piece.getOrientation(); i++) {
				pieceComp.rotateGraphicsClockwise();
			}
		}
		return areEqual;
	}

	
	/*
	 * Tests the solve method.
	 */
	public static void main(String[] args) {
//		
//		ArrayList<PieceComponent> pieces = new ArrayList<PieceComponent>();
//
//		pieces.add(new PieceComponent("resources\\puzzle pieces\\piece_1.png", new Piece(Side.CLUBOUT, Side.HEARTOUT, Side.DIAMONDIN, Side.CLUBIN)));
//		pieces.add(new PieceComponent("resources\\puzzle pieces\\piece_2.png", new Piece(Side.SPADEOUT, Side.DIAMONDOUT, Side.SPADEIN, Side.HEARTIN)));
//		pieces.add(new PieceComponent("resources\\puzzle pieces\\piece_3.png", new Piece(Side.HEARTOUT, Side.SPADEOUT, Side.HEARTIN, Side.CLUBIN)));
//		pieces.add(new PieceComponent("resources\\puzzle pieces\\piece_4.png", new Piece(Side.HEARTOUT, Side.DIAMONDOUT, Side.CLUBIN, Side.CLUBIN)));
//		pieces.add(new PieceComponent("resources\\puzzle pieces\\piece_5.png", new Piece(Side.SPADEOUT, Side.SPADEOUT, Side.HEARTIN, Side.CLUBIN)));
//		pieces.add(new PieceComponent("resources\\puzzle pieces\\piece_6.png", new Piece(Side.HEARTOUT, Side.DIAMONDOUT, Side.DIAMONDIN, Side.HEARTIN)));
//		pieces.add(new PieceComponent("resources\\puzzle pieces\\piece_7.png", new Piece(Side.SPADEOUT, Side.DIAMONDOUT, Side.HEARTIN, Side.DIAMONDIN)));
//		pieces.add(new PieceComponent("resources\\puzzle pieces\\piece_8.png", new Piece(Side.CLUBOUT, Side.HEARTOUT, Side.SPADEIN, Side.HEARTIN)));
//		pieces.add(new PieceComponent("resources\\puzzle pieces\\piece_9.png", new Piece(Side.DIAMONDOUT, Side.CLUBOUT, Side.CLUBIN, Side.DIAMONDIN)));
//
//		
//		Piece piece = new Piece(Side.CLUBOUT, Side.HEARTOUT, Side.DIAMONDIN, Side.CLUBIN);
//		System.out.println(areEqual(piece,pieces.get(1)));
//		ArrayList<Piece> x = new ArrayList<Piece>();
//		x.add(new Piece(Side.CLUB_OUT, Side.HEART_OUT, Side.DIAMOND_IN, Side.CLUB_IN));
//		x.add(new Piece(Side.SPADE_OUT, Side.DIAMOND_OUT, Side.SPADE_IN, Side.HEART_IN));
//		x.add(new Piece(Side.HEART_OUT, Side.SPADE_OUT, Side.SPADE_IN, Side.CLUB_IN));
//		x.add(new Piece(Side.HEART_OUT, Side.DIAMOND_OUT, Side.CLUB_IN, Side.CLUB_IN));
//		x.add(new Piece(Side.SPADE_OUT, Side.SPADE_OUT, Side.HEART_IN, Side.CLUB_IN));
//		x.add(new Piece(Side.HEART_OUT, Side.DIAMOND_OUT, Side.DIAMOND_IN, Side.HEART_IN));
//		x.add(new Piece(Side.SPADE_OUT, Side.DIAMOND_OUT, Side.HEART_IN, Side.DIAMOND_IN));
//		x.add(new Piece(Side.CLUB_OUT, Side.HEART_OUT, Side.SPADE_IN, Side.HEART_IN));
//		x.add(new Piece(Side.DIAMOND_OUT, Side.CLUB_OUT, Side.CLUB_IN, Side.DIAMOND_IN));
//		Puzzle p = new Puzzle(3, 3, x);
//		for(int row =0; row< p.getRows();row++){
//			for(int col = 0; col < p.getCols(); col++){
//				System.out.println(p.getPiece(row, col));
//			}
//		}
//		p.solve();
//		for(int row =0; row< p.getRows();row++){
//			for(int col = 0; col < p.getCols(); col++){
//				System.out.println(p.getPiece(row, col));
//			}
//		}
//		System.out.println(p);
	}







}
