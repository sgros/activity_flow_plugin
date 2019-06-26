// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.animation.content.MergePathsContent;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;

public class MergePaths implements ContentModel
{
    private final boolean hidden;
    private final MergePathsMode mode;
    private final String name;
    
    public MergePaths(final String name, final MergePathsMode mode, final boolean hidden) {
        this.name = name;
        this.mode = mode;
        this.hidden = hidden;
    }
    
    public MergePathsMode getMode() {
        return this.mode;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    @Override
    public Content toContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer) {
        if (!lottieDrawable.enableMergePathsForKitKatAndAbove()) {
            Logger.warning("Animation contains merge paths but they are disabled.");
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
        ADD, 
        EXCLUDE_INTERSECTIONS, 
        INTERSECT, 
        MERGE, 
        SUBTRACT;
        
        public static MergePathsMode forId(final int n) {
            if (n == 1) {
                return MergePathsMode.MERGE;
            }
            if (n == 2) {
                return MergePathsMode.ADD;
            }
            if (n == 3) {
                return MergePathsMode.SUBTRACT;
            }
            if (n == 4) {
                return MergePathsMode.INTERSECT;
            }
            if (n != 5) {
                return MergePathsMode.MERGE;
            }
            return MergePathsMode.EXCLUDE_INTERSECTIONS;
        }
    }
}
