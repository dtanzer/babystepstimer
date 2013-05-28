package net.davidtanzer.babysteps;

import net.davidtanzer.babysteps.ui.TimerPresentationModel;
import net.davidtanzer.babysteps.ui.TimerPresentationModel.TimerState;

public class TimerThread extends Thread {
	private final Timer timer;
	private boolean timerRunning;
	private final TimerPresentationModel presentationModel;
	private final long secondsInCycle;

	public TimerThread(final Timer timer, final TimerPresentationModel presentationModel, final long secondsInCycle) {
		this.timer = timer;
		this.presentationModel = presentationModel;
		this.secondsInCycle = secondsInCycle;
	}

	@Override
	public void run() {
		timerRunning = true;
		presentationModel.setRunning(true);
		timer.start();

		while(timerRunning) {
			timer.runTimerStep();
		}
	}

	public void stopTimer() {
		presentationModel.setRemainingSeconds(secondsInCycle);
		presentationModel.setRunning(false);
		presentationModel.setTimerState(TimerState.NORMAL);
		
		timerRunning = false;
	}

	public void resetTimer() {
		presentationModel.setTimerState(TimerState.FINISHED_IN_TIME);
		timer.resetTimer();
	}
}
