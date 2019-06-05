package com.journeyapps.barcodescanner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CaptureManager {
   private static final String SAVED_ORIENTATION_LOCK = "SAVED_ORIENTATION_LOCK";
   private static final String TAG = CaptureManager.class.getSimpleName();
   private static int cameraPermissionReqCode = 250;
   private Activity activity;
   private boolean askedPermission = false;
   private DecoratedBarcodeView barcodeView;
   private BeepManager beepManager;
   private BarcodeCallback callback = new BarcodeCallback() {
      public void barcodeResult(final BarcodeResult var1) {
         CaptureManager.this.barcodeView.pause();
         CaptureManager.this.beepManager.playBeepSoundAndVibrate();
         CaptureManager.this.handler.post(new Runnable() {
            public void run() {
               CaptureManager.this.returnResult(var1);
            }
         });
      }

      public void possibleResultPoints(List var1) {
      }
   };
   private boolean destroyed = false;
   private boolean finishWhenClosed = false;
   private Handler handler;
   private InactivityTimer inactivityTimer;
   private int orientationLock = -1;
   private boolean returnBarcodeImagePath = false;
   private final CameraPreview.StateListener stateListener = new CameraPreview.StateListener() {
      public void cameraClosed() {
         if (CaptureManager.this.finishWhenClosed) {
            Log.d(CaptureManager.TAG, "Camera closed; finishing activity");
            CaptureManager.this.finish();
         }

      }

      public void cameraError(Exception var1) {
         CaptureManager.this.displayFrameworkBugMessageAndExit();
      }

      public void previewSized() {
      }

      public void previewStarted() {
      }

      public void previewStopped() {
      }
   };

   public CaptureManager(Activity var1, DecoratedBarcodeView var2) {
      this.activity = var1;
      this.barcodeView = var2;
      var2.getBarcodeView().addStateListener(this.stateListener);
      this.handler = new Handler();
      this.inactivityTimer = new InactivityTimer(var1, new Runnable() {
         public void run() {
            Log.d(CaptureManager.TAG, "Finishing due to inactivity");
            CaptureManager.this.finish();
         }
      });
      this.beepManager = new BeepManager(var1);
   }

   private void finish() {
      this.activity.finish();
   }

   private String getBarcodeImagePath(BarcodeResult var1) {
      Object var2 = null;
      String var3 = (String)var2;
      if (this.returnBarcodeImagePath) {
         Bitmap var6 = var1.getBitmap();

         try {
            File var4 = File.createTempFile("barcodeimage", ".jpg", this.activity.getCacheDir());
            FileOutputStream var7 = new FileOutputStream(var4);
            var6.compress(CompressFormat.JPEG, 100, var7);
            var7.close();
            var3 = var4.getAbsolutePath();
         } catch (IOException var5) {
            Log.w(TAG, "Unable to create temporary file and store bitmap! " + var5);
            var3 = (String)var2;
         }
      }

      return var3;
   }

   public static int getCameraPermissionReqCode() {
      return cameraPermissionReqCode;
   }

   @TargetApi(23)
   private void openCameraWithPermission() {
      if (ContextCompat.checkSelfPermission(this.activity, "android.permission.CAMERA") == 0) {
         this.barcodeView.resume();
      } else if (!this.askedPermission) {
         Activity var1 = this.activity;
         int var2 = cameraPermissionReqCode;
         ActivityCompat.requestPermissions(var1, new String[]{"android.permission.CAMERA"}, var2);
         this.askedPermission = true;
      }

   }

   public static Intent resultIntent(BarcodeResult var0, String var1) {
      Intent var2 = new Intent("com.google.zxing.client.android.SCAN");
      var2.addFlags(524288);
      var2.putExtra("SCAN_RESULT", var0.toString());
      var2.putExtra("SCAN_RESULT_FORMAT", var0.getBarcodeFormat().toString());
      byte[] var3 = var0.getRawBytes();
      if (var3 != null && var3.length > 0) {
         var2.putExtra("SCAN_RESULT_BYTES", var3);
      }

      Map var5 = var0.getResultMetadata();
      if (var5 != null) {
         if (var5.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
            var2.putExtra("SCAN_RESULT_UPC_EAN_EXTENSION", var5.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
         }

         Number var8 = (Number)var5.get(ResultMetadataType.ORIENTATION);
         if (var8 != null) {
            var2.putExtra("SCAN_RESULT_ORIENTATION", var8.intValue());
         }

         String var9 = (String)var5.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
         if (var9 != null) {
            var2.putExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL", var9);
         }

         Iterable var6 = (Iterable)var5.get(ResultMetadataType.BYTE_SEGMENTS);
         if (var6 != null) {
            int var4 = 0;

            for(Iterator var10 = var6.iterator(); var10.hasNext(); ++var4) {
               byte[] var7 = (byte[])var10.next();
               var2.putExtra("SCAN_RESULT_BYTE_SEGMENTS_" + var4, var7);
            }
         }
      }

      if (var1 != null) {
         var2.putExtra("SCAN_RESULT_IMAGE_PATH", var1);
      }

      return var2;
   }

   public static void setCameraPermissionReqCode(int var0) {
      cameraPermissionReqCode = var0;
   }

   protected void closeAndFinish() {
      if (this.barcodeView.getBarcodeView().isCameraClosed()) {
         this.finish();
      } else {
         this.finishWhenClosed = true;
      }

      this.barcodeView.pause();
      this.inactivityTimer.cancel();
   }

   public void decode() {
      this.barcodeView.decodeSingle(this.callback);
   }

   protected void displayFrameworkBugMessageAndExit() {
      if (!this.activity.isFinishing() && !this.destroyed && !this.finishWhenClosed) {
         Builder var1 = new Builder(this.activity);
         var1.setTitle(this.activity.getString(R.string.zxing_app_name));
         var1.setMessage(this.activity.getString(R.string.zxing_msg_camera_framework_bug));
         var1.setPositiveButton(R.string.zxing_button_ok, new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               CaptureManager.this.finish();
            }
         });
         var1.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface var1) {
               CaptureManager.this.finish();
            }
         });
         var1.show();
      }

   }

   public void initializeFromIntent(Intent var1, Bundle var2) {
      this.activity.getWindow().addFlags(128);
      if (var2 != null) {
         this.orientationLock = var2.getInt("SAVED_ORIENTATION_LOCK", -1);
      }

      if (var1 != null) {
         if (var1.getBooleanExtra("SCAN_ORIENTATION_LOCKED", true)) {
            this.lockOrientation();
         }

         if ("com.google.zxing.client.android.SCAN".equals(var1.getAction())) {
            this.barcodeView.initializeFromIntent(var1);
         }

         if (!var1.getBooleanExtra("BEEP_ENABLED", true)) {
            this.beepManager.setBeepEnabled(false);
         }

         if (var1.hasExtra("TIMEOUT")) {
            Runnable var3 = new Runnable() {
               public void run() {
                  CaptureManager.this.returnResultTimeout();
               }
            };
            this.handler.postDelayed(var3, var1.getLongExtra("TIMEOUT", 0L));
         }

         if (var1.getBooleanExtra("BARCODE_IMAGE_ENABLED", false)) {
            this.returnBarcodeImagePath = true;
         }
      }

   }

   protected void lockOrientation() {
      if (this.orientationLock == -1) {
         int var1 = this.activity.getWindowManager().getDefaultDisplay().getRotation();
         int var2 = this.activity.getResources().getConfiguration().orientation;
         byte var3 = 0;
         if (var2 == 2) {
            if (var1 != 0 && var1 != 1) {
               var3 = 8;
            } else {
               var3 = 0;
            }
         } else if (var2 == 1) {
            if (var1 != 0 && var1 != 3) {
               var3 = 9;
            } else {
               var3 = 1;
            }
         }

         this.orientationLock = var3;
      }

      this.activity.setRequestedOrientation(this.orientationLock);
   }

   public void onDestroy() {
      this.destroyed = true;
      this.inactivityTimer.cancel();
      this.handler.removeCallbacksAndMessages((Object)null);
   }

   public void onPause() {
      this.inactivityTimer.cancel();
      this.barcodeView.pauseAndWait();
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      if (var1 == cameraPermissionReqCode) {
         if (var3.length > 0 && var3[0] == 0) {
            this.barcodeView.resume();
         } else {
            this.displayFrameworkBugMessageAndExit();
         }
      }

   }

   public void onResume() {
      if (VERSION.SDK_INT >= 23) {
         this.openCameraWithPermission();
      } else {
         this.barcodeView.resume();
      }

      this.inactivityTimer.start();
   }

   public void onSaveInstanceState(Bundle var1) {
      var1.putInt("SAVED_ORIENTATION_LOCK", this.orientationLock);
   }

   protected void returnResult(BarcodeResult var1) {
      Intent var2 = resultIntent(var1, this.getBarcodeImagePath(var1));
      this.activity.setResult(-1, var2);
      this.closeAndFinish();
   }

   protected void returnResultTimeout() {
      Intent var1 = new Intent("com.google.zxing.client.android.SCAN");
      var1.putExtra("TIMEOUT", true);
      this.activity.setResult(0, var1);
      this.closeAndFinish();
   }
}
