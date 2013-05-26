package net.davidtanzer.babysteps;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class TimerTest {
	@Test
	public void shouldInformListenerWhenNewTimerDataIsAvailable() throws InterruptedException {
		TimerPresentationModel presentationModel = new TimerPresentationModel();
		TimerDataListener dataListener = mock(TimerDataListener.class);
		TimerSoundsPlayer soundsPlayer = mock(TimerSoundsPlayer.class);
		
		Timer timer = new Timer(presentationModel, dataListener, soundsPlayer);
		timer.runTimerStep();
		
		verify(dataListener, atLeastOnce()).updatedTimerDataAvailable();
	}
	
	@Test
	public void shouldPlayASoundAtTheTenSecondMark() throws InterruptedException {
		TimerPresentationModel presentationModel = mock(TimerPresentationModel.class);
		when(presentationModel.getRemainingTimeCaption()).thenReturn("00:11").thenReturn("00:10");

		TimerDataListener dataListener = mock(TimerDataListener.class);
		TimerSoundsPlayer soundsPlayer = mock(TimerSoundsPlayer.class);
		
		Timer timer = new Timer(presentationModel, dataListener, soundsPlayer);
		timer.runTimerStep();
		timer.runTimerStep();
		
		verify(soundsPlayer).playTenSecondsWarningSound();
	}
}
