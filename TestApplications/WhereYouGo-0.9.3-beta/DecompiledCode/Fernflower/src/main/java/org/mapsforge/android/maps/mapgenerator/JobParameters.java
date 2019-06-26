package org.mapsforge.android.maps.mapgenerator;

import java.io.Serializable;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

public class JobParameters implements Serializable {
   private static final long serialVersionUID = 1L;
   private final int hashCodeValue;
   public final XmlRenderTheme jobTheme;
   public final float textScale;

   public JobParameters(XmlRenderTheme var1, float var2) {
      this.jobTheme = var1;
      this.textScale = var2;
      this.hashCodeValue = this.calculateHashCode();
   }

   private int calculateHashCode() {
      int var1;
      if (this.jobTheme == null) {
         var1 = 0;
      } else {
         var1 = this.jobTheme.hashCode();
      }

      return (var1 + 217) * 31 + Float.floatToIntBits(this.textScale);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof JobParameters)) {
            var2 = false;
         } else {
            JobParameters var3 = (JobParameters)var1;
            if (this.jobTheme == null) {
               if (var3.jobTheme != null) {
                  var2 = false;
                  return var2;
               }
            } else if (!this.jobTheme.equals(var3.jobTheme)) {
               var2 = false;
               return var2;
            }

            if (Float.floatToIntBits(this.textScale) != Float.floatToIntBits(var3.textScale)) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      return this.hashCodeValue;
   }
}
