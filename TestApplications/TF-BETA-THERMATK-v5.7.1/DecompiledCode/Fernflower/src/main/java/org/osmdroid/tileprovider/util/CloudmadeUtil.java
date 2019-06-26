package org.osmdroid.tileprovider.util;

import android.content.SharedPreferences.Editor;

public class CloudmadeUtil {
   public static boolean DEBUGMODE;
   private static String mAndroidId;
   private static String mKey;
   private static Editor mPreferenceEditor;
   private static String mToken;

   public static String getCloudmadeKey() {
      return mKey;
   }

   public static String getCloudmadeToken() {
      // $FF: Couldn't be decompiled
   }
}
