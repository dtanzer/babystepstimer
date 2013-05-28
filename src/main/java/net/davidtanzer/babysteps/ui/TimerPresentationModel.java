/*  Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

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
			timerHtml += "<a style=\"color: #555555;\" href=\""+TimerWindowCommandsHyperlinkListener.COMMAND_STOP+"\">Stop</a> " +
					"<a style=\"color: #555555;\" href=\""+TimerWindowCommandsHyperlinkListener.COMMAND_RESET+"\">Reset</a> ";
		} else {
			timerHtml += "<a style=\"color: #555555;\" href=\""+TimerWindowCommandsHyperlinkListener.COMMAND_START+"\">Start</a> ";
		}
		timerHtml += "<a style=\"color: #555555;\" href=\""+TimerWindowCommandsHyperlinkListener.COMMAND_QUIT+"\">Quit</a> ";
		timerHtml += "</div>" +
				"</body></html>";
		return timerHtml;
	}
}
