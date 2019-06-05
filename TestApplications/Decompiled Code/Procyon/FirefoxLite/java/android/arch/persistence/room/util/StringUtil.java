// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room.util;

public class StringUtil
{
    public static final String[] EMPTY_STRING_ARRAY;
    
    static {
        EMPTY_STRING_ARRAY = new String[0];
    }
    
    public static void appendPlaceholders(final StringBuilder sb, final int n) {
        for (int i = 0; i < n; ++i) {
            sb.append("?");
            if (i < n - 1) {
                sb.append(",");
            }
        }
    }
    
    public static StringBuilder newStringBuilder() {
        return new StringBuilder();
    }
}
