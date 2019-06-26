// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import java.util.Collection;
import com.airbnb.lottie.model.KeyPath;
import android.graphics.Canvas;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.content.ContentModel;
import java.util.ArrayList;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.ShapeGroup;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import android.graphics.RectF;
import android.graphics.Path;
import android.graphics.Matrix;
import com.airbnb.lottie.LottieDrawable;
import java.util.List;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class ContentGroup implements DrawingContent, PathContent, AnimationListener, KeyPathElement
{
    private final List<Content> contents;
    private final boolean hidden;
    private final LottieDrawable lottieDrawable;
    private final Matrix matrix;
    private final String name;
    private final Path path;
    private List<PathContent> pathContents;
    private final RectF rect;
    private TransformKeyframeAnimation transformAnimation;
    
    public ContentGroup(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final ShapeGroup shapeGroup) {
        this(lottieDrawable, baseLayer, shapeGroup.getName(), shapeGroup.isHidden(), contentsFromModels(lottieDrawable, baseLayer, shapeGroup.getItems()), findTransform(shapeGroup.getItems()));
    }
    
    ContentGroup(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final String name, final boolean hidden, final List<Content> contents, final AnimatableTransform animatableTransform) {
        this.matrix = new Matrix();
        this.path = new Path();
        this.rect = new RectF();
        this.name = name;
        this.lottieDrawable = lottieDrawable;
        this.hidden = hidden;
        this.contents = contents;
        if (animatableTransform != null) {
            (this.transformAnimation = animatableTransform.createAnimation()).addAnimationsToLayer(baseLayer);
            this.transformAnimation.addListener(this);
        }
        final ArrayList<GreedyContent> list = new ArrayList<GreedyContent>();
        for (int i = contents.size() - 1; i >= 0; --i) {
            final Content content = contents.get(i);
            if (content instanceof GreedyContent) {
                list.add((GreedyContent)content);
            }
        }
        for (int j = list.size() - 1; j >= 0; --j) {
            ((GreedyContent)list.get(j)).absorbContent(contents.listIterator(contents.size()));
        }
    }
    
    private static List<Content> contentsFromModels(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final List<ContentModel> list) {
        final ArrayList<Content> list2 = new ArrayList<Content>(list.size());
        for (int i = 0; i < list.size(); ++i) {
            final Content content = list.get(i).toContent(lottieDrawable, baseLayer);
            if (content != null) {
                list2.add(content);
            }
        }
        return list2;
    }
    
    static AnimatableTransform findTransform(final List<ContentModel> list) {
        for (int i = 0; i < list.size(); ++i) {
            final ContentModel contentModel = list.get(i);
            if (contentModel instanceof AnimatableTransform) {
                return (AnimatableTransform)contentModel;
            }
        }
        return null;
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        final TransformKeyframeAnimation transformAnimation = this.transformAnimation;
        if (transformAnimation != null) {
            transformAnimation.applyValueCallback(t, lottieValueCallback);
        }
    }
    
    @Override
    public void draw(final Canvas canvas, final Matrix matrix, int i) {
        if (this.hidden) {
            return;
        }
        this.matrix.set(matrix);
        final TransformKeyframeAnimation transformAnimation = this.transformAnimation;
        int n = i;
        if (transformAnimation != null) {
            this.matrix.preConcat(transformAnimation.getMatrix());
            int intValue;
            if (this.transformAnimation.getOpacity() == null) {
                intValue = 100;
            }
            else {
                intValue = this.transformAnimation.getOpacity().getValue();
            }
            n = (int)(intValue / 100.0f * i / 255.0f * 255.0f);
        }
        DrawingContent value;
        for (i = this.contents.size() - 1; i >= 0; --i) {
            value = this.contents.get(i);
            if (value instanceof DrawingContent) {
                value.draw(canvas, this.matrix, n);
            }
        }
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix, final boolean b) {
        this.matrix.set(matrix);
        final TransformKeyframeAnimation transformAnimation = this.transformAnimation;
        if (transformAnimation != null) {
            this.matrix.preConcat(transformAnimation.getMatrix());
        }
        this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
        for (int i = this.contents.size() - 1; i >= 0; --i) {
            final Content content = this.contents.get(i);
            if (content instanceof DrawingContent) {
                ((DrawingContent)content).getBounds(this.rect, this.matrix, b);
                rectF.union(this.rect);
            }
        }
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Path getPath() {
        this.matrix.reset();
        final TransformKeyframeAnimation transformAnimation = this.transformAnimation;
        if (transformAnimation != null) {
            this.matrix.set(transformAnimation.getMatrix());
        }
        this.path.reset();
        if (this.hidden) {
            return this.path;
        }
        for (int i = this.contents.size() - 1; i >= 0; --i) {
            final Content content = this.contents.get(i);
            if (content instanceof PathContent) {
                this.path.addPath(((PathContent)content).getPath(), this.matrix);
            }
        }
        return this.path;
    }
    
    List<PathContent> getPathList() {
        if (this.pathContents == null) {
            this.pathContents = new ArrayList<PathContent>();
            for (int i = 0; i < this.contents.size(); ++i) {
                final Content content = this.contents.get(i);
                if (content instanceof PathContent) {
                    this.pathContents.add((PathContent)content);
                }
            }
        }
        return this.pathContents;
    }
    
    Matrix getTransformationMatrix() {
        final TransformKeyframeAnimation transformAnimation = this.transformAnimation;
        if (transformAnimation != null) {
            return transformAnimation.getMatrix();
        }
        this.matrix.reset();
        return this.matrix;
    }
    
    @Override
    public void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }
    
    @Override
    public void resolveKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, KeyPath keyPath2) {
        if (!keyPath.matches(this.getName(), n)) {
            return;
        }
        KeyPath addKey = keyPath2;
        if (!"__container".equals(this.getName())) {
            keyPath2 = (addKey = keyPath2.addKey(this.getName()));
            if (keyPath.fullyResolvesTo(this.getName(), n)) {
                list.add(keyPath2.resolve(this));
                addKey = keyPath2;
            }
        }
        if (keyPath.propagateToChildren(this.getName(), n)) {
            final int incrementDepthBy = keyPath.incrementDepthBy(this.getName(), n);
            for (int i = 0; i < this.contents.size(); ++i) {
                final Content content = this.contents.get(i);
                if (content instanceof KeyPathElement) {
                    ((KeyPathElement)content).resolveKeyPath(keyPath, n + incrementDepthBy, list, addKey);
                }
            }
        }
    }
    
    @Override
    public void setContents(final List<Content> list, final List<Content> list2) {
        final ArrayList list3 = new ArrayList<Object>(list.size() + this.contents.size());
        list3.addAll(list);
        for (int i = this.contents.size() - 1; i >= 0; --i) {
            final Content content = this.contents.get(i);
            content.setContents(list3, this.contents.subList(0, i));
            list3.add(content);
        }
    }
}
