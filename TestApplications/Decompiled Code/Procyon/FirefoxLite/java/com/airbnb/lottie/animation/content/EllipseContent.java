// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.utils.MiscUtils;
import java.util.List;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.layer.BaseLayer;
import android.graphics.PointF;
import android.graphics.Path;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.content.CircleShape;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class EllipseContent implements KeyPathElementContent, PathContent, AnimationListener
{
    private final CircleShape circleShape;
    private boolean isPathValid;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final Path path;
    private final BaseKeyframeAnimation<?, PointF> positionAnimation;
    private final BaseKeyframeAnimation<?, PointF> sizeAnimation;
    private TrimPathContent trimPath;
    
    public EllipseContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final CircleShape circleShape) {
        this.path = new Path();
        this.name = circleShape.getName();
        this.lottieDrawable = lottieDrawable;
        this.sizeAnimation = circleShape.getSize().createAnimation();
        this.positionAnimation = circleShape.getPosition().createAnimation();
        this.circleShape = circleShape;
        baseLayer.addAnimation(this.sizeAnimation);
        baseLayer.addAnimation(this.positionAnimation);
        this.sizeAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.positionAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
    }
    
    private void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.ELLIPSE_SIZE) {
            this.sizeAnimation.setValueCallback((LottieValueCallback<PointF>)lottieValueCallback);
        }
        else if (t == LottieProperty.POSITION) {
            this.positionAnimation.setValueCallback((LottieValueCallback<PointF>)lottieValueCallback);
        }
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
        final PointF pointF = this.sizeAnimation.getValue();
        final float n = pointF.x / 2.0f;
        final float n2 = pointF.y / 2.0f;
        final float n3 = n * 0.55228f;
        final float n4 = 0.55228f * n2;
        this.path.reset();
        if (this.circleShape.isReversed()) {
            final Path path = this.path;
            final float n5 = -n2;
            path.moveTo(0.0f, n5);
            final Path path2 = this.path;
            final float n6 = 0.0f - n3;
            final float n7 = -n;
            final float n8 = 0.0f - n4;
            path2.cubicTo(n6, n5, n7, n8, n7, 0.0f);
            final Path path3 = this.path;
            final float n9 = n4 + 0.0f;
            path3.cubicTo(n7, n9, n6, n2, 0.0f, n2);
            final Path path4 = this.path;
            final float n10 = n3 + 0.0f;
            path4.cubicTo(n10, n2, n, n9, n, 0.0f);
            this.path.cubicTo(n, n8, n10, n5, 0.0f, n5);
        }
        else {
            final Path path5 = this.path;
            final float n11 = -n2;
            path5.moveTo(0.0f, n11);
            final Path path6 = this.path;
            final float n12 = n3 + 0.0f;
            final float n13 = 0.0f - n4;
            path6.cubicTo(n12, n11, n, n13, n, 0.0f);
            final Path path7 = this.path;
            final float n14 = n4 + 0.0f;
            path7.cubicTo(n, n14, n12, n2, 0.0f, n2);
            final Path path8 = this.path;
            final float n15 = 0.0f - n3;
            final float n16 = -n;
            path8.cubicTo(n15, n2, n16, n14, n16, 0.0f);
            this.path.cubicTo(n16, n13, n15, n11, 0.0f, n11);
        }
        final PointF pointF2 = this.positionAnimation.getValue();
        this.path.offset(pointF2.x, pointF2.y);
        this.path.close();
        Utils.applyTrimPathIfNeeded(this.path, this.trimPath);
        this.isPathValid = true;
        return this.path;
    }
    
    @Override
    public void onValueChanged() {
        this.invalidate();
    }
    
    @Override
    public void resolveKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, final KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, n, list, keyPath2, this);
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
