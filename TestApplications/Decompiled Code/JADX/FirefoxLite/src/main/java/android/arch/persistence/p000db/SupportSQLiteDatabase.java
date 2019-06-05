package android.arch.persistence.p000db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Pair;
import java.io.Closeable;
import java.util.List;

/* renamed from: android.arch.persistence.db.SupportSQLiteDatabase */
public interface SupportSQLiteDatabase extends Closeable {
    void beginTransaction();

    SupportSQLiteStatement compileStatement(String str);

    int delete(String str, String str2, Object[] objArr);

    void endTransaction();

    void execSQL(String str) throws SQLException;

    List<Pair<String, String>> getAttachedDbs();

    String getPath();

    boolean inTransaction();

    long insert(String str, int i, ContentValues contentValues) throws SQLException;

    boolean isOpen();

    Cursor query(SupportSQLiteQuery supportSQLiteQuery);

    Cursor query(String str);

    void setTransactionSuccessful();

    int update(String str, int i, ContentValues contentValues, String str2, Object[] objArr);
}
