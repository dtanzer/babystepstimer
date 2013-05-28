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