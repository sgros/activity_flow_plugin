package com.journeyapps.barcodescanner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import com.google.zxing.client.android.R;

public class CaptureActivity extends Activity {
   private DecoratedBarcodeView barcodeScannerView;
   private CaptureManager capture;

   protected DecoratedBarcodeView initializeContent() {
      this.setContentView(R.layout.zxing_capture);
      return (DecoratedBarcodeView)this.findViewById(R.id.zxing_barcode_scanner);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.barcodeScannerView = this.initializeContent();
      this.capture = new CaptureManager(this, this.barcodeScannerView);
      this.capture.initializeFromIntent(this.getIntent(), var1);
      this.capture.decode();
   }

   protected void onDestroy() {
      super.onDestroy();
      this.capture.onDestroy();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3;
      if (!this.barcodeScannerView.onKeyDown(var1, var2) && !super.onKeyDown(var1, var2)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   protected void onPause() {
      super.onPause();
      this.capture.onPause();
   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
      this.capture.onRequestPermissionsResult(var1, var2, var3);
   }

   protected void onResume() {
      super.onResume();
      this.capture.onResume();
   }

   protected void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      this.capture.onSaveInstanceState(var1);
   }
}
