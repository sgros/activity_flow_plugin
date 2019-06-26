package android.support.v7.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import android.widget.ProgressBar;

class AppCompatProgressBarHelper {
   private static final int[] TINT_ATTRS = new int[]{16843067, 16843068};
   private Bitmap mSampleTile;
   private final ProgressBar mView;

   AppCompatProgressBarHelper(ProgressBar var1) {
      this.mView = var1;
   }

   private Shape getDrawableShape() {
      return new RoundRectShape(new float[]{5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F}, (RectF)null, (float[])null);
   }

   private Drawable tileify(Drawable var1, boolean var2) {
      if (var1 instanceof DrawableWrapper) {
         DrawableWrapper var3 = (DrawableWrapper)var1;
         Drawable var4 = var3.getWrappedDrawable();
         if (var4 != null) {
            var3.setWrappedDrawable(this.tileify(var4, var2));
         }
      } else {
         if (var1 instanceof LayerDrawable) {
            LayerDrawable var10 = (LayerDrawable)var1;
            int var5 = var10.getNumberOfLayers();
            Drawable[] var15 = new Drawable[var5];
            byte var6 = 0;

            int var7;
            for(var7 = 0; var7 < var5; ++var7) {
               int var8 = var10.getId(var7);
               Drawable var13 = var10.getDrawable(var7);
               if (var8 != 16908301 && var8 != 16908303) {
                  var2 = false;
               } else {
                  var2 = true;
               }

               var15[var7] = this.tileify(var13, var2);
            }

            LayerDrawable var16 = new LayerDrawable(var15);

            for(var7 = var6; var7 < var5; ++var7) {
               var16.setId(var7, var10.getId(var7));
            }

            return var16;
         }

         if (var1 instanceof BitmapDrawable) {
            BitmapDrawable var14 = (BitmapDrawable)var1;
            Bitmap var11 = var14.getBitmap();
            if (this.mSampleTile == null) {
               this.mSampleTile = var11;
            }

            Object var9 = new ShapeDrawable(this.getDrawableShape());
            BitmapShader var12 = new BitmapShader(var11, TileMode.REPEAT, TileMode.CLAMP);
            ((ShapeDrawable)var9).getPaint().setShader(var12);
            ((ShapeDrawable)var9).getPaint().setColorFilter(var14.getPaint().getColorFilter());
            if (var2) {
               var9 = new ClipDrawable((Drawable)var9, 3, 1);
            }

            return (Drawable)var9;
         }
      }

      return var1;
   }

   private Drawable tileifyIndeterminate(Drawable var1) {
      Object var2 = var1;
      if (var1 instanceof AnimationDrawable) {
         AnimationDrawable var3 = (AnimationDrawable)var1;
         int var4 = var3.getNumberOfFrames();
         var2 = new AnimationDrawable();
         ((AnimationDrawable)var2).setOneShot(var3.isOneShot());

         for(int var5 = 0; var5 < var4; ++var5) {
            var1 = this.tileify(var3.getFrame(var5), true);
            var1.setLevel(10000);
            ((AnimationDrawable)var2).addFrame(var1, var3.getDuration(var5));
         }

         ((AnimationDrawable)var2).setLevel(10000);
      }

      return (Drawable)var2;
   }

   Bitmap getSampleTime() {
      return this.mSampleTile;
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, TINT_ATTRS, var2, 0);
      Drawable var3 = var4.getDrawableIfKnown(0);
      if (var3 != null) {
         this.mView.setIndeterminateDrawable(this.tileifyIndeterminate(var3));
      }

      var3 = var4.getDrawableIfKnown(1);
      if (var3 != null) {
         this.mView.setProgressDrawable(this.tileify(var3, false));
      }

      var4.recycle();
   }
}
