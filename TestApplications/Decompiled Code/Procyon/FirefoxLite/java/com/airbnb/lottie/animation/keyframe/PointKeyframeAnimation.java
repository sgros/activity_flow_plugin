// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import android.graphics.PointF;

public class PointKeyframeAnimation extends KeyframeAnimation<PointF>
{
    private final PointF point;
    
    public PointKeyframeAnimation(final List<Keyframe<PointF>> list) {
        super(list);
        this.point = new PointF();
    }
    
    public PointF getValue(final Keyframe<PointF> keyframe, final float n) {
        if (keyframe.startValue != null && keyframe.endValue != null) {
            final PointF pointF = keyframe.startValue;
            final PointF pointF2 = keyframe.endValue;
            if (this.valueCallback != null) {
                final PointF pointF3 = (PointF)this.valueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame, (A)pointF, (A)pointF2, n, this.getLinearCurrentKeyframeProgress(), this.getProgress());
                if (pointF3 != null) {
                    return pointF3;
                }
            }
            this.point.set(pointF.x + (pointF2.x - pointF.x) * n, pointF.y + n * (pointF2.y - pointF.y));
            return this.point;
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
