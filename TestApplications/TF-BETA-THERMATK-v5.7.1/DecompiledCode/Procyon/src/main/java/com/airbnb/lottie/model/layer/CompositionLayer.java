// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.layer;

import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.L;
import android.graphics.Matrix;
import android.graphics.Canvas;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import androidx.collection.LongSparseArray;
import java.util.ArrayList;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import android.graphics.RectF;
import java.util.List;

public class CompositionLayer extends BaseLayer
{
    private final List<BaseLayer> layers;
    private final RectF newClipRect;
    private final RectF rect;
    private BaseKeyframeAnimation<Float, Float> timeRemapping;
    
    public CompositionLayer(final LottieDrawable lottieDrawable, final Layer layer, final List<Layer> list, final LottieComposition lottieComposition) {
        super(lottieDrawable, layer);
        this.layers = new ArrayList<BaseLayer>();
        this.rect = new RectF();
        this.newClipRect = new RectF();
        final AnimatableFloatValue timeRemapping = layer.getTimeRemapping();
        if (timeRemapping != null) {
            this.addAnimation(this.timeRemapping = timeRemapping.createAnimation());
            this.timeRemapping.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        }
        else {
            this.timeRemapping = null;
        }
        final LongSparseArray<BaseLayer> longSparseArray = new LongSparseArray<BaseLayer>(lottieComposition.getLayers().size());
        int n = list.size() - 1;
        BaseLayer baseLayer = null;
        int i;
        while (true) {
            i = 0;
            if (n < 0) {
                break;
            }
            final Layer layer2 = list.get(n);
            final BaseLayer forModel = BaseLayer.forModel(layer2, lottieDrawable, lottieComposition);
            if (forModel != null) {
                longSparseArray.put(forModel.getLayerModel().getId(), forModel);
                if (baseLayer != null) {
                    baseLayer.setMatteLayer(forModel);
                    baseLayer = null;
                }
                else {
                    this.layers.add(0, forModel);
                    final int n2 = CompositionLayer$1.$SwitchMap$com$airbnb$lottie$model$layer$Layer$MatteType[layer2.getMatteType().ordinal()];
                    if (n2 == 1 || n2 == 2) {
                        baseLayer = forModel;
                    }
                }
            }
            --n;
        }
        while (i < longSparseArray.size()) {
            final BaseLayer baseLayer2 = longSparseArray.get(longSparseArray.keyAt(i));
            if (baseLayer2 != null) {
                final BaseLayer parentLayer = longSparseArray.get(baseLayer2.getLayerModel().getParentId());
                if (parentLayer != null) {
                    baseLayer2.setParentLayer(parentLayer);
                }
            }
            ++i;
        }
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t == LottieProperty.TIME_REMAP) {
            if (lottieValueCallback == null) {
                this.timeRemapping = null;
            }
            else {
                this.addAnimation(this.timeRemapping = new ValueCallbackKeyframeAnimation<Float, Float>((LottieValueCallback<Float>)lottieValueCallback));
            }
        }
    }
    
    @Override
    void drawLayer(final Canvas canvas, final Matrix matrix, final int n) {
        L.beginSection("CompositionLayer#draw");
        canvas.save();
        this.newClipRect.set(0.0f, 0.0f, (float)super.layerModel.getPreCompWidth(), (float)super.layerModel.getPreCompHeight());
        matrix.mapRect(this.newClipRect);
        for (int i = this.layers.size() - 1; i >= 0; --i) {
            if (this.newClipRect.isEmpty() || canvas.clipRect(this.newClipRect)) {
                this.layers.get(i).draw(canvas, matrix, n);
            }
        }
        canvas.restore();
        L.endSection("CompositionLayer#draw");
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix, final boolean b) {
        super.getBounds(rectF, matrix, b);
        for (int i = this.layers.size() - 1; i >= 0; --i) {
            this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
            this.layers.get(i).getBounds(this.rect, super.boundsMatrix, true);
            rectF.union(this.rect);
        }
    }
    
    protected void resolveChildKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, final KeyPath keyPath2) {
        for (int i = 0; i < this.layers.size(); ++i) {
            this.layers.get(i).resolveKeyPath(keyPath, n, list, keyPath2);
        }
    }
    
    public void setProgress(float progress) {
        super.setProgress(progress);
        if (this.timeRemapping != null) {
            progress = super.lottieDrawable.getComposition().getDuration();
            progress = (long)(this.timeRemapping.getValue() * 1000.0f) / progress;
        }
        float n = progress;
        if (super.layerModel.getTimeStretch() != 0.0f) {
            n = progress / super.layerModel.getTimeStretch();
        }
        progress = super.layerModel.getStartProgress();
        for (int i = this.layers.size() - 1; i >= 0; --i) {
            this.layers.get(i).setProgress(n - progress);
        }
    }
}
