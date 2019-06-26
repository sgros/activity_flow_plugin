package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.MergePathsContent;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.Logger;

public class MergePaths implements ContentModel {
   private final boolean hidden;
   private final MergePaths.MergePathsMode mode;
   private final String name;

   public MergePaths(String var1, MergePaths.MergePathsMode var2, boolean var3) {
      this.name = var1;
      this.mode = var2;
      this.hidden = var3;
   }

   public MergePaths.MergePathsMode getMode() {
      return this.mode;
   }

   public String getName() {
      return this.name;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      if (!var1.enableMergePathsForKitKatAndAbove()) {
         Logger.warning("Animation contains merge paths but they are disabled.");
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
      ADD,
      EXCLUDE_INTERSECTIONS,
      INTERSECT,
      MERGE,
      SUBTRACT;

      public static MergePaths.MergePathsMode forId(int var0) {
         if (var0 != 1) {
            if (var0 != 2) {
               if (var0 != 3) {
                  if (var0 != 4) {
                     return var0 != 5 ? MERGE : EXCLUDE_INTERSECTIONS;
                  } else {
                     return INTERSECT;
                  }
               } else {
                  return SUBTRACT;
               }
            } else {
               return ADD;
            }
         } else {
            return MERGE;
         }
      }
   }
}
