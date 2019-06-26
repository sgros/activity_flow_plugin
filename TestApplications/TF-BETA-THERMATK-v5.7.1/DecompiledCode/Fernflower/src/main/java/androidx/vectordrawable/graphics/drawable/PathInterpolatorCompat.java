package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import org.xmlpull.v1.XmlPullParser;

public class PathInterpolatorCompat implements Interpolator {
   private float[] mX;
   private float[] mY;

   public PathInterpolatorCompat(Context var1, AttributeSet var2, XmlPullParser var3) {
      this(var1.getResources(), var1.getTheme(), var2, var3);
   }

   public PathInterpolatorCompat(Resources var1, Theme var2, AttributeSet var3, XmlPullParser var4) {
      TypedArray var5 = TypedArrayUtils.obtainAttributes(var1, var2, var3, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
      this.parseInterpolatorFromTypeArray(var5, var4);
      var5.recycle();
   }

   private void initCubic(float var1, float var2, float var3, float var4) {
      Path var5 = new Path();
      var5.moveTo(0.0F, 0.0F);
      var5.cubicTo(var1, var2, var3, var4, 1.0F, 1.0F);
      this.initPath(var5);
   }

   private void initPath(Path var1) {
      int var2 = 0;
      PathMeasure var8 = new PathMeasure(var1, false);
      float var3 = var8.getLength();
      int var4 = Math.min(3000, (int)(var3 / 0.002F) + 1);
      StringBuilder var10;
      if (var4 <= 0) {
         var10 = new StringBuilder();
         var10.append("The Path has a invalid length ");
         var10.append(var3);
         throw new IllegalArgumentException(var10.toString());
      } else {
         this.mX = new float[var4];
         this.mY = new float[var4];
         float[] var5 = new float[2];

         int var6;
         for(var6 = 0; var6 < var4; ++var6) {
            var8.getPosTan((float)var6 * var3 / (float)(var4 - 1), var5, (float[])null);
            this.mX[var6] = var5[0];
            this.mY[var6] = var5[1];
         }

         if ((double)Math.abs(this.mX[0]) <= 1.0E-5D && (double)Math.abs(this.mY[0]) <= 1.0E-5D) {
            var5 = this.mX;
            var6 = var4 - 1;
            if ((double)Math.abs(var5[var6] - 1.0F) <= 1.0E-5D && (double)Math.abs(this.mY[var6] - 1.0F) <= 1.0E-5D) {
               var6 = 0;

               for(var3 = 0.0F; var2 < var4; ++var6) {
                  var5 = this.mX;
                  float var7 = var5[var6];
                  if (var7 < var3) {
                     var10 = new StringBuilder();
                     var10.append("The Path cannot loop back on itself, x :");
                     var10.append(var7);
                     throw new IllegalArgumentException(var10.toString());
                  }

                  var5[var2] = var7;
                  ++var2;
                  var3 = var7;
               }

               if (!var8.nextContour()) {
                  return;
               }

               throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
            }
         }

         StringBuilder var11 = new StringBuilder();
         var11.append("The Path must start at (0,0) and end at (1,1) start: ");
         var11.append(this.mX[0]);
         var11.append(",");
         var11.append(this.mY[0]);
         var11.append(" end:");
         float[] var9 = this.mX;
         var6 = var4 - 1;
         var11.append(var9[var6]);
         var11.append(",");
         var11.append(this.mY[var6]);
         throw new IllegalArgumentException(var11.toString());
      }
   }

   private void initQuad(float var1, float var2) {
      Path var3 = new Path();
      var3.moveTo(0.0F, 0.0F);
      var3.quadTo(var1, var2, 1.0F, 1.0F);
      this.initPath(var3);
   }

   private void parseInterpolatorFromTypeArray(TypedArray var1, XmlPullParser var2) {
      if (TypedArrayUtils.hasAttribute(var2, "pathData")) {
         String var6 = TypedArrayUtils.getNamedString(var1, var2, "pathData", 4);
         Path var7 = PathParser.createPathFromPathData(var6);
         if (var7 == null) {
            StringBuilder var8 = new StringBuilder();
            var8.append("The path is null, which is created from ");
            var8.append(var6);
            throw new InflateException(var8.toString());
         }

         this.initPath(var7);
      } else {
         if (!TypedArrayUtils.hasAttribute(var2, "controlX1")) {
            throw new InflateException("pathInterpolator requires the controlX1 attribute");
         }

         if (!TypedArrayUtils.hasAttribute(var2, "controlY1")) {
            throw new InflateException("pathInterpolator requires the controlY1 attribute");
         }

         float var3 = TypedArrayUtils.getNamedFloat(var1, var2, "controlX1", 0, 0.0F);
         float var4 = TypedArrayUtils.getNamedFloat(var1, var2, "controlY1", 1, 0.0F);
         boolean var5 = TypedArrayUtils.hasAttribute(var2, "controlX2");
         if (var5 != TypedArrayUtils.hasAttribute(var2, "controlY2")) {
            throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
         }

         if (!var5) {
            this.initQuad(var3, var4);
         } else {
            this.initCubic(var3, var4, TypedArrayUtils.getNamedFloat(var1, var2, "controlX2", 2, 0.0F), TypedArrayUtils.getNamedFloat(var1, var2, "controlY2", 3, 0.0F));
         }
      }

   }

   public float getInterpolation(float var1) {
      if (var1 <= 0.0F) {
         return 0.0F;
      } else if (var1 >= 1.0F) {
         return 1.0F;
      } else {
         int var2 = 0;
         int var3 = this.mX.length - 1;

         while(var3 - var2 > 1) {
            int var4 = (var2 + var3) / 2;
            if (var1 < this.mX[var4]) {
               var3 = var4;
            } else {
               var2 = var4;
            }
         }

         float[] var5 = this.mX;
         float var6 = var5[var3] - var5[var2];
         if (var6 == 0.0F) {
            return this.mY[var2];
         } else {
            var6 = (var1 - var5[var2]) / var6;
            var5 = this.mY;
            var1 = var5[var2];
            return var1 + var6 * (var5[var3] - var1);
         }
      }
   }
}
