import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * TimerPanel.java
 * 
 * The TimerPanel is used to display a timer/stopwatch for
 * a user solving the puzzle game. It represents the time
 * to tenths of seconds.
 * 
 * @author Peter Meglis and Ajay Suresh
 * 6 May 2016
 */
public class TimerPanel extends JPanel {

	private JLabel text;
	private JButton startTimer;
	private Timer timer = new Timer(100, null);
	private JLabel timeDisplay;
	private String displayTime = "0.0";
	private ActionListener timerListener;

	/*
	 * Creates a new TimerPanel with some text, a button, 
	 * and a display for the time.
	 */
	public TimerPanel() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 75, 0));
		this.setSize(850,100);
		this.setLocation(0,600);
		
		text = new JLabel("Want to time yourself?");
		text.setFont(new Font("Serif", Font.BOLD, 25));
		

		startTimer = new JButton("Start Timer");
		startTimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				long timestamp = new java.util.Date().getTime();
				
				if (timer.isRunning()) {
					return;
				}
				
				try {
					timer.removeActionListener(timerListener);
				} catch (NullPointerException n) {}
				
				timerListener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						long timerTime = e.getWhen();
						long totalTime = (timerTime - timestamp) / 100;
						long seconds = totalTime / 10;
						long decimal = totalTime % 10;
						displayTime = "" + seconds + "." + decimal;

						timeDisplay.setText(displayTime);
					}
				};
				
				timer.addActionListener(timerListener);
				timer.start();
				text.setVisible(false);
			}

		});
		timeDisplay = new JLabel();
		timeDisplay.setText("0.0");
		timeDisplay.setFont(new Font("Serif", Font.BOLD, 50));

		this.add(text);
		this.add(startTimer);
		this.add(timeDisplay);
		this.setVisible(true);
	}

	/*
	 * Stops the timer, returning a String representation of the time.
	 * 
	 * Returns:
	 * 		String; the String equivalent of the time (ex. "5.4")
	 */
	public String stopTimer() {
		String time = displayTime;
		resetTimer();
		return time;
	}

	/*
	 * Resets the timer to 0.0.
	 */
	public void resetTimer() {
		text.setVisible(true);
		timer.stop();
		timeDisplay.setText("0.0");
		displayTime = "0.0";
	}

	/*
	 * Checks if the timer is currently running.
	 * 
	 * Returns:
	 * 		boolean; true - the timer is running
	 * 				 false - the timer is not running
	 */
	public boolean isRunning() {
		return timer.isRunning();
	}
	
	/*
	 * Updates the visibility of the TimerPanel.
	 * 
	 * Parameters:
	 * 		boolean b = the new visibility of the TimerPanel.
	 */
	public void updateVisible(boolean b) {
		Display.isTimerVisible = b;
		this.setVisible(b);
	}

}
