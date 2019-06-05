package com.airbnb.lottie;

import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.Log;
import android.view.View.BaseSavedState;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LottieAnimationView extends AppCompatImageView {
   private static final String TAG = "LottieAnimationView";
   private String animationName;
   private int animationResId;
   private boolean autoPlay = false;
   private LottieComposition composition;
   private LottieTask compositionTask;
   private final LottieListener failureListener = new LottieListener() {
      public void onResult(Throwable var1) {
         throw new IllegalStateException("Unable to parse composition", var1);
      }
   };
   private final LottieListener loadedListener = new LottieListener() {
      public void onResult(LottieComposition var1) {
         LottieAnimationView.this.setComposition(var1);
      }
   };
   private final LottieDrawable lottieDrawable = new LottieDrawable();
   private Set lottieOnCompositionLoadedListeners = new HashSet();
   private boolean useHardwareLayer = false;
   private boolean wasAnimatingWhenDetached = false;

   public LottieAnimationView(Context var1) {
      super(var1);
      this.init((AttributeSet)null);
   }

   public LottieAnimationView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init(var2);
   }

   public LottieAnimationView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init(var2);
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
      boolean var1 = this.useHardwareLayer;
      byte var2 = 1;
      boolean var3;
      if (var1 && this.lottieDrawable.isAnimating()) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         var2 = 2;
      }

      this.setLayerType(var2, (Paint)null);
   }

   private void init(AttributeSet var1) {
      TypedArray var8 = this.getContext().obtainStyledAttributes(var1, R.styleable.LottieAnimationView);
      if (!this.isInEditMode()) {
         boolean var2 = var8.hasValue(R.styleable.LottieAnimationView_lottie_rawRes);
         boolean var3 = var8.hasValue(R.styleable.LottieAnimationView_lottie_fileName);
         boolean var4 = var8.hasValue(R.styleable.LottieAnimationView_lottie_url);
         if (var2 && var3) {
            throw new IllegalArgumentException("lottie_rawRes and lottie_fileName cannot be used at the same time. Please use only one at once.");
         }

         if (var2) {
            int var5 = var8.getResourceId(R.styleable.LottieAnimationView_lottie_rawRes, 0);
            if (var5 != 0) {
               this.setAnimation(var5);
            }
         } else {
            String var6;
            if (var3) {
               var6 = var8.getString(R.styleable.LottieAnimationView_lottie_fileName);
               if (var6 != null) {
                  this.setAnimation(var6);
               }
            } else if (var4) {
               var6 = var8.getString(R.styleable.LottieAnimationView_lottie_url);
               if (var6 != null) {
                  this.setAnimationFromUrl(var6);
               }
            }
         }
      }

      if (var8.getBoolean(R.styleable.LottieAnimationView_lottie_autoPlay, false)) {
         this.wasAnimatingWhenDetached = true;
         this.autoPlay = true;
      }

      if (var8.getBoolean(R.styleable.LottieAnimationView_lottie_loop, false)) {
         this.lottieDrawable.setRepeatCount(-1);
      }

      if (var8.hasValue(R.styleable.LottieAnimationView_lottie_repeatMode)) {
         this.setRepeatMode(var8.getInt(R.styleable.LottieAnimationView_lottie_repeatMode, 1));
      }

      if (var8.hasValue(R.styleable.LottieAnimationView_lottie_repeatCount)) {
         this.setRepeatCount(var8.getInt(R.styleable.LottieAnimationView_lottie_repeatCount, -1));
      }

      this.setImageAssetsFolder(var8.getString(R.styleable.LottieAnimationView_lottie_imageAssetsFolder));
      this.setProgress(var8.getFloat(R.styleable.LottieAnimationView_lottie_progress, 0.0F));
      this.enableMergePathsForKitKatAndAbove(var8.getBoolean(R.styleable.LottieAnimationView_lottie_enableMergePathsForKitKatAndAbove, false));
      if (var8.hasValue(R.styleable.LottieAnimationView_lottie_colorFilter)) {
         SimpleColorFilter var7 = new SimpleColorFilter(var8.getColor(R.styleable.LottieAnimationView_lottie_colorFilter, 0));
         KeyPath var9 = new KeyPath(new String[]{"**"});
         LottieValueCallback var10 = new LottieValueCallback(var7);
         this.addValueCallback(var9, LottieProperty.COLOR_FILTER, var10);
      }

      if (var8.hasValue(R.styleable.LottieAnimationView_lottie_scale)) {
         this.lottieDrawable.setScale(var8.getFloat(R.styleable.LottieAnimationView_lottie_scale, 1.0F));
      }

      var8.recycle();
      this.enableOrDisableHardwareLayer();
   }

   private void setCompositionTask(LottieTask var1) {
      this.clearComposition();
      this.cancelLoaderTask();
      this.compositionTask = var1.addListener(this.loadedListener).addFailureListener(this.failureListener);
   }

   private void setImageDrawable(Drawable var1, boolean var2) {
      if (var2 && var1 != this.lottieDrawable) {
         this.recycleBitmaps();
      }

      this.cancelLoaderTask();
      super.setImageDrawable(var1);
   }

   public void addAnimatorListener(AnimatorListener var1) {
      this.lottieDrawable.addAnimatorListener(var1);
   }

   public void addValueCallback(KeyPath var1, Object var2, LottieValueCallback var3) {
      this.lottieDrawable.addValueCallback(var1, var2, var3);
   }

   public void cancelAnimation() {
      this.lottieDrawable.cancelAnimation();
      this.enableOrDisableHardwareLayer();
   }

   public void enableMergePathsForKitKatAndAbove(boolean var1) {
      this.lottieDrawable.enableMergePathsForKitKatAndAbove(var1);
   }

   public LottieComposition getComposition() {
      return this.composition;
   }

   public long getDuration() {
      long var1;
      if (this.composition != null) {
         var1 = (long)this.composition.getDuration();
      } else {
         var1 = 0L;
      }

      return var1;
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

   public void invalidateDrawable(Drawable var1) {
      if (this.getDrawable() == this.lottieDrawable) {
         super.invalidateDrawable(this.lottieDrawable);
      } else {
         super.invalidateDrawable(var1);
      }

   }

   public boolean isAnimating() {
      return this.lottieDrawable.isAnimating();
   }

   @Deprecated
   public void loop(boolean var1) {
      LottieDrawable var2 = this.lottieDrawable;
      byte var3;
      if (var1) {
         var3 = -1;
      } else {
         var3 = 0;
      }

      var2.setRepeatCount(var3);
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

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof LottieAnimationView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         LottieAnimationView.SavedState var2 = (LottieAnimationView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.animationName = var2.animationName;
         if (!TextUtils.isEmpty(this.animationName)) {
            this.setAnimation(this.animationName);
         }

         this.animationResId = var2.animationResId;
         if (this.animationResId != 0) {
            this.setAnimation(this.animationResId);
         }

         this.setProgress(var2.progress);
         if (var2.isAnimating) {
            this.playAnimation();
         }

         this.lottieDrawable.setImagesAssetsFolder(var2.imageAssetsFolder);
         this.setRepeatMode(var2.repeatMode);
         this.setRepeatCount(var2.repeatCount);
      }
   }

   protected Parcelable onSaveInstanceState() {
      LottieAnimationView.SavedState var1 = new LottieAnimationView.SavedState(super.onSaveInstanceState());
      var1.animationName = this.animationName;
      var1.animationResId = this.animationResId;
      var1.progress = this.lottieDrawable.getProgress();
      var1.isAnimating = this.lottieDrawable.isAnimating();
      var1.imageAssetsFolder = this.lottieDrawable.getImageAssetsFolder();
      var1.repeatMode = this.lottieDrawable.getRepeatMode();
      var1.repeatCount = this.lottieDrawable.getRepeatCount();
      return var1;
   }

   public void playAnimation() {
      this.lottieDrawable.playAnimation();
      this.enableOrDisableHardwareLayer();
   }

   void recycleBitmaps() {
      this.lottieDrawable.recycleBitmaps();
   }

   public void setAnimation(int var1) {
      this.animationResId = var1;
      this.animationName = null;
      this.setCompositionTask(LottieCompositionFactory.fromRawRes(this.getContext(), var1));
   }

   public void setAnimation(JsonReader var1, String var2) {
      this.setCompositionTask(LottieCompositionFactory.fromJsonReader(var1, var2));
   }

   public void setAnimation(String var1) {
      this.animationName = var1;
      this.animationResId = 0;
      this.setCompositionTask(LottieCompositionFactory.fromAsset(this.getContext(), var1));
   }

   @Deprecated
   public void setAnimationFromJson(String var1) {
      this.setAnimationFromJson(var1, (String)null);
   }

   public void setAnimationFromJson(String var1, String var2) {
      this.setAnimation(new JsonReader(new StringReader(var1)), var2);
   }

   public void setAnimationFromUrl(String var1) {
      this.setCompositionTask(LottieCompositionFactory.fromUrl(this.getContext(), var1));
   }

   public void setComposition(LottieComposition var1) {
      if (L.DBG) {
         String var2 = TAG;
         StringBuilder var3 = new StringBuilder();
         var3.append("Set Composition \n");
         var3.append(var1);
         Log.v(var2, var3.toString());
      }

      this.lottieDrawable.setCallback(this);
      this.composition = var1;
      boolean var4 = this.lottieDrawable.setComposition(var1);
      this.enableOrDisableHardwareLayer();
      if (this.getDrawable() != this.lottieDrawable || var4) {
         this.setImageDrawable((Drawable)null);
         this.setImageDrawable(this.lottieDrawable);
         this.requestLayout();
         Iterator var5 = this.lottieOnCompositionLoadedListeners.iterator();

         while(var5.hasNext()) {
            ((LottieOnCompositionLoadedListener)var5.next()).onCompositionLoaded(var1);
         }

      }
   }

   public void setFontAssetDelegate(FontAssetDelegate var1) {
      this.lottieDrawable.setFontAssetDelegate(var1);
   }

   public void setFrame(int var1) {
      this.lottieDrawable.setFrame(var1);
   }

   public void setImageAssetDelegate(ImageAssetDelegate var1) {
      this.lottieDrawable.setImageAssetDelegate(var1);
   }

   public void setImageAssetsFolder(String var1) {
      this.lottieDrawable.setImagesAssetsFolder(var1);
   }

   public void setImageBitmap(Bitmap var1) {
      this.recycleBitmaps();
      this.cancelLoaderTask();
      super.setImageBitmap(var1);
   }

   public void setImageDrawable(Drawable var1) {
      this.setImageDrawable(var1, true);
   }

   public void setImageResource(int var1) {
      this.recycleBitmaps();
      this.cancelLoaderTask();
      super.setImageResource(var1);
   }

   public void setMaxFrame(int var1) {
      this.lottieDrawable.setMaxFrame(var1);
   }

   public void setMaxProgress(float var1) {
      this.lottieDrawable.setMaxProgress(var1);
   }

   public void setMinFrame(int var1) {
      this.lottieDrawable.setMinFrame(var1);
   }

   public void setMinProgress(float var1) {
      this.lottieDrawable.setMinProgress(var1);
   }

   public void setPerformanceTrackingEnabled(boolean var1) {
      this.lottieDrawable.setPerformanceTrackingEnabled(var1);
   }

   public void setProgress(float var1) {
      this.lottieDrawable.setProgress(var1);
   }

   public void setRepeatCount(int var1) {
      this.lottieDrawable.setRepeatCount(var1);
   }

   public void setRepeatMode(int var1) {
      this.lottieDrawable.setRepeatMode(var1);
   }

   public void setScale(float var1) {
      this.lottieDrawable.setScale(var1);
      if (this.getDrawable() == this.lottieDrawable) {
         this.setImageDrawable((Drawable)null, false);
         this.setImageDrawable(this.lottieDrawable, false);
      }

   }

   public void setSpeed(float var1) {
      this.lottieDrawable.setSpeed(var1);
   }

   public void setTextDelegate(TextDelegate var1) {
      this.lottieDrawable.setTextDelegate(var1);
   }

   private static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public LottieAnimationView.SavedState createFromParcel(Parcel var1) {
            return new LottieAnimationView.SavedState(var1);
         }

         public LottieAnimationView.SavedState[] newArray(int var1) {
            return new LottieAnimationView.SavedState[var1];
         }
      };
      String animationName;
      int animationResId;
      String imageAssetsFolder;
      boolean isAnimating;
      float progress;
      int repeatCount;
      int repeatMode;

      private SavedState(Parcel var1) {
         super(var1);
         this.animationName = var1.readString();
         this.progress = var1.readFloat();
         int var2 = var1.readInt();
         boolean var3 = true;
         if (var2 != 1) {
            var3 = false;
         }

         this.isAnimating = var3;
         this.imageAssetsFolder = var1.readString();
         this.repeatMode = var1.readInt();
         this.repeatCount = var1.readInt();
      }

      // $FF: synthetic method
      SavedState(Parcel var1, Object var2) {
         this(var1);
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeString(this.animationName);
         var1.writeFloat(this.progress);
         var1.writeInt(this.isAnimating);
         var1.writeString(this.imageAssetsFolder);
         var1.writeInt(this.repeatMode);
         var1.writeInt(this.repeatCount);
      }
   }
}
