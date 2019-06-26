// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.value.ScaleXY;

public class ScaleKeyframeAnimation extends KeyframeAnimation<ScaleXY>
{
    private final ScaleXY scaleXY;
    
    public ScaleKeyframeAnimation(final List<Keyframe<ScaleXY>> list) {
        super(list);
        this.scaleXY = new ScaleXY();
    }
    
    public ScaleXY getValue(final Keyframe<ScaleXY> keyframe, final float n) {
        final ScaleXY startValue = keyframe.startValue;
        if (startValue != null) {
            final ScaleXY endValue = keyframe.endValue;
            if (endValue != null) {
                final ScaleXY scaleXY = startValue;
                final ScaleXY scaleXY2 = endValue;
                final LottieValueCallback<A> valueCallback = (LottieValueCallback<A>)super.valueCallback;
                if (valueCallback != null) {
                    final ScaleXY scaleXY3 = (ScaleXY)valueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame, (A)scaleXY, (A)scaleXY2, n, this.getLinearCurrentKeyframeProgress(), this.getProgress());
                    if (scaleXY3 != null) {
                        return scaleXY3;
                    }
                }
                this.scaleXY.set(MiscUtils.lerp(scaleXY.getScaleX(), scaleXY2.getScaleX(), n), MiscUtils.lerp(scaleXY.getScaleY(), scaleXY2.getScaleY(), n));
                return this.scaleXY;
            }
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
