import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;

/**
 * Display.java
 * 
 * The Display class is the foundation of the Graphics component 
 * to the Puzzle Program. It creates a frame with a HolderPanel for 
 * PieceComponents on the left side, the actual Puzzle/Board on 
 * the right side, and a couple of options buttons next to it.
 * 
 * @author Peter Meglis and Ajay Suresh
 * 6 May 2016
 */
public class Display {

	private static JFrame frame;
	private static HolderPanel holderPanel;
	private static BoardComponent boardComponent;
	private static DragListener listener;
	private static JPanel puzzlePanel;
	private static JPanel glassPane;
	private static TimerPanel timerPanel;
	protected static boolean isTimerVisible;
	private List<Piece> pieces;
	protected static List<PieceComponent> pieceComps;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Display window = new Display();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Display() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		Dimension size = new Dimension(1100,700);
		frame.setSize(size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initPieces();
		initPieceComps();

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setSize(size);
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setSize(size);
		splitPane.setDividerLocation(250);
		splitPane.setEnabled(false);
		layeredPane.add(splitPane);

		/*
		 * Left Side - HolderPanel
		 */
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(tabbedPane);

		holderPanel = new HolderPanel(pieceComps);
		holderPanel.initialize();
		tabbedPane.addTab("Puzzle Pieces", null, holderPanel, null);
		holderPanel.setLayout(new GridLayout(5, 2, 0, 0));

		/*
		 * Right side - PuzzlePanel and TimerPanel
		 */
		puzzlePanel = new JPanel();
		puzzlePanel.setLayout(null);

		Puzzle puzzle = new Puzzle(3,pieces);
		boardComponent = new BoardComponent(puzzle,holderPanel);
		puzzlePanel.add(boardComponent);
		boardComponent.setSize(450,450);
		boardComponent.setLocation(150,100);
		puzzlePanel.setComponentZOrder(boardComponent, 0);

		timerPanel = new TimerPanel();
		isTimerVisible = true;
		puzzlePanel.add(timerPanel);
		puzzlePanel.setComponentZOrder(timerPanel, 0);

		/*
		 * Options Buttons
		 */
		JLabel options = new JLabel("Options");
		puzzlePanel.add(options);
		options.setFont(new Font("Serif", Font.BOLD, 20));
		options.setSize(150, 30);
		options.setLocation(650, 10);
		
		JButton resetButton = new JButton("Reset Game");
		puzzlePanel.add(resetButton);
		resetButton.setSize(150, 30);
		resetButton.setLocation(650, 50);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.setSolutionDisplayed(false);
				boardComponent.reset();
				holderPanel.reset();
				
				timerPanel.stopTimer();
				timerPanel.updateVisible(true);
				
			}
		});
		
		JButton solveButton = new JButton("Show Solution");
		puzzlePanel.add(solveButton);
		solveButton.setSize(150, 30);
		solveButton.setLocation(650, 90);
		solveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.setSolutionDisplayed(true);
				timerPanel.updateVisible(false);
				boardComponent.reset();
				holderPanel.reset();
				boardComponent.solve();
				listener.updateBoardComponent();
			}
		});
		
		JButton timerOnOff = new JButton("Timer On/Off");
		puzzlePanel.add(timerOnOff);
		timerOnOff.setSize(150, 30);
		timerOnOff.setLocation(650, 130);
		timerOnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isTimerVisible) {
					timerPanel.updateVisible(false);
					timerPanel.stopTimer();
				}
				else if (holderPanel.isFull()){
					timerPanel.updateVisible(true);
				}
			}
		});
		
		JButton randomize = new JButton("Shuffle Pieces");
		puzzlePanel.add(randomize);
		randomize.setSize(150, 30);
		randomize.setLocation(650, 170);
		randomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				holderPanel.randomize();
			}
		});
		
		splitPane.setRightComponent(puzzlePanel);
		
		
		// Initialize listeners
		listener = new DragListener(layeredPane,holderPanel,puzzlePanel,boardComponent,timerPanel);
		holderPanel.addMouseListener(listener);
		holderPanel.addMouseMotionListener(listener);
		holderPanel.addMouseWheelListener(listener);
		puzzlePanel.addMouseListener(listener);
		puzzlePanel.addMouseMotionListener(listener);

		glassPane = new JPanel();
		frame.setGlassPane(glassPane);

		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	/*
	 * Initializes the Piece objects to private data.
	 */
	public void initPieces() {
		pieces = new ArrayList<Piece>();

		pieces.add(new Piece(Side.CLUB_OUT, Side.HEART_OUT, Side.DIAMOND_IN, Side.CLUB_IN));
		pieces.add(new Piece(Side.SPADE_OUT, Side.DIAMOND_OUT, Side.SPADE_IN, Side.HEART_IN));
		pieces.add(new Piece(Side.HEART_OUT, Side.SPADE_OUT, Side.SPADE_IN, Side.CLUB_IN));
		pieces.add(new Piece(Side.HEART_OUT, Side.DIAMOND_OUT, Side.CLUB_IN, Side.CLUB_IN));
		pieces.add(new Piece(Side.SPADE_OUT, Side.SPADE_OUT, Side.HEART_IN, Side.CLUB_IN));
		pieces.add(new Piece(Side.HEART_OUT, Side.DIAMOND_OUT, Side.DIAMOND_IN, Side.HEART_IN));
		pieces.add(new Piece(Side.SPADE_OUT, Side.DIAMOND_OUT, Side.HEART_IN, Side.DIAMOND_IN));
		pieces.add(new Piece(Side.CLUB_OUT, Side.HEART_OUT, Side.SPADE_IN, Side.HEART_IN));
		pieces.add(new Piece(Side.DIAMOND_OUT, Side.CLUB_OUT, Side.CLUB_IN, Side.DIAMOND_IN));
	}

	/*
	 * Initializes all of the PieceComponents that go with the Piece objects.
	 */
	public void initPieceComps() {
		pieceComps = new ArrayList<PieceComponent>();

		pieceComps.add(new PieceComponent("resources\\puzzle pieces\\piece_1.png", pieces.get(0),0));
		pieceComps.add(new PieceComponent("resources\\puzzle pieces\\piece_2.png", pieces.get(1),1));
		pieceComps.add(new PieceComponent("resources\\puzzle pieces\\piece_3.png", pieces.get(2),2));
		pieceComps.add(new PieceComponent("resources\\puzzle pieces\\piece_4.png", pieces.get(3),3));
		pieceComps.add(new PieceComponent("resources\\puzzle pieces\\piece_5.png", pieces.get(4),4));
		pieceComps.add(new PieceComponent("resources\\puzzle pieces\\piece_6.png", pieces.get(5),5));
		pieceComps.add(new PieceComponent("resources\\puzzle pieces\\piece_7.png", pieces.get(6),6));
		pieceComps.add(new PieceComponent("resources\\puzzle pieces\\piece_8.png", pieces.get(7),7));
		pieceComps.add(new PieceComponent("resources\\puzzle pieces\\piece_9.png", pieces.get(8),8));
	}

	/*
	 * If the puzzle is solved, a congratulations screen is displayed 
	 * with your time (if you started the timer) and a button to restart.
	 */
	public static void solved() {
		glassPane.setLayout(new GridBagLayout());
		glassPane.setVisible(true);
		glassPane.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		GridBagConstraints d = new GridBagConstraints();
		d.gridwidth = GridBagConstraints.REMAINDER;
		d.fill = GridBagConstraints.HORIZONTAL;
		d.insets = new Insets(0,150,0,0);
		
		JLabel grats = new JLabel("Good Job! You solved the puzzle!");
		grats.setFont(new Font("Serif", Font.BOLD, 40));
		glassPane.add(grats, c);
		
		try {
			String time = timerPanel.stopTimer();
			if (!time.equals("0.0")) {
				JLabel timeScore = new JLabel();
				timeScore.setText("Time: " + time + " seconds");
				timeScore.setFont(new Font("Serif", Font.BOLD, 40));
				glassPane.add(timeScore,d);
			}
		} catch (NullPointerException n) {}

		JButton startOverButton = new JButton("Click to Restart");
		startOverButton.setFont(new Font("Serif", Font.BOLD, 40));
		startOverButton.setBackground(Color.decode("#3498db"));
		startOverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boardComponent.reset();
				holderPanel.reset();
				glassPane.removeAll();
				glassPane.setVisible(false);
			}
		});
		glassPane.add(startOverButton,c);
	}
	

}
