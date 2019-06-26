// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.layer;

import android.graphics.RectF;
import com.airbnb.lottie.utils.Utils;
import android.graphics.Matrix;
import android.graphics.Canvas;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import android.graphics.Bitmap;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.LottieDrawable;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.ColorFilter;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class ImageLayer extends BaseLayer
{
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation;
    private final Rect dst;
    private final Paint paint;
    private final Rect src;
    
    ImageLayer(final LottieDrawable lottieDrawable, final Layer layer) {
        super(lottieDrawable, layer);
        this.paint = new LPaint(3);
        this.src = new Rect();
        this.dst = new Rect();
    }
    
    private Bitmap getBitmap() {
        return super.lottieDrawable.getImageAsset(super.layerModel.getRefId());
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t == LottieProperty.COLOR_FILTER) {
            if (lottieValueCallback == null) {
                this.colorFilterAnimation = null;
            }
            else {
                this.colorFilterAnimation = new ValueCallbackKeyframeAnimation<ColorFilter, ColorFilter>((LottieValueCallback<ColorFilter>)lottieValueCallback);
            }
        }
    }
    
    public void drawLayer(final Canvas canvas, final Matrix matrix, final int alpha) {
        final Bitmap bitmap = this.getBitmap();
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                final float dpScale = Utils.dpScale();
                this.paint.setAlpha(alpha);
                final BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation = this.colorFilterAnimation;
                if (colorFilterAnimation != null) {
                    this.paint.setColorFilter((ColorFilter)colorFilterAnimation.getValue());
                }
                canvas.save();
                canvas.concat(matrix);
                this.src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
                this.dst.set(0, 0, (int)(bitmap.getWidth() * dpScale), (int)(bitmap.getHeight() * dpScale));
                canvas.drawBitmap(bitmap, this.src, this.dst, this.paint);
                canvas.restore();
            }
        }
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix, final boolean b) {
        super.getBounds(rectF, matrix, b);
        final Bitmap bitmap = this.getBitmap();
        if (bitmap != null) {
            rectF.set(0.0f, 0.0f, bitmap.getWidth() * Utils.dpScale(), bitmap.getHeight() * Utils.dpScale());
            super.boundsMatrix.mapRect(rectF);
        }
    }
}
