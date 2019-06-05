package android.arch.persistence.p000db;

/* renamed from: android.arch.persistence.db.SupportSQLiteStatement */
public interface SupportSQLiteStatement extends SupportSQLiteProgram {
    long executeInsert();

    int executeUpdateDelete();
}
