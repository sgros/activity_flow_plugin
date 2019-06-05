// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import java.util.ArrayList;
import com.airbnb.lottie.model.content.Mask;
import android.graphics.Path;
import com.airbnb.lottie.model.content.ShapeData;
import java.util.List;

public class MaskKeyframeAnimation
{
    private final List<BaseKeyframeAnimation<ShapeData, Path>> maskAnimations;
    private final List<Mask> masks;
    private final List<BaseKeyframeAnimation<Integer, Integer>> opacityAnimations;
    
    public MaskKeyframeAnimation(final List<Mask> masks) {
        this.masks = masks;
        this.maskAnimations = new ArrayList<BaseKeyframeAnimation<ShapeData, Path>>(masks.size());
        this.opacityAnimations = new ArrayList<BaseKeyframeAnimation<Integer, Integer>>(masks.size());
        for (int i = 0; i < masks.size(); ++i) {
            this.maskAnimations.add(masks.get(i).getMaskPath().createAnimation());
            this.opacityAnimations.add(masks.get(i).getOpacity().createAnimation());
        }
    }
    
    public List<BaseKeyframeAnimation<ShapeData, Path>> getMaskAnimations() {
        return this.maskAnimations;
    }
    
    public List<Mask> getMasks() {
        return this.masks;
    }
    
    public List<BaseKeyframeAnimation<Integer, Integer>> getOpacityAnimations() {
        return this.opacityAnimations;
    }
}
