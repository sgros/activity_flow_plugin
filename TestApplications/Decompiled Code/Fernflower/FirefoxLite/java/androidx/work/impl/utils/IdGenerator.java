package androidx.work.impl.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class IdGenerator {
   private final Context mContext;
   private boolean mLoadedPreferences;
   private SharedPreferences mSharedPrefs;

   public IdGenerator(Context var1) {
      this.mContext = var1;
   }

   private void loadPreferencesIfNecessary() {
      if (!this.mLoadedPreferences) {
         this.mSharedPrefs = this.mContext.getSharedPreferences("androidx.work.util.id", 0);
         this.mLoadedPreferences = true;
      }

   }

   private int nextId(String var1) {
      SharedPreferences var2 = this.mSharedPrefs;
      int var3 = 0;
      int var4 = var2.getInt(var1, 0);
      if (var4 != Integer.MAX_VALUE) {
         var3 = var4 + 1;
      }

      this.update(var1, var3);
      return var4;
   }

   private void update(String var1, int var2) {
      this.mSharedPrefs.edit().putInt(var1, var2).apply();
   }

   public int nextAlarmManagerId() {
      // $FF: Couldn't be decompiled
   }

   public int nextJobSchedulerIdWithRange(int var1, int var2) {
      synchronized(IdGenerator.class){}

      Throwable var10000;
      boolean var10001;
      label203: {
         int var3;
         try {
            this.loadPreferencesIfNecessary();
            var3 = this.nextId("next_job_scheduler_id");
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label203;
         }

         if (var3 >= var1 && var3 <= var2) {
            var1 = var3;
         } else {
            try {
               this.update("next_job_scheduler_id", var1 + 1);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label203;
            }
         }

         label188:
         try {
            return var1;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label188;
         }
      }

      while(true) {
         Throwable var4 = var10000;

         try {
            throw var4;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }
}
