package net.davidtanzer.babysteps.ui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import net.davidtanzer.babysteps.BabystepsTimer;

public class TimerSoundsPlayer {

	private synchronized void playSound(final String url) {
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

	public void playTenSecondsWarningSound() {
		playSound("2166__suburban-grilla__bowl-struck.wav");
	}
	
	public void playTimeElapsedInfoSound() {
		playSound("32304__acclivity__shipsbell.wav");
	}
}
