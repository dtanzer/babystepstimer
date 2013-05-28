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
import static org.mockito.Mockito.*;
import net.davidtanzer.babysteps.ui.TimerPresentationModel;

import org.junit.Before;
import org.junit.Test;

public class TimerIntegrationTest {
	private WallClock wallClock;
	private TimerPresentationModel presentationModel;
	private Timer timer;

	@Before
	public void setup() {
		wallClock = mock(WallClock.class);
		
		TimerFactory factory = new DefaultTimerFactory() {
			@Override
			public Timer createTimer(final long secondsInCycle, final TimerPresentationModel presentationModel) {
				Timer timer = super.createTimer(secondsInCycle, presentationModel);
				timer.setWallClock(wallClock);
				return timer;
			}
		};
		
		presentationModel = new TimerPresentationModel();
		
		timer = factory.createTimer(12L, presentationModel);
	}
	
	@Test
	public void shouldShowSecondsInCycleRightAfterStarted() throws InterruptedException {
		when(wallClock.currentTime()).thenReturn(0L);
		timer.start();
		timer.runTimerStep();
		
		assertTrue(presentationModel.getTimerHtml(), presentationModel.getTimerHtml().contains("00:12"));
	}
	
	@Test
	public void timerShouldShowRemainingTimeWhenRunning() throws InterruptedException {
		when(wallClock.currentTime()).thenReturn(0L).thenReturn(2000L);
		timer.start();
		timer.runTimerStep();
		assertTrue(presentationModel.getTimerHtml(), presentationModel.getTimerHtml().contains("00:10"));
	}
}
