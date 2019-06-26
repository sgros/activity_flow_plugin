package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;
import java.util.List;

public class ScaleKeyframeAnimation extends KeyframeAnimation<ScaleXY> {
    private final ScaleXY scaleXY = new ScaleXY();

    public ScaleKeyframeAnimation(List<Keyframe<ScaleXY>> list) {
        super(list);
    }

    public ScaleXY getValue(Keyframe<ScaleXY> keyframe, float f) {
        Object obj = keyframe.startValue;
        if (obj != null) {
            Object obj2 = keyframe.endValue;
            if (obj2 != null) {
                ScaleXY scaleXY = (ScaleXY) obj;
                ScaleXY scaleXY2 = (ScaleXY) obj2;
                LottieValueCallback lottieValueCallback = this.valueCallback;
                if (lottieValueCallback != null) {
                    ScaleXY scaleXY3 = (ScaleXY) lottieValueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame.floatValue(), scaleXY, scaleXY2, f, getLinearCurrentKeyframeProgress(), getProgress());
                    if (scaleXY3 != null) {
                        return scaleXY3;
                    }
                }
                this.scaleXY.set(MiscUtils.lerp(scaleXY.getScaleX(), scaleXY2.getScaleX(), f), MiscUtils.lerp(scaleXY.getScaleY(), scaleXY2.getScaleY(), f));
                return this.scaleXY;
            }
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
