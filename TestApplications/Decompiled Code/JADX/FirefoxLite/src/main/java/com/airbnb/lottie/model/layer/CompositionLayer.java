package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.p001v4.util.LongSparseArray;
import com.airbnb.lottie.C0352L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public class CompositionLayer extends BaseLayer {
    private final List<BaseLayer> layers = new ArrayList();
    private final RectF newClipRect = new RectF();
    private final RectF rect = new RectF();
    private BaseKeyframeAnimation<Float, Float> timeRemapping;

    public CompositionLayer(LottieDrawable lottieDrawable, Layer layer, List<Layer> list, LottieComposition lottieComposition) {
        super(lottieDrawable, layer);
        AnimatableFloatValue timeRemapping = layer.getTimeRemapping();
        if (timeRemapping != null) {
            this.timeRemapping = timeRemapping.createAnimation();
            addAnimation(this.timeRemapping);
            this.timeRemapping.addUpdateListener(this);
        } else {
            this.timeRemapping = null;
        }
        LongSparseArray longSparseArray = new LongSparseArray(lottieComposition.getLayers().size());
        int size = list.size() - 1;
        BaseLayer baseLayer = null;
        while (true) {
            int i = 0;
            if (size >= 0) {
                Layer layer2 = (Layer) list.get(size);
                BaseLayer forModel = BaseLayer.forModel(layer2, lottieDrawable, lottieComposition);
                if (forModel != null) {
                    longSparseArray.put(forModel.getLayerModel().getId(), forModel);
                    if (baseLayer == null) {
                        this.layers.add(0, forModel);
                        switch (layer2.getMatteType()) {
                            case Add:
                            case Invert:
                                baseLayer = forModel;
                                break;
                            default:
                                break;
                        }
                    }
                    baseLayer.setMatteLayer(forModel);
                    baseLayer = null;
                }
                size--;
            } else {
                while (i < longSparseArray.size()) {
                    BaseLayer baseLayer2 = (BaseLayer) longSparseArray.get(longSparseArray.keyAt(i));
                    if (baseLayer2 != null) {
                        BaseLayer baseLayer3 = (BaseLayer) longSparseArray.get(baseLayer2.getLayerModel().getParentId());
                        if (baseLayer3 != null) {
                            baseLayer2.setParentLayer(baseLayer3);
                        }
                    }
                    i++;
                }
                return;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawLayer(Canvas canvas, Matrix matrix, int i) {
        C0352L.beginSection("CompositionLayer#draw");
        canvas.save();
        this.newClipRect.set(0.0f, 0.0f, (float) this.layerModel.getPreCompWidth(), (float) this.layerModel.getPreCompHeight());
        matrix.mapRect(this.newClipRect);
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            if (!this.newClipRect.isEmpty() ? canvas.clipRect(this.newClipRect) : true) {
                ((BaseLayer) this.layers.get(size)).draw(canvas, matrix, i);
            }
        }
        canvas.restore();
        C0352L.endSection("CompositionLayer#draw");
    }

    public void getBounds(RectF rectF, Matrix matrix) {
        super.getBounds(rectF, matrix);
        this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            ((BaseLayer) this.layers.get(size)).getBounds(this.rect, this.boundsMatrix);
            if (rectF.isEmpty()) {
                rectF.set(this.rect);
            } else {
                rectF.set(Math.min(rectF.left, this.rect.left), Math.min(rectF.top, this.rect.top), Math.max(rectF.right, this.rect.right), Math.max(rectF.bottom, this.rect.bottom));
            }
        }
    }

    public void setProgress(float f) {
        super.setProgress(f);
        if (this.timeRemapping != null) {
            f = ((float) ((long) (((Float) this.timeRemapping.getValue()).floatValue() * 1000.0f))) / this.lottieDrawable.getComposition().getDuration();
        }
        if (this.layerModel.getTimeStretch() != 0.0f) {
            f /= this.layerModel.getTimeStretch();
        }
        f -= this.layerModel.getStartProgress();
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            ((BaseLayer) this.layers.get(size)).setProgress(f);
        }
    }

    /* Access modifiers changed, original: protected */
    public void resolveChildKeyPath(KeyPath keyPath, int i, List<KeyPath> list, KeyPath keyPath2) {
        for (int i2 = 0; i2 < this.layers.size(); i2++) {
            ((BaseLayer) this.layers.get(i2)).resolveKeyPath(keyPath, i, list, keyPath2);
        }
    }

    public <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t != LottieProperty.TIME_REMAP) {
            return;
        }
        if (lottieValueCallback == null) {
            this.timeRemapping = null;
            return;
        }
        this.timeRemapping = new ValueCallbackKeyframeAnimation(lottieValueCallback);
        addAnimation(this.timeRemapping);
    }
}
