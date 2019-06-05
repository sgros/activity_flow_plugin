package com.journeyapps.barcodescanner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import com.google.zxing.client.android.C0186R;

public class CaptureActivity extends Activity {
    private DecoratedBarcodeView barcodeScannerView;
    private CaptureManager capture;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.barcodeScannerView = initializeContent();
        this.capture = new CaptureManager(this, this.barcodeScannerView);
        this.capture.initializeFromIntent(getIntent(), savedInstanceState);
        this.capture.decode();
    }

    /* Access modifiers changed, original: protected */
    public DecoratedBarcodeView initializeContent() {
        setContentView(C0186R.layout.zxing_capture);
        return (DecoratedBarcodeView) findViewById(C0186R.C0185id.zxing_barcode_scanner);
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        this.capture.onResume();
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        this.capture.onPause();
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        this.capture.onDestroy();
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.capture.onSaveInstanceState(outState);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        this.capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
