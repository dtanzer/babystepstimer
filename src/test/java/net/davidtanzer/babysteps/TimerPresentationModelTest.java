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

import static org.junit.Assert.*;
import net.davidtanzer.babysteps.ui.TimerPresentationModel;
import net.davidtanzer.babysteps.ui.TimerPresentationModel.TimerState;

import org.junit.Test;

public class TimerPresentationModelTest {
	@Test
	public void shouldSetTheBackgroudColorToWhiteWhenTimerStateIsNormal() {
		TimerPresentationModel pm = new TimerPresentationModel();
		pm.setTimerState(TimerState.NORMAL);
		
		assertTrue(pm.getTimerHtml().contains("background: #ffffff;"));
	}

	@Test
	public void shouldSetTheBackgroudColorToGreenWhenTimerStateIsPassed() {
		TimerPresentationModel pm = new TimerPresentationModel();
		pm.setTimerState(TimerState.FINISHED_IN_TIME);
		
		assertTrue(pm.getTimerHtml().contains("background: #ccffcc;"));
	}

	@Test
	public void shouldSetTheBackgroudColorToRedWhenTimerStateIsFailed() {
		TimerPresentationModel pm = new TimerPresentationModel();
		pm.setTimerState(TimerState.ALL_TIME_ELAPSED);
		
		assertTrue(pm.getTimerHtml().contains("background: #ffcccc;"));
	}
	
	@Test
	public void shouldDisplayStopLinkWhenTimerIsRunning() {
		TimerPresentationModel pm = new TimerPresentationModel();
		pm.setRunning(true);
		
		assertTrue(pm.getTimerHtml().contains("Stop"));
	}
	
	@Test
	public void shouldNotDisplayStopLinkWhenTimerIsStopped() {
		TimerPresentationModel pm = new TimerPresentationModel();
		pm.setRunning(false);
		
		assertTrue(!pm.getTimerHtml().contains("Stop"));
	}
}
