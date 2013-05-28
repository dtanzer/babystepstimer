package net.davidtanzer.babysteps.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

class MoveTimerWindowMouseMotionListener implements MouseMotionListener {
	private int lastX;
	private int lastY;
	private final TimerView timerView;

	public MoveTimerWindowMouseMotionListener(final TimerView timerView) {
		this.timerView = timerView;
	}
	
	@Override
	public void mouseMoved(final MouseEvent e) {
		lastX = e.getXOnScreen();
		lastY = e.getYOnScreen();
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		int x = e.getXOnScreen();
		int y = e.getYOnScreen();
		
		timerView.setLocationDelta((x-lastX), (y-lastY));
		
		lastX = x;
		lastY = y;
	}
}