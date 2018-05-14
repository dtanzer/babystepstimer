package net.davidtanzer.babysteps;

import org.junit.Test;

import javax.swing.event.HyperlinkEvent;

import java.net.MalformedURLException;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class BabystepsTimerTest {
	@Test
	public void shows_2_00_atStartup() throws Exception {
		startTimerApp();

		assertTimerShows("2:00");
	}

	@Test
	public void startsCountingDownWhenStartWasPressed() throws Exception {
		startTimerApp();

		press("start");
		waitFor(1000L);

		assertTimerShows("1:59");
	}

	@Test
	public void stopsCountingDownWhenStopWasPressed() throws Exception {
		startTimerApp();

		press("start");
		waitFor(1000L);
		assumeTimerShows("1:59");
		press("stop");

		assertTimerShows("2:00");
		waitFor(2000L);
		assertTimerShows("2:00");
	}

	@Test
	public void startsAt_2_00_AgainWhenResetWasPressed() throws Exception {
		startTimerApp();

		press("start");
		waitFor(1000L);
		assumeTimerShows("1:59");
		press("reset");

		assertTimerShows("2:00");
		waitFor(1000L);
		assertTimerShows("1:59");
	}

	private void assertTimerShows(String time) {
		final String appText = BabystepsTimer.timerPane.getText();
		assertTrue(
				"Expected timer to show \""+time+"\" (Full window content: \""+appText+"\")",
				appText.contains(time));
	}

	private void assumeTimerShows(String time) {
		final String appText = BabystepsTimer.timerPane.getText();
		assumeTrue(
				"Expected timer to show \""+time+"\" (Full window content: \""+appText+"\")",
				appText.contains(time));
	}

	private void startTimerApp() throws Exception {
		BabystepsTimer.main(null);
		Thread.sleep(100L);
	}

	private void press(String button) throws MalformedURLException, InterruptedException {
		final HyperlinkEvent event = new HyperlinkEvent(BabystepsTimer.timerPane, HyperlinkEvent.EventType.ACTIVATED, null, "command://"+button);
		BabystepsTimer.timerPane.getHyperlinkListeners()[0].hyperlinkUpdate(event);
		Thread.sleep(50L);
	}

	private void waitFor(long millis) throws InterruptedException {
		Thread.sleep(millis);
	}

}