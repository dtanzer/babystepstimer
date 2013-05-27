package net.davidtanzer.babysteps;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class TimerTest {
	@Test
	public void shouldNotifyListenersWhenNewTimeIsAvailable() throws InterruptedException {
		TimerPresentationModel presentationModel = mock(TimerPresentationModel.class);
		WallClock wallClock = mock(WallClock.class);
		
		//Sound should be played when ten seconds are remaining, that is at two seconds.
		when(wallClock.currentTime()).thenReturn(0L).thenReturn(10L);
		
		Timer timer = new Timer(12L, presentationModel);
		timer.setWallClock(wallClock);
		TimerEventListener listener = mock(TimerEventListener.class);
		timer.addTimerEventListener(listener);
		
		//Make sure the timer uses the first time value as starting time.
		timer.resetTimer();
		timer.runTimerStep();
		
		verify(listener).onNewTimeAvailable(eq(0L), eq(12L));
	}
}
