package net.davidtanzer.babysteps;

public class TimerThread extends Thread {
	private final Timer timer;
	private boolean timerRunning;

	public TimerThread(final Timer timer) {
		this.timer = timer;
	}

	@Override
	public void run() {
		timerRunning = true;

		while(timerRunning) {
			timer.runTimerStep();
		}
	}

	public void stopTimer() {
		timerRunning = false;
	}
}
