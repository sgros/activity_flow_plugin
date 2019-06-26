package com.journeyapps.barcodescanner;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import com.google.zxing.LuminanceSource;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.android.R;
import com.journeyapps.barcodescanner.camera.CameraInstance;
import com.journeyapps.barcodescanner.camera.PreviewCallback;
import java.util.List;

public class DecoderThread {
   private static final String TAG = DecoderThread.class.getSimpleName();
   private final Object LOCK = new Object();
   private final Callback callback = new Callback() {
      public boolean handleMessage(Message var1) {
         if (var1.what == R.id.zxing_decode) {
            DecoderThread.this.decode((SourceData)var1.obj);
         } else if (var1.what == R.id.zxing_preview_failed) {
            DecoderThread.this.requestNextPreview();
         }

         return true;
      }
   };
   private CameraInstance cameraInstance;
   private Rect cropRect;
   private Decoder decoder;
   private Handler handler;
   private final PreviewCallback previewCallback = new PreviewCallback() {
      public void onPreview(SourceData var1) {
         Object var2 = DecoderThread.this.LOCK;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label122: {
            try {
               if (DecoderThread.this.running) {
                  DecoderThread.this.handler.obtainMessage(R.id.zxing_decode, var1).sendToTarget();
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label122;
            }

            label119:
            try {
               return;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label119;
            }
         }

         while(true) {
            Throwable var15 = var10000;

            try {
               throw var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      }

      public void onPreviewError(Exception var1) {
         Object var15 = DecoderThread.this.LOCK;
         synchronized(var15){}

         Throwable var10000;
         boolean var10001;
         label122: {
            try {
               if (DecoderThread.this.running) {
                  DecoderThread.this.handler.obtainMessage(R.id.zxing_preview_failed).sendToTarget();
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label122;
            }

            label119:
            try {
               return;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label119;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      }
   };
   private Handler resultHandler;
   private boolean running = false;
   private HandlerThread thread;

   public DecoderThread(CameraInstance var1, Decoder var2, Handler var3) {
      Util.validateMainThread();
      this.cameraInstance = var1;
      this.decoder = var2;
      this.resultHandler = var3;
   }

   private void decode(SourceData var1) {
      long var2 = System.currentTimeMillis();
      Result var4 = null;
      var1.setCropRect(this.cropRect);
      LuminanceSource var5 = this.createSource(var1);
      if (var5 != null) {
         var4 = this.decoder.decode(var5);
      }

      if (var4 != null) {
         long var6 = System.currentTimeMillis();
         Log.d(TAG, "Found barcode in " + (var6 - var2) + " ms");
         if (this.resultHandler != null) {
            BarcodeResult var8 = new BarcodeResult(var4, var1);
            Message var9 = Message.obtain(this.resultHandler, R.id.zxing_decode_succeeded, var8);
            var9.setData(new Bundle());
            var9.sendToTarget();
         }
      } else if (this.resultHandler != null) {
         Message.obtain(this.resultHandler, R.id.zxing_decode_failed).sendToTarget();
      }

      if (this.resultHandler != null) {
         List var10 = this.decoder.getPossibleResultPoints();
         Message.obtain(this.resultHandler, R.id.zxing_possible_result_points, var10).sendToTarget();
      }

      this.requestNextPreview();
   }

   private void requestNextPreview() {
      if (this.cameraInstance.isOpen()) {
         this.cameraInstance.requestPreview(this.previewCallback);
      }

   }

   protected LuminanceSource createSource(SourceData var1) {
      PlanarYUVLuminanceSource var2;
      if (this.cropRect == null) {
         var2 = null;
      } else {
         var2 = var1.createSource();
      }

      return var2;
   }

   public Rect getCropRect() {
      return this.cropRect;
   }

   public Decoder getDecoder() {
      return this.decoder;
   }

   public void setCropRect(Rect var1) {
      this.cropRect = var1;
   }

   public void setDecoder(Decoder var1) {
      this.decoder = var1;
   }

   public void start() {
      Util.validateMainThread();
      this.thread = new HandlerThread(TAG);
      this.thread.start();
      this.handler = new Handler(this.thread.getLooper(), this.callback);
      this.running = true;
      this.requestNextPreview();
   }

   public void stop() {
      // $FF: Couldn't be decompiled
   }
}
