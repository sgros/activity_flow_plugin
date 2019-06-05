package android.support.v4.content.res;

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

      int var5;
      byte var8;
      do {
         var5 = var3.next();
         var8 = 1;
      } while(var5 != 2 && var5 != 1);

      if (var5 != 2) {
         throw new XmlPullParserException("No start tag found");
      } else {
         String var6;
         label29: {
            var6 = var3.getName();
            var5 = var6.hashCode();
            if (var5 != 89650992) {
               if (var5 == 1191572447 && var6.equals("selector")) {
                  var8 = 0;
                  break label29;
               }
            } else if (var6.equals("gradient")) {
               break label29;
            }

            var8 = -1;
         }

         switch(var8) {
         case 0:
            return from(ColorStateListInflaterCompat.createFromXmlInner(var0, var3, var4, var2));
         case 1:
            return from(GradientColorInflaterCompat.createFromXmlInner(var0, var3, var4, var2));
         default:
            StringBuilder var7 = new StringBuilder();
            var7.append(var3.getPositionDescription());
            var7.append(": unsupported complex color tag ");
            var7.append(var6);
            throw new XmlPullParserException(var7.toString());
         }
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
      boolean var1;
      if (this.mShader == null && this.mColorStateList != null && this.mColorStateList.isStateful()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean onStateChanged(int[] var1) {
      boolean var3;
      if (this.isStateful()) {
         int var2 = this.mColorStateList.getColorForState(var1, this.mColorStateList.getDefaultColor());
         if (var2 != this.mColor) {
            var3 = true;
            this.mColor = var2;
            return var3;
         }
      }

      var3 = false;
      return var3;
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
