package net.davidtanzer.babysteps;

public interface TimerEventListener {
	void onNewTimeAvailable(final long elapsedSeconds, final long remainingSeconds);
}
