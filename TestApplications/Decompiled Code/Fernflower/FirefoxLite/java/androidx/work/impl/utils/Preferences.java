package androidx.work.impl.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
   private Context mContext;
   private SharedPreferences mSharedPreferences;

   public Preferences(Context var1) {
      this.mContext = var1;
   }

   private SharedPreferences getSharedPreferences() {
      synchronized(Preferences.class){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.mSharedPreferences == null) {
               this.mSharedPreferences = this.mContext.getSharedPreferences("androidx.work.util.preferences", 0);
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            SharedPreferences var14 = this.mSharedPreferences;
            return var14;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean needsReschedule() {
      return this.getSharedPreferences().getBoolean("reschedule_needed", false);
   }

   public void setNeedsReschedule(boolean var1) {
      this.getSharedPreferences().edit().putBoolean("reschedule_needed", var1).apply();
   }
}
