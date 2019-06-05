package com.airbnb.lottie;

import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.p004v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.Log;
import android.view.View.BaseSavedState;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class LottieAnimationView extends AppCompatImageView {
    private static final String TAG = "LottieAnimationView";
    private String animationName;
    private int animationResId;
    private boolean autoPlay = false;
    private LottieComposition composition;
    private LottieTask<LottieComposition> compositionTask;
    private final LottieListener<Throwable> failureListener = new C06692();
    private final LottieListener<LottieComposition> loadedListener = new C06681();
    private final LottieDrawable lottieDrawable = new LottieDrawable();
    private Set<LottieOnCompositionLoadedListener> lottieOnCompositionLoadedListeners = new HashSet();
    private boolean useHardwareLayer = false;
    private boolean wasAnimatingWhenDetached = false;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C03531();
        String animationName;
        int animationResId;
        String imageAssetsFolder;
        boolean isAnimating;
        float progress;
        int repeatCount;
        int repeatMode;

        /* renamed from: com.airbnb.lottie.LottieAnimationView$SavedState$1 */
        static class C03531 implements Creator<SavedState> {
            C03531() {
            }

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        }

        /* synthetic */ SavedState(Parcel parcel, C06681 c06681) {
            this(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.animationName = parcel.readString();
            this.progress = parcel.readFloat();
            boolean z = true;
            if (parcel.readInt() != 1) {
                z = false;
            }
            this.isAnimating = z;
            this.imageAssetsFolder = parcel.readString();
            this.repeatMode = parcel.readInt();
            this.repeatCount = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.animationName);
            parcel.writeFloat(this.progress);
            parcel.writeInt(this.isAnimating);
            parcel.writeString(this.imageAssetsFolder);
            parcel.writeInt(this.repeatMode);
            parcel.writeInt(this.repeatCount);
        }
    }

    /* renamed from: com.airbnb.lottie.LottieAnimationView$1 */
    class C06681 implements LottieListener<LottieComposition> {
        C06681() {
        }

        public void onResult(LottieComposition lottieComposition) {
            LottieAnimationView.this.setComposition(lottieComposition);
        }
    }

    /* renamed from: com.airbnb.lottie.LottieAnimationView$2 */
    class C06692 implements LottieListener<Throwable> {
        C06692() {
        }

        public void onResult(Throwable th) {
            throw new IllegalStateException("Unable to parse composition", th);
        }
    }

    public LottieAnimationView(Context context) {
        super(context);
        init(null);
    }

    public LottieAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public LottieAnimationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, C0373R.styleable.LottieAnimationView);
        if (!isInEditMode()) {
            boolean hasValue = obtainStyledAttributes.hasValue(C0373R.styleable.LottieAnimationView_lottie_rawRes);
            boolean hasValue2 = obtainStyledAttributes.hasValue(C0373R.styleable.LottieAnimationView_lottie_fileName);
            boolean hasValue3 = obtainStyledAttributes.hasValue(C0373R.styleable.LottieAnimationView_lottie_url);
            String string;
            if (hasValue && hasValue2) {
                throw new IllegalArgumentException("lottie_rawRes and lottie_fileName cannot be used at the same time. Please use only one at once.");
            } else if (hasValue) {
                int resourceId = obtainStyledAttributes.getResourceId(C0373R.styleable.LottieAnimationView_lottie_rawRes, 0);
                if (resourceId != 0) {
                    setAnimation(resourceId);
                }
            } else if (hasValue2) {
                string = obtainStyledAttributes.getString(C0373R.styleable.LottieAnimationView_lottie_fileName);
                if (string != null) {
                    setAnimation(string);
                }
            } else if (hasValue3) {
                string = obtainStyledAttributes.getString(C0373R.styleable.LottieAnimationView_lottie_url);
                if (string != null) {
                    setAnimationFromUrl(string);
                }
            }
        }
        if (obtainStyledAttributes.getBoolean(C0373R.styleable.LottieAnimationView_lottie_autoPlay, false)) {
            this.wasAnimatingWhenDetached = true;
            this.autoPlay = true;
        }
        if (obtainStyledAttributes.getBoolean(C0373R.styleable.LottieAnimationView_lottie_loop, false)) {
            this.lottieDrawable.setRepeatCount(-1);
        }
        if (obtainStyledAttributes.hasValue(C0373R.styleable.LottieAnimationView_lottie_repeatMode)) {
            setRepeatMode(obtainStyledAttributes.getInt(C0373R.styleable.LottieAnimationView_lottie_repeatMode, 1));
        }
        if (obtainStyledAttributes.hasValue(C0373R.styleable.LottieAnimationView_lottie_repeatCount)) {
            setRepeatCount(obtainStyledAttributes.getInt(C0373R.styleable.LottieAnimationView_lottie_repeatCount, -1));
        }
        setImageAssetsFolder(obtainStyledAttributes.getString(C0373R.styleable.LottieAnimationView_lottie_imageAssetsFolder));
        setProgress(obtainStyledAttributes.getFloat(C0373R.styleable.LottieAnimationView_lottie_progress, 0.0f));
        enableMergePathsForKitKatAndAbove(obtainStyledAttributes.getBoolean(C0373R.styleable.LottieAnimationView_lottie_enableMergePathsForKitKatAndAbove, false));
        if (obtainStyledAttributes.hasValue(C0373R.styleable.LottieAnimationView_lottie_colorFilter)) {
            SimpleColorFilter simpleColorFilter = new SimpleColorFilter(obtainStyledAttributes.getColor(C0373R.styleable.LottieAnimationView_lottie_colorFilter, 0));
            addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER, new LottieValueCallback(simpleColorFilter));
        }
        if (obtainStyledAttributes.hasValue(C0373R.styleable.LottieAnimationView_lottie_scale)) {
            this.lottieDrawable.setScale(obtainStyledAttributes.getFloat(C0373R.styleable.LottieAnimationView_lottie_scale, 1.0f));
        }
        obtainStyledAttributes.recycle();
        enableOrDisableHardwareLayer();
    }

    public void setImageResource(int i) {
        recycleBitmaps();
        cancelLoaderTask();
        super.setImageResource(i);
    }

    public void setImageDrawable(Drawable drawable) {
        setImageDrawable(drawable, true);
    }

    private void setImageDrawable(Drawable drawable, boolean z) {
        if (z && drawable != this.lottieDrawable) {
            recycleBitmaps();
        }
        cancelLoaderTask();
        super.setImageDrawable(drawable);
    }

    public void setImageBitmap(Bitmap bitmap) {
        recycleBitmaps();
        cancelLoaderTask();
        super.setImageBitmap(bitmap);
    }

    public void invalidateDrawable(Drawable drawable) {
        if (getDrawable() == this.lottieDrawable) {
            super.invalidateDrawable(this.lottieDrawable);
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.animationName = this.animationName;
        savedState.animationResId = this.animationResId;
        savedState.progress = this.lottieDrawable.getProgress();
        savedState.isAnimating = this.lottieDrawable.isAnimating();
        savedState.imageAssetsFolder = this.lottieDrawable.getImageAssetsFolder();
        savedState.repeatMode = this.lottieDrawable.getRepeatMode();
        savedState.repeatCount = this.lottieDrawable.getRepeatCount();
        return savedState;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            this.animationName = savedState.animationName;
            if (!TextUtils.isEmpty(this.animationName)) {
                setAnimation(this.animationName);
            }
            this.animationResId = savedState.animationResId;
            if (this.animationResId != 0) {
                setAnimation(this.animationResId);
            }
            setProgress(savedState.progress);
            if (savedState.isAnimating) {
                playAnimation();
            }
            this.lottieDrawable.setImagesAssetsFolder(savedState.imageAssetsFolder);
            setRepeatMode(savedState.repeatMode);
            setRepeatCount(savedState.repeatCount);
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.autoPlay && this.wasAnimatingWhenDetached) {
            playAnimation();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        if (isAnimating()) {
            cancelAnimation();
            this.wasAnimatingWhenDetached = true;
        }
        recycleBitmaps();
        super.onDetachedFromWindow();
    }

    /* Access modifiers changed, original: 0000 */
    public void recycleBitmaps() {
        this.lottieDrawable.recycleBitmaps();
    }

    public void enableMergePathsForKitKatAndAbove(boolean z) {
        this.lottieDrawable.enableMergePathsForKitKatAndAbove(z);
    }

    public boolean getUseHardwareAcceleration() {
        return this.useHardwareLayer;
    }

    public void setAnimation(int i) {
        this.animationResId = i;
        this.animationName = null;
        setCompositionTask(LottieCompositionFactory.fromRawRes(getContext(), i));
    }

    public void setAnimation(String str) {
        this.animationName = str;
        this.animationResId = 0;
        setCompositionTask(LottieCompositionFactory.fromAsset(getContext(), str));
    }

    @Deprecated
    public void setAnimationFromJson(String str) {
        setAnimationFromJson(str, null);
    }

    public void setAnimationFromJson(String str, String str2) {
        setAnimation(new JsonReader(new StringReader(str)), str2);
    }

    public void setAnimation(JsonReader jsonReader, String str) {
        setCompositionTask(LottieCompositionFactory.fromJsonReader(jsonReader, str));
    }

    public void setAnimationFromUrl(String str) {
        setCompositionTask(LottieCompositionFactory.fromUrl(getContext(), str));
    }

    private void setCompositionTask(LottieTask<LottieComposition> lottieTask) {
        clearComposition();
        cancelLoaderTask();
        this.compositionTask = lottieTask.addListener(this.loadedListener).addFailureListener(this.failureListener);
    }

    private void cancelLoaderTask() {
        if (this.compositionTask != null) {
            this.compositionTask.removeListener(this.loadedListener);
            this.compositionTask.removeFailureListener(this.failureListener);
        }
    }

    public void setComposition(LottieComposition lottieComposition) {
        if (C0352L.DBG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Set Composition \n");
            stringBuilder.append(lottieComposition);
            Log.v(str, stringBuilder.toString());
        }
        this.lottieDrawable.setCallback(this);
        this.composition = lottieComposition;
        boolean composition = this.lottieDrawable.setComposition(lottieComposition);
        enableOrDisableHardwareLayer();
        if (getDrawable() != this.lottieDrawable || composition) {
            setImageDrawable(null);
            setImageDrawable(this.lottieDrawable);
            requestLayout();
            for (LottieOnCompositionLoadedListener onCompositionLoaded : this.lottieOnCompositionLoadedListeners) {
                onCompositionLoaded.onCompositionLoaded(lottieComposition);
            }
        }
    }

    public LottieComposition getComposition() {
        return this.composition;
    }

    public void playAnimation() {
        this.lottieDrawable.playAnimation();
        enableOrDisableHardwareLayer();
    }

    public void setMinFrame(int i) {
        this.lottieDrawable.setMinFrame(i);
    }

    public float getMinFrame() {
        return this.lottieDrawable.getMinFrame();
    }

    public void setMinProgress(float f) {
        this.lottieDrawable.setMinProgress(f);
    }

    public void setMaxFrame(int i) {
        this.lottieDrawable.setMaxFrame(i);
    }

    public float getMaxFrame() {
        return this.lottieDrawable.getMaxFrame();
    }

    public void setMaxProgress(float f) {
        this.lottieDrawable.setMaxProgress(f);
    }

    public void setSpeed(float f) {
        this.lottieDrawable.setSpeed(f);
    }

    public float getSpeed() {
        return this.lottieDrawable.getSpeed();
    }

    public void addAnimatorListener(AnimatorListener animatorListener) {
        this.lottieDrawable.addAnimatorListener(animatorListener);
    }

    @Deprecated
    public void loop(boolean z) {
        this.lottieDrawable.setRepeatCount(z ? -1 : 0);
    }

    public void setRepeatMode(int i) {
        this.lottieDrawable.setRepeatMode(i);
    }

    public int getRepeatMode() {
        return this.lottieDrawable.getRepeatMode();
    }

    public void setRepeatCount(int i) {
        this.lottieDrawable.setRepeatCount(i);
    }

    public int getRepeatCount() {
        return this.lottieDrawable.getRepeatCount();
    }

    public boolean isAnimating() {
        return this.lottieDrawable.isAnimating();
    }

    public void setImageAssetsFolder(String str) {
        this.lottieDrawable.setImagesAssetsFolder(str);
    }

    public String getImageAssetsFolder() {
        return this.lottieDrawable.getImageAssetsFolder();
    }

    public void setImageAssetDelegate(ImageAssetDelegate imageAssetDelegate) {
        this.lottieDrawable.setImageAssetDelegate(imageAssetDelegate);
    }

    public void setFontAssetDelegate(FontAssetDelegate fontAssetDelegate) {
        this.lottieDrawable.setFontAssetDelegate(fontAssetDelegate);
    }

    public void setTextDelegate(TextDelegate textDelegate) {
        this.lottieDrawable.setTextDelegate(textDelegate);
    }

    public <T> void addValueCallback(KeyPath keyPath, T t, LottieValueCallback<T> lottieValueCallback) {
        this.lottieDrawable.addValueCallback(keyPath, t, lottieValueCallback);
    }

    public void setScale(float f) {
        this.lottieDrawable.setScale(f);
        if (getDrawable() == this.lottieDrawable) {
            setImageDrawable(null, false);
            setImageDrawable(this.lottieDrawable, false);
        }
    }

    public float getScale() {
        return this.lottieDrawable.getScale();
    }

    public void cancelAnimation() {
        this.lottieDrawable.cancelAnimation();
        enableOrDisableHardwareLayer();
    }

    public void setFrame(int i) {
        this.lottieDrawable.setFrame(i);
    }

    public int getFrame() {
        return this.lottieDrawable.getFrame();
    }

    public void setProgress(float f) {
        this.lottieDrawable.setProgress(f);
    }

    public float getProgress() {
        return this.lottieDrawable.getProgress();
    }

    public long getDuration() {
        return this.composition != null ? (long) this.composition.getDuration() : 0;
    }

    public void setPerformanceTrackingEnabled(boolean z) {
        this.lottieDrawable.setPerformanceTrackingEnabled(z);
    }

    public PerformanceTracker getPerformanceTracker() {
        return this.lottieDrawable.getPerformanceTracker();
    }

    private void clearComposition() {
        this.composition = null;
        this.lottieDrawable.clearComposition();
    }

    private void enableOrDisableHardwareLayer() {
        int i = 1;
        Object obj = (this.useHardwareLayer && this.lottieDrawable.isAnimating()) ? 1 : null;
        if (obj != null) {
            i = 2;
        }
        setLayerType(i, null);
    }
}
