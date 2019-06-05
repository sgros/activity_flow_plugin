package com.adjust.sdk;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimerCycle {
   private Runnable command;
   private long cycleDelay;
   private CustomScheduledExecutor executor;
   private long initialDelay;
   private boolean isPaused;
   private ILogger logger;
   private String name;
   private ScheduledFuture waitingTask;

   public TimerCycle(Runnable var1, long var2, long var4, String var6) {
      this.executor = new CustomScheduledExecutor(var6, true);
      this.name = var6;
      this.command = var1;
      this.initialDelay = var2;
      this.cycleDelay = var4;
      this.isPaused = true;
      this.logger = AdjustFactory.getLogger();
      String var8 = Util.SecondsDisplayFormat.format((double)var4 / 1000.0D);
      String var7 = Util.SecondsDisplayFormat.format((double)var2 / 1000.0D);
      this.logger.verbose("%s configured to fire after %s seconds of starting and cycles every %s seconds", var6, var7, var8);
   }

   private void cancel(boolean var1) {
      if (this.waitingTask != null) {
         this.waitingTask.cancel(var1);
      }

      this.waitingTask = null;
   }

   public void start() {
      if (!this.isPaused) {
         this.logger.verbose("%s is already started", this.name);
      } else {
         this.logger.verbose("%s starting", this.name);
         this.waitingTask = this.executor.scheduleWithFixedDelay(new Runnable() {
            public void run() {
               TimerCycle.this.logger.verbose("%s fired", TimerCycle.this.name);
               TimerCycle.this.command.run();
            }
         }, this.initialDelay, this.cycleDelay, TimeUnit.MILLISECONDS);
         this.isPaused = false;
      }
   }

   public void suspend() {
      if (this.isPaused) {
         this.logger.verbose("%s is already suspended", this.name);
      } else {
         this.initialDelay = this.waitingTask.getDelay(TimeUnit.MILLISECONDS);
         this.waitingTask.cancel(false);
         String var1 = Util.SecondsDisplayFormat.format((double)this.initialDelay / 1000.0D);
         this.logger.verbose("%s suspended with %s seconds left", this.name, var1);
         this.isPaused = true;
      }
   }

   public void teardown() {
      this.cancel(true);
      if (this.executor != null) {
         try {
            this.executor.shutdownNow();
         } catch (SecurityException var2) {
         }
      }

      this.executor = null;
   }
}
