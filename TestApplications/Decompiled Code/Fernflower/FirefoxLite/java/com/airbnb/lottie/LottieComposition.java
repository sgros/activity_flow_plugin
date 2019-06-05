package com.airbnb.lottie;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import com.airbnb.lottie.model.layer.Layer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LottieComposition {
   private Rect bounds;
   private SparseArrayCompat characters;
   private float endFrame;
   private Map fonts;
   private float frameRate;
   private Map images;
   private LongSparseArray layerMap;
   private List layers;
   private final PerformanceTracker performanceTracker = new PerformanceTracker();
   private Map precomps;
   private float startFrame;
   private final HashSet warnings = new HashSet();

   public void addWarning(String var1) {
      Log.w("LOTTIE", var1);
      this.warnings.add(var1);
   }

   public Rect getBounds() {
      return this.bounds;
   }

   public SparseArrayCompat getCharacters() {
      return this.characters;
   }

   public float getDuration() {
      return (float)((long)(this.getDurationFrames() / this.frameRate * 1000.0F));
   }

   public float getDurationFrames() {
      return this.endFrame - this.startFrame;
   }

   public float getEndFrame() {
      return this.endFrame;
   }

   public Map getFonts() {
      return this.fonts;
   }

   public float getFrameRate() {
      return this.frameRate;
   }

   public Map getImages() {
      return this.images;
   }

   public List getLayers() {
      return this.layers;
   }

   public PerformanceTracker getPerformanceTracker() {
      return this.performanceTracker;
   }

   public List getPrecomps(String var1) {
      return (List)this.precomps.get(var1);
   }

   public float getStartFrame() {
      return this.startFrame;
   }

   public void init(Rect var1, float var2, float var3, float var4, List var5, LongSparseArray var6, Map var7, Map var8, SparseArrayCompat var9, Map var10) {
      this.bounds = var1;
      this.startFrame = var2;
      this.endFrame = var3;
      this.frameRate = var4;
      this.layers = var5;
      this.layerMap = var6;
      this.precomps = var7;
      this.images = var8;
      this.characters = var9;
      this.fonts = var10;
   }

   public Layer layerModelForId(long var1) {
      return (Layer)this.layerMap.get(var1);
   }

   public void setPerformanceTrackingEnabled(boolean var1) {
      this.performanceTracker.setEnabled(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("LottieComposition:\n");
      Iterator var2 = this.layers.iterator();

      while(var2.hasNext()) {
         var1.append(((Layer)var2.next()).toString("\t"));
      }

      return var1.toString();
   }

   @Deprecated
   public static class Factory {
      @Deprecated
      public static Cancellable fromAssetFileName(Context var0, String var1, OnCompositionLoadedListener var2) {
         LottieComposition.Factory.ListenerAdapter var3 = new LottieComposition.Factory.ListenerAdapter(var2);
         LottieCompositionFactory.fromAsset(var0, var1).addListener(var3);
         return var3;
      }

      private static final class ListenerAdapter implements Cancellable, LottieListener {
         private boolean cancelled;
         private final OnCompositionLoadedListener listener;

         private ListenerAdapter(OnCompositionLoadedListener var1) {
            this.cancelled = false;
            this.listener = var1;
         }

         // $FF: synthetic method
         ListenerAdapter(OnCompositionLoadedListener var1, Object var2) {
            this(var1);
         }

         public void onResult(LottieComposition var1) {
            if (!this.cancelled) {
               this.listener.onCompositionLoaded(var1);
            }
         }
      }
   }
}
