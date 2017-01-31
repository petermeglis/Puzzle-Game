
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 * PieceComponent.java
 * 
 * A PieceComponent is used as a visual representation of a
 * Piece object. After the image has been read from the resources
 * file, a PieceComponent can be rotated and scaled to look
 * good on the board. A PieceComponent also has an orientation,
 * because the visual may not always line up with its Piece
 * object's orientation. 
 * 
 * Finally, each PieceComponent has an order number, used when 
 * sorting multiple PieceComponents. The order number is obtained 
 * from looking at the order of the images in the resources file.
 * For example, the first image in the file will get put with 
 * a corresponding Piece and PieceComponent, and given an order
 * number 0.
 * 
 * @author Peter Meglis and Ajay Suresh
 * 6 May 2016
 */
public class PieceComponent extends JLabel{

	private Piece piece;
	private BufferedImage buffImage;
	private Image image;
	private int currentScale = 121;
	private int orientation;
	private int order;
	
	/*
	 * Constructs a PieceComponent object based on an image file,
	 * a Piece object, and an order number.
	 * 
	 * Parameters:
	 * 		String imageFile = the image file corresponding to the given Piece object
	 * 		Piece piece = the piece object
	 * 		int order = the order number, or "index" of the PieceComponent, obtained from
	 * 			looking at the order images
	 */
	public PieceComponent(String imageFile, Piece piece, int order) {
		try {
		    this.buffImage = ImageIO.read(new File(imageFile));
		    this.image = buffImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.piece = piece;
		this.orientation = 0;
		this.order = order;
	}
	
	/*
	 * Draws the image of the PieceComponent
	 * 
	 * Parameters:
	 * 		Graphics g = the Graphics object used
	 */
	public void paint(Graphics g) {
		g.drawImage(this.image,0,0,null);
	}
	
	/*
	 * Rotates the PieceComponent and its Piece clockwise.
	 */
	public void rotateClockwise() {
		piece.rotateClockwise();
		rotateGraphicsClockwise();
	}
	
	/*
	 * Rotates the PieceComponent and its Piece counterclockwise.
	 */
	public void rotateCounterClockwise() {
		piece.rotateCounterClockwise();
		rotateGraphicsCounterClockwise();
	}
	
	/*
	 * Rotates just the PieceComponent's graphics clockwise.
	 */
	public void rotateGraphicsClockwise() {
		orientation = (orientation + 1) % 4;
		AffineTransform tx = new AffineTransform();
	    tx.rotate(Math.PI / 2, this.buffImage.getWidth() / 2, this.buffImage.getHeight() / 2);

	    AffineTransformOp op = new AffineTransformOp(tx,
	        AffineTransformOp.TYPE_BILINEAR);
	    this.buffImage = op.filter(this.buffImage, null);
	    image = Toolkit.getDefaultToolkit().createImage(buffImage.getSource());
	    scaleImage(currentScale);
	    this.repaint();
	}
	
	/*
	 * Rotates just the PieceComponent's graphics counterclockwise.
	 */
	public void rotateGraphicsCounterClockwise() {
		orientation = (orientation + 3) % 4;
		AffineTransform tx = new AffineTransform();
	    tx.rotate(-Math.PI / 2, this.buffImage.getWidth() / 2, this.buffImage.getHeight() / 2);

	    AffineTransformOp op = new AffineTransformOp(tx,
	        AffineTransformOp.TYPE_BILINEAR);
	    this.buffImage = op.filter(this.buffImage, null);
	    image = Toolkit.getDefaultToolkit().createImage(buffImage.getSource());
	    scaleImage(currentScale);
	    this.repaint();
	}
	
	/*
	 * Gets the PieceComponent's Piece object.
	 * 
	 * Returns:
	 * 		Piece; the Piece object
	 */
	public Piece getPiece() {
		return this.piece;
	}
	
	/*
	 * Scales the PieceComponent's image based on the parameter given.
	 * 
	 * Parameters:
	 * 		int n = the scale number to scale the image
	 */
	public void scaleImage(int n) {
		this.currentScale = n;
		this.setSize(n, n);
		image = image.getScaledInstance(n,n,Image.SCALE_REPLICATE);
	}
	
	/*
	 * Gets the orientation of the PieceComponent.
	 * 		0 - no rotations
	 * 		1 - one rotation clockwise
	 * 		2 - two rotations clockwise
	 * 		3 - three rotations clockwise
	 * 
	 * Returns:
	 * 		int; the orientation (number of rotations from its initial state)
	 */
	public int getOrientation() {
		return this.orientation;
	}
	
	/*
	 * Gets the order number of the PieceComponent.
	 * 		0 - the first PieceComponent
	 * 		1 - the second PieceComponent 
	 * 		etc.
	 * 
	 * Returns:
	 * 		int; the order number ("index") of the PieceComponent
	 */
	public int getOrder() {
		return order;
	}
	
	/*
	 * Returns the toString() of the PieceComponent's Piece object.
	 * 
	 * Returns:
	 * 		String; the String representation of the Piece object
	 */
	public String toString() {
		return piece.toString();
	}

}
