package androidx.work.impl.utils;

import android.content.ComponentName;
import android.content.Context;
import androidx.work.Logger;

public class PackageManagerHelper {
    private static final String TAG = Logger.tagWithPrefix("PackageManagerHelper");

    public static void setComponentEnabled(Context context, Class cls, boolean z) {
        String str;
        try {
            context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, cls.getName()), z ? 1 : 2, 1);
            Logger logger = Logger.get();
            String str2 = TAG;
            str = "%s %s";
            Object[] objArr = new Object[2];
            objArr[0] = cls.getName();
            objArr[1] = z ? "enabled" : "disabled";
            logger.debug(str2, String.format(str, objArr), new Throwable[0]);
        } catch (Exception e) {
            Logger logger2 = Logger.get();
            str = TAG;
            String str3 = "%s could not be %s";
            Object[] objArr2 = new Object[2];
            objArr2[0] = cls.getName();
            objArr2[1] = z ? "enabled" : "disabled";
            logger2.debug(str, String.format(str3, objArr2), e);
        }
    }
}
