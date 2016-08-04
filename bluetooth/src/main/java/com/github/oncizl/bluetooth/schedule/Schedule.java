package com.github.oncizl.bluetooth.schedule;

import java.util.Timer;

abstract class Schedule {

	private int mCounter;
	private int mCount;
	private long mPeriod;
	private Timer mTimer = new Timer();

	Schedule() {
		this(15 * 1000, 1);
	}

	Schedule(long period, int count) {
		period(period);
		count(count);
	}

	Timer timer() {
		return mTimer;
	}

	boolean isTimer() {
		return timer() != null;
	}

	boolean isContinue() {
		counter(counter()+1);
		if (counter() > count()) {
			counter(0);
			purge();
			return false;
		}
		return true;
	}

	public int counter() {
		return mCounter;
	}

	void counter(int counter) {
		mCounter = counter;
	}

	protected boolean isTimeout() {
		return mCounter > mCount;
	}

	long period() {
		return mPeriod;
	}

	private void period(long period) {
		mPeriod = period;
	}

	private long count() {
		return mCount;
	}

	private void count(int count) {
		mCount = count;
	}

	public abstract void purge();

	public abstract void cancel();

	public abstract void execute(Schedulable schedulable);
}