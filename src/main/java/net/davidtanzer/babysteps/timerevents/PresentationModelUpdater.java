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

package net.davidtanzer.babysteps.timerevents;

import net.davidtanzer.babysteps.Timer;
import net.davidtanzer.babysteps.TimerEventListener;
import net.davidtanzer.babysteps.ui.TimerPresentationModel;
import net.davidtanzer.babysteps.ui.TimerPresentationModel.TimerState;

public class PresentationModelUpdater implements TimerEventListener {
	private final TimerPresentationModel presentationModel;

	public PresentationModelUpdater(final TimerPresentationModel presentationModel) {
		this.presentationModel = presentationModel;
	}

	@Override
	public void onNewTimeAvailable(final long elapsedSeconds, final long remainingSeconds) {
		presentationModel.setRemainingSeconds(remainingSeconds);
		if(remainingSeconds == Timer.FINAL_WARNING_TIME) {
			presentationModel.setTimerState(TimerState.ALL_TIME_ELAPSED);
		}
		if(elapsedSeconds == Timer.RESET_BACKGROUND_TIME) {
			presentationModel.setTimerState(TimerState.NORMAL);
		}
	}

}
