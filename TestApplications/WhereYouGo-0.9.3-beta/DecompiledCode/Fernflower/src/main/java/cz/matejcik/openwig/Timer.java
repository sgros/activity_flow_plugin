package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaState;

public class Timer extends EventTable {
   private static final int COUNTDOWN = 0;
   private static final int INTERVAL = 1;
   private static final Double ZERO = new Double(0.0D);
   private static java.util.Timer globalTimer;
   private static JavaFunction start = new JavaFunction() {
      public int call(LuaCallFrame var1, int var2) {
         ((Timer)var1.get(0)).start();
         return 0;
      }
   };
   private static JavaFunction stop = new JavaFunction() {
      public int call(LuaCallFrame var1, int var2) {
         ((Timer)var1.get(0)).stop();
         return 0;
      }
   };
   private static JavaFunction tick = new JavaFunction() {
      public int call(LuaCallFrame var1, int var2) {
         ((Timer)var1.get(0)).callEvent("OnTick", (Object)null);
         return 0;
      }
   };
   private long duration = -1L;
   private long lastTick = 0L;
   private Timer.TimerTask task = null;
   private int type = 0;

   public Timer() {
      if (globalTimer == null) {
         globalTimer = new java.util.Timer();
      }

      this.table.rawset("Start", start);
      this.table.rawset("Stop", stop);
      this.table.rawset("Tick", tick);
   }

   public static void kill() {
      if (globalTimer != null) {
         globalTimer.cancel();
      }

      globalTimer = null;
   }

   public static void register() {
      Engine.instance.savegame.addJavafunc(start);
      Engine.instance.savegame.addJavafunc(stop);
      Engine.instance.savegame.addJavafunc(tick);
   }

   private void start(long var1, boolean var3) {
      this.task = new Timer.TimerTask();
      this.lastTick = System.currentTimeMillis();
      if (var3) {
         this.callEvent("OnStart", (Object)null);
      }

      this.updateRemaining();
      switch(this.type) {
      case 0:
         globalTimer.schedule(this.task, var1);
         break;
      case 1:
         globalTimer.scheduleAtFixedRate(this.task, var1, this.duration);
      }

   }

   public void deserialize(DataInputStream var1) throws IOException {
      boolean var2 = var1.readBoolean();
      this.lastTick = var1.readLong();
      super.deserialize(var1);
      if (var2) {
         if (this.lastTick + this.duration < System.currentTimeMillis()) {
            Engine.callEvent(this, "OnTick", (Object)null);
         } else {
            this.start(this.lastTick + this.duration - System.currentTimeMillis(), false);
         }

         if (this.type == 1) {
            this.start();
         }
      }

   }

   protected String luaTostring() {
      return "a ZTimer instance";
   }

   public void serialize(DataOutputStream var1) throws IOException {
      boolean var2;
      if (this.task != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      var1.writeBoolean(var2);
      var1.writeLong(this.lastTick);
      super.serialize(var1);
   }

   protected void setItem(String var1, Object var2) {
      if ("Type".equals(var1) && var2 instanceof String) {
         var1 = (String)var2;
         int var3 = this.type;
         byte var4;
         if ("Countdown".equals(var1)) {
            var4 = 0;
            var3 = var4;
            if (this.type != 0) {
               var3 = var4;
               if (this.task != null) {
                  this.task.restart = false;
                  var3 = var4;
               }
            }
         } else if ("Interval".equals(var1)) {
            var4 = 1;
            var3 = var4;
            if (1 != this.type) {
               var3 = var4;
               if (this.task != null) {
                  this.task.restart = true;
                  var3 = var4;
               }
            }
         }

         this.type = var3;
      } else if ("Duration".equals(var1) && var2 instanceof Double) {
         long var5 = (long)LuaState.fromDouble(var2);
         this.table.rawset("Remaining", ZERO);
         this.duration = 1000L * var5;
      } else {
         super.setItem(var1, var2);
      }

   }

   public void start() {
      Engine.log("TIME: " + this.name + " start", 1);
      if (this.task == null) {
         if (this.duration == 0L) {
            this.callEvent("OnStart", (Object)null);
            this.callEvent("OnTick", (Object)null);
         } else {
            this.start(this.duration, true);
         }
      }

   }

   public void stop() {
      if (this.task != null) {
         Engine.log("TIME: " + this.name + " stop", 1);
         this.task.cancel();
         this.task = null;
         this.callEvent("OnStop", (Object)null);
      }

   }

   public void tick() {
      Engine.log("TIME: " + this.name + " tick", 1);
      Engine.callEvent(this, "OnTick", (Object)null);
      this.lastTick = System.currentTimeMillis();
      this.updateRemaining();
      if (this.type == 0 && this.task != null) {
         this.task.cancel();
         this.task = null;
      }

      if (this.type == 1 && this.task != null && !this.task.restart) {
         Engine.callEvent(this, "OnStart", (Object)null);
      }

   }

   public void updateRemaining() {
      if (this.task == null) {
         this.table.rawset("Remaining", ZERO);
      } else {
         long var1 = System.currentTimeMillis();
         long var3 = this.duration / 1000L;
         var1 = (var1 - this.lastTick) / 1000L;
         this.table.rawset("Remaining", LuaState.toDouble(var3 - var1));
      }

   }

   private class TimerTask extends java.util.TimerTask {
      public boolean restart;

      private TimerTask() {
         this.restart = false;
      }

      // $FF: synthetic method
      TimerTask(Object var2) {
         this();
      }

      public void run() {
         Timer.this.tick();
         Engine.refreshUI();
         if (this.restart) {
            this.cancel();
            Timer.this.task = null;
            Timer.this.start();
         }

      }
   }
}
