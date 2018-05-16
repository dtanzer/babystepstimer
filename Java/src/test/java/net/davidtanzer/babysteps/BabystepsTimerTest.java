package net.davidtanzer.babysteps;

import org.junit.Test;

import javax.swing.event.HyperlinkEvent;

import java.net.MalformedURLException;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class BabystepsTimerTest {
	private TestingWallClock wallClock;

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
		press("stop");
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
		press("stop");
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
		press("stop");
	}

	@Test
	public void startsAt_2_00_AgainWhenTimeIsUp() throws Exception {
		startTimerApp();

		press("start");
		waitFor(119000L);
		assumeTimerShows("0:01");

		//Advance after end of cycle, so wall clock starts a new cycle
		waitFor(2000L);

		waitFor(1000L);
		assertTimerShows("1:59");
		press("stop");
	}

	@Test
	public void showsRedBackgroundWhenTimeIsUp() throws Exception {
		startTimerApp();

		press("start");
		waitFor(119000L);
		assumeTimerShows("0:01");

		//Advance *exactly to* end of cycle, so timer sets the background
		waitFor(1000L);

		assertBackgroundIs(BabystepsTimer.BACKGROUND_COLOR_FAILED);
		press("stop");
	}

	@Test
	public void backgroundTurnsGreenWhenResetInTime() throws Exception {
		startTimerApp();

		press("start");
		waitFor(119000L);
		assumeTimerShows("0:01");

		press("reset");

		assertBackgroundIs(BabystepsTimer.BACKGROUND_COLOR_PASSED);
		press("stop");
	}

	@Test
	public void backgroundTurnsWhiteAgainAfterSomeTime() throws Exception {
		startTimerApp();

		press("start");
		waitFor(119000L);
		assumeTimerShows("0:01");

		press("reset");

		assumeBackgroundIs(BabystepsTimer.BACKGROUND_COLOR_PASSED);
		//Changes background back between 5 and six seconds
		waitFor(5500);
		assertBackgroundIs(BabystepsTimer.BACKGROUND_COLOR_NEUTRAL);

		press("stop");
	}

	private void assertBackgroundIs(String backgroundColor) {
		final String appText = BabystepsTimer.TimerRenderer.timerPane.getText();
		assertTrue(
				"Expected timer background to be \""+backgroundColor+"\" (Full window content: \""+appText+"\")",
				appText.contains("background-color: "+backgroundColor));
	}

	private void assumeBackgroundIs(String backgroundColor) {
		final String appText = BabystepsTimer.TimerRenderer.timerPane.getText();
		assumeTrue(
				"Expected timer background to be \""+backgroundColor+"\" (Full window content: \""+appText+"\")",
				appText.contains("background-color: "+backgroundColor));
	}

	private void assertTimerShows(String time) {
		final String appText = BabystepsTimer.TimerRenderer.timerPane.getText();
		assertTrue(
				"Expected timer to show \""+time+"\" (Full window content: \""+appText+"\")",
				appText.contains(time));
	}

	private void assumeTimerShows(String time) {
		final String appText = BabystepsTimer.TimerRenderer.timerPane.getText();
		assumeTrue(
				"Expected timer to show \""+time+"\" (Full window content: \""+appText+"\")",
				appText.contains(time));
	}

	private void startTimerApp() throws Exception {
		wallClock  = new TestingWallClock();

		BabystepsTimer.wallclock = wallClock;
		BabystepsTimer.main(null);

		Thread.yield();
		Thread.sleep(50L);
		wallClock.advanceBy(100);
	}

	private void press(String button) throws MalformedURLException, InterruptedException {
		final HyperlinkEvent event = new HyperlinkEvent(BabystepsTimer.TimerRenderer.timerPane, HyperlinkEvent.EventType.ACTIVATED, null, "command://"+button);
		BabystepsTimer.TimerRenderer.timerPane.getHyperlinkListeners()[0].hyperlinkUpdate(event);
		waitFor(0);
	}

	private void waitFor(long millis) throws InterruptedException {
		wallClock.advanceBy(millis);
		Thread.yield();
		Thread.sleep(50L);
	}

	private class TestingWallClock implements WallClock {
		private long currentTime;

		@Override
		public long currentTimeMillis() {
			return currentTime;
		}

		@Override
		public synchronized void nextTick() throws InterruptedException {
			wait();
		}

		public synchronized void advanceBy(long millis) {
			currentTime += millis;
			notify();
		}
	}
}