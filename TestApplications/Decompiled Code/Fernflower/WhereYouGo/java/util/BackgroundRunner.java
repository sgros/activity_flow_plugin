package util;

import java.util.Vector;

public class BackgroundRunner extends Thread {
   private static BackgroundRunner instance;
   private boolean end = false;
   private boolean paused = false;
   private Vector queue = new Vector();
   private Runnable queueProcessedListener = null;

   public BackgroundRunner() {
      this.start();
   }

   public BackgroundRunner(boolean var1) {
      this.paused = var1;
      this.start();
   }

   public static BackgroundRunner getInstance() {
      if (instance == null) {
         instance = new BackgroundRunner();
      }

      return instance;
   }

   public static void performTask(Runnable var0) {
      getInstance().perform(var0);
   }

   public void kill() {
      synchronized(this){}

      try {
         this.end = true;
         this.notify();
      } finally {
         ;
      }

   }

   public void pause() {
      synchronized(this){}

      try {
         this.paused = true;
      } finally {
         ;
      }

   }

   public void perform(Runnable var1) {
      synchronized(this){}

      try {
         this.queue.addElement(var1);
         this.notify();
      } finally {
         ;
      }

   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void setQueueListener(Runnable var1) {
      this.queueProcessedListener = var1;
   }

   public void unpause() {
      synchronized(this){}

      try {
         this.paused = false;
         this.notify();
      } finally {
         ;
      }

   }
}
