package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

public class LibraryLoader {
   static final String BASE_LIBRARY_NAME = "pl_droidsonroids_gif";
   static final String SURFACE_LIBRARY_NAME = "pl_droidsonroids_gif_surface";
   @SuppressLint({"StaticFieldLeak"})
   private static Context sAppContext;

   private LibraryLoader() {
   }

   private static Context getContext() {
      if (sAppContext == null) {
         try {
            sAppContext = (Context)Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication").invoke((Object)null);
         } catch (Exception var1) {
            throw new IllegalStateException("LibraryLoader not initialized. Call LibraryLoader.initialize() before using library classes.", var1);
         }
      }

      return sAppContext;
   }

   public static void initialize(@NonNull Context var0) {
      sAppContext = var0.getApplicationContext();
   }

   static void loadLibrary(Context var0) {
      try {
         System.loadLibrary("pl_droidsonroids_gif");
      } catch (UnsatisfiedLinkError var2) {
         Context var1 = var0;
         if (var0 == null) {
            var1 = getContext();
         }

         ReLinker.loadLibrary(var1);
      }

   }
}
