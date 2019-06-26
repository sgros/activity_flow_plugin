package com.airbnb.lottie;

import android.graphics.Rect;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import com.airbnb.lottie.model.Marker;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.utils.Logger;
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
   private boolean hasDashPattern;
   private Map images;
   private LongSparseArray layerMap;
   private List layers;
   private List markers;
   private int maskAndMatteCount = 0;
   private final PerformanceTracker performanceTracker = new PerformanceTracker();
   private Map precomps;
   private float startFrame;
   private final HashSet warnings = new HashSet();

   public void addWarning(String var1) {
      Logger.warning(var1);
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

   public Marker getMarker(String var1) {
      this.markers.size();

      for(int var2 = 0; var2 < this.markers.size(); ++var2) {
         Marker var3 = (Marker)this.markers.get(var2);
         if (var3.matchesName(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getMaskAndMatteCount() {
      return this.maskAndMatteCount;
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

   public boolean hasDashPattern() {
      return this.hasDashPattern;
   }

   public void incrementMatteOrMaskCount(int var1) {
      this.maskAndMatteCount += var1;
   }

   public void init(Rect var1, float var2, float var3, float var4, List var5, LongSparseArray var6, Map var7, Map var8, SparseArrayCompat var9, Map var10, List var11) {
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
      this.markers = var11;
   }

   public Layer layerModelForId(long var1) {
      return (Layer)this.layerMap.get(var1);
   }

   public void setHasDashPattern(boolean var1) {
      this.hasDashPattern = var1;
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
}
