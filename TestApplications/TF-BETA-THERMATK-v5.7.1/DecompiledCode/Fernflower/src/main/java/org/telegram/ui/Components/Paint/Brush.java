package org.telegram.ui.Components.Paint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import org.telegram.messenger.ApplicationLoader;

public interface Brush {
   float getAlpha();

   float getAngle();

   float getScale();

   float getSpacing();

   Bitmap getStamp();

   boolean isLightSaber();

   public static class Elliptical implements Brush {
      public float getAlpha() {
         return 0.3F;
      }

      public float getAngle() {
         return (float)Math.toRadians(125.0D);
      }

      public float getScale() {
         return 1.5F;
      }

      public float getSpacing() {
         return 0.04F;
      }

      public Bitmap getStamp() {
         Options var1 = new Options();
         var1.inScaled = false;
         return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), 2131165727, var1);
      }

      public boolean isLightSaber() {
         return false;
      }
   }

   public static class Neon implements Brush {
      public float getAlpha() {
         return 0.7F;
      }

      public float getAngle() {
         return 0.0F;
      }

      public float getScale() {
         return 1.45F;
      }

      public float getSpacing() {
         return 0.07F;
      }

      public Bitmap getStamp() {
         Options var1 = new Options();
         var1.inScaled = false;
         return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), 2131165729, var1);
      }

      public boolean isLightSaber() {
         return true;
      }
   }

   public static class Radial implements Brush {
      public float getAlpha() {
         return 0.85F;
      }

      public float getAngle() {
         return 0.0F;
      }

      public float getScale() {
         return 1.0F;
      }

      public float getSpacing() {
         return 0.15F;
      }

      public Bitmap getStamp() {
         Options var1 = new Options();
         var1.inScaled = false;
         return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), 2131165731, var1);
      }

      public boolean isLightSaber() {
         return false;
      }
   }
}
