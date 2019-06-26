package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.TextDelegate;
import com.airbnb.lottie.animation.content.ContentGroup;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TextKeyframeAnimation;
import com.airbnb.lottie.model.DocumentData;
import com.airbnb.lottie.model.Font;
import com.airbnb.lottie.model.FontCharacter;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.content.ShapeGroup;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextLayer extends BaseLayer {
   private final LongSparseArray codePointCache = new LongSparseArray();
   private BaseKeyframeAnimation colorAnimation;
   private final LottieComposition composition;
   private final Map contentsForCharacter = new HashMap();
   private final Paint fillPaint = new Paint(1) {
      {
         this.setStyle(Style.FILL);
      }
   };
   private final LottieDrawable lottieDrawable;
   private final Matrix matrix = new Matrix();
   private final RectF rectF = new RectF();
   private final StringBuilder stringBuilder = new StringBuilder(2);
   private BaseKeyframeAnimation strokeColorAnimation;
   private final Paint strokePaint = new Paint(1) {
      {
         this.setStyle(Style.STROKE);
      }
   };
   private BaseKeyframeAnimation strokeWidthAnimation;
   private final TextKeyframeAnimation textAnimation;
   private BaseKeyframeAnimation trackingAnimation;

   TextLayer(LottieDrawable var1, Layer var2) {
      super(var1, var2);
      this.lottieDrawable = var1;
      this.composition = var2.getComposition();
      this.textAnimation = var2.getText().createAnimation();
      this.textAnimation.addUpdateListener(this);
      this.addAnimation(this.textAnimation);
      AnimatableTextProperties var3 = var2.getTextProperties();
      AnimatableColorValue var5;
      if (var3 != null) {
         var5 = var3.color;
         if (var5 != null) {
            this.colorAnimation = var5.createAnimation();
            this.colorAnimation.addUpdateListener(this);
            this.addAnimation(this.colorAnimation);
         }
      }

      if (var3 != null) {
         var5 = var3.stroke;
         if (var5 != null) {
            this.strokeColorAnimation = var5.createAnimation();
            this.strokeColorAnimation.addUpdateListener(this);
            this.addAnimation(this.strokeColorAnimation);
         }
      }

      if (var3 != null) {
         AnimatableFloatValue var6 = var3.strokeWidth;
         if (var6 != null) {
            this.strokeWidthAnimation = var6.createAnimation();
            this.strokeWidthAnimation.addUpdateListener(this);
            this.addAnimation(this.strokeWidthAnimation);
         }
      }

      if (var3 != null) {
         AnimatableFloatValue var4 = var3.tracking;
         if (var4 != null) {
            this.trackingAnimation = var4.createAnimation();
            this.trackingAnimation.addUpdateListener(this);
            this.addAnimation(this.trackingAnimation);
         }
      }

   }

   private void applyJustification(DocumentData.Justification var1, Canvas var2, float var3) {
      int var4 = null.$SwitchMap$com$airbnb$lottie$model$DocumentData$Justification[var1.ordinal()];
      if (var4 != 1) {
         if (var4 != 2) {
            if (var4 == 3) {
               var2.translate(-var3 / 2.0F, 0.0F);
            }
         } else {
            var2.translate(-var3, 0.0F);
         }
      }

   }

   private String codePointToString(String var1, int var2) {
      int var3 = var1.codePointAt(var2);

      int var4;
      int var5;
      for(var4 = Character.charCount(var3) + var2; var4 < var1.length(); var3 = var3 * 31 + var5) {
         var5 = var1.codePointAt(var4);
         if (!this.isModifier(var5)) {
            break;
         }

         var4 += Character.charCount(var5);
      }

      LongSparseArray var6 = this.codePointCache;
      long var7 = (long)var3;
      if (var6.containsKey(var7)) {
         return (String)this.codePointCache.get(var7);
      } else {
         this.stringBuilder.setLength(0);

         while(var2 < var4) {
            var3 = var1.codePointAt(var2);
            this.stringBuilder.appendCodePoint(var3);
            var2 += Character.charCount(var3);
         }

         var1 = this.stringBuilder.toString();
         this.codePointCache.put(var7, var1);
         return var1;
      }
   }

   private void drawCharacter(String var1, Paint var2, Canvas var3) {
      if (var2.getColor() != 0) {
         if (var2.getStyle() != Style.STROKE || var2.getStrokeWidth() != 0.0F) {
            var3.drawText(var1, 0, var1.length(), 0.0F, 0.0F, var2);
         }
      }
   }

   private void drawCharacterAsGlyph(FontCharacter var1, Matrix var2, float var3, DocumentData var4, Canvas var5) {
      List var6 = this.getContentsForCharacter(var1);

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         Path var8 = ((ContentGroup)var6.get(var7)).getPath();
         var8.computeBounds(this.rectF, false);
         this.matrix.set(var2);
         this.matrix.preTranslate(0.0F, (float)(-var4.baselineShift) * Utils.dpScale());
         this.matrix.preScale(var3, var3);
         var8.transform(this.matrix);
         if (var4.strokeOverFill) {
            this.drawGlyph(var8, this.fillPaint, var5);
            this.drawGlyph(var8, this.strokePaint, var5);
         } else {
            this.drawGlyph(var8, this.strokePaint, var5);
            this.drawGlyph(var8, this.fillPaint, var5);
         }
      }

   }

   private void drawCharacterFromFont(String var1, DocumentData var2, Canvas var3) {
      if (var2.strokeOverFill) {
         this.drawCharacter(var1, this.fillPaint, var3);
         this.drawCharacter(var1, this.strokePaint, var3);
      } else {
         this.drawCharacter(var1, this.strokePaint, var3);
         this.drawCharacter(var1, this.fillPaint, var3);
      }

   }

   private void drawFontTextLine(String var1, DocumentData var2, Canvas var3, float var4) {
      float var7;
      float var9;
      for(int var5 = 0; var5 < var1.length(); var3.translate(var7 + var9 * var4, 0.0F)) {
         String var6 = this.codePointToString(var1, var5);
         var5 += var6.length();
         this.drawCharacterFromFont(var6, var2, var3);
         var7 = this.fillPaint.measureText(var6, 0, 1);
         float var8 = (float)var2.tracking / 10.0F;
         BaseKeyframeAnimation var10 = this.trackingAnimation;
         var9 = var8;
         if (var10 != null) {
            var9 = var8 + (Float)var10.getValue();
         }
      }

   }

   private void drawGlyph(Path var1, Paint var2, Canvas var3) {
      if (var2.getColor() != 0) {
         if (var2.getStyle() != Style.STROKE || var2.getStrokeWidth() != 0.0F) {
            var3.drawPath(var1, var2);
         }
      }
   }

   private void drawGlyphTextLine(String var1, DocumentData var2, Matrix var3, Font var4, Canvas var5, float var6, float var7) {
      for(int var8 = 0; var8 < var1.length(); ++var8) {
         int var9 = FontCharacter.hashFor(var1.charAt(var8), var4.getFamily(), var4.getStyle());
         FontCharacter var10 = (FontCharacter)this.composition.getCharacters().get(var9);
         if (var10 != null) {
            this.drawCharacterAsGlyph(var10, var3, var7, var2, var5);
            float var11 = (float)var10.getWidth();
            float var12 = Utils.dpScale();
            float var13 = (float)var2.tracking / 10.0F;
            BaseKeyframeAnimation var15 = this.trackingAnimation;
            float var14 = var13;
            if (var15 != null) {
               var14 = var13 + (Float)var15.getValue();
            }

            var5.translate(var11 * var7 * var12 * var6 + var14 * var6, 0.0F);
         }
      }

   }

   private void drawTextGlyphs(DocumentData var1, Matrix var2, Font var3, Canvas var4) {
      float var5 = (float)var1.size / 100.0F;
      float var6 = Utils.getScale(var2);
      String var7 = var1.text;
      float var8 = (float)var1.lineHeight * Utils.dpScale();
      List var13 = this.getTextLines(var7);
      int var9 = var13.size();

      for(int var10 = 0; var10 < var9; ++var10) {
         String var11 = (String)var13.get(var10);
         float var12 = this.getTextLineWidthForGlyphs(var11, var3, var5, var6);
         var4.save();
         this.applyJustification(var1.justification, var4, var12);
         var12 = (float)(var9 - 1) * var8 / 2.0F;
         var4.translate(0.0F, (float)var10 * var8 - var12);
         this.drawGlyphTextLine(var11, var1, var2, var3, var4, var6, var5);
         var4.restore();
      }

   }

   private void drawTextWithFont(DocumentData var1, Font var2, Matrix var3, Canvas var4) {
      float var5 = Utils.getScale(var3);
      Typeface var6 = this.lottieDrawable.getTypeface(var2.getFamily(), var2.getStyle());
      if (var6 != null) {
         String var16 = var1.text;
         TextDelegate var7 = this.lottieDrawable.getTextDelegate();
         if (var7 != null) {
            var7.getTextInternal(var16);
            throw null;
         } else {
            this.fillPaint.setTypeface(var6);
            Paint var17 = this.fillPaint;
            double var8 = var1.size;
            double var10 = (double)Utils.dpScale();
            Double.isNaN(var10);
            var17.setTextSize((float)(var8 * var10));
            this.strokePaint.setTypeface(this.fillPaint.getTypeface());
            this.strokePaint.setTextSize(this.fillPaint.getTextSize());
            float var12 = (float)var1.lineHeight * Utils.dpScale();
            List var18 = this.getTextLines(var16);
            int var13 = var18.size();

            for(int var14 = 0; var14 < var13; ++var14) {
               var16 = (String)var18.get(var14);
               float var15 = this.strokePaint.measureText(var16);
               this.applyJustification(var1.justification, var4, var15);
               var15 = (float)(var13 - 1) * var12 / 2.0F;
               var4.translate(0.0F, (float)var14 * var12 - var15);
               this.drawFontTextLine(var16, var1, var4, var5);
               var4.setMatrix(var3);
            }

         }
      }
   }

   private List getContentsForCharacter(FontCharacter var1) {
      if (this.contentsForCharacter.containsKey(var1)) {
         return (List)this.contentsForCharacter.get(var1);
      } else {
         List var2 = var1.getShapes();
         int var3 = var2.size();
         ArrayList var4 = new ArrayList(var3);

         for(int var5 = 0; var5 < var3; ++var5) {
            ShapeGroup var6 = (ShapeGroup)var2.get(var5);
            var4.add(new ContentGroup(this.lottieDrawable, this, var6));
         }

         this.contentsForCharacter.put(var1, var4);
         return var4;
      }
   }

   private float getTextLineWidthForGlyphs(String var1, Font var2, float var3, float var4) {
      float var5 = 0.0F;

      for(int var6 = 0; var6 < var1.length(); ++var6) {
         int var7 = FontCharacter.hashFor(var1.charAt(var6), var2.getFamily(), var2.getStyle());
         FontCharacter var8 = (FontCharacter)this.composition.getCharacters().get(var7);
         if (var8 != null) {
            double var9 = (double)var5;
            double var11 = var8.getWidth();
            double var13 = (double)var3;
            Double.isNaN(var13);
            double var15 = (double)Utils.dpScale();
            Double.isNaN(var15);
            double var17 = (double)var4;
            Double.isNaN(var17);
            Double.isNaN(var9);
            var5 = (float)(var9 + var11 * var13 * var15 * var17);
         }
      }

      return var5;
   }

   private List getTextLines(String var1) {
      return Arrays.asList(var1.replaceAll("\r\n", "\r").replaceAll("\n", "\r").split("\r"));
   }

   private boolean isModifier(int var1) {
      boolean var2;
      if (Character.getType(var1) != 16 && Character.getType(var1) != 27 && Character.getType(var1) != 6 && Character.getType(var1) != 28 && Character.getType(var1) != 19) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      super.addValueCallback(var1, var2);
      BaseKeyframeAnimation var3;
      if (var1 == LottieProperty.COLOR) {
         var3 = this.colorAnimation;
         if (var3 != null) {
            var3.setValueCallback(var2);
            return;
         }
      }

      if (var1 == LottieProperty.STROKE_COLOR) {
         var3 = this.strokeColorAnimation;
         if (var3 != null) {
            var3.setValueCallback(var2);
            return;
         }
      }

      if (var1 == LottieProperty.STROKE_WIDTH) {
         var3 = this.strokeWidthAnimation;
         if (var3 != null) {
            var3.setValueCallback(var2);
            return;
         }
      }

      if (var1 == LottieProperty.TEXT_TRACKING) {
         BaseKeyframeAnimation var4 = this.trackingAnimation;
         if (var4 != null) {
            var4.setValueCallback(var2);
         }
      }

   }

   void drawLayer(Canvas var1, Matrix var2, int var3) {
      var1.save();
      if (!this.lottieDrawable.useTextGlyphs()) {
         var1.setMatrix(var2);
      }

      DocumentData var4 = (DocumentData)this.textAnimation.getValue();
      Font var5 = (Font)this.composition.getFonts().get(var4.fontName);
      if (var5 == null) {
         var1.restore();
      } else {
         BaseKeyframeAnimation var6 = this.colorAnimation;
         if (var6 != null) {
            this.fillPaint.setColor((Integer)var6.getValue());
         } else {
            this.fillPaint.setColor(var4.color);
         }

         var6 = this.strokeColorAnimation;
         if (var6 != null) {
            this.strokePaint.setColor((Integer)var6.getValue());
         } else {
            this.strokePaint.setColor(var4.strokeColor);
         }

         if (super.transform.getOpacity() == null) {
            var3 = 100;
         } else {
            var3 = (Integer)super.transform.getOpacity().getValue();
         }

         var3 = var3 * 255 / 100;
         this.fillPaint.setAlpha(var3);
         this.strokePaint.setAlpha(var3);
         var6 = this.strokeWidthAnimation;
         if (var6 != null) {
            this.strokePaint.setStrokeWidth((Float)var6.getValue());
         } else {
            float var7 = Utils.getScale(var2);
            Paint var14 = this.strokePaint;
            double var8 = var4.strokeWidth;
            double var10 = (double)Utils.dpScale();
            Double.isNaN(var10);
            double var12 = (double)var7;
            Double.isNaN(var12);
            var14.setStrokeWidth((float)(var8 * var10 * var12));
         }

         if (this.lottieDrawable.useTextGlyphs()) {
            this.drawTextGlyphs(var4, var2, var5, var1);
         } else {
            this.drawTextWithFont(var4, var5, var2, var1);
         }

         var1.restore();
      }
   }

   public void getBounds(RectF var1, Matrix var2, boolean var3) {
      super.getBounds(var1, var2, var3);
      var1.set(0.0F, 0.0F, (float)this.composition.getBounds().width(), (float)this.composition.getBounds().height());
   }
}
