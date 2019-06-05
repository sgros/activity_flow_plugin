// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;

public class Mask
{
    private final MaskMode maskMode;
    private final AnimatableShapeValue maskPath;
    private final AnimatableIntegerValue opacity;
    
    public Mask(final MaskMode maskMode, final AnimatableShapeValue maskPath, final AnimatableIntegerValue opacity) {
        this.maskMode = maskMode;
        this.maskPath = maskPath;
        this.opacity = opacity;
    }
    
    public MaskMode getMaskMode() {
        return this.maskMode;
    }
    
    public AnimatableShapeValue getMaskPath() {
        return this.maskPath;
    }
    
    public AnimatableIntegerValue getOpacity() {
        return this.opacity;
    }
    
    public enum MaskMode
    {
        MaskModeAdd, 
        MaskModeIntersect, 
        MaskModeSubtract;
    }
}
