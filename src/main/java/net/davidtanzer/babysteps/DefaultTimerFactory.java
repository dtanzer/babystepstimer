package net.davidtanzer.babysteps;

import net.davidtanzer.babysteps.timerevents.FinalWarningSoundPlayer;
import net.davidtanzer.babysteps.timerevents.FirstWarningSoundPlayer;
import net.davidtanzer.babysteps.timerevents.PresentationModelUpdater;
import net.davidtanzer.babysteps.ui.TimerPresentationModel;
import net.davidtanzer.babysteps.ui.TimerSoundsPlayer;

class DefaultTimerFactory extends TimerFactory {
	@Override
	public Timer createTimer(final long secondsInCycle, final TimerPresentationModel presentationModel) {
		TimerSoundsPlayer soundsPlayer = new TimerSoundsPlayer();
		Timer timer = new Timer(secondsInCycle);
		timer.addTimerEventListener(new PresentationModelUpdater(presentationModel));
		timer.addTimerEventListener(new FirstWarningSoundPlayer(soundsPlayer));
		timer.addTimerEventListener(new FinalWarningSoundPlayer(soundsPlayer));
		return timer;
	}
}
