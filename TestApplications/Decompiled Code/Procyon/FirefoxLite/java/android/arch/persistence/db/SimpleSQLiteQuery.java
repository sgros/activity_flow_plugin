// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db;

public final class SimpleSQLiteQuery implements SupportSQLiteQuery
{
    private final Object[] mBindArgs;
    private final String mQuery;
    
    public SimpleSQLiteQuery(final String s) {
        this(s, null);
    }
    
    public SimpleSQLiteQuery(final String mQuery, final Object[] mBindArgs) {
        this.mQuery = mQuery;
        this.mBindArgs = mBindArgs;
    }
    
    private static void bind(final SupportSQLiteProgram supportSQLiteProgram, final int i, final Object obj) {
        if (obj == null) {
            supportSQLiteProgram.bindNull(i);
        }
        else if (obj instanceof byte[]) {
            supportSQLiteProgram.bindBlob(i, (byte[])obj);
        }
        else if (obj instanceof Float) {
            supportSQLiteProgram.bindDouble(i, (float)obj);
        }
        else if (obj instanceof Double) {
            supportSQLiteProgram.bindDouble(i, (double)obj);
        }
        else if (obj instanceof Long) {
            supportSQLiteProgram.bindLong(i, (long)obj);
        }
        else if (obj instanceof Integer) {
            supportSQLiteProgram.bindLong(i, (int)obj);
        }
        else if (obj instanceof Short) {
            supportSQLiteProgram.bindLong(i, (short)obj);
        }
        else if (obj instanceof Byte) {
            supportSQLiteProgram.bindLong(i, (byte)obj);
        }
        else if (obj instanceof String) {
            supportSQLiteProgram.bindString(i, (String)obj);
        }
        else {
            if (!(obj instanceof Boolean)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Cannot bind ");
                sb.append(obj);
                sb.append(" at index ");
                sb.append(i);
                sb.append(" Supported types: null, byte[], float, double, long, int, short, byte,");
                sb.append(" string");
                throw new IllegalArgumentException(sb.toString());
            }
            long n;
            if (obj) {
                n = 1L;
            }
            else {
                n = 0L;
            }
            supportSQLiteProgram.bindLong(i, n);
        }
    }
    
    public static void bind(final SupportSQLiteProgram supportSQLiteProgram, final Object[] array) {
        if (array == null) {
            return;
        }
        final int length = array.length;
        int i = 0;
        while (i < length) {
            final Object o = array[i];
            ++i;
            bind(supportSQLiteProgram, i, o);
        }
    }
    
    @Override
    public void bindTo(final SupportSQLiteProgram supportSQLiteProgram) {
        bind(supportSQLiteProgram, this.mBindArgs);
    }
    
    @Override
    public String getSql() {
        return this.mQuery;
    }
}
