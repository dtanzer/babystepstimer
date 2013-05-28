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

import net.davidtanzer.babysteps.timerevents.FinalWarningSoundPlayer;
import net.davidtanzer.babysteps.timerevents.FirstWarningSoundPlayer;
import net.davidtanzer.babysteps.timerevents.PresentationModelUpdater;
import net.davidtanzer.babysteps.ui.TimerPresentationModel;
import net.davidtanzer.babysteps.ui.TimerSoundsPlayer;

class DefaultTimerFactory extends TimerFactory {
	@Override
	public Timer createTimer(final long secondsInCycle, final TimerPresentationModel presentationModel) {
		TimerSoundsPlayer soundsPlayer = new TimerSoundsPlayer();
		Timer timer = new Timer(secondsInCycle);
		timer.addTimerEventListener(new PresentationModelUpdater(presentationModel));
		timer.addTimerEventListener(new FirstWarningSoundPlayer(soundsPlayer));
		timer.addTimerEventListener(new FinalWarningSoundPlayer(soundsPlayer));
		return timer;
	}
}
