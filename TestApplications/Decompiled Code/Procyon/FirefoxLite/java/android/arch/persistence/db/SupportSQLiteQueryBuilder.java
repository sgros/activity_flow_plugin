// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db;

import java.util.regex.Pattern;

public final class SupportSQLiteQueryBuilder
{
    private static final Pattern sLimitPattern;
    private Object[] mBindArgs;
    private String[] mColumns;
    private boolean mDistinct;
    private String mGroupBy;
    private String mHaving;
    private String mLimit;
    private String mOrderBy;
    private String mSelection;
    private final String mTable;
    
    static {
        sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
    }
    
    private SupportSQLiteQueryBuilder(final String mTable) {
        this.mDistinct = false;
        this.mColumns = null;
        this.mGroupBy = null;
        this.mHaving = null;
        this.mOrderBy = null;
        this.mLimit = null;
        this.mTable = mTable;
    }
    
    private static void appendClause(final StringBuilder sb, final String str, final String str2) {
        if (!isEmpty(str2)) {
            sb.append(str);
            sb.append(str2);
        }
    }
    
    private static void appendColumns(final StringBuilder sb, final String[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final String str = array[i];
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(str);
        }
        sb.append(' ');
    }
    
    public static SupportSQLiteQueryBuilder builder(final String s) {
        return new SupportSQLiteQueryBuilder(s);
    }
    
    private static boolean isEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    public SupportSQLiteQueryBuilder columns(final String[] mColumns) {
        this.mColumns = mColumns;
        return this;
    }
    
    public SupportSQLiteQuery create() {
        if (isEmpty(this.mGroupBy) && !isEmpty(this.mHaving)) {
            throw new IllegalArgumentException("HAVING clauses are only permitted when using a groupBy clause");
        }
        final StringBuilder sb = new StringBuilder(120);
        sb.append("SELECT ");
        if (this.mDistinct) {
            sb.append("DISTINCT ");
        }
        if (this.mColumns != null && this.mColumns.length != 0) {
            appendColumns(sb, this.mColumns);
        }
        else {
            sb.append(" * ");
        }
        sb.append(" FROM ");
        sb.append(this.mTable);
        appendClause(sb, " WHERE ", this.mSelection);
        appendClause(sb, " GROUP BY ", this.mGroupBy);
        appendClause(sb, " HAVING ", this.mHaving);
        appendClause(sb, " ORDER BY ", this.mOrderBy);
        appendClause(sb, " LIMIT ", this.mLimit);
        return new SimpleSQLiteQuery(sb.toString(), this.mBindArgs);
    }
    
    public SupportSQLiteQueryBuilder groupBy(final String mGroupBy) {
        this.mGroupBy = mGroupBy;
        return this;
    }
    
    public SupportSQLiteQueryBuilder having(final String mHaving) {
        this.mHaving = mHaving;
        return this;
    }
    
    public SupportSQLiteQueryBuilder limit(final String mLimit) {
        if (!isEmpty(mLimit) && !SupportSQLiteQueryBuilder.sLimitPattern.matcher(mLimit).matches()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid LIMIT clauses:");
            sb.append(mLimit);
            throw new IllegalArgumentException(sb.toString());
        }
        this.mLimit = mLimit;
        return this;
    }
    
    public SupportSQLiteQueryBuilder orderBy(final String mOrderBy) {
        this.mOrderBy = mOrderBy;
        return this;
    }
    
    public SupportSQLiteQueryBuilder selection(final String mSelection, final Object[] mBindArgs) {
        this.mSelection = mSelection;
        this.mBindArgs = mBindArgs;
        return this;
    }
}
