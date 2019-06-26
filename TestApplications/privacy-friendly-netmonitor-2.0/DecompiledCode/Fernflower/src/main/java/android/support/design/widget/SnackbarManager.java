package android.support.design.widget;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

class SnackbarManager {
   private static final int LONG_DURATION_MS = 2750;
   static final int MSG_TIMEOUT = 0;
   private static final int SHORT_DURATION_MS = 1500;
   private static SnackbarManager sSnackbarManager;
   private SnackbarManager.SnackbarRecord mCurrentSnackbar;
   private final Handler mHandler = new Handler(Looper.getMainLooper(), new android.os.Handler.Callback() {
      public boolean handleMessage(Message var1) {
         if (var1.what != 0) {
            return false;
         } else {
            SnackbarManager.this.handleTimeout((SnackbarManager.SnackbarRecord)var1.obj);
            return true;
         }
      }
   });
   private final Object mLock = new Object();
   private SnackbarManager.SnackbarRecord mNextSnackbar;

   private SnackbarManager() {
   }

   private boolean cancelSnackbarLocked(SnackbarManager.SnackbarRecord var1, int var2) {
      SnackbarManager.Callback var3 = (SnackbarManager.Callback)var1.callback.get();
      if (var3 != null) {
         this.mHandler.removeCallbacksAndMessages(var1);
         var3.dismiss(var2);
         return true;
      } else {
         return false;
      }
   }

   static SnackbarManager getInstance() {
      if (sSnackbarManager == null) {
         sSnackbarManager = new SnackbarManager();
      }

      return sSnackbarManager;
   }

   private boolean isCurrentSnackbarLocked(SnackbarManager.Callback var1) {
      boolean var2;
      if (this.mCurrentSnackbar != null && this.mCurrentSnackbar.isSnackbar(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean isNextSnackbarLocked(SnackbarManager.Callback var1) {
      boolean var2;
      if (this.mNextSnackbar != null && this.mNextSnackbar.isSnackbar(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void scheduleTimeoutLocked(SnackbarManager.SnackbarRecord var1) {
      if (var1.duration != -2) {
         int var2 = 2750;
         if (var1.duration > 0) {
            var2 = var1.duration;
         } else if (var1.duration == -1) {
            var2 = 1500;
         }

         this.mHandler.removeCallbacksAndMessages(var1);
         this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, 0, var1), (long)var2);
      }
   }

   private void showNextSnackbarLocked() {
      if (this.mNextSnackbar != null) {
         this.mCurrentSnackbar = this.mNextSnackbar;
         this.mNextSnackbar = null;
         SnackbarManager.Callback var1 = (SnackbarManager.Callback)this.mCurrentSnackbar.callback.get();
         if (var1 != null) {
            var1.show();
         } else {
            this.mCurrentSnackbar = null;
         }
      }

   }

   public void dismiss(SnackbarManager.Callback var1, int var2) {
      Object var3 = this.mLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label200: {
         label204: {
            try {
               if (this.isCurrentSnackbarLocked(var1)) {
                  this.cancelSnackbarLocked(this.mCurrentSnackbar, var2);
                  break label204;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label200;
            }

            try {
               if (this.isNextSnackbarLocked(var1)) {
                  this.cancelSnackbarLocked(this.mNextSnackbar, var2);
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label200;
            }
         }

         label190:
         try {
            return;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label190;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   void handleTimeout(SnackbarManager.SnackbarRecord var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label183: {
         label182: {
            try {
               if (this.mCurrentSnackbar != var1 && this.mNextSnackbar != var1) {
                  break label182;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label183;
            }

            try {
               this.cancelSnackbarLocked(var1, 2);
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label183;
            }
         }

         label173:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label173;
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isCurrent(SnackbarManager.Callback param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean isCurrentOrNext(SnackbarManager.Callback var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label151: {
         boolean var3;
         label150: {
            label149: {
               try {
                  if (!this.isCurrentSnackbarLocked(var1) && !this.isNextSnackbarLocked(var1)) {
                     break label149;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label151;
               }

               var3 = true;
               break label150;
            }

            var3 = false;
         }

         label140:
         try {
            return var3;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label140;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public void onDismissed(SnackbarManager.Callback var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label130: {
         try {
            if (this.isCurrentSnackbarLocked(var1)) {
               this.mCurrentSnackbar = null;
               if (this.mNextSnackbar != null) {
                  this.showNextSnackbarLocked();
               }
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label130;
         }

         label127:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label127;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void onShown(SnackbarManager.Callback var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.isCurrentSnackbarLocked(var1)) {
               this.scheduleTimeoutLocked(this.mCurrentSnackbar);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void pauseTimeout(SnackbarManager.Callback var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label130: {
         try {
            if (this.isCurrentSnackbarLocked(var1) && !this.mCurrentSnackbar.paused) {
               this.mCurrentSnackbar.paused = true;
               this.mHandler.removeCallbacksAndMessages(this.mCurrentSnackbar);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label130;
         }

         label127:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label127;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void restoreTimeoutIfPaused(SnackbarManager.Callback var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label130: {
         try {
            if (this.isCurrentSnackbarLocked(var1) && this.mCurrentSnackbar.paused) {
               this.mCurrentSnackbar.paused = false;
               this.scheduleTimeoutLocked(this.mCurrentSnackbar);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label130;
         }

         label127:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label127;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void show(int var1, SnackbarManager.Callback var2) {
      Object var3 = this.mLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label405: {
         try {
            if (this.isCurrentSnackbarLocked(var2)) {
               this.mCurrentSnackbar.duration = var1;
               this.mHandler.removeCallbacksAndMessages(this.mCurrentSnackbar);
               this.scheduleTimeoutLocked(this.mCurrentSnackbar);
               return;
            }
         } catch (Throwable var46) {
            var10000 = var46;
            var10001 = false;
            break label405;
         }

         label406: {
            try {
               if (this.isNextSnackbarLocked(var2)) {
                  this.mNextSnackbar.duration = var1;
                  break label406;
               }
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label405;
            }

            try {
               SnackbarManager.SnackbarRecord var4 = new SnackbarManager.SnackbarRecord(var1, var2);
               this.mNextSnackbar = var4;
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label405;
            }
         }

         try {
            if (this.mCurrentSnackbar != null && this.cancelSnackbarLocked(this.mCurrentSnackbar, 4)) {
               return;
            }
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label405;
         }

         label385:
         try {
            this.mCurrentSnackbar = null;
            this.showNextSnackbarLocked();
            return;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label385;
         }
      }

      while(true) {
         Throwable var47 = var10000;

         try {
            throw var47;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            continue;
         }
      }
   }

   interface Callback {
      void dismiss(int var1);

      void show();
   }

   private static class SnackbarRecord {
      final WeakReference callback;
      int duration;
      boolean paused;

      SnackbarRecord(int var1, SnackbarManager.Callback var2) {
         this.callback = new WeakReference(var2);
         this.duration = var1;
      }

      boolean isSnackbar(SnackbarManager.Callback var1) {
         boolean var2;
         if (var1 != null && this.callback.get() == var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }
}
