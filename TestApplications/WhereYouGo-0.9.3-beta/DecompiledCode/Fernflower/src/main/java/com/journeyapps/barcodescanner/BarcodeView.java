package com.journeyapps.barcodescanner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import com.google.zxing.DecodeHintType;
import com.google.zxing.client.android.R;
import java.util.HashMap;
import java.util.List;

public class BarcodeView extends CameraPreview {
   private BarcodeCallback callback;
   private BarcodeView.DecodeMode decodeMode;
   private DecoderFactory decoderFactory;
   private DecoderThread decoderThread;
   private final Callback resultCallback;
   private Handler resultHandler;

   public BarcodeView(Context var1) {
      super(var1);
      this.decodeMode = BarcodeView.DecodeMode.NONE;
      this.callback = null;
      this.resultCallback = new Callback() {
         public boolean handleMessage(Message var1) {
            boolean var2 = true;
            boolean var3;
            if (var1.what == R.id.zxing_decode_succeeded) {
               BarcodeResult var4 = (BarcodeResult)var1.obj;
               var3 = var2;
               if (var4 != null) {
                  var3 = var2;
                  if (BarcodeView.this.callback != null) {
                     var3 = var2;
                     if (BarcodeView.this.decodeMode != BarcodeView.DecodeMode.NONE) {
                        BarcodeView.this.callback.barcodeResult(var4);
                        var3 = var2;
                        if (BarcodeView.this.decodeMode == BarcodeView.DecodeMode.SINGLE) {
                           BarcodeView.this.stopDecoding();
                           var3 = var2;
                        }
                     }
                  }
               }
            } else {
               var3 = var2;
               if (var1.what != R.id.zxing_decode_failed) {
                  if (var1.what == R.id.zxing_possible_result_points) {
                     List var5 = (List)var1.obj;
                     var3 = var2;
                     if (BarcodeView.this.callback != null) {
                        var3 = var2;
                        if (BarcodeView.this.decodeMode != BarcodeView.DecodeMode.NONE) {
                           BarcodeView.this.callback.possibleResultPoints(var5);
                           var3 = var2;
                        }
                     }
                  } else {
                     var3 = false;
                  }
               }
            }

            return var3;
         }
      };
      this.initialize();
   }

   public BarcodeView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.decodeMode = BarcodeView.DecodeMode.NONE;
      this.callback = null;
      this.resultCallback = new Callback() {
         public boolean handleMessage(Message var1) {
            boolean var2 = true;
            boolean var3;
            if (var1.what == R.id.zxing_decode_succeeded) {
               BarcodeResult var4 = (BarcodeResult)var1.obj;
               var3 = var2;
               if (var4 != null) {
                  var3 = var2;
                  if (BarcodeView.this.callback != null) {
                     var3 = var2;
                     if (BarcodeView.this.decodeMode != BarcodeView.DecodeMode.NONE) {
                        BarcodeView.this.callback.barcodeResult(var4);
                        var3 = var2;
                        if (BarcodeView.this.decodeMode == BarcodeView.DecodeMode.SINGLE) {
                           BarcodeView.this.stopDecoding();
                           var3 = var2;
                        }
                     }
                  }
               }
            } else {
               var3 = var2;
               if (var1.what != R.id.zxing_decode_failed) {
                  if (var1.what == R.id.zxing_possible_result_points) {
                     List var5 = (List)var1.obj;
                     var3 = var2;
                     if (BarcodeView.this.callback != null) {
                        var3 = var2;
                        if (BarcodeView.this.decodeMode != BarcodeView.DecodeMode.NONE) {
                           BarcodeView.this.callback.possibleResultPoints(var5);
                           var3 = var2;
                        }
                     }
                  } else {
                     var3 = false;
                  }
               }
            }

            return var3;
         }
      };
      this.initialize();
   }

   public BarcodeView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.decodeMode = BarcodeView.DecodeMode.NONE;
      this.callback = null;
      this.resultCallback = new Callback() {
         public boolean handleMessage(Message var1) {
            boolean var2 = true;
            boolean var3;
            if (var1.what == R.id.zxing_decode_succeeded) {
               BarcodeResult var4 = (BarcodeResult)var1.obj;
               var3 = var2;
               if (var4 != null) {
                  var3 = var2;
                  if (BarcodeView.this.callback != null) {
                     var3 = var2;
                     if (BarcodeView.this.decodeMode != BarcodeView.DecodeMode.NONE) {
                        BarcodeView.this.callback.barcodeResult(var4);
                        var3 = var2;
                        if (BarcodeView.this.decodeMode == BarcodeView.DecodeMode.SINGLE) {
                           BarcodeView.this.stopDecoding();
                           var3 = var2;
                        }
                     }
                  }
               }
            } else {
               var3 = var2;
               if (var1.what != R.id.zxing_decode_failed) {
                  if (var1.what == R.id.zxing_possible_result_points) {
                     List var5 = (List)var1.obj;
                     var3 = var2;
                     if (BarcodeView.this.callback != null) {
                        var3 = var2;
                        if (BarcodeView.this.decodeMode != BarcodeView.DecodeMode.NONE) {
                           BarcodeView.this.callback.possibleResultPoints(var5);
                           var3 = var2;
                        }
                     }
                  } else {
                     var3 = false;
                  }
               }
            }

            return var3;
         }
      };
      this.initialize();
   }

   private Decoder createDecoder() {
      if (this.decoderFactory == null) {
         this.decoderFactory = this.createDefaultDecoderFactory();
      }

      DecoderResultPointCallback var1 = new DecoderResultPointCallback();
      HashMap var2 = new HashMap();
      var2.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, var1);
      Decoder var3 = this.decoderFactory.createDecoder(var2);
      var1.setDecoder(var3);
      return var3;
   }

   private void initialize() {
      this.decoderFactory = new DefaultDecoderFactory();
      this.resultHandler = new Handler(this.resultCallback);
   }

   private void startDecoderThread() {
      this.stopDecoderThread();
      if (this.decodeMode != BarcodeView.DecodeMode.NONE && this.isPreviewActive()) {
         this.decoderThread = new DecoderThread(this.getCameraInstance(), this.createDecoder(), this.resultHandler);
         this.decoderThread.setCropRect(this.getPreviewFramingRect());
         this.decoderThread.start();
      }

   }

   private void stopDecoderThread() {
      if (this.decoderThread != null) {
         this.decoderThread.stop();
         this.decoderThread = null;
      }

   }

   protected DecoderFactory createDefaultDecoderFactory() {
      return new DefaultDecoderFactory();
   }

   public void decodeContinuous(BarcodeCallback var1) {
      this.decodeMode = BarcodeView.DecodeMode.CONTINUOUS;
      this.callback = var1;
      this.startDecoderThread();
   }

   public void decodeSingle(BarcodeCallback var1) {
      this.decodeMode = BarcodeView.DecodeMode.SINGLE;
      this.callback = var1;
      this.startDecoderThread();
   }

   public DecoderFactory getDecoderFactory() {
      return this.decoderFactory;
   }

   public void pause() {
      this.stopDecoderThread();
      super.pause();
   }

   protected void previewStarted() {
      super.previewStarted();
      this.startDecoderThread();
   }

   public void setDecoderFactory(DecoderFactory var1) {
      Util.validateMainThread();
      this.decoderFactory = var1;
      if (this.decoderThread != null) {
         this.decoderThread.setDecoder(this.createDecoder());
      }

   }

   public void stopDecoding() {
      this.decodeMode = BarcodeView.DecodeMode.NONE;
      this.callback = null;
      this.stopDecoderThread();
   }

   private static enum DecodeMode {
      CONTINUOUS,
      NONE,
      SINGLE;
   }
}
