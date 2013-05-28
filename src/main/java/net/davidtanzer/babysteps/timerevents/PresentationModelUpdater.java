package net.davidtanzer.babysteps.timerevents;

import net.davidtanzer.babysteps.Timer;
import net.davidtanzer.babysteps.TimerEventListener;
import net.davidtanzer.babysteps.ui.TimerPresentationModel;
import net.davidtanzer.babysteps.ui.TimerPresentationModel.TimerState;

public class PresentationModelUpdater implements TimerEventListener {
	private final TimerPresentationModel presentationModel;

	public PresentationModelUpdater(final TimerPresentationModel presentationModel) {
		this.presentationModel = presentationModel;
	}

	@Override
	public void onNewTimeAvailable(final long elapsedSeconds, final long remainingSeconds) {
		presentationModel.setRemainingSeconds(remainingSeconds);
		if(remainingSeconds == Timer.FINAL_WARNING_TIME) {
			presentationModel.setTimerState(TimerState.ALL_TIME_ELAPSED);
		}
		if(elapsedSeconds == Timer.RESET_BACKGROUND_TIME) {
			presentationModel.setTimerState(TimerState.NORMAL);
		}
	}

}
