package net.davidtanzer.babysteps;

import net.davidtanzer.babysteps.ui.TimerPresentationModel;
import net.davidtanzer.babysteps.ui.TimerView;

public abstract class TimerFactory {
	private static TimerFactory defaultTimerFactory = new DefaultTimerFactory();

	public static TimerFactory get() {
		return defaultTimerFactory;
	}
	
	public abstract Timer createTimer(long secondsInCycle, TimerPresentationModel presentationModel, TimerView view);
}
