package com.github.oncizl.helper;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

public class TimerHelper {

	private long delay;
	private long period;
	private int times;
	private boolean isDaemon;
	private int count;
	private Timer timer;
	private TimerTask timerTask;

	public TimerHelper(long period) {
		this(0, period);
	}

	public TimerHelper(long delay, long period) {
		this(delay, period, 1);
	}

	public TimerHelper(long period, int times) {
		this(0, period, times);
	}

	public TimerHelper(long delay, long period, int times) {
		this(delay, period, times, false);
	}

	public TimerHelper(long delay, long period, int times, boolean isDaemon) {
		this.delay = delay;
		this.period = period;
		this.times = times;
		this.isDaemon = isDaemon;
	}

	public void cancel() {
		if (timerTask != null) timerTask.cancel();
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
	}

	private TimerTask generateTimerTask(final Runnable runnable, final Timeout timeout, final boolean isUiThread) {
		count = 0;
		return new TimerTask() {
			@Override
			public void run() {
				final int ts = count;
				if (ts <= times) {
					if (isUiThread) {
						new Handler(Looper.getMainLooper()).post(new java.lang.Runnable() {
							@Override
							public void run() {
								runnable.run(ts, TimerHelper.this);
							}
						});
					} else {
						runnable.run(ts, TimerHelper.this);
					}
					count++;
				} else {
					cancel();
					if (timeout != null) {
						if (isUiThread) {
							new Handler(Looper.getMainLooper()).post(new java.lang.Runnable() {
								@Override
								public void run() {
									timeout.run();
								}
							});
						} else {
							timeout.run();
						}
					}
				}
			}
		};
	}

	public void schedule(Runnable runnable) {
		schedule(runnable, null, false);
	}

	public void schedule(Runnable runnable, Timeout timeout) {
		schedule(runnable, timeout, false);
	}

	public void schedule(Runnable runnable, boolean isUiThread) {
		schedule(runnable, null, isUiThread);
	}

	public void schedule(Runnable runnable, Timeout timeout, boolean isUiThread) {
		cancel();
		timer = new Timer(isDaemon);
		if (runnable != null) {
			timerTask = generateTimerTask(runnable, timeout, isUiThread);
			timer.schedule(timerTask, delay, period);
		}
	}

	public void scheduleAtFixedRate(Runnable runnable) {
		scheduleAtFixedRate(runnable, null, false);
	}

	public void scheduleAtFixedRate(Runnable runnable, Timeout timeout) {
		scheduleAtFixedRate(runnable, timeout, false);
	}

	public void scheduleAtFixedRate(Runnable runnable, boolean isUiThread) {
		scheduleAtFixedRate(runnable, null, isUiThread);
	}

	public void scheduleAtFixedRate(Runnable runnable, Timeout timeout, boolean isUiThread) {
		cancel();
		timer = new Timer(isDaemon);
		if (runnable != null) {
			timerTask = generateTimerTask(runnable, timeout, isUiThread);
			timer.scheduleAtFixedRate(timerTask, delay, period);
		}
	}

	public interface Runnable {
		void run(int times, TimerHelper timerHelper);
	}

	public interface Timeout {
		void run();
	}

}
