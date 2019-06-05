// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import android.util.Log;
import net.sqlcipher.Cursor;
import java.util.Iterator;
import java.util.Set;
import net.sqlcipher.DatabaseUtils;
import android.text.TextUtils;
import java.util.Map;
import java.util.regex.Pattern;

public class SQLiteQueryBuilder
{
    private static final String TAG = "SQLiteQueryBuilder";
    private static final Pattern sLimitPattern;
    private boolean mDistinct;
    private SQLiteDatabase.CursorFactory mFactory;
    private Map<String, String> mProjectionMap;
    private boolean mStrictProjectionMap;
    private String mTables;
    private StringBuilder mWhereClause;
    
    static {
        sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
    }
    
    public SQLiteQueryBuilder() {
        this.mProjectionMap = null;
        this.mTables = "";
        this.mWhereClause = null;
        this.mDistinct = false;
        this.mFactory = null;
    }
    
    private static void appendClause(final StringBuilder sb, final String str, final String str2) {
        if (!TextUtils.isEmpty((CharSequence)str2)) {
            sb.append(str);
            sb.append(str2);
        }
    }
    
    private static void appendClauseEscapeClause(final StringBuilder sb, final String str, final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            sb.append(str);
            DatabaseUtils.appendEscapedSQLString(sb, s);
        }
    }
    
    public static void appendColumns(final StringBuilder sb, final String[] array) {
        for (int i = 0; i < array.length; ++i) {
            final String str = array[i];
            if (str != null) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(str);
            }
        }
        sb.append(' ');
    }
    
    public static String buildQueryString(final boolean b, final String str, final String[] array, final String s, final String s2, final String s3, final String s4, final String s5) {
        if (TextUtils.isEmpty((CharSequence)s2) && !TextUtils.isEmpty((CharSequence)s3)) {
            throw new IllegalArgumentException("HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!TextUtils.isEmpty((CharSequence)s5) && !SQLiteQueryBuilder.sLimitPattern.matcher(s5).matches()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid LIMIT clauses:");
            sb.append(s5);
            throw new IllegalArgumentException(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder(120);
        sb2.append("SELECT ");
        if (b) {
            sb2.append("DISTINCT ");
        }
        if (array != null && array.length != 0) {
            appendColumns(sb2, array);
        }
        else {
            sb2.append("* ");
        }
        sb2.append("FROM ");
        sb2.append(str);
        appendClause(sb2, " WHERE ", s);
        appendClause(sb2, " GROUP BY ", s2);
        appendClause(sb2, " HAVING ", s3);
        appendClause(sb2, " ORDER BY ", s4);
        appendClause(sb2, " LIMIT ", s5);
        return sb2.toString();
    }
    
    private String[] computeProjection(String[] array) {
        final int n = 0;
        int i = 0;
        if (array != null && array.length > 0) {
            if (this.mProjectionMap != null) {
                final String[] array2 = new String[array.length];
                while (i < array.length) {
                    final String s = array[i];
                    final String s2 = this.mProjectionMap.get(s);
                    if (s2 != null) {
                        array2[i] = s2;
                    }
                    else {
                        if (this.mStrictProjectionMap || (!s.contains(" AS ") && !s.contains(" as "))) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Invalid column ");
                            sb.append(array[i]);
                            throw new IllegalArgumentException(sb.toString());
                        }
                        array2[i] = s;
                    }
                    ++i;
                }
                return array2;
            }
            return array;
        }
        else {
            if (this.mProjectionMap != null) {
                final Set<Map.Entry<String, String>> entrySet = this.mProjectionMap.entrySet();
                array = new String[entrySet.size()];
                final Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
                int n2 = n;
                while (iterator.hasNext()) {
                    final Map.Entry<String, String> entry = iterator.next();
                    if (entry.getKey().equals("_count")) {
                        continue;
                    }
                    array[n2] = entry.getValue();
                    ++n2;
                }
                return array;
            }
            return null;
        }
    }
    
    public void appendWhere(final CharSequence s) {
        if (this.mWhereClause == null) {
            this.mWhereClause = new StringBuilder(s.length() + 16);
        }
        if (this.mWhereClause.length() == 0) {
            this.mWhereClause.append('(');
        }
        this.mWhereClause.append(s);
    }
    
    public void appendWhereEscapeString(final String s) {
        if (this.mWhereClause == null) {
            this.mWhereClause = new StringBuilder(s.length() + 16);
        }
        if (this.mWhereClause.length() == 0) {
            this.mWhereClause.append('(');
        }
        DatabaseUtils.appendEscapedSQLString(this.mWhereClause, s);
    }
    
    public String buildQuery(final String[] array, final String str, String[] computeProjection, final String s, final String s2, final String s3, final String s4) {
        computeProjection = this.computeProjection(array);
        final StringBuilder sb = new StringBuilder();
        final boolean b = this.mWhereClause != null && this.mWhereClause.length() > 0;
        if (b) {
            sb.append(this.mWhereClause.toString());
            sb.append(')');
        }
        if (str != null && str.length() > 0) {
            if (b) {
                sb.append(" AND ");
            }
            sb.append('(');
            sb.append(str);
            sb.append(')');
        }
        return buildQueryString(this.mDistinct, this.mTables, computeProjection, sb.toString(), s, s2, s3, s4);
    }
    
    public String buildUnionQuery(final String[] array, final String s, final String s2) {
        final StringBuilder sb = new StringBuilder(128);
        final int length = array.length;
        String str;
        if (this.mDistinct) {
            str = " UNION ";
        }
        else {
            str = " UNION ALL ";
        }
        for (int i = 0; i < length; ++i) {
            if (i > 0) {
                sb.append(str);
            }
            sb.append(array[i]);
        }
        appendClause(sb, " ORDER BY ", s);
        appendClause(sb, " LIMIT ", s2);
        return sb.toString();
    }
    
    public String buildUnionSubQuery(final String s, final String[] array, final Set<String> set, final int n, final String str, final String s2, final String[] array2, final String s3, final String s4) {
        int i = 0;
        final int length = array.length;
        final String[] array3 = new String[length];
        while (i < length) {
            final String str2 = array[i];
            if (str2.equals(s)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("'");
                sb.append(str);
                sb.append("' AS ");
                sb.append(s);
                array3[i] = sb.toString();
            }
            else if (i > n && !set.contains(str2)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("NULL AS ");
                sb2.append(str2);
                array3[i] = sb2.toString();
            }
            else {
                array3[i] = str2;
            }
            ++i;
        }
        return this.buildQuery(array3, s2, array2, s3, s4, null, null);
    }
    
    public String getTables() {
        return this.mTables;
    }
    
    public Cursor query(final SQLiteDatabase sqLiteDatabase, final String[] array, final String s, final String[] array2, final String s2, final String s3, final String s4) {
        return this.query(sqLiteDatabase, array, s, array2, s2, s3, s4, null);
    }
    
    public Cursor query(final SQLiteDatabase sqLiteDatabase, final String[] array, final String s, final String[] array2, final String s2, final String s3, final String s4, final String s5) {
        if (this.mTables == null) {
            return null;
        }
        final String buildQuery = this.buildQuery(array, s, array2, s2, s3, s4, s5);
        if (Log.isLoggable("SQLiteQueryBuilder", 3)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Performing query: ");
            sb.append(buildQuery);
            Log.d("SQLiteQueryBuilder", sb.toString());
        }
        return sqLiteDatabase.rawQueryWithFactory(this.mFactory, buildQuery, array2, SQLiteDatabase.findEditTable(this.mTables));
    }
    
    public void setCursorFactory(final SQLiteDatabase.CursorFactory mFactory) {
        this.mFactory = mFactory;
    }
    
    public void setDistinct(final boolean mDistinct) {
        this.mDistinct = mDistinct;
    }
    
    public void setProjectionMap(final Map<String, String> mProjectionMap) {
        this.mProjectionMap = mProjectionMap;
    }
    
    public void setStrictProjectionMap(final boolean mStrictProjectionMap) {
        this.mStrictProjectionMap = mStrictProjectionMap;
    }
    
    public void setTables(final String mTables) {
        this.mTables = mTables;
    }
}
