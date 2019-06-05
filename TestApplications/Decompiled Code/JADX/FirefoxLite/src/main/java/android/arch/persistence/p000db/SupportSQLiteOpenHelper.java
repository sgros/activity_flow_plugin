package android.arch.persistence.p000db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;

/* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper */
public interface SupportSQLiteOpenHelper {

    /* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper$Callback */
    public static abstract class Callback {
        public final int version;

        public void onConfigure(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public abstract void onCreate(SupportSQLiteDatabase supportSQLiteDatabase);

        public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public abstract void onUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2);

        public Callback(int i) {
            this.version = i;
        }

        public void onDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't downgrade database from version ");
            stringBuilder.append(i);
            stringBuilder.append(" to ");
            stringBuilder.append(i2);
            throw new SQLiteException(stringBuilder.toString());
        }

        /* JADX WARNING: Removed duplicated region for block: B:24:0x0071  */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x0059  */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0031 */
        /* JADX WARNING: Removed duplicated region for block: B:8:0x002f A:{PHI: r0 , ExcHandler: all (th java.lang.Throwable), Splitter:B:5:0x0029} */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing block: B:11:0x0035, code skipped:
            if (r0 != null) goto L_0x0037;
     */
        /* JADX WARNING: Missing block: B:12:0x0037, code skipped:
            r4 = r0.iterator();
     */
        /* JADX WARNING: Missing block: B:14:0x003f, code skipped:
            if (r4.hasNext() != false) goto L_0x0041;
     */
        /* JADX WARNING: Missing block: B:15:0x0041, code skipped:
            deleteDatabaseFile((java.lang.String) ((android.util.Pair) r4.next()).second);
     */
        /* JADX WARNING: Missing block: B:16:0x004f, code skipped:
            deleteDatabaseFile(r4.getPath());
     */
        public void onCorruption(android.arch.persistence.p000db.SupportSQLiteDatabase r4) {
            /*
            r3 = this;
            r0 = "SupportSQLite";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Corruption reported by sqlite on database: ";
            r1.append(r2);
            r2 = r4.getPath();
            r1.append(r2);
            r1 = r1.toString();
            android.util.Log.e(r0, r1);
            r0 = r4.isOpen();
            if (r0 != 0) goto L_0x0028;
        L_0x0020:
            r4 = r4.getPath();
            r3.deleteDatabaseFile(r4);
            return;
        L_0x0028:
            r0 = 0;
            r1 = r4.getAttachedDbs();	 Catch:{ SQLiteException -> 0x0031, all -> 0x002f }
            r0 = r1;
            goto L_0x0031;
        L_0x002f:
            r1 = move-exception;
            goto L_0x0035;
        L_0x0031:
            r4.close();	 Catch:{ IOException -> 0x0057, all -> 0x002f }
            goto L_0x0057;
        L_0x0035:
            if (r0 == 0) goto L_0x004f;
        L_0x0037:
            r4 = r0.iterator();
        L_0x003b:
            r0 = r4.hasNext();
            if (r0 == 0) goto L_0x0056;
        L_0x0041:
            r0 = r4.next();
            r0 = (android.util.Pair) r0;
            r0 = r0.second;
            r0 = (java.lang.String) r0;
            r3.deleteDatabaseFile(r0);
            goto L_0x003b;
        L_0x004f:
            r4 = r4.getPath();
            r3.deleteDatabaseFile(r4);
        L_0x0056:
            throw r1;
        L_0x0057:
            if (r0 == 0) goto L_0x0071;
        L_0x0059:
            r4 = r0.iterator();
        L_0x005d:
            r0 = r4.hasNext();
            if (r0 == 0) goto L_0x0078;
        L_0x0063:
            r0 = r4.next();
            r0 = (android.util.Pair) r0;
            r0 = r0.second;
            r0 = (java.lang.String) r0;
            r3.deleteDatabaseFile(r0);
            goto L_0x005d;
        L_0x0071:
            r4 = r4.getPath();
            r3.deleteDatabaseFile(r4);
        L_0x0078:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.arch.persistence.p000db.SupportSQLiteOpenHelper$Callback.onCorruption(android.arch.persistence.db.SupportSQLiteDatabase):void");
        }

        private void deleteDatabaseFile(String str) {
            if (!str.equalsIgnoreCase(":memory:") && str.trim().length() != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("deleting the database file: ");
                stringBuilder.append(str);
                Log.w("SupportSQLite", stringBuilder.toString());
                try {
                    if (VERSION.SDK_INT >= 16) {
                        SQLiteDatabase.deleteDatabase(new File(str));
                    } else {
                        try {
                            if (!new File(str).delete()) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Could not delete the database file ");
                                stringBuilder.append(str);
                                Log.e("SupportSQLite", stringBuilder.toString());
                            }
                        } catch (Exception e) {
                            Log.e("SupportSQLite", "error while deleting corrupted database file", e);
                        }
                    }
                } catch (Exception e2) {
                    Log.w("SupportSQLite", "delete failed: ", e2);
                }
            }
        }
    }

    /* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper$Configuration */
    public static class Configuration {
        public final Callback callback;
        public final Context context;
        public final String name;

        /* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper$Configuration$Builder */
        public static class Builder {
            Callback mCallback;
            Context mContext;
            String mName;

            public Configuration build() {
                if (this.mCallback == null) {
                    throw new IllegalArgumentException("Must set a callback to create the configuration.");
                } else if (this.mContext != null) {
                    return new Configuration(this.mContext, this.mName, this.mCallback);
                } else {
                    throw new IllegalArgumentException("Must set a non-null context to create the configuration.");
                }
            }

            Builder(Context context) {
                this.mContext = context;
            }

            public Builder name(String str) {
                this.mName = str;
                return this;
            }

            public Builder callback(Callback callback) {
                this.mCallback = callback;
                return this;
            }
        }

        Configuration(Context context, String str, Callback callback) {
            this.context = context;
            this.name = str;
            this.callback = callback;
        }

        public static Builder builder(Context context) {
            return new Builder(context);
        }
    }

    /* renamed from: android.arch.persistence.db.SupportSQLiteOpenHelper$Factory */
    public interface Factory {
        SupportSQLiteOpenHelper create(Configuration configuration);
    }

    SupportSQLiteDatabase getReadableDatabase();

    SupportSQLiteDatabase getWritableDatabase();

    void setWriteAheadLoggingEnabled(boolean z);
}
