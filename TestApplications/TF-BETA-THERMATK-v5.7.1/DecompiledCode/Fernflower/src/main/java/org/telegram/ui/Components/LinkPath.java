package org.telegram.ui.Components;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import org.telegram.messenger.AndroidUtilities;

public class LinkPath extends Path {
   private boolean allowReset = true;
   private int baselineShift;
   private StaticLayout currentLayout;
   private int currentLine;
   private float heightOffset;
   private float lastTop = -1.0F;
   private int lineHeight;
   private RectF rect;
   private boolean useRoundRect;

   public LinkPath() {
   }

   public LinkPath(boolean var1) {
      this.useRoundRect = var1;
   }

   public void addRect(float var1, float var2, float var3, float var4, Direction var5) {
      float var6 = this.heightOffset;
      float var7 = var2 + var6;
      var4 += var6;
      var2 = this.lastTop;
      if (var2 == -1.0F) {
         this.lastTop = var7;
      } else if (var2 != var7) {
         this.lastTop = var7;
         ++this.currentLine;
      }

      var2 = this.currentLayout.getLineRight(this.currentLine);
      var6 = this.currentLayout.getLineLeft(this.currentLine);
      if (var1 < var2 && (var1 > var6 || var3 > var6)) {
         if (var3 <= var2) {
            var2 = var3;
         }

         if (var1 < var6) {
            var3 = var6;
         } else {
            var3 = var1;
         }

         int var8 = VERSION.SDK_INT;
         var1 = 0.0F;
         var6 = 0.0F;
         if (var8 >= 28) {
            var1 = var4;
            if (var4 - var7 > (float)this.lineHeight) {
               float var9 = this.heightOffset;
               var1 = var6;
               if (var4 != (float)this.currentLayout.getHeight()) {
                  var1 = (float)this.currentLayout.getLineBottom(this.currentLine) - this.currentLayout.getSpacingAdd();
               }

               var1 += var9;
            }
         } else {
            if (var4 != (float)this.currentLayout.getHeight()) {
               var1 = this.currentLayout.getSpacingAdd();
            }

            var1 = var4 - var1;
         }

         var8 = this.baselineShift;
         if (var8 < 0) {
            var6 = var1 + (float)var8;
            var4 = var7;
         } else {
            var4 = var7;
            var6 = var1;
            if (var8 > 0) {
               var4 = var7 + (float)var8;
               var6 = var1;
            }
         }

         if (this.useRoundRect) {
            if (this.rect == null) {
               this.rect = new RectF();
            }

            this.rect.set(var3 - (float)AndroidUtilities.dp(4.0F), var4, var2 + (float)AndroidUtilities.dp(4.0F), var6);
            super.addRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), var5);
         } else {
            super.addRect(var3, var4, var2, var6, var5);
         }
      }

   }

   public boolean isUsingRoundRect() {
      return this.useRoundRect;
   }

   public void reset() {
      if (this.allowReset) {
         super.reset();
      }
   }

   public void setAllowReset(boolean var1) {
      this.allowReset = var1;
   }

   public void setBaselineShift(int var1) {
      this.baselineShift = var1;
   }

   public void setCurrentLayout(StaticLayout var1, int var2, float var3) {
      this.currentLayout = var1;
      this.currentLine = var1.getLineForOffset(var2);
      this.lastTop = -1.0F;
      this.heightOffset = var3;
      if (VERSION.SDK_INT >= 28) {
         var2 = var1.getLineCount();
         if (var2 > 0) {
            --var2;
            this.lineHeight = var1.getLineBottom(var2) - var1.getLineTop(var2);
         }
      }

   }

   public void setUseRoundRect(boolean var1) {
      this.useRoundRect = var1;
   }
}
