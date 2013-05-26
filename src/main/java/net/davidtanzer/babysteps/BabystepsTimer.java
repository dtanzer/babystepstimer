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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import net.davidtanzer.babysteps.TimerPresentationModel.TimerState;

public class BabystepsTimer {

	static final long SECONDS_IN_CYCLE = 12;

	static boolean timerRunning;
	static long currentCycleStartTime;
	private static String lastRemainingTime;

	private static TimerPresentationModel presentationModel;
	private static TimerView timerView;

	public static void main(final String[] args) throws InterruptedException {
		presentationModel = new TimerPresentationModel();
		presentationModel.setRemainingSeconds(SECONDS_IN_CYCLE);
		
		timerView = new TimerView(presentationModel);
	}

	public static synchronized void playSound(final String url) {
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

	static final class TimerThread extends Thread {
		@Override
		public void run() {
			timerRunning = true;
			presentationModel.setRunning(true);
			currentCycleStartTime = System.currentTimeMillis();
			
			while(timerRunning) {
				long elapsedTime = System.currentTimeMillis() - currentCycleStartTime;
				
				if(elapsedTime >= SECONDS_IN_CYCLE*1000+980) {
					currentCycleStartTime = System.currentTimeMillis();
					elapsedTime = System.currentTimeMillis() - currentCycleStartTime;
				}
				if(elapsedTime >= 5000 && elapsedTime < 6000) {
					presentationModel.setTimerState(TimerState.NORMAL);
				}
				
				long elapsedSeconds = elapsedTime/1000;
				long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;

				presentationModel.setRemainingSeconds(remainingSeconds);
				
				String remainingTime = presentationModel.getRemainingTimeCaption();
				if(!remainingTime.equals(lastRemainingTime)) {
					if(remainingTime.equals("00:10")) {
						playSound("2166__suburban-grilla__bowl-struck.wav");
					} else if(remainingTime.equals("00:00")) {
						playSound("32304__acclivity__shipsbell.wav");
						presentationModel.setTimerState(TimerState.ALL_TIME_ELAPSED);
					}
					
					timerView.updatedTimerDataAvailable();
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
