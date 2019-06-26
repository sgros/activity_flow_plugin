// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.LottieComposition;
import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;

public class PathKeyframe extends Keyframe<PointF>
{
    private Path path;
    private final Keyframe<PointF> pointKeyFrame;
    
    public PathKeyframe(final LottieComposition lottieComposition, final Keyframe<PointF> pointKeyFrame) {
        super(lottieComposition, pointKeyFrame.startValue, pointKeyFrame.endValue, pointKeyFrame.interpolator, pointKeyFrame.startFrame, pointKeyFrame.endFrame);
        this.pointKeyFrame = pointKeyFrame;
        this.createPath();
    }
    
    public void createPath() {
        final T endValue = (T)super.endValue;
        boolean b = false;
        Label_0049: {
            if (endValue != null) {
                final T startValue = (T)super.startValue;
                if (startValue != null && ((PointF)startValue).equals(((PointF)endValue).x, ((PointF)endValue).y)) {
                    b = true;
                    break Label_0049;
                }
            }
            b = false;
        }
        final T endValue2 = (T)super.endValue;
        if (endValue2 != null && !b) {
            final PointF pointF = (PointF)super.startValue;
            final PointF pointF2 = (PointF)endValue2;
            final Keyframe<PointF> pointKeyFrame = this.pointKeyFrame;
            this.path = Utils.createPath(pointF, pointF2, pointKeyFrame.pathCp1, pointKeyFrame.pathCp2);
        }
    }
    
    Path getPath() {
        return this.path;
    }
}
