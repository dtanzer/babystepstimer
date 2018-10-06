package net.davidtanzer.babysteps;

public class ProgressLogic {

	private final SoundPlayer soundPlayer;
	private BackgroundDriver backgroundDriver;

	public ProgressLogic(BackgroundDriver backgroundDriver, SoundPlayer soundPlayer) {
		this.backgroundDriver = backgroundDriver;
		this.soundPlayer = soundPlayer;
	}

	void updateProgress(long elapsedTime) {
		String remainingTime = BabystepsTimer.getRemainingTimeCaption(elapsedTime);
		if(!remainingTime.equals(BabystepsTimer.lastRemainingTime)) {
			update(remainingTime);
		}
	}

	private void update(String remainingTime) {
		if(remainingTime.equals("00:10")) {
			soundPlayer.playSound("2166__suburban-grilla__bowl-struck.wav");
		} else if(remainingTime.equals("00:00")) {
			soundPlayer.playSound("32304__acclivity__shipsbell.wav");
			this.backgroundDriver.updateBackground(BabystepsTimer.BACKGROUND_COLOR_FAILED);
		}

		String backgroundColor = this.backgroundDriver.getBodyBackgroundColor();
		String timerHtml = BabystepsTimer.createTimerHtml(remainingTime, backgroundColor, true);
		BabystepsTimer.timerPane.setText(timerHtml);
		BabystepsTimer.timerFrame.repaint();
		BabystepsTimer.lastRemainingTime = remainingTime;
	}
}
