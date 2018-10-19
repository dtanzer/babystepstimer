package net.davidtanzer.babysteps;

public interface TimerGui {
	String getRemainingTimeCaption(long elapsedTime);

	String getBodyBackgroundColor();

	void updateUi(String remainingTime, String bodyBackgroundColor, boolean b);
}
