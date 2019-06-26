// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.LottieValueCallback;
import android.graphics.Path;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import android.graphics.PathMeasure;
import android.graphics.PointF;

public class PathKeyframeAnimation extends KeyframeAnimation<PointF>
{
    private PathMeasure pathMeasure;
    private PathKeyframe pathMeasureKeyframe;
    private final PointF point;
    private final float[] pos;
    
    public PathKeyframeAnimation(final List<? extends Keyframe<PointF>> list) {
        super(list);
        this.point = new PointF();
        this.pos = new float[2];
        this.pathMeasure = new PathMeasure();
    }
    
    public PointF getValue(final Keyframe<PointF> keyframe, final float n) {
        final PathKeyframe pathMeasureKeyframe = (PathKeyframe)keyframe;
        final Path path = pathMeasureKeyframe.getPath();
        if (path == null) {
            return keyframe.startValue;
        }
        final LottieValueCallback<A> valueCallback = (LottieValueCallback<A>)super.valueCallback;
        if (valueCallback != null) {
            final PointF pointF = (PointF)valueCallback.getValueInternal(pathMeasureKeyframe.startFrame, pathMeasureKeyframe.endFrame, (A)pathMeasureKeyframe.startValue, (A)pathMeasureKeyframe.endValue, this.getLinearCurrentKeyframeProgress(), n, this.getProgress());
            if (pointF != null) {
                return pointF;
            }
        }
        if (this.pathMeasureKeyframe != pathMeasureKeyframe) {
            this.pathMeasure.setPath(path, false);
            this.pathMeasureKeyframe = pathMeasureKeyframe;
        }
        final PathMeasure pathMeasure = this.pathMeasure;
        pathMeasure.getPosTan(n * pathMeasure.getLength(), this.pos, (float[])null);
        final PointF point = this.point;
        final float[] pos = this.pos;
        point.set(pos[0], pos[1]);
        return this.point;
    }
}
