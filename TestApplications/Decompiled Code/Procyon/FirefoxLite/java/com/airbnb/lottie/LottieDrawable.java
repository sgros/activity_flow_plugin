// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import com.airbnb.lottie.utils.MiscUtils;
import java.util.Iterator;
import java.util.Collection;
import android.graphics.ColorFilter;
import java.util.Collections;
import android.graphics.Typeface;
import android.graphics.Bitmap;
import android.util.Log;
import android.os.Build$VERSION;
import java.util.List;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.KeyPath;
import android.animation.Animator$AnimatorListener;
import android.graphics.Canvas;
import android.view.View;
import android.content.Context;
import com.airbnb.lottie.parser.LayerParser;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import java.util.HashSet;
import android.graphics.Matrix;
import java.util.ArrayList;
import com.airbnb.lottie.manager.ImageAssetManager;
import com.airbnb.lottie.manager.FontAssetManager;
import com.airbnb.lottie.model.layer.CompositionLayer;
import java.util.Set;
import com.airbnb.lottie.utils.LottieValueAnimator;
import android.graphics.drawable.Drawable$Callback;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public class LottieDrawable extends Drawable implements Animatable, Drawable$Callback
{
    private static final String TAG = "LottieDrawable";
    private int alpha;
    private final LottieValueAnimator animator;
    private final Set<Object> colorFilterData;
    private LottieComposition composition;
    private CompositionLayer compositionLayer;
    private boolean enableMergePaths;
    FontAssetDelegate fontAssetDelegate;
    private FontAssetManager fontAssetManager;
    private ImageAssetDelegate imageAssetDelegate;
    private ImageAssetManager imageAssetManager;
    private String imageAssetsFolder;
    private final ArrayList<LazyCompositionTask> lazyCompositionTasks;
    private final Matrix matrix;
    private boolean performanceTrackingEnabled;
    private float scale;
    TextDelegate textDelegate;
    
    public LottieDrawable() {
        this.matrix = new Matrix();
        this.animator = new LottieValueAnimator();
        this.scale = 1.0f;
        this.colorFilterData = new HashSet<Object>();
        this.lazyCompositionTasks = new ArrayList<LazyCompositionTask>();
        this.alpha = 255;
        this.animator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                if (LottieDrawable.this.compositionLayer != null) {
                    LottieDrawable.this.compositionLayer.setProgress(LottieDrawable.this.animator.getAnimatedValueAbsolute());
                }
            }
        });
    }
    
    private void buildCompositionLayer() {
        this.compositionLayer = new CompositionLayer(this, LayerParser.parse(this.composition), this.composition.getLayers(), this.composition);
    }
    
    private Context getContext() {
        final Drawable$Callback callback = this.getCallback();
        if (callback == null) {
            return null;
        }
        if (callback instanceof View) {
            return ((View)callback).getContext();
        }
        return null;
    }
    
    private FontAssetManager getFontAssetManager() {
        if (this.getCallback() == null) {
            return null;
        }
        if (this.fontAssetManager == null) {
            this.fontAssetManager = new FontAssetManager(this.getCallback(), this.fontAssetDelegate);
        }
        return this.fontAssetManager;
    }
    
    private ImageAssetManager getImageAssetManager() {
        if (this.getCallback() == null) {
            return null;
        }
        if (this.imageAssetManager != null && !this.imageAssetManager.hasSameContext(this.getContext())) {
            this.imageAssetManager.recycleBitmaps();
            this.imageAssetManager = null;
        }
        if (this.imageAssetManager == null) {
            this.imageAssetManager = new ImageAssetManager(this.getCallback(), this.imageAssetsFolder, this.imageAssetDelegate, this.composition.getImages());
        }
        return this.imageAssetManager;
    }
    
    private float getMaxScale(final Canvas canvas) {
        return Math.min(canvas.getWidth() / (float)this.composition.getBounds().width(), canvas.getHeight() / (float)this.composition.getBounds().height());
    }
    
    private void updateBounds() {
        if (this.composition == null) {
            return;
        }
        final float scale = this.getScale();
        this.setBounds(0, 0, (int)(this.composition.getBounds().width() * scale), (int)(this.composition.getBounds().height() * scale));
    }
    
    public void addAnimatorListener(final Animator$AnimatorListener animator$AnimatorListener) {
        this.animator.addListener(animator$AnimatorListener);
    }
    
    public <T> void addValueCallback(final KeyPath keyPath, final T t, final LottieValueCallback<T> lottieValueCallback) {
        if (this.compositionLayer == null) {
            this.lazyCompositionTasks.add((LazyCompositionTask)new LazyCompositionTask() {
                @Override
                public void run(final LottieComposition lottieComposition) {
                    LottieDrawable.this.addValueCallback(keyPath, t, lottieValueCallback);
                }
            });
            return;
        }
        final KeyPathElement resolvedElement = keyPath.getResolvedElement();
        boolean b = true;
        if (resolvedElement != null) {
            keyPath.getResolvedElement().addValueCallback(t, lottieValueCallback);
        }
        else {
            final List<KeyPath> resolveKeyPath = this.resolveKeyPath(keyPath);
            for (int i = 0; i < resolveKeyPath.size(); ++i) {
                resolveKeyPath.get(i).getResolvedElement().addValueCallback(t, lottieValueCallback);
            }
            b = (true ^ resolveKeyPath.isEmpty());
        }
        if (b) {
            this.invalidateSelf();
            if (t == LottieProperty.TIME_REMAP) {
                this.setProgress(this.getProgress());
            }
        }
    }
    
    public void cancelAnimation() {
        this.lazyCompositionTasks.clear();
        this.animator.cancel();
    }
    
    public void clearComposition() {
        this.recycleBitmaps();
        if (this.animator.isRunning()) {
            this.animator.cancel();
        }
        this.composition = null;
        this.compositionLayer = null;
        this.imageAssetManager = null;
        this.animator.clearComposition();
        this.invalidateSelf();
    }
    
    public void draw(final Canvas canvas) {
        L.beginSection("Drawable#draw");
        if (this.compositionLayer == null) {
            return;
        }
        final float scale = this.scale;
        float maxScale = this.getMaxScale(canvas);
        float n;
        if (scale > maxScale) {
            n = this.scale / maxScale;
        }
        else {
            maxScale = scale;
            n = 1.0f;
        }
        final float n2 = fcmpl(n, 1.0f);
        if (n2 > 0) {
            canvas.save();
            final float n3 = this.composition.getBounds().width() / 2.0f;
            final float n4 = this.composition.getBounds().height() / 2.0f;
            final float n5 = n3 * maxScale;
            final float n6 = n4 * maxScale;
            canvas.translate(this.getScale() * n3 - n5, this.getScale() * n4 - n6);
            canvas.scale(n, n, n5, n6);
        }
        this.matrix.reset();
        this.matrix.preScale(maxScale, maxScale);
        this.compositionLayer.draw(canvas, this.matrix, this.alpha);
        L.endSection("Drawable#draw");
        if (n2 > 0) {
            canvas.restore();
        }
    }
    
    public void enableMergePathsForKitKatAndAbove(final boolean enableMergePaths) {
        if (this.enableMergePaths == enableMergePaths) {
            return;
        }
        if (Build$VERSION.SDK_INT < 19) {
            Log.w(LottieDrawable.TAG, "Merge paths are not supported pre-Kit Kat.");
            return;
        }
        this.enableMergePaths = enableMergePaths;
        if (this.composition != null) {
            this.buildCompositionLayer();
        }
    }
    
    public boolean enableMergePathsForKitKatAndAbove() {
        return this.enableMergePaths;
    }
    
    public void endAnimation() {
        this.lazyCompositionTasks.clear();
        this.animator.endAnimation();
    }
    
    public int getAlpha() {
        return this.alpha;
    }
    
    public LottieComposition getComposition() {
        return this.composition;
    }
    
    public int getFrame() {
        return (int)this.animator.getFrame();
    }
    
    public Bitmap getImageAsset(final String s) {
        final ImageAssetManager imageAssetManager = this.getImageAssetManager();
        if (imageAssetManager != null) {
            return imageAssetManager.bitmapForId(s);
        }
        return null;
    }
    
    public String getImageAssetsFolder() {
        return this.imageAssetsFolder;
    }
    
    public int getIntrinsicHeight() {
        int n;
        if (this.composition == null) {
            n = -1;
        }
        else {
            n = (int)(this.composition.getBounds().height() * this.getScale());
        }
        return n;
    }
    
    public int getIntrinsicWidth() {
        int n;
        if (this.composition == null) {
            n = -1;
        }
        else {
            n = (int)(this.composition.getBounds().width() * this.getScale());
        }
        return n;
    }
    
    public float getMaxFrame() {
        return this.animator.getMaxFrame();
    }
    
    public float getMinFrame() {
        return this.animator.getMinFrame();
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public PerformanceTracker getPerformanceTracker() {
        if (this.composition != null) {
            return this.composition.getPerformanceTracker();
        }
        return null;
    }
    
    public float getProgress() {
        return this.animator.getAnimatedValueAbsolute();
    }
    
    public int getRepeatCount() {
        return this.animator.getRepeatCount();
    }
    
    public int getRepeatMode() {
        return this.animator.getRepeatMode();
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public float getSpeed() {
        return this.animator.getSpeed();
    }
    
    public TextDelegate getTextDelegate() {
        return this.textDelegate;
    }
    
    public Typeface getTypeface(final String s, final String s2) {
        final FontAssetManager fontAssetManager = this.getFontAssetManager();
        if (fontAssetManager != null) {
            return fontAssetManager.getTypeface(s, s2);
        }
        return null;
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        final Drawable$Callback callback = this.getCallback();
        if (callback == null) {
            return;
        }
        callback.invalidateDrawable((Drawable)this);
    }
    
    public void invalidateSelf() {
        final Drawable$Callback callback = this.getCallback();
        if (callback != null) {
            callback.invalidateDrawable((Drawable)this);
        }
    }
    
    public boolean isAnimating() {
        return this.animator.isRunning();
    }
    
    public boolean isRunning() {
        return this.isAnimating();
    }
    
    public void playAnimation() {
        if (this.compositionLayer == null) {
            this.lazyCompositionTasks.add((LazyCompositionTask)new LazyCompositionTask() {
                @Override
                public void run(final LottieComposition lottieComposition) {
                    LottieDrawable.this.playAnimation();
                }
            });
            return;
        }
        this.animator.playAnimation();
    }
    
    public void recycleBitmaps() {
        if (this.imageAssetManager != null) {
            this.imageAssetManager.recycleBitmaps();
        }
    }
    
    public List<KeyPath> resolveKeyPath(final KeyPath keyPath) {
        if (this.compositionLayer == null) {
            Log.w("LOTTIE", "Cannot resolve KeyPath. Composition is not set yet.");
            return Collections.emptyList();
        }
        final ArrayList<KeyPath> list = new ArrayList<KeyPath>();
        this.compositionLayer.resolveKeyPath(keyPath, 0, list, new KeyPath(new String[0]));
        return list;
    }
    
    public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
        final Drawable$Callback callback = this.getCallback();
        if (callback == null) {
            return;
        }
        callback.scheduleDrawable((Drawable)this, runnable, n);
    }
    
    public void setAlpha(final int alpha) {
        this.alpha = alpha;
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        Log.w("LOTTIE", "Use addColorFilter instead.");
    }
    
    public boolean setComposition(final LottieComposition lottieComposition) {
        if (this.composition == lottieComposition) {
            return false;
        }
        this.clearComposition();
        this.composition = lottieComposition;
        this.buildCompositionLayer();
        this.animator.setComposition(lottieComposition);
        this.setProgress(this.animator.getAnimatedFraction());
        this.setScale(this.scale);
        this.updateBounds();
        final Iterator<LazyCompositionTask> iterator = new ArrayList<LazyCompositionTask>(this.lazyCompositionTasks).iterator();
        while (iterator.hasNext()) {
            iterator.next().run(lottieComposition);
            iterator.remove();
        }
        this.lazyCompositionTasks.clear();
        lottieComposition.setPerformanceTrackingEnabled(this.performanceTrackingEnabled);
        return true;
    }
    
    public void setFontAssetDelegate(final FontAssetDelegate fontAssetDelegate) {
        this.fontAssetDelegate = fontAssetDelegate;
        if (this.fontAssetManager != null) {
            this.fontAssetManager.setDelegate(fontAssetDelegate);
        }
    }
    
    public void setFrame(final int frame) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add((LazyCompositionTask)new LazyCompositionTask() {
                @Override
                public void run(final LottieComposition lottieComposition) {
                    LottieDrawable.this.setFrame(frame);
                }
            });
            return;
        }
        this.animator.setFrame(frame);
    }
    
    public void setImageAssetDelegate(final ImageAssetDelegate imageAssetDelegate) {
        this.imageAssetDelegate = imageAssetDelegate;
        if (this.imageAssetManager != null) {
            this.imageAssetManager.setDelegate(imageAssetDelegate);
        }
    }
    
    public void setImagesAssetsFolder(final String imageAssetsFolder) {
        this.imageAssetsFolder = imageAssetsFolder;
    }
    
    public void setMaxFrame(final int maxFrame) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add((LazyCompositionTask)new LazyCompositionTask() {
                @Override
                public void run(final LottieComposition lottieComposition) {
                    LottieDrawable.this.setMaxFrame(maxFrame);
                }
            });
            return;
        }
        this.animator.setMaxFrame(maxFrame);
    }
    
    public void setMaxProgress(final float n) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add((LazyCompositionTask)new LazyCompositionTask() {
                @Override
                public void run(final LottieComposition lottieComposition) {
                    LottieDrawable.this.setMaxProgress(n);
                }
            });
            return;
        }
        this.setMaxFrame((int)MiscUtils.lerp(this.composition.getStartFrame(), this.composition.getEndFrame(), n));
    }
    
    public void setMinFrame(final int minFrame) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add((LazyCompositionTask)new LazyCompositionTask() {
                @Override
                public void run(final LottieComposition lottieComposition) {
                    LottieDrawable.this.setMinFrame(minFrame);
                }
            });
            return;
        }
        this.animator.setMinFrame(minFrame);
    }
    
    public void setMinProgress(final float n) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add((LazyCompositionTask)new LazyCompositionTask() {
                @Override
                public void run(final LottieComposition lottieComposition) {
                    LottieDrawable.this.setMinProgress(n);
                }
            });
            return;
        }
        this.setMinFrame((int)MiscUtils.lerp(this.composition.getStartFrame(), this.composition.getEndFrame(), n));
    }
    
    public void setPerformanceTrackingEnabled(final boolean b) {
        this.performanceTrackingEnabled = b;
        if (this.composition != null) {
            this.composition.setPerformanceTrackingEnabled(b);
        }
    }
    
    public void setProgress(final float n) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add((LazyCompositionTask)new LazyCompositionTask() {
                @Override
                public void run(final LottieComposition lottieComposition) {
                    LottieDrawable.this.setProgress(n);
                }
            });
            return;
        }
        this.setFrame((int)MiscUtils.lerp(this.composition.getStartFrame(), this.composition.getEndFrame(), n));
    }
    
    public void setRepeatCount(final int repeatCount) {
        this.animator.setRepeatCount(repeatCount);
    }
    
    public void setRepeatMode(final int repeatMode) {
        this.animator.setRepeatMode(repeatMode);
    }
    
    public void setScale(final float scale) {
        this.scale = scale;
        this.updateBounds();
    }
    
    public void setSpeed(final float speed) {
        this.animator.setSpeed(speed);
    }
    
    public void setTextDelegate(final TextDelegate textDelegate) {
        this.textDelegate = textDelegate;
    }
    
    public void start() {
        this.playAnimation();
    }
    
    public void stop() {
        this.endAnimation();
    }
    
    public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
        final Drawable$Callback callback = this.getCallback();
        if (callback == null) {
            return;
        }
        callback.unscheduleDrawable((Drawable)this, runnable);
    }
    
    public boolean useTextGlyphs() {
        return this.textDelegate == null && this.composition.getCharacters().size() > 0;
    }
    
    private interface LazyCompositionTask
    {
        void run(final LottieComposition p0);
    }
}
