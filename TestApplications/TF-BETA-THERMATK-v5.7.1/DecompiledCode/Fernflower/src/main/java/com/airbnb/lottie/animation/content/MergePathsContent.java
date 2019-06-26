package com.airbnb.lottie.animation.content;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.Path.Op;
import android.os.Build.VERSION;
import com.airbnb.lottie.model.content.MergePaths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@TargetApi(19)
public class MergePathsContent implements PathContent, GreedyContent {
   private final Path firstPath = new Path();
   private final MergePaths mergePaths;
   private final String name;
   private final Path path = new Path();
   private final List pathContents = new ArrayList();
   private final Path remainderPath = new Path();

   public MergePathsContent(MergePaths var1) {
      if (VERSION.SDK_INT >= 19) {
         this.name = var1.getName();
         this.mergePaths = var1;
      } else {
         throw new IllegalStateException("Merge paths are not supported pre-KitKat.");
      }
   }

   private void addPaths() {
      for(int var1 = 0; var1 < this.pathContents.size(); ++var1) {
         this.path.addPath(((PathContent)this.pathContents.get(var1)).getPath());
      }

   }

   @TargetApi(19)
   private void opFirstPathWithRest(Op var1) {
      this.remainderPath.reset();
      this.firstPath.reset();

      int var2;
      PathContent var3;
      for(var2 = this.pathContents.size() - 1; var2 >= 1; --var2) {
         var3 = (PathContent)this.pathContents.get(var2);
         if (var3 instanceof ContentGroup) {
            ContentGroup var7 = (ContentGroup)var3;
            List var4 = var7.getPathList();

            for(int var5 = var4.size() - 1; var5 >= 0; --var5) {
               Path var6 = ((PathContent)var4.get(var5)).getPath();
               var6.transform(var7.getTransformationMatrix());
               this.remainderPath.addPath(var6);
            }
         } else {
            this.remainderPath.addPath(var3.getPath());
         }
      }

      List var8 = this.pathContents;
      var2 = 0;
      var3 = (PathContent)var8.get(0);
      if (var3 instanceof ContentGroup) {
         ContentGroup var9 = (ContentGroup)var3;

         for(List var11 = var9.getPathList(); var2 < var11.size(); ++var2) {
            Path var10 = ((PathContent)var11.get(var2)).getPath();
            var10.transform(var9.getTransformationMatrix());
            this.firstPath.addPath(var10);
         }
      } else {
         this.firstPath.set(var3.getPath());
      }

      this.path.op(this.firstPath, this.remainderPath, var1);
   }

   public void absorbContent(ListIterator var1) {
      while(var1.hasPrevious() && var1.previous() != this) {
      }

      while(var1.hasPrevious()) {
         Content var2 = (Content)var1.previous();
         if (var2 instanceof PathContent) {
            this.pathContents.add((PathContent)var2);
            var1.remove();
         }
      }

   }

   public Path getPath() {
      this.path.reset();
      if (this.mergePaths.isHidden()) {
         return this.path;
      } else {
         int var1 = null.$SwitchMap$com$airbnb$lottie$model$content$MergePaths$MergePathsMode[this.mergePaths.getMode().ordinal()];
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 3) {
                  if (var1 != 4) {
                     if (var1 == 5) {
                        this.opFirstPathWithRest(Op.XOR);
                     }
                  } else {
                     this.opFirstPathWithRest(Op.INTERSECT);
                  }
               } else {
                  this.opFirstPathWithRest(Op.REVERSE_DIFFERENCE);
               }
            } else {
               this.opFirstPathWithRest(Op.UNION);
            }
         } else {
            this.addPaths();
         }

         return this.path;
      }
   }

   public void setContents(List var1, List var2) {
      for(int var3 = 0; var3 < this.pathContents.size(); ++var3) {
         ((PathContent)this.pathContents.get(var3)).setContents(var1, var2);
      }

   }
}
