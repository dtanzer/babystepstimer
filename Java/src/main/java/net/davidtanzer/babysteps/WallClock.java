package net.davidtanzer.babysteps;

public interface WallClock {
	long currentTimeMillis();

	void nextTick() throws InterruptedException;
}
