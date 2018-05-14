package net.davidtanzer.babysteps;

import org.junit.Test;

import static org.junit.Assert.*;

public class BabystepsTimerTest {
	@Test
	public void shows_2_00_atStartup() throws Exception {
		startTimer();
		assertWindowShows("2:00");
	}

	private void assertWindowShows(String text) {
		assertTrue(BabystepsTimer.timerPane.getText().contains(text));
	}

	private void startTimer() throws Exception {
		BabystepsTimer.main(null);
		Thread.sleep(100);
	}
}