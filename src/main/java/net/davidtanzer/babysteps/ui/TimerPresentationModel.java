package net.davidtanzer.babysteps.ui;

import java.text.DecimalFormat;

public class TimerPresentationModel {
	public static enum TimerState {
		NORMAL, FINISHED_IN_TIME, ALL_TIME_ELAPSED
	}

	private static final String BACKGROUND_COLOR_NEUTRAL = "#ffffff";
	private static final String BACKGROUND_COLOR_FAILED = "#ffcccc";
	private static final String BACKGROUND_COLOR_PASSED = "#ccffcc";

	private String bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
	private final DecimalFormat twoDigitsFormat = new DecimalFormat("00");
	private boolean running;
	private long remainingSeconds;

	public void setRunning(final boolean running) {
		this.running = running;
	}
	
	public void setRemainingSeconds(final long secondsInCycle) {
		this.remainingSeconds = secondsInCycle;
	}
	
	public void setTimerState(final TimerState state) {
		switch (state) {
		case FINISHED_IN_TIME:
			bodyBackgroundColor = BACKGROUND_COLOR_PASSED;
			break;
		case ALL_TIME_ELAPSED:
			bodyBackgroundColor = BACKGROUND_COLOR_FAILED;
			break;

		default:
			bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
			break;
		}
	}
	
	public String getTimerHtml() {
		return createTimerHtml(getRemainingTimeCaption());
	}
	
	private String getRemainingTimeCaption() {
		long remainingMinutes = remainingSeconds/60;
		return twoDigitsFormat.format(remainingMinutes)+":"+twoDigitsFormat.format(remainingSeconds-remainingMinutes*60);
	}

	private String createTimerHtml(final String timerText) {
		String timerHtml = "<html><body style=\"border: 3px solid #555555; background: "+bodyBackgroundColor+"; margin: 0; padding: 0;\">" +
				"<h1 style=\"text-align: center; font-size: 30px; color: #333333;\">"+timerText+"</h1>" +
				"<div style=\"text-align: center\">";
		if(running) {
			timerHtml += "<a style=\"color: #555555;\" href=\"command://stop\">Stop</a> " +
					"<a style=\"color: #555555;\" href=\"command://reset\">Reset</a> ";
		} else {
			timerHtml += "<a style=\"color: #555555;\" href=\"command://start\">Start</a> ";
		}
		timerHtml += "<a style=\"color: #555555;\" href=\"command://quit\">Quit</a> ";
		timerHtml += "</div>" +
				"</body></html>";
		return timerHtml;
	}
}
