// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import android.graphics.Matrix;
import android.graphics.Canvas;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.content.ShapeStroke;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.layer.BaseLayer;
import android.graphics.ColorFilter;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class StrokeContent extends BaseStrokeContent
{
    private final BaseKeyframeAnimation<Integer, Integer> colorAnimation;
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation;
    private final BaseLayer layer;
    private final String name;
    
    public StrokeContent(final LottieDrawable lottieDrawable, final BaseLayer layer, final ShapeStroke shapeStroke) {
        super(lottieDrawable, layer, shapeStroke.getCapType().toPaintCap(), shapeStroke.getJoinType().toPaintJoin(), shapeStroke.getMiterLimit(), shapeStroke.getOpacity(), shapeStroke.getWidth(), shapeStroke.getLineDashPattern(), shapeStroke.getDashOffset());
        this.layer = layer;
        this.name = shapeStroke.getName();
        (this.colorAnimation = shapeStroke.getColor().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        layer.addAnimation(this.colorAnimation);
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> valueCallback) {
        super.addValueCallback(t, valueCallback);
        if (t == LottieProperty.STROKE_COLOR) {
            this.colorAnimation.setValueCallback((LottieValueCallback<Integer>)valueCallback);
        }
        else if (t == LottieProperty.COLOR_FILTER) {
            if (valueCallback == null) {
                this.colorFilterAnimation = null;
            }
            else {
                (this.colorFilterAnimation = new ValueCallbackKeyframeAnimation<ColorFilter, ColorFilter>((LottieValueCallback<ColorFilter>)valueCallback)).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
                this.layer.addAnimation(this.colorAnimation);
            }
        }
    }
    
    @Override
    public void draw(final Canvas canvas, final Matrix matrix, final int n) {
        this.paint.setColor((int)this.colorAnimation.getValue());
        if (this.colorFilterAnimation != null) {
            this.paint.setColorFilter((ColorFilter)this.colorFilterAnimation.getValue());
        }
        super.draw(canvas, matrix, n);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
