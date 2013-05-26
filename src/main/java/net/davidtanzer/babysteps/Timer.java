package net.davidtanzer.babysteps;

import net.davidtanzer.babysteps.TimerPresentationModel.TimerState;

final class Timer extends Thread {
	private boolean timerRunning;
	private long currentCycleStartTime;
	private String lastRemainingTime;
	private final TimerPresentationModel presentationModel;
	private final TimerDataListener dataListener;

	public Timer(final TimerPresentationModel presentationModel, final TimerDataListener dataListener) {
		this.presentationModel = presentationModel;
		this.dataListener = dataListener;
	}
	
	@Override
	public void run() {
		timerRunning = true;
		presentationModel.setRunning(true);
		currentCycleStartTime = System.currentTimeMillis();
		
		while(timerRunning) {
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
					BabystepsTimer.playSound("2166__suburban-grilla__bowl-struck.wav");
				} else if(remainingTime.equals("00:00")) {
					BabystepsTimer.playSound("32304__acclivity__shipsbell.wav");
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
	}
	public void stopTimer() {
		timerRunning = false;
	}
	public void resetTimer() {
		currentCycleStartTime = System.currentTimeMillis();
		presentationModel.setTimerState(TimerState.FINISHED_IN_TIME);
	}
}