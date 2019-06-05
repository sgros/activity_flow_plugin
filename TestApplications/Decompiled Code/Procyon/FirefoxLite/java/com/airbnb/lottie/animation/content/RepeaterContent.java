// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.KeyPath;
import android.graphics.RectF;
import com.airbnb.lottie.utils.MiscUtils;
import android.graphics.Canvas;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.ListIterator;
import com.airbnb.lottie.model.content.Repeater;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import android.graphics.Path;
import android.graphics.Matrix;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class RepeaterContent implements DrawingContent, GreedyContent, KeyPathElementContent, PathContent, AnimationListener
{
    private ContentGroup contentGroup;
    private final BaseKeyframeAnimation<Float, Float> copies;
    private final BaseLayer layer;
    private final LottieDrawable lottieDrawable;
    private final Matrix matrix;
    private final String name;
    private final BaseKeyframeAnimation<Float, Float> offset;
    private final Path path;
    private final TransformKeyframeAnimation transform;
    
    public RepeaterContent(final LottieDrawable lottieDrawable, final BaseLayer layer, final Repeater repeater) {
        this.matrix = new Matrix();
        this.path = new Path();
        this.lottieDrawable = lottieDrawable;
        this.layer = layer;
        this.name = repeater.getName();
        layer.addAnimation(this.copies = repeater.getCopies().createAnimation());
        this.copies.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        layer.addAnimation(this.offset = repeater.getOffset().createAnimation());
        this.offset.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        (this.transform = repeater.getTransform().createAnimation()).addAnimationsToLayer(layer);
        this.transform.addListener(this);
    }
    
    @Override
    public void absorbContent(final ListIterator<Content> listIterator) {
        if (this.contentGroup != null) {
            return;
        }
        while (listIterator.hasPrevious() && listIterator.previous() != this) {}
        final ArrayList<Content> list = new ArrayList<Content>();
        while (listIterator.hasPrevious()) {
            list.add(listIterator.previous());
            listIterator.remove();
        }
        Collections.reverse(list);
        this.contentGroup = new ContentGroup(this.lottieDrawable, this.layer, "Repeater", list, null);
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        if (this.transform.applyValueCallback(t, lottieValueCallback)) {
            return;
        }
        if (t == LottieProperty.REPEATER_COPIES) {
            this.copies.setValueCallback((LottieValueCallback<Float>)lottieValueCallback);
        }
        else if (t == LottieProperty.REPEATER_OFFSET) {
            this.offset.setValueCallback((LottieValueCallback<Float>)lottieValueCallback);
        }
    }
    
    @Override
    public void draw(final Canvas canvas, final Matrix matrix, final int n) {
        final float floatValue = this.copies.getValue();
        final float floatValue2 = this.offset.getValue();
        final float n2 = this.transform.getStartOpacity().getValue() / 100.0f;
        final float n3 = this.transform.getEndOpacity().getValue() / 100.0f;
        for (int i = (int)floatValue - 1; i >= 0; --i) {
            this.matrix.set(matrix);
            final Matrix matrix2 = this.matrix;
            final TransformKeyframeAnimation transform = this.transform;
            final float n4 = (float)i;
            matrix2.preConcat(transform.getMatrixForRepeater(n4 + floatValue2));
            this.contentGroup.draw(canvas, this.matrix, (int)(n * MiscUtils.lerp(n2, n3, n4 / floatValue)));
        }
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix) {
        this.contentGroup.getBounds(rectF, matrix);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Path getPath() {
        final Path path = this.contentGroup.getPath();
        this.path.reset();
        final float floatValue = this.copies.getValue();
        final float floatValue2 = this.offset.getValue();
        for (int i = (int)floatValue - 1; i >= 0; --i) {
            this.matrix.set(this.transform.getMatrixForRepeater(i + floatValue2));
            this.path.addPath(path, this.matrix);
        }
        return this.path;
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
        this.contentGroup.setContents(list, list2);
    }
}
