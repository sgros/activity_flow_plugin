package locus.api.android.utils;

import android.database.Cursor;
import locus.api.utils.Logger;

public class Utils extends locus.api.utils.Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception e) {
                Logger.logE(TAG, "closeQuietly(" + cursor + "), e:" + e);
                e.printStackTrace();
            }
        }
    }
}
