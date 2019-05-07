package net.davidtanzer.babysteps;

final class TimerThread extends Thread {
	private static boolean timerRunning;
	private static long currentCycleStartTime;
	public static final long SECONDS_IN_CYCLE = 120;
	private static String lastRemainingTime;
	private final TimerUI timerUI;

	public TimerThread(TimerUI timerUI) {

		this.timerUI = timerUI;
	}


	public void stopTimer(){
		timerRunning = false;
	}
	@Override
	public void run() {
		timerRunning = true;
		currentCycleStartTime = BabystepsTimer.wallclock.currentTimeMillis();

		while(timerRunning) {
			long elapsedTime = BabystepsTimer.wallclock.currentTimeMillis() - currentCycleStartTime;

			if(elapsedTime >= SECONDS_IN_CYCLE*1000+980) {
				currentCycleStartTime = BabystepsTimer.wallclock.currentTimeMillis();
				elapsedTime = BabystepsTimer.wallclock.currentTimeMillis() - currentCycleStartTime;
			}
			if(elapsedTime >= 5000 && elapsedTime < 6000) {
				timerUI.resetBackgroundColor();

			}

			String remainingTime = timerUI.getRemainingTimeCaption(elapsedTime);
			if(!remainingTime.equals(lastRemainingTime)) {
				if(remainingTime.equals("00:10")) {
					timerUI.warning();
				} else if(remainingTime.equals("00:00")) {
					timerUI.timerFinished();
				}

				timerUI.showTime(remainingTime);
				lastRemainingTime = remainingTime;
			}
			try {
				BabystepsTimer.wallclock.nextTick();
			} catch (InterruptedException e) {
				//We don't really care about this one...
			}
		}
	}

	public void reset() {
		currentCycleStartTime = BabystepsTimer.wallclock.currentTimeMillis();
	}
}
