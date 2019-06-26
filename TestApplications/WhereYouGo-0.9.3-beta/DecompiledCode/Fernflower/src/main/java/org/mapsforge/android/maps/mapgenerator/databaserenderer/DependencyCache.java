package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

class DependencyCache {
   private DependencyCache.DependencyOnTile currentDependencyOnTile;
   private Tile currentTile;
   DependencyCache.Dependency depLabel;
   final Map dependencyTable = new Hashtable(60);
   Rect rect1;
   Rect rect2;
   SymbolContainer smb;
   DependencyCache.DependencyOnTile tmp;

   private void addLabelsFromDependencyOnTile(List var1) {
      for(int var2 = 0; var2 < this.currentDependencyOnTile.labels.size(); ++var2) {
         this.depLabel = (DependencyCache.Dependency)this.currentDependencyOnTile.labels.get(var2);
         if (((DependencyCache.DependencyText)this.depLabel.value).paintBack != null) {
            var1.add(new PointTextContainer(((DependencyCache.DependencyText)this.depLabel.value).text, this.depLabel.point.x, this.depLabel.point.y, ((DependencyCache.DependencyText)this.depLabel.value).paintFront, ((DependencyCache.DependencyText)this.depLabel.value).paintBack));
         } else {
            var1.add(new PointTextContainer(((DependencyCache.DependencyText)this.depLabel.value).text, this.depLabel.point.x, this.depLabel.point.y, ((DependencyCache.DependencyText)this.depLabel.value).paintFront));
         }
      }

   }

   private void addSymbolsFromDependencyOnTile(List var1) {
      Iterator var2 = this.currentDependencyOnTile.symbols.iterator();

      while(var2.hasNext()) {
         DependencyCache.Dependency var3 = (DependencyCache.Dependency)var2.next();
         var1.add(new SymbolContainer(((DependencyCache.DependencySymbol)var3.value).symbol, var3.point));
      }

   }

   private void fillDependencyLabels(List var1) {
      Tile var2 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var3 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var4 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
      Tile var5 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
      Tile var6 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
      Tile var7 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
      Tile var8 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
      Tile var9 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);

      for(int var10 = 0; var10 < var1.size(); ++var10) {
         PointTextContainer var11 = (PointTextContainer)var1.get(var10);
         DependencyCache.DependencyText var12 = null;
         DependencyCache.DependencyText var13 = var12;
         DependencyCache.DependencyOnTile var16;
         if (var11.y - (double)var11.boundary.height() < 0.0D) {
            var13 = var12;
            if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var4)).drawn) {
               var16 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var4);
               var12 = new DependencyCache.DependencyText(var11.paintFront, var11.paintBack, var11.text, var11.boundary, this.currentTile);
               this.currentDependencyOnTile.addText(new DependencyCache.Dependency(var12, new Point(var11.x, var11.y)));
               var16.addText(new DependencyCache.Dependency(var12, new Point(var11.x, var11.y + 256.0D)));
               var12.addTile(var4);
               if (var11.x < 0.0D && !((DependencyCache.DependencyOnTile)this.dependencyTable.get(var6)).drawn) {
                  ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var6)).addText(new DependencyCache.Dependency(var12, new Point(var11.x + 256.0D, var11.y + 256.0D)));
                  var12.addTile(var6);
               }

               var13 = var12;
               if (var11.x + (double)var11.boundary.width() > 256.0D) {
                  var13 = var12;
                  if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var8)).drawn) {
                     ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var8)).addText(new DependencyCache.Dependency(var12, new Point(var11.x - 256.0D, var11.y + 256.0D)));
                     var12.addTile(var8);
                     var13 = var12;
                  }
               }
            }
         }

         var12 = var13;
         DependencyCache.DependencyText var14;
         DependencyCache.DependencyOnTile var15;
         if (var11.y > 256.0D) {
            var12 = var13;
            if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var5)).drawn) {
               var15 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var5);
               var14 = var13;
               if (var13 == null) {
                  var14 = new DependencyCache.DependencyText(var11.paintFront, var11.paintBack, var11.text, var11.boundary, this.currentTile);
                  this.currentDependencyOnTile.addText(new DependencyCache.Dependency(var14, new Point(var11.x, var11.y)));
               }

               var15.addText(new DependencyCache.Dependency(var14, new Point(var11.x, var11.y - 256.0D)));
               var14.addTile(var5);
               if (var11.x < 0.0D && !((DependencyCache.DependencyOnTile)this.dependencyTable.get(var7)).drawn) {
                  ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var7)).addText(new DependencyCache.Dependency(var14, new Point(var11.x + 256.0D, var11.y - 256.0D)));
                  var14.addTile(var7);
               }

               var12 = var14;
               if (var11.x + (double)var11.boundary.width() > 256.0D) {
                  var12 = var14;
                  if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var9)).drawn) {
                     ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var9)).addText(new DependencyCache.Dependency(var14, new Point(var11.x - 256.0D, var11.y - 256.0D)));
                     var14.addTile(var9);
                     var12 = var14;
                  }
               }
            }
         }

         var13 = var12;
         DependencyCache.DependencyOnTile var17;
         if (var11.x < 0.0D) {
            var13 = var12;
            if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var2)).drawn) {
               var17 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var2);
               var13 = var12;
               if (var12 == null) {
                  var13 = new DependencyCache.DependencyText(var11.paintFront, var11.paintBack, var11.text, var11.boundary, this.currentTile);
                  this.currentDependencyOnTile.addText(new DependencyCache.Dependency(var13, new Point(var11.x, var11.y)));
               }

               var17.addText(new DependencyCache.Dependency(var13, new Point(var11.x + 256.0D, var11.y)));
               var13.addTile(var2);
            }
         }

         var14 = var13;
         if (var11.x + (double)var11.boundary.width() > 256.0D) {
            var14 = var13;
            if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var3)).drawn) {
               var15 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var3);
               var14 = var13;
               if (var13 == null) {
                  var14 = new DependencyCache.DependencyText(var11.paintFront, var11.paintBack, var11.text, var11.boundary, this.currentTile);
                  this.currentDependencyOnTile.addText(new DependencyCache.Dependency(var14, new Point(var11.x, var11.y)));
               }

               var15.addText(new DependencyCache.Dependency(var14, new Point(var11.x - 256.0D, var11.y)));
               var14.addTile(var3);
            }
         }

         if (var11.symbol != null && var14 == null) {
            var12 = var14;
            if (var11.symbol.point.y <= 0.0D) {
               var12 = var14;
               if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var4)).drawn) {
                  var15 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var4);
                  var13 = new DependencyCache.DependencyText(var11.paintFront, var11.paintBack, var11.text, var11.boundary, this.currentTile);
                  this.currentDependencyOnTile.addText(new DependencyCache.Dependency(var13, new Point(var11.x, var11.y)));
                  var15.addText(new DependencyCache.Dependency(var13, new Point(var11.x, var11.y + 256.0D)));
                  var13.addTile(var4);
                  if (var11.symbol.point.x < 0.0D && !((DependencyCache.DependencyOnTile)this.dependencyTable.get(var6)).drawn) {
                     ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var6)).addText(new DependencyCache.Dependency(var13, new Point(var11.x + 256.0D, var11.y + 256.0D)));
                     var13.addTile(var6);
                  }

                  var12 = var13;
                  if (var11.symbol.point.x + (double)var11.symbol.symbol.getWidth() > 256.0D) {
                     var12 = var13;
                     if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var8)).drawn) {
                        ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var8)).addText(new DependencyCache.Dependency(var13, new Point(var11.x - 256.0D, var11.y + 256.0D)));
                        var13.addTile(var8);
                        var12 = var13;
                     }
                  }
               }
            }

            var13 = var12;
            if (var11.symbol.point.y + (double)var11.symbol.symbol.getHeight() >= 256.0D) {
               var13 = var12;
               if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var5)).drawn) {
                  var16 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var5);
                  var14 = var12;
                  if (var12 == null) {
                     var14 = new DependencyCache.DependencyText(var11.paintFront, var11.paintBack, var11.text, var11.boundary, this.currentTile);
                     this.currentDependencyOnTile.addText(new DependencyCache.Dependency(var14, new Point(var11.x, var11.y)));
                  }

                  var16.addText(new DependencyCache.Dependency(var14, new Point(var11.x, var11.y + 256.0D)));
                  var14.addTile(var4);
                  if (var11.symbol.point.x < 0.0D && !((DependencyCache.DependencyOnTile)this.dependencyTable.get(var7)).drawn) {
                     ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var7)).addText(new DependencyCache.Dependency(var14, new Point(var11.x + 256.0D, var11.y - 256.0D)));
                     var14.addTile(var7);
                  }

                  var13 = var14;
                  if (var11.symbol.point.x + (double)var11.symbol.symbol.getWidth() > 256.0D) {
                     var13 = var14;
                     if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var9)).drawn) {
                        ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var9)).addText(new DependencyCache.Dependency(var14, new Point(var11.x - 256.0D, var11.y - 256.0D)));
                        var14.addTile(var9);
                        var13 = var14;
                     }
                  }
               }
            }

            var12 = var13;
            if (var11.symbol.point.x <= 0.0D) {
               var12 = var13;
               if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var2)).drawn) {
                  var17 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var2);
                  var12 = var13;
                  if (var13 == null) {
                     var12 = new DependencyCache.DependencyText(var11.paintFront, var11.paintBack, var11.text, var11.boundary, this.currentTile);
                     this.currentDependencyOnTile.addText(new DependencyCache.Dependency(var12, new Point(var11.x, var11.y)));
                  }

                  var17.addText(new DependencyCache.Dependency(var12, new Point(var11.x - 256.0D, var11.y)));
                  var12.addTile(var2);
               }
            }

            if (var11.symbol.point.x + (double)var11.symbol.symbol.getWidth() >= 256.0D && !((DependencyCache.DependencyOnTile)this.dependencyTable.get(var3)).drawn) {
               var17 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var3);
               var13 = var12;
               if (var12 == null) {
                  var13 = new DependencyCache.DependencyText(var11.paintFront, var11.paintBack, var11.text, var11.boundary, this.currentTile);
                  this.currentDependencyOnTile.addText(new DependencyCache.Dependency(var13, new Point(var11.x, var11.y)));
               }

               var17.addText(new DependencyCache.Dependency(var13, new Point(var11.x + 256.0D, var11.y)));
               var13.addTile(var3);
            }
         }
      }

   }

   private void fillDependencyOnTile2(List var1, List var2, List var3) {
      Tile var4 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var5 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var6 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
      Tile var7 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
      Tile var8 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
      Tile var9 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
      Tile var10 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
      Tile var11 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
      if (this.dependencyTable.get(var6) == null) {
         this.dependencyTable.put(var6, new DependencyCache.DependencyOnTile());
      }

      if (this.dependencyTable.get(var7) == null) {
         this.dependencyTable.put(var7, new DependencyCache.DependencyOnTile());
      }

      if (this.dependencyTable.get(var4) == null) {
         this.dependencyTable.put(var4, new DependencyCache.DependencyOnTile());
      }

      if (this.dependencyTable.get(var5) == null) {
         this.dependencyTable.put(var5, new DependencyCache.DependencyOnTile());
      }

      if (this.dependencyTable.get(var9) == null) {
         this.dependencyTable.put(var9, new DependencyCache.DependencyOnTile());
      }

      if (this.dependencyTable.get(var10) == null) {
         this.dependencyTable.put(var10, new DependencyCache.DependencyOnTile());
      }

      if (this.dependencyTable.get(var8) == null) {
         this.dependencyTable.put(var8, new DependencyCache.DependencyOnTile());
      }

      if (this.dependencyTable.get(var11) == null) {
         this.dependencyTable.put(var11, new DependencyCache.DependencyOnTile());
      }

      this.fillDependencyLabels(var1);
      this.fillDependencyLabels(var3);
      Iterator var12 = var2.iterator();

      while(var12.hasNext()) {
         SymbolContainer var13 = (SymbolContainer)var12.next();
         var1 = null;
         DependencyCache.DependencySymbol var15 = var1;
         DependencyCache.DependencySymbol var14;
         if (var13.point.y < 0.0D) {
            var15 = var1;
            if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var6)).drawn) {
               DependencyCache.DependencyOnTile var17 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var6);
               var14 = new DependencyCache.DependencySymbol(var13.symbol, this.currentTile);
               this.currentDependencyOnTile.addSymbol(new DependencyCache.Dependency(var14, new Point(var13.point.x, var13.point.y)));
               var17.addSymbol(new DependencyCache.Dependency(var14, new Point(var13.point.x, var13.point.y + 256.0D)));
               var14.addTile(var6);
               if (var13.point.x < 0.0D && !((DependencyCache.DependencyOnTile)this.dependencyTable.get(var8)).drawn) {
                  ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var8)).addSymbol(new DependencyCache.Dependency(var14, new Point(var13.point.x + 256.0D, var13.point.y + 256.0D)));
                  var14.addTile(var8);
               }

               var15 = var14;
               if (var13.point.x + (double)var13.symbol.getWidth() > 256.0D) {
                  var15 = var14;
                  if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var10)).drawn) {
                     ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var10)).addSymbol(new DependencyCache.Dependency(var14, new Point(var13.point.x - 256.0D, var13.point.y + 256.0D)));
                     var14.addTile(var10);
                     var15 = var14;
                  }
               }
            }
         }

         var14 = var15;
         if (var13.point.y + (double)var13.symbol.getHeight() > 256.0D) {
            var14 = var15;
            if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var7)).drawn) {
               DependencyCache.DependencyOnTile var16 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var7);
               DependencyCache.DependencySymbol var18 = var15;
               if (var15 == null) {
                  var18 = new DependencyCache.DependencySymbol(var13.symbol, this.currentTile);
                  this.currentDependencyOnTile.addSymbol(new DependencyCache.Dependency(var18, new Point(var13.point.x, var13.point.y)));
               }

               var16.addSymbol(new DependencyCache.Dependency(var18, new Point(var13.point.x, var13.point.y - 256.0D)));
               var18.addTile(var7);
               if (var13.point.x < 0.0D && !((DependencyCache.DependencyOnTile)this.dependencyTable.get(var9)).drawn) {
                  ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var9)).addSymbol(new DependencyCache.Dependency(var18, new Point(var13.point.x + 256.0D, var13.point.y - 256.0D)));
                  var18.addTile(var9);
               }

               var14 = var18;
               if (var13.point.x + (double)var13.symbol.getWidth() > 256.0D) {
                  var14 = var18;
                  if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var11)).drawn) {
                     ((DependencyCache.DependencyOnTile)this.dependencyTable.get(var11)).addSymbol(new DependencyCache.Dependency(var18, new Point(var13.point.x - 256.0D, var13.point.y - 256.0D)));
                     var18.addTile(var11);
                     var14 = var18;
                  }
               }
            }
         }

         var15 = var14;
         DependencyCache.DependencyOnTile var19;
         if (var13.point.x < 0.0D) {
            var15 = var14;
            if (!((DependencyCache.DependencyOnTile)this.dependencyTable.get(var4)).drawn) {
               var19 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var4);
               var15 = var14;
               if (var14 == null) {
                  var15 = new DependencyCache.DependencySymbol(var13.symbol, this.currentTile);
                  this.currentDependencyOnTile.addSymbol(new DependencyCache.Dependency(var15, new Point(var13.point.x, var13.point.y)));
               }

               var19.addSymbol(new DependencyCache.Dependency(var15, new Point(var13.point.x + 256.0D, var13.point.y)));
               var15.addTile(var4);
            }
         }

         if (var13.point.x + (double)var13.symbol.getWidth() > 256.0D && !((DependencyCache.DependencyOnTile)this.dependencyTable.get(var5)).drawn) {
            var19 = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var5);
            var14 = var15;
            if (var15 == null) {
               var14 = new DependencyCache.DependencySymbol(var13.symbol, this.currentTile);
               this.currentDependencyOnTile.addSymbol(new DependencyCache.Dependency(var14, new Point(var13.point.x, var13.point.y)));
            }

            var19.addSymbol(new DependencyCache.Dependency(var14, new Point(var13.point.x - 256.0D, var13.point.y)));
            var14.addTile(var5);
         }
      }

   }

   private void removeOverlappingAreaLabelsWithDependencyLabels(List var1) {
      for(int var2 = 0; var2 < this.currentDependencyOnTile.labels.size(); ++var2) {
         this.depLabel = (DependencyCache.Dependency)this.currentDependencyOnTile.labels.get(var2);
         this.rect1 = new Rect((int)this.depLabel.point.x, (int)(this.depLabel.point.y - (double)((DependencyCache.DependencyText)this.depLabel.value).boundary.height()), (int)(this.depLabel.point.x + (double)((DependencyCache.DependencyText)this.depLabel.value).boundary.width()), (int)this.depLabel.point.y);

         int var5;
         for(int var3 = 0; var3 < var1.size(); var3 = var5 + 1) {
            PointTextContainer var4 = (PointTextContainer)var1.get(var3);
            this.rect2 = new Rect((int)var4.x, (int)var4.y - var4.boundary.height(), (int)var4.x + var4.boundary.width(), (int)var4.y);
            var5 = var3;
            if (Rect.intersects(this.rect2, this.rect1)) {
               var1.remove(var3);
               var5 = var3 - 1;
            }
         }
      }

   }

   private void removeOverlappingAreaLabelsWithDependencySymbols(List var1) {
      Iterator var2 = this.currentDependencyOnTile.symbols.iterator();

      while(var2.hasNext()) {
         DependencyCache.Dependency var3 = (DependencyCache.Dependency)var2.next();
         int var4 = (int)var3.point.x;
         int var5 = (int)var3.point.y;
         int var6 = (int)var3.point.x;
         int var7 = ((DependencyCache.DependencySymbol)var3.value).symbol.getWidth();
         int var8 = (int)var3.point.y;
         this.rect1 = new Rect(var4, var5, var6 + var7, ((DependencyCache.DependencySymbol)var3.value).symbol.getHeight() + var8);

         for(var4 = 0; var4 < var1.size(); var4 = var6 + 1) {
            PointTextContainer var9 = (PointTextContainer)var1.get(var4);
            this.rect2 = new Rect((int)var9.x, (int)(var9.y - (double)var9.boundary.height()), (int)(var9.x + (double)var9.boundary.width()), (int)var9.y);
            var6 = var4;
            if (Rect.intersects(this.rect2, this.rect1)) {
               var1.remove(var4);
               var6 = var4 - 1;
            }
         }
      }

   }

   private void removeOverlappingLabelsWithDependencyLabels(List var1) {
      int var4;
      for(int var2 = 0; var2 < this.currentDependencyOnTile.labels.size(); var2 = var4 + 1) {
         int var3 = 0;

         while(true) {
            var4 = var2;
            if (var3 >= var1.size()) {
               break;
            }

            if (((PointTextContainer)var1.get(var3)).text.equals(((DependencyCache.DependencyText)((DependencyCache.Dependency)this.currentDependencyOnTile.labels.get(var2)).value).text) && ((PointTextContainer)var1.get(var3)).paintFront.equals(((DependencyCache.DependencyText)((DependencyCache.Dependency)this.currentDependencyOnTile.labels.get(var2)).value).paintFront) && ((PointTextContainer)var1.get(var3)).paintBack.equals(((DependencyCache.DependencyText)((DependencyCache.Dependency)this.currentDependencyOnTile.labels.get(var2)).value).paintBack)) {
               var1.remove(var3);
               var4 = var2 - 1;
               break;
            }

            ++var3;
         }
      }

   }

   private void removeOverlappingSymbolsWithDepencySymbols(List var1, int var2) {
      for(int var3 = 0; var3 < this.currentDependencyOnTile.symbols.size(); ++var3) {
         DependencyCache.Dependency var4 = (DependencyCache.Dependency)this.currentDependencyOnTile.symbols.get(var3);
         int var5 = (int)var4.point.x;
         int var6 = (int)var4.point.y;
         int var7 = (int)var4.point.x;
         int var8 = ((DependencyCache.DependencySymbol)var4.value).symbol.getWidth();
         int var9 = (int)var4.point.y;
         this.rect1 = new Rect(var5 - var2, var6 - var2, var8 + var7 + var2, ((DependencyCache.DependencySymbol)var4.value).symbol.getHeight() + var9 + var2);

         for(var5 = 0; var5 < var1.size(); var5 = var8 + 1) {
            SymbolContainer var10 = (SymbolContainer)var1.get(var5);
            this.rect2 = new Rect((int)var10.point.x, (int)var10.point.y, (int)var10.point.x + var10.symbol.getWidth(), (int)var10.point.y + var10.symbol.getHeight());
            var8 = var5;
            if (Rect.intersects(this.rect2, this.rect1)) {
               var1.remove(var5);
               var8 = var5 - 1;
            }
         }
      }

   }

   private void removeOverlappingSymbolsWithDependencyLabels(List var1) {
      for(int var2 = 0; var2 < this.currentDependencyOnTile.labels.size(); ++var2) {
         this.depLabel = (DependencyCache.Dependency)this.currentDependencyOnTile.labels.get(var2);
         this.rect1 = new Rect((int)this.depLabel.point.x, (int)(this.depLabel.point.y - (double)((DependencyCache.DependencyText)this.depLabel.value).boundary.height()), (int)(this.depLabel.point.x + (double)((DependencyCache.DependencyText)this.depLabel.value).boundary.width()), (int)this.depLabel.point.y);

         int var4;
         for(int var3 = 0; var3 < var1.size(); var3 = var4 + 1) {
            this.smb = (SymbolContainer)var1.get(var3);
            this.rect2 = new Rect((int)this.smb.point.x, (int)this.smb.point.y, (int)this.smb.point.x + this.smb.symbol.getWidth(), (int)this.smb.point.y + this.smb.symbol.getHeight());
            var4 = var3;
            if (Rect.intersects(this.rect2, this.rect1)) {
               var1.remove(var3);
               var4 = var3 - 1;
            }
         }
      }

   }

   void fillDependencyOnTile(List var1, List var2, List var3) {
      this.currentDependencyOnTile.drawn = true;
      if (!var1.isEmpty() || !var2.isEmpty() || !var3.isEmpty()) {
         this.fillDependencyOnTile2(var1, var2, var3);
      }

      if (this.currentDependencyOnTile.labels != null) {
         this.addLabelsFromDependencyOnTile(var1);
      }

      if (this.currentDependencyOnTile.symbols != null) {
         this.addSymbolsFromDependencyOnTile(var2);
      }

   }

   void generateTileAndDependencyOnTile(Tile var1) {
      this.currentTile = new Tile(var1.tileX, var1.tileY, var1.zoomLevel);
      this.currentDependencyOnTile = (DependencyCache.DependencyOnTile)this.dependencyTable.get(this.currentTile);
      if (this.currentDependencyOnTile == null) {
         this.dependencyTable.put(this.currentTile, new DependencyCache.DependencyOnTile());
         this.currentDependencyOnTile = (DependencyCache.DependencyOnTile)this.dependencyTable.get(this.currentTile);
      }

   }

   void removeAreaLabelsInAlreadyDrawnAreas(List var1) {
      Tile var2 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var3 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var4 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
      Tile var5 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var2);
      boolean var6;
      if (this.tmp == null) {
         var6 = false;
      } else {
         var6 = this.tmp.drawn;
      }

      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var3);
      boolean var7;
      if (this.tmp == null) {
         var7 = false;
      } else {
         var7 = this.tmp.drawn;
      }

      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var4);
      boolean var8;
      if (this.tmp == null) {
         var8 = false;
      } else {
         var8 = this.tmp.drawn;
      }

      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var5);
      boolean var9;
      if (this.tmp == null) {
         var9 = false;
      } else {
         var9 = this.tmp.drawn;
      }

      int var11;
      for(int var10 = 0; var10 < var1.size(); var10 = var11 + 1) {
         PointTextContainer var12 = (PointTextContainer)var1.get(var10);
         if (var8 && var12.y - (double)var12.boundary.height() < 0.0D) {
            var1.remove(var10);
            var11 = var10 - 1;
         } else if (var9 && var12.y > 256.0D) {
            var1.remove(var10);
            var11 = var10 - 1;
         } else if (var6 && var12.x < 0.0D) {
            var1.remove(var10);
            var11 = var10 - 1;
         } else {
            var11 = var10;
            if (var7) {
               var11 = var10;
               if (var12.x + (double)var12.boundary.width() > 256.0D) {
                  var1.remove(var10);
                  var11 = var10 - 1;
               }
            }
         }
      }

   }

   void removeOverlappingObjectsWithDependencyOnTile(List var1, List var2, List var3) {
      if (this.currentDependencyOnTile.labels != null && this.currentDependencyOnTile.labels.size() != 0) {
         this.removeOverlappingLabelsWithDependencyLabels(var1);
         this.removeOverlappingSymbolsWithDependencyLabels(var3);
         this.removeOverlappingAreaLabelsWithDependencyLabels(var2);
      }

      if (this.currentDependencyOnTile.symbols != null && this.currentDependencyOnTile.symbols.size() != 0) {
         this.removeOverlappingSymbolsWithDepencySymbols(var3, 2);
         this.removeOverlappingAreaLabelsWithDependencySymbols(var2);
      }

   }

   void removeReferencePointsFromDependencyCache(LabelPlacement.ReferencePosition[] var1) {
      Tile var2 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var3 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var4 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
      Tile var5 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var2);
      boolean var6;
      if (this.tmp == null) {
         var6 = false;
      } else {
         var6 = this.tmp.drawn;
      }

      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var3);
      boolean var7;
      if (this.tmp == null) {
         var7 = false;
      } else {
         var7 = this.tmp.drawn;
      }

      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var4);
      boolean var8;
      if (this.tmp == null) {
         var8 = false;
      } else {
         var8 = this.tmp.drawn;
      }

      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var5);
      boolean var9;
      if (this.tmp == null) {
         var9 = false;
      } else {
         var9 = this.tmp.drawn;
      }

      int var10;
      for(var10 = 0; var10 < var1.length; ++var10) {
         LabelPlacement.ReferencePosition var13 = var1[var10];
         if (var13 != null) {
            if (var8 && var13.y - (double)var13.height < 0.0D) {
               var1[var10] = null;
            } else if (var9 && var13.y >= 256.0D) {
               var1[var10] = null;
            } else if (var6 && var13.x < 0.0D) {
               var1[var10] = null;
            } else if (var7 && var13.x + (double)var13.width > 256.0D) {
               var1[var10] = null;
            }
         }
      }

      if (this.currentDependencyOnTile != null) {
         if (this.currentDependencyOnTile.labels != null) {
            for(var10 = 0; var10 < this.currentDependencyOnTile.labels.size(); ++var10) {
               this.depLabel = (DependencyCache.Dependency)this.currentDependencyOnTile.labels.get(var10);
               this.rect1 = new Rect((int)this.depLabel.point.x - 2, (int)(this.depLabel.point.y - (double)((DependencyCache.DependencyText)this.depLabel.value).boundary.height()) - 2, (int)(this.depLabel.point.x + (double)((DependencyCache.DependencyText)this.depLabel.value).boundary.width() + (double)2), (int)(this.depLabel.point.y + (double)2));

               for(int var11 = 0; var11 < var1.length; ++var11) {
                  if (var1[var11] != null) {
                     this.rect2 = new Rect((int)var1[var11].x, (int)(var1[var11].y - (double)var1[var11].height), (int)(var1[var11].x + (double)var1[var11].width), (int)var1[var11].y);
                     if (Rect.intersects(this.rect2, this.rect1)) {
                        var1[var11] = null;
                     }
                  }
               }
            }
         }

         if (this.currentDependencyOnTile.symbols != null) {
            Iterator var14 = this.currentDependencyOnTile.symbols.iterator();

            while(var14.hasNext()) {
               DependencyCache.Dependency var12 = (DependencyCache.Dependency)var14.next();
               this.rect1 = new Rect((int)var12.point.x, (int)var12.point.y, (int)(var12.point.x + (double)((DependencyCache.DependencySymbol)var12.value).symbol.getWidth()), (int)(var12.point.y + (double)((DependencyCache.DependencySymbol)var12.value).symbol.getHeight()));

               for(var10 = 0; var10 < var1.length; ++var10) {
                  if (var1[var10] != null) {
                     this.rect2 = new Rect((int)var1[var10].x, (int)(var1[var10].y - (double)var1[var10].height), (int)(var1[var10].x + (double)var1[var10].width), (int)var1[var10].y);
                     if (Rect.intersects(this.rect2, this.rect1)) {
                        var1[var10] = null;
                     }
                  }
               }
            }
         }
      }

   }

   void removeSymbolsFromDrawnAreas(List var1) {
      Tile var2 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var3 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
      Tile var4 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
      Tile var5 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var2);
      boolean var6;
      if (this.tmp == null) {
         var6 = false;
      } else {
         var6 = this.tmp.drawn;
      }

      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var3);
      boolean var7;
      if (this.tmp == null) {
         var7 = false;
      } else {
         var7 = this.tmp.drawn;
      }

      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var4);
      boolean var8;
      if (this.tmp == null) {
         var8 = false;
      } else {
         var8 = this.tmp.drawn;
      }

      this.tmp = (DependencyCache.DependencyOnTile)this.dependencyTable.get(var5);
      boolean var9;
      if (this.tmp == null) {
         var9 = false;
      } else {
         var9 = this.tmp.drawn;
      }

      int var11;
      for(int var10 = 0; var10 < var1.size(); var10 = var11 + 1) {
         SymbolContainer var12 = (SymbolContainer)var1.get(var10);
         if (var8 && var12.point.y < 0.0D) {
            var1.remove(var10);
            var11 = var10 - 1;
         } else if (var9 && var12.point.y + (double)var12.symbol.getHeight() > 256.0D) {
            var1.remove(var10);
            var11 = var10 - 1;
         } else if (var6 && var12.point.x < 0.0D) {
            var1.remove(var10);
            var11 = var10 - 1;
         } else {
            var11 = var10;
            if (var7) {
               var11 = var10;
               if (var12.point.x + (double)var12.symbol.getWidth() > 256.0D) {
                  var1.remove(var10);
                  var11 = var10 - 1;
               }
            }
         }
      }

   }

   private static class Dependency {
      final Point point;
      final Object value;

      Dependency(Object var1, Point var2) {
         this.value = var1;
         this.point = var2;
      }
   }

   private static class DependencyOnTile {
      boolean drawn = false;
      List labels = null;
      List symbols = null;

      DependencyOnTile() {
      }

      void addSymbol(DependencyCache.Dependency var1) {
         if (this.symbols == null) {
            this.symbols = new ArrayList();
         }

         this.symbols.add(var1);
      }

      void addText(DependencyCache.Dependency var1) {
         if (this.labels == null) {
            this.labels = new ArrayList();
         }

         this.labels.add(var1);
      }
   }

   private static class DependencySymbol {
      final Bitmap symbol;
      private final List tiles;

      DependencySymbol(Bitmap var1, Tile var2) {
         this.symbol = var1;
         this.tiles = new LinkedList();
         this.tiles.add(var2);
      }

      void addTile(Tile var1) {
         this.tiles.add(var1);
      }
   }

   private static class DependencyText {
      final Rect boundary;
      final Paint paintBack;
      final Paint paintFront;
      final String text;
      final List tiles;

      DependencyText(Paint var1, Paint var2, String var3, Rect var4, Tile var5) {
         this.paintFront = var1;
         this.paintBack = var2;
         this.text = var3;
         this.tiles = new LinkedList();
         this.tiles.add(var5);
         this.boundary = var4;
      }

      void addTile(Tile var1) {
         this.tiles.add(var1);
      }
   }
}
