package android.support.v4.app;

import android.app.Service;
import android.support.annotation.RestrictTo;
import android.support.v4.os.BuildCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ServiceCompat {
   static final ServiceCompat.ServiceCompatImpl IMPL;
   public static final int START_STICKY = 1;
   public static final int STOP_FOREGROUND_DETACH = 2;
   public static final int STOP_FOREGROUND_REMOVE = 1;

   static {
      if (BuildCompat.isAtLeastN()) {
         IMPL = new ServiceCompat.Api24ServiceCompatImpl();
      } else {
         IMPL = new ServiceCompat.BaseServiceCompatImpl();
      }

   }

   private ServiceCompat() {
   }

   public static void stopForeground(Service var0, int var1) {
      IMPL.stopForeground(var0, var1);
   }

   static class Api24ServiceCompatImpl implements ServiceCompat.ServiceCompatImpl {
      public void stopForeground(Service var1, int var2) {
         ServiceCompatApi24.stopForeground(var1, var2);
      }
   }

   static class BaseServiceCompatImpl implements ServiceCompat.ServiceCompatImpl {
      public void stopForeground(Service var1, int var2) {
         boolean var3;
         if ((var2 & 1) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var1.stopForeground(var3);
      }
   }

   interface ServiceCompatImpl {
      void stopForeground(Service var1, int var2);
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface StopForegroundFlags {
   }
}
