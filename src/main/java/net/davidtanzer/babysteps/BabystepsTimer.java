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


public class BabystepsTimer {

	static final long SECONDS_IN_CYCLE = 12;

	static TimerPresentationModel presentationModel;
	static TimerView timerView;

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
}
