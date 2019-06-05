package org.mozilla.rocket.privately;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import java.io.File;
import java.util.Iterator;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.threadutils.ThreadUtils;

public final class PrivateMode {
   public static final PrivateMode.Companion Companion = new PrivateMode.Companion((DefaultConstructorMarker)null);

   public static final boolean hasPrivateSession(Context var0) {
      return Companion.hasPrivateSession(var0);
   }

   public static final boolean isEnable(Context var0) {
      return Companion.isEnable(var0);
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      private final void clean(File var1, Context var2) {
         if (!FileUtils.deleteDirectory(var1)) {
            SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(var2);
            if (var4 != null) {
               Editor var5 = var4.edit();
               if (var5 != null) {
                  Editor var3 = var5.putString("pref_key_sanitize_reminder", var1.getAbsolutePath());
                  if (var3 != null) {
                     var3.apply();
                  }
               }
            }
         }

      }

      public final boolean hasPrivateSession(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         Object var4 = var1.getSystemService("activity");
         if (var4 != null) {
            Iterator var5 = ((ActivityManager)var4).getRunningServices(Integer.MAX_VALUE).iterator();

            String var3;
            ComponentName var6;
            do {
               if (!var5.hasNext()) {
                  return false;
               }

               RunningServiceInfo var2 = (RunningServiceInfo)var5.next();
               var3 = PrivateSessionNotificationService.class.getName();
               var6 = var2.service;
               Intrinsics.checkExpressionValueIsNotNull(var6, "service.service");
            } while(!Intrinsics.areEqual(var3, var6.getClassName()));

            return true;
         } else {
            throw new TypeCastException("null cannot be cast to non-null type android.app.ActivityManager");
         }
      }

      public final boolean isEnable(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         return !AppConstants.isReleaseBuild() && !AppConstants.isBetaBuild() ? PreferenceManager.getDefaultSharedPreferences(var1).getBoolean("pref_key_private_mode_enabled", true) : AppConfigWrapper.isPrivateModeEnabled(var1);
      }

      public final void sanitize(final Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         ThreadUtils.postToBackgroundThread((Runnable)(new Runnable() {
            public final void run() {
               Context var1x = var1.getApplicationContext();
               Intrinsics.checkExpressionValueIsNotNull(var1x, "context.applicationContext");
               File var2 = var1x.getCacheDir();
               if (var2 != null) {
                  PrivateMode.Companion.clean(var2, var1);
               }

               var2 = var1.getApplicationContext().getDir("webview", 0);
               if (var2 != null) {
                  PrivateMode.Companion.clean(var2, var1);
               }

            }
         }));
      }
   }
}
