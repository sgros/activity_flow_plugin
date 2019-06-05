package com.journeyapps.barcodescanner.camera;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;

public final class AutoFocusManager {
   private static final long AUTO_FOCUS_INTERVAL_MS = 2000L;
   private static final Collection FOCUS_MODES_CALLING_AF = new ArrayList(2);
   private static final String TAG = AutoFocusManager.class.getSimpleName();
   private int MESSAGE_FOCUS;
   private final AutoFocusCallback autoFocusCallback;
   private final Camera camera;
   private final Callback focusHandlerCallback;
   private boolean focusing;
   private Handler handler;
   private boolean stopped;
   private final boolean useAutoFocus;

   static {
      FOCUS_MODES_CALLING_AF.add("auto");
      FOCUS_MODES_CALLING_AF.add("macro");
   }

   public AutoFocusManager(Camera var1, CameraSettings var2) {
      boolean var3 = true;
      super();
      this.MESSAGE_FOCUS = 1;
      this.focusHandlerCallback = new Callback() {
         public boolean handleMessage(Message var1) {
            boolean var2;
            if (var1.what == AutoFocusManager.this.MESSAGE_FOCUS) {
               AutoFocusManager.this.focus();
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }
      };
      this.autoFocusCallback = new AutoFocusCallback() {
         public void onAutoFocus(boolean var1, Camera var2) {
            AutoFocusManager.this.handler.post(new Runnable() {
               public void run() {
                  AutoFocusManager.this.focusing = false;
                  AutoFocusManager.this.autoFocusAgainLater();
               }
            });
         }
      };
      this.handler = new Handler(this.focusHandlerCallback);
      this.camera = var1;
      String var4 = var1.getParameters().getFocusMode();
      if (!var2.isAutoFocusEnabled() || !FOCUS_MODES_CALLING_AF.contains(var4)) {
         var3 = false;
      }

      this.useAutoFocus = var3;
      Log.i(TAG, "Current focus mode '" + var4 + "'; use auto focus? " + this.useAutoFocus);
      this.start();
   }

   private void autoFocusAgainLater() {
      synchronized(this){}

      try {
         if (!this.stopped && !this.handler.hasMessages(this.MESSAGE_FOCUS)) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(this.MESSAGE_FOCUS), 2000L);
         }
      } finally {
         ;
      }

   }

   private void cancelOutstandingTask() {
      this.handler.removeMessages(this.MESSAGE_FOCUS);
   }

   private void focus() {
      if (this.useAutoFocus && !this.stopped && !this.focusing) {
         try {
            this.camera.autoFocus(this.autoFocusCallback);
            this.focusing = true;
         } catch (RuntimeException var2) {
            Log.w(TAG, "Unexpected exception while focusing", var2);
            this.autoFocusAgainLater();
         }
      }

   }

   public void start() {
      this.stopped = false;
      this.focus();
   }

   public void stop() {
      this.stopped = true;
      this.focusing = false;
      this.cancelOutstandingTask();
      if (this.useAutoFocus) {
         try {
            this.camera.cancelAutoFocus();
         } catch (RuntimeException var2) {
            Log.w(TAG, "Unexpected exception while cancelling focusing", var2);
         }
      }

   }
}
