package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Rect;
import org.telegram.ui.Components.Paint.Swatch;

public class TextPaintView extends EntityView {
   private int baseFontSize;
   private EditTextOutline editText;
   private boolean stroke;
   private Swatch swatch;

   public TextPaintView(Context var1, TextPaintView var2, Point var3) {
      this(var1, var3, var2.baseFontSize, var2.getText(), var2.getSwatch(), var2.stroke);
      this.setRotation(var2.getRotation());
      this.setScale(var2.getScale());
   }

   public TextPaintView(Context var1, Point var2, int var3, String var4, Swatch var5, boolean var6) {
      super(var1, var2);
      this.baseFontSize = var3;
      this.editText = new EditTextOutline(var1);
      this.editText.setBackgroundColor(0);
      this.editText.setPadding(AndroidUtilities.dp(7.0F), AndroidUtilities.dp(7.0F), AndroidUtilities.dp(7.0F), AndroidUtilities.dp(7.0F));
      this.editText.setClickable(false);
      this.editText.setEnabled(false);
      this.editText.setTextSize(0, (float)this.baseFontSize);
      this.editText.setText(var4);
      this.editText.setTextColor(var5.color);
      this.editText.setTypeface((Typeface)null, 1);
      this.editText.setGravity(17);
      this.editText.setHorizontallyScrolling(false);
      this.editText.setImeOptions(268435456);
      this.editText.setFocusableInTouchMode(true);
      EditTextOutline var7 = this.editText;
      var7.setInputType(var7.getInputType() | 16384);
      this.addView(this.editText, LayoutHelper.createFrame(-2, -2, 51));
      if (VERSION.SDK_INT >= 23) {
         this.editText.setBreakStrategy(0);
      }

      this.setSwatch(var5);
      this.setStroke(var6);
      this.updatePosition();
      this.editText.addTextChangedListener(new TextWatcher() {
         private int beforeCursorPosition = 0;
         private String text;

         public void afterTextChanged(Editable var1) {
            TextPaintView.this.editText.removeTextChangedListener(this);
            if (TextPaintView.this.editText.getLineCount() > 9) {
               TextPaintView.this.editText.setText(this.text);
               TextPaintView.this.editText.setSelection(this.beforeCursorPosition);
            }

            TextPaintView.this.editText.addTextChangedListener(this);
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            this.text = var1.toString();
            this.beforeCursorPosition = var2;
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }
      });
   }

   private void updateColor() {
      if (this.stroke) {
         this.editText.setTextColor(-1);
         this.editText.setStrokeColor(this.swatch.color);
         this.editText.setShadowLayer(0.0F, 0.0F, 0.0F, 0);
      } else {
         this.editText.setTextColor(this.swatch.color);
         this.editText.setStrokeColor(0);
         this.editText.setShadowLayer(8.0F, 0.0F, 2.0F, -1442840576);
      }

   }

   public void beginEditing() {
      this.editText.setEnabled(true);
      this.editText.setClickable(true);
      this.editText.requestFocus();
      EditTextOutline var1 = this.editText;
      var1.setSelection(var1.getText().length());
   }

   protected TextPaintView.TextViewSelectionView createSelectionView() {
      return new TextPaintView.TextViewSelectionView(this.getContext());
   }

   public void endEditing() {
      this.editText.clearFocus();
      this.editText.setEnabled(false);
      this.editText.setClickable(false);
      this.updateSelectionView();
   }

   public View getFocusedView() {
      return this.editText;
   }

   protected Rect getSelectionBounds() {
      float var1 = ((ViewGroup)this.getParent()).getScaleX();
      float var2 = (float)this.getWidth() * this.getScale() + (float)AndroidUtilities.dp(46.0F) / var1;
      float var3 = (float)this.getHeight() * this.getScale() + (float)AndroidUtilities.dp(20.0F) / var1;
      Point var4 = super.position;
      return new Rect((var4.x - var2 / 2.0F) * var1, (var4.y - var3 / 2.0F) * var1, var2 * var1, var3 * var1);
   }

   public Swatch getSwatch() {
      return this.swatch;
   }

   public String getText() {
      return this.editText.getText().toString();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.updatePosition();
   }

   public void setMaxWidth(int var1) {
      this.editText.setMaxWidth(var1);
   }

   public void setStroke(boolean var1) {
      this.stroke = var1;
      this.updateColor();
   }

   public void setSwatch(Swatch var1) {
      this.swatch = var1;
      this.updateColor();
   }

   public void setText(String var1) {
      this.editText.setText(var1);
   }

   public class TextViewSelectionView extends EntityView.SelectionView {
      public TextViewSelectionView(Context var2) {
         super(var2);
      }

      protected void onDraw(Canvas var1) {
         super.onDraw(var1);
         float var2 = (float)AndroidUtilities.dp(3.0F);
         float var3 = (float)AndroidUtilities.dp(3.0F);
         float var4 = (float)AndroidUtilities.dp(1.0F);
         float var5 = (float)AndroidUtilities.dp(4.5F);
         float var6 = var5 + var4 + (float)AndroidUtilities.dp(15.0F);
         float var7 = (float)this.getWidth();
         float var8 = var6 * 2.0F;
         var7 -= var8;
         float var9 = (float)this.getHeight() - var8;
         float var10 = var2 + var3;
         int var11 = (int)Math.floor((double)(var7 / var10));
         var8 = (float)Math.ceil((double)((var7 - (float)var11 * var10 + var2) / 2.0F));

         int var12;
         float var13;
         float var14;
         float var15;
         for(var12 = 0; var12 < var11; ++var12) {
            var13 = var8 + var6 + (float)var12 * var10;
            var14 = var4 / 2.0F;
            var15 = var13 + var3;
            var1.drawRect(var13, var6 - var14, var15, var6 + var14, super.paint);
            float var16 = var6 + var9;
            var1.drawRect(var13, var16 - var14, var15, var16 + var14, super.paint);
         }

         var11 = (int)Math.floor((double)(var9 / var10));
         var13 = (float)Math.ceil((double)((var9 - (float)var11 * var10 + var2) / 2.0F));

         for(var12 = 0; var12 < var11; ++var12) {
            var15 = var13 + var6 + (float)var12 * var10;
            var8 = var4 / 2.0F;
            var2 = var15 + var3;
            var1.drawRect(var6 - var8, var15, var6 + var8, var2, super.paint);
            var14 = var6 + var7;
            var1.drawRect(var14 - var8, var15, var14 + var8, var2, super.paint);
         }

         var8 = var9 / 2.0F + var6;
         var1.drawCircle(var6, var8, var5, super.dotPaint);
         var1.drawCircle(var6, var8, var5, super.dotStrokePaint);
         var6 += var7;
         var1.drawCircle(var6, var8, var5, super.dotPaint);
         var1.drawCircle(var6, var8, var5, super.dotStrokePaint);
      }

      protected int pointInsideHandle(float var1, float var2) {
         float var3 = (float)AndroidUtilities.dp(1.0F);
         float var4 = (float)AndroidUtilities.dp(19.5F);
         var3 += var4;
         float var5 = (float)this.getWidth();
         float var6 = var3 * 2.0F;
         var5 -= var6;
         var6 = (float)this.getHeight() - var6;
         float var7 = var6 / 2.0F + var3;
         if (var1 > var3 - var4 && var2 > var7 - var4 && var1 < var3 + var4 && var2 < var7 + var4) {
            return 1;
         } else {
            float var8 = var3 + var5;
            if (var1 > var8 - var4 && var2 > var7 - var4 && var1 < var8 + var4 && var2 < var7 + var4) {
               return 2;
            } else {
               return var1 > var3 && var1 < var5 && var2 > var3 && var2 < var6 ? 3 : 0;
            }
         }
      }
   }
}
