package net.davidtanzer.babysteps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BabystepsTimerTest {

	private BabystepsTimer babystepsTimer;
	private TestingClock clock = new TestingClock();

	@BeforeEach
	public void setup() {
		startUp();
	}

	@Test
	public void onStartUpFrameIsVisible() {
		assertThat(babystepsTimer.timerFrame.isVisible()).isTrue();
	}

	@Test
	public void onStartUpTimerShowsTwoMinutes() {
		assertThat(getTime()).isEqualTo("02:00");
	}

	@Test
	public void onPressingStartTimerCountsDown()  {
		pressStart();
		skipTime(1300L);
		assertThat(getTime()).isEqualTo("01:59");
	}

	private String getTime() {
		String timerHtml = babystepsTimer.timerPane.getText();
		Pattern pattern = Pattern.compile("\\d{2}:\\d{2}");
		Matcher matcher = pattern.matcher(timerHtml);
		if (!matcher.find())  {
			throw new RuntimeException("Time not found.");
		}
		return matcher.group();
	}

	private void startUp() {
		babystepsTimer = new BabystepsTimer();
		babystepsTimer.start(clock);
	}

	private void skipTime(long millis) {
		clock.addMillis(millis);
		yieldToTimerThread();
	}

	private class TestingClock extends Clock {

		private long millis = 0;

		@Override
		public long getCurrentTimeMillis() {
			return millis;
		}

		public void addMillis(long millis){
			this.millis += millis;
		}
	}

	private void pressStart() {
		HyperlinkListener listener = babystepsTimer.timerPane.getHyperlinkListeners()[0];
		HyperlinkEvent hyperlinkEvent = mock(HyperlinkEvent.class);
		when(hyperlinkEvent.getDescription()).thenReturn("command://start");
		when(hyperlinkEvent.getEventType()).thenReturn(HyperlinkEvent.EventType.ACTIVATED);
		listener.hyperlinkUpdate(hyperlinkEvent);

		yieldToTimerThread();

	}

	private void yieldToTimerThread() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}

}