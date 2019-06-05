// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.util;

import org.mozilla.logger.Logger;
import org.mozilla.focus.utils.AppConstants;

public class LoggerWrapper
{
    public static void throwOrWarn(final String s, final String s2) {
        throwOrWarn(s, s2, null);
    }
    
    public static void throwOrWarn(final String s, final String s2, final RuntimeException ex) {
        Logger.throwOrWarn(AppConstants.isReleaseBuild(), s, s2, ex);
    }
}
