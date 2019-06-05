// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import android.graphics.Path;
import com.airbnb.lottie.model.content.ShapeData;

public class ShapeKeyframeAnimation extends BaseKeyframeAnimation<ShapeData, Path>
{
    private final Path tempPath;
    private final ShapeData tempShapeData;
    
    public ShapeKeyframeAnimation(final List<Keyframe<ShapeData>> list) {
        super(list);
        this.tempShapeData = new ShapeData();
        this.tempPath = new Path();
    }
    
    public Path getValue(final Keyframe<ShapeData> keyframe, final float n) {
        this.tempShapeData.interpolateBetween(keyframe.startValue, keyframe.endValue, n);
        MiscUtils.getPathFromData(this.tempShapeData, this.tempPath);
        return this.tempPath;
    }
}
