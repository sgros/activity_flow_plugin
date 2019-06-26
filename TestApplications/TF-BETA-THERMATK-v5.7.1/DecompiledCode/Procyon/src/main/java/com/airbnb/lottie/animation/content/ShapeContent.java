// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.content.ShapeTrimPath;
import java.util.List;
import android.graphics.Path$FillType;
import com.airbnb.lottie.model.content.ShapePath;
import com.airbnb.lottie.model.layer.BaseLayer;
import android.graphics.Path;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class ShapeContent implements PathContent, AnimationListener
{
    private final boolean hidden;
    private boolean isPathValid;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final Path path;
    private final BaseKeyframeAnimation<?, Path> shapeAnimation;
    private CompoundTrimPathContent trimPaths;
    
    public ShapeContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final ShapePath shapePath) {
        this.path = new Path();
        this.trimPaths = new CompoundTrimPathContent();
        this.name = shapePath.getName();
        this.hidden = shapePath.isHidden();
        this.lottieDrawable = lottieDrawable;
        baseLayer.addAnimation(this.shapeAnimation = shapePath.getShapePath().createAnimation());
        this.shapeAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
    }
    
    private void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }
    
    @Override
    public Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        this.path.set((Path)this.shapeAnimation.getValue());
        this.path.setFillType(Path$FillType.EVEN_ODD);
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }
    
    @Override
    public void onValueChanged() {
        this.invalidate();
    }
    
    @Override
    public void setContents(final List<Content> list, final List<Content> list2) {
        for (int i = 0; i < list.size(); ++i) {
            final Content content = list.get(i);
            if (content instanceof TrimPathContent) {
                final TrimPathContent trimPathContent = (TrimPathContent)content;
                if (trimPathContent.getType() == ShapeTrimPath.Type.SIMULTANEOUSLY) {
                    this.trimPaths.addTrimPath(trimPathContent);
                    trimPathContent.addListener(this);
                }
            }
        }
    }
}
