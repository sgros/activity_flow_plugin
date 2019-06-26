// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import android.content.OperationApplicationException;
import java.io.FileNotFoundException;
import net.sqlcipher.database.SQLiteAbortException;
import net.sqlcipher.database.SQLiteConstraintException;
import net.sqlcipher.database.SQLiteDatabaseCorruptException;
import net.sqlcipher.database.SQLiteFullException;
import net.sqlcipher.database.SQLiteDiskIOException;
import android.os.Parcel;
import net.sqlcipher.database.SQLiteStatement;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import java.io.PrintStream;
import android.database.CursorWindow;
import android.content.ContentValues;
import android.text.TextUtils;
import net.sqlcipher.database.SQLiteProgram;
import java.text.Collator;

public class DatabaseUtils
{
    private static final boolean DEBUG = false;
    private static final char[] HEX_DIGITS_LOWER;
    private static final boolean LOCAL_LOGV = false;
    private static final String TAG = "DatabaseUtils";
    private static final String[] countProjection;
    private static Collator mColl;
    
    static {
        countProjection = new String[] { "count(*)" };
        HEX_DIGITS_LOWER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
    
    public static void appendEscapedSQLString(final StringBuilder sb, final String str) {
        sb.append('\'');
        if (str.indexOf(39) != -1) {
            for (int length = str.length(), i = 0; i < length; ++i) {
                final char char1 = str.charAt(i);
                if (char1 == '\'') {
                    sb.append('\'');
                }
                sb.append(char1);
            }
        }
        else {
            sb.append(str);
        }
        sb.append('\'');
    }
    
    public static final void appendValueToSql(final StringBuilder sb, final Object o) {
        if (o == null) {
            sb.append("NULL");
        }
        else if (o instanceof Boolean) {
            if (o) {
                sb.append('1');
            }
            else {
                sb.append('0');
            }
        }
        else {
            appendEscapedSQLString(sb, o.toString());
        }
    }
    
    public static void bindObjectToProgram(final SQLiteProgram sqLiteProgram, final int n, final Object o) {
        if (o == null) {
            sqLiteProgram.bindNull(n);
        }
        else if (!(o instanceof Double) && !(o instanceof Float)) {
            if (o instanceof Number) {
                sqLiteProgram.bindLong(n, ((Number)o).longValue());
            }
            else if (o instanceof Boolean) {
                if (o) {
                    sqLiteProgram.bindLong(n, 1L);
                }
                else {
                    sqLiteProgram.bindLong(n, 0L);
                }
            }
            else if (o instanceof byte[]) {
                sqLiteProgram.bindBlob(n, (byte[])o);
            }
            else {
                sqLiteProgram.bindString(n, o.toString());
            }
        }
        else {
            sqLiteProgram.bindDouble(n, ((Number)o).doubleValue());
        }
    }
    
    public static String concatenateWhere(final String str, final String str2) {
        if (TextUtils.isEmpty((CharSequence)str)) {
            return str2;
        }
        if (TextUtils.isEmpty((CharSequence)str2)) {
            return str;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(str);
        sb.append(") AND (");
        sb.append(str2);
        sb.append(")");
        return sb.toString();
    }
    
    public static void cursorDoubleToContentValues(final Cursor cursor, final String s, final ContentValues contentValues, final String s2) {
        final int columnIndex = cursor.getColumnIndex(s);
        if (!cursor.isNull(columnIndex)) {
            contentValues.put(s2, Double.valueOf(cursor.getDouble(columnIndex)));
        }
        else {
            contentValues.put(s2, (Double)null);
        }
    }
    
    public static void cursorDoubleToContentValuesIfPresent(final Cursor cursor, final ContentValues contentValues, final String s) {
        final int columnIndexOrThrow = cursor.getColumnIndexOrThrow(s);
        if (!cursor.isNull(columnIndexOrThrow)) {
            contentValues.put(s, Double.valueOf(cursor.getDouble(columnIndexOrThrow)));
        }
    }
    
    public static void cursorDoubleToCursorValues(final Cursor cursor, final String s, final ContentValues contentValues) {
        cursorDoubleToContentValues(cursor, s, contentValues, s);
    }
    
    public static void cursorFillWindow(final Cursor cursor, int startPosition, final CursorWindow cursorWindow) {
        if (startPosition >= 0 && startPosition < cursor.getCount()) {
            final int position = cursor.getPosition();
            final int columnCount = cursor.getColumnCount();
            cursorWindow.clear();
            cursorWindow.setStartPosition(startPosition);
            cursorWindow.setNumColumns(columnCount);
            if (cursor.moveToPosition(startPosition)) {
                while (cursorWindow.allocRow()) {
                    for (int i = 0; i < columnCount; ++i) {
                        final int type = cursor.getType(i);
                        boolean b = false;
                        if (type != 4) {
                            switch (type) {
                                default: {
                                    final String string = cursor.getString(i);
                                    if (string != null) {
                                        b = cursorWindow.putString(string, startPosition, i);
                                        break;
                                    }
                                    b = cursorWindow.putNull(startPosition, i);
                                    break;
                                }
                                case 2: {
                                    b = cursorWindow.putDouble(cursor.getDouble(i), startPosition, i);
                                    break;
                                }
                                case 1: {
                                    b = cursorWindow.putLong(cursor.getLong(i), startPosition, i);
                                    break;
                                }
                                case 0: {
                                    b = cursorWindow.putNull(startPosition, i);
                                    break;
                                }
                            }
                        }
                        else {
                            final byte[] blob = cursor.getBlob(i);
                            if (blob != null) {
                                b = cursorWindow.putBlob(blob, startPosition, i);
                            }
                            else {
                                b = cursorWindow.putNull(startPosition, i);
                            }
                        }
                        if (!b) {
                            cursorWindow.freeLastRow();
                            break;
                        }
                    }
                    ++startPosition;
                    if (!cursor.moveToNext()) {
                        break;
                    }
                }
            }
            cursor.moveToPosition(position);
        }
    }
    
    public static void cursorFloatToContentValuesIfPresent(final Cursor cursor, final ContentValues contentValues, final String s) {
        final int columnIndexOrThrow = cursor.getColumnIndexOrThrow(s);
        if (!cursor.isNull(columnIndexOrThrow)) {
            contentValues.put(s, Float.valueOf(cursor.getFloat(columnIndexOrThrow)));
        }
    }
    
    public static void cursorIntToContentValues(final Cursor cursor, final String s, final ContentValues contentValues) {
        cursorIntToContentValues(cursor, s, contentValues, s);
    }
    
    public static void cursorIntToContentValues(final Cursor cursor, final String s, final ContentValues contentValues, final String s2) {
        final int columnIndex = cursor.getColumnIndex(s);
        if (!cursor.isNull(columnIndex)) {
            contentValues.put(s2, Integer.valueOf(cursor.getInt(columnIndex)));
        }
        else {
            contentValues.put(s2, (Integer)null);
        }
    }
    
    public static void cursorIntToContentValuesIfPresent(final Cursor cursor, final ContentValues contentValues, final String s) {
        final int columnIndexOrThrow = cursor.getColumnIndexOrThrow(s);
        if (!cursor.isNull(columnIndexOrThrow)) {
            contentValues.put(s, Integer.valueOf(cursor.getInt(columnIndexOrThrow)));
        }
    }
    
    public static void cursorLongToContentValues(final Cursor cursor, final String s, final ContentValues contentValues) {
        cursorLongToContentValues(cursor, s, contentValues, s);
    }
    
    public static void cursorLongToContentValues(final Cursor cursor, final String s, final ContentValues contentValues, final String s2) {
        final int columnIndex = cursor.getColumnIndex(s);
        if (!cursor.isNull(columnIndex)) {
            contentValues.put(s2, Long.valueOf(cursor.getLong(columnIndex)));
        }
        else {
            contentValues.put(s2, (Long)null);
        }
    }
    
    public static void cursorLongToContentValuesIfPresent(final Cursor cursor, final ContentValues contentValues, final String s) {
        final int columnIndexOrThrow = cursor.getColumnIndexOrThrow(s);
        if (!cursor.isNull(columnIndexOrThrow)) {
            contentValues.put(s, Long.valueOf(cursor.getLong(columnIndexOrThrow)));
        }
    }
    
    public static void cursorRowToContentValues(final Cursor cursor, final ContentValues contentValues) {
        AbstractWindowedCursor abstractWindowedCursor;
        if (cursor instanceof AbstractWindowedCursor) {
            abstractWindowedCursor = (AbstractWindowedCursor)cursor;
        }
        else {
            abstractWindowedCursor = null;
        }
        final String[] columnNames = cursor.getColumnNames();
        for (int length = columnNames.length, i = 0; i < length; ++i) {
            if (abstractWindowedCursor != null && abstractWindowedCursor.isBlob(i)) {
                contentValues.put(columnNames[i], cursor.getBlob(i));
            }
            else {
                contentValues.put(columnNames[i], cursor.getString(i));
            }
        }
    }
    
    public static void cursorShortToContentValuesIfPresent(final Cursor cursor, final ContentValues contentValues, final String s) {
        final int columnIndexOrThrow = cursor.getColumnIndexOrThrow(s);
        if (!cursor.isNull(columnIndexOrThrow)) {
            contentValues.put(s, Short.valueOf(cursor.getShort(columnIndexOrThrow)));
        }
    }
    
    public static void cursorStringToContentValues(final Cursor cursor, final String s, final ContentValues contentValues) {
        cursorStringToContentValues(cursor, s, contentValues, s);
    }
    
    public static void cursorStringToContentValues(final Cursor cursor, final String s, final ContentValues contentValues, final String s2) {
        contentValues.put(s2, cursor.getString(cursor.getColumnIndexOrThrow(s)));
    }
    
    public static void cursorStringToContentValuesIfPresent(final Cursor cursor, final ContentValues contentValues, final String s) {
        final int columnIndexOrThrow = cursor.getColumnIndexOrThrow(s);
        if (!cursor.isNull(columnIndexOrThrow)) {
            contentValues.put(s, cursor.getString(columnIndexOrThrow));
        }
    }
    
    public static void cursorStringToInsertHelper(final Cursor cursor, final String s, final InsertHelper insertHelper, final int n) {
        insertHelper.bind(n, cursor.getString(cursor.getColumnIndexOrThrow(s)));
    }
    
    public static void dumpCurrentRow(final Cursor cursor) {
        dumpCurrentRow(cursor, System.out);
    }
    
    public static void dumpCurrentRow(final Cursor cursor, final PrintStream printStream) {
        final String[] columnNames = cursor.getColumnNames();
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(cursor.getPosition());
        sb.append(" {");
        printStream.println(sb.toString());
        for (int i = 0; i < columnNames.length; ++i) {
            String string;
            try {
                string = cursor.getString(i);
            }
            catch (SQLiteException ex) {
                string = "<unprintable>";
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("   ");
            sb2.append(columnNames[i]);
            sb2.append('=');
            sb2.append(string);
            printStream.println(sb2.toString());
        }
        printStream.println("}");
    }
    
    public static void dumpCurrentRow(final Cursor cursor, final StringBuilder sb) {
        final String[] columnNames = cursor.getColumnNames();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(cursor.getPosition());
        sb2.append(" {\n");
        sb.append(sb2.toString());
        for (int i = 0; i < columnNames.length; ++i) {
            String string;
            try {
                string = cursor.getString(i);
            }
            catch (SQLiteException ex) {
                string = "<unprintable>";
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("   ");
            sb3.append(columnNames[i]);
            sb3.append('=');
            sb3.append(string);
            sb3.append("\n");
            sb.append(sb3.toString());
        }
        sb.append("}\n");
    }
    
    public static String dumpCurrentRowToString(final Cursor cursor) {
        final StringBuilder sb = new StringBuilder();
        dumpCurrentRow(cursor, sb);
        return sb.toString();
    }
    
    public static void dumpCursor(final Cursor cursor) {
        dumpCursor(cursor, System.out);
    }
    
    public static void dumpCursor(final Cursor obj, final PrintStream printStream) {
        final StringBuilder sb = new StringBuilder();
        sb.append(">>>>> Dumping cursor ");
        sb.append(obj);
        printStream.println(sb.toString());
        if (obj != null) {
            final int position = obj.getPosition();
            obj.moveToPosition(-1);
            while (obj.moveToNext()) {
                dumpCurrentRow(obj, printStream);
            }
            obj.moveToPosition(position);
        }
        printStream.println("<<<<<");
    }
    
    public static void dumpCursor(final Cursor obj, final StringBuilder sb) {
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(">>>>> Dumping cursor ");
        sb2.append(obj);
        sb2.append("\n");
        sb.append(sb2.toString());
        if (obj != null) {
            final int position = obj.getPosition();
            obj.moveToPosition(-1);
            while (obj.moveToNext()) {
                dumpCurrentRow(obj, sb);
            }
            obj.moveToPosition(position);
        }
        sb.append("<<<<<\n");
    }
    
    public static String dumpCursorToString(final Cursor cursor) {
        final StringBuilder sb = new StringBuilder();
        dumpCursor(cursor, sb);
        return sb.toString();
    }
    
    private static char[] encodeHex(final byte[] array, final char[] array2) {
        int i = 0;
        final int length = array.length;
        final char[] array3 = new char[length << 1];
        int n = 0;
        while (i < length) {
            final int n2 = n + 1;
            array3[n] = array2[(0xF0 & array[i]) >>> 4];
            n = n2 + 1;
            array3[n2] = array2[0xF & array[i]];
            ++i;
        }
        return array3;
    }
    
    public static String getCollationKey(String s) {
        final byte[] collationKeyInBytes = getCollationKeyInBytes(s);
        try {
            s = new String(collationKeyInBytes, 0, getKeyLen(collationKeyInBytes), "ISO8859_1");
            return s;
        }
        catch (Exception ex) {
            return "";
        }
    }
    
    private static byte[] getCollationKeyInBytes(final String s) {
        if (DatabaseUtils.mColl == null) {
            (DatabaseUtils.mColl = Collator.getInstance()).setStrength(0);
        }
        return DatabaseUtils.mColl.getCollationKey(s).toByteArray();
    }
    
    public static String getHexCollationKey(final String s) {
        final byte[] collationKeyInBytes = getCollationKeyInBytes(s);
        return new String(encodeHex(collationKeyInBytes, DatabaseUtils.HEX_DIGITS_LOWER), 0, getKeyLen(collationKeyInBytes) * 2);
    }
    
    private static int getKeyLen(final byte[] array) {
        if (array[array.length - 1] != 0) {
            return array.length;
        }
        return array.length - 1;
    }
    
    public static int getTypeOfObject(final Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof byte[]) {
            return 4;
        }
        if (o instanceof Float || o instanceof Double) {
            return 2;
        }
        if (!(o instanceof Long) && !(o instanceof Integer)) {
            return 3;
        }
        return 1;
    }
    
    public static long longForQuery(SQLiteDatabase compileStatement, final String s, final String[] array) {
        compileStatement = (SQLiteDatabase)compileStatement.compileStatement(s);
        try {
            return longForQuery((SQLiteStatement)compileStatement, array);
        }
        finally {
            ((SQLiteProgram)compileStatement).close();
        }
    }
    
    public static long longForQuery(final SQLiteStatement sqLiteStatement, final String[] array) {
        if (array != null) {
            int n;
            for (int length = array.length, i = 0; i < length; i = n) {
                n = i + 1;
                bindObjectToProgram(sqLiteStatement, n, array[i]);
            }
        }
        return sqLiteStatement.simpleQueryForLong();
    }
    
    public static long queryNumEntries(final SQLiteDatabase sqLiteDatabase, String query) {
        query = (String)sqLiteDatabase.query(query, DatabaseUtils.countProjection, null, null, null, null, null);
        try {
            ((Cursor)query).moveToFirst();
            return ((Cursor)query).getLong(0);
        }
        finally {
            ((Cursor)query).close();
        }
    }
    
    public static final void readExceptionFromParcel(final Parcel parcel) {
        final int int1 = parcel.readInt();
        if (int1 == 0) {
            return;
        }
        readExceptionFromParcel(parcel, parcel.readString(), int1);
    }
    
    private static final void readExceptionFromParcel(final Parcel parcel, final String s, final int n) {
        switch (n) {
            default: {
                parcel.readException(n, s);
            }
            case 9: {
                throw new SQLiteException(s);
            }
            case 8: {
                throw new SQLiteDiskIOException(s);
            }
            case 7: {
                throw new SQLiteFullException(s);
            }
            case 6: {
                throw new SQLiteDatabaseCorruptException(s);
            }
            case 5: {
                throw new SQLiteConstraintException(s);
            }
            case 4: {
                throw new SQLiteAbortException(s);
            }
            case 3: {
                throw new UnsupportedOperationException(s);
            }
            case 2: {
                throw new IllegalArgumentException(s);
            }
        }
    }
    
    public static void readExceptionWithFileNotFoundExceptionFromParcel(final Parcel parcel) throws FileNotFoundException {
        final int int1 = parcel.readInt();
        if (int1 == 0) {
            return;
        }
        final String string = parcel.readString();
        if (int1 == 1) {
            throw new FileNotFoundException(string);
        }
        readExceptionFromParcel(parcel, string, int1);
    }
    
    public static void readExceptionWithOperationApplicationExceptionFromParcel(final Parcel parcel) throws OperationApplicationException {
        final int int1 = parcel.readInt();
        if (int1 == 0) {
            return;
        }
        final String string = parcel.readString();
        if (int1 == 10) {
            throw new OperationApplicationException(string);
        }
        readExceptionFromParcel(parcel, string, int1);
    }
    
    public static String sqlEscapeString(final String s) {
        final StringBuilder sb = new StringBuilder();
        appendEscapedSQLString(sb, s);
        return sb.toString();
    }
    
    public static String stringForQuery(SQLiteDatabase compileStatement, String stringForQuery, final String[] array) {
        compileStatement = (SQLiteDatabase)compileStatement.compileStatement(stringForQuery);
        try {
            stringForQuery = stringForQuery((SQLiteStatement)compileStatement, array);
            return stringForQuery;
        }
        finally {
            ((SQLiteProgram)compileStatement).close();
        }
    }
    
    public static String stringForQuery(final SQLiteStatement sqLiteStatement, final String[] array) {
        if (array != null) {
            int n;
            for (int length = array.length, i = 0; i < length; i = n) {
                n = i + 1;
                bindObjectToProgram(sqLiteStatement, n, array[i]);
            }
        }
        return sqLiteStatement.simpleQueryForString();
    }
    
    public static final void writeExceptionToParcel(final Parcel parcel, final Exception ex) {
        final boolean b = ex instanceof FileNotFoundException;
        boolean b2 = true;
        int n;
        if (b) {
            b2 = false;
            n = 1;
        }
        else if (ex instanceof IllegalArgumentException) {
            n = 2;
        }
        else if (ex instanceof UnsupportedOperationException) {
            n = 3;
        }
        else if (ex instanceof SQLiteAbortException) {
            n = 4;
        }
        else if (ex instanceof SQLiteConstraintException) {
            n = 5;
        }
        else if (ex instanceof SQLiteDatabaseCorruptException) {
            n = 6;
        }
        else if (ex instanceof SQLiteFullException) {
            n = 7;
        }
        else if (ex instanceof SQLiteDiskIOException) {
            n = 8;
        }
        else if (ex instanceof SQLiteException) {
            n = 9;
        }
        else {
            if (!(ex instanceof OperationApplicationException)) {
                parcel.writeException(ex);
                Log.e("DatabaseUtils", "Writing exception to parcel", (Throwable)ex);
                return;
            }
            n = 10;
        }
        parcel.writeInt(n);
        parcel.writeString(ex.getMessage());
        if (b2) {
            Log.e("DatabaseUtils", "Writing exception to parcel", (Throwable)ex);
        }
    }
    
    public static class InsertHelper
    {
        public static final int TABLE_INFO_PRAGMA_COLUMNNAME_INDEX = 1;
        public static final int TABLE_INFO_PRAGMA_DEFAULT_INDEX = 4;
        private HashMap<String, Integer> mColumns;
        private final SQLiteDatabase mDb;
        private String mInsertSQL;
        private SQLiteStatement mInsertStatement;
        private SQLiteStatement mPreparedStatement;
        private SQLiteStatement mReplaceStatement;
        private final String mTableName;
        
        public InsertHelper(final SQLiteDatabase mDb, final String mTableName) {
            this.mInsertSQL = null;
            this.mInsertStatement = null;
            this.mReplaceStatement = null;
            this.mPreparedStatement = null;
            this.mDb = mDb;
            this.mTableName = mTableName;
        }
        
        private void buildSQL() throws SQLException {
            final StringBuilder sb = new StringBuilder(128);
            sb.append("INSERT INTO ");
            sb.append(this.mTableName);
            sb.append(" (");
            final StringBuilder s = new StringBuilder(128);
            s.append("VALUES (");
            Cursor cursor;
            try {
                final SQLiteDatabase mDb = this.mDb;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("PRAGMA table_info(");
                sb2.append(this.mTableName);
                sb2.append(")");
                final Cursor rawQuery = mDb.rawQuery(sb2.toString(), null);
                try {
                    this.mColumns = new HashMap<String, Integer>(rawQuery.getCount());
                    int i = 1;
                    while (rawQuery.moveToNext()) {
                        final String string = rawQuery.getString(1);
                        final String string2 = rawQuery.getString(4);
                        this.mColumns.put(string, i);
                        sb.append("'");
                        sb.append(string);
                        sb.append("'");
                        if (string2 == null) {
                            s.append("?");
                        }
                        else {
                            s.append("COALESCE(?, ");
                            s.append(string2);
                            s.append(")");
                        }
                        String str;
                        if (i == rawQuery.getCount()) {
                            str = ") ";
                        }
                        else {
                            str = ", ";
                        }
                        sb.append(str);
                        String str2;
                        if (i == rawQuery.getCount()) {
                            str2 = ");";
                        }
                        else {
                            str2 = ", ";
                        }
                        s.append(str2);
                        ++i;
                    }
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    sb.append((CharSequence)s);
                    this.mInsertSQL = sb.toString();
                    return;
                }
                finally {}
            }
            finally {
                cursor = null;
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        
        private SQLiteStatement getStatement(final boolean b) throws SQLException {
            if (b) {
                if (this.mReplaceStatement == null) {
                    if (this.mInsertSQL == null) {
                        this.buildSQL();
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("INSERT OR REPLACE");
                    sb.append(this.mInsertSQL.substring(6));
                    this.mReplaceStatement = this.mDb.compileStatement(sb.toString());
                }
                return this.mReplaceStatement;
            }
            if (this.mInsertStatement == null) {
                if (this.mInsertSQL == null) {
                    this.buildSQL();
                }
                this.mInsertStatement = this.mDb.compileStatement(this.mInsertSQL);
            }
            return this.mInsertStatement;
        }
        
        private long insertInternal(final ContentValues obj, final boolean b) {
            // monitorenter(this)
            try {
                try {
                    final SQLiteStatement statement = this.getStatement(b);
                    statement.clearBindings();
                    for (final Map.Entry<String, V> entry : obj.valueSet()) {
                        DatabaseUtils.bindObjectToProgram(statement, this.getColumnIndex(entry.getKey()), entry.getValue());
                    }
                    // monitorexit(this)
                    return statement.executeInsert();
                }
                finally {}
            }
            catch (SQLException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Error inserting ");
                sb.append(obj);
                sb.append(" into table  ");
                sb.append(this.mTableName);
                Log.e("DatabaseUtils", sb.toString(), (Throwable)ex);
                // monitorexit(this)
                return -1L;
            }
        }
        // monitorexit(this)
        
        public void bind(final int n, final double n2) {
            this.mPreparedStatement.bindDouble(n, n2);
        }
        
        public void bind(final int n, final float n2) {
            this.mPreparedStatement.bindDouble(n, n2);
        }
        
        public void bind(final int n, final int n2) {
            this.mPreparedStatement.bindLong(n, n2);
        }
        
        public void bind(final int n, final long n2) {
            this.mPreparedStatement.bindLong(n, n2);
        }
        
        public void bind(final int n, final String s) {
            if (s == null) {
                this.mPreparedStatement.bindNull(n);
            }
            else {
                this.mPreparedStatement.bindString(n, s);
            }
        }
        
        public void bind(final int n, final boolean b) {
            final SQLiteStatement mPreparedStatement = this.mPreparedStatement;
            long n2;
            if (b) {
                n2 = 1L;
            }
            else {
                n2 = 0L;
            }
            mPreparedStatement.bindLong(n, n2);
        }
        
        public void bind(final int n, final byte[] array) {
            if (array == null) {
                this.mPreparedStatement.bindNull(n);
            }
            else {
                this.mPreparedStatement.bindBlob(n, array);
            }
        }
        
        public void bindNull(final int n) {
            this.mPreparedStatement.bindNull(n);
        }
        
        public void close() {
            if (this.mInsertStatement != null) {
                this.mInsertStatement.close();
                this.mInsertStatement = null;
            }
            if (this.mReplaceStatement != null) {
                this.mReplaceStatement.close();
                this.mReplaceStatement = null;
            }
            this.mInsertSQL = null;
            this.mColumns = null;
        }
        
        public long execute() {
            if (this.mPreparedStatement == null) {
                throw new IllegalStateException("you must prepare this inserter before calling execute");
            }
            try {
                try {
                    final long executeInsert = this.mPreparedStatement.executeInsert();
                    this.mPreparedStatement = null;
                    return executeInsert;
                }
                finally {}
            }
            catch (SQLException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Error executing InsertHelper with table ");
                sb.append(this.mTableName);
                Log.e("DatabaseUtils", sb.toString(), (Throwable)ex);
                this.mPreparedStatement = null;
                return -1L;
            }
            this.mPreparedStatement = null;
        }
        
        public int getColumnIndex(final String s) {
            this.getStatement(false);
            final Integer n = this.mColumns.get(s);
            if (n == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("column '");
                sb.append(s);
                sb.append("' is invalid");
                throw new IllegalArgumentException(sb.toString());
            }
            return n;
        }
        
        public long insert(final ContentValues contentValues) {
            return this.insertInternal(contentValues, false);
        }
        
        public void prepareForInsert() {
            (this.mPreparedStatement = this.getStatement(false)).clearBindings();
        }
        
        public void prepareForReplace() {
            (this.mPreparedStatement = this.getStatement(true)).clearBindings();
        }
        
        public long replace(final ContentValues contentValues) {
            return this.insertInternal(contentValues, true);
        }
    }
}
