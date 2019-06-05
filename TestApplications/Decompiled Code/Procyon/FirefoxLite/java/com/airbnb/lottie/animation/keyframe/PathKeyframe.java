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
    
    public PathKeyframe(final LottieComposition lottieComposition, final Keyframe<PointF> keyframe) {
        super(lottieComposition, keyframe.startValue, keyframe.endValue, keyframe.interpolator, keyframe.startFrame, keyframe.endFrame);
        final boolean b = this.endValue != null && this.startValue != null && ((PointF)this.startValue).equals(((PointF)this.endValue).x, ((PointF)this.endValue).y);
        if (this.endValue != null && !b) {
            this.path = Utils.createPath((PointF)this.startValue, (PointF)this.endValue, keyframe.pathCp1, keyframe.pathCp2);
        }
    }
    
    Path getPath() {
        return this.path;
    }
}
