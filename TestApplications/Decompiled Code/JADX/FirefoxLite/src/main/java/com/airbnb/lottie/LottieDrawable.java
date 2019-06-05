package com.airbnb.lottie;

import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import com.airbnb.lottie.manager.FontAssetManager;
import com.airbnb.lottie.manager.ImageAssetManager;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.layer.CompositionLayer;
import com.airbnb.lottie.parser.LayerParser;
import com.airbnb.lottie.utils.LottieValueAnimator;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LottieDrawable extends Drawable implements Animatable, Callback {
    private static final String TAG = "LottieDrawable";
    private int alpha = 255;
    private final LottieValueAnimator animator = new LottieValueAnimator();
    private final Set<Object> colorFilterData = new HashSet();
    private LottieComposition composition;
    private CompositionLayer compositionLayer;
    private boolean enableMergePaths;
    FontAssetDelegate fontAssetDelegate;
    private FontAssetManager fontAssetManager;
    private ImageAssetDelegate imageAssetDelegate;
    private ImageAssetManager imageAssetManager;
    private String imageAssetsFolder;
    private final ArrayList<LazyCompositionTask> lazyCompositionTasks = new ArrayList();
    private final Matrix matrix = new Matrix();
    private boolean performanceTrackingEnabled;
    private float scale = 1.0f;
    TextDelegate textDelegate;

    /* renamed from: com.airbnb.lottie.LottieDrawable$1 */
    class C03611 implements AnimatorUpdateListener {
        C03611() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (LottieDrawable.this.compositionLayer != null) {
                LottieDrawable.this.compositionLayer.setProgress(LottieDrawable.this.animator.getAnimatedValueAbsolute());
            }
        }
    }

    private interface LazyCompositionTask {
        void run(LottieComposition lottieComposition);
    }

    /* renamed from: com.airbnb.lottie.LottieDrawable$2 */
    class C03652 implements LazyCompositionTask {
        C03652() {
        }

        public void run(LottieComposition lottieComposition) {
            LottieDrawable.this.playAnimation();
        }
    }

    public int getOpacity() {
        return -3;
    }

    public LottieDrawable() {
        this.animator.addUpdateListener(new C03611());
    }

    public boolean enableMergePathsForKitKatAndAbove() {
        return this.enableMergePaths;
    }

    public void enableMergePathsForKitKatAndAbove(boolean z) {
        if (this.enableMergePaths != z) {
            if (VERSION.SDK_INT < 19) {
                Log.w(TAG, "Merge paths are not supported pre-Kit Kat.");
                return;
            }
            this.enableMergePaths = z;
            if (this.composition != null) {
                buildCompositionLayer();
            }
        }
    }

    public void setImagesAssetsFolder(String str) {
        this.imageAssetsFolder = str;
    }

    public String getImageAssetsFolder() {
        return this.imageAssetsFolder;
    }

    public void recycleBitmaps() {
        if (this.imageAssetManager != null) {
            this.imageAssetManager.recycleBitmaps();
        }
    }

    public boolean setComposition(LottieComposition lottieComposition) {
        if (this.composition == lottieComposition) {
            return false;
        }
        clearComposition();
        this.composition = lottieComposition;
        buildCompositionLayer();
        this.animator.setComposition(lottieComposition);
        setProgress(this.animator.getAnimatedFraction());
        setScale(this.scale);
        updateBounds();
        Iterator it = new ArrayList(this.lazyCompositionTasks).iterator();
        while (it.hasNext()) {
            ((LazyCompositionTask) it.next()).run(lottieComposition);
            it.remove();
        }
        this.lazyCompositionTasks.clear();
        lottieComposition.setPerformanceTrackingEnabled(this.performanceTrackingEnabled);
        return true;
    }

    public void setPerformanceTrackingEnabled(boolean z) {
        this.performanceTrackingEnabled = z;
        if (this.composition != null) {
            this.composition.setPerformanceTrackingEnabled(z);
        }
    }

    public PerformanceTracker getPerformanceTracker() {
        return this.composition != null ? this.composition.getPerformanceTracker() : null;
    }

    private void buildCompositionLayer() {
        this.compositionLayer = new CompositionLayer(this, LayerParser.parse(this.composition), this.composition.getLayers(), this.composition);
    }

    public void clearComposition() {
        recycleBitmaps();
        if (this.animator.isRunning()) {
            this.animator.cancel();
        }
        this.composition = null;
        this.compositionLayer = null;
        this.imageAssetManager = null;
        this.animator.clearComposition();
        invalidateSelf();
    }

    public void invalidateSelf() {
        Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    public void setAlpha(int i) {
        this.alpha = i;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        Log.w("LOTTIE", "Use addColorFilter instead.");
    }

    public void draw(Canvas canvas) {
        C0352L.beginSection("Drawable#draw");
        if (this.compositionLayer != null) {
            float f = this.scale;
            float maxScale = getMaxScale(canvas);
            if (f > maxScale) {
                f = this.scale / maxScale;
            } else {
                maxScale = f;
                f = 1.0f;
            }
            int i = (f > 1.0f ? 1 : (f == 1.0f ? 0 : -1));
            if (i > 0) {
                canvas.save();
                float width = ((float) this.composition.getBounds().width()) / 2.0f;
                float height = ((float) this.composition.getBounds().height()) / 2.0f;
                float f2 = width * maxScale;
                float f3 = height * maxScale;
                canvas.translate((getScale() * width) - f2, (getScale() * height) - f3);
                canvas.scale(f, f, f2, f3);
            }
            this.matrix.reset();
            this.matrix.preScale(maxScale, maxScale);
            this.compositionLayer.draw(canvas, this.matrix, this.alpha);
            C0352L.endSection("Drawable#draw");
            if (i > 0) {
                canvas.restore();
            }
        }
    }

    public void start() {
        playAnimation();
    }

    public void stop() {
        endAnimation();
    }

    public boolean isRunning() {
        return isAnimating();
    }

    public void playAnimation() {
        if (this.compositionLayer == null) {
            this.lazyCompositionTasks.add(new C03652());
        } else {
            this.animator.playAnimation();
        }
    }

    public void endAnimation() {
        this.lazyCompositionTasks.clear();
        this.animator.endAnimation();
    }

    public void setMinFrame(final int i) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() {
                public void run(LottieComposition lottieComposition) {
                    LottieDrawable.this.setMinFrame(i);
                }
            });
        } else {
            this.animator.setMinFrame(i);
        }
    }

    public float getMinFrame() {
        return this.animator.getMinFrame();
    }

    public void setMinProgress(final float f) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() {
                public void run(LottieComposition lottieComposition) {
                    LottieDrawable.this.setMinProgress(f);
                }
            });
        } else {
            setMinFrame((int) MiscUtils.lerp(this.composition.getStartFrame(), this.composition.getEndFrame(), f));
        }
    }

    public void setMaxFrame(final int i) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() {
                public void run(LottieComposition lottieComposition) {
                    LottieDrawable.this.setMaxFrame(i);
                }
            });
        } else {
            this.animator.setMaxFrame(i);
        }
    }

    public float getMaxFrame() {
        return this.animator.getMaxFrame();
    }

    public void setMaxProgress(final float f) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() {
                public void run(LottieComposition lottieComposition) {
                    LottieDrawable.this.setMaxProgress(f);
                }
            });
        } else {
            setMaxFrame((int) MiscUtils.lerp(this.composition.getStartFrame(), this.composition.getEndFrame(), f));
        }
    }

    public void setSpeed(float f) {
        this.animator.setSpeed(f);
    }

    public float getSpeed() {
        return this.animator.getSpeed();
    }

    public void addAnimatorListener(AnimatorListener animatorListener) {
        this.animator.addListener(animatorListener);
    }

    public void setFrame(final int i) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() {
                public void run(LottieComposition lottieComposition) {
                    LottieDrawable.this.setFrame(i);
                }
            });
        } else {
            this.animator.setFrame(i);
        }
    }

    public int getFrame() {
        return (int) this.animator.getFrame();
    }

    public void setProgress(final float f) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() {
                public void run(LottieComposition lottieComposition) {
                    LottieDrawable.this.setProgress(f);
                }
            });
        } else {
            setFrame((int) MiscUtils.lerp(this.composition.getStartFrame(), this.composition.getEndFrame(), f));
        }
    }

    public void setRepeatMode(int i) {
        this.animator.setRepeatMode(i);
    }

    public int getRepeatMode() {
        return this.animator.getRepeatMode();
    }

    public void setRepeatCount(int i) {
        this.animator.setRepeatCount(i);
    }

    public int getRepeatCount() {
        return this.animator.getRepeatCount();
    }

    public boolean isAnimating() {
        return this.animator.isRunning();
    }

    public void setScale(float f) {
        this.scale = f;
        updateBounds();
    }

    public void setImageAssetDelegate(ImageAssetDelegate imageAssetDelegate) {
        this.imageAssetDelegate = imageAssetDelegate;
        if (this.imageAssetManager != null) {
            this.imageAssetManager.setDelegate(imageAssetDelegate);
        }
    }

    public void setFontAssetDelegate(FontAssetDelegate fontAssetDelegate) {
        this.fontAssetDelegate = fontAssetDelegate;
        if (this.fontAssetManager != null) {
            this.fontAssetManager.setDelegate(fontAssetDelegate);
        }
    }

    public void setTextDelegate(TextDelegate textDelegate) {
        this.textDelegate = textDelegate;
    }

    public TextDelegate getTextDelegate() {
        return this.textDelegate;
    }

    public boolean useTextGlyphs() {
        return this.textDelegate == null && this.composition.getCharacters().size() > 0;
    }

    public float getScale() {
        return this.scale;
    }

    public LottieComposition getComposition() {
        return this.composition;
    }

    private void updateBounds() {
        if (this.composition != null) {
            float scale = getScale();
            setBounds(0, 0, (int) (((float) this.composition.getBounds().width()) * scale), (int) (((float) this.composition.getBounds().height()) * scale));
        }
    }

    public void cancelAnimation() {
        this.lazyCompositionTasks.clear();
        this.animator.cancel();
    }

    public float getProgress() {
        return this.animator.getAnimatedValueAbsolute();
    }

    public int getIntrinsicWidth() {
        return this.composition == null ? -1 : (int) (((float) this.composition.getBounds().width()) * getScale());
    }

    public int getIntrinsicHeight() {
        return this.composition == null ? -1 : (int) (((float) this.composition.getBounds().height()) * getScale());
    }

    public List<KeyPath> resolveKeyPath(KeyPath keyPath) {
        if (this.compositionLayer == null) {
            Log.w("LOTTIE", "Cannot resolve KeyPath. Composition is not set yet.");
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        this.compositionLayer.resolveKeyPath(keyPath, 0, arrayList, new KeyPath(new String[0]));
        return arrayList;
    }

    public <T> void addValueCallback(final KeyPath keyPath, final T t, final LottieValueCallback<T> lottieValueCallback) {
        if (this.compositionLayer == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() {
                public void run(LottieComposition lottieComposition) {
                    LottieDrawable.this.addValueCallback(keyPath, t, lottieValueCallback);
                }
            });
            return;
        }
        int i = 1;
        if (keyPath.getResolvedElement() != null) {
            keyPath.getResolvedElement().addValueCallback(t, lottieValueCallback);
        } else {
            List resolveKeyPath = resolveKeyPath(keyPath);
            for (int i2 = 0; i2 < resolveKeyPath.size(); i2++) {
                ((KeyPath) resolveKeyPath.get(i2)).getResolvedElement().addValueCallback(t, lottieValueCallback);
            }
            i = 1 ^ resolveKeyPath.isEmpty();
        }
        if (i != 0) {
            invalidateSelf();
            if (t == LottieProperty.TIME_REMAP) {
                setProgress(getProgress());
            }
        }
    }

    public Bitmap getImageAsset(String str) {
        ImageAssetManager imageAssetManager = getImageAssetManager();
        return imageAssetManager != null ? imageAssetManager.bitmapForId(str) : null;
    }

    private ImageAssetManager getImageAssetManager() {
        if (getCallback() == null) {
            return null;
        }
        if (!(this.imageAssetManager == null || this.imageAssetManager.hasSameContext(getContext()))) {
            this.imageAssetManager.recycleBitmaps();
            this.imageAssetManager = null;
        }
        if (this.imageAssetManager == null) {
            this.imageAssetManager = new ImageAssetManager(getCallback(), this.imageAssetsFolder, this.imageAssetDelegate, this.composition.getImages());
        }
        return this.imageAssetManager;
    }

    public Typeface getTypeface(String str, String str2) {
        FontAssetManager fontAssetManager = getFontAssetManager();
        return fontAssetManager != null ? fontAssetManager.getTypeface(str, str2) : null;
    }

    private FontAssetManager getFontAssetManager() {
        if (getCallback() == null) {
            return null;
        }
        if (this.fontAssetManager == null) {
            this.fontAssetManager = new FontAssetManager(getCallback(), this.fontAssetDelegate);
        }
        return this.fontAssetManager;
    }

    private Context getContext() {
        Callback callback = getCallback();
        if (callback != null && (callback instanceof View)) {
            return ((View) callback).getContext();
        }
        return null;
    }

    public void invalidateDrawable(Drawable drawable) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, runnable, j);
        }
    }

    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, runnable);
        }
    }

    private float getMaxScale(Canvas canvas) {
        return Math.min(((float) canvas.getWidth()) / ((float) this.composition.getBounds().width()), ((float) canvas.getHeight()) / ((float) this.composition.getBounds().height()));
    }
}
