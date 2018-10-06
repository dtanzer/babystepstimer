package net.davidtanzer.babysteps;

public class BackgroundDriver {
	public String getBodyBackgroundColor() {
		return BabystepsTimer.bodyBackgroundColor;
	}

	void updateBackground(String backgroundColor) {
		BabystepsTimer.bodyBackgroundColor= backgroundColor;
	}
}
