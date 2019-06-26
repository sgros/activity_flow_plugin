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
import com.airbnb.lottie.animation.LPaint;
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
        this.paint = new LPaint();
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
        int intValue;
        if (super.transform.getOpacity() == null) {
            intValue = 100;
        }
        else {
            intValue = super.transform.getOpacity().getValue();
        }
        alpha = (int)(alpha / 255.0f * (alpha2 / 255.0f * intValue / 100.0f) * 255.0f);
        this.paint.setAlpha(alpha);
        final BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation = this.colorFilterAnimation;
        if (colorFilterAnimation != null) {
            this.paint.setColorFilter((ColorFilter)colorFilterAnimation.getValue());
        }
        if (alpha > 0) {
            final float[] points = this.points;
            points[1] = (points[0] = 0.0f);
            points[2] = (float)this.layerModel.getSolidWidth();
            final float[] points2 = this.points;
            points2[3] = 0.0f;
            points2[4] = (float)this.layerModel.getSolidWidth();
            this.points[5] = (float)this.layerModel.getSolidHeight();
            final float[] points3 = this.points;
            points3[6] = 0.0f;
            points3[7] = (float)this.layerModel.getSolidHeight();
            matrix.mapPoints(this.points);
            this.path.reset();
            final Path path = this.path;
            final float[] points4 = this.points;
            path.moveTo(points4[0], points4[1]);
            final Path path2 = this.path;
            final float[] points5 = this.points;
            path2.lineTo(points5[2], points5[3]);
            final Path path3 = this.path;
            final float[] points6 = this.points;
            path3.lineTo(points6[4], points6[5]);
            final Path path4 = this.path;
            final float[] points7 = this.points;
            path4.lineTo(points7[6], points7[7]);
            final Path path5 = this.path;
            final float[] points8 = this.points;
            path5.lineTo(points8[0], points8[1]);
            this.path.close();
            canvas.drawPath(this.path, this.paint);
        }
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix, final boolean b) {
        super.getBounds(rectF, matrix, b);
        this.rect.set(0.0f, 0.0f, (float)this.layerModel.getSolidWidth(), (float)this.layerModel.getSolidHeight());
        super.boundsMatrix.mapRect(this.rect);
        rectF.set(this.rect);
    }
}
