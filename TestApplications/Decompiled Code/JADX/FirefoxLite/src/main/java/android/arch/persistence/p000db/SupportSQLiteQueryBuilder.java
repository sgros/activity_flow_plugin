package android.arch.persistence.p000db;

import java.util.regex.Pattern;

/* renamed from: android.arch.persistence.db.SupportSQLiteQueryBuilder */
public final class SupportSQLiteQueryBuilder {
    private static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
    private Object[] mBindArgs;
    private String[] mColumns = null;
    private boolean mDistinct = false;
    private String mGroupBy = null;
    private String mHaving = null;
    private String mLimit = null;
    private String mOrderBy = null;
    private String mSelection;
    private final String mTable;

    public static SupportSQLiteQueryBuilder builder(String str) {
        return new SupportSQLiteQueryBuilder(str);
    }

    private SupportSQLiteQueryBuilder(String str) {
        this.mTable = str;
    }

    public SupportSQLiteQueryBuilder columns(String[] strArr) {
        this.mColumns = strArr;
        return this;
    }

    public SupportSQLiteQueryBuilder selection(String str, Object[] objArr) {
        this.mSelection = str;
        this.mBindArgs = objArr;
        return this;
    }

    public SupportSQLiteQueryBuilder groupBy(String str) {
        this.mGroupBy = str;
        return this;
    }

    public SupportSQLiteQueryBuilder having(String str) {
        this.mHaving = str;
        return this;
    }

    public SupportSQLiteQueryBuilder orderBy(String str) {
        this.mOrderBy = str;
        return this;
    }

    public SupportSQLiteQueryBuilder limit(String str) {
        if (SupportSQLiteQueryBuilder.isEmpty(str) || sLimitPattern.matcher(str).matches()) {
            this.mLimit = str;
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("invalid LIMIT clauses:");
        stringBuilder.append(str);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public SupportSQLiteQuery create() {
        if (!SupportSQLiteQueryBuilder.isEmpty(this.mGroupBy) || SupportSQLiteQueryBuilder.isEmpty(this.mHaving)) {
            StringBuilder stringBuilder = new StringBuilder(120);
            stringBuilder.append("SELECT ");
            if (this.mDistinct) {
                stringBuilder.append("DISTINCT ");
            }
            if (this.mColumns == null || this.mColumns.length == 0) {
                stringBuilder.append(" * ");
            } else {
                SupportSQLiteQueryBuilder.appendColumns(stringBuilder, this.mColumns);
            }
            stringBuilder.append(" FROM ");
            stringBuilder.append(this.mTable);
            SupportSQLiteQueryBuilder.appendClause(stringBuilder, " WHERE ", this.mSelection);
            SupportSQLiteQueryBuilder.appendClause(stringBuilder, " GROUP BY ", this.mGroupBy);
            SupportSQLiteQueryBuilder.appendClause(stringBuilder, " HAVING ", this.mHaving);
            SupportSQLiteQueryBuilder.appendClause(stringBuilder, " ORDER BY ", this.mOrderBy);
            SupportSQLiteQueryBuilder.appendClause(stringBuilder, " LIMIT ", this.mLimit);
            return new SimpleSQLiteQuery(stringBuilder.toString(), this.mBindArgs);
        }
        throw new IllegalArgumentException("HAVING clauses are only permitted when using a groupBy clause");
    }

    private static void appendClause(StringBuilder stringBuilder, String str, String str2) {
        if (!SupportSQLiteQueryBuilder.isEmpty(str2)) {
            stringBuilder.append(str);
            stringBuilder.append(str2);
        }
    }

    private static void appendColumns(StringBuilder stringBuilder, String[] strArr) {
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            String str = strArr[i];
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(str);
        }
        stringBuilder.append(' ');
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
