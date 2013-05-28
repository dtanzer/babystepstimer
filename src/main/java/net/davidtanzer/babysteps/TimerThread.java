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

import net.davidtanzer.babysteps.ui.TimerPresentationModel;
import net.davidtanzer.babysteps.ui.TimerPresentationModel.TimerState;

public class TimerThread extends Thread {
	private final Timer timer;
	private boolean timerRunning;
	private final TimerPresentationModel presentationModel;
	private final long secondsInCycle;

	public TimerThread(final Timer timer, final TimerPresentationModel presentationModel, final long secondsInCycle) {
		this.timer = timer;
		this.presentationModel = presentationModel;
		this.secondsInCycle = secondsInCycle;
	}

	@Override
	public void run() {
		timerRunning = true;
		presentationModel.setRunning(true);
		timer.start();

		while(timerRunning) {
			timer.runTimerStep();
		}
	}

	public void stopTimer() {
		presentationModel.setRemainingSeconds(secondsInCycle);
		presentationModel.setRunning(false);
		presentationModel.setTimerState(TimerState.NORMAL);
		
		timerRunning = false;
	}

	public void resetTimer() {
		presentationModel.setTimerState(TimerState.FINISHED_IN_TIME);
		timer.resetTimer();
	}
}
