package net.davidtanzer.babysteps.ui;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.davidtanzer.babysteps.TimerThread;

final class TimerWindowCommandsHyperlinkListener implements HyperlinkListener {
	private final long secondsInCycle;
	private final TimerPresentationModel presentationModel;
	private TimerThread timerThread;
	private final TimerView timerView;

	TimerWindowCommandsHyperlinkListener(final TimerView timerView, final long secondsInCycle, final TimerPresentationModel presentationModel) {
		this.timerView = timerView;
		this.secondsInCycle = secondsInCycle;
		this.presentationModel = presentationModel;
	}

	@Override
	public void hyperlinkUpdate(final HyperlinkEvent e) {
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			if("command://start".equals(e.getDescription())) {
				timerView.updateTimerFrame(true);
				
				timerThread = new TimerThread(timerView.timer, presentationModel, secondsInCycle);
				timerThread.start();
			} else if("command://stop".equals(e.getDescription())) {
				timerThread.stopTimer();
				
				timerView.updateTimerFrame(false);
			} else  if("command://reset".equals(e.getDescription())) {
				timerThread.resetTimer();
			} else  if("command://quit".equals(e.getDescription())) {
				System.exit(0);
			}
		}
	}
}