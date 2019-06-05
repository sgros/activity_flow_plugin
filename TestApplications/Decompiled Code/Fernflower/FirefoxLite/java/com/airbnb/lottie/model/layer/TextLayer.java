package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
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
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.content.ShapeGroup;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextLayer extends BaseLayer {
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
   private BaseKeyframeAnimation strokeColorAnimation;
   private final Paint strokePaint = new Paint(1) {
      {
         this.setStyle(Style.STROKE);
      }
   };
   private BaseKeyframeAnimation strokeWidthAnimation;
   private final char[] tempCharArray = new char[1];
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
      if (var3 != null && var3.color != null) {
         this.colorAnimation = var3.color.createAnimation();
         this.colorAnimation.addUpdateListener(this);
         this.addAnimation(this.colorAnimation);
      }

      if (var3 != null && var3.stroke != null) {
         this.strokeColorAnimation = var3.stroke.createAnimation();
         this.strokeColorAnimation.addUpdateListener(this);
         this.addAnimation(this.strokeColorAnimation);
      }

      if (var3 != null && var3.strokeWidth != null) {
         this.strokeWidthAnimation = var3.strokeWidth.createAnimation();
         this.strokeWidthAnimation.addUpdateListener(this);
         this.addAnimation(this.strokeWidthAnimation);
      }

      if (var3 != null && var3.tracking != null) {
         this.trackingAnimation = var3.tracking.createAnimation();
         this.trackingAnimation.addUpdateListener(this);
         this.addAnimation(this.trackingAnimation);
      }

   }

   private void drawCharacter(char[] var1, Paint var2, Canvas var3) {
      if (var2.getColor() != 0) {
         if (var2.getStyle() != Style.STROKE || var2.getStrokeWidth() != 0.0F) {
            var3.drawText(var1, 0, 1, 0.0F, 0.0F, var2);
         }
      }
   }

   private void drawCharacterAsGlyph(FontCharacter var1, Matrix var2, float var3, DocumentData var4, Canvas var5) {
      List var8 = this.getContentsForCharacter(var1);

      for(int var6 = 0; var6 < var8.size(); ++var6) {
         Path var7 = ((ContentGroup)var8.get(var6)).getPath();
         var7.computeBounds(this.rectF, false);
         this.matrix.set(var2);
         this.matrix.preTranslate(0.0F, (float)(-var4.baselineShift) * Utils.dpScale());
         this.matrix.preScale(var3, var3);
         var7.transform(this.matrix);
         if (var4.strokeOverFill) {
            this.drawGlyph(var7, this.fillPaint, var5);
            this.drawGlyph(var7, this.strokePaint, var5);
         } else {
            this.drawGlyph(var7, this.strokePaint, var5);
            this.drawGlyph(var7, this.fillPaint, var5);
         }
      }

   }

   private void drawCharacterFromFont(char var1, DocumentData var2, Canvas var3) {
      this.tempCharArray[0] = (char)var1;
      if (var2.strokeOverFill) {
         this.drawCharacter(this.tempCharArray, this.fillPaint, var3);
         this.drawCharacter(this.tempCharArray, this.strokePaint, var3);
      } else {
         this.drawCharacter(this.tempCharArray, this.strokePaint, var3);
         this.drawCharacter(this.tempCharArray, this.fillPaint, var3);
      }

   }

   private void drawGlyph(Path var1, Paint var2, Canvas var3) {
      if (var2.getColor() != 0) {
         if (var2.getStyle() != Style.STROKE || var2.getStrokeWidth() != 0.0F) {
            var3.drawPath(var1, var2);
         }
      }
   }

   private void drawTextGlyphs(DocumentData var1, Matrix var2, Font var3, Canvas var4) {
      float var5 = (float)var1.size / 100.0F;
      float var6 = Utils.getScale(var2);
      String var7 = var1.text;

      for(int var8 = 0; var8 < var7.length(); ++var8) {
         int var9 = FontCharacter.hashFor(var7.charAt(var8), var3.getFamily(), var3.getStyle());
         FontCharacter var10 = (FontCharacter)this.composition.getCharacters().get(var9);
         if (var10 != null) {
            this.drawCharacterAsGlyph(var10, var2, var5, var1, var4);
            float var11 = (float)var10.getWidth();
            float var12 = Utils.dpScale();
            float var13 = (float)var1.tracking / 10.0F;
            float var14 = var13;
            if (this.trackingAnimation != null) {
               var14 = var13 + (Float)this.trackingAnimation.getValue();
            }

            var4.translate(var11 * var5 * var12 * var6 + var14 * var6, 0.0F);
         }
      }

   }

   private void drawTextWithFont(DocumentData var1, Font var2, Matrix var3, Canvas var4) {
      float var5 = Utils.getScale(var3);
      Typeface var6 = this.lottieDrawable.getTypeface(var2.getFamily(), var2.getStyle());
      if (var6 != null) {
         String var14 = var1.text;
         TextDelegate var7 = this.lottieDrawable.getTextDelegate();
         String var13 = var14;
         if (var7 != null) {
            var13 = var7.getTextInternal(var14);
         }

         this.fillPaint.setTypeface(var6);
         this.fillPaint.setTextSize((float)(var1.size * (double)Utils.dpScale()));
         this.strokePaint.setTypeface(this.fillPaint.getTypeface());
         this.strokePaint.setTextSize(this.fillPaint.getTextSize());

         for(int var8 = 0; var8 < var13.length(); ++var8) {
            char var9 = var13.charAt(var8);
            this.drawCharacterFromFont(var9, var1, var4);
            this.tempCharArray[0] = var9;
            float var10 = this.fillPaint.measureText(this.tempCharArray, 0, 1);
            float var11 = (float)var1.tracking / 10.0F;
            float var12 = var11;
            if (this.trackingAnimation != null) {
               var12 = var11 + (Float)this.trackingAnimation.getValue();
            }

            var4.translate(var10 + var12 * var5, 0.0F);
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

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      super.addValueCallback(var1, var2);
      if (var1 == LottieProperty.COLOR && this.colorAnimation != null) {
         this.colorAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.STROKE_COLOR && this.strokeColorAnimation != null) {
         this.strokeColorAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.STROKE_WIDTH && this.strokeWidthAnimation != null) {
         this.strokeWidthAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.TEXT_TRACKING && this.trackingAnimation != null) {
         this.trackingAnimation.setValueCallback(var2);
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
         if (this.colorAnimation != null) {
            this.fillPaint.setColor((Integer)this.colorAnimation.getValue());
         } else {
            this.fillPaint.setColor(var4.color);
         }

         if (this.strokeColorAnimation != null) {
            this.strokePaint.setColor((Integer)this.strokeColorAnimation.getValue());
         } else {
            this.strokePaint.setColor(var4.strokeColor);
         }

         var3 = (Integer)this.transform.getOpacity().getValue() * 255 / 100;
         this.fillPaint.setAlpha(var3);
         this.strokePaint.setAlpha(var3);
         if (this.strokeWidthAnimation != null) {
            this.strokePaint.setStrokeWidth((Float)this.strokeWidthAnimation.getValue());
         } else {
            float var6 = Utils.getScale(var2);
            this.strokePaint.setStrokeWidth((float)(var4.strokeWidth * (double)Utils.dpScale() * (double)var6));
         }

         if (this.lottieDrawable.useTextGlyphs()) {
            this.drawTextGlyphs(var4, var2, var5, var1);
         } else {
            this.drawTextWithFont(var4, var5, var2, var1);
         }

         var1.restore();
      }
   }
}
