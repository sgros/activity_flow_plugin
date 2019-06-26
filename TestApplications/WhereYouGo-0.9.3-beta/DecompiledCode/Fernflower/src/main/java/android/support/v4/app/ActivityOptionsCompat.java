package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;
import android.view.View;

public class ActivityOptionsCompat {
   public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
   public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

   protected ActivityOptionsCompat() {
   }

   public static ActivityOptionsCompat makeBasic() {
      Object var0;
      if (VERSION.SDK_INT >= 24) {
         var0 = new ActivityOptionsCompat.ActivityOptionsImpl24(ActivityOptionsCompat24.makeBasic());
      } else if (VERSION.SDK_INT >= 23) {
         var0 = new ActivityOptionsCompat.ActivityOptionsImpl23(ActivityOptionsCompat23.makeBasic());
      } else {
         var0 = new ActivityOptionsCompat();
      }

      return (ActivityOptionsCompat)var0;
   }

   public static ActivityOptionsCompat makeClipRevealAnimation(View var0, int var1, int var2, int var3, int var4) {
      Object var5;
      if (VERSION.SDK_INT >= 24) {
         var5 = new ActivityOptionsCompat.ActivityOptionsImpl24(ActivityOptionsCompat24.makeClipRevealAnimation(var0, var1, var2, var3, var4));
      } else if (VERSION.SDK_INT >= 23) {
         var5 = new ActivityOptionsCompat.ActivityOptionsImpl23(ActivityOptionsCompat23.makeClipRevealAnimation(var0, var1, var2, var3, var4));
      } else {
         var5 = new ActivityOptionsCompat();
      }

      return (ActivityOptionsCompat)var5;
   }

   public static ActivityOptionsCompat makeCustomAnimation(Context var0, int var1, int var2) {
      Object var3;
      if (VERSION.SDK_INT >= 24) {
         var3 = new ActivityOptionsCompat.ActivityOptionsImpl24(ActivityOptionsCompat24.makeCustomAnimation(var0, var1, var2));
      } else if (VERSION.SDK_INT >= 23) {
         var3 = new ActivityOptionsCompat.ActivityOptionsImpl23(ActivityOptionsCompat23.makeCustomAnimation(var0, var1, var2));
      } else if (VERSION.SDK_INT >= 21) {
         var3 = new ActivityOptionsCompat.ActivityOptionsImpl21(ActivityOptionsCompat21.makeCustomAnimation(var0, var1, var2));
      } else if (VERSION.SDK_INT >= 16) {
         var3 = new ActivityOptionsCompat.ActivityOptionsImplJB(ActivityOptionsCompatJB.makeCustomAnimation(var0, var1, var2));
      } else {
         var3 = new ActivityOptionsCompat();
      }

      return (ActivityOptionsCompat)var3;
   }

   public static ActivityOptionsCompat makeScaleUpAnimation(View var0, int var1, int var2, int var3, int var4) {
      Object var5;
      if (VERSION.SDK_INT >= 24) {
         var5 = new ActivityOptionsCompat.ActivityOptionsImpl24(ActivityOptionsCompat24.makeScaleUpAnimation(var0, var1, var2, var3, var4));
      } else if (VERSION.SDK_INT >= 23) {
         var5 = new ActivityOptionsCompat.ActivityOptionsImpl23(ActivityOptionsCompat23.makeScaleUpAnimation(var0, var1, var2, var3, var4));
      } else if (VERSION.SDK_INT >= 21) {
         var5 = new ActivityOptionsCompat.ActivityOptionsImpl21(ActivityOptionsCompat21.makeScaleUpAnimation(var0, var1, var2, var3, var4));
      } else if (VERSION.SDK_INT >= 16) {
         var5 = new ActivityOptionsCompat.ActivityOptionsImplJB(ActivityOptionsCompatJB.makeScaleUpAnimation(var0, var1, var2, var3, var4));
      } else {
         var5 = new ActivityOptionsCompat();
      }

      return (ActivityOptionsCompat)var5;
   }

   public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity var0, View var1, String var2) {
      Object var3;
      if (VERSION.SDK_INT >= 24) {
         var3 = new ActivityOptionsCompat.ActivityOptionsImpl24(ActivityOptionsCompat24.makeSceneTransitionAnimation(var0, var1, var2));
      } else if (VERSION.SDK_INT >= 23) {
         var3 = new ActivityOptionsCompat.ActivityOptionsImpl23(ActivityOptionsCompat23.makeSceneTransitionAnimation(var0, var1, var2));
      } else if (VERSION.SDK_INT >= 21) {
         var3 = new ActivityOptionsCompat.ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(var0, var1, var2));
      } else {
         var3 = new ActivityOptionsCompat();
      }

      return (ActivityOptionsCompat)var3;
   }

   public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity var0, Pair... var1) {
      Object var7;
      if (VERSION.SDK_INT >= 21) {
         View[] var2 = null;
         String[] var3 = null;
         if (var1 != null) {
            View[] var4 = new View[var1.length];
            String[] var5 = new String[var1.length];
            int var6 = 0;

            while(true) {
               var3 = var5;
               var2 = var4;
               if (var6 >= var1.length) {
                  break;
               }

               var4[var6] = (View)var1[var6].first;
               var5[var6] = (String)var1[var6].second;
               ++var6;
            }
         }

         if (VERSION.SDK_INT >= 24) {
            var7 = new ActivityOptionsCompat.ActivityOptionsImpl24(ActivityOptionsCompat24.makeSceneTransitionAnimation(var0, var2, var3));
         } else if (VERSION.SDK_INT >= 23) {
            var7 = new ActivityOptionsCompat.ActivityOptionsImpl23(ActivityOptionsCompat23.makeSceneTransitionAnimation(var0, var2, var3));
         } else {
            var7 = new ActivityOptionsCompat.ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(var0, var2, var3));
         }
      } else {
         var7 = new ActivityOptionsCompat();
      }

      return (ActivityOptionsCompat)var7;
   }

   public static ActivityOptionsCompat makeTaskLaunchBehind() {
      Object var0;
      if (VERSION.SDK_INT >= 24) {
         var0 = new ActivityOptionsCompat.ActivityOptionsImpl24(ActivityOptionsCompat24.makeTaskLaunchBehind());
      } else if (VERSION.SDK_INT >= 23) {
         var0 = new ActivityOptionsCompat.ActivityOptionsImpl23(ActivityOptionsCompat23.makeTaskLaunchBehind());
      } else if (VERSION.SDK_INT >= 21) {
         var0 = new ActivityOptionsCompat.ActivityOptionsImpl21(ActivityOptionsCompat21.makeTaskLaunchBehind());
      } else {
         var0 = new ActivityOptionsCompat();
      }

      return (ActivityOptionsCompat)var0;
   }

   public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View var0, Bitmap var1, int var2, int var3) {
      Object var4;
      if (VERSION.SDK_INT >= 24) {
         var4 = new ActivityOptionsCompat.ActivityOptionsImpl24(ActivityOptionsCompat24.makeThumbnailScaleUpAnimation(var0, var1, var2, var3));
      } else if (VERSION.SDK_INT >= 23) {
         var4 = new ActivityOptionsCompat.ActivityOptionsImpl23(ActivityOptionsCompat23.makeThumbnailScaleUpAnimation(var0, var1, var2, var3));
      } else if (VERSION.SDK_INT >= 21) {
         var4 = new ActivityOptionsCompat.ActivityOptionsImpl21(ActivityOptionsCompat21.makeThumbnailScaleUpAnimation(var0, var1, var2, var3));
      } else if (VERSION.SDK_INT >= 16) {
         var4 = new ActivityOptionsCompat.ActivityOptionsImplJB(ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(var0, var1, var2, var3));
      } else {
         var4 = new ActivityOptionsCompat();
      }

      return (ActivityOptionsCompat)var4;
   }

   @Nullable
   public Rect getLaunchBounds() {
      return null;
   }

   public void requestUsageTimeReport(PendingIntent var1) {
   }

   public ActivityOptionsCompat setLaunchBounds(@Nullable Rect var1) {
      return null;
   }

   public Bundle toBundle() {
      return null;
   }

   public void update(ActivityOptionsCompat var1) {
   }

   @TargetApi(21)
   @RequiresApi(21)
   private static class ActivityOptionsImpl21 extends ActivityOptionsCompat {
      private final ActivityOptionsCompat21 mImpl;

      ActivityOptionsImpl21(ActivityOptionsCompat21 var1) {
         this.mImpl = var1;
      }

      public Bundle toBundle() {
         return this.mImpl.toBundle();
      }

      public void update(ActivityOptionsCompat var1) {
         if (var1 instanceof ActivityOptionsCompat.ActivityOptionsImpl21) {
            ActivityOptionsCompat.ActivityOptionsImpl21 var2 = (ActivityOptionsCompat.ActivityOptionsImpl21)var1;
            this.mImpl.update(var2.mImpl);
         }

      }
   }

   @TargetApi(23)
   @RequiresApi(23)
   private static class ActivityOptionsImpl23 extends ActivityOptionsCompat {
      private final ActivityOptionsCompat23 mImpl;

      ActivityOptionsImpl23(ActivityOptionsCompat23 var1) {
         this.mImpl = var1;
      }

      public void requestUsageTimeReport(PendingIntent var1) {
         this.mImpl.requestUsageTimeReport(var1);
      }

      public Bundle toBundle() {
         return this.mImpl.toBundle();
      }

      public void update(ActivityOptionsCompat var1) {
         if (var1 instanceof ActivityOptionsCompat.ActivityOptionsImpl23) {
            ActivityOptionsCompat.ActivityOptionsImpl23 var2 = (ActivityOptionsCompat.ActivityOptionsImpl23)var1;
            this.mImpl.update(var2.mImpl);
         }

      }
   }

   @TargetApi(24)
   @RequiresApi(24)
   private static class ActivityOptionsImpl24 extends ActivityOptionsCompat {
      private final ActivityOptionsCompat24 mImpl;

      ActivityOptionsImpl24(ActivityOptionsCompat24 var1) {
         this.mImpl = var1;
      }

      public Rect getLaunchBounds() {
         return this.mImpl.getLaunchBounds();
      }

      public void requestUsageTimeReport(PendingIntent var1) {
         this.mImpl.requestUsageTimeReport(var1);
      }

      public ActivityOptionsCompat setLaunchBounds(@Nullable Rect var1) {
         return new ActivityOptionsCompat.ActivityOptionsImpl24(this.mImpl.setLaunchBounds(var1));
      }

      public Bundle toBundle() {
         return this.mImpl.toBundle();
      }

      public void update(ActivityOptionsCompat var1) {
         if (var1 instanceof ActivityOptionsCompat.ActivityOptionsImpl24) {
            ActivityOptionsCompat.ActivityOptionsImpl24 var2 = (ActivityOptionsCompat.ActivityOptionsImpl24)var1;
            this.mImpl.update(var2.mImpl);
         }

      }
   }

   @TargetApi(16)
   @RequiresApi(16)
   private static class ActivityOptionsImplJB extends ActivityOptionsCompat {
      private final ActivityOptionsCompatJB mImpl;

      ActivityOptionsImplJB(ActivityOptionsCompatJB var1) {
         this.mImpl = var1;
      }

      public Bundle toBundle() {
         return this.mImpl.toBundle();
      }

      public void update(ActivityOptionsCompat var1) {
         if (var1 instanceof ActivityOptionsCompat.ActivityOptionsImplJB) {
            ActivityOptionsCompat.ActivityOptionsImplJB var2 = (ActivityOptionsCompat.ActivityOptionsImplJB)var1;
            this.mImpl.update(var2.mImpl);
         }

      }
   }
}
