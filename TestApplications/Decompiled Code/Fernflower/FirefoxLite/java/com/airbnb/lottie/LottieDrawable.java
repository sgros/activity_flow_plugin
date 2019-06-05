package com.airbnb.lottie;

import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
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
import com.airbnb.lottie.model.KeyPathElement;
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
   private final Set colorFilterData = new HashSet();
   private LottieComposition composition;
   private CompositionLayer compositionLayer;
   private boolean enableMergePaths;
   FontAssetDelegate fontAssetDelegate;
   private FontAssetManager fontAssetManager;
   private ImageAssetDelegate imageAssetDelegate;
   private ImageAssetManager imageAssetManager;
   private String imageAssetsFolder;
   private final ArrayList lazyCompositionTasks = new ArrayList();
   private final Matrix matrix = new Matrix();
   private boolean performanceTrackingEnabled;
   private float scale = 1.0F;
   TextDelegate textDelegate;

   public LottieDrawable() {
      this.animator.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
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
      Callback var1 = this.getCallback();
      if (var1 == null) {
         return null;
      } else {
         return var1 instanceof View ? ((View)var1).getContext() : null;
      }
   }

   private FontAssetManager getFontAssetManager() {
      if (this.getCallback() == null) {
         return null;
      } else {
         if (this.fontAssetManager == null) {
            this.fontAssetManager = new FontAssetManager(this.getCallback(), this.fontAssetDelegate);
         }

         return this.fontAssetManager;
      }
   }

   private ImageAssetManager getImageAssetManager() {
      if (this.getCallback() == null) {
         return null;
      } else {
         if (this.imageAssetManager != null && !this.imageAssetManager.hasSameContext(this.getContext())) {
            this.imageAssetManager.recycleBitmaps();
            this.imageAssetManager = null;
         }

         if (this.imageAssetManager == null) {
            this.imageAssetManager = new ImageAssetManager(this.getCallback(), this.imageAssetsFolder, this.imageAssetDelegate, this.composition.getImages());
         }

         return this.imageAssetManager;
      }
   }

   private float getMaxScale(Canvas var1) {
      return Math.min((float)var1.getWidth() / (float)this.composition.getBounds().width(), (float)var1.getHeight() / (float)this.composition.getBounds().height());
   }

   private void updateBounds() {
      if (this.composition != null) {
         float var1 = this.getScale();
         this.setBounds(0, 0, (int)((float)this.composition.getBounds().width() * var1), (int)((float)this.composition.getBounds().height() * var1));
      }
   }

   public void addAnimatorListener(AnimatorListener var1) {
      this.animator.addListener(var1);
   }

   public void addValueCallback(final KeyPath var1, final Object var2, final LottieValueCallback var3) {
      if (this.compositionLayer == null) {
         this.lazyCompositionTasks.add(new LottieDrawable.LazyCompositionTask() {
            public void run(LottieComposition var1x) {
               LottieDrawable.this.addValueCallback(var1, var2, var3);
            }
         });
      } else {
         KeyPathElement var4 = var1.getResolvedElement();
         boolean var5 = true;
         if (var4 != null) {
            var1.getResolvedElement().addValueCallback(var2, var3);
         } else {
            List var6 = this.resolveKeyPath(var1);

            for(int var7 = 0; var7 < var6.size(); ++var7) {
               ((KeyPath)var6.get(var7)).getResolvedElement().addValueCallback(var2, var3);
            }

            var5 = true ^ var6.isEmpty();
         }

         if (var5) {
            this.invalidateSelf();
            if (var2 == LottieProperty.TIME_REMAP) {
               this.setProgress(this.getProgress());
            }
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

   public void draw(Canvas var1) {
      L.beginSection("Drawable#draw");
      if (this.compositionLayer != null) {
         float var2 = this.scale;
         float var3 = this.getMaxScale(var1);
         if (var2 > var3) {
            var2 = this.scale / var3;
         } else {
            var3 = var2;
            var2 = 1.0F;
         }

         float var9;
         int var4 = (var9 = var2 - 1.0F) == 0.0F ? 0 : (var9 < 0.0F ? -1 : 1);
         if (var4 > 0) {
            var1.save();
            float var5 = (float)this.composition.getBounds().width() / 2.0F;
            float var6 = (float)this.composition.getBounds().height() / 2.0F;
            float var7 = var5 * var3;
            float var8 = var6 * var3;
            var1.translate(this.getScale() * var5 - var7, this.getScale() * var6 - var8);
            var1.scale(var2, var2, var7, var8);
         }

         this.matrix.reset();
         this.matrix.preScale(var3, var3);
         this.compositionLayer.draw(var1, this.matrix, this.alpha);
         L.endSection("Drawable#draw");
         if (var4 > 0) {
            var1.restore();
         }

      }
   }

   public void enableMergePathsForKitKatAndAbove(boolean var1) {
      if (this.enableMergePaths != var1) {
         if (VERSION.SDK_INT < 19) {
            Log.w(TAG, "Merge paths are not supported pre-Kit Kat.");
         } else {
            this.enableMergePaths = var1;
            if (this.composition != null) {
               this.buildCompositionLayer();
            }

         }
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

   public Bitmap getImageAsset(String var1) {
      ImageAssetManager var2 = this.getImageAssetManager();
      return var2 != null ? var2.bitmapForId(var1) : null;
   }

   public String getImageAssetsFolder() {
      return this.imageAssetsFolder;
   }

   public int getIntrinsicHeight() {
      int var1;
      if (this.composition == null) {
         var1 = -1;
      } else {
         var1 = (int)((float)this.composition.getBounds().height() * this.getScale());
      }

      return var1;
   }

   public int getIntrinsicWidth() {
      int var1;
      if (this.composition == null) {
         var1 = -1;
      } else {
         var1 = (int)((float)this.composition.getBounds().width() * this.getScale());
      }

      return var1;
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
      return this.composition != null ? this.composition.getPerformanceTracker() : null;
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

   public Typeface getTypeface(String var1, String var2) {
      FontAssetManager var3 = this.getFontAssetManager();
      return var3 != null ? var3.getTypeface(var1, var2) : null;
   }

   public void invalidateDrawable(Drawable var1) {
      Callback var2 = this.getCallback();
      if (var2 != null) {
         var2.invalidateDrawable(this);
      }
   }

   public void invalidateSelf() {
      Callback var1 = this.getCallback();
      if (var1 != null) {
         var1.invalidateDrawable(this);
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
         this.lazyCompositionTasks.add(new LottieDrawable.LazyCompositionTask() {
            public void run(LottieComposition var1) {
               LottieDrawable.this.playAnimation();
            }
         });
      } else {
         this.animator.playAnimation();
      }
   }

   public void recycleBitmaps() {
      if (this.imageAssetManager != null) {
         this.imageAssetManager.recycleBitmaps();
      }

   }

   public List resolveKeyPath(KeyPath var1) {
      if (this.compositionLayer == null) {
         Log.w("LOTTIE", "Cannot resolve KeyPath. Composition is not set yet.");
         return Collections.emptyList();
      } else {
         ArrayList var2 = new ArrayList();
         this.compositionLayer.resolveKeyPath(var1, 0, var2, new KeyPath(new String[0]));
         return var2;
      }
   }

   public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
      Callback var5 = this.getCallback();
      if (var5 != null) {
         var5.scheduleDrawable(this, var2, var3);
      }
   }

   public void setAlpha(int var1) {
      this.alpha = var1;
   }

   public void setColorFilter(ColorFilter var1) {
      Log.w("LOTTIE", "Use addColorFilter instead.");
   }

   public boolean setComposition(LottieComposition var1) {
      if (this.composition == var1) {
         return false;
      } else {
         this.clearComposition();
         this.composition = var1;
         this.buildCompositionLayer();
         this.animator.setComposition(var1);
         this.setProgress(this.animator.getAnimatedFraction());
         this.setScale(this.scale);
         this.updateBounds();
         Iterator var2 = (new ArrayList(this.lazyCompositionTasks)).iterator();

         while(var2.hasNext()) {
            ((LottieDrawable.LazyCompositionTask)var2.next()).run(var1);
            var2.remove();
         }

         this.lazyCompositionTasks.clear();
         var1.setPerformanceTrackingEnabled(this.performanceTrackingEnabled);
         return true;
      }
   }

   public void setFontAssetDelegate(FontAssetDelegate var1) {
      this.fontAssetDelegate = var1;
      if (this.fontAssetManager != null) {
         this.fontAssetManager.setDelegate(var1);
      }

   }

   public void setFrame(final int var1) {
      if (this.composition == null) {
         this.lazyCompositionTasks.add(new LottieDrawable.LazyCompositionTask() {
            public void run(LottieComposition var1x) {
               LottieDrawable.this.setFrame(var1);
            }
         });
      } else {
         this.animator.setFrame(var1);
      }
   }

   public void setImageAssetDelegate(ImageAssetDelegate var1) {
      this.imageAssetDelegate = var1;
      if (this.imageAssetManager != null) {
         this.imageAssetManager.setDelegate(var1);
      }

   }

   public void setImagesAssetsFolder(String var1) {
      this.imageAssetsFolder = var1;
   }

   public void setMaxFrame(final int var1) {
      if (this.composition == null) {
         this.lazyCompositionTasks.add(new LottieDrawable.LazyCompositionTask() {
            public void run(LottieComposition var1x) {
               LottieDrawable.this.setMaxFrame(var1);
            }
         });
      } else {
         this.animator.setMaxFrame(var1);
      }
   }

   public void setMaxProgress(final float var1) {
      if (this.composition == null) {
         this.lazyCompositionTasks.add(new LottieDrawable.LazyCompositionTask() {
            public void run(LottieComposition var1x) {
               LottieDrawable.this.setMaxProgress(var1);
            }
         });
      } else {
         this.setMaxFrame((int)MiscUtils.lerp(this.composition.getStartFrame(), this.composition.getEndFrame(), var1));
      }
   }

   public void setMinFrame(final int var1) {
      if (this.composition == null) {
         this.lazyCompositionTasks.add(new LottieDrawable.LazyCompositionTask() {
            public void run(LottieComposition var1x) {
               LottieDrawable.this.setMinFrame(var1);
            }
         });
      } else {
         this.animator.setMinFrame(var1);
      }
   }

   public void setMinProgress(final float var1) {
      if (this.composition == null) {
         this.lazyCompositionTasks.add(new LottieDrawable.LazyCompositionTask() {
            public void run(LottieComposition var1x) {
               LottieDrawable.this.setMinProgress(var1);
            }
         });
      } else {
         this.setMinFrame((int)MiscUtils.lerp(this.composition.getStartFrame(), this.composition.getEndFrame(), var1));
      }
   }

   public void setPerformanceTrackingEnabled(boolean var1) {
      this.performanceTrackingEnabled = var1;
      if (this.composition != null) {
         this.composition.setPerformanceTrackingEnabled(var1);
      }

   }

   public void setProgress(final float var1) {
      if (this.composition == null) {
         this.lazyCompositionTasks.add(new LottieDrawable.LazyCompositionTask() {
            public void run(LottieComposition var1x) {
               LottieDrawable.this.setProgress(var1);
            }
         });
      } else {
         this.setFrame((int)MiscUtils.lerp(this.composition.getStartFrame(), this.composition.getEndFrame(), var1));
      }
   }

   public void setRepeatCount(int var1) {
      this.animator.setRepeatCount(var1);
   }

   public void setRepeatMode(int var1) {
      this.animator.setRepeatMode(var1);
   }

   public void setScale(float var1) {
      this.scale = var1;
      this.updateBounds();
   }

   public void setSpeed(float var1) {
      this.animator.setSpeed(var1);
   }

   public void setTextDelegate(TextDelegate var1) {
      this.textDelegate = var1;
   }

   public void start() {
      this.playAnimation();
   }

   public void stop() {
      this.endAnimation();
   }

   public void unscheduleDrawable(Drawable var1, Runnable var2) {
      Callback var3 = this.getCallback();
      if (var3 != null) {
         var3.unscheduleDrawable(this, var2);
      }
   }

   public boolean useTextGlyphs() {
      boolean var1;
      if (this.textDelegate == null && this.composition.getCharacters().size() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private interface LazyCompositionTask {
      void run(LottieComposition var1);
   }
}
