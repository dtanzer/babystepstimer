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

import java.util.ArrayList;
import java.util.List;

public class Timer {
	public static final long FINAL_WARNING_TIME = 0L;
	public static final long FIRST_WARNING_TIME = 10L;
	public static final long RESET_BACKGROUND_TIME = 5L;
	
	private static final int MILLISECONDS_IN_SECOND = 1000;
	
	private WallClock wallClock = new WallClock();
	
	private long currentCycleStartTime;
	private long lastRemainingSeconds;
	
	private final long secondsInCycle;
	
	private final List<TimerEventListener> timerEventListeners = new ArrayList<>();

	public Timer(final long secondsInCycle) {
		this.secondsInCycle = secondsInCycle;
	}
	
	void start() {
		currentCycleStartTime = wallClock.currentTime();
	}
	
	void runTimerStep() {
		long elapsedTime = wallClock.currentTime() - currentCycleStartTime;
		
		if(isAllTimeElapsed(elapsedTime)) {
			currentCycleStartTime = wallClock.currentTime();
			elapsedTime = 0L;
		}
	
		long elapsedSeconds = elapsedTime/MILLISECONDS_IN_SECOND;
		long remainingSeconds = secondsInCycle - elapsedSeconds;

		if(remainingSeconds != lastRemainingSeconds) {
			onNewTimeAvailable(elapsedSeconds, remainingSeconds);
			lastRemainingSeconds = remainingSeconds;
		}
		sleep();
	}

	private void onNewTimeAvailable(final long elapsedSeconds, final long remainingSeconds) {
		for(TimerEventListener listener : timerEventListeners) {
			listener.onNewTimeAvailable(elapsedSeconds, remainingSeconds);
		}
	}

	private boolean isAllTimeElapsed(final long elapsedTime) {
		long almostOneSecond = MILLISECONDS_IN_SECOND-20L;
		long cycleInMilliseconds = secondsInCycle*MILLISECONDS_IN_SECOND;
		
		//The time "00:00" should show for just about one second, but not longer or we risk to show "00:-01"!
		return elapsedTime >= cycleInMilliseconds+almostOneSecond;
	}

	private void sleep() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			//We don't really care about this one...
		}
	}
	
	void resetTimer() {
		currentCycleStartTime = wallClock.currentTime();
	}
	
	void setWallClock(final WallClock wallClock) {
		this.wallClock = wallClock;
	}
	
	public void addTimerEventListener(final TimerEventListener listener) {
		timerEventListeners.add(listener);
	}
}