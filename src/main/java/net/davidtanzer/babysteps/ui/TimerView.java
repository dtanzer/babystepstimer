/*  Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package net.davidtanzer.babysteps.ui;


import javax.swing.JFrame;
import javax.swing.JTextPane;

import net.davidtanzer.babysteps.Timer;
import net.davidtanzer.babysteps.TimerEventListener;

public class TimerView implements TimerEventListener {
	private final JFrame timerFrame;
	private final JTextPane timerPane;
	private final TimerPresentationModel presentationModel;
	final Timer timer;
	
	public TimerView(final TimerPresentationModel presentationModel, final long secondsInCycle, final Timer timer) {
		this.presentationModel = presentationModel;
		this.timer = timer;
		
		timerFrame = new JFrame("Babysteps Timer");
		timerFrame.setUndecorated(true);

		timerFrame.setSize(250, 120);
		timerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		timerPane = new JTextPane();
		timerPane.setContentType("text/html");
		timerPane.setText(presentationModel.getTimerHtml());
		timerPane.setEditable(false);
		timerPane.addMouseMotionListener(new MoveTimerWindowMouseMotionListener(this));
		timerPane.addHyperlinkListener(new TimerWindowCommandsHyperlinkListener(this, secondsInCycle, presentationModel));
		timerFrame.getContentPane().add(timerPane);

		timerFrame.setVisible(true);
	}

	void updateTimerFrame(final boolean alwaysOnTop) {
		timerFrame.setAlwaysOnTop(alwaysOnTop);
		timerPane.setText(presentationModel.getTimerHtml());
		timerFrame.repaint();
	}

	@Override
	public void onNewTimeAvailable(final long elapsedSeconds, final long remainingSeconds) {
		timerPane.setText(presentationModel.getTimerHtml());
		timerFrame.repaint();
	}

	public void setLocationDelta(final int deltaX, final int deltaY) {
		timerFrame.setLocation(timerFrame.getLocation().x+deltaX, timerFrame.getLocation().y+deltaY);
	}
}
