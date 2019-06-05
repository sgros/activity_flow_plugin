// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import java.util.Collections;
import android.graphics.PointF;

public class SplitDimensionPathKeyframeAnimation extends BaseKeyframeAnimation<PointF, PointF>
{
    private final PointF point;
    private final BaseKeyframeAnimation<Float, Float> xAnimation;
    private final BaseKeyframeAnimation<Float, Float> yAnimation;
    
    public SplitDimensionPathKeyframeAnimation(final BaseKeyframeAnimation<Float, Float> xAnimation, final BaseKeyframeAnimation<Float, Float> yAnimation) {
        super(Collections.emptyList());
        this.point = new PointF();
        this.xAnimation = xAnimation;
        this.yAnimation = yAnimation;
        this.setProgress(this.getProgress());
    }
    
    @Override
    public PointF getValue() {
        return this.getValue(null, 0.0f);
    }
    
    @Override
    PointF getValue(final Keyframe<PointF> keyframe, final float n) {
        return this.point;
    }
    
    @Override
    public void setProgress(final float n) {
        this.xAnimation.setProgress(n);
        this.yAnimation.setProgress(n);
        this.point.set((float)this.xAnimation.getValue(), (float)this.yAnimation.getValue());
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((AnimationListener)this.listeners.get(i)).onValueChanged();
        }
    }
}
