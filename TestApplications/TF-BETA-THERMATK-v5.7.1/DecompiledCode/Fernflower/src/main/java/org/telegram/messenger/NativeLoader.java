package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.io.File;

public class NativeLoader {
   private static final String LIB_NAME = "tmessages.30";
   private static final String LIB_SO_NAME = "libtmessages.30.so";
   private static final int LIB_VERSION = 30;
   private static final String LOCALE_LIB_SO_NAME = "libtmessages.30loc.so";
   private static volatile boolean nativeLoaded;
   private String crashPath = "";

   private static File getNativeLibraryDir(Context var0) {
      File var1;
      label24: {
         if (var0 != null) {
            try {
               var1 = new File((String)ApplicationInfo.class.getField("nativeLibraryDir").get(var0.getApplicationInfo()));
               break label24;
            } catch (Throwable var3) {
               var3.printStackTrace();
            }
         }

         var1 = null;
      }

      File var2 = var1;
      if (var1 == null) {
         var2 = new File(var0.getApplicationInfo().dataDir, "lib");
      }

      return var2.isDirectory() ? var2 : null;
   }

   private static native void init(String var0, boolean var1);

   @SuppressLint({"UnsafeDynamicallyLoadedCode"})
   public static void initNativeLibs(Context param0) {
      // $FF: Couldn't be decompiled
   }

   @SuppressLint({"UnsafeDynamicallyLoadedCode", "SetWorldReadable"})
   private static boolean loadFromZip(Context param0, File param1, File param2, String param3) {
      // $FF: Couldn't be decompiled
   }
}
