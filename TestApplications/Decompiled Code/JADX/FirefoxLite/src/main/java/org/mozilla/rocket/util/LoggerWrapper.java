package org.mozilla.rocket.util;

import org.mozilla.focus.utils.AppConstants;
import org.mozilla.logger.Logger;

public class LoggerWrapper {
    public static void throwOrWarn(String str, String str2) {
        throwOrWarn(str, str2, null);
    }

    public static void throwOrWarn(String str, String str2, RuntimeException runtimeException) {
        Logger.throwOrWarn(AppConstants.isReleaseBuild(), str, str2, runtimeException);
    }
}
