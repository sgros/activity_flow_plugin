package com.airbnb.lottie.animation.keyframe;

import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class PointKeyframeAnimation extends KeyframeAnimation<PointF> {
    private final PointF point = new PointF();

    public PointKeyframeAnimation(List<Keyframe<PointF>> list) {
        super(list);
    }

    public PointF getValue(Keyframe<PointF> keyframe, float f) {
        Object obj = keyframe.startValue;
        if (obj != null) {
            Object obj2 = keyframe.endValue;
            if (obj2 != null) {
                PointF pointF;
                PointF pointF2 = (PointF) obj;
                PointF pointF3 = (PointF) obj2;
                LottieValueCallback lottieValueCallback = this.valueCallback;
                if (lottieValueCallback != null) {
                    pointF = (PointF) lottieValueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame.floatValue(), pointF2, pointF3, f, getLinearCurrentKeyframeProgress(), getProgress());
                    if (pointF != null) {
                        return pointF;
                    }
                }
                pointF = this.point;
                float f2 = pointF2.x;
                f2 += (pointF3.x - f2) * f;
                float f3 = pointF2.y;
                pointF.set(f2, f3 + (f * (pointF3.y - f3)));
                return this.point;
            }
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
