package net.davidtanzer.babysteps;

public class Progress {

	public boolean isTimeUp(String remainingTime) {
		return remainingTime.equals("00:00");
	}

	public boolean aboutToRunOutOfTime(String remainingTime) {
		return remainingTime.equals("00:10");
	}
}
