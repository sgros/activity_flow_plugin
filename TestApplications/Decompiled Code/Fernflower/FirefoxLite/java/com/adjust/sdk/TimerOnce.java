package com.adjust.sdk;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimerOnce {
   private Runnable command;
   private CustomScheduledExecutor executor;
   private ILogger logger;
   private String name;
   private ScheduledFuture waitingTask;

   public TimerOnce(Runnable var1, String var2) {
      this.name = var2;
      this.executor = new CustomScheduledExecutor(var2, true);
      this.command = var1;
      this.logger = AdjustFactory.getLogger();
   }

   private void cancel(boolean var1) {
      if (this.waitingTask != null) {
         this.waitingTask.cancel(var1);
      }

      this.waitingTask = null;
      this.logger.verbose("%s canceled", this.name);
   }

   public void cancel() {
      this.cancel(false);
   }

   public long getFireIn() {
      return this.waitingTask == null ? 0L : this.waitingTask.getDelay(TimeUnit.MILLISECONDS);
   }

   public void startIn(long var1) {
      this.cancel(false);
      String var3 = Util.SecondsDisplayFormat.format((double)var1 / 1000.0D);
      this.logger.verbose("%s starting. Launching in %s seconds", this.name, var3);
      this.waitingTask = this.executor.schedule(new Runnable() {
         public void run() {
            TimerOnce.this.logger.verbose("%s fired", TimerOnce.this.name);
            TimerOnce.this.command.run();
            TimerOnce.this.waitingTask = null;
         }
      }, var1, TimeUnit.MILLISECONDS);
   }

   public void teardown() {
      this.cancel(true);
      this.executor = null;
   }
}
