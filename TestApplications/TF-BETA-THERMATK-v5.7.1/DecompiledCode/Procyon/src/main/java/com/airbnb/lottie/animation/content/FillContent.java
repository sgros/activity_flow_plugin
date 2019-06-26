// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.KeyPath;
import android.graphics.RectF;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation;
import com.airbnb.lottie.L;
import android.graphics.Matrix;
import android.graphics.Canvas;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.model.content.ShapeFill;
import java.util.List;
import android.graphics.Path;
import android.graphics.Paint;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.layer.BaseLayer;
import android.graphics.ColorFilter;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class FillContent implements DrawingContent, AnimationListener, KeyPathElementContent
{
    private final BaseKeyframeAnimation<Integer, Integer> colorAnimation;
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation;
    private final boolean hidden;
    private final BaseLayer layer;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final BaseKeyframeAnimation<Integer, Integer> opacityAnimation;
    private final Paint paint;
    private final Path path;
    private final List<PathContent> paths;
    
    public FillContent(final LottieDrawable lottieDrawable, final BaseLayer layer, final ShapeFill shapeFill) {
        this.path = new Path();
        this.paint = new LPaint(1);
        this.paths = new ArrayList<PathContent>();
        this.layer = layer;
        this.name = shapeFill.getName();
        this.hidden = shapeFill.isHidden();
        this.lottieDrawable = lottieDrawable;
        if (shapeFill.getColor() != null && shapeFill.getOpacity() != null) {
            this.path.setFillType(shapeFill.getFillType());
            (this.colorAnimation = shapeFill.getColor().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            layer.addAnimation(this.colorAnimation);
            (this.opacityAnimation = shapeFill.getOpacity().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            layer.addAnimation(this.opacityAnimation);
            return;
        }
        this.colorAnimation = null;
        this.opacityAnimation = null;
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.COLOR) {
            this.colorAnimation.setValueCallback((LottieValueCallback<Integer>)lottieValueCallback);
        }
        else if (t == LottieProperty.OPACITY) {
            this.opacityAnimation.setValueCallback((LottieValueCallback<Integer>)lottieValueCallback);
        }
        else if (t == LottieProperty.COLOR_FILTER) {
            if (lottieValueCallback == null) {
                this.colorFilterAnimation = null;
            }
            else {
                (this.colorFilterAnimation = new ValueCallbackKeyframeAnimation<ColorFilter, ColorFilter>((LottieValueCallback<ColorFilter>)lottieValueCallback)).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
                this.layer.addAnimation(this.colorFilterAnimation);
            }
        }
    }
    
    @Override
    public void draw(final Canvas canvas, final Matrix matrix, int i) {
        if (this.hidden) {
            return;
        }
        L.beginSection("FillContent#draw");
        this.paint.setColor(((ColorKeyframeAnimation)this.colorAnimation).getIntValue());
        final int n = (int)(i / 255.0f * this.opacityAnimation.getValue() / 100.0f * 255.0f);
        final Paint paint = this.paint;
        i = 0;
        paint.setAlpha(MiscUtils.clamp(n, 0, 255));
        final BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation = this.colorFilterAnimation;
        if (colorFilterAnimation != null) {
            this.paint.setColorFilter((ColorFilter)colorFilterAnimation.getValue());
        }
        this.path.reset();
        while (i < this.paths.size()) {
            this.path.addPath(this.paths.get(i).getPath(), matrix);
            ++i;
        }
        canvas.drawPath(this.path, this.paint);
        L.endSection("FillContent#draw");
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix, final boolean b) {
        this.path.reset();
        for (int i = 0; i < this.paths.size(); ++i) {
            this.path.addPath(this.paths.get(i).getPath(), matrix);
        }
        this.path.computeBounds(rectF, false);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }
    
    @Override
    public void resolveKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, final KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, n, list, keyPath2, this);
    }
    
    @Override
    public void setContents(final List<Content> list, final List<Content> list2) {
        for (int i = 0; i < list2.size(); ++i) {
            final Content content = list2.get(i);
            if (content instanceof PathContent) {
                this.paths.add((PathContent)content);
            }
        }
    }
}
