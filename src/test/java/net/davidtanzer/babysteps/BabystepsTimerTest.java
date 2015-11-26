package net.davidtanzer.babysteps;

import org.junit.Test;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

/**
 * Created by David Tanzer on 11/25/2015.
 */
public class BabystepsTimerTest {
	@Test
	public void timerShowsStartTimeAfterIStartedTheProgram() throws Exception {
		BabystepsTimer.main(null);

		HTMLEditorKit kit = new HTMLEditorKit();
		HTMLDocument doc = (HTMLDocument) BabystepsTimer.timerPane.getDocument();
		StringWriter writer = new StringWriter();
		kit.write(writer, doc, 0, doc.getLength());
		String currentHtml = writer.toString();

		assertThat(currentHtml, containsString("02:00"));
	}
}