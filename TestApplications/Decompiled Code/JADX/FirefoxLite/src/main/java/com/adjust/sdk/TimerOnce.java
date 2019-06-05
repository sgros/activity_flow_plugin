package com.adjust.sdk;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimerOnce {
    private Runnable command;
    private CustomScheduledExecutor executor;
    private ILogger logger = AdjustFactory.getLogger();
    private String name;
    private ScheduledFuture waitingTask;

    /* renamed from: com.adjust.sdk.TimerOnce$1 */
    class C03501 implements Runnable {
        C03501() {
        }

        public void run() {
            TimerOnce.this.logger.verbose("%s fired", TimerOnce.this.name);
            TimerOnce.this.command.run();
            TimerOnce.this.waitingTask = null;
        }
    }

    public TimerOnce(Runnable runnable, String str) {
        this.name = str;
        this.executor = new CustomScheduledExecutor(str, true);
        this.command = runnable;
    }

    public void startIn(long j) {
        cancel(false);
        String format = Util.SecondsDisplayFormat.format(((double) j) / 1000.0d);
        this.logger.verbose("%s starting. Launching in %s seconds", this.name, format);
        this.waitingTask = this.executor.schedule(new C03501(), j, TimeUnit.MILLISECONDS);
    }

    public long getFireIn() {
        if (this.waitingTask == null) {
            return 0;
        }
        return this.waitingTask.getDelay(TimeUnit.MILLISECONDS);
    }

    private void cancel(boolean z) {
        if (this.waitingTask != null) {
            this.waitingTask.cancel(z);
        }
        this.waitingTask = null;
        this.logger.verbose("%s canceled", this.name);
    }

    public void cancel() {
        cancel(false);
    }

    public void teardown() {
        cancel(true);
        this.executor = null;
    }
}
