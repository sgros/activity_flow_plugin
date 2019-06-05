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
public class MergePathsContent implements GreedyContent, PathContent {
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
      List var7;
      for(var2 = this.pathContents.size() - 1; var2 >= 1; --var2) {
         var3 = (PathContent)this.pathContents.get(var2);
         if (var3 instanceof ContentGroup) {
            ContentGroup var4 = (ContentGroup)var3;
            var7 = var4.getPathList();

            for(int var5 = var7.size() - 1; var5 >= 0; --var5) {
               Path var6 = ((PathContent)var7.get(var5)).getPath();
               var6.transform(var4.getTransformationMatrix());
               this.remainderPath.addPath(var6);
            }
         } else {
            this.remainderPath.addPath(var3.getPath());
         }
      }

      var7 = this.pathContents;
      var2 = 0;
      var3 = (PathContent)var7.get(0);
      if (var3 instanceof ContentGroup) {
         ContentGroup var9 = (ContentGroup)var3;

         for(var7 = var9.getPathList(); var2 < var7.size(); ++var2) {
            Path var8 = ((PathContent)var7.get(var2)).getPath();
            var8.transform(var9.getTransformationMatrix());
            this.firstPath.addPath(var8);
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

   public String getName() {
      return this.name;
   }

   public Path getPath() {
      this.path.reset();
      switch(this.mergePaths.getMode()) {
      case Merge:
         this.addPaths();
         break;
      case Add:
         this.opFirstPathWithRest(Op.UNION);
         break;
      case Subtract:
         this.opFirstPathWithRest(Op.REVERSE_DIFFERENCE);
         break;
      case Intersect:
         this.opFirstPathWithRest(Op.INTERSECT);
         break;
      case ExcludeIntersections:
         this.opFirstPathWithRest(Op.XOR);
      }

      return this.path;
   }

   public void setContents(List var1, List var2) {
      for(int var3 = 0; var3 < this.pathContents.size(); ++var3) {
         ((PathContent)this.pathContents.get(var3)).setContents(var1, var2);
      }

   }
}
