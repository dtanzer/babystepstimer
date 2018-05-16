package net.davidtanzer.babysteps;

final class TimerThread extends Thread {
	private static boolean timerRunning;
	private static long currentCycleStartTime;
	private static String lastRemainingTime;
	private final BabystepsTimer.TimerRenderer timerRenderer;

	public TimerThread(BabystepsTimer.TimerRenderer timerRenderer) {
		this.timerRenderer = timerRenderer;
	}

	public static void stopTimer() {
		timerRunning = false;
	}

	public static void resetTimer(long newTime) {
		currentCycleStartTime = newTime;
	}

	@Override
	public void run() {
		timerRunning = true;
		currentCycleStartTime = BabystepsTimer.wallclock.currentTimeMillis();

		while(timerRunning) {
			long elapsedTime = BabystepsTimer.wallclock.currentTimeMillis() - currentCycleStartTime;

			if(elapsedTime >= BabystepsTimer.SECONDS_IN_CYCLE*1000+980) {
				currentCycleStartTime = BabystepsTimer.wallclock.currentTimeMillis();
				elapsedTime = BabystepsTimer.wallclock.currentTimeMillis() - currentCycleStartTime;
			}
			String bodyBackgroundColor = timerRenderer.getBodyBackgroundColor();
			if(elapsedTime >= 5000 && elapsedTime < 6000 && !BabystepsTimer.BACKGROUND_COLOR_NEUTRAL.equals(bodyBackgroundColor)) {
				bodyBackgroundColor = BabystepsTimer.BACKGROUND_COLOR_NEUTRAL;
			}

			String remainingTime = timerRenderer.getRemainingTimeCaption(elapsedTime);
			if(!remainingTime.equals(lastRemainingTime)) {
				if(remainingTime.equals("00:10")) {
					BabystepsTimer.playSound("2166__suburban-grilla__bowl-struck.wav");
				} else if(remainingTime.equals("00:00")) {
					BabystepsTimer.playSound("32304__acclivity__shipsbell.wav");
					bodyBackgroundColor= BabystepsTimer.BACKGROUND_COLOR_FAILED;
				}

				lastRemainingTime = remainingTime;
			}
			timerRenderer.update(remainingTime, bodyBackgroundColor, true);
			try {
				BabystepsTimer.wallclock.nextTick();
			} catch (InterruptedException e) {
				//We don't really care about this one...
			}
		}
	}
}
