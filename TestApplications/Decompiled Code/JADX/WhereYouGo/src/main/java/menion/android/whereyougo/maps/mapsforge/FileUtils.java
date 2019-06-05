package menion.android.whereyougo.maps.mapsforge;

import android.content.res.Resources;
import java.text.DecimalFormat;
import menion.android.whereyougo.C0254R;

final class FileUtils {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00 ");
    private static final double ONE_GIGABYTE = 1.0E9d;
    private static final double ONE_KILOBYTE = 1000.0d;
    private static final double ONE_MEGABYTE = 1000000.0d;

    private FileUtils() {
        throw new IllegalStateException();
    }

    static String formatFileSize(long fileSize, Resources resources) {
        if (fileSize < 0) {
            throw new IllegalArgumentException("invalid file size: " + fileSize);
        } else if (fileSize < 1000) {
            if (fileSize == 1) {
                return "1 " + resources.getString(C0254R.string.file_size_byte);
            }
            return fileSize + " " + resources.getString(C0254R.string.file_size_bytes);
        } else if (((double) fileSize) < ONE_MEGABYTE) {
            return DECIMAL_FORMAT.format(((double) fileSize) / 1000.0d) + resources.getString(C0254R.string.file_size_kb);
        } else {
            if (((double) fileSize) < ONE_GIGABYTE) {
                return DECIMAL_FORMAT.format(((double) fileSize) / ONE_MEGABYTE) + resources.getString(C0254R.string.file_size_mb);
            }
            return DECIMAL_FORMAT.format(((double) fileSize) / ONE_GIGABYTE) + resources.getString(C0254R.string.file_size_gb);
        }
    }
}
