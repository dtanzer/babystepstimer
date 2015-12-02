package net.davidtanzer.babysteps;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by David Tanzer on 12/2/2015.
 */
public class TestingClock {
	private Clock clock;
	private Instant currentTime = Instant.now();

	public TestingClock() {
		clock = mock(Clock.class);
		when(clock.millis()).thenReturn(currentTime.toEpochMilli());
	}

	public Clock clock() {
		return clock;
	}

	void advanceTimeBy(Duration duration) throws InterruptedException {
		currentTime = currentTime.plus(duration);
		when(clock.millis()).thenReturn(currentTime.toEpochMilli());
		Thread.sleep(50L);
	}
}
