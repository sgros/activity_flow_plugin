package com.journeyapps.barcodescanner;

import android.content.Context;
import android.view.OrientationEventListener;
import android.view.WindowManager;

public class RotationListener {
   private RotationCallback callback;
   private int lastRotation;
   private OrientationEventListener orientationEventListener;
   private WindowManager windowManager;

   public void listen(Context var1, RotationCallback var2) {
      this.stop();
      var1 = var1.getApplicationContext();
      this.callback = var2;
      this.windowManager = (WindowManager)var1.getSystemService("window");
      this.orientationEventListener = new OrientationEventListener(var1, 3) {
         public void onOrientationChanged(int var1) {
            WindowManager var2 = RotationListener.this.windowManager;
            RotationCallback var3 = RotationListener.this.callback;
            if (RotationListener.this.windowManager != null && var3 != null) {
               var1 = var2.getDefaultDisplay().getRotation();
               if (var1 != RotationListener.this.lastRotation) {
                  RotationListener.this.lastRotation = var1;
                  var3.onRotationChanged(var1);
               }
            }

         }
      };
      this.orientationEventListener.enable();
      this.lastRotation = this.windowManager.getDefaultDisplay().getRotation();
   }

   public void stop() {
      if (this.orientationEventListener != null) {
         this.orientationEventListener.disable();
      }

      this.orientationEventListener = null;
      this.windowManager = null;
      this.callback = null;
   }
}
