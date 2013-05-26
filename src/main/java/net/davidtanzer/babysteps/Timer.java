package net.davidtanzer.babysteps;

import net.davidtanzer.babysteps.TimerPresentationModel.TimerState;

final class Timer extends Thread {
	private boolean timerRunning;
	private long currentCycleStartTime;
	private String lastRemainingTime;
	private final TimerPresentationModel presentationModel;
	private final TimerDataListener dataListener;
	private final TimerSoundsPlayer soundsPlayer;

	public Timer(final TimerPresentationModel presentationModel, final TimerDataListener dataListener, final TimerSoundsPlayer soundsPlayer) {
		this.presentationModel = presentationModel;
		this.dataListener = dataListener;
		this.soundsPlayer = soundsPlayer;
	}
	
	@Override
	public void run() {
		timerRunning = true;
		presentationModel.setRunning(true);
		currentCycleStartTime = System.currentTimeMillis();
		
		while(timerRunning) {
			runTimerStep();
		}
	}

	void runTimerStep() {
		long elapsedTime = System.currentTimeMillis() - currentCycleStartTime;
		
		if(elapsedTime >= BabystepsTimer.SECONDS_IN_CYCLE*1000+980) {
			currentCycleStartTime = System.currentTimeMillis();
			elapsedTime = System.currentTimeMillis() - currentCycleStartTime;
		}
		if(elapsedTime >= 5000 && elapsedTime < 6000) {
			presentationModel.setTimerState(TimerState.NORMAL);
		}
		
		long elapsedSeconds = elapsedTime/1000;
		long remainingSeconds = BabystepsTimer.SECONDS_IN_CYCLE - elapsedSeconds;

		presentationModel.setRemainingSeconds(remainingSeconds);
		
		String remainingTime = presentationModel.getRemainingTimeCaption();
		if(!remainingTime.equals(lastRemainingTime)) {
			if(remainingTime.equals("00:10")) {
				soundsPlayer.playTenSecondsWarningSound();
			} else if(remainingTime.equals("00:00")) {
				soundsPlayer.playTimeElapsedInfoSound();
				presentationModel.setTimerState(TimerState.ALL_TIME_ELAPSED);
			}
			
			dataListener.updatedTimerDataAvailable();
			lastRemainingTime = remainingTime;
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
		currentCycleStartTime = System.currentTimeMillis();
		presentationModel.setTimerState(TimerState.FINISHED_IN_TIME);
	}
}