package com.github.oncizl.bluetooth.schedule;

import java.util.TimerTask;

public final class ScanSchedule extends Schedule {

	private Schedulable mSchedulable;
	private TimerTask mTimerTask;

	public ScanSchedule() {
		super(30 * 1000, 1);
	}

	public ScanSchedule(long period) {
		super(period, 1);
	}

	@Override
	public void execute(final Schedulable schedulable) {
		if (schedulable != null) {
			counter(0);
			schedulable(schedulable);
			if (isTimerTask()) timerTask().cancel();
			timerTask(new TimerTask() {
				@Override
				public void run() {
					if (isContinue() && isSchedulable()) {
						schedulable().schedule();
					}
				}
			});
			if (isTimer()) timer().schedule(timerTask(), period(), period());
		}
	}

	@Override
	public void purge() {
		if (isTimerTask()) timerTask().cancel();
		if (isTimer()) timer().purge();
	}

	@Override
	public void cancel() {
		if (isTimer()) timer().cancel();
	}

	private boolean isSchedulable() {
		return mSchedulable != null;
	}

	private Schedulable schedulable() {
		return mSchedulable;
	}

	private void schedulable(Schedulable runnable) {
		mSchedulable = runnable;
	}

	private boolean isTimerTask() {
		return mTimerTask != null;
	}

	private TimerTask timerTask() {
		return mTimerTask;
	}

	private void timerTask(TimerTask timerTask) {
		mTimerTask = timerTask;
	}
}