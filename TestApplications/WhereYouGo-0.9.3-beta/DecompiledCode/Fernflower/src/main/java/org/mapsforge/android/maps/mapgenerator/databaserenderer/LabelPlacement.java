package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Rect;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import org.mapsforge.core.model.Tile;

class LabelPlacement {
   private static final int LABEL_DISTANCE_TO_LABEL = 2;
   private static final int LABEL_DISTANCE_TO_SYMBOL = 2;
   private static final int PLACEMENT_MODEL = 1;
   private static final int START_DISTANCE_TO_SYMBOLS = 4;
   private static final int SYMBOL_DISTANCE_TO_SYMBOL = 2;
   final DependencyCache dependencyCache = new DependencyCache();
   PointTextContainer label;
   Rect rect1;
   Rect rect2;
   LabelPlacement.ReferencePosition referencePosition;
   SymbolContainer symbolContainer;

   private void centerLabels(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.label = (PointTextContainer)var1.get(var2);
         this.label.x -= (double)(this.label.boundary.width() / 2);
      }

   }

   private void preprocessAreaLabels(List var1) {
      this.centerLabels(var1);
      this.removeOutOfTileAreaLabels(var1);
      this.removeOverlappingAreaLabels(var1);
      if (!var1.isEmpty()) {
         this.dependencyCache.removeAreaLabelsInAlreadyDrawnAreas(var1);
      }

   }

   private void preprocessLabels(List var1) {
      this.removeOutOfTileLabels(var1);
   }

   private void preprocessSymbols(List var1) {
      this.removeOutOfTileSymbols(var1);
      this.removeOverlappingSymbols(var1);
      this.dependencyCache.removeSymbolsFromDrawnAreas(var1);
   }

   private List processFourPointGreedy(List var1, List var2, List var3) {
      ArrayList var4 = new ArrayList();
      LabelPlacement.ReferencePosition[] var5 = new LabelPlacement.ReferencePosition[var1.size() * 4];
      PriorityQueue var6 = new PriorityQueue(var1.size() * 4 * 2 + var1.size() / 10 * 2, LabelPlacement.ReferencePositionYComparator.INSTANCE);
      PriorityQueue var7 = new PriorityQueue(var1.size() * 4 * 2 + var1.size() / 10 * 2, LabelPlacement.ReferencePositionHeightComparator.INSTANCE);

      int var8;
      for(var8 = 0; var8 < var1.size(); ++var8) {
         if (var1.get(var8) != null) {
            if (((PointTextContainer)var1.get(var8)).symbol != null) {
               PointTextContainer var9 = (PointTextContainer)var1.get(var8);
               var5[var8 * 4] = new LabelPlacement.ReferencePosition(var9.x - (double)(var9.boundary.width() / 2), var9.y - (double)(var9.symbol.symbol.getHeight() / 2) - (double)4, var8, (float)var9.boundary.width(), (float)var9.boundary.height(), var9.symbol);
               var5[var8 * 4 + 1] = new LabelPlacement.ReferencePosition(var9.x - (double)(var9.boundary.width() / 2), var9.y + (double)(var9.symbol.symbol.getHeight() / 2) + (double)var9.boundary.height() + (double)4, var8, (float)var9.boundary.width(), (float)var9.boundary.height(), var9.symbol);
               var5[var8 * 4 + 2] = new LabelPlacement.ReferencePosition(var9.x - (double)(var9.symbol.symbol.getWidth() / 2) - (double)var9.boundary.width() - (double)4, var9.y + (double)(var9.boundary.height() / 2), var8, (float)var9.boundary.width(), (float)var9.boundary.height(), var9.symbol);
               var5[var8 * 4 + 3] = new LabelPlacement.ReferencePosition(var9.x + (double)(var9.symbol.symbol.getWidth() / 2) + (double)4, var9.y + (double)(var9.boundary.height() / 2) - 0.10000000149011612D, var8, (float)var9.boundary.width(), (float)var9.boundary.height(), var9.symbol);
            } else {
               var5[var8 * 4] = new LabelPlacement.ReferencePosition(((PointTextContainer)var1.get(var8)).x - (double)(((PointTextContainer)var1.get(var8)).boundary.width() / 2), ((PointTextContainer)var1.get(var8)).y, var8, (float)((PointTextContainer)var1.get(var8)).boundary.width(), (float)((PointTextContainer)var1.get(var8)).boundary.height(), (SymbolContainer)null);
               var5[var8 * 4 + 1] = null;
               var5[var8 * 4 + 2] = null;
               var5[var8 * 4 + 3] = null;
            }
         }
      }

      this.removeNonValidateReferencePosition(var5, var2, var3);

      for(var8 = 0; var8 < var5.length; ++var8) {
         this.referencePosition = var5[var8];
         if (this.referencePosition != null) {
            var6.add(this.referencePosition);
            var7.add(this.referencePosition);
         }
      }

      while(var6.size() != 0) {
         this.referencePosition = (LabelPlacement.ReferencePosition)var6.remove();
         this.label = (PointTextContainer)var1.get(this.referencePosition.nodeNumber);
         var4.add(new PointTextContainer(this.label.text, this.referencePosition.x, this.referencePosition.y, this.label.paintFront, this.label.paintBack, this.label.symbol));
         if (var6.size() == 0) {
            break;
         }

         var6.remove(var5[this.referencePosition.nodeNumber * 4 + 0]);
         var6.remove(var5[this.referencePosition.nodeNumber * 4 + 1]);
         var6.remove(var5[this.referencePosition.nodeNumber * 4 + 2]);
         var6.remove(var5[this.referencePosition.nodeNumber * 4 + 3]);
         var7.remove(var5[this.referencePosition.nodeNumber * 4 + 0]);
         var7.remove(var5[this.referencePosition.nodeNumber * 4 + 1]);
         var7.remove(var5[this.referencePosition.nodeNumber * 4 + 2]);
         var7.remove(var5[this.referencePosition.nodeNumber * 4 + 3]);
         LinkedList var11 = new LinkedList();

         while(var7.size() != 0) {
            double var10001 = this.referencePosition.x + (double)this.referencePosition.width;
            if (((LabelPlacement.ReferencePosition)var7.peek()).x >= var10001) {
               break;
            }

            var11.add(var7.remove());
         }

         int var10;
         for(var8 = 0; var8 < var11.size(); var8 = var10 + 1) {
            var10 = var8;
            if (((LabelPlacement.ReferencePosition)var11.get(var8)).x <= this.referencePosition.x + (double)this.referencePosition.width) {
               var10 = var8;
               if (((LabelPlacement.ReferencePosition)var11.get(var8)).y >= this.referencePosition.y - (double)((LabelPlacement.ReferencePosition)var11.get(var8)).height) {
                  var10 = var8;
                  if (((LabelPlacement.ReferencePosition)var11.get(var8)).y <= this.referencePosition.y + (double)((LabelPlacement.ReferencePosition)var11.get(var8)).height) {
                     var6.remove(var11.get(var8));
                     var11.remove(var8);
                     var10 = var8 - 1;
                  }
               }
            }
         }

         var7.addAll(var11);
      }

      return var4;
   }

   private List processTwoPointGreedy(List var1, List var2, List var3) {
      ArrayList var4 = new ArrayList();
      LabelPlacement.ReferencePosition[] var5 = new LabelPlacement.ReferencePosition[var1.size() * 2];
      PriorityQueue var6 = new PriorityQueue(var1.size() * 2 + var1.size() / 10 * 2, LabelPlacement.ReferencePositionWidthComparator.INSTANCE);
      PriorityQueue var7 = new PriorityQueue(var1.size() * 2 + var1.size() / 10 * 2, LabelPlacement.ReferencePositionXComparator.INSTANCE);

      int var8;
      for(var8 = 0; var8 < var1.size(); ++var8) {
         this.label = (PointTextContainer)var1.get(var8);
         if (this.label.symbol != null) {
            var5[var8 * 2] = new LabelPlacement.ReferencePosition(this.label.x - (double)(this.label.boundary.width() / 2) - 0.10000000149011612D, this.label.y - (double)this.label.boundary.height() - 4.0D, var8, (float)this.label.boundary.width(), (float)this.label.boundary.height(), this.label.symbol);
            var5[var8 * 2 + 1] = new LabelPlacement.ReferencePosition(this.label.x - (double)(this.label.boundary.width() / 2), this.label.y + (double)this.label.symbol.symbol.getHeight() + 4.0D, var8, (float)this.label.boundary.width(), (float)this.label.boundary.height(), this.label.symbol);
         } else {
            var5[var8 * 2] = new LabelPlacement.ReferencePosition(this.label.x - (double)(this.label.boundary.width() / 2) - 0.10000000149011612D, this.label.y, var8, (float)this.label.boundary.width(), (float)this.label.boundary.height(), (SymbolContainer)null);
            var5[var8 * 2 + 1] = null;
         }
      }

      this.removeNonValidateReferencePosition(var5, var2, var3);

      for(var8 = 0; var8 < var5.length; ++var8) {
         this.referencePosition = var5[var8];
         if (this.referencePosition != null) {
            var7.add(this.referencePosition);
            var6.add(this.referencePosition);
         }
      }

      while(var6.size() != 0) {
         this.referencePosition = (LabelPlacement.ReferencePosition)var6.remove();
         this.label = (PointTextContainer)var1.get(this.referencePosition.nodeNumber);
         var4.add(new PointTextContainer(this.label.text, this.referencePosition.x, this.referencePosition.y, this.label.paintFront, this.label.paintBack, this.referencePosition.symbol));
         var6.remove(var5[this.referencePosition.nodeNumber * 2 + 1]);
         if (var6.size() == 0) {
            break;
         }

         var7.remove(this.referencePosition);
         var7.remove(var5[this.referencePosition.nodeNumber * 2 + 1]);
         LinkedList var10 = new LinkedList();

         while(var7.size() != 0) {
            double var10001 = this.referencePosition.x + (double)this.referencePosition.width;
            if (((LabelPlacement.ReferencePosition)var7.peek()).x >= var10001) {
               break;
            }

            var10.add(var7.remove());
         }

         int var9;
         for(var8 = 0; var8 < var10.size(); var8 = var9 + 1) {
            var9 = var8;
            if (((LabelPlacement.ReferencePosition)var10.get(var8)).x <= this.referencePosition.x + (double)this.referencePosition.width) {
               var9 = var8;
               if (((LabelPlacement.ReferencePosition)var10.get(var8)).y >= this.referencePosition.y - (double)((LabelPlacement.ReferencePosition)var10.get(var8)).height) {
                  var9 = var8;
                  if (((LabelPlacement.ReferencePosition)var10.get(var8)).y <= this.referencePosition.y + (double)((LabelPlacement.ReferencePosition)var10.get(var8)).height) {
                     var6.remove(var10.get(var8));
                     var10.remove(var8);
                     var9 = var8 - 1;
                  }
               }
            }
         }

         var7.addAll(var10);
      }

      return var4;
   }

   private void removeEmptySymbolReferences(List var1, List var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         this.label = (PointTextContainer)var1.get(var3);
         if (!var2.contains(this.label.symbol)) {
            this.label.symbol = null;
         }
      }

   }

   private void removeNonValidateReferencePosition(LabelPlacement.ReferencePosition[] var1, List var2, List var3) {
      int var4;
      for(var4 = 0; var4 < var2.size(); ++var4) {
         this.symbolContainer = (SymbolContainer)var2.get(var4);
         this.rect1 = new Rect((int)this.symbolContainer.point.x - 2, (int)this.symbolContainer.point.y - 2, (int)this.symbolContainer.point.x + this.symbolContainer.symbol.getWidth() + 2, (int)this.symbolContainer.point.y + this.symbolContainer.symbol.getHeight() + 2);

         for(int var5 = 0; var5 < var1.length; ++var5) {
            if (var1[var5] != null) {
               this.rect2 = new Rect((int)var1[var5].x, (int)(var1[var5].y - (double)var1[var5].height), (int)(var1[var5].x + (double)var1[var5].width), (int)var1[var5].y);
               if (Rect.intersects(this.rect2, this.rect1)) {
                  var1[var5] = null;
               }
            }
         }
      }

      Iterator var7 = var3.iterator();

      while(var7.hasNext()) {
         PointTextContainer var6 = (PointTextContainer)var7.next();
         this.rect1 = new Rect((int)var6.x - 2, (int)var6.y - var6.boundary.height() - 2, (int)var6.x + var6.boundary.width() + 2, (int)var6.y + 2);

         for(var4 = 0; var4 < var1.length; ++var4) {
            if (var1[var4] != null) {
               this.rect2 = new Rect((int)var1[var4].x, (int)(var1[var4].y - (double)var1[var4].height), (int)(var1[var4].x + (double)var1[var4].width), (int)var1[var4].y);
               if (Rect.intersects(this.rect2, this.rect1)) {
                  var1[var4] = null;
               }
            }
         }
      }

      this.dependencyCache.removeReferencePointsFromDependencyCache(var1);
   }

   private void removeOutOfTileAreaLabels(List var1) {
      int var3;
      for(int var2 = 0; var2 < var1.size(); var2 = var3 + 1) {
         this.label = (PointTextContainer)var1.get(var2);
         if (this.label.x > 256.0D) {
            var1.remove(var2);
            var3 = var2 - 1;
         } else if (this.label.y - (double)this.label.boundary.height() > 256.0D) {
            var1.remove(var2);
            var3 = var2 - 1;
         } else if (this.label.x + (double)this.label.boundary.width() < 0.0D) {
            var1.remove(var2);
            var3 = var2 - 1;
         } else {
            var3 = var2;
            if (this.label.y + (double)this.label.boundary.height() < 0.0D) {
               var1.remove(var2);
               var3 = var2 - 1;
            }
         }
      }

   }

   private void removeOutOfTileLabels(List var1) {
      int var2 = 0;

      while(var2 < var1.size()) {
         this.label = (PointTextContainer)var1.get(var2);
         if (this.label.x - (double)(this.label.boundary.width() / 2) > 256.0D) {
            var1.remove(var2);
            this.label = null;
         } else if (this.label.y - (double)this.label.boundary.height() > 256.0D) {
            var1.remove(var2);
            this.label = null;
         } else if (this.label.x - (double)(this.label.boundary.width() / 2) + (double)this.label.boundary.width() < 0.0D) {
            var1.remove(var2);
            this.label = null;
         } else if (this.label.y < 0.0D) {
            var1.remove(var2);
            this.label = null;
         } else {
            ++var2;
         }
      }

   }

   private void removeOutOfTileSymbols(List var1) {
      int var2 = 0;

      while(var2 < var1.size()) {
         this.symbolContainer = (SymbolContainer)var1.get(var2);
         if (this.symbolContainer.point.x > 256.0D) {
            var1.remove(var2);
         } else if (this.symbolContainer.point.y > 256.0D) {
            var1.remove(var2);
         } else if (this.symbolContainer.point.x + (double)this.symbolContainer.symbol.getWidth() < 0.0D) {
            var1.remove(var2);
         } else if (this.symbolContainer.point.y + (double)this.symbolContainer.symbol.getHeight() < 0.0D) {
            var1.remove(var2);
         } else {
            ++var2;
         }
      }

   }

   private void removeOverlappingAreaLabels(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.label = (PointTextContainer)var1.get(var2);
         this.rect1 = new Rect((int)this.label.x - 2, (int)this.label.y - 2, (int)(this.label.x + (double)this.label.boundary.width()) + 2, (int)(this.label.y + (double)this.label.boundary.height() + (double)2));

         int var4;
         for(int var3 = var2 + 1; var3 < var1.size(); var3 = var4 + 1) {
            var4 = var3;
            if (var3 != var2) {
               this.label = (PointTextContainer)var1.get(var3);
               this.rect2 = new Rect((int)this.label.x, (int)this.label.y, (int)(this.label.x + (double)this.label.boundary.width()), (int)(this.label.y + (double)this.label.boundary.height()));
               var4 = var3;
               if (Rect.intersects(this.rect1, this.rect2)) {
                  var1.remove(var3);
                  var4 = var3 - 1;
               }
            }
         }
      }

   }

   private void removeOverlappingSymbolsWithAreaLabels(List var1, List var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         this.label = (PointTextContainer)var2.get(var3);
         this.rect1 = new Rect((int)this.label.x - 2, (int)(this.label.y - (double)this.label.boundary.height()) - 2, (int)(this.label.x + (double)this.label.boundary.width() + (double)2), (int)(this.label.y + (double)2));

         int var5;
         for(int var4 = 0; var4 < var1.size(); var4 = var5 + 1) {
            this.symbolContainer = (SymbolContainer)var1.get(var4);
            this.rect2 = new Rect((int)this.symbolContainer.point.x, (int)this.symbolContainer.point.y, (int)(this.symbolContainer.point.x + (double)this.symbolContainer.symbol.getWidth()), (int)(this.symbolContainer.point.y + (double)this.symbolContainer.symbol.getHeight()));
            var5 = var4;
            if (Rect.intersects(this.rect1, this.rect2)) {
               var1.remove(var4);
               var5 = var4 - 1;
            }
         }
      }

   }

   List placeLabels(List var1, List var2, List var3, Tile var4) {
      List var5 = var1;
      this.dependencyCache.generateTileAndDependencyOnTile(var4);
      this.preprocessAreaLabels(var3);
      this.preprocessLabels(var1);
      this.preprocessSymbols(var2);
      this.removeEmptySymbolReferences(var1, var2);
      this.removeOverlappingSymbolsWithAreaLabels(var2, var3);
      this.dependencyCache.removeOverlappingObjectsWithDependencyOnTile(var1, var3, var2);
      var1 = var1;
      if (!var5.isEmpty()) {
         switch(1) {
         case 0:
            var1 = this.processTwoPointGreedy(var5, var2, var3);
            break;
         case 1:
            var1 = this.processFourPointGreedy(var5, var2, var3);
            break;
         default:
            var1 = var5;
         }
      }

      this.dependencyCache.fillDependencyOnTile(var1, var2, var3);
      return var1;
   }

   void removeOverlappingSymbols(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.symbolContainer = (SymbolContainer)var1.get(var2);
         this.rect1 = new Rect((int)this.symbolContainer.point.x - 2, (int)this.symbolContainer.point.y - 2, (int)this.symbolContainer.point.x + this.symbolContainer.symbol.getWidth() + 2, (int)this.symbolContainer.point.y + this.symbolContainer.symbol.getHeight() + 2);

         int var4;
         for(int var3 = var2 + 1; var3 < var1.size(); var3 = var4 + 1) {
            var4 = var3;
            if (var3 != var2) {
               this.symbolContainer = (SymbolContainer)var1.get(var3);
               this.rect2 = new Rect((int)this.symbolContainer.point.x, (int)this.symbolContainer.point.y, (int)this.symbolContainer.point.x + this.symbolContainer.symbol.getWidth(), (int)this.symbolContainer.point.y + this.symbolContainer.symbol.getHeight());
               var4 = var3;
               if (Rect.intersects(this.rect2, this.rect1)) {
                  var1.remove(var3);
                  var4 = var3 - 1;
               }
            }
         }
      }

   }

   static class ReferencePosition {
      final float height;
      final int nodeNumber;
      SymbolContainer symbol;
      final float width;
      final double x;
      final double y;

      ReferencePosition(double var1, double var3, int var5, float var6, float var7, SymbolContainer var8) {
         this.x = var1;
         this.y = var3;
         this.nodeNumber = var5;
         this.width = var6;
         this.height = var7;
         this.symbol = var8;
      }
   }

   static final class ReferencePositionHeightComparator implements Comparator, Serializable {
      static final LabelPlacement.ReferencePositionHeightComparator INSTANCE = new LabelPlacement.ReferencePositionHeightComparator();
      private static final long serialVersionUID = 1L;

      private ReferencePositionHeightComparator() {
      }

      public int compare(LabelPlacement.ReferencePosition var1, LabelPlacement.ReferencePosition var2) {
         byte var3;
         if (var1.y - (double)var1.height < var2.y - (double)var2.height) {
            var3 = -1;
         } else if (var1.y - (double)var1.height > var2.y - (double)var2.height) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         return var3;
      }
   }

   static final class ReferencePositionWidthComparator implements Comparator, Serializable {
      static final LabelPlacement.ReferencePositionWidthComparator INSTANCE = new LabelPlacement.ReferencePositionWidthComparator();
      private static final long serialVersionUID = 1L;

      private ReferencePositionWidthComparator() {
      }

      public int compare(LabelPlacement.ReferencePosition var1, LabelPlacement.ReferencePosition var2) {
         byte var3;
         if (var1.x + (double)var1.width < var2.x + (double)var2.width) {
            var3 = -1;
         } else if (var1.x + (double)var1.width > var2.x + (double)var2.width) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         return var3;
      }
   }

   static final class ReferencePositionXComparator implements Comparator, Serializable {
      static final LabelPlacement.ReferencePositionXComparator INSTANCE = new LabelPlacement.ReferencePositionXComparator();
      private static final long serialVersionUID = 1L;

      private ReferencePositionXComparator() {
      }

      public int compare(LabelPlacement.ReferencePosition var1, LabelPlacement.ReferencePosition var2) {
         byte var3;
         if (var1.x < var2.x) {
            var3 = -1;
         } else if (var1.x > var2.x) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         return var3;
      }
   }

   static final class ReferencePositionYComparator implements Comparator, Serializable {
      static final LabelPlacement.ReferencePositionYComparator INSTANCE = new LabelPlacement.ReferencePositionYComparator();
      private static final long serialVersionUID = 1L;

      private ReferencePositionYComparator() {
      }

      public int compare(LabelPlacement.ReferencePosition var1, LabelPlacement.ReferencePosition var2) {
         byte var3;
         if (var1.y < var2.y) {
            var3 = -1;
         } else if (var1.y > var2.y) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         return var3;
      }
   }
}
