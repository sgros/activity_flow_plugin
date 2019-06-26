package androidx.core.app;

import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;
import android.os.Build.VERSION;

public class ActivityOptionsCompat {
   protected ActivityOptionsCompat() {
   }

   public static ActivityOptionsCompat makeCustomAnimation(Context var0, int var1, int var2) {
      return (ActivityOptionsCompat)(VERSION.SDK_INT >= 16 ? new ActivityOptionsCompat.ActivityOptionsCompatImpl(ActivityOptions.makeCustomAnimation(var0, var1, var2)) : new ActivityOptionsCompat());
   }

   public Bundle toBundle() {
      return null;
   }

   private static class ActivityOptionsCompatImpl extends ActivityOptionsCompat {
      private final ActivityOptions mActivityOptions;

      ActivityOptionsCompatImpl(ActivityOptions var1) {
         this.mActivityOptions = var1;
      }

      public Bundle toBundle() {
         return this.mActivityOptions.toBundle();
      }
   }
}
