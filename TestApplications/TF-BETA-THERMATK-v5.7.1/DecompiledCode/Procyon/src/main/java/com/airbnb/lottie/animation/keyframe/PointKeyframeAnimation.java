// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.LottieValueCallback;
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
        final PointF startValue = keyframe.startValue;
        if (startValue != null) {
            final PointF endValue = keyframe.endValue;
            if (endValue != null) {
                final PointF pointF = startValue;
                final PointF pointF2 = endValue;
                final LottieValueCallback<A> valueCallback = (LottieValueCallback<A>)super.valueCallback;
                if (valueCallback != null) {
                    final PointF pointF3 = (PointF)valueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame, (A)pointF, (A)pointF2, n, this.getLinearCurrentKeyframeProgress(), this.getProgress());
                    if (pointF3 != null) {
                        return pointF3;
                    }
                }
                final PointF point = this.point;
                final float x = pointF.x;
                final float x2 = pointF2.x;
                final float y = pointF.y;
                point.set(x + (x2 - x) * n, y + n * (pointF2.y - y));
                return this.point;
            }
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
