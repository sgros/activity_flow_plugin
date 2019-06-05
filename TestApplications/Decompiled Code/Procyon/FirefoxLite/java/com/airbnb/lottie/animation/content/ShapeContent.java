// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.content.ShapeTrimPath;
import java.util.List;
import com.airbnb.lottie.utils.Utils;
import android.graphics.Path$FillType;
import com.airbnb.lottie.model.content.ShapePath;
import com.airbnb.lottie.model.layer.BaseLayer;
import android.graphics.Path;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class ShapeContent implements PathContent, AnimationListener
{
    private boolean isPathValid;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final Path path;
    private final BaseKeyframeAnimation<?, Path> shapeAnimation;
    private TrimPathContent trimPath;
    
    public ShapeContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final ShapePath shapePath) {
        this.path = new Path();
        this.name = shapePath.getName();
        this.lottieDrawable = lottieDrawable;
        baseLayer.addAnimation(this.shapeAnimation = shapePath.getShapePath().createAnimation());
        this.shapeAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
    }
    
    private void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        this.path.set((Path)this.shapeAnimation.getValue());
        this.path.setFillType(Path$FillType.EVEN_ODD);
        Utils.applyTrimPathIfNeeded(this.path, this.trimPath);
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
                final TrimPathContent trimPath = (TrimPathContent)content;
                if (trimPath.getType() == ShapeTrimPath.Type.Simultaneously) {
                    (this.trimPath = trimPath).addListener(this);
                }
            }
        }
    }
}
