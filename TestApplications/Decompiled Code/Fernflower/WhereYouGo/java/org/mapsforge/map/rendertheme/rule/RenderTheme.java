package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.util.LRUCache;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;

public class RenderTheme {
   private static final int MATCHING_CACHE_SIZE = 512;
   private final float baseStrokeWidth;
   private final float baseTextSize;
   private int levels;
   private final int mapBackground;
   private final LRUCache matchingCache;
   private final ArrayList rulesList;

   RenderTheme(RenderThemeBuilder var1) {
      this.baseStrokeWidth = var1.baseStrokeWidth;
      this.baseTextSize = var1.baseTextSize;
      this.mapBackground = var1.mapBackground;
      this.rulesList = new ArrayList();
      this.matchingCache = new LRUCache(512);
   }

   private void matchWay(RenderCallback var1, List var2, byte var3, Closed var4) {
      MatchingCacheKey var5 = new MatchingCacheKey(var2, var3, var4);
      List var6 = (List)this.matchingCache.get(var5);
      int var7;
      int var8;
      if (var6 != null) {
         var7 = 0;

         for(var8 = var6.size(); var7 < var8; ++var7) {
            ((RenderInstruction)var6.get(var7)).renderWay(var1, var2);
         }
      } else {
         ArrayList var9 = new ArrayList();
         var7 = 0;

         for(var8 = this.rulesList.size(); var7 < var8; ++var7) {
            ((Rule)this.rulesList.get(var7)).matchWay(var1, var2, var3, var4, var9);
         }

         this.matchingCache.put(var5, var9);
      }

   }

   void addRule(Rule var1) {
      this.rulesList.add(var1);
   }

   void complete() {
      this.rulesList.trimToSize();
      int var1 = 0;

      for(int var2 = this.rulesList.size(); var1 < var2; ++var1) {
         ((Rule)this.rulesList.get(var1)).onComplete();
      }

   }

   public void destroy() {
      this.matchingCache.clear();
      int var1 = 0;

      for(int var2 = this.rulesList.size(); var1 < var2; ++var1) {
         ((Rule)this.rulesList.get(var1)).onDestroy();
      }

   }

   public int getLevels() {
      return this.levels;
   }

   public int getMapBackground() {
      return this.mapBackground;
   }

   public void matchClosedWay(RenderCallback var1, List var2, byte var3) {
      this.matchWay(var1, var2, var3, Closed.YES);
   }

   public void matchLinearWay(RenderCallback var1, List var2, byte var3) {
      this.matchWay(var1, var2, var3, Closed.NO);
   }

   public void matchNode(RenderCallback var1, List var2, byte var3) {
      int var4 = 0;

      for(int var5 = this.rulesList.size(); var4 < var5; ++var4) {
         ((Rule)this.rulesList.get(var4)).matchNode(var1, var2, var3);
      }

   }

   public void scaleStrokeWidth(float var1) {
      int var2 = 0;

      for(int var3 = this.rulesList.size(); var2 < var3; ++var2) {
         ((Rule)this.rulesList.get(var2)).scaleStrokeWidth(this.baseStrokeWidth * var1);
      }

   }

   public void scaleTextSize(float var1) {
      int var2 = 0;

      for(int var3 = this.rulesList.size(); var2 < var3; ++var2) {
         ((Rule)this.rulesList.get(var2)).scaleTextSize(this.baseTextSize * var1);
      }

   }

   void setLevels(int var1) {
      this.levels = var1;
   }
}
