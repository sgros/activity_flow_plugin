package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;

abstract class Rule {
   static final Map MATCHERS_CACHE_KEY = new HashMap();
   static final Map MATCHERS_CACHE_VALUE = new HashMap();
   final ClosedMatcher closedMatcher;
   final ElementMatcher elementMatcher;
   private final ArrayList renderInstructions;
   private final ArrayList subRules;
   final byte zoomMax;
   final byte zoomMin;

   Rule(RuleBuilder var1) {
      this.closedMatcher = var1.closedMatcher;
      this.elementMatcher = var1.elementMatcher;
      this.zoomMax = (byte)var1.zoomMax;
      this.zoomMin = (byte)var1.zoomMin;
      this.renderInstructions = new ArrayList(4);
      this.subRules = new ArrayList(4);
   }

   void addRenderingInstruction(RenderInstruction var1) {
      this.renderInstructions.add(var1);
   }

   void addSubRule(Rule var1) {
      this.subRules.add(var1);
   }

   void matchNode(RenderCallback var1, List var2, byte var3) {
      if (this.matchesNode(var2, var3)) {
         int var4 = 0;

         int var5;
         for(var5 = this.renderInstructions.size(); var4 < var5; ++var4) {
            ((RenderInstruction)this.renderInstructions.get(var4)).renderNode(var1, var2);
         }

         var4 = 0;

         for(var5 = this.subRules.size(); var4 < var5; ++var4) {
            ((Rule)this.subRules.get(var4)).matchNode(var1, var2, var3);
         }
      }

   }

   void matchWay(RenderCallback var1, List var2, byte var3, Closed var4, List var5) {
      if (this.matchesWay(var2, var3, var4)) {
         int var6 = 0;

         int var7;
         for(var7 = this.renderInstructions.size(); var6 < var7; ++var6) {
            ((RenderInstruction)this.renderInstructions.get(var6)).renderWay(var1, var2);
            var5.add(this.renderInstructions.get(var6));
         }

         var6 = 0;

         for(var7 = this.subRules.size(); var6 < var7; ++var6) {
            ((Rule)this.subRules.get(var6)).matchWay(var1, var2, var3, var4, var5);
         }
      }

   }

   abstract boolean matchesNode(List var1, byte var2);

   abstract boolean matchesWay(List var1, byte var2, Closed var3);

   void onComplete() {
      MATCHERS_CACHE_KEY.clear();
      MATCHERS_CACHE_VALUE.clear();
      this.renderInstructions.trimToSize();
      this.subRules.trimToSize();
      int var1 = 0;

      for(int var2 = this.subRules.size(); var1 < var2; ++var1) {
         ((Rule)this.subRules.get(var1)).onComplete();
      }

   }

   void onDestroy() {
      int var1 = 0;

      int var2;
      for(var2 = this.renderInstructions.size(); var1 < var2; ++var1) {
         ((RenderInstruction)this.renderInstructions.get(var1)).destroy();
      }

      var1 = 0;

      for(var2 = this.subRules.size(); var1 < var2; ++var1) {
         ((Rule)this.subRules.get(var1)).onDestroy();
      }

   }

   void scaleStrokeWidth(float var1) {
      int var2 = 0;

      int var3;
      for(var3 = this.renderInstructions.size(); var2 < var3; ++var2) {
         ((RenderInstruction)this.renderInstructions.get(var2)).scaleStrokeWidth(var1);
      }

      var2 = 0;

      for(var3 = this.subRules.size(); var2 < var3; ++var2) {
         ((Rule)this.subRules.get(var2)).scaleStrokeWidth(var1);
      }

   }

   void scaleTextSize(float var1) {
      int var2 = 0;

      int var3;
      for(var3 = this.renderInstructions.size(); var2 < var3; ++var2) {
         ((RenderInstruction)this.renderInstructions.get(var2)).scaleTextSize(var1);
      }

      var2 = 0;

      for(var3 = this.subRules.size(); var2 < var3; ++var2) {
         ((Rule)this.subRules.get(var2)).scaleTextSize(var1);
      }

   }
}
