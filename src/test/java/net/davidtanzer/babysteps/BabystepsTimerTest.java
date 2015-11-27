package net.davidtanzer.babysteps;

import org.junit.Test;

import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

/**
 * Created by David Tanzer on 11/25/2015.
 */
public class BabystepsTimerTest {
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
		Thread.sleep(1100L);

		assertThat(getCurrentHtml(), containsString("01:59"));
	}

	@Test
	public void timerShowsStartTimeAgainAfterIPressTheResetButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		Thread.sleep(1100L);
		assumeThat(getCurrentHtml(), containsString("01:59"));

		press("reset");
		Thread.sleep(100L);

		assertThat(getCurrentHtml(), containsString("02:00"));
	}

	@Test
	public void timerIsStillRunningAfterIPressTheResetButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		Thread.sleep(1100L);
		assumeThat(getCurrentHtml(), containsString("01:59"));
		press("reset");
		Thread.sleep(100L);
		assumeThat(getCurrentHtml(), containsString("02:00"));

		Thread.sleep(1100L);
		assertThat(getCurrentHtml(), containsString("01:59"));
	}

	@Test
	public void timerShowsStartTimeAgainAfterIPressTheStopButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		Thread.sleep(1100L);
		assumeThat(getCurrentHtml(), containsString("01:59"));

		press("stop");
		Thread.sleep(100L);

		assertThat(getCurrentHtml(), containsString("02:00"));
	}

	@Test
	public void timerIsNotRunningAfterIPressTheStopButton() throws Exception {
		BabystepsTimer.main(null);
		assumeThat(getCurrentHtml(), containsString("02:00"));
		press("start");
		Thread.sleep(1100L);
		assumeThat(getCurrentHtml(), containsString("01:59"));
		press("stop");
		Thread.sleep(100L);
		assumeThat(getCurrentHtml(), containsString("02:00"));

		Thread.sleep(1100L);
		assertThat(getCurrentHtml(), containsString("02:00"));
	}

	private void press(String command) {
		BabystepsTimer.timerPane.getHyperlinkListeners()[0].hyperlinkUpdate(
				new HyperlinkEvent(BabystepsTimer.timerPane, HyperlinkEvent.EventType.ACTIVATED, null, "command://"+ command));
	}

	private String getCurrentHtml() throws IOException, BadLocationException {
		HTMLEditorKit kit = new HTMLEditorKit();
		HTMLDocument doc = (HTMLDocument) BabystepsTimer.timerPane.getDocument();
		StringWriter writer = new StringWriter();
		kit.write(writer, doc, 0, doc.getLength());
		return writer.toString();
	}
}