package net.davidtanzer.babysteps;

public class TimerThread extends Thread {
	static final long SECONDS_IN_CYCLE = 120;

	private Clock clock;
	private BabyStepsTimerUI ui;
	private boolean timerRunning;
	private long currentCycleStartTime;



	public TimerThread(Clock clock, BabyStepsTimerUI ui) {
		this.clock = clock;
		this.ui = ui;
	}

	@Override
	public void run() {
		timerRunning = true;
		currentCycleStartTime = this.clock.getCurrentTimeMillis();

		while (timerRunning) {
			long elapsedTime = clock.getCurrentTimeMillis() - currentCycleStartTime;

			if (elapsedTime >= SECONDS_IN_CYCLE * 1000 + 980) {
				currentCycleStartTime = this.clock.getCurrentTimeMillis();
				elapsedTime = this.clock.getCurrentTimeMillis() - currentCycleStartTime;
			}
			long elapsedSeconds = elapsedTime / 1000;
			long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;

			ui.update(elapsedSeconds, remainingSeconds);
			try {
				sleep(10);
			} catch (InterruptedException e) {
				//We don't really care about this one...
			}
		}
	}


	public void stopTimer() {
		timerRunning = false;
	}

	public void resetTimer() {
		currentCycleStartTime = clock.getCurrentTimeMillis();
	}
}
