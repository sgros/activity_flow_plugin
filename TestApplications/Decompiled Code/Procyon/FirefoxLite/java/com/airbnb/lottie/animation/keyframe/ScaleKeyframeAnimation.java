// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.value.ScaleXY;

public class ScaleKeyframeAnimation extends KeyframeAnimation<ScaleXY>
{
    public ScaleKeyframeAnimation(final List<Keyframe<ScaleXY>> list) {
        super(list);
    }
    
    public ScaleXY getValue(final Keyframe<ScaleXY> keyframe, final float n) {
        if (keyframe.startValue != null && keyframe.endValue != null) {
            final ScaleXY scaleXY = keyframe.startValue;
            final ScaleXY scaleXY2 = keyframe.endValue;
            if (this.valueCallback != null) {
                final ScaleXY scaleXY3 = (ScaleXY)this.valueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame, (A)scaleXY, (A)scaleXY2, n, this.getLinearCurrentKeyframeProgress(), this.getProgress());
                if (scaleXY3 != null) {
                    return scaleXY3;
                }
            }
            return new ScaleXY(MiscUtils.lerp(scaleXY.getScaleX(), scaleXY2.getScaleX(), n), MiscUtils.lerp(scaleXY.getScaleY(), scaleXY2.getScaleY(), n));
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
