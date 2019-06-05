// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge;

import android.content.res.Resources;
import java.text.DecimalFormat;

final class FileUtils
{
    private static final DecimalFormat DECIMAL_FORMAT;
    private static final double ONE_GIGABYTE = 1.0E9;
    private static final double ONE_KILOBYTE = 1000.0;
    private static final double ONE_MEGABYTE = 1000000.0;
    
    static {
        DECIMAL_FORMAT = new DecimalFormat("#.00 ");
    }
    
    private FileUtils() {
        throw new IllegalStateException();
    }
    
    static String formatFileSize(final long n, final Resources resources) {
        if (n < 0L) {
            throw new IllegalArgumentException("invalid file size: " + n);
        }
        String s;
        if (n < 1000L) {
            if (n == 1L) {
                s = "1 " + resources.getString(2131165417);
            }
            else {
                s = n + " " + resources.getString(2131165418);
            }
        }
        else if (n < 1000000.0) {
            s = FileUtils.DECIMAL_FORMAT.format(n / 1000.0) + resources.getString(2131165420);
        }
        else if (n < 1.0E9) {
            s = FileUtils.DECIMAL_FORMAT.format(n / 1000000.0) + resources.getString(2131165421);
        }
        else {
            s = FileUtils.DECIMAL_FORMAT.format(n / 1.0E9) + resources.getString(2131165419);
        }
        return s;
    }
}
