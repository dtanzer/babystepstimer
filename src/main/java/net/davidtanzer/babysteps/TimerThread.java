package net.davidtanzer.babysteps;

import net.davidtanzer.babysteps.TimerPresentationModel.TimerState;

public class TimerThread extends Thread {
	private final Timer timer;
	private boolean timerRunning;
	private final TimerPresentationModel presentationModel;

	public TimerThread(final Timer timer, final TimerPresentationModel presentationModel) {
		this.timer = timer;
		this.presentationModel = presentationModel;
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
		timerRunning = false;
	}

	public void resetTimer() {
		presentationModel.setTimerState(TimerState.FINISHED_IN_TIME);
		timer.resetTimer();
	}
}
