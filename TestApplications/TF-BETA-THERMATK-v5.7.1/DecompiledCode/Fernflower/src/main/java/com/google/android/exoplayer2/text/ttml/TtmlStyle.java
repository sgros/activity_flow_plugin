package com.google.android.exoplayer2.text.ttml;

import android.text.Layout.Alignment;
import com.google.android.exoplayer2.util.Assertions;

final class TtmlStyle {
   private int backgroundColor;
   private int bold = -1;
   private int fontColor;
   private String fontFamily;
   private float fontSize;
   private int fontSizeUnit = -1;
   private boolean hasBackgroundColor;
   private boolean hasFontColor;
   private String id;
   private TtmlStyle inheritableStyle;
   private int italic = -1;
   private int linethrough = -1;
   private Alignment textAlign;
   private int underline = -1;

   public TtmlStyle() {
   }

   private TtmlStyle inherit(TtmlStyle var1, boolean var2) {
      if (var1 != null) {
         if (!this.hasFontColor && var1.hasFontColor) {
            this.setFontColor(var1.fontColor);
         }

         if (this.bold == -1) {
            this.bold = var1.bold;
         }

         if (this.italic == -1) {
            this.italic = var1.italic;
         }

         if (this.fontFamily == null) {
            this.fontFamily = var1.fontFamily;
         }

         if (this.linethrough == -1) {
            this.linethrough = var1.linethrough;
         }

         if (this.underline == -1) {
            this.underline = var1.underline;
         }

         if (this.textAlign == null) {
            this.textAlign = var1.textAlign;
         }

         if (this.fontSizeUnit == -1) {
            this.fontSizeUnit = var1.fontSizeUnit;
            this.fontSize = var1.fontSize;
         }

         if (var2 && !this.hasBackgroundColor && var1.hasBackgroundColor) {
            this.setBackgroundColor(var1.backgroundColor);
         }
      }

      return this;
   }

   public TtmlStyle chain(TtmlStyle var1) {
      this.inherit(var1, true);
      return this;
   }

   public int getBackgroundColor() {
      if (this.hasBackgroundColor) {
         return this.backgroundColor;
      } else {
         throw new IllegalStateException("Background color has not been defined.");
      }
   }

   public int getFontColor() {
      if (this.hasFontColor) {
         return this.fontColor;
      } else {
         throw new IllegalStateException("Font color has not been defined.");
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

   public String getId() {
      return this.id;
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

   public TtmlStyle setBackgroundColor(int var1) {
      this.backgroundColor = var1;
      this.hasBackgroundColor = true;
      return this;
   }

   public TtmlStyle setBold(boolean var1) {
      boolean var2;
      if (this.inheritableStyle == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.bold = var1;
      return this;
   }

   public TtmlStyle setFontColor(int var1) {
      boolean var2;
      if (this.inheritableStyle == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.fontColor = var1;
      this.hasFontColor = true;
      return this;
   }

   public TtmlStyle setFontFamily(String var1) {
      boolean var2;
      if (this.inheritableStyle == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.fontFamily = var1;
      return this;
   }

   public TtmlStyle setFontSize(float var1) {
      this.fontSize = var1;
      return this;
   }

   public TtmlStyle setFontSizeUnit(int var1) {
      this.fontSizeUnit = var1;
      return this;
   }

   public TtmlStyle setId(String var1) {
      this.id = var1;
      return this;
   }

   public TtmlStyle setItalic(boolean var1) {
      boolean var2;
      if (this.inheritableStyle == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.italic = var1;
      return this;
   }

   public TtmlStyle setLinethrough(boolean var1) {
      boolean var2;
      if (this.inheritableStyle == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.linethrough = var1;
      return this;
   }

   public TtmlStyle setTextAlign(Alignment var1) {
      this.textAlign = var1;
      return this;
   }

   public TtmlStyle setUnderline(boolean var1) {
      boolean var2;
      if (this.inheritableStyle == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.underline = var1;
      return this;
   }
}
