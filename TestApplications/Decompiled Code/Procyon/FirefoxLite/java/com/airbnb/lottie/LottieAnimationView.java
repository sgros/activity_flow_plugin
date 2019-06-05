// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import android.graphics.Bitmap;
import java.util.Iterator;
import android.graphics.drawable.Drawable$Callback;
import android.util.Log;
import java.io.Reader;
import java.io.StringReader;
import android.util.JsonReader;
import android.text.TextUtils;
import android.os.Parcelable;
import android.animation.Animator$AnimatorListener;
import android.graphics.drawable.Drawable;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.KeyPath;
import android.graphics.Paint;
import android.util.AttributeSet;
import java.util.HashSet;
import android.content.Context;
import java.util.Set;
import android.support.v7.widget.AppCompatImageView;

public class LottieAnimationView extends AppCompatImageView
{
    private static final String TAG = "LottieAnimationView";
    private String animationName;
    private int animationResId;
    private boolean autoPlay;
    private LottieComposition composition;
    private LottieTask<LottieComposition> compositionTask;
    private final LottieListener<Throwable> failureListener;
    private final LottieListener<LottieComposition> loadedListener;
    private final LottieDrawable lottieDrawable;
    private Set<LottieOnCompositionLoadedListener> lottieOnCompositionLoadedListeners;
    private boolean useHardwareLayer;
    private boolean wasAnimatingWhenDetached;
    
    public LottieAnimationView(final Context context) {
        super(context);
        this.loadedListener = new LottieListener<LottieComposition>() {
            @Override
            public void onResult(final LottieComposition composition) {
                LottieAnimationView.this.setComposition(composition);
            }
        };
        this.failureListener = new LottieListener<Throwable>() {
            @Override
            public void onResult(final Throwable cause) {
                throw new IllegalStateException("Unable to parse composition", cause);
            }
        };
        this.lottieDrawable = new LottieDrawable();
        this.wasAnimatingWhenDetached = false;
        this.autoPlay = false;
        this.useHardwareLayer = false;
        this.lottieOnCompositionLoadedListeners = new HashSet<LottieOnCompositionLoadedListener>();
        this.init(null);
    }
    
    public LottieAnimationView(final Context context, final AttributeSet set) {
        super(context, set);
        this.loadedListener = new LottieListener<LottieComposition>() {
            @Override
            public void onResult(final LottieComposition composition) {
                LottieAnimationView.this.setComposition(composition);
            }
        };
        this.failureListener = new LottieListener<Throwable>() {
            @Override
            public void onResult(final Throwable cause) {
                throw new IllegalStateException("Unable to parse composition", cause);
            }
        };
        this.lottieDrawable = new LottieDrawable();
        this.wasAnimatingWhenDetached = false;
        this.autoPlay = false;
        this.useHardwareLayer = false;
        this.lottieOnCompositionLoadedListeners = new HashSet<LottieOnCompositionLoadedListener>();
        this.init(set);
    }
    
    public LottieAnimationView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.loadedListener = new LottieListener<LottieComposition>() {
            @Override
            public void onResult(final LottieComposition composition) {
                LottieAnimationView.this.setComposition(composition);
            }
        };
        this.failureListener = new LottieListener<Throwable>() {
            @Override
            public void onResult(final Throwable cause) {
                throw new IllegalStateException("Unable to parse composition", cause);
            }
        };
        this.lottieDrawable = new LottieDrawable();
        this.wasAnimatingWhenDetached = false;
        this.autoPlay = false;
        this.useHardwareLayer = false;
        this.lottieOnCompositionLoadedListeners = new HashSet<LottieOnCompositionLoadedListener>();
        this.init(set);
    }
    
    private void cancelLoaderTask() {
        if (this.compositionTask != null) {
            this.compositionTask.removeListener(this.loadedListener);
            this.compositionTask.removeFailureListener(this.failureListener);
        }
    }
    
    private void clearComposition() {
        this.composition = null;
        this.lottieDrawable.clearComposition();
    }
    
    private void enableOrDisableHardwareLayer() {
        final boolean useHardwareLayer = this.useHardwareLayer;
        int n = 1;
        if (useHardwareLayer && this.lottieDrawable.isAnimating()) {
            n = 2;
        }
        this.setLayerType(n, (Paint)null);
    }
    
    private void init(final AttributeSet set) {
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.LottieAnimationView);
        if (!this.isInEditMode()) {
            final boolean hasValue = obtainStyledAttributes.hasValue(R.styleable.LottieAnimationView_lottie_rawRes);
            final boolean hasValue2 = obtainStyledAttributes.hasValue(R.styleable.LottieAnimationView_lottie_fileName);
            final boolean hasValue3 = obtainStyledAttributes.hasValue(R.styleable.LottieAnimationView_lottie_url);
            if (hasValue && hasValue2) {
                throw new IllegalArgumentException("lottie_rawRes and lottie_fileName cannot be used at the same time. Please use only one at once.");
            }
            if (hasValue) {
                final int resourceId = obtainStyledAttributes.getResourceId(R.styleable.LottieAnimationView_lottie_rawRes, 0);
                if (resourceId != 0) {
                    this.setAnimation(resourceId);
                }
            }
            else if (hasValue2) {
                final String string = obtainStyledAttributes.getString(R.styleable.LottieAnimationView_lottie_fileName);
                if (string != null) {
                    this.setAnimation(string);
                }
            }
            else if (hasValue3) {
                final String string2 = obtainStyledAttributes.getString(R.styleable.LottieAnimationView_lottie_url);
                if (string2 != null) {
                    this.setAnimationFromUrl(string2);
                }
            }
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.LottieAnimationView_lottie_autoPlay, false)) {
            this.wasAnimatingWhenDetached = true;
            this.autoPlay = true;
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.LottieAnimationView_lottie_loop, false)) {
            this.lottieDrawable.setRepeatCount(-1);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.LottieAnimationView_lottie_repeatMode)) {
            this.setRepeatMode(obtainStyledAttributes.getInt(R.styleable.LottieAnimationView_lottie_repeatMode, 1));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.LottieAnimationView_lottie_repeatCount)) {
            this.setRepeatCount(obtainStyledAttributes.getInt(R.styleable.LottieAnimationView_lottie_repeatCount, -1));
        }
        this.setImageAssetsFolder(obtainStyledAttributes.getString(R.styleable.LottieAnimationView_lottie_imageAssetsFolder));
        this.setProgress(obtainStyledAttributes.getFloat(R.styleable.LottieAnimationView_lottie_progress, 0.0f));
        this.enableMergePathsForKitKatAndAbove(obtainStyledAttributes.getBoolean(R.styleable.LottieAnimationView_lottie_enableMergePathsForKitKatAndAbove, false));
        if (obtainStyledAttributes.hasValue(R.styleable.LottieAnimationView_lottie_colorFilter)) {
            this.addValueCallback(new KeyPath(new String[] { "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>(new SimpleColorFilter(obtainStyledAttributes.getColor(R.styleable.LottieAnimationView_lottie_colorFilter, 0))));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.LottieAnimationView_lottie_scale)) {
            this.lottieDrawable.setScale(obtainStyledAttributes.getFloat(R.styleable.LottieAnimationView_lottie_scale, 1.0f));
        }
        obtainStyledAttributes.recycle();
        this.enableOrDisableHardwareLayer();
    }
    
    private void setCompositionTask(final LottieTask<LottieComposition> lottieTask) {
        this.clearComposition();
        this.cancelLoaderTask();
        this.compositionTask = lottieTask.addListener(this.loadedListener).addFailureListener(this.failureListener);
    }
    
    private void setImageDrawable(final Drawable imageDrawable, final boolean b) {
        if (b && imageDrawable != this.lottieDrawable) {
            this.recycleBitmaps();
        }
        this.cancelLoaderTask();
        super.setImageDrawable(imageDrawable);
    }
    
    public void addAnimatorListener(final Animator$AnimatorListener animator$AnimatorListener) {
        this.lottieDrawable.addAnimatorListener(animator$AnimatorListener);
    }
    
    public <T> void addValueCallback(final KeyPath keyPath, final T t, final LottieValueCallback<T> lottieValueCallback) {
        this.lottieDrawable.addValueCallback(keyPath, t, lottieValueCallback);
    }
    
    public void cancelAnimation() {
        this.lottieDrawable.cancelAnimation();
        this.enableOrDisableHardwareLayer();
    }
    
    public void enableMergePathsForKitKatAndAbove(final boolean b) {
        this.lottieDrawable.enableMergePathsForKitKatAndAbove(b);
    }
    
    public LottieComposition getComposition() {
        return this.composition;
    }
    
    public long getDuration() {
        long n;
        if (this.composition != null) {
            n = (long)this.composition.getDuration();
        }
        else {
            n = 0L;
        }
        return n;
    }
    
    public int getFrame() {
        return this.lottieDrawable.getFrame();
    }
    
    public String getImageAssetsFolder() {
        return this.lottieDrawable.getImageAssetsFolder();
    }
    
    public float getMaxFrame() {
        return this.lottieDrawable.getMaxFrame();
    }
    
    public float getMinFrame() {
        return this.lottieDrawable.getMinFrame();
    }
    
    public PerformanceTracker getPerformanceTracker() {
        return this.lottieDrawable.getPerformanceTracker();
    }
    
    public float getProgress() {
        return this.lottieDrawable.getProgress();
    }
    
    public int getRepeatCount() {
        return this.lottieDrawable.getRepeatCount();
    }
    
    public int getRepeatMode() {
        return this.lottieDrawable.getRepeatMode();
    }
    
    public float getScale() {
        return this.lottieDrawable.getScale();
    }
    
    public float getSpeed() {
        return this.lottieDrawable.getSpeed();
    }
    
    public boolean getUseHardwareAcceleration() {
        return this.useHardwareLayer;
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        if (this.getDrawable() == this.lottieDrawable) {
            super.invalidateDrawable((Drawable)this.lottieDrawable);
        }
        else {
            super.invalidateDrawable(drawable);
        }
    }
    
    public boolean isAnimating() {
        return this.lottieDrawable.isAnimating();
    }
    
    @Deprecated
    public void loop(final boolean b) {
        final LottieDrawable lottieDrawable = this.lottieDrawable;
        int repeatCount;
        if (b) {
            repeatCount = -1;
        }
        else {
            repeatCount = 0;
        }
        lottieDrawable.setRepeatCount(repeatCount);
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.autoPlay && this.wasAnimatingWhenDetached) {
            this.playAnimation();
        }
    }
    
    protected void onDetachedFromWindow() {
        if (this.isAnimating()) {
            this.cancelAnimation();
            this.wasAnimatingWhenDetached = true;
        }
        this.recycleBitmaps();
        super.onDetachedFromWindow();
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.animationName = savedState.animationName;
        if (!TextUtils.isEmpty((CharSequence)this.animationName)) {
            this.setAnimation(this.animationName);
        }
        this.animationResId = savedState.animationResId;
        if (this.animationResId != 0) {
            this.setAnimation(this.animationResId);
        }
        this.setProgress(savedState.progress);
        if (savedState.isAnimating) {
            this.playAnimation();
        }
        this.lottieDrawable.setImagesAssetsFolder(savedState.imageAssetsFolder);
        this.setRepeatMode(savedState.repeatMode);
        this.setRepeatCount(savedState.repeatCount);
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.animationName = this.animationName;
        savedState.animationResId = this.animationResId;
        savedState.progress = this.lottieDrawable.getProgress();
        savedState.isAnimating = this.lottieDrawable.isAnimating();
        savedState.imageAssetsFolder = this.lottieDrawable.getImageAssetsFolder();
        savedState.repeatMode = this.lottieDrawable.getRepeatMode();
        savedState.repeatCount = this.lottieDrawable.getRepeatCount();
        return (Parcelable)savedState;
    }
    
    public void playAnimation() {
        this.lottieDrawable.playAnimation();
        this.enableOrDisableHardwareLayer();
    }
    
    void recycleBitmaps() {
        this.lottieDrawable.recycleBitmaps();
    }
    
    public void setAnimation(final int animationResId) {
        this.animationResId = animationResId;
        this.animationName = null;
        this.setCompositionTask(LottieCompositionFactory.fromRawRes(this.getContext(), animationResId));
    }
    
    public void setAnimation(final JsonReader jsonReader, final String s) {
        this.setCompositionTask(LottieCompositionFactory.fromJsonReader(jsonReader, s));
    }
    
    public void setAnimation(final String animationName) {
        this.animationName = animationName;
        this.animationResId = 0;
        this.setCompositionTask(LottieCompositionFactory.fromAsset(this.getContext(), animationName));
    }
    
    @Deprecated
    public void setAnimationFromJson(final String s) {
        this.setAnimationFromJson(s, null);
    }
    
    public void setAnimationFromJson(final String s, final String s2) {
        this.setAnimation(new JsonReader((Reader)new StringReader(s)), s2);
    }
    
    public void setAnimationFromUrl(final String s) {
        this.setCompositionTask(LottieCompositionFactory.fromUrl(this.getContext(), s));
    }
    
    public void setComposition(final LottieComposition composition) {
        if (L.DBG) {
            final String tag = LottieAnimationView.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Set Composition \n");
            sb.append(composition);
            Log.v(tag, sb.toString());
        }
        this.lottieDrawable.setCallback((Drawable$Callback)this);
        this.composition = composition;
        final boolean setComposition = this.lottieDrawable.setComposition(composition);
        this.enableOrDisableHardwareLayer();
        if (this.getDrawable() == this.lottieDrawable && !setComposition) {
            return;
        }
        this.setImageDrawable(null);
        this.setImageDrawable(this.lottieDrawable);
        this.requestLayout();
        final Iterator<LottieOnCompositionLoadedListener> iterator = this.lottieOnCompositionLoadedListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onCompositionLoaded(composition);
        }
    }
    
    public void setFontAssetDelegate(final FontAssetDelegate fontAssetDelegate) {
        this.lottieDrawable.setFontAssetDelegate(fontAssetDelegate);
    }
    
    public void setFrame(final int frame) {
        this.lottieDrawable.setFrame(frame);
    }
    
    public void setImageAssetDelegate(final ImageAssetDelegate imageAssetDelegate) {
        this.lottieDrawable.setImageAssetDelegate(imageAssetDelegate);
    }
    
    public void setImageAssetsFolder(final String imagesAssetsFolder) {
        this.lottieDrawable.setImagesAssetsFolder(imagesAssetsFolder);
    }
    
    @Override
    public void setImageBitmap(final Bitmap imageBitmap) {
        this.recycleBitmaps();
        this.cancelLoaderTask();
        super.setImageBitmap(imageBitmap);
    }
    
    @Override
    public void setImageDrawable(final Drawable drawable) {
        this.setImageDrawable(drawable, true);
    }
    
    @Override
    public void setImageResource(final int imageResource) {
        this.recycleBitmaps();
        this.cancelLoaderTask();
        super.setImageResource(imageResource);
    }
    
    public void setMaxFrame(final int maxFrame) {
        this.lottieDrawable.setMaxFrame(maxFrame);
    }
    
    public void setMaxProgress(final float maxProgress) {
        this.lottieDrawable.setMaxProgress(maxProgress);
    }
    
    public void setMinFrame(final int minFrame) {
        this.lottieDrawable.setMinFrame(minFrame);
    }
    
    public void setMinProgress(final float minProgress) {
        this.lottieDrawable.setMinProgress(minProgress);
    }
    
    public void setPerformanceTrackingEnabled(final boolean performanceTrackingEnabled) {
        this.lottieDrawable.setPerformanceTrackingEnabled(performanceTrackingEnabled);
    }
    
    public void setProgress(final float progress) {
        this.lottieDrawable.setProgress(progress);
    }
    
    public void setRepeatCount(final int repeatCount) {
        this.lottieDrawable.setRepeatCount(repeatCount);
    }
    
    public void setRepeatMode(final int repeatMode) {
        this.lottieDrawable.setRepeatMode(repeatMode);
    }
    
    public void setScale(final float scale) {
        this.lottieDrawable.setScale(scale);
        if (this.getDrawable() == this.lottieDrawable) {
            this.setImageDrawable(null, false);
            this.setImageDrawable(this.lottieDrawable, false);
        }
    }
    
    public void setSpeed(final float speed) {
        this.lottieDrawable.setSpeed(speed);
    }
    
    public void setTextDelegate(final TextDelegate textDelegate) {
        this.lottieDrawable.setTextDelegate(textDelegate);
    }
    
    private static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        String animationName;
        int animationResId;
        String imageAssetsFolder;
        boolean isAnimating;
        float progress;
        int repeatCount;
        int repeatMode;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        private SavedState(final Parcel parcel) {
            super(parcel);
            this.animationName = parcel.readString();
            this.progress = parcel.readFloat();
            final int int1 = parcel.readInt();
            boolean isAnimating = true;
            if (int1 != 1) {
                isAnimating = false;
            }
            this.isAnimating = isAnimating;
            this.imageAssetsFolder = parcel.readString();
            this.repeatMode = parcel.readInt();
            this.repeatCount = parcel.readInt();
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeString(this.animationName);
            parcel.writeFloat(this.progress);
            parcel.writeInt((int)(this.isAnimating ? 1 : 0));
            parcel.writeString(this.imageAssetsFolder);
            parcel.writeInt(this.repeatMode);
            parcel.writeInt(this.repeatCount);
        }
    }
}
