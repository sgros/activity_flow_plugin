package com.journeyapps.barcodescanner;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.android.C0186R;
import com.journeyapps.barcodescanner.camera.CameraInstance;
import com.journeyapps.barcodescanner.camera.PreviewCallback;

public class DecoderThread {
    private static final String TAG = DecoderThread.class.getSimpleName();
    private final Object LOCK = new Object();
    private final Callback callback = new C02171();
    private CameraInstance cameraInstance;
    private Rect cropRect;
    private Decoder decoder;
    private Handler handler;
    private final PreviewCallback previewCallback = new C02182();
    private Handler resultHandler;
    private boolean running = false;
    private HandlerThread thread;

    /* renamed from: com.journeyapps.barcodescanner.DecoderThread$1 */
    class C02171 implements Callback {
        C02171() {
        }

        public boolean handleMessage(Message message) {
            if (message.what == C0186R.C0185id.zxing_decode) {
                DecoderThread.this.decode((SourceData) message.obj);
            } else if (message.what == C0186R.C0185id.zxing_preview_failed) {
                DecoderThread.this.requestNextPreview();
            }
            return true;
        }
    }

    /* renamed from: com.journeyapps.barcodescanner.DecoderThread$2 */
    class C02182 implements PreviewCallback {
        C02182() {
        }

        public void onPreview(SourceData sourceData) {
            synchronized (DecoderThread.this.LOCK) {
                if (DecoderThread.this.running) {
                    DecoderThread.this.handler.obtainMessage(C0186R.C0185id.zxing_decode, sourceData).sendToTarget();
                }
            }
        }

        public void onPreviewError(Exception e) {
            synchronized (DecoderThread.this.LOCK) {
                if (DecoderThread.this.running) {
                    DecoderThread.this.handler.obtainMessage(C0186R.C0185id.zxing_preview_failed).sendToTarget();
                }
            }
        }
    }

    public DecoderThread(CameraInstance cameraInstance, Decoder decoder, Handler resultHandler) {
        Util.validateMainThread();
        this.cameraInstance = cameraInstance;
        this.decoder = decoder;
        this.resultHandler = resultHandler;
    }

    public Decoder getDecoder() {
        return this.decoder;
    }

    public void setDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    public Rect getCropRect() {
        return this.cropRect;
    }

    public void setCropRect(Rect cropRect) {
        this.cropRect = cropRect;
    }

    public void start() {
        Util.validateMainThread();
        this.thread = new HandlerThread(TAG);
        this.thread.start();
        this.handler = new Handler(this.thread.getLooper(), this.callback);
        this.running = true;
        requestNextPreview();
    }

    public void stop() {
        Util.validateMainThread();
        synchronized (this.LOCK) {
            this.running = false;
            this.handler.removeCallbacksAndMessages(null);
            this.thread.quit();
        }
    }

    private void requestNextPreview() {
        if (this.cameraInstance.isOpen()) {
            this.cameraInstance.requestPreview(this.previewCallback);
        }
    }

    /* Access modifiers changed, original: protected */
    public LuminanceSource createSource(SourceData sourceData) {
        if (this.cropRect == null) {
            return null;
        }
        return sourceData.createSource();
    }

    private void decode(SourceData sourceData) {
        long start = System.currentTimeMillis();
        Result rawResult = null;
        sourceData.setCropRect(this.cropRect);
        LuminanceSource source = createSource(sourceData);
        if (source != null) {
            rawResult = this.decoder.decode(source);
        }
        if (rawResult != null) {
            Log.d(TAG, "Found barcode in " + (System.currentTimeMillis() - start) + " ms");
            if (this.resultHandler != null) {
                Message message = Message.obtain(this.resultHandler, C0186R.C0185id.zxing_decode_succeeded, new BarcodeResult(rawResult, sourceData));
                message.setData(new Bundle());
                message.sendToTarget();
            }
        } else if (this.resultHandler != null) {
            Message.obtain(this.resultHandler, C0186R.C0185id.zxing_decode_failed).sendToTarget();
        }
        if (this.resultHandler != null) {
            Message.obtain(this.resultHandler, C0186R.C0185id.zxing_possible_result_points, this.decoder.getPossibleResultPoints()).sendToTarget();
        }
        requestNextPreview();
    }
}
