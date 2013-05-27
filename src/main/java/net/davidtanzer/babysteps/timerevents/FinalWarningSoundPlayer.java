package net.davidtanzer.babysteps.timerevents;

import net.davidtanzer.babysteps.Timer;
import net.davidtanzer.babysteps.TimerEventListener;
import net.davidtanzer.babysteps.TimerSoundsPlayer;

public class FinalWarningSoundPlayer implements TimerEventListener {

	private final TimerSoundsPlayer soundsPlayer;

	public FinalWarningSoundPlayer(final TimerSoundsPlayer soundsPlayer) {
		this.soundsPlayer = soundsPlayer;
	}

	@Override
	public void onNewTimeAvailable(final long elapsedSeconds, final long remainingSeconds) {
		if(remainingSeconds == Timer.FINAL_WARNING_TIME) {
			soundsPlayer.playTimeElapsedInfoSound();
		}
	}

}
