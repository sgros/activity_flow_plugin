package com.journeyapps.barcodescanner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.ContextCompat;
import android.util.Log;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.C0186R;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.Intents.Scan;
import com.journeyapps.barcodescanner.CameraPreview.StateListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import menion.android.whereyougo.maps.mapsforge.MapsforgeActivity;

public class CaptureManager {
    private static final String SAVED_ORIENTATION_LOCK = "SAVED_ORIENTATION_LOCK";
    private static final String TAG = CaptureManager.class.getSimpleName();
    private static int cameraPermissionReqCode = MapsforgeActivity.FILE_SYSTEM_CACHE_SIZE_DEFAULT;
    private Activity activity;
    private boolean askedPermission = false;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private BarcodeCallback callback = new C02151();
    private boolean destroyed = false;
    private boolean finishWhenClosed = false;
    private Handler handler;
    private InactivityTimer inactivityTimer;
    private int orientationLock = -1;
    private boolean returnBarcodeImagePath = false;
    private final StateListener stateListener = new C02162();

    /* renamed from: com.journeyapps.barcodescanner.CaptureManager$3 */
    class C02113 implements Runnable {
        C02113() {
        }

        public void run() {
            Log.d(CaptureManager.TAG, "Finishing due to inactivity");
            CaptureManager.this.finish();
        }
    }

    /* renamed from: com.journeyapps.barcodescanner.CaptureManager$4 */
    class C02124 implements Runnable {
        C02124() {
        }

        public void run() {
            CaptureManager.this.returnResultTimeout();
        }
    }

    /* renamed from: com.journeyapps.barcodescanner.CaptureManager$5 */
    class C02135 implements OnClickListener {
        C02135() {
        }

        public void onClick(DialogInterface dialog, int which) {
            CaptureManager.this.finish();
        }
    }

    /* renamed from: com.journeyapps.barcodescanner.CaptureManager$6 */
    class C02146 implements OnCancelListener {
        C02146() {
        }

        public void onCancel(DialogInterface dialog) {
            CaptureManager.this.finish();
        }
    }

    /* renamed from: com.journeyapps.barcodescanner.CaptureManager$1 */
    class C02151 implements BarcodeCallback {
        C02151() {
        }

        public void barcodeResult(final BarcodeResult result) {
            CaptureManager.this.barcodeView.pause();
            CaptureManager.this.beepManager.playBeepSoundAndVibrate();
            CaptureManager.this.handler.post(new Runnable() {
                public void run() {
                    CaptureManager.this.returnResult(result);
                }
            });
        }

        public void possibleResultPoints(List<ResultPoint> list) {
        }
    }

    /* renamed from: com.journeyapps.barcodescanner.CaptureManager$2 */
    class C02162 implements StateListener {
        C02162() {
        }

        public void previewSized() {
        }

        public void previewStarted() {
        }

        public void previewStopped() {
        }

        public void cameraError(Exception error) {
            CaptureManager.this.displayFrameworkBugMessageAndExit();
        }

        public void cameraClosed() {
            if (CaptureManager.this.finishWhenClosed) {
                Log.d(CaptureManager.TAG, "Camera closed; finishing activity");
                CaptureManager.this.finish();
            }
        }
    }

    public CaptureManager(Activity activity, DecoratedBarcodeView barcodeView) {
        this.activity = activity;
        this.barcodeView = barcodeView;
        barcodeView.getBarcodeView().addStateListener(this.stateListener);
        this.handler = new Handler();
        this.inactivityTimer = new InactivityTimer(activity, new C02113());
        this.beepManager = new BeepManager(activity);
    }

    public void initializeFromIntent(Intent intent, Bundle savedInstanceState) {
        this.activity.getWindow().addFlags(128);
        if (savedInstanceState != null) {
            this.orientationLock = savedInstanceState.getInt(SAVED_ORIENTATION_LOCK, -1);
        }
        if (intent != null) {
            if (intent.getBooleanExtra(Scan.ORIENTATION_LOCKED, true)) {
                lockOrientation();
            }
            if (Scan.ACTION.equals(intent.getAction())) {
                this.barcodeView.initializeFromIntent(intent);
            }
            if (!intent.getBooleanExtra(Scan.BEEP_ENABLED, true)) {
                this.beepManager.setBeepEnabled(false);
            }
            if (intent.hasExtra(Scan.TIMEOUT)) {
                this.handler.postDelayed(new C02124(), intent.getLongExtra(Scan.TIMEOUT, 0));
            }
            if (intent.getBooleanExtra(Scan.BARCODE_IMAGE_ENABLED, false)) {
                this.returnBarcodeImagePath = true;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void lockOrientation() {
        if (this.orientationLock == -1) {
            int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
            int baseOrientation = this.activity.getResources().getConfiguration().orientation;
            int orientation = 0;
            if (baseOrientation == 2) {
                if (rotation == 0 || rotation == 1) {
                    orientation = 0;
                } else {
                    orientation = 8;
                }
            } else if (baseOrientation == 1) {
                if (rotation == 0 || rotation == 3) {
                    orientation = 1;
                } else {
                    orientation = 9;
                }
            }
            this.orientationLock = orientation;
        }
        this.activity.setRequestedOrientation(this.orientationLock);
    }

    public void decode() {
        this.barcodeView.decodeSingle(this.callback);
    }

    public void onResume() {
        if (VERSION.SDK_INT >= 23) {
            openCameraWithPermission();
        } else {
            this.barcodeView.resume();
        }
        this.inactivityTimer.start();
    }

    @TargetApi(23)
    private void openCameraWithPermission() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.CAMERA") == 0) {
            this.barcodeView.resume();
        } else if (!this.askedPermission) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.CAMERA"}, cameraPermissionReqCode);
            this.askedPermission = true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != cameraPermissionReqCode) {
            return;
        }
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            displayFrameworkBugMessageAndExit();
        } else {
            this.barcodeView.resume();
        }
    }

    public void onPause() {
        this.inactivityTimer.cancel();
        this.barcodeView.pauseAndWait();
    }

    public void onDestroy() {
        this.destroyed = true;
        this.inactivityTimer.cancel();
        this.handler.removeCallbacksAndMessages(null);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVED_ORIENTATION_LOCK, this.orientationLock);
    }

    public static Intent resultIntent(BarcodeResult rawResult, String barcodeImagePath) {
        Intent intent = new Intent(Scan.ACTION);
        intent.addFlags(524288);
        intent.putExtra(Scan.RESULT, rawResult.toString());
        intent.putExtra(Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
        byte[] rawBytes = rawResult.getRawBytes();
        if (rawBytes != null && rawBytes.length > 0) {
            intent.putExtra(Scan.RESULT_BYTES, rawBytes);
        }
        Map<ResultMetadataType, ?> metadata = rawResult.getResultMetadata();
        if (metadata != null) {
            if (metadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
                intent.putExtra(Scan.RESULT_UPC_EAN_EXTENSION, metadata.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
            }
            Number orientation = (Number) metadata.get(ResultMetadataType.ORIENTATION);
            if (orientation != null) {
                intent.putExtra(Scan.RESULT_ORIENTATION, orientation.intValue());
            }
            String ecLevel = (String) metadata.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
            if (ecLevel != null) {
                intent.putExtra(Scan.RESULT_ERROR_CORRECTION_LEVEL, ecLevel);
            }
            Iterable<byte[]> byteSegments = (Iterable) metadata.get(ResultMetadataType.BYTE_SEGMENTS);
            if (byteSegments != null) {
                int i = 0;
                for (byte[] byteSegment : byteSegments) {
                    intent.putExtra(Scan.RESULT_BYTE_SEGMENTS_PREFIX + i, byteSegment);
                    i++;
                }
            }
        }
        if (barcodeImagePath != null) {
            intent.putExtra(Scan.RESULT_BARCODE_IMAGE_PATH, barcodeImagePath);
        }
        return intent;
    }

    private String getBarcodeImagePath(BarcodeResult rawResult) {
        String barcodeImagePath = null;
        if (!this.returnBarcodeImagePath) {
            return barcodeImagePath;
        }
        Bitmap bmp = rawResult.getBitmap();
        try {
            File bitmapFile = File.createTempFile("barcodeimage", ".jpg", this.activity.getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(bitmapFile);
            bmp.compress(CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return bitmapFile.getAbsolutePath();
        } catch (IOException e) {
            Log.w(TAG, "Unable to create temporary file and store bitmap! " + e);
            return barcodeImagePath;
        }
    }

    private void finish() {
        this.activity.finish();
    }

    /* Access modifiers changed, original: protected */
    public void closeAndFinish() {
        if (this.barcodeView.getBarcodeView().isCameraClosed()) {
            finish();
        } else {
            this.finishWhenClosed = true;
        }
        this.barcodeView.pause();
        this.inactivityTimer.cancel();
    }

    /* Access modifiers changed, original: protected */
    public void returnResultTimeout() {
        Intent intent = new Intent(Scan.ACTION);
        intent.putExtra(Scan.TIMEOUT, true);
        this.activity.setResult(0, intent);
        closeAndFinish();
    }

    /* Access modifiers changed, original: protected */
    public void returnResult(BarcodeResult rawResult) {
        this.activity.setResult(-1, resultIntent(rawResult, getBarcodeImagePath(rawResult)));
        closeAndFinish();
    }

    /* Access modifiers changed, original: protected */
    public void displayFrameworkBugMessageAndExit() {
        if (!this.activity.isFinishing() && !this.destroyed && !this.finishWhenClosed) {
            Builder builder = new Builder(this.activity);
            builder.setTitle(this.activity.getString(C0186R.string.zxing_app_name));
            builder.setMessage(this.activity.getString(C0186R.string.zxing_msg_camera_framework_bug));
            builder.setPositiveButton(C0186R.string.zxing_button_ok, new C02135());
            builder.setOnCancelListener(new C02146());
            builder.show();
        }
    }

    public static int getCameraPermissionReqCode() {
        return cameraPermissionReqCode;
    }

    public static void setCameraPermissionReqCode(int cameraPermissionReqCode) {
        cameraPermissionReqCode = cameraPermissionReqCode;
    }
}
