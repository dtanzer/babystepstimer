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

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class TimerTest {
	@Test
	public void shouldNotifyListenersWhenNewTimeIsAvailable() throws InterruptedException {
		WallClock wallClock = mock(WallClock.class);
		
		//Sound should be played when ten seconds are remaining, that is at two seconds.
		when(wallClock.currentTime()).thenReturn(0L).thenReturn(10L);
		
		Timer timer = new Timer(12L);
		timer.setWallClock(wallClock);
		TimerEventListener listener = mock(TimerEventListener.class);
		timer.addTimerEventListener(listener);
		
		//Make sure the timer uses the first time value as starting time.
		timer.resetTimer();
		timer.runTimerStep();
		
		verify(listener).onNewTimeAvailable(eq(0L), eq(12L));
	}
}
