package com.adjust.sdk;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimerCycle {
    private Runnable command;
    private long cycleDelay;
    private CustomScheduledExecutor executor;
    private long initialDelay;
    private boolean isPaused = true;
    private ILogger logger = AdjustFactory.getLogger();
    private String name;
    private ScheduledFuture waitingTask;

    /* renamed from: com.adjust.sdk.TimerCycle$1 */
    class C03491 implements Runnable {
        C03491() {
        }

        public void run() {
            TimerCycle.this.logger.verbose("%s fired", TimerCycle.this.name);
            TimerCycle.this.command.run();
        }
    }

    public TimerCycle(Runnable runnable, long j, long j2, String str) {
        this.executor = new CustomScheduledExecutor(str, true);
        this.name = str;
        this.command = runnable;
        this.initialDelay = j;
        this.cycleDelay = j2;
        String format = Util.SecondsDisplayFormat.format(((double) j2) / 1000.0d);
        String format2 = Util.SecondsDisplayFormat.format(((double) j) / 1000.0d);
        this.logger.verbose("%s configured to fire after %s seconds of starting and cycles every %s seconds", str, format2, format);
    }

    public void start() {
        if (this.isPaused) {
            this.logger.verbose("%s starting", this.name);
            this.waitingTask = this.executor.scheduleWithFixedDelay(new C03491(), this.initialDelay, this.cycleDelay, TimeUnit.MILLISECONDS);
            this.isPaused = false;
            return;
        }
        this.logger.verbose("%s is already started", this.name);
    }

    public void suspend() {
        if (this.isPaused) {
            this.logger.verbose("%s is already suspended", this.name);
            return;
        }
        this.initialDelay = this.waitingTask.getDelay(TimeUnit.MILLISECONDS);
        this.waitingTask.cancel(false);
        String format = Util.SecondsDisplayFormat.format(((double) this.initialDelay) / 1000.0d);
        this.logger.verbose("%s suspended with %s seconds left", this.name, format);
        this.isPaused = true;
    }

    private void cancel(boolean z) {
        if (this.waitingTask != null) {
            this.waitingTask.cancel(z);
        }
        this.waitingTask = null;
    }

    public void teardown() {
        cancel(true);
        if (this.executor != null) {
            try {
                this.executor.shutdownNow();
            } catch (SecurityException unused) {
            }
        }
        this.executor = null;
    }
}
