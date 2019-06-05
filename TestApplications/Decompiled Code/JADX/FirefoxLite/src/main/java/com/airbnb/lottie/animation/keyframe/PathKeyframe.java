package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;

public class PathKeyframe extends Keyframe<PointF> {
    private Path path;

    public PathKeyframe(LottieComposition lottieComposition, Keyframe<PointF> keyframe) {
        super(lottieComposition, keyframe.startValue, keyframe.endValue, keyframe.interpolator, keyframe.startFrame, keyframe.endFrame);
        Object obj = (this.endValue == null || this.startValue == null || !((PointF) this.startValue).equals(((PointF) this.endValue).x, ((PointF) this.endValue).y)) ? null : 1;
        if (this.endValue != null && obj == null) {
            this.path = Utils.createPath((PointF) this.startValue, (PointF) this.endValue, keyframe.pathCp1, keyframe.pathCp2);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Path getPath() {
        return this.path;
    }
}
