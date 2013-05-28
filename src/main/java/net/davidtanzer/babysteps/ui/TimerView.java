package net.davidtanzer.babysteps.ui;


import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.davidtanzer.babysteps.Timer;
import net.davidtanzer.babysteps.TimerEventListener;
import net.davidtanzer.babysteps.TimerThread;
import net.davidtanzer.babysteps.ui.TimerPresentationModel.TimerState;

public class TimerView implements TimerEventListener {
	private final JFrame timerFrame;
	private final JTextPane timerPane;
	private final TimerPresentationModel presentationModel;
	private final Timer timer;
	
	public TimerView(final TimerPresentationModel presentationModel, final long secondsInCycle, final Timer timer) {
		this.presentationModel = presentationModel;
		this.timer = timer;
		
		timerFrame = new JFrame("Babysteps Timer");
		timerFrame.setUndecorated(true);

		timerFrame.setSize(250, 120);
		timerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		timerPane = new JTextPane();
		timerPane.setContentType("text/html");
		timerPane.setText(presentationModel.getTimerHtml());
		timerPane.setEditable(false);
		timerPane.addMouseMotionListener(new MoveTimerWindowMouseMotionListener(this));
		timerPane.addHyperlinkListener(new HyperlinkListener() {
			private TimerThread timerThread;
			
			@Override
			public void hyperlinkUpdate(final HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if("command://start".equals(e.getDescription())) {
						timerFrame.setAlwaysOnTop(true);
						timerPane.setText(presentationModel.getTimerHtml());
						timerFrame.repaint();
						
						timerThread = new TimerThread(TimerView.this.timer, presentationModel);
						timerThread.start();
					} else if("command://stop".equals(e.getDescription())) {
						timerThread.stopTimer();
						timerFrame.setAlwaysOnTop(false);
						
						presentationModel.setRemainingSeconds(secondsInCycle);
						presentationModel.setRunning(false);
						presentationModel.setTimerState(TimerState.NORMAL);
						
						timerPane.setText(presentationModel.getTimerHtml());
						timerFrame.repaint();
					} else  if("command://reset".equals(e.getDescription())) {
						timerThread.resetTimer();
					} else  if("command://quit".equals(e.getDescription())) {
						System.exit(0);
					}
				}
			}
		});
		timerFrame.getContentPane().add(timerPane);

		timerFrame.setVisible(true);
	}

	@Override
	public void onNewTimeAvailable(final long elapsedSeconds, final long remainingSeconds) {
		timerPane.setText(presentationModel.getTimerHtml());
		timerFrame.repaint();
	}

	public void setLocationDelta(final int deltaX, final int deltaY) {
		timerFrame.setLocation(timerFrame.getLocation().x+deltaX, timerFrame.getLocation().y+deltaY);
	}
}
