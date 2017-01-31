import java.util.List;
import javax.swing.JPanel;

/**
 * HolderPanel.java
 * 
 * The HolderPanel is used to store the PieceComponents 
 * in an organized way. One can add and remove PieceComponents,
 * as well as sort and reset them to their initial states. Randomizing 
 * them will shuffle the order, and randomly rotate each PieceComponent.
 * 
 * @author Peter Meglis and Ajay Suresh
 * 6 May 2016
 */
public class HolderPanel extends JPanel {
	
	private static List<PieceComponent> pieceComps;
	
	/*
	 * Constructs a HolderPanel from a List of PieceComponents.
	 */
	public HolderPanel(List<PieceComponent> pieceComps) {
		HolderPanel.pieceComps = pieceComps;
	}	
	
	/*
	 * Initializes the HolderPanel by adding all of the stored PieceComponents
	 * into the HolderPanel (JPanel).
	 */
	public void initialize() {
		for (PieceComponent p : pieceComps) {
			this.add(p);
		}
	}
	
	/*
	 * Adds a PieceComponent to the HolderPanel.
	 * 
	 * Parameters:
	 * 		PieceComponent p = the PieceComponent being added to the HolderPanel
	 */
	public void addToPanel(PieceComponent p) {
		pieceComps.add(p);
		this.add(p);
		this.repaint();
		this.revalidate();
	}
	
	/*
	 * Removes a PieceComponent from the HolderPanel.
	 * 
	 * Parameters:
	 * 		PieceComponent p = the PieceComponent to remove
	 */
	public void removeFromPanel(PieceComponent p) {
		for (int i = 0; i < pieceComps.size(); i++) {
			if (pieceComps.get(i).equals(p)) {
				pieceComps.remove(i);
			}
		}
		this.remove(p);
		this.repaint();
	}
	
	/*
	 * Resets all the PieceComponents so that they all have 
	 * an orientation of 0, their initial state.
	 */
	public void reset() {
		for (PieceComponent pieceComp : HolderPanel.pieceComps) {
			for (int i = pieceComp.getOrientation(); i < 4; i++) {
				pieceComp.rotateClockwise();
			}
		}
		this.removeAll();
		this.sort();
		for (PieceComponent p : pieceComps) {
			this.add(p);
		}
		this.revalidate();
		this.repaint();
	}
	
	/*
	 * Sorts the PieceComponents in the HolderPanel based on their order number.
	 */
	public void sort() {
		for (int fixedIndex = 0; fixedIndex < pieceComps.size(); fixedIndex++){
			int smallestIndex = fixedIndex;
			
			for (int checkIndex = fixedIndex + 1; checkIndex < pieceComps.size(); checkIndex++){
				if (pieceComps.get(smallestIndex).getOrder() != fixedIndex) {
					smallestIndex = checkIndex;
				}
			}
			PieceComponent temp = pieceComps.set(fixedIndex, pieceComps.get(smallestIndex));
			pieceComps.set(smallestIndex, temp);
		}
	}
	
	/*
	 * Shuffles the order of the PieceComponents in the HolderPanel, 
	 * and randomly rotates each one.
	 */
	public void randomize() {
		this.removeAll();
		for (int i = 0; i < pieceComps.size(); i++) {
			int randInt = (int)(Math.random() * pieceComps.size()) ;
			PieceComponent temp = pieceComps.get(i);
			int randRotation = (int)(Math.random() * 4);
			for (int j = 0; j < randRotation; j++) {
				temp.rotateClockwise();
			}
			pieceComps.set(i, pieceComps.get(randInt));
			pieceComps.set(randInt, temp);
		}
		for (PieceComponent p : pieceComps) {
			this.add(p);
		}
		this.revalidate();
		this.repaint();
	}
	
	/*
	 * Checks if the HolderPanel is full of PieceComponents.
	 * 
	 * Returns:
	 * 		boolean; true - the HolderPanel has all of the PieceComponents
	 * 				 false - the HolderPanel is not full of the PieceComponents
	 */
	public boolean isFull() {
		return (pieceComps.size() == 9);
	}

}
