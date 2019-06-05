package com.journeyapps.barcodescanner;

import java.util.List;

public interface BarcodeCallback {
   void barcodeResult(BarcodeResult var1);

   void possibleResultPoints(List var1);
}
