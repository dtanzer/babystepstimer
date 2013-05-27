package net.davidtanzer.babysteps;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class TimerTest {
	@Test
	public void shouldInformListenerWhenNewTimerDataIsAvailable() throws InterruptedException {
		TimerPresentationModel presentationModel = new TimerPresentationModel();
		TimerDataListener dataListener = mock(TimerDataListener.class);
		TimerSoundsPlayer soundsPlayer = mock(TimerSoundsPlayer.class);
		
		Timer timer = new Timer(12L, presentationModel, dataListener, soundsPlayer);
		timer.runTimerStep();
		
		verify(dataListener, atLeastOnce()).updatedTimerDataAvailable();
	}
	
	@Test
	public void shouldPlayASoundAtTheTenSecondMark() throws InterruptedException {
		TimerPresentationModel presentationModel = mock(TimerPresentationModel.class);
		TimerDataListener dataListener = mock(TimerDataListener.class);
		TimerSoundsPlayer soundsPlayer = mock(TimerSoundsPlayer.class);
		WallClock wallClock = mock(WallClock.class);
		
		//Sound should be played when ten seconds are remaining, that is at two seconds.
		when(wallClock.currentTime()).thenReturn(0L).thenReturn(1990L).thenReturn(2000L).thenReturn(2100L);
		
		Timer timer = new Timer(12L, presentationModel, dataListener, soundsPlayer);
		timer.setWallClock(wallClock);
		
		//Make sure the timer uses the first time value as starting time.
		timer.resetTimer();
		timer.runTimerStep();
		timer.runTimerStep();
		timer.runTimerStep();
		
		verify(soundsPlayer).playTenSecondsWarningSound();
	}
}
