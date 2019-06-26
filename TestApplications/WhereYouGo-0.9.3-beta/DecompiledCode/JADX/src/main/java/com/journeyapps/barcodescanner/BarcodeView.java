package com.journeyapps.barcodescanner;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.C0186R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarcodeView extends CameraPreview {
    private BarcodeCallback callback = null;
    private DecodeMode decodeMode = DecodeMode.NONE;
    private DecoderFactory decoderFactory;
    private DecoderThread decoderThread;
    private final Callback resultCallback = new C02031();
    private Handler resultHandler;

    /* renamed from: com.journeyapps.barcodescanner.BarcodeView$1 */
    class C02031 implements Callback {
        C02031() {
        }

        public boolean handleMessage(Message message) {
            if (message.what == C0186R.C0185id.zxing_decode_succeeded) {
                BarcodeResult result = message.obj;
                if (result == null || BarcodeView.this.callback == null || BarcodeView.this.decodeMode == DecodeMode.NONE) {
                    return true;
                }
                BarcodeView.this.callback.barcodeResult(result);
                if (BarcodeView.this.decodeMode != DecodeMode.SINGLE) {
                    return true;
                }
                BarcodeView.this.stopDecoding();
                return true;
            } else if (message.what == C0186R.C0185id.zxing_decode_failed) {
                return true;
            } else {
                if (message.what != C0186R.C0185id.zxing_possible_result_points) {
                    return false;
                }
                List<ResultPoint> resultPoints = message.obj;
                if (BarcodeView.this.callback == null || BarcodeView.this.decodeMode == DecodeMode.NONE) {
                    return true;
                }
                BarcodeView.this.callback.possibleResultPoints(resultPoints);
                return true;
            }
        }
    }

    private enum DecodeMode {
        NONE,
        SINGLE,
        CONTINUOUS
    }

    public BarcodeView(Context context) {
        super(context);
        initialize();
    }

    public BarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public BarcodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        this.decoderFactory = new DefaultDecoderFactory();
        this.resultHandler = new Handler(this.resultCallback);
    }

    public void setDecoderFactory(DecoderFactory decoderFactory) {
        Util.validateMainThread();
        this.decoderFactory = decoderFactory;
        if (this.decoderThread != null) {
            this.decoderThread.setDecoder(createDecoder());
        }
    }

    private Decoder createDecoder() {
        if (this.decoderFactory == null) {
            this.decoderFactory = createDefaultDecoderFactory();
        }
        DecoderResultPointCallback callback = new DecoderResultPointCallback();
        Map<DecodeHintType, Object> hints = new HashMap();
        hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, callback);
        Decoder decoder = this.decoderFactory.createDecoder(hints);
        callback.setDecoder(decoder);
        return decoder;
    }

    public DecoderFactory getDecoderFactory() {
        return this.decoderFactory;
    }

    public void decodeSingle(BarcodeCallback callback) {
        this.decodeMode = DecodeMode.SINGLE;
        this.callback = callback;
        startDecoderThread();
    }

    public void decodeContinuous(BarcodeCallback callback) {
        this.decodeMode = DecodeMode.CONTINUOUS;
        this.callback = callback;
        startDecoderThread();
    }

    public void stopDecoding() {
        this.decodeMode = DecodeMode.NONE;
        this.callback = null;
        stopDecoderThread();
    }

    /* Access modifiers changed, original: protected */
    public DecoderFactory createDefaultDecoderFactory() {
        return new DefaultDecoderFactory();
    }

    private void startDecoderThread() {
        stopDecoderThread();
        if (this.decodeMode != DecodeMode.NONE && isPreviewActive()) {
            this.decoderThread = new DecoderThread(getCameraInstance(), createDecoder(), this.resultHandler);
            this.decoderThread.setCropRect(getPreviewFramingRect());
            this.decoderThread.start();
        }
    }

    /* Access modifiers changed, original: protected */
    public void previewStarted() {
        super.previewStarted();
        startDecoderThread();
    }

    private void stopDecoderThread() {
        if (this.decoderThread != null) {
            this.decoderThread.stop();
            this.decoderThread = null;
        }
    }

    public void pause() {
        stopDecoderThread();
        super.pause();
    }
}
