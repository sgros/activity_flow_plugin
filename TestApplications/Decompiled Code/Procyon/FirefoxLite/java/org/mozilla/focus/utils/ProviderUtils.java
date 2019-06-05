// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

public class ProviderUtils
{
    public static String getLimitParam(final String str, String string) {
        if (string == null) {
            string = null;
        }
        else if (str != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(",");
            sb.append(string);
            string = sb.toString();
        }
        return string;
    }
}
