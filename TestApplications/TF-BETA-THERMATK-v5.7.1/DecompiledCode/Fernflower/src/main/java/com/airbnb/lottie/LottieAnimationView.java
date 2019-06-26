package com.airbnb.lottie;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.View.BaseSavedState;
import androidx.appcompat.widget.AppCompatImageView;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.utils.Utils;
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
   private Set lottieOnCompositionLoadedListeners;
   private RenderMode renderMode;
   private boolean wasAnimatingWhenDetached = false;
   private boolean wasAnimatingWhenNotShown = false;

   public LottieAnimationView(Context var1) {
      super(var1);
      this.renderMode = RenderMode.AUTOMATIC;
      this.lottieOnCompositionLoadedListeners = new HashSet();
      this.init((AttributeSet)null);
   }

   private void cancelLoaderTask() {
      LottieTask var1 = this.compositionTask;
      if (var1 != null) {
         var1.removeListener(this.loadedListener);
         this.compositionTask.removeFailureListener(this.failureListener);
      }

   }

   private void clearComposition() {
      this.composition = null;
      this.lottieDrawable.clearComposition();
   }

   private void enableOrDisableHardwareLayer() {
      int var1 = null.$SwitchMap$com$airbnb$lottie$RenderMode[this.renderMode.ordinal()];
      byte var2 = 2;
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 == 3) {
               LottieComposition var3 = this.composition;
               boolean var4 = false;
               if (var3 == null || !var3.hasDashPattern() || VERSION.SDK_INT >= 28) {
                  var3 = this.composition;
                  if (var3 == null || var3.getMaskAndMatteCount() <= 4) {
                     var4 = true;
                  }
               }

               byte var5;
               if (var4) {
                  var5 = var2;
               } else {
                  var5 = 1;
               }

               this.setLayerType(var5, (Paint)null);
            }
         } else {
            this.setLayerType(1, (Paint)null);
         }
      } else {
         this.setLayerType(2, (Paint)null);
      }

   }

   private void init(AttributeSet var1) {
      TypedArray var9 = this.getContext().obtainStyledAttributes(var1, R$styleable.LottieAnimationView);
      boolean var2 = this.isInEditMode();
      boolean var3 = false;
      if (!var2) {
         var2 = var9.hasValue(R$styleable.LottieAnimationView_lottie_rawRes);
         boolean var4 = var9.hasValue(R$styleable.LottieAnimationView_lottie_fileName);
         boolean var5 = var9.hasValue(R$styleable.LottieAnimationView_lottie_url);
         if (var2 && var4) {
            throw new IllegalArgumentException("lottie_rawRes and lottie_fileName cannot be used at the same time. Please use only one at once.");
         }

         if (var2) {
            int var6 = var9.getResourceId(R$styleable.LottieAnimationView_lottie_rawRes, 0);
            if (var6 != 0) {
               this.setAnimation(var6);
            }
         } else {
            String var7;
            if (var4) {
               var7 = var9.getString(R$styleable.LottieAnimationView_lottie_fileName);
               if (var7 != null) {
                  this.setAnimation(var7);
               }
            } else if (var5) {
               var7 = var9.getString(R$styleable.LottieAnimationView_lottie_url);
               if (var7 != null) {
                  this.setAnimationFromUrl(var7);
               }
            }
         }
      }

      if (var9.getBoolean(R$styleable.LottieAnimationView_lottie_autoPlay, false)) {
         this.wasAnimatingWhenDetached = true;
         this.autoPlay = true;
      }

      if (var9.getBoolean(R$styleable.LottieAnimationView_lottie_loop, false)) {
         this.lottieDrawable.setRepeatCount(-1);
      }

      if (var9.hasValue(R$styleable.LottieAnimationView_lottie_repeatMode)) {
         this.setRepeatMode(var9.getInt(R$styleable.LottieAnimationView_lottie_repeatMode, 1));
      }

      if (var9.hasValue(R$styleable.LottieAnimationView_lottie_repeatCount)) {
         this.setRepeatCount(var9.getInt(R$styleable.LottieAnimationView_lottie_repeatCount, -1));
      }

      if (var9.hasValue(R$styleable.LottieAnimationView_lottie_speed)) {
         this.setSpeed(var9.getFloat(R$styleable.LottieAnimationView_lottie_speed, 1.0F));
      }

      this.setImageAssetsFolder(var9.getString(R$styleable.LottieAnimationView_lottie_imageAssetsFolder));
      this.setProgress(var9.getFloat(R$styleable.LottieAnimationView_lottie_progress, 0.0F));
      this.enableMergePathsForKitKatAndAbove(var9.getBoolean(R$styleable.LottieAnimationView_lottie_enableMergePathsForKitKatAndAbove, false));
      if (var9.hasValue(R$styleable.LottieAnimationView_lottie_colorFilter)) {
         SimpleColorFilter var8 = new SimpleColorFilter(var9.getColor(R$styleable.LottieAnimationView_lottie_colorFilter, 0));
         KeyPath var11 = new KeyPath(new String[]{"**"});
         LottieValueCallback var12 = new LottieValueCallback(var8);
         this.addValueCallback(var11, LottieProperty.COLOR_FILTER, var12);
      }

      if (var9.hasValue(R$styleable.LottieAnimationView_lottie_scale)) {
         this.lottieDrawable.setScale(var9.getFloat(R$styleable.LottieAnimationView_lottie_scale, 1.0F));
      }

      var9.recycle();
      LottieDrawable var10 = this.lottieDrawable;
      if (Utils.getAnimationScale(this.getContext()) != 0.0F) {
         var3 = true;
      }

      var10.setSystemAnimationsAreEnabled(var3);
      this.enableOrDisableHardwareLayer();
   }

   private void setCompositionTask(LottieTask var1) {
      this.clearComposition();
      this.cancelLoaderTask();
      var1.addListener(this.loadedListener);
      var1.addFailureListener(this.failureListener);
      this.compositionTask = var1;
   }

   public void addValueCallback(KeyPath var1, Object var2, LottieValueCallback var3) {
      this.lottieDrawable.addValueCallback(var1, var2, var3);
   }

   public void buildDrawingCache(boolean var1) {
      super.buildDrawingCache(var1);
      if (this.getLayerType() == 1 && this.getDrawingCache(var1) == null) {
         this.setRenderMode(RenderMode.HARDWARE);
      }

   }

   public void cancelAnimation() {
      this.wasAnimatingWhenNotShown = false;
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
      LottieComposition var1 = this.composition;
      long var2;
      if (var1 != null) {
         var2 = (long)var1.getDuration();
      } else {
         var2 = 0L;
      }

      return var2;
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

   public void invalidateDrawable(Drawable var1) {
      Drawable var2 = this.getDrawable();
      LottieDrawable var3 = this.lottieDrawable;
      if (var2 == var3) {
         super.invalidateDrawable(var3);
      } else {
         super.invalidateDrawable(var1);
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

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof LottieAnimationView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         LottieAnimationView.SavedState var3 = (LottieAnimationView.SavedState)var1;
         super.onRestoreInstanceState(var3.getSuperState());
         this.animationName = var3.animationName;
         if (!TextUtils.isEmpty(this.animationName)) {
            this.setAnimation(this.animationName);
         }

         this.animationResId = var3.animationResId;
         int var2 = this.animationResId;
         if (var2 != 0) {
            this.setAnimation(var2);
         }

         this.setProgress(var3.progress);
         if (var3.isAnimating) {
            this.playAnimation();
         }

         this.lottieDrawable.setImagesAssetsFolder(var3.imageAssetsFolder);
         this.setRepeatMode(var3.repeatMode);
         this.setRepeatCount(var3.repeatCount);
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

   protected void onVisibilityChanged(View var1, int var2) {
      if (this.lottieDrawable != null) {
         if (this.isShown()) {
            if (this.wasAnimatingWhenNotShown) {
               this.resumeAnimation();
               this.wasAnimatingWhenNotShown = false;
            }
         } else if (this.isAnimating()) {
            this.pauseAnimation();
            this.wasAnimatingWhenNotShown = true;
         }

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
      } else {
         this.wasAnimatingWhenNotShown = true;
      }

   }

   public void resumeAnimation() {
      if (this.isShown()) {
         this.lottieDrawable.resumeAnimation();
         this.enableOrDisableHardwareLayer();
      } else {
         this.wasAnimatingWhenNotShown = true;
      }

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
      this.cancelLoaderTask();
      super.setImageBitmap(var1);
   }

   public void setImageDrawable(Drawable var1) {
      this.cancelLoaderTask();
      super.setImageDrawable(var1);
   }

   public void setImageResource(int var1) {
      this.cancelLoaderTask();
      super.setImageResource(var1);
   }

   public void setMaxFrame(int var1) {
      this.lottieDrawable.setMaxFrame(var1);
   }

   public void setMaxFrame(String var1) {
      this.lottieDrawable.setMaxFrame(var1);
   }

   public void setMaxProgress(float var1) {
      this.lottieDrawable.setMaxProgress(var1);
   }

   public void setMinAndMaxFrame(String var1) {
      this.lottieDrawable.setMinAndMaxFrame(var1);
   }

   public void setMinFrame(int var1) {
      this.lottieDrawable.setMinFrame(var1);
   }

   public void setMinFrame(String var1) {
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

   public void setRenderMode(RenderMode var1) {
      this.renderMode = var1;
      this.enableOrDisableHardwareLayer();
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
         this.setImageDrawable((Drawable)null);
         this.setImageDrawable(this.lottieDrawable);
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
