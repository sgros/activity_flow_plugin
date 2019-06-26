package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.Theme;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public final class ComplexColorCompat {
   private int mColor;
   private final ColorStateList mColorStateList;
   private final Shader mShader;

   private ComplexColorCompat(Shader var1, ColorStateList var2, int var3) {
      this.mShader = var1;
      this.mColorStateList = var2;
      this.mColor = var3;
   }

   private static ComplexColorCompat createFromXml(Resources var0, int var1, Theme var2) throws IOException, XmlPullParserException {
      XmlResourceParser var3 = var0.getXml(var1);
      AttributeSet var4 = Xml.asAttributeSet(var3);

      do {
         var1 = var3.next();
      } while(var1 != 2 && var1 != 1);

      if (var1 == 2) {
         String var5 = var3.getName();
         byte var8 = -1;
         int var6 = var5.hashCode();
         if (var6 != 89650992) {
            if (var6 == 1191572447 && var5.equals("selector")) {
               var8 = 0;
            }
         } else if (var5.equals("gradient")) {
            var8 = 1;
         }

         if (var8 != 0) {
            if (var8 == 1) {
               return from(GradientColorInflaterCompat.createFromXmlInner(var0, var3, var4, var2));
            } else {
               StringBuilder var7 = new StringBuilder();
               var7.append(var3.getPositionDescription());
               var7.append(": unsupported complex color tag ");
               var7.append(var5);
               throw new XmlPullParserException(var7.toString());
            }
         } else {
            return from(ColorStateListInflaterCompat.createFromXmlInner(var0, var3, var4, var2));
         }
      } else {
         throw new XmlPullParserException("No start tag found");
      }
   }

   static ComplexColorCompat from(int var0) {
      return new ComplexColorCompat((Shader)null, (ColorStateList)null, var0);
   }

   static ComplexColorCompat from(ColorStateList var0) {
      return new ComplexColorCompat((Shader)null, var0, var0.getDefaultColor());
   }

   static ComplexColorCompat from(Shader var0) {
      return new ComplexColorCompat(var0, (ColorStateList)null, 0);
   }

   public static ComplexColorCompat inflate(Resources var0, int var1, Theme var2) {
      try {
         ComplexColorCompat var4 = createFromXml(var0, var1, var2);
         return var4;
      } catch (Exception var3) {
         Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", var3);
         return null;
      }
   }

   public int getColor() {
      return this.mColor;
   }

   public Shader getShader() {
      return this.mShader;
   }

   public boolean isGradient() {
      boolean var1;
      if (this.mShader != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isStateful() {
      boolean var2;
      if (this.mShader == null) {
         ColorStateList var1 = this.mColorStateList;
         if (var1 != null && var1.isStateful()) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean onStateChanged(int[] var1) {
      boolean var4;
      if (this.isStateful()) {
         ColorStateList var2 = this.mColorStateList;
         int var3 = var2.getColorForState(var1, var2.getDefaultColor());
         if (var3 != this.mColor) {
            var4 = true;
            this.mColor = var3;
            return var4;
         }
      }

      var4 = false;
      return var4;
   }

   public void setColor(int var1) {
      this.mColor = var1;
   }

   public boolean willDraw() {
      boolean var1;
      if (!this.isGradient() && this.mColor == 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }
}
