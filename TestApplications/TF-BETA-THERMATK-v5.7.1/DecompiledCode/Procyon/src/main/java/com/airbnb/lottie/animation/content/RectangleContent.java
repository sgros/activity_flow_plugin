// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.utils.MiscUtils;
import java.util.List;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.content.RectangleShape;
import com.airbnb.lottie.model.layer.BaseLayer;
import android.graphics.RectF;
import android.graphics.PointF;
import android.graphics.Path;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class RectangleContent implements AnimationListener, KeyPathElementContent, PathContent
{
    private final BaseKeyframeAnimation<?, Float> cornerRadiusAnimation;
    private final boolean hidden;
    private boolean isPathValid;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final Path path;
    private final BaseKeyframeAnimation<?, PointF> positionAnimation;
    private final RectF rect;
    private final BaseKeyframeAnimation<?, PointF> sizeAnimation;
    private CompoundTrimPathContent trimPaths;
    
    public RectangleContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final RectangleShape rectangleShape) {
        this.path = new Path();
        this.rect = new RectF();
        this.trimPaths = new CompoundTrimPathContent();
        this.name = rectangleShape.getName();
        this.hidden = rectangleShape.isHidden();
        this.lottieDrawable = lottieDrawable;
        this.positionAnimation = rectangleShape.getPosition().createAnimation();
        this.sizeAnimation = rectangleShape.getSize().createAnimation();
        this.cornerRadiusAnimation = rectangleShape.getCornerRadius().createAnimation();
        baseLayer.addAnimation(this.positionAnimation);
        baseLayer.addAnimation(this.sizeAnimation);
        baseLayer.addAnimation(this.cornerRadiusAnimation);
        this.positionAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.sizeAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.cornerRadiusAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
    }
    
    private void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> valueCallback) {
        if (t == LottieProperty.RECTANGLE_SIZE) {
            this.sizeAnimation.setValueCallback((LottieValueCallback<PointF>)valueCallback);
        }
        else if (t == LottieProperty.POSITION) {
            this.positionAnimation.setValueCallback((LottieValueCallback<PointF>)valueCallback);
        }
        else if (t == LottieProperty.CORNER_RADIUS) {
            this.cornerRadiusAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
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
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        final PointF pointF = this.sizeAnimation.getValue();
        final float a = pointF.x / 2.0f;
        final float b = pointF.y / 2.0f;
        final BaseKeyframeAnimation<?, Float> cornerRadiusAnimation = this.cornerRadiusAnimation;
        float floatValue;
        if (cornerRadiusAnimation == null) {
            floatValue = 0.0f;
        }
        else {
            floatValue = ((FloatKeyframeAnimation)cornerRadiusAnimation).getFloatValue();
        }
        final float min = Math.min(a, b);
        float n = floatValue;
        if (floatValue > min) {
            n = min;
        }
        final PointF pointF2 = this.positionAnimation.getValue();
        this.path.moveTo(pointF2.x + a, pointF2.y - b + n);
        this.path.lineTo(pointF2.x + a, pointF2.y + b - n);
        if (n > 0.0f) {
            final RectF rect = this.rect;
            final float x = pointF2.x;
            final float n2 = n * 2.0f;
            final float y = pointF2.y;
            rect.set(x + a - n2, y + b - n2, x + a, y + b);
            this.path.arcTo(this.rect, 0.0f, 90.0f, false);
        }
        this.path.lineTo(pointF2.x - a + n, pointF2.y + b);
        if (n > 0.0f) {
            final RectF rect2 = this.rect;
            final float x2 = pointF2.x;
            final float y2 = pointF2.y;
            final float n3 = n * 2.0f;
            rect2.set(x2 - a, y2 + b - n3, x2 - a + n3, y2 + b);
            this.path.arcTo(this.rect, 90.0f, 90.0f, false);
        }
        this.path.lineTo(pointF2.x - a, pointF2.y - b + n);
        if (n > 0.0f) {
            final RectF rect3 = this.rect;
            final float x3 = pointF2.x;
            final float y3 = pointF2.y;
            final float n4 = n * 2.0f;
            rect3.set(x3 - a, y3 - b, x3 - a + n4, y3 - b + n4);
            this.path.arcTo(this.rect, 180.0f, 90.0f, false);
        }
        this.path.lineTo(pointF2.x + a - n, pointF2.y - b);
        if (n > 0.0f) {
            final RectF rect4 = this.rect;
            final float x4 = pointF2.x;
            final float n5 = n * 2.0f;
            final float y4 = pointF2.y;
            rect4.set(x4 + a - n5, y4 - b, x4 + a, y4 - b + n5);
            this.path.arcTo(this.rect, 270.0f, 90.0f, false);
        }
        this.path.close();
        this.trimPaths.apply(this.path);
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
                final TrimPathContent trimPathContent = (TrimPathContent)content;
                if (trimPathContent.getType() == ShapeTrimPath.Type.SIMULTANEOUSLY) {
                    this.trimPaths.addTrimPath(trimPathContent);
                    trimPathContent.addListener(this);
                }
            }
        }
    }
}
