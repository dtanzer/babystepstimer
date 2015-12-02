package net.davidtanzer.babysteps;

import org.junit.Before;
import org.junit.Test;

import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Duration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

/**
 * Created by David Tanzer on 11/25/2015.
 */
public class BabystepsTimerTest {
	private TestingClock testingClock;

	@Before
	public void setup() {
		testingClock = new TestingClock();
		BabystepsTimer.clock = testingClock.clock();
	}

	@Test
	public void timerShowsStartTimeAfterIStartedTheProgram() throws Exception {
		BabystepsTimer.main(null);

		assertThat(getCurrentHtml(), containsString("02:00"));
	}

	@Test
	public void timerStartsToCountDownAfterIPressTheStartButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));

		press("start");
		testingClock.advanceTimeBy(Duration.ofMillis(1100L));

		assertThat(getCurrentHtml(), containsString("01:59"));
	}

	@Test
	public void timerShowsStartTimeAgainAfterIPressTheResetButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		testingClock.advanceTimeBy(Duration.ofMillis(1100L));
		assumeThat(getCurrentHtml(), containsString("01:59"));

		press("reset");
		testingClock.advanceTimeBy(Duration.ofMillis(100L));

		assertThat(getCurrentHtml(), containsString("02:00"));
	}

	@Test
	public void timerIsStillRunningAfterIPressTheResetButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		testingClock.advanceTimeBy(Duration.ofMillis(1100L));
		assumeThat(getCurrentHtml(), containsString("01:59"));
		press("reset");
		testingClock.advanceTimeBy(Duration.ofMillis(100L));
		assumeThat(getCurrentHtml(), containsString("02:00"));

		testingClock.advanceTimeBy(Duration.ofMillis(1100L));
		assertThat(getCurrentHtml(), containsString("01:59"));
	}

	@Test
	public void timerShowsStartTimeAgainAfterIPressTheStopButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		testingClock.advanceTimeBy(Duration.ofMillis(1100L));
		assumeThat(getCurrentHtml(), containsString("01:59"));

		press("stop");
		testingClock.advanceTimeBy(Duration.ofMillis(100L));

		assertThat(getCurrentHtml(), containsString("02:00"));
	}

	@Test
	public void timerIsNotRunningAfterIPressTheStopButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		testingClock.advanceTimeBy(Duration.ofMillis(1100L));
		assumeThat(getCurrentHtml(), containsString("01:59"));
		press("stop");
		testingClock.advanceTimeBy(Duration.ofMillis(100L));
		assumeThat(getCurrentHtml(), containsString("02:00"));

		testingClock.advanceTimeBy(Duration.ofMillis(1100L));
		assertThat(getCurrentHtml(), containsString("02:00"));
	}

	@Test
	public void timerShowsGreenBackgroundAfterIPressTheResetButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		testingClock.advanceTimeBy(Duration.ofMillis(119100L));
		assumeThat(getCurrentHtml(), containsString("00:01"));
		press("reset");
		assumeThat(getCurrentHtml(), containsString("02:00"));

		assertThat(getCurrentHtml(), containsString("background-color: #ccffcc;"));
	}

	@Test
	public void timerShowsRedBackgroundWhenItIsRunningToZero() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		testingClock.advanceTimeBy(Duration.ofMillis(119100L));
		assumeThat(getCurrentHtml(), containsString("00:01"));
		testingClock.advanceTimeBy(Duration.ofMillis(1100L));
		assumeThat(getCurrentHtml(), containsString("00:00"));

		assertThat(getCurrentHtml(), containsString("background-color: #ffcccc;"));
	}

	private void press(String command) throws InterruptedException {
		BabystepsTimer.timerPane.getHyperlinkListeners()[0].hyperlinkUpdate(
				new HyperlinkEvent(BabystepsTimer.timerPane, HyperlinkEvent.EventType.ACTIVATED, null, "command://"+ command));
		Thread.sleep(50L);
	}

	private String getCurrentHtml() throws IOException, BadLocationException {
		HTMLEditorKit kit = new HTMLEditorKit();
		HTMLDocument doc = (HTMLDocument) BabystepsTimer.timerPane.getDocument();
		StringWriter writer = new StringWriter();
		kit.write(writer, doc, 0, doc.getLength());
		return writer.toString();
	}
}