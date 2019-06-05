// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.MergePathsContent;
import com.airbnb.lottie.L;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;

public class MergePaths implements ContentModel
{
    private final MergePathsMode mode;
    private final String name;
    
    public MergePaths(final String name, final MergePathsMode mode) {
        this.name = name;
        this.mode = mode;
    }
    
    public MergePathsMode getMode() {
        return this.mode;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        if (!lottieDrawable.enableMergePathsForKitKatAndAbove()) {
            L.warn("Animation contains merge paths but they are disabled.");
            return null;
        }
        return new MergePathsContent(this);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MergePaths{mode=");
        sb.append(this.mode);
        sb.append('}');
        return sb.toString();
    }
    
    public enum MergePathsMode
    {
        Add, 
        ExcludeIntersections, 
        Intersect, 
        Merge, 
        Subtract;
        
        public static MergePathsMode forId(final int n) {
            switch (n) {
                default: {
                    return MergePathsMode.Merge;
                }
                case 5: {
                    return MergePathsMode.ExcludeIntersections;
                }
                case 4: {
                    return MergePathsMode.Intersect;
                }
                case 3: {
                    return MergePathsMode.Subtract;
                }
                case 2: {
                    return MergePathsMode.Add;
                }
                case 1: {
                    return MergePathsMode.Merge;
                }
            }
        }
    }
}
