package androidx.work.impl.constraints;

import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.constraints.controllers.BatteryChargingController;
import androidx.work.impl.constraints.controllers.BatteryNotLowController;
import androidx.work.impl.constraints.controllers.ConstraintController;
import androidx.work.impl.constraints.controllers.NetworkConnectedController;
import androidx.work.impl.constraints.controllers.NetworkMeteredController;
import androidx.work.impl.constraints.controllers.NetworkNotRoamingController;
import androidx.work.impl.constraints.controllers.NetworkUnmeteredController;
import androidx.work.impl.constraints.controllers.StorageNotLowController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkConstraintsTracker implements ConstraintController.OnConstraintUpdatedCallback {
   private static final String TAG = Logger.tagWithPrefix("WorkConstraintsTracker");
   private final WorkConstraintsCallback mCallback;
   private final ConstraintController[] mConstraintControllers;
   private final Object mLock;

   public WorkConstraintsTracker(Context var1, WorkConstraintsCallback var2) {
      var1 = var1.getApplicationContext();
      this.mCallback = var2;
      this.mConstraintControllers = new ConstraintController[]{new BatteryChargingController(var1), new BatteryNotLowController(var1), new StorageNotLowController(var1), new NetworkConnectedController(var1), new NetworkUnmeteredController(var1), new NetworkNotRoamingController(var1), new NetworkMeteredController(var1)};
      this.mLock = new Object();
   }

   public boolean areAllConstraintsMet(String var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label241: {
         ConstraintController[] var3;
         int var4;
         try {
            var3 = this.mConstraintControllers;
            var4 = var3.length;
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label241;
         }

         int var5 = 0;

         while(true) {
            if (var5 >= var4) {
               try {
                  return true;
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break;
               }
            }

            ConstraintController var6 = var3[var5];

            try {
               if (var6.isWorkSpecConstrained(var1)) {
                  Logger.get().debug(TAG, String.format("Work %s constrained by %s", var1, var6.getClass().getSimpleName()));
                  return false;
               }
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break;
            }

            ++var5;
         }
      }

      while(true) {
         Throwable var27 = var10000;

         try {
            throw var27;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            continue;
         }
      }
   }

   public void onConstraintMet(List var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label326: {
         ArrayList var3;
         Iterator var4;
         try {
            var3 = new ArrayList();
            var4 = var1.iterator();
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label326;
         }

         while(true) {
            try {
               if (!var4.hasNext()) {
                  break;
               }

               String var35 = (String)var4.next();
               if (this.areAllConstraintsMet(var35)) {
                  Logger.get().debug(TAG, String.format("Constraints met for %s", var35));
                  var3.add(var35);
               }
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label326;
            }
         }

         try {
            if (this.mCallback != null) {
               this.mCallback.onAllConstraintsMet(var3);
            }
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label326;
         }

         label306:
         try {
            return;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label306;
         }
      }

      while(true) {
         Throwable var36 = var10000;

         try {
            throw var36;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public void onConstraintNotMet(List var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.mCallback != null) {
               this.mCallback.onAllConstraintsNotMet(var1);
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

   public void replace(List var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label745: {
         ConstraintController[] var3;
         int var4;
         try {
            var3 = this.mConstraintControllers;
            var4 = var3.length;
         } catch (Throwable var78) {
            var10000 = var78;
            var10001 = false;
            break label745;
         }

         byte var5 = 0;

         int var6;
         for(var6 = 0; var6 < var4; ++var6) {
            try {
               var3[var6].setCallback((ConstraintController.OnConstraintUpdatedCallback)null);
            } catch (Throwable var77) {
               var10000 = var77;
               var10001 = false;
               break label745;
            }
         }

         try {
            var3 = this.mConstraintControllers;
            var4 = var3.length;
         } catch (Throwable var76) {
            var10000 = var76;
            var10001 = false;
            break label745;
         }

         for(var6 = 0; var6 < var4; ++var6) {
            try {
               var3[var6].replace(var1);
            } catch (Throwable var75) {
               var10000 = var75;
               var10001 = false;
               break label745;
            }
         }

         ConstraintController[] var79;
         try {
            var79 = this.mConstraintControllers;
            var4 = var79.length;
         } catch (Throwable var74) {
            var10000 = var74;
            var10001 = false;
            break label745;
         }

         for(var6 = var5; var6 < var4; ++var6) {
            try {
               var79[var6].setCallback(this);
            } catch (Throwable var73) {
               var10000 = var73;
               var10001 = false;
               break label745;
            }
         }

         label705:
         try {
            return;
         } catch (Throwable var72) {
            var10000 = var72;
            var10001 = false;
            break label705;
         }
      }

      while(true) {
         Throwable var80 = var10000;

         try {
            throw var80;
         } catch (Throwable var71) {
            var10000 = var71;
            var10001 = false;
            continue;
         }
      }
   }

   public void reset() {
      Object var1 = this.mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label215: {
         ConstraintController[] var2;
         int var3;
         try {
            var2 = this.mConstraintControllers;
            var3 = var2.length;
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label215;
         }

         for(int var4 = 0; var4 < var3; ++var4) {
            try {
               var2[var4].reset();
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label215;
            }
         }

         label199:
         try {
            return;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label199;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }
}
