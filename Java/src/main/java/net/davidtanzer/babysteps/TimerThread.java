package net.davidtanzer.babysteps;

final class TimerThread extends Thread {
	private static boolean timerRunning;
	private static long currentCycleStartTime;
	private static String lastRemainingTime;

	private final TimerGui gui;

	public TimerThread(TimerGui gui) {
		this.gui = gui;
	}

	public static void stopTimer() {
		timerRunning = false;
	}

	public static void resetTimer() {
		currentCycleStartTime = BabystepsTimer.wallclock.currentTimeMillis();
	}

	@Override
	public void run() {
		timerRunning = true;
		resetTimer();

		while(timerRunning) {
			long elapsedTime = BabystepsTimer.wallclock.currentTimeMillis() - currentCycleStartTime;

			if(elapsedTime >= BabystepsTimer.SECONDS_IN_CYCLE*1000+980) {
				resetTimer();
				elapsedTime = BabystepsTimer.wallclock.currentTimeMillis() - currentCycleStartTime;
			}

			String bodyBackgroundColor = gui.getBodyBackgroundColor();
			if(elapsedTime >= 5000 && elapsedTime < 6000 && !BabystepsTimer.BACKGROUND_COLOR_NEUTRAL.equals(bodyBackgroundColor)) {
				bodyBackgroundColor = BabystepsTimer.BACKGROUND_COLOR_NEUTRAL;
			}

			String remainingTime = gui.getRemainingTimeCaption(elapsedTime);
			if(!remainingTime.equals(lastRemainingTime)) {
				if(remainingTime.equals("00:10")) {
					BabystepsTimer.playSound("2166__suburban-grilla__bowl-struck.wav");
				} else if(remainingTime.equals("00:00")) {
					BabystepsTimer.playSound("32304__acclivity__shipsbell.wav");
					bodyBackgroundColor= BabystepsTimer.BACKGROUND_COLOR_FAILED;
				}

				gui.updateUi(remainingTime, bodyBackgroundColor, true);
				lastRemainingTime = remainingTime;
			}
			try {
				BabystepsTimer.wallclock.nextTick();
			} catch (InterruptedException e) {
				//We don't really care about this one...
			}
		}
	}
}
