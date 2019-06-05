package com.journeyapps.barcodescanner;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.DecodeFormatManager;
import com.google.zxing.client.android.DecodeHintManager;
import com.google.zxing.client.android.R;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecoratedBarcodeView extends FrameLayout {
   private BarcodeView barcodeView;
   private TextView statusView;
   private DecoratedBarcodeView.TorchListener torchListener;
   private ViewfinderView viewFinder;

   public DecoratedBarcodeView(Context var1) {
      super(var1);
      this.initialize();
   }

   public DecoratedBarcodeView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.initialize(var2);
   }

   public DecoratedBarcodeView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.initialize(var2);
   }

   private void initialize() {
      this.initialize((AttributeSet)null);
   }

   private void initialize(AttributeSet var1) {
      TypedArray var2 = this.getContext().obtainStyledAttributes(var1, R.styleable.zxing_view);
      int var3 = var2.getResourceId(R.styleable.zxing_view_zxing_scanner_layout, R.layout.zxing_barcode_scanner);
      var2.recycle();
      inflate(this.getContext(), var3, this);
      this.barcodeView = (BarcodeView)this.findViewById(R.id.zxing_barcode_surface);
      if (this.barcodeView == null) {
         throw new IllegalArgumentException("There is no a com.journeyapps.barcodescanner.BarcodeView on provided layout with the id \"zxing_barcode_surface\".");
      } else {
         this.barcodeView.initializeAttributes(var1);
         this.viewFinder = (ViewfinderView)this.findViewById(R.id.zxing_viewfinder_view);
         if (this.viewFinder == null) {
            throw new IllegalArgumentException("There is no a com.journeyapps.barcodescanner.ViewfinderView on provided layout with the id \"zxing_viewfinder_view\".");
         } else {
            this.viewFinder.setCameraPreview(this.barcodeView);
            this.statusView = (TextView)this.findViewById(R.id.zxing_status_view);
         }
      }
   }

   public void decodeContinuous(BarcodeCallback var1) {
      this.barcodeView.decodeContinuous(new DecoratedBarcodeView.WrappedCallback(var1));
   }

   public void decodeSingle(BarcodeCallback var1) {
      this.barcodeView.decodeSingle(new DecoratedBarcodeView.WrappedCallback(var1));
   }

   public BarcodeView getBarcodeView() {
      return (BarcodeView)this.findViewById(R.id.zxing_barcode_surface);
   }

   public TextView getStatusView() {
      return this.statusView;
   }

   public ViewfinderView getViewFinder() {
      return this.viewFinder;
   }

   public void initializeFromIntent(Intent var1) {
      Set var2 = DecodeFormatManager.parseDecodeFormats(var1);
      Map var3 = DecodeHintManager.parseDecodeHints(var1);
      CameraSettings var4 = new CameraSettings();
      if (var1.hasExtra("SCAN_CAMERA_ID")) {
         int var5 = var1.getIntExtra("SCAN_CAMERA_ID", -1);
         if (var5 >= 0) {
            var4.setRequestedCameraId(var5);
         }
      }

      String var6 = var1.getStringExtra("PROMPT_MESSAGE");
      if (var6 != null) {
         this.setStatusText(var6);
      }

      boolean var7 = var1.getBooleanExtra("INVERTED_SCAN", false);
      String var8 = var1.getStringExtra("CHARACTER_SET");
      (new MultiFormatReader()).setHints(var3);
      this.barcodeView.setCameraSettings(var4);
      this.barcodeView.setDecoderFactory(new DefaultDecoderFactory(var2, var3, var8, var7));
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3 = true;
      boolean var4 = var3;
      switch(var1) {
      case 24:
         this.setTorchOn();
         var4 = var3;
         break;
      case 25:
         this.setTorchOff();
         var4 = var3;
      case 27:
      case 80:
         break;
      default:
         var4 = super.onKeyDown(var1, var2);
      }

      return var4;
   }

   public void pause() {
      this.barcodeView.pause();
   }

   public void pauseAndWait() {
      this.barcodeView.pauseAndWait();
   }

   public void resume() {
      this.barcodeView.resume();
   }

   public void setStatusText(String var1) {
      if (this.statusView != null) {
         this.statusView.setText(var1);
      }

   }

   public void setTorchListener(DecoratedBarcodeView.TorchListener var1) {
      this.torchListener = var1;
   }

   public void setTorchOff() {
      this.barcodeView.setTorch(false);
      if (this.torchListener != null) {
         this.torchListener.onTorchOff();
      }

   }

   public void setTorchOn() {
      this.barcodeView.setTorch(true);
      if (this.torchListener != null) {
         this.torchListener.onTorchOn();
      }

   }

   public interface TorchListener {
      void onTorchOff();

      void onTorchOn();
   }

   private class WrappedCallback implements BarcodeCallback {
      private BarcodeCallback delegate;

      public WrappedCallback(BarcodeCallback var2) {
         this.delegate = var2;
      }

      public void barcodeResult(BarcodeResult var1) {
         this.delegate.barcodeResult(var1);
      }

      public void possibleResultPoints(List var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ResultPoint var3 = (ResultPoint)var2.next();
            DecoratedBarcodeView.this.viewFinder.addPossibleResultPoint(var3);
         }

         this.delegate.possibleResultPoints(var1);
      }
   }
}
