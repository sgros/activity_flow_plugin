package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import java.util.List;
import java.util.Map;

public class ActivityCompat extends ContextCompat {
   protected ActivityCompat() {
   }

   private static ActivityCompatApi21.SharedElementCallback21 createCallback(SharedElementCallback var0) {
      ActivityCompat.SharedElementCallback21Impl var1 = null;
      if (var0 != null) {
         var1 = new ActivityCompat.SharedElementCallback21Impl(var0);
      }

      return var1;
   }

   private static ActivityCompatApi23.SharedElementCallback23 createCallback23(SharedElementCallback var0) {
      ActivityCompat.SharedElementCallback23Impl var1 = null;
      if (var0 != null) {
         var1 = new ActivityCompat.SharedElementCallback23Impl(var0);
      }

      return var1;
   }

   public static void finishAffinity(Activity var0) {
      if (VERSION.SDK_INT >= 16) {
         ActivityCompatJB.finishAffinity(var0);
      } else {
         var0.finish();
      }

   }

   public static void finishAfterTransition(Activity var0) {
      if (VERSION.SDK_INT >= 21) {
         ActivityCompatApi21.finishAfterTransition(var0);
      } else {
         var0.finish();
      }

   }

   @Nullable
   public static Uri getReferrer(Activity var0) {
      Uri var3;
      if (VERSION.SDK_INT >= 22) {
         var3 = ActivityCompatApi22.getReferrer(var0);
      } else {
         Intent var1 = var0.getIntent();
         Uri var2 = (Uri)var1.getParcelableExtra("android.intent.extra.REFERRER");
         var3 = var2;
         if (var2 == null) {
            String var4 = var1.getStringExtra("android.intent.extra.REFERRER_NAME");
            if (var4 != null) {
               var3 = Uri.parse(var4);
            } else {
               var3 = null;
            }
         }
      }

      return var3;
   }

   public static boolean invalidateOptionsMenu(Activity var0) {
      boolean var1;
      if (VERSION.SDK_INT >= 11) {
         ActivityCompatHoneycomb.invalidateOptionsMenu(var0);
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static void postponeEnterTransition(Activity var0) {
      if (VERSION.SDK_INT >= 21) {
         ActivityCompatApi21.postponeEnterTransition(var0);
      }

   }

   public static void requestPermissions(@NonNull final Activity var0, @NonNull final String[] var1, @IntRange(from = 0L) final int var2) {
      if (VERSION.SDK_INT >= 23) {
         ActivityCompatApi23.requestPermissions(var0, var1, var2);
      } else if (var0 instanceof ActivityCompat.OnRequestPermissionsResultCallback) {
         (new Handler(Looper.getMainLooper())).post(new Runnable() {
            public void run() {
               int[] var1x = new int[var1.length];
               PackageManager var2x = var0.getPackageManager();
               String var3 = var0.getPackageName();
               int var4 = var1.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  var1x[var5] = var2x.checkPermission(var1[var5], var3);
               }

               ((ActivityCompat.OnRequestPermissionsResultCallback)var0).onRequestPermissionsResult(var2, var1, var1x);
            }
         });
      }

   }

   public static void setEnterSharedElementCallback(Activity var0, SharedElementCallback var1) {
      if (VERSION.SDK_INT >= 23) {
         ActivityCompatApi23.setEnterSharedElementCallback(var0, createCallback23(var1));
      } else if (VERSION.SDK_INT >= 21) {
         ActivityCompatApi21.setEnterSharedElementCallback(var0, createCallback(var1));
      }

   }

   public static void setExitSharedElementCallback(Activity var0, SharedElementCallback var1) {
      if (VERSION.SDK_INT >= 23) {
         ActivityCompatApi23.setExitSharedElementCallback(var0, createCallback23(var1));
      } else if (VERSION.SDK_INT >= 21) {
         ActivityCompatApi21.setExitSharedElementCallback(var0, createCallback(var1));
      }

   }

   public static boolean shouldShowRequestPermissionRationale(@NonNull Activity var0, @NonNull String var1) {
      boolean var2;
      if (VERSION.SDK_INT >= 23) {
         var2 = ActivityCompatApi23.shouldShowRequestPermissionRationale(var0, var1);
      } else {
         var2 = false;
      }

      return var2;
   }

   public static void startActivityForResult(Activity var0, Intent var1, int var2, @Nullable Bundle var3) {
      if (VERSION.SDK_INT >= 16) {
         ActivityCompatJB.startActivityForResult(var0, var1, var2, var3);
      } else {
         var0.startActivityForResult(var1, var2);
      }

   }

   public static void startIntentSenderForResult(Activity var0, IntentSender var1, int var2, Intent var3, int var4, int var5, int var6, @Nullable Bundle var7) throws SendIntentException {
      if (VERSION.SDK_INT >= 16) {
         ActivityCompatJB.startIntentSenderForResult(var0, var1, var2, var3, var4, var5, var6, var7);
      } else {
         var0.startIntentSenderForResult(var1, var2, var3, var4, var5, var6);
      }

   }

   public static void startPostponedEnterTransition(Activity var0) {
      if (VERSION.SDK_INT >= 21) {
         ActivityCompatApi21.startPostponedEnterTransition(var0);
      }

   }

   public interface OnRequestPermissionsResultCallback {
      void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3);
   }

   private static class SharedElementCallback21Impl extends ActivityCompatApi21.SharedElementCallback21 {
      private SharedElementCallback mCallback;

      public SharedElementCallback21Impl(SharedElementCallback var1) {
         this.mCallback = var1;
      }

      public Parcelable onCaptureSharedElementSnapshot(View var1, Matrix var2, RectF var3) {
         return this.mCallback.onCaptureSharedElementSnapshot(var1, var2, var3);
      }

      public View onCreateSnapshotView(Context var1, Parcelable var2) {
         return this.mCallback.onCreateSnapshotView(var1, var2);
      }

      public void onMapSharedElements(List var1, Map var2) {
         this.mCallback.onMapSharedElements(var1, var2);
      }

      public void onRejectSharedElements(List var1) {
         this.mCallback.onRejectSharedElements(var1);
      }

      public void onSharedElementEnd(List var1, List var2, List var3) {
         this.mCallback.onSharedElementEnd(var1, var2, var3);
      }

      public void onSharedElementStart(List var1, List var2, List var3) {
         this.mCallback.onSharedElementStart(var1, var2, var3);
      }
   }

   private static class SharedElementCallback23Impl extends ActivityCompatApi23.SharedElementCallback23 {
      private SharedElementCallback mCallback;

      public SharedElementCallback23Impl(SharedElementCallback var1) {
         this.mCallback = var1;
      }

      public Parcelable onCaptureSharedElementSnapshot(View var1, Matrix var2, RectF var3) {
         return this.mCallback.onCaptureSharedElementSnapshot(var1, var2, var3);
      }

      public View onCreateSnapshotView(Context var1, Parcelable var2) {
         return this.mCallback.onCreateSnapshotView(var1, var2);
      }

      public void onMapSharedElements(List var1, Map var2) {
         this.mCallback.onMapSharedElements(var1, var2);
      }

      public void onRejectSharedElements(List var1) {
         this.mCallback.onRejectSharedElements(var1);
      }

      public void onSharedElementEnd(List var1, List var2, List var3) {
         this.mCallback.onSharedElementEnd(var1, var2, var3);
      }

      public void onSharedElementStart(List var1, List var2, List var3) {
         this.mCallback.onSharedElementStart(var1, var2, var3);
      }

      public void onSharedElementsArrived(List var1, List var2, final ActivityCompatApi23.OnSharedElementsReadyListenerBridge var3) {
         this.mCallback.onSharedElementsArrived(var1, var2, new SharedElementCallback.OnSharedElementsReadyListener() {
            public void onSharedElementsReady() {
               var3.onSharedElementsReady();
            }
         });
      }
   }
}
