import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * DragListener.java
 * 
 * This DragListener is used to move PieceComponents to and 
 * from the HolderPanel and BoardComponent.
 * 
 * If the solution is displayed, it prevents the user from 
 * doing anything besides hit the reset button.
 * 
 * @author Peter Meglis and Ajay Suresh
 * 6 May 2016
 */
public class DragListener extends MouseInputAdapter {

	private PieceComponent piece;
	private HolderPanel holderPanel;
	private JLayeredPane layeredPane;
	private JPanel puzzlePanel;
	private BoardComponent boardComponent;
	private TimerPanel timerPanel;
	
	private int boardStartX = 407;
	private int boardStartY = 74;
	
	private final int xGap = 150;
	private final int yGap = 150;
	
	private final int placeShiftX = 307;
	private final int placeShiftY = 24;

	private int clickShiftX;
	private int clickShiftY;

	private int scale = 252;

	private boolean isPressed = false;
	private boolean pickedUpFromBoard = false;
	
	private boolean isSolutionDisplayed = false;

	/*
	 * A DragListener is constructed from the JLayeredPane,
	 * the JPanel (for the actual puzzle), the BoardComponent 
	 * (that the PieceComponents go on), and a TimerPanel.
	 */
	public DragListener(JLayeredPane layeredPane,
			HolderPanel holderPanel, 
			JPanel puzzlePanel,
			BoardComponent boardComponent,
			TimerPanel timerPanel) {
		this.layeredPane = layeredPane;
		this.holderPanel = holderPanel;
		this.puzzlePanel = puzzlePanel;
		this.boardComponent = boardComponent;
		this.timerPanel = timerPanel;
	}

	/*
	 * When the mouse is pressed, if it is a right click, it
	 * will rotate the PieceComponent under it. If it is a left
	 * click, it will pick up the PieceComponent and put it on 
	 * the Drag Layer to move around. Does nothing if the solution
	 * has been displayed.
	 */
	public void mousePressed(MouseEvent e)
	{
		if (isSolutionDisplayed) return;
		if (SwingUtilities.isRightMouseButton(e)) {
			if (piece != null) {
				piece.rotateClockwise();
				return;
			}
			Component c = e.getComponent();
			if (c instanceof HolderPanel) {
				c =  c.getComponentAt(e.getX(), e.getY());
				if (c instanceof PieceComponent) {
					piece = (PieceComponent)c;
					piece.rotateClockwise();
					pickedUpFromBoard = false;
					piece = null;
				}
			}
			return;
		} 
		if (!isPressed) {
			isPressed = true;
			piece = null;
			
			// Gets the PieceComponent from the holder and moves to Drag Layer
			Component c =  e.getComponent();
			if (c instanceof HolderPanel) {
				c = holderPanel.getComponentAt(e.getX(),e.getY());
				if (c instanceof PieceComponent) {
					clickShiftX = -57;
					clickShiftY = -32;
					piece = (PieceComponent)c;
					
					holderPanel.removeFromPanel(piece);
					piece.setLocation(e.getX() + clickShiftX, e.getY() + clickShiftY);
					layeredPane.add(piece, JLayeredPane.DRAG_LAYER);
				}
			}
			// Gets the PieceComponent from the BoardComponent and moves to the Drag Layer
			else if (c instanceof JPanel) {
				c = boardComponent.getComponentAt(e.getX(), e.getY());
				
				Point rowCol = toRowCol(e.getPoint(),false);
				int row = rowCol.x;
				int col = rowCol.y;
				clickShiftX = 135;
				clickShiftY = -120;

				if (row > -1 && col > -1) {
					piece = boardComponent.getPieceComp(row, col);
					if (piece == null) return;
					pickedUpFromBoard = true;
					boardComponent.removePieceComp(row,col);

					puzzlePanel.remove(piece);
					puzzlePanel.validate();
					puzzlePanel.repaint();
					
					piece.setLocation(e.getX() + clickShiftX, e.getY() + clickShiftY);
					layeredPane.add(piece, JLayeredPane.DRAG_LAYER);
				}
			}
			return;
		}
	}

	/*
	 * Moves the PieceComponent around, staying within the 
	 * boundaries of the Frame. Does nothing if the solution
	 * has been displayed.
	 */
	public void mouseDragged(MouseEvent e)
	{
		if (piece == null || isSolutionDisplayed) return;

		int x = e.getX() + clickShiftX;
		int xMax = layeredPane.getWidth() - piece.getWidth();
		x = Math.min(x, xMax);
		x = Math.max(x, 0);

		int y = e.getY() + clickShiftY;
		int yMax = layeredPane.getHeight() - piece.getHeight();
		y = Math.min(y, yMax);
		y = Math.max(y, 0);

		piece.setLocation(x, y);
	}

	/*
	 * When the mouse is released, the PieceComponent is 
	 * dropped in either the BoardComponent or the HolderPanel.
	 * If it is dropped in the BoardComponent, and the 
	 * BoardComponent becomes solved, the congratulations window 
	 * from Display.java will show. Does nothing if the solution
	 * has been displayed.
	 */
	public void mouseReleased(MouseEvent e)
	{
		if (SwingUtilities.isRightMouseButton(e) || isSolutionDisplayed) {
			return;
		}
		if (isPressed) {
			isPressed = false;
			if (piece == null) return;

			piece.setVisible(false);
			layeredPane.remove(piece);
			piece.setVisible(true);
			layeredPane.repaint();
			layeredPane.revalidate();


			int xMax = layeredPane.getWidth() - piece.getWidth();
			int x = Math.min(e.getX(), xMax);
			if (pickedUpFromBoard) x += 260;
			x = Math.max(x, 0);

			int yMax = layeredPane.getHeight() - piece.getHeight();
			int y = Math.min(e.getY(), yMax);
			if (pickedUpFromBoard) y -= 23;
			y = Math.max(y, 0);

			
			toBoard(piece,toRowCol(new Point(x,y),true));
			pickedUpFromBoard = false;
			piece = null;
			if (boardComponent.isSolved()) {
				Display.solved();
			}
			
		}
	}
	
	
	/*
	 * Displays the image for a PieceComponent at a row and col.
	 * 
	 * Parameters:
	 * 		PieceComponent pieceComp = the PieceComponent to display
	 * 		Point rowCol = the row (rowCol.x) and column (rowCol.y) to
	 * 			display the PieceComponent
	 */
	public void displayImage(PieceComponent pieceComp, Point rowCol) {
		pieceComp.scaleImage(scale);
		Point xYCoords = toXY(rowCol);
		pieceComp.setLocation(xYCoords.x,xYCoords.y);
		puzzlePanel.add(pieceComp);
		puzzlePanel.setComponentZOrder(pieceComp, 0);
	}
	
	/*
	 * Updates the PieceComponent image for every spot in
	 * the BoardComponent.
	 */
	public void updateBoardComponent() {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				PieceComponent pieceComp = boardComponent.getPieceComp(row, col);
				if (pieceComp != null) {
					displayImage(pieceComp,new Point(row,col));
				}
			}
		}
		puzzlePanel.revalidate();
		puzzlePanel.repaint();
	}

	/*
	 * Scales the PieceComponent down, and adds the 
	 * PieceComponent to the HolderPanel on the left side.
	 * 
	 * Parameters:
	 * 		PieceComponent piece = the PieceComponent to move to the HolderPanel
	 */
	public void toHolder(PieceComponent pieceComp) {
		pieceComp.scaleImage(121);
		holderPanel.addToPanel(pieceComp);
	}

	/*
	 * Converts an xy point into rows (x) and colums (y) for the 
	 * BoardComponent. If the PieceComponent was taken from the BoardComponent, 
	 * then re-added, the flag isPlaceOnBoard should be true, the xy 
	 * location to place the PieceComponent is shifted, and must be corrected.
	 * 
	 * Parameters:
	 * 		Point xYPoint = the (x,y) point being converted to rows and columns
	 * 		boolean isPlaceOnBoard = true if a PieceComponent was re-added to the
	 * 			BoardComponent after just being removed from the BoardComponent. False
	 * 			if the PieceComponent was removed from the holder, and added to the
	 * 			BoardComponent.
	 * 
	 * Returns:
	 * 		Point; the point in row (x) column (y) form
	 */
	public Point toRowCol(Point xYPoint, boolean isPlaceOnBoard) {
		int boardStartX;
		int boardStartY;
		
		if (!isPlaceOnBoard) {
			boardStartX = 150;
			boardStartY = 100;
		} else {
			boardStartX = this.boardStartX;
			boardStartY = this.boardStartY;
		}
		int xPos = xYPoint.x;
		int yPos = xYPoint.y;

		if (yPos < boardStartY || yPos > boardStartY + 3*yGap) return new Point(-1,-1);
		if (xPos < boardStartX || xPos > boardStartX + 3*xGap) return new Point(-1,-1);

		if (yPos < boardStartY + yGap) {
			if (xPos < boardStartX + xGap) return new Point(0,0);
			else if (xPos < boardStartX + (2*xGap)) return new Point(0,1);
			else return new Point(0,2);
		}
		else if (yPos < boardStartY + 2*yGap) {
			if (xPos < boardStartX + xGap) return new Point(1,0);
			else if (xPos < boardStartX + (2*xGap)) return new Point(1,1);
			else return new Point(1,2);
		}
		else {
			if (xPos < boardStartX + xGap) return new Point(2,0);
			else if (xPos < boardStartX + (2*xGap)) return new Point(2,1);
			else return new Point(2,2);
		}
	}

	/*
	 * Converts a row (x) and column (y) point to xy (on the puzzlePanel) coordinates.
	 * 
	 * Parameters:
	 * 		Point rowCol = the point in row (x) column (y) form
	 * 
	 * Returns:
	 * 		Point; the point in xy form for the Puzzle Panel
	 */
	public Point toXY(Point rowCol) {	
		int row = rowCol.x;
		int col = rowCol.y;

		if (row == 0) {
			if (col == 0) return new Point(boardStartX - placeShiftX, boardStartY - placeShiftY);
			else if (col == 1) return new Point(boardStartX + xGap - placeShiftX, boardStartY - placeShiftY);
			else return new Point(boardStartX + 2*xGap - placeShiftX, boardStartY - placeShiftY);
		}
		else if (row == 1) {
			if (col == 0) return new Point(boardStartX - placeShiftX, boardStartY + yGap - placeShiftY);
			else if (col == 1) return new Point(boardStartX + xGap - placeShiftX, boardStartY + yGap - placeShiftY);
			else return new Point(boardStartX + 2*xGap - placeShiftX, boardStartY + yGap - placeShiftY);
		}
		else {
			if (col == 0) return new Point(boardStartX - placeShiftX, boardStartY + 2*yGap - placeShiftY);
			else if (col == 1) return new Point(boardStartX + xGap - placeShiftX, boardStartY + 2*yGap - placeShiftY);
			else if (col == 2) return new Point(boardStartX + 2*xGap - placeShiftX, boardStartY + 2*yGap - placeShiftY);
			return new Point(-2,-2);
		}
	}
		

	/*
	 * Tries to add a PieceComponent to the BoardComponent at a 
	 * certain row and column, if it doesn't fit, it gets stored 
	 * back in the holder. If the PieceComponent is added to the 
	 * BoardComponent and the timer is not running, the timer will
	 * disappear. If the PieceComponent is instead added to the 
	 * HolderPanel, and it becomes full, the timer will reappear.
	 */
	public void toBoard(PieceComponent piece, Point rowCol) {
		if (boardComponent.doesFit(piece, rowCol.x, rowCol.y)) {
			displayImage(piece, rowCol);
			boardComponent.setPieceComp(piece, rowCol.x, rowCol.y);
			piece = null;
			if (!timerPanel.isRunning()) {
				timerPanel.setVisible(false);
			}
		}
		else {
			toHolder(piece);
			if (holderPanel.isFull()) {
				timerPanel.updateVisible(true);
			}
		}
	}

	/*
	 * Updates the variable isSolutionDisplayed.
	 */
	public void setSolutionDisplayed(boolean isSolutionDisplayed) {
		this.isSolutionDisplayed = isSolutionDisplayed;
	}
	

}
