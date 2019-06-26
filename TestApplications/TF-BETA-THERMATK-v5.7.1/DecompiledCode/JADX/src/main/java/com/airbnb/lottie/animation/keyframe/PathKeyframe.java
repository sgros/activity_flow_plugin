package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;

public class PathKeyframe extends Keyframe<PointF> {
    private Path path;
    private final Keyframe<PointF> pointKeyFrame;

    public PathKeyframe(LottieComposition lottieComposition, Keyframe<PointF> keyframe) {
        super(lottieComposition, keyframe.startValue, keyframe.endValue, keyframe.interpolator, keyframe.startFrame, keyframe.endFrame);
        this.pointKeyFrame = keyframe;
        createPath();
    }

    public void createPath() {
        Object obj;
        Object obj2 = this.endValue;
        if (obj2 != null) {
            obj = this.startValue;
            if (obj != null && ((PointF) obj).equals(((PointF) obj2).x, ((PointF) obj2).y)) {
                obj2 = 1;
                obj = this.endValue;
                if (obj != null && r0 == null) {
                    PointF pointF = (PointF) this.startValue;
                    PointF pointF2 = (PointF) obj;
                    Keyframe keyframe = this.pointKeyFrame;
                    this.path = Utils.createPath(pointF, pointF2, keyframe.pathCp1, keyframe.pathCp2);
                    return;
                }
            }
        }
        obj2 = null;
        obj = this.endValue;
        if (obj != null) {
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Path getPath() {
        return this.path;
    }
}
