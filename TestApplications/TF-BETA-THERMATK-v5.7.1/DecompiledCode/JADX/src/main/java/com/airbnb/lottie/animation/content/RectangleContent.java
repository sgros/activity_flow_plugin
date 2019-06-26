package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.RectangleShape;
import com.airbnb.lottie.model.content.ShapeTrimPath.Type;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class RectangleContent implements AnimationListener, KeyPathElementContent, PathContent {
    private final BaseKeyframeAnimation<?, Float> cornerRadiusAnimation;
    private final boolean hidden;
    private boolean isPathValid;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final Path path = new Path();
    private final BaseKeyframeAnimation<?, PointF> positionAnimation;
    private final RectF rect = new RectF();
    private final BaseKeyframeAnimation<?, PointF> sizeAnimation;
    private CompoundTrimPathContent trimPaths = new CompoundTrimPathContent();

    public RectangleContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, RectangleShape rectangleShape) {
        this.name = rectangleShape.getName();
        this.hidden = rectangleShape.isHidden();
        this.lottieDrawable = lottieDrawable;
        this.positionAnimation = rectangleShape.getPosition().createAnimation();
        this.sizeAnimation = rectangleShape.getSize().createAnimation();
        this.cornerRadiusAnimation = rectangleShape.getCornerRadius().createAnimation();
        baseLayer.addAnimation(this.positionAnimation);
        baseLayer.addAnimation(this.sizeAnimation);
        baseLayer.addAnimation(this.cornerRadiusAnimation);
        this.positionAnimation.addUpdateListener(this);
        this.sizeAnimation.addUpdateListener(this);
        this.cornerRadiusAnimation.addUpdateListener(this);
    }

    public String getName() {
        return this.name;
    }

    public void onValueChanged() {
        invalidate();
    }

    private void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }

    public void setContents(List<Content> list, List<Content> list2) {
        for (int i = 0; i < list.size(); i++) {
            Content content = (Content) list.get(i);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent = (TrimPathContent) content;
                if (trimPathContent.getType() == Type.SIMULTANEOUSLY) {
                    this.trimPaths.addTrimPath(trimPathContent);
                    trimPathContent.addListener(this);
                }
            }
        }
    }

    public Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        float f;
        RectF rectF;
        float f2;
        float f3;
        float f4;
        float f5;
        PointF pointF = (PointF) this.sizeAnimation.getValue();
        float f6 = pointF.x / 2.0f;
        float f7 = pointF.y / 2.0f;
        BaseKeyframeAnimation baseKeyframeAnimation = this.cornerRadiusAnimation;
        if (baseKeyframeAnimation == null) {
            f = 0.0f;
        } else {
            f = ((FloatKeyframeAnimation) baseKeyframeAnimation).getFloatValue();
        }
        float min = Math.min(f6, f7);
        if (f > min) {
            f = min;
        }
        PointF pointF2 = (PointF) this.positionAnimation.getValue();
        this.path.moveTo(pointF2.x + f6, (pointF2.y - f7) + f);
        this.path.lineTo(pointF2.x + f6, (pointF2.y + f7) - f);
        if (f > 0.0f) {
            rectF = this.rect;
            f2 = pointF2.x;
            f3 = f * 2.0f;
            f4 = (f2 + f6) - f3;
            float f8 = pointF2.y;
            rectF.set(f4, (f8 + f7) - f3, f2 + f6, f8 + f7);
            this.path.arcTo(this.rect, 0.0f, 90.0f, false);
        }
        this.path.lineTo((pointF2.x - f6) + f, pointF2.y + f7);
        if (f > 0.0f) {
            rectF = this.rect;
            f2 = pointF2.x;
            f4 = f2 - f6;
            f3 = pointF2.y;
            f5 = f * 2.0f;
            rectF.set(f4, (f3 + f7) - f5, (f2 - f6) + f5, f3 + f7);
            this.path.arcTo(this.rect, 90.0f, 90.0f, false);
        }
        this.path.lineTo(pointF2.x - f6, (pointF2.y - f7) + f);
        if (f > 0.0f) {
            rectF = this.rect;
            f2 = pointF2.x;
            f4 = f2 - f6;
            f3 = pointF2.y;
            f5 = f * 2.0f;
            rectF.set(f4, f3 - f7, (f2 - f6) + f5, (f3 - f7) + f5);
            this.path.arcTo(this.rect, 180.0f, 90.0f, false);
        }
        this.path.lineTo((pointF2.x + f6) - f, pointF2.y - f7);
        if (f > 0.0f) {
            RectF rectF2 = this.rect;
            float f9 = pointF2.x;
            f *= 2.0f;
            f2 = (f9 + f6) - f;
            float f10 = pointF2.y;
            rectF2.set(f2, f10 - f7, f9 + f6, (f10 - f7) + f);
            this.path.arcTo(this.rect, 270.0f, 90.0f, false);
        }
        this.path.close();
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }

    public void resolveKeyPath(KeyPath keyPath, int i, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, i, list, keyPath2, this);
    }

    public <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.RECTANGLE_SIZE) {
            this.sizeAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POSITION) {
            this.positionAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.CORNER_RADIUS) {
            this.cornerRadiusAnimation.setValueCallback(lottieValueCallback);
        }
    }
}
