package net.davidtanzer.babysteps;

import static org.junit.Assert.*;
import net.davidtanzer.babysteps.TimerPresentationModel.TimerState;

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
