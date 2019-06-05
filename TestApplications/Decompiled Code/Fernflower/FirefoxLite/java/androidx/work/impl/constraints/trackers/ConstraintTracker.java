package androidx.work.impl.constraints.trackers;

import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.constraints.ConstraintListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class ConstraintTracker {
   private static final String TAG = Logger.tagWithPrefix("ConstraintTracker");
   protected final Context mAppContext;
   private Object mCurrentState;
   private final Set mListeners = new LinkedHashSet();
   private final Object mLock = new Object();

   ConstraintTracker(Context var1) {
      this.mAppContext = var1.getApplicationContext();
   }

   public void addListener(ConstraintListener var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label197: {
         label196: {
            try {
               if (!this.mListeners.add(var1)) {
                  break label196;
               }

               if (this.mListeners.size() == 1) {
                  this.mCurrentState = this.getInitialState();
                  Logger.get().debug(TAG, String.format("%s: initial state = %s", this.getClass().getSimpleName(), this.mCurrentState));
                  this.startTracking();
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label197;
            }

            try {
               var1.onConstraintChanged(this.mCurrentState);
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label197;
            }
         }

         label189:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label189;
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

   public abstract Object getInitialState();

   public void removeListener(ConstraintListener var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label130: {
         try {
            if (this.mListeners.remove(var1) && this.mListeners.isEmpty()) {
               this.stopTracking();
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

   public void setState(Object var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label428: {
         label430: {
            try {
               if (this.mCurrentState != var1 && (this.mCurrentState == null || !this.mCurrentState.equals(var1))) {
                  break label430;
               }
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label428;
            }

            try {
               return;
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label428;
            }
         }

         Iterator var46;
         try {
            this.mCurrentState = var1;
            ArrayList var45 = new ArrayList(this.mListeners);
            var46 = var45.iterator();
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label428;
         }

         while(true) {
            try {
               if (!var46.hasNext()) {
                  break;
               }

               ((ConstraintListener)var46.next()).onConstraintChanged(this.mCurrentState);
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label428;
            }
         }

         label400:
         try {
            return;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            break label400;
         }
      }

      while(true) {
         Throwable var47 = var10000;

         try {
            throw var47;
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            continue;
         }
      }
   }

   public abstract void startTracking();

   public abstract void stopTracking();
}
