package net.davidtanzer.babysteps.timerevents;

import net.davidtanzer.babysteps.Timer;
import net.davidtanzer.babysteps.TimerEventListener;
import net.davidtanzer.babysteps.TimerSoundsPlayer;

public class FirstWarningSoundPlayer implements TimerEventListener {

	private final TimerSoundsPlayer soundsPlayer;

	public FirstWarningSoundPlayer(final TimerSoundsPlayer soundsPlayer) {
		this.soundsPlayer = soundsPlayer;
	}

	@Override
	public void onNewTimeAvailable(final long elapsedSeconds, final long remainingSeconds) {
		if(remainingSeconds == Timer.FIRST_WARNING_TIME) {
			soundsPlayer.playTenSecondsWarningSound();
		}
	}

}
