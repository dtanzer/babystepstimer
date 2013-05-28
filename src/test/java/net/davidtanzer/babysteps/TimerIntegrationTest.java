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
