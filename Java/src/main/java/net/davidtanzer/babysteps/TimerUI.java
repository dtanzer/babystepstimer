package net.davidtanzer.babysteps;

public interface TimerUI {
	String getRemainingTimeCaption(long elapsedTime);

	void resetBackgroundColor();

	void timerFinished();

	void warning();

	void showTime(String remainingTime);
}
