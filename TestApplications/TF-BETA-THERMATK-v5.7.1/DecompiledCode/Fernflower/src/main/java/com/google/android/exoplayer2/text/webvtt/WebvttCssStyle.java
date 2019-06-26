package com.google.android.exoplayer2.text.webvtt;

import android.text.Layout.Alignment;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class WebvttCssStyle {
   private int backgroundColor;
   private int bold;
   private int fontColor;
   private String fontFamily;
   private float fontSize;
   private int fontSizeUnit;
   private boolean hasBackgroundColor;
   private boolean hasFontColor;
   private int italic;
   private int linethrough;
   private List targetClasses;
   private String targetId;
   private String targetTag;
   private String targetVoice;
   private Alignment textAlign;
   private int underline;

   public WebvttCssStyle() {
      this.reset();
   }

   private static int updateScoreForMatch(int var0, String var1, String var2, int var3) {
      if (!var1.isEmpty()) {
         int var4 = -1;
         if (var0 != -1) {
            if (var1.equals(var2)) {
               var4 = var0 + var3;
            }

            return var4;
         }
      }

      return var0;
   }

   public int getBackgroundColor() {
      if (this.hasBackgroundColor) {
         return this.backgroundColor;
      } else {
         throw new IllegalStateException("Background color not defined.");
      }
   }

   public int getFontColor() {
      if (this.hasFontColor) {
         return this.fontColor;
      } else {
         throw new IllegalStateException("Font color not defined");
      }
   }

   public String getFontFamily() {
      return this.fontFamily;
   }

   public float getFontSize() {
      return this.fontSize;
   }

   public int getFontSizeUnit() {
      return this.fontSizeUnit;
   }

   public int getSpecificityScore(String var1, String var2, String[] var3, String var4) {
      if (this.targetId.isEmpty() && this.targetTag.isEmpty() && this.targetClasses.isEmpty() && this.targetVoice.isEmpty()) {
         return var2.isEmpty();
      } else {
         int var5 = updateScoreForMatch(updateScoreForMatch(updateScoreForMatch(0, this.targetId, var1, 1073741824), this.targetTag, var2, 2), this.targetVoice, var4, 4);
         return var5 != -1 && Arrays.asList(var3).containsAll(this.targetClasses) ? var5 + this.targetClasses.size() * 4 : 0;
      }
   }

   public int getStyle() {
      if (this.bold == -1 && this.italic == -1) {
         return -1;
      } else {
         int var1 = this.bold;
         byte var2 = 0;
         byte var3;
         if (var1 == 1) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         if (this.italic == 1) {
            var2 = 2;
         }

         return var3 | var2;
      }
   }

   public Alignment getTextAlign() {
      return this.textAlign;
   }

   public boolean hasBackgroundColor() {
      return this.hasBackgroundColor;
   }

   public boolean hasFontColor() {
      return this.hasFontColor;
   }

   public boolean isLinethrough() {
      int var1 = this.linethrough;
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   public boolean isUnderline() {
      int var1 = this.underline;
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   public void reset() {
      this.targetId = "";
      this.targetTag = "";
      this.targetClasses = Collections.emptyList();
      this.targetVoice = "";
      this.fontFamily = null;
      this.hasFontColor = false;
      this.hasBackgroundColor = false;
      this.linethrough = -1;
      this.underline = -1;
      this.bold = -1;
      this.italic = -1;
      this.fontSizeUnit = -1;
      this.textAlign = null;
   }

   public WebvttCssStyle setBackgroundColor(int var1) {
      this.backgroundColor = var1;
      this.hasBackgroundColor = true;
      return this;
   }

   public WebvttCssStyle setBold(boolean var1) {
      this.bold = var1;
      return this;
   }

   public WebvttCssStyle setFontColor(int var1) {
      this.fontColor = var1;
      this.hasFontColor = true;
      return this;
   }

   public WebvttCssStyle setFontFamily(String var1) {
      this.fontFamily = Util.toLowerInvariant(var1);
      return this;
   }

   public WebvttCssStyle setItalic(boolean var1) {
      this.italic = var1;
      return this;
   }

   public void setTargetClasses(String[] var1) {
      this.targetClasses = Arrays.asList(var1);
   }

   public void setTargetId(String var1) {
      this.targetId = var1;
   }

   public void setTargetTagName(String var1) {
      this.targetTag = var1;
   }

   public void setTargetVoice(String var1) {
      this.targetVoice = var1;
   }

   public WebvttCssStyle setUnderline(boolean var1) {
      this.underline = var1;
      return this;
   }
}
