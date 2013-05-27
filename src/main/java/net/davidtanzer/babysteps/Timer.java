package net.davidtanzer.babysteps;

import net.davidtanzer.babysteps.TimerPresentationModel.TimerState;

final class Timer extends Thread {
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
		
		if(elapsedTime >= secondsInCycle*1000+980) {
			currentCycleStartTime = wallClock.currentTime();
			elapsedTime = wallClock.currentTime() - currentCycleStartTime;
		}
		if(elapsedTime >= 5000 && elapsedTime < 6000) {
			presentationModel.setTimerState(TimerState.NORMAL);
		}
		
		long elapsedSeconds = elapsedTime/1000;
		long remainingSeconds = secondsInCycle - elapsedSeconds;

		presentationModel.setRemainingSeconds(remainingSeconds);
		
		if(remainingSeconds != lastRemainingSeconds) {
			if(remainingSeconds == 10L) {
				soundsPlayer.playTenSecondsWarningSound();
			} else if(remainingSeconds == 0L) {
				soundsPlayer.playTimeElapsedInfoSound();
				presentationModel.setTimerState(TimerState.ALL_TIME_ELAPSED);
			}
			
			dataListener.updatedTimerDataAvailable();
			lastRemainingSeconds = remainingSeconds;
		}
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