// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation;
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
    private final boolean hidden;
    private final BaseLayer layer;
    private final String name;
    
    public StrokeContent(final LottieDrawable lottieDrawable, final BaseLayer layer, final ShapeStroke shapeStroke) {
        super(lottieDrawable, layer, shapeStroke.getCapType().toPaintCap(), shapeStroke.getJoinType().toPaintJoin(), shapeStroke.getMiterLimit(), shapeStroke.getOpacity(), shapeStroke.getWidth(), shapeStroke.getLineDashPattern(), shapeStroke.getDashOffset());
        this.layer = layer;
        this.name = shapeStroke.getName();
        this.hidden = shapeStroke.isHidden();
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
        if (this.hidden) {
            return;
        }
        super.paint.setColor(((ColorKeyframeAnimation)this.colorAnimation).getIntValue());
        final BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation = this.colorFilterAnimation;
        if (colorFilterAnimation != null) {
            super.paint.setColorFilter((ColorFilter)colorFilterAnimation.getValue());
        }
        super.draw(canvas, matrix, n);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
