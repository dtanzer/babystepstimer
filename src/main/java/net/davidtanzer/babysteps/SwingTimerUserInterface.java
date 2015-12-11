package net.davidtanzer.babysteps;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class SwingTimerUserInterface {
	JFrame timerFrame;
	JTextPane timerPane;

	void initialize(BabystepsTimer babystepsTimer) {
		timerFrame = new JFrame("Babysteps Timer");
		timerFrame.setUndecorated(true);

		timerFrame.setSize(250, 120);
		timerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timerPane = new JTextPane();
		timerPane.setContentType("text/html");
		timerPane.setText(babystepsTimer.userInterface.createTimerHtml(babystepsTimer.getRemainingTimeCaption(0L), BabystepsTimer.BACKGROUND_COLOR_NEUTRAL, false));
		timerPane.setEditable(false);
		timerPane.addMouseMotionListener(new MouseMotionListener() {
			private int lastX;
			private int lastY;

			@Override
			public void mouseMoved(final MouseEvent e) {
				lastX = e.getXOnScreen();
				lastY = e.getYOnScreen();
			}

			@Override
			public void mouseDragged(final MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();

				timerFrame.setLocation(timerFrame.getLocation().x + (x-lastX), timerFrame.getLocation().y + (y-lastY));

				lastX = x;
				lastY = y;
			}
		});
		timerPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(final HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if("command://start".equals(e.getDescription())) {
						timerFrame.setAlwaysOnTop(true);
						timerPane.setText(babystepsTimer.userInterface.createTimerHtml(babystepsTimer.getRemainingTimeCaption(0L), BabystepsTimer.BACKGROUND_COLOR_NEUTRAL, true));
						timerFrame.repaint();
						babystepsTimer.new TimerThread().start();
					} else if("command://stop".equals(e.getDescription())) {
						babystepsTimer.timerRunning = false;
						timerFrame.setAlwaysOnTop(false);
						timerPane.setText(babystepsTimer.userInterface.createTimerHtml(babystepsTimer.getRemainingTimeCaption(0L), BabystepsTimer.BACKGROUND_COLOR_NEUTRAL, false));
						timerFrame.repaint();
					} else  if("command://reset".equals(e.getDescription())) {
						babystepsTimer.currentCycleStartTime = babystepsTimer.clock.millis();
						babystepsTimer.bodyBackgroundColor= BabystepsTimer.BACKGROUND_COLOR_PASSED;
					} else  if("command://quit".equals(e.getDescription())) {
						System.exit(0);
					}
				}
			}
		});
		timerFrame.getContentPane().add(timerPane);
	}

	String createTimerHtml(final String timerText, final String bodyColor, final boolean running) {
		String timerHtml = "<html><body style=\"border: 3px solid #555555; background: "+bodyColor+"; margin: 0; padding: 0;\">" +
				"<h1 style=\"text-align: center; font-size: 30px; color: #333333;\">"+timerText+"</h1>" +
				"<div style=\"text-align: center\">";
		if(running) {
			timerHtml += "<a style=\"color: #555555;\" href=\"command://stop\">Stop</a> " +
					"<a style=\"color: #555555;\" href=\"command://reset\">Reset</a> ";
		} else {
			timerHtml += "<a style=\"color: #555555;\" href=\"command://start\">Start</a> ";
		}
		timerHtml += "<a style=\"color: #555555;\" href=\"command://quit\">Quit</a> ";
		timerHtml += "</div>" +
				"</body></html>";
		return timerHtml;
	}

	public void show() {
		timerFrame.setVisible(true);
	}

	public void showNewTime(String remainingTime, String bodyBackgroundColor) {
		timerPane.setText(createTimerHtml(remainingTime, bodyBackgroundColor, true));
		timerFrame.repaint();
	}
}
