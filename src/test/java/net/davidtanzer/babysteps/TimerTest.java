package net.davidtanzer.babysteps;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {
	private TimerPresentationModel presentationModel;
	private TimerDataListener dataListener;
	private Timer timer;
	
	@Before
	public void setup() {
		presentationModel = new TimerPresentationModel();
		dataListener = mock(TimerDataListener.class);
		
		timer = new Timer(presentationModel, dataListener);
	}
	@Test
	public void shouldInformListenerWhenNewTimerDataIsAvailable() throws InterruptedException {
		timer.start();
		
		Thread.sleep(1500);
		
		verify(dataListener, atLeastOnce()).updatedTimerDataAvailable();
	}
}
