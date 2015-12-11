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

package net.davidtanzer.babysteps;

import java.text.DecimalFormat;
import java.time.Clock;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class BabystepsTimer {
	static final String BACKGROUND_COLOR_NEUTRAL = "#ffffff";
	static final String BACKGROUND_COLOR_FAILED = "#ffcccc";
	static final String BACKGROUND_COLOR_PASSED = "#ccffcc";

	static final long SECONDS_IN_CYCLE = 120;
	SwingTimerUserInterface userInterface;

	Clock clock;
	boolean timerRunning;
	long currentCycleStartTime;
	String lastRemainingTime;
	String bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
	
	DecimalFormat twoDigitsFormat = new DecimalFormat("00");

	public BabystepsTimer(Clock clock) {
		this.clock = clock;
		userInterface = new SwingTimerUserInterface();
		userInterface.initialize(this);
	}

	public static void main(final String[] args) throws InterruptedException {
		BabystepsTimer timer = new BabystepsTimer(Clock.systemDefaultZone());
		timer.showUserInterface();
	}

	void showUserInterface() {
		userInterface.show();
	}

	String getRemainingTimeCaption(final long elapsedTime) {
		long elapsedSeconds = elapsedTime/1000;
		long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;
		
		long remainingMinutes = remainingSeconds/60;
		return twoDigitsFormat.format(remainingMinutes)+":"+twoDigitsFormat.format(remainingSeconds-remainingMinutes*60);
	}

	synchronized void playSound(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(
							BabystepsTimer.class.getResourceAsStream("/"+url));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	final class TimerThread extends Thread {
		@Override
		public void run() {
			timerRunning = true;
			currentCycleStartTime = clock.millis();
			
			while(timerRunning) {
				long elapsedTime = clock.millis() - currentCycleStartTime;
				
				if(elapsedTime >= SECONDS_IN_CYCLE*1000+980) {
					currentCycleStartTime = clock.millis();
					elapsedTime = clock.millis() - currentCycleStartTime;
				}
				if(elapsedTime >= 5000 && elapsedTime < 6000 && !BACKGROUND_COLOR_NEUTRAL.equals(bodyBackgroundColor)) {
					bodyBackgroundColor = BACKGROUND_COLOR_NEUTRAL;
				}
				
				String remainingTime = getRemainingTimeCaption(elapsedTime);
				if(!remainingTime.equals(lastRemainingTime)) {
					if(remainingTime.equals("00:10")) {
						playSound("2166__suburban-grilla__bowl-struck.wav");
					} else if(remainingTime.equals("00:00")) {
						playSound("32304__acclivity__shipsbell.wav");
						bodyBackgroundColor=BACKGROUND_COLOR_FAILED;
					}

					userInterface.showNewTime(remainingTime, bodyBackgroundColor);
					lastRemainingTime = remainingTime;
				}
				try {
					sleep(10);
				} catch (InterruptedException e) {
					//We don't really care about this one...
				}
			}
		}
	}
}
