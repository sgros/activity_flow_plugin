package com.adjust.sdk.plugin;

import android.content.Context;
import android.provider.Settings.Secure;

public class AndroidIdUtil {
   public static String getAndroidId(Context var0) {
      return Secure.getString(var0.getContentResolver(), "android_id");
   }
}
