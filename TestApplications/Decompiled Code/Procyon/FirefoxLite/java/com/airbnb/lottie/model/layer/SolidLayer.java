// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.layer;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Canvas;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import android.graphics.Paint$Style;
import com.airbnb.lottie.LottieDrawable;
import android.graphics.RectF;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.ColorFilter;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class SolidLayer extends BaseLayer
{
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation;
    private final Layer layerModel;
    private final Paint paint;
    private final Path path;
    private final float[] points;
    private final RectF rect;
    
    SolidLayer(final LottieDrawable lottieDrawable, final Layer layerModel) {
        super(lottieDrawable, layerModel);
        this.rect = new RectF();
        this.paint = new Paint();
        this.points = new float[8];
        this.path = new Path();
        this.layerModel = layerModel;
        this.paint.setAlpha(0);
        this.paint.setStyle(Paint$Style.FILL);
        this.paint.setColor(layerModel.getSolidColor());
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
    
    public void drawLayer(final Canvas canvas, final Matrix matrix, int alpha) {
        final int alpha2 = Color.alpha(this.layerModel.getSolidColor());
        if (alpha2 == 0) {
            return;
        }
        alpha = (int)(alpha / 255.0f * (alpha2 / 255.0f * this.transform.getOpacity().getValue() / 100.0f) * 255.0f);
        this.paint.setAlpha(alpha);
        if (this.colorFilterAnimation != null) {
            this.paint.setColorFilter((ColorFilter)this.colorFilterAnimation.getValue());
        }
        if (alpha > 0) {
            this.points[0] = 0.0f;
            this.points[1] = 0.0f;
            this.points[2] = (float)this.layerModel.getSolidWidth();
            this.points[3] = 0.0f;
            this.points[4] = (float)this.layerModel.getSolidWidth();
            this.points[5] = (float)this.layerModel.getSolidHeight();
            this.points[6] = 0.0f;
            this.points[7] = (float)this.layerModel.getSolidHeight();
            matrix.mapPoints(this.points);
            this.path.reset();
            this.path.moveTo(this.points[0], this.points[1]);
            this.path.lineTo(this.points[2], this.points[3]);
            this.path.lineTo(this.points[4], this.points[5]);
            this.path.lineTo(this.points[6], this.points[7]);
            this.path.lineTo(this.points[0], this.points[1]);
            this.path.close();
            canvas.drawPath(this.path, this.paint);
        }
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix) {
        super.getBounds(rectF, matrix);
        this.rect.set(0.0f, 0.0f, (float)this.layerModel.getSolidWidth(), (float)this.layerModel.getSolidHeight());
        this.boundsMatrix.mapRect(this.rect);
        rectF.set(this.rect);
    }
}
