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
import android.view.View;
import android.text.TextUtils;
import android.os.Parcelable;
import android.graphics.drawable.Drawable;
import android.content.res.TypedArray;
import com.airbnb.lottie.utils.Utils;
import android.graphics.ColorFilter;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.KeyPath;
import android.graphics.Paint;
import android.os.Build$VERSION;
import android.util.AttributeSet;
import java.util.HashSet;
import android.content.Context;
import java.util.Set;
import androidx.appcompat.widget.AppCompatImageView;

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
    private RenderMode renderMode;
    private boolean wasAnimatingWhenDetached;
    private boolean wasAnimatingWhenNotShown;
    
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
        this.wasAnimatingWhenNotShown = false;
        this.wasAnimatingWhenDetached = false;
        this.autoPlay = false;
        this.renderMode = RenderMode.AUTOMATIC;
        this.lottieOnCompositionLoadedListeners = new HashSet<LottieOnCompositionLoadedListener>();
        this.init(null);
    }
    
    private void cancelLoaderTask() {
        final LottieTask<LottieComposition> compositionTask = this.compositionTask;
        if (compositionTask != null) {
            compositionTask.removeListener(this.loadedListener);
            this.compositionTask.removeFailureListener(this.failureListener);
        }
    }
    
    private void clearComposition() {
        this.composition = null;
        this.lottieDrawable.clearComposition();
    }
    
    private void enableOrDisableHardwareLayer() {
        final int n = LottieAnimationView$4.$SwitchMap$com$airbnb$lottie$RenderMode[this.renderMode.ordinal()];
        final int n2 = 2;
        if (n != 1) {
            if (n != 2) {
                if (n == 3) {
                    final LottieComposition composition = this.composition;
                    boolean b = false;
                    if (composition == null || !composition.hasDashPattern() || Build$VERSION.SDK_INT >= 28) {
                        final LottieComposition composition2 = this.composition;
                        if (composition2 == null || composition2.getMaskAndMatteCount() <= 4) {
                            b = true;
                        }
                    }
                    int n3;
                    if (b) {
                        n3 = n2;
                    }
                    else {
                        n3 = 1;
                    }
                    this.setLayerType(n3, (Paint)null);
                }
            }
            else {
                this.setLayerType(1, (Paint)null);
            }
        }
        else {
            this.setLayerType(2, (Paint)null);
        }
    }
    
    private void init(final AttributeSet set) {
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R$styleable.LottieAnimationView);
        final boolean inEditMode = this.isInEditMode();
        boolean b = false;
        if (!inEditMode) {
            final boolean hasValue = obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_rawRes);
            final boolean hasValue2 = obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_fileName);
            final boolean hasValue3 = obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_url);
            if (hasValue && hasValue2) {
                throw new IllegalArgumentException("lottie_rawRes and lottie_fileName cannot be used at the same time. Please use only one at once.");
            }
            if (hasValue) {
                final int resourceId = obtainStyledAttributes.getResourceId(R$styleable.LottieAnimationView_lottie_rawRes, 0);
                if (resourceId != 0) {
                    this.setAnimation(resourceId);
                }
            }
            else if (hasValue2) {
                final String string = obtainStyledAttributes.getString(R$styleable.LottieAnimationView_lottie_fileName);
                if (string != null) {
                    this.setAnimation(string);
                }
            }
            else if (hasValue3) {
                final String string2 = obtainStyledAttributes.getString(R$styleable.LottieAnimationView_lottie_url);
                if (string2 != null) {
                    this.setAnimationFromUrl(string2);
                }
            }
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_autoPlay, false)) {
            this.wasAnimatingWhenDetached = true;
            this.autoPlay = true;
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_loop, false)) {
            this.lottieDrawable.setRepeatCount(-1);
        }
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_repeatMode)) {
            this.setRepeatMode(obtainStyledAttributes.getInt(R$styleable.LottieAnimationView_lottie_repeatMode, 1));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_repeatCount)) {
            this.setRepeatCount(obtainStyledAttributes.getInt(R$styleable.LottieAnimationView_lottie_repeatCount, -1));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_speed)) {
            this.setSpeed(obtainStyledAttributes.getFloat(R$styleable.LottieAnimationView_lottie_speed, 1.0f));
        }
        this.setImageAssetsFolder(obtainStyledAttributes.getString(R$styleable.LottieAnimationView_lottie_imageAssetsFolder));
        this.setProgress(obtainStyledAttributes.getFloat(R$styleable.LottieAnimationView_lottie_progress, 0.0f));
        this.enableMergePathsForKitKatAndAbove(obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_enableMergePathsForKitKatAndAbove, false));
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_colorFilter)) {
            this.addValueCallback(new KeyPath(new String[] { "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>(new SimpleColorFilter(obtainStyledAttributes.getColor(R$styleable.LottieAnimationView_lottie_colorFilter, 0))));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_scale)) {
            this.lottieDrawable.setScale(obtainStyledAttributes.getFloat(R$styleable.LottieAnimationView_lottie_scale, 1.0f));
        }
        obtainStyledAttributes.recycle();
        final LottieDrawable lottieDrawable = this.lottieDrawable;
        if (Utils.getAnimationScale(this.getContext()) != 0.0f) {
            b = true;
        }
        lottieDrawable.setSystemAnimationsAreEnabled(b);
        this.enableOrDisableHardwareLayer();
    }
    
    private void setCompositionTask(final LottieTask<LottieComposition> compositionTask) {
        this.clearComposition();
        this.cancelLoaderTask();
        compositionTask.addListener(this.loadedListener);
        compositionTask.addFailureListener(this.failureListener);
        this.compositionTask = compositionTask;
    }
    
    public <T> void addValueCallback(final KeyPath keyPath, final T t, final LottieValueCallback<T> lottieValueCallback) {
        this.lottieDrawable.addValueCallback(keyPath, t, lottieValueCallback);
    }
    
    public void buildDrawingCache(final boolean b) {
        super.buildDrawingCache(b);
        if (this.getLayerType() == 1 && this.getDrawingCache(b) == null) {
            this.setRenderMode(RenderMode.HARDWARE);
        }
    }
    
    public void cancelAnimation() {
        this.wasAnimatingWhenNotShown = false;
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
        final LottieComposition composition = this.composition;
        long n;
        if (composition != null) {
            n = (long)composition.getDuration();
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
    
    public void invalidateDrawable(final Drawable drawable) {
        final Drawable drawable2 = this.getDrawable();
        final LottieDrawable lottieDrawable = this.lottieDrawable;
        if (drawable2 == lottieDrawable) {
            super.invalidateDrawable((Drawable)lottieDrawable);
        }
        else {
            super.invalidateDrawable(drawable);
        }
    }
    
    public boolean isAnimating() {
        return this.lottieDrawable.isAnimating();
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
        final int animationResId = this.animationResId;
        if (animationResId != 0) {
            this.setAnimation(animationResId);
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
    
    protected void onVisibilityChanged(final View view, final int n) {
        if (this.lottieDrawable == null) {
            return;
        }
        if (this.isShown()) {
            if (this.wasAnimatingWhenNotShown) {
                this.resumeAnimation();
                this.wasAnimatingWhenNotShown = false;
            }
        }
        else if (this.isAnimating()) {
            this.pauseAnimation();
            this.wasAnimatingWhenNotShown = true;
        }
    }
    
    public void pauseAnimation() {
        this.wasAnimatingWhenDetached = false;
        this.wasAnimatingWhenNotShown = false;
        this.lottieDrawable.pauseAnimation();
        this.enableOrDisableHardwareLayer();
    }
    
    public void playAnimation() {
        if (this.isShown()) {
            this.lottieDrawable.playAnimation();
            this.enableOrDisableHardwareLayer();
        }
        else {
            this.wasAnimatingWhenNotShown = true;
        }
    }
    
    public void resumeAnimation() {
        if (this.isShown()) {
            this.lottieDrawable.resumeAnimation();
            this.enableOrDisableHardwareLayer();
        }
        else {
            this.wasAnimatingWhenNotShown = true;
        }
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
        this.cancelLoaderTask();
        super.setImageBitmap(imageBitmap);
    }
    
    @Override
    public void setImageDrawable(final Drawable imageDrawable) {
        this.cancelLoaderTask();
        super.setImageDrawable(imageDrawable);
    }
    
    @Override
    public void setImageResource(final int imageResource) {
        this.cancelLoaderTask();
        super.setImageResource(imageResource);
    }
    
    public void setMaxFrame(final int maxFrame) {
        this.lottieDrawable.setMaxFrame(maxFrame);
    }
    
    public void setMaxFrame(final String maxFrame) {
        this.lottieDrawable.setMaxFrame(maxFrame);
    }
    
    public void setMaxProgress(final float maxProgress) {
        this.lottieDrawable.setMaxProgress(maxProgress);
    }
    
    public void setMinAndMaxFrame(final String minAndMaxFrame) {
        this.lottieDrawable.setMinAndMaxFrame(minAndMaxFrame);
    }
    
    public void setMinFrame(final int minFrame) {
        this.lottieDrawable.setMinFrame(minFrame);
    }
    
    public void setMinFrame(final String minFrame) {
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
    
    public void setRenderMode(final RenderMode renderMode) {
        this.renderMode = renderMode;
        this.enableOrDisableHardwareLayer();
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
            this.setImageDrawable(null);
            this.setImageDrawable(this.lottieDrawable);
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
