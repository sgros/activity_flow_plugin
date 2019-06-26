package menion.android.whereyougo.maps.mapsforge;

import android.content.res.Resources;
import java.text.DecimalFormat;

final class FileUtils {
   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00 ");
   private static final double ONE_GIGABYTE = 1.0E9D;
   private static final double ONE_KILOBYTE = 1000.0D;
   private static final double ONE_MEGABYTE = 1000000.0D;

   private FileUtils() {
      throw new IllegalStateException();
   }

   static String formatFileSize(long var0, Resources var2) {
      if (var0 < 0L) {
         throw new IllegalArgumentException("invalid file size: " + var0);
      } else {
         String var3;
         if (var0 < 1000L) {
            if (var0 == 1L) {
               var3 = "1 " + var2.getString(2131165417);
            } else {
               var3 = var0 + " " + var2.getString(2131165418);
            }
         } else if ((double)var0 < 1000000.0D) {
            var3 = DECIMAL_FORMAT.format((double)var0 / 1000.0D) + var2.getString(2131165420);
         } else if ((double)var0 < 1.0E9D) {
            var3 = DECIMAL_FORMAT.format((double)var0 / 1000000.0D) + var2.getString(2131165421);
         } else {
            var3 = DECIMAL_FORMAT.format((double)var0 / 1.0E9D) + var2.getString(2131165419);
         }

         return var3;
      }
   }
}
