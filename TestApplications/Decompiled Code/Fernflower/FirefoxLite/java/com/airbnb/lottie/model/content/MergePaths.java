package com.airbnb.lottie.model.content;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.MergePathsContent;
import com.airbnb.lottie.model.layer.BaseLayer;

public class MergePaths implements ContentModel {
   private final MergePaths.MergePathsMode mode;
   private final String name;

   public MergePaths(String var1, MergePaths.MergePathsMode var2) {
      this.name = var1;
      this.mode = var2;
   }

   public MergePaths.MergePathsMode getMode() {
      return this.mode;
   }

   public String getName() {
      return this.name;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      if (!var1.enableMergePathsForKitKatAndAbove()) {
         L.warn("Animation contains merge paths but they are disabled.");
         return null;
      } else {
         return new MergePathsContent(this);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("MergePaths{mode=");
      var1.append(this.mode);
      var1.append('}');
      return var1.toString();
   }

   public static enum MergePathsMode {
      Add,
      ExcludeIntersections,
      Intersect,
      Merge,
      Subtract;

      public static MergePaths.MergePathsMode forId(int var0) {
         switch(var0) {
         case 1:
            return Merge;
         case 2:
            return Add;
         case 3:
            return Subtract;
         case 4:
            return Intersect;
         case 5:
            return ExcludeIntersections;
         default:
            return Merge;
         }
      }
   }
}
