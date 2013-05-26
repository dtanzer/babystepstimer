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

package net.davidtanzer.babysteps;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.davidtanzer.babysteps.TimerPresentationModel.TimerState;

public class BabystepsTimer {

	private static final long SECONDS_IN_CYCLE = 12;

	private static JFrame timerFrame;
	private static JTextPane timerPane;
	private static boolean timerRunning;
	private static long currentCycleStartTime;
	private static String lastRemainingTime;

	private static TimerPresentationModel presentationModel;

	public static void main(final String[] args) throws InterruptedException {
		timerFrame = new JFrame("Babysteps Timer");
		timerFrame.setUndecorated(true);

		timerFrame.setSize(250, 120);
		timerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		presentationModel = new TimerPresentationModel();
		presentationModel.setRemainingSeconds(SECONDS_IN_CYCLE);
		
		timerPane = new JTextPane();
		timerPane.setContentType("text/html");
		timerPane.setText(presentationModel.getTimerHtml());
		timerPane.setEditable(false);
		timerPane.addMouseMotionListener(new MouseMotionListener() {
			private int lastX;
			private int lastY;

			@Override
			public void mouseMoved(final MouseEvent e) {
				lastX = e.getXOnScreen();
				lastY = e.getYOnScreen();
			}
			
			@Override
			public void mouseDragged(final MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				
				timerFrame.setLocation(timerFrame.getLocation().x + (x-lastX), timerFrame.getLocation().y + (y-lastY));
				
				lastX = x;
				lastY = y;
			}
		});
		timerPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(final HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if("command://start".equals(e.getDescription())) {
						timerFrame.setAlwaysOnTop(true);
						timerPane.setText(presentationModel.getTimerHtml());
						timerFrame.repaint();
						new TimerThread().start();
					} else if("command://stop".equals(e.getDescription())) {
						timerRunning = false;
						timerFrame.setAlwaysOnTop(false);
						
						presentationModel.setRemainingSeconds(SECONDS_IN_CYCLE);
						presentationModel.setRunning(false);
						presentationModel.setTimerState(TimerState.NORMAL);
						
						timerPane.setText(presentationModel.getTimerHtml());
						timerFrame.repaint();
					} else  if("command://reset".equals(e.getDescription())) {
						currentCycleStartTime = System.currentTimeMillis();
						presentationModel.setTimerState(TimerState.FINISHED_IN_TIME);
					} else  if("command://quit".equals(e.getDescription())) {
						System.exit(0);
					}
				}
			}
		});
		timerFrame.getContentPane().add(timerPane);

		timerFrame.setVisible(true);
	}

	public static synchronized void playSound(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(
							BabystepsTimer.class.getResourceAsStream("/"+url));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	private static final class TimerThread extends Thread {
		@Override
		public void run() {
			timerRunning = true;
			presentationModel.setRunning(true);
			currentCycleStartTime = System.currentTimeMillis();
			
			while(timerRunning) {
				long elapsedTime = System.currentTimeMillis() - currentCycleStartTime;
				
				if(elapsedTime >= SECONDS_IN_CYCLE*1000+980) {
					currentCycleStartTime = System.currentTimeMillis();
					elapsedTime = System.currentTimeMillis() - currentCycleStartTime;
				}
				if(elapsedTime >= 5000 && elapsedTime < 6000) {
					presentationModel.setTimerState(TimerState.NORMAL);
				}
				
				long elapsedSeconds = elapsedTime/1000;
				long remainingSeconds = SECONDS_IN_CYCLE - elapsedSeconds;

				presentationModel.setRemainingSeconds(remainingSeconds);
				
				String remainingTime = presentationModel.getRemainingTimeCaption();
				if(!remainingTime.equals(lastRemainingTime)) {
					if(remainingTime.equals("00:10")) {
						playSound("2166__suburban-grilla__bowl-struck.wav");
					} else if(remainingTime.equals("00:00")) {
						playSound("32304__acclivity__shipsbell.wav");
						presentationModel.setTimerState(TimerState.ALL_TIME_ELAPSED);
					}
					
					timerPane.setText(presentationModel.getTimerHtml());
					timerFrame.repaint();
					lastRemainingTime = remainingTime;
				}
				try {
					sleep(10);
				} catch (InterruptedException e) {
					//We don't really care about this one...
				}
			}
		}
	}
}
