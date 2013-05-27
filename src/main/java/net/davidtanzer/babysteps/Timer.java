package net.davidtanzer.babysteps;

import net.davidtanzer.babysteps.TimerPresentationModel.TimerState;

final class Timer extends Thread {
	private static final long FINAL_WARNING_TIME = 0L;
	private static final long FIRST_WARNING_TIME = 10L;
	private static final long RESET_BACKGROUND_TIME = 5L;
	private static final int MILLISECONDS_IN_SECOND = 1000;
	private boolean timerRunning;
	private long currentCycleStartTime;
	private long lastRemainingSeconds;
	private final TimerPresentationModel presentationModel;
	private final TimerDataListener dataListener;
	private final TimerSoundsPlayer soundsPlayer;
	private WallClock wallClock = new WallClock();
	private final long secondsInCycle;

	public Timer(final long secondsInCycle, final TimerPresentationModel presentationModel, final TimerDataListener dataListener, final TimerSoundsPlayer soundsPlayer) {
		this.presentationModel = presentationModel;
		this.dataListener = dataListener;
		this.soundsPlayer = soundsPlayer;
		this.secondsInCycle = secondsInCycle;
	}
	
	@Override
	public void run() {
		timerRunning = true;
		presentationModel.setRunning(true);
		currentCycleStartTime = wallClock.currentTime();
		
		while(timerRunning) {
			runTimerStep();
		}
	}

	void runTimerStep() {
		long elapsedTime = wallClock.currentTime() - currentCycleStartTime;
		
		if(isAllTimeElapsed(elapsedTime)) {
			currentCycleStartTime = wallClock.currentTime();
			elapsedTime = 0L;
		}
	
		long elapsedSeconds = elapsedTime/MILLISECONDS_IN_SECOND;
		long remainingSeconds = secondsInCycle - elapsedSeconds;

		if(remainingSeconds != lastRemainingSeconds) {
			onNewTimeAvailable(elapsedSeconds, remainingSeconds);
			lastRemainingSeconds = remainingSeconds;
		}
		sleep();
	}

	private void onNewTimeAvailable(final long elapsedSeconds, final long remainingSeconds) {
		presentationModel.setRemainingSeconds(remainingSeconds);
		
		if(elapsedSeconds == RESET_BACKGROUND_TIME) {
			presentationModel.setTimerState(TimerState.NORMAL);
		}
		if(remainingSeconds == FIRST_WARNING_TIME) {
			soundsPlayer.playTenSecondsWarningSound();
		} else if(remainingSeconds == FINAL_WARNING_TIME) {
			soundsPlayer.playTimeElapsedInfoSound();
			presentationModel.setTimerState(TimerState.ALL_TIME_ELAPSED);
		}
		
		dataListener.updatedTimerDataAvailable();
	}

	private boolean isAllTimeElapsed(final long elapsedTime) {
		long almostOneSecond = MILLISECONDS_IN_SECOND-20L;
		long cycleInMilliseconds = secondsInCycle*MILLISECONDS_IN_SECOND;
		
		//The time "00:00" should show for just about one second, but not longer or we risk to show "00:-01"!
		return elapsedTime >= cycleInMilliseconds+almostOneSecond;
	}

	private void sleep() {
		try {
			sleep(10);
		} catch (InterruptedException e) {
			//We don't really care about this one...
		}
	}
	
	public void stopTimer() {
		timerRunning = false;
	}
	
	public void resetTimer() {
		currentCycleStartTime = wallClock.currentTime();
		presentationModel.setTimerState(TimerState.FINISHED_IN_TIME);
	}
	
	void setWallClock(final WallClock wallClock) {
		this.wallClock = wallClock;
	}
}