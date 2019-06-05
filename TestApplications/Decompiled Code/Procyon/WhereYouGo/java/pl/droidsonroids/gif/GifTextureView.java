// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.view.Surface;
import java.lang.ref.WeakReference;
import android.graphics.Canvas;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import pl.droidsonroids.gif.annotations.Beta;
import android.os.Parcelable;
import android.graphics.SurfaceTexture;
import android.support.annotation.Nullable;
import java.io.IOException;
import android.graphics.Matrix$ScaleToFit;
import android.graphics.RectF;
import android.view.View;
import android.util.TypedValue;
import android.content.res.TypedArray;
import android.view.TextureView$SurfaceTextureListener;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Matrix;
import android.widget.ImageView$ScaleType;
import android.support.annotation.RequiresApi;
import android.view.TextureView;

@RequiresApi(14)
public class GifTextureView extends TextureView
{
    private static final ImageView$ScaleType[] sScaleTypeArray;
    private boolean mFreezesAnimation;
    private InputSource mInputSource;
    private RenderThread mRenderThread;
    private ImageView$ScaleType mScaleType;
    private float mSpeedFactor;
    private final Matrix mTransform;
    
    static {
        sScaleTypeArray = new ImageView$ScaleType[] { ImageView$ScaleType.MATRIX, ImageView$ScaleType.FIT_XY, ImageView$ScaleType.FIT_START, ImageView$ScaleType.FIT_CENTER, ImageView$ScaleType.FIT_END, ImageView$ScaleType.CENTER, ImageView$ScaleType.CENTER_CROP, ImageView$ScaleType.CENTER_INSIDE };
    }
    
    public GifTextureView(final Context context) {
        super(context);
        this.mScaleType = ImageView$ScaleType.FIT_CENTER;
        this.mTransform = new Matrix();
        this.mSpeedFactor = 1.0f;
        this.init(null, 0, 0);
    }
    
    public GifTextureView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mScaleType = ImageView$ScaleType.FIT_CENTER;
        this.mTransform = new Matrix();
        this.mSpeedFactor = 1.0f;
        this.init(set, 0, 0);
    }
    
    public GifTextureView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mScaleType = ImageView$ScaleType.FIT_CENTER;
        this.mTransform = new Matrix();
        this.mSpeedFactor = 1.0f;
        this.init(set, n, 0);
    }
    
    @RequiresApi(21)
    public GifTextureView(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.mScaleType = ImageView$ScaleType.FIT_CENTER;
        this.mTransform = new Matrix();
        this.mSpeedFactor = 1.0f;
        this.init(set, n, n2);
    }
    
    private static InputSource findSource(final TypedArray typedArray) {
        final TypedValue typedValue = new TypedValue();
        InputSource inputSource;
        if (!typedArray.getValue(R.styleable.GifTextureView_gifSource, typedValue)) {
            inputSource = null;
        }
        else {
            if (typedValue.resourceId != 0) {
                final String resourceTypeName = typedArray.getResources().getResourceTypeName(typedValue.resourceId);
                if (GifViewUtils.SUPPORTED_RESOURCE_TYPE_NAMES.contains(resourceTypeName)) {
                    inputSource = new InputSource.ResourcesSource(typedArray.getResources(), typedValue.resourceId);
                    return inputSource;
                }
                if (!"string".equals(resourceTypeName)) {
                    throw new IllegalArgumentException("Expected string, drawable, mipmap or raw resource type. '" + resourceTypeName + "' is not supported");
                }
            }
            inputSource = new InputSource.AssetSource(typedArray.getResources().getAssets(), typedValue.string.toString());
        }
        return inputSource;
    }
    
    private void init(final AttributeSet set, final int n, final int n2) {
        if (set != null) {
            final int attributeIntValue = set.getAttributeIntValue("http://schemas.android.com/apk/res/android", "scaleType", -1);
            if (attributeIntValue >= 0 && attributeIntValue < GifTextureView.sScaleTypeArray.length) {
                this.mScaleType = GifTextureView.sScaleTypeArray[attributeIntValue];
            }
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.GifTextureView, n, n2);
            this.mInputSource = findSource(obtainStyledAttributes);
            super.setOpaque(obtainStyledAttributes.getBoolean(R.styleable.GifTextureView_isOpaque, false));
            obtainStyledAttributes.recycle();
            this.mFreezesAnimation = GifViewUtils.isFreezingAnimation((View)this, set, n, n2);
        }
        else {
            super.setOpaque(false);
        }
        if (!this.isInEditMode()) {
            this.mRenderThread = new RenderThread(this);
            if (this.mInputSource != null) {
                this.mRenderThread.start();
            }
        }
    }
    
    private void setSuperSurfaceTextureListener(final TextureView$SurfaceTextureListener surfaceTextureListener) {
        super.setSurfaceTextureListener(surfaceTextureListener);
    }
    
    private void updateTextureViewSize(final GifInfoHandle gifInfoHandle) {
        final Matrix transform = new Matrix();
        final float n = (float)this.getWidth();
        final float n2 = (float)this.getHeight();
        final float a = gifInfoHandle.getWidth() / n;
        final float b = gifInfoHandle.getHeight() / n2;
        final RectF rectF = new RectF(0.0f, 0.0f, (float)gifInfoHandle.getWidth(), (float)gifInfoHandle.getHeight());
        final RectF rectF2 = new RectF(0.0f, 0.0f, n, n2);
        switch (this.mScaleType) {
            case FIT_XY: {
                return;
            }
            case CENTER: {
                transform.setScale(a, b, n / 2.0f, n2 / 2.0f);
                break;
            }
            case CENTER_CROP: {
                final float n3 = 1.0f / Math.min(a, b);
                transform.setScale(n3 * a, n3 * b, n / 2.0f, n2 / 2.0f);
                break;
            }
            case CENTER_INSIDE: {
                float min;
                if (gifInfoHandle.getWidth() <= n && gifInfoHandle.getHeight() <= n2) {
                    min = 1.0f;
                }
                else {
                    min = Math.min(1.0f / a, 1.0f / b);
                }
                transform.setScale(min * a, min * b, n / 2.0f, n2 / 2.0f);
                break;
            }
            case FIT_CENTER: {
                transform.setRectToRect(rectF, rectF2, Matrix$ScaleToFit.CENTER);
                transform.preScale(a, b);
                break;
            }
            case FIT_END: {
                transform.setRectToRect(rectF, rectF2, Matrix$ScaleToFit.END);
                transform.preScale(a, b);
                break;
            }
            case FIT_START: {
                transform.setRectToRect(rectF, rectF2, Matrix$ScaleToFit.START);
                transform.preScale(a, b);
                break;
            }
            case MATRIX: {
                transform.set(this.mTransform);
                transform.preScale(a, b);
                break;
            }
        }
        super.setTransform(transform);
    }
    
    @Nullable
    public IOException getIOException() {
        IOException ex;
        if (this.mRenderThread.mIOException != null) {
            ex = this.mRenderThread.mIOException;
        }
        else {
            ex = GifIOException.fromCode(this.mRenderThread.mGifInfoHandle.getNativeErrorCode());
        }
        return ex;
    }
    
    public ImageView$ScaleType getScaleType() {
        return this.mScaleType;
    }
    
    public TextureView$SurfaceTextureListener getSurfaceTextureListener() {
        return null;
    }
    
    public Matrix getTransform(final Matrix matrix) {
        Matrix matrix2 = matrix;
        if (matrix == null) {
            matrix2 = new Matrix();
        }
        matrix2.set(this.mTransform);
        return matrix2;
    }
    
    protected void onDetachedFromWindow() {
        this.mRenderThread.dispose(this, null);
        super.onDetachedFromWindow();
        final SurfaceTexture surfaceTexture = this.getSurfaceTexture();
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof GifViewSavedState)) {
            super.onRestoreInstanceState(parcelable);
        }
        else {
            final GifViewSavedState gifViewSavedState = (GifViewSavedState)parcelable;
            super.onRestoreInstanceState(gifViewSavedState.getSuperState());
            this.mRenderThread.mSavedState = gifViewSavedState.mStates[0];
        }
    }
    
    public Parcelable onSaveInstanceState() {
        this.mRenderThread.mSavedState = this.mRenderThread.mGifInfoHandle.getSavedState();
        final Parcelable onSaveInstanceState = super.onSaveInstanceState();
        long[] mSavedState;
        if (this.mFreezesAnimation) {
            mSavedState = this.mRenderThread.mSavedState;
        }
        else {
            mSavedState = null;
        }
        return (Parcelable)new GifViewSavedState(onSaveInstanceState, mSavedState);
    }
    
    public void setFreezesAnimation(final boolean mFreezesAnimation) {
        this.mFreezesAnimation = mFreezesAnimation;
    }
    
    public void setImageMatrix(final Matrix transform) {
        this.setTransform(transform);
    }
    
    public void setInputSource(@Nullable final InputSource inputSource) {
        synchronized (this) {
            this.setInputSource(inputSource, null);
        }
    }
    
    @Beta
    public void setInputSource(@Nullable final InputSource mInputSource, @Nullable final PlaceholderDrawListener placeholderDrawListener) {
        synchronized (this) {
            this.mRenderThread.dispose(this, placeholderDrawListener);
            this.mInputSource = mInputSource;
            this.mRenderThread = new RenderThread(this);
            if (mInputSource != null) {
                this.mRenderThread.start();
            }
        }
    }
    
    public void setOpaque(final boolean opaque) {
        if (opaque != this.isOpaque()) {
            super.setOpaque(opaque);
            this.setInputSource(this.mInputSource);
        }
    }
    
    public void setScaleType(@NonNull final ImageView$ScaleType mScaleType) {
        this.mScaleType = mScaleType;
        this.updateTextureViewSize(this.mRenderThread.mGifInfoHandle);
    }
    
    public void setSpeed(@FloatRange(from = 0.0, fromInclusive = false) final float n) {
        this.mSpeedFactor = n;
        this.mRenderThread.mGifInfoHandle.setSpeedFactor(n);
    }
    
    public void setSurfaceTexture(final SurfaceTexture surfaceTexture) {
        throw new UnsupportedOperationException("Changing SurfaceTexture is not supported");
    }
    
    public void setSurfaceTextureListener(final TextureView$SurfaceTextureListener textureView$SurfaceTextureListener) {
        throw new UnsupportedOperationException("Changing SurfaceTextureListener is not supported");
    }
    
    public void setTransform(final Matrix matrix) {
        this.mTransform.set(matrix);
        this.updateTextureViewSize(this.mRenderThread.mGifInfoHandle);
    }
    
    @Beta
    public interface PlaceholderDrawListener
    {
        void onDrawPlaceholder(final Canvas p0);
    }
    
    private static class RenderThread extends Thread implements TextureView$SurfaceTextureListener
    {
        final ConditionVariable isSurfaceValid;
        private GifInfoHandle mGifInfoHandle;
        private final WeakReference<GifTextureView> mGifTextureViewReference;
        private IOException mIOException;
        long[] mSavedState;
        
        RenderThread(final GifTextureView referent) {
            super("GifRenderThread");
            this.isSurfaceValid = new ConditionVariable();
            this.mGifInfoHandle = new GifInfoHandle();
            this.mGifTextureViewReference = new WeakReference<GifTextureView>(referent);
        }
        
        void dispose(@NonNull final GifTextureView gifTextureView, @Nullable final PlaceholderDrawListener placeholderDrawListener) {
            this.isSurfaceValid.close();
            Object o;
            if (placeholderDrawListener != null) {
                o = new PlaceholderDrawingSurfaceTextureListener(placeholderDrawListener);
            }
            else {
                o = null;
            }
            gifTextureView.setSuperSurfaceTextureListener((TextureView$SurfaceTextureListener)o);
            this.mGifInfoHandle.postUnbindSurface();
            this.interrupt();
        }
        
        public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
            final GifTextureView gifTextureView = this.mGifTextureViewReference.get();
            if (gifTextureView != null) {
                gifTextureView.updateTextureViewSize(this.mGifInfoHandle);
            }
            this.isSurfaceValid.open();
        }
        
        public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
            this.isSurfaceValid.close();
            this.mGifInfoHandle.postUnbindSurface();
            return false;
        }
        
        public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
        }
        
        public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
        }
        
        @Override
        public void run() {
            while (true) {
                Object surfaceTexture = null;
                GifTextureView gifTextureView = null;
                Label_0073: {
                    try {
                        surfaceTexture = this.mGifTextureViewReference.get();
                        if (surfaceTexture != null) {
                            (this.mGifInfoHandle = ((GifTextureView)surfaceTexture).mInputSource.open()).setOptions('\u0001', ((GifTextureView)surfaceTexture).isOpaque());
                            gifTextureView = this.mGifTextureViewReference.get();
                            if (gifTextureView != null) {
                                break Label_0073;
                            }
                            this.mGifInfoHandle.recycle();
                        }
                        return;
                    }
                    catch (IOException mioException) {
                        this.mIOException = mioException;
                        return;
                    }
                }
                gifTextureView.setSuperSurfaceTextureListener((TextureView$SurfaceTextureListener)this);
                final boolean available = gifTextureView.isAvailable();
                this.isSurfaceValid.set(available);
                if (available) {
                    gifTextureView.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            gifTextureView.updateTextureViewSize(RenderThread.this.mGifInfoHandle);
                        }
                    });
                }
                this.mGifInfoHandle.setSpeedFactor(gifTextureView.mSpeedFactor);
                while (true) {
                    Label_0185: {
                        if (this.isInterrupted()) {
                            break Label_0185;
                        }
                        Surface surface = null;
                        try {
                            this.isSurfaceValid.block();
                            surfaceTexture = gifTextureView.getSurfaceTexture();
                            if (surfaceTexture != null) {
                                surface = new Surface((SurfaceTexture)surfaceTexture);
                                final RenderThread renderThread = this;
                                final GifInfoHandle gifInfoHandle = renderThread.mGifInfoHandle;
                                final Surface surface2 = surface;
                                final RenderThread renderThread2 = this;
                                final long[] array = renderThread2.mSavedState;
                                gifInfoHandle.bindSurface(surface2, array);
                                continue;
                            }
                            continue;
                        }
                        catch (InterruptedException surfaceTexture) {
                            Thread.currentThread().interrupt();
                        }
                        try {
                            final RenderThread renderThread = this;
                            final GifInfoHandle gifInfoHandle = renderThread.mGifInfoHandle;
                            final Surface surface2 = surface;
                            final RenderThread renderThread2 = this;
                            final long[] array = renderThread2.mSavedState;
                            gifInfoHandle.bindSurface(surface2, array);
                            continue;
                            this.mGifInfoHandle.recycle();
                            this.mGifInfoHandle = new GifInfoHandle();
                        }
                        finally {
                            surface.release();
                            ((SurfaceTexture)surfaceTexture).release();
                        }
                    }
                }
            }
        }
    }
}
