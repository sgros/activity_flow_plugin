// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.internal;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;

public class SqlUtils
{
    private static final char[] HEX_ARRAY;
    
    static {
        HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    }
    
    public static StringBuilder appendColumn(final StringBuilder sb, final String str) {
        sb.append('\"');
        sb.append(str);
        sb.append('\"');
        return sb;
    }
    
    public static StringBuilder appendColumn(final StringBuilder sb, final String str, final String str2) {
        sb.append(str);
        sb.append(".\"");
        sb.append(str2);
        sb.append('\"');
        return sb;
    }
    
    public static StringBuilder appendColumns(final StringBuilder sb, final String s, final String[] array) {
        for (int i = 0, length = array.length; i < length; ++i) {
            appendColumn(sb, s, array[i]);
            if (i < length - 1) {
                sb.append(',');
            }
        }
        return sb;
    }
    
    public static StringBuilder appendColumns(final StringBuilder sb, final String[] array) {
        for (int i = 0, length = array.length; i < length; ++i) {
            sb.append('\"');
            sb.append(array[i]);
            sb.append('\"');
            if (i < length - 1) {
                sb.append(',');
            }
        }
        return sb;
    }
    
    public static StringBuilder appendColumnsEqValue(final StringBuilder sb, final String s, final String[] array) {
        for (int i = 0; i < array.length; ++i) {
            appendColumn(sb, s, array[i]).append("=?");
            if (i < array.length - 1) {
                sb.append(',');
            }
        }
        return sb;
    }
    
    public static StringBuilder appendColumnsEqualPlaceholders(final StringBuilder sb, final String[] array) {
        for (int i = 0; i < array.length; ++i) {
            appendColumn(sb, array[i]).append("=?");
            if (i < array.length - 1) {
                sb.append(',');
            }
        }
        return sb;
    }
    
    public static StringBuilder appendPlaceholders(final StringBuilder sb, final int n) {
        for (int i = 0; i < n; ++i) {
            if (i < n - 1) {
                sb.append("?,");
            }
            else {
                sb.append('?');
            }
        }
        return sb;
    }
    
    public static StringBuilder appendProperty(final StringBuilder sb, final String str, final Property property) {
        if (str != null) {
            sb.append(str);
            sb.append('.');
        }
        sb.append('\"');
        sb.append(property.columnName);
        sb.append('\"');
        return sb;
    }
    
    public static String createSqlCount(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM \"");
        sb.append(str);
        sb.append('\"');
        return sb.toString();
    }
    
    public static String createSqlDelete(String string, final String[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append('\"');
        sb.append(string);
        sb.append('\"');
        string = sb.toString();
        final StringBuilder sb2 = new StringBuilder("DELETE FROM ");
        sb2.append(string);
        if (array != null && array.length > 0) {
            sb2.append(" WHERE ");
            appendColumnsEqValue(sb2, string, array);
        }
        return sb2.toString();
    }
    
    public static String createSqlInsert(final String str, final String str2, final String[] array) {
        final StringBuilder sb = new StringBuilder(str);
        sb.append('\"');
        sb.append(str2);
        sb.append('\"');
        sb.append(" (");
        appendColumns(sb, array);
        sb.append(") VALUES (");
        appendPlaceholders(sb, array.length);
        sb.append(')');
        return sb.toString();
    }
    
    public static String createSqlSelect(final String str, final String str2, final String[] array, final boolean b) {
        if (str2 != null && str2.length() >= 0) {
            String str3;
            if (b) {
                str3 = "SELECT DISTINCT ";
            }
            else {
                str3 = "SELECT ";
            }
            final StringBuilder sb = new StringBuilder(str3);
            appendColumns(sb, str2, array).append(" FROM ");
            sb.append('\"');
            sb.append(str);
            sb.append('\"');
            sb.append(' ');
            sb.append(str2);
            sb.append(' ');
            return sb.toString();
        }
        throw new DaoException("Table alias required");
    }
    
    public static String createSqlSelectCountStar(final String str, final String str2) {
        final StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM ");
        sb.append('\"');
        sb.append(str);
        sb.append('\"');
        sb.append(' ');
        if (str2 != null) {
            sb.append(str2);
            sb.append(' ');
        }
        return sb.toString();
    }
    
    public static String createSqlUpdate(String string, final String[] array, final String[] array2) {
        final StringBuilder sb = new StringBuilder();
        sb.append('\"');
        sb.append(string);
        sb.append('\"');
        string = sb.toString();
        final StringBuilder sb2 = new StringBuilder("UPDATE ");
        sb2.append(string);
        sb2.append(" SET ");
        appendColumnsEqualPlaceholders(sb2, array);
        sb2.append(" WHERE ");
        appendColumnsEqValue(sb2, string, array2);
        return sb2.toString();
    }
    
    public static String escapeBlobArgument(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append("X'");
        sb.append(toHex(array));
        sb.append('\'');
        return sb.toString();
    }
    
    public static String toHex(final byte[] array) {
        int i = 0;
        final char[] value = new char[array.length * 2];
        while (i < array.length) {
            final int n = array[i] & 0xFF;
            final int n2 = i * 2;
            value[n2] = SqlUtils.HEX_ARRAY[n >>> 4];
            value[n2 + 1] = SqlUtils.HEX_ARRAY[n & 0xF];
            ++i;
        }
        return new String(value);
    }
}
