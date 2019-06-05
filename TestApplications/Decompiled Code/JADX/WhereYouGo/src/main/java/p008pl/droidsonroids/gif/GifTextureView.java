package p008pl.droidsonroids.gif;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.ImageView.ScaleType;
import java.io.IOException;
import java.lang.ref.WeakReference;
import p008pl.droidsonroids.gif.InputSource.AssetSource;
import p008pl.droidsonroids.gif.InputSource.ResourcesSource;
import p008pl.droidsonroids.gif.annotations.Beta;
import p009se.krka.kahlua.stdlib.BaseLib;

@RequiresApi(14)
/* renamed from: pl.droidsonroids.gif.GifTextureView */
public class GifTextureView extends TextureView {
    private static final ScaleType[] sScaleTypeArray = new ScaleType[]{ScaleType.MATRIX, ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER, ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE};
    private boolean mFreezesAnimation;
    private InputSource mInputSource;
    private RenderThread mRenderThread;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;
    private float mSpeedFactor = 1.0f;
    private final Matrix mTransform = new Matrix();

    /* renamed from: pl.droidsonroids.gif.GifTextureView$1 */
    static /* synthetic */ class C03821 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];

        static {
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    @Beta
    /* renamed from: pl.droidsonroids.gif.GifTextureView$PlaceholderDrawListener */
    public interface PlaceholderDrawListener {
        void onDrawPlaceholder(Canvas canvas);
    }

    /* renamed from: pl.droidsonroids.gif.GifTextureView$RenderThread */
    private static class RenderThread extends Thread implements SurfaceTextureListener {
        final ConditionVariable isSurfaceValid = new ConditionVariable();
        private GifInfoHandle mGifInfoHandle = new GifInfoHandle();
        private final WeakReference<GifTextureView> mGifTextureViewReference;
        private IOException mIOException;
        long[] mSavedState;

        RenderThread(GifTextureView gifTextureView) {
            super("GifRenderThread");
            this.mGifTextureViewReference = new WeakReference(gifTextureView);
        }

        public void run() {
            try {
                GifTextureView gifTextureView = (GifTextureView) this.mGifTextureViewReference.get();
                if (gifTextureView != null) {
                    this.mGifInfoHandle = gifTextureView.mInputSource.open();
                    this.mGifInfoHandle.setOptions(1, gifTextureView.isOpaque());
                    gifTextureView = (GifTextureView) this.mGifTextureViewReference.get();
                    if (gifTextureView == null) {
                        this.mGifInfoHandle.recycle();
                        return;
                    }
                    gifTextureView.setSuperSurfaceTextureListener(this);
                    boolean isSurfaceAvailable = gifTextureView.isAvailable();
                    this.isSurfaceValid.set(isSurfaceAvailable);
                    if (isSurfaceAvailable) {
                        gifTextureView.post(new Runnable() {
                            public void run() {
                                gifTextureView.updateTextureViewSize(RenderThread.this.mGifInfoHandle);
                            }
                        });
                    }
                    this.mGifInfoHandle.setSpeedFactor(gifTextureView.mSpeedFactor);
                    while (!isInterrupted()) {
                        try {
                            this.isSurfaceValid.block();
                            SurfaceTexture surfaceTexture = gifTextureView.getSurfaceTexture();
                            if (surfaceTexture != null) {
                                Surface surface = new Surface(surfaceTexture);
                                try {
                                    this.mGifInfoHandle.bindSurface(surface, this.mSavedState);
                                } finally {
                                    surface.release();
                                    surfaceTexture.release();
                                }
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    this.mGifInfoHandle.recycle();
                    this.mGifInfoHandle = new GifInfoHandle();
                }
            } catch (IOException ex) {
                this.mIOException = ex;
            }
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            GifTextureView gifTextureView = (GifTextureView) this.mGifTextureViewReference.get();
            if (gifTextureView != null) {
                gifTextureView.updateTextureViewSize(this.mGifInfoHandle);
            }
            this.isSurfaceValid.open();
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            this.isSurfaceValid.close();
            this.mGifInfoHandle.postUnbindSurface();
            return false;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }

        /* Access modifiers changed, original: 0000 */
        public void dispose(@NonNull GifTextureView gifTextureView, @Nullable PlaceholderDrawListener drawer) {
            this.isSurfaceValid.close();
            gifTextureView.setSuperSurfaceTextureListener(drawer != null ? new PlaceholderDrawingSurfaceTextureListener(drawer) : null);
            this.mGifInfoHandle.postUnbindSurface();
            interrupt();
        }
    }

    public GifTextureView(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public GifTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public GifTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(21)
    public GifTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs != null) {
            int scaleTypeIndex = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "scaleType", -1);
            if (scaleTypeIndex >= 0 && scaleTypeIndex < sScaleTypeArray.length) {
                this.mScaleType = sScaleTypeArray[scaleTypeIndex];
            }
            TypedArray textureViewAttributes = getContext().obtainStyledAttributes(attrs, C0386R.styleable.GifTextureView, defStyleAttr, defStyleRes);
            this.mInputSource = GifTextureView.findSource(textureViewAttributes);
            super.setOpaque(textureViewAttributes.getBoolean(C0386R.styleable.GifTextureView_isOpaque, false));
            textureViewAttributes.recycle();
            this.mFreezesAnimation = GifViewUtils.isFreezingAnimation(this, attrs, defStyleAttr, defStyleRes);
        } else {
            super.setOpaque(false);
        }
        if (!isInEditMode()) {
            this.mRenderThread = new RenderThread(this);
            if (this.mInputSource != null) {
                this.mRenderThread.start();
            }
        }
    }

    public void setSurfaceTextureListener(SurfaceTextureListener listener) {
        throw new UnsupportedOperationException("Changing SurfaceTextureListener is not supported");
    }

    public SurfaceTextureListener getSurfaceTextureListener() {
        return null;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        throw new UnsupportedOperationException("Changing SurfaceTexture is not supported");
    }

    private static InputSource findSource(TypedArray textureViewAttributes) {
        TypedValue value = new TypedValue();
        if (!textureViewAttributes.getValue(C0386R.styleable.GifTextureView_gifSource, value)) {
            return null;
        }
        if (value.resourceId != 0) {
            String resourceTypeName = textureViewAttributes.getResources().getResourceTypeName(value.resourceId);
            if (GifViewUtils.SUPPORTED_RESOURCE_TYPE_NAMES.contains(resourceTypeName)) {
                return new ResourcesSource(textureViewAttributes.getResources(), value.resourceId);
            }
            if (!BaseLib.TYPE_STRING.equals(resourceTypeName)) {
                throw new IllegalArgumentException("Expected string, drawable, mipmap or raw resource type. '" + resourceTypeName + "' is not supported");
            }
        }
        return new AssetSource(textureViewAttributes.getResources().getAssets(), value.string.toString());
    }

    private void setSuperSurfaceTextureListener(SurfaceTextureListener listener) {
        super.setSurfaceTextureListener(listener);
    }

    public void setOpaque(boolean opaque) {
        if (opaque != isOpaque()) {
            super.setOpaque(opaque);
            setInputSource(this.mInputSource);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        this.mRenderThread.dispose(this, null);
        super.onDetachedFromWindow();
        SurfaceTexture surfaceTexture = getSurfaceTexture();
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
    }

    public synchronized void setInputSource(@Nullable InputSource inputSource) {
        setInputSource(inputSource, null);
    }

    @Beta
    public synchronized void setInputSource(@Nullable InputSource inputSource, @Nullable PlaceholderDrawListener placeholderDrawListener) {
        this.mRenderThread.dispose(this, placeholderDrawListener);
        this.mInputSource = inputSource;
        this.mRenderThread = new RenderThread(this);
        if (inputSource != null) {
            this.mRenderThread.start();
        }
    }

    public void setSpeed(@FloatRange(from = 0.0d, fromInclusive = false) float factor) {
        this.mSpeedFactor = factor;
        this.mRenderThread.mGifInfoHandle.setSpeedFactor(factor);
    }

    @Nullable
    public IOException getIOException() {
        if (this.mRenderThread.mIOException != null) {
            return this.mRenderThread.mIOException;
        }
        return GifIOException.fromCode(this.mRenderThread.mGifInfoHandle.getNativeErrorCode());
    }

    public void setScaleType(@NonNull ScaleType scaleType) {
        this.mScaleType = scaleType;
        updateTextureViewSize(this.mRenderThread.mGifInfoHandle);
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    private void updateTextureViewSize(GifInfoHandle gifInfoHandle) {
        Matrix transform = new Matrix();
        float viewWidth = (float) getWidth();
        float viewHeight = (float) getHeight();
        float scaleX = ((float) gifInfoHandle.getWidth()) / viewWidth;
        float scaleY = ((float) gifInfoHandle.getHeight()) / viewHeight;
        RectF src = new RectF(0.0f, 0.0f, (float) gifInfoHandle.getWidth(), (float) gifInfoHandle.getHeight());
        RectF dst = new RectF(0.0f, 0.0f, viewWidth, viewHeight);
        float scaleRef;
        switch (C03821.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
            case 1:
                transform.setScale(scaleX, scaleY, viewWidth / 2.0f, viewHeight / 2.0f);
                break;
            case 2:
                scaleRef = 1.0f / Math.min(scaleX, scaleY);
                transform.setScale(scaleRef * scaleX, scaleRef * scaleY, viewWidth / 2.0f, viewHeight / 2.0f);
                break;
            case 3:
                if (((float) gifInfoHandle.getWidth()) > viewWidth || ((float) gifInfoHandle.getHeight()) > viewHeight) {
                    scaleRef = Math.min(1.0f / scaleX, 1.0f / scaleY);
                } else {
                    scaleRef = 1.0f;
                }
                transform.setScale(scaleRef * scaleX, scaleRef * scaleY, viewWidth / 2.0f, viewHeight / 2.0f);
                break;
            case 4:
                transform.setRectToRect(src, dst, ScaleToFit.CENTER);
                transform.preScale(scaleX, scaleY);
                break;
            case 5:
                transform.setRectToRect(src, dst, ScaleToFit.END);
                transform.preScale(scaleX, scaleY);
                break;
            case 6:
                transform.setRectToRect(src, dst, ScaleToFit.START);
                transform.preScale(scaleX, scaleY);
                break;
            case 7:
                return;
            case 8:
                transform.set(this.mTransform);
                transform.preScale(scaleX, scaleY);
                break;
        }
        super.setTransform(transform);
    }

    public void setImageMatrix(Matrix matrix) {
        setTransform(matrix);
    }

    public void setTransform(Matrix transform) {
        this.mTransform.set(transform);
        updateTextureViewSize(this.mRenderThread.mGifInfoHandle);
    }

    public Matrix getTransform(Matrix transform) {
        if (transform == null) {
            transform = new Matrix();
        }
        transform.set(this.mTransform);
        return transform;
    }

    public Parcelable onSaveInstanceState() {
        this.mRenderThread.mSavedState = this.mRenderThread.mGifInfoHandle.getSavedState();
        return new GifViewSavedState(super.onSaveInstanceState(), this.mFreezesAnimation ? this.mRenderThread.mSavedState : null);
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof GifViewSavedState) {
            GifViewSavedState ss = (GifViewSavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            this.mRenderThread.mSavedState = ss.mStates[0];
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setFreezesAnimation(boolean freezesAnimation) {
        this.mFreezesAnimation = freezesAnimation;
    }
}
