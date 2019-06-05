package com.airbnb.lottie.model.layer;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Layer {
   private final LottieComposition composition;
   private final List inOutKeyframes;
   private final long layerId;
   private final String layerName;
   private final Layer.LayerType layerType;
   private final List masks;
   private final Layer.MatteType matteType;
   private final long parentId;
   private final int preCompHeight;
   private final int preCompWidth;
   private final String refId;
   private final List shapes;
   private final int solidColor;
   private final int solidHeight;
   private final int solidWidth;
   private final float startFrame;
   private final AnimatableTextFrame text;
   private final AnimatableTextProperties textProperties;
   private final AnimatableFloatValue timeRemapping;
   private final float timeStretch;
   private final AnimatableTransform transform;

   public Layer(List var1, LottieComposition var2, String var3, long var4, Layer.LayerType var6, long var7, String var9, List var10, AnimatableTransform var11, int var12, int var13, int var14, float var15, float var16, int var17, int var18, AnimatableTextFrame var19, AnimatableTextProperties var20, List var21, Layer.MatteType var22, AnimatableFloatValue var23) {
      this.shapes = var1;
      this.composition = var2;
      this.layerName = var3;
      this.layerId = var4;
      this.layerType = var6;
      this.parentId = var7;
      this.refId = var9;
      this.masks = var10;
      this.transform = var11;
      this.solidWidth = var12;
      this.solidHeight = var13;
      this.solidColor = var14;
      this.timeStretch = var15;
      this.startFrame = var16;
      this.preCompWidth = var17;
      this.preCompHeight = var18;
      this.text = var19;
      this.textProperties = var20;
      this.inOutKeyframes = var21;
      this.matteType = var22;
      this.timeRemapping = var23;
   }

   LottieComposition getComposition() {
      return this.composition;
   }

   public long getId() {
      return this.layerId;
   }

   List getInOutKeyframes() {
      return this.inOutKeyframes;
   }

   public Layer.LayerType getLayerType() {
      return this.layerType;
   }

   List getMasks() {
      return this.masks;
   }

   Layer.MatteType getMatteType() {
      return this.matteType;
   }

   String getName() {
      return this.layerName;
   }

   long getParentId() {
      return this.parentId;
   }

   int getPreCompHeight() {
      return this.preCompHeight;
   }

   int getPreCompWidth() {
      return this.preCompWidth;
   }

   String getRefId() {
      return this.refId;
   }

   List getShapes() {
      return this.shapes;
   }

   int getSolidColor() {
      return this.solidColor;
   }

   int getSolidHeight() {
      return this.solidHeight;
   }

   int getSolidWidth() {
      return this.solidWidth;
   }

   float getStartProgress() {
      return this.startFrame / this.composition.getDurationFrames();
   }

   AnimatableTextFrame getText() {
      return this.text;
   }

   AnimatableTextProperties getTextProperties() {
      return this.textProperties;
   }

   AnimatableFloatValue getTimeRemapping() {
      return this.timeRemapping;
   }

   float getTimeStretch() {
      return this.timeStretch;
   }

   AnimatableTransform getTransform() {
      return this.transform;
   }

   public String toString() {
      return this.toString("");
   }

   public String toString(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var1);
      var2.append(this.getName());
      var2.append("\n");
      Layer var3 = this.composition.layerModelForId(this.getParentId());
      if (var3 != null) {
         var2.append("\t\tParents: ");
         var2.append(var3.getName());

         for(var3 = this.composition.layerModelForId(var3.getParentId()); var3 != null; var3 = this.composition.layerModelForId(var3.getParentId())) {
            var2.append("->");
            var2.append(var3.getName());
         }

         var2.append(var1);
         var2.append("\n");
      }

      if (!this.getMasks().isEmpty()) {
         var2.append(var1);
         var2.append("\tMasks: ");
         var2.append(this.getMasks().size());
         var2.append("\n");
      }

      if (this.getSolidWidth() != 0 && this.getSolidHeight() != 0) {
         var2.append(var1);
         var2.append("\tBackground: ");
         var2.append(String.format(Locale.US, "%dx%d %X\n", this.getSolidWidth(), this.getSolidHeight(), this.getSolidColor()));
      }

      if (!this.shapes.isEmpty()) {
         var2.append(var1);
         var2.append("\tShapes:\n");
         Iterator var5 = this.shapes.iterator();

         while(var5.hasNext()) {
            Object var4 = var5.next();
            var2.append(var1);
            var2.append("\t\t");
            var2.append(var4);
            var2.append("\n");
         }
      }

      return var2.toString();
   }

   public static enum LayerType {
      Image,
      Null,
      PreComp,
      Shape,
      Solid,
      Text,
      Unknown;
   }

   public static enum MatteType {
      Add,
      Invert,
      None,
      Unknown;
   }
}
