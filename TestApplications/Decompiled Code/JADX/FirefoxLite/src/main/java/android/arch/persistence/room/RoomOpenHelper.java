package android.arch.persistence.room;

import android.arch.persistence.p000db.SimpleSQLiteQuery;
import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Callback;
import android.database.Cursor;

public class RoomOpenHelper extends Callback {
    private DatabaseConfiguration mConfiguration;
    private final Delegate mDelegate;
    private final String mIdentityHash;
    private final String mLegacyHash;

    public static abstract class Delegate {
        public final int version;

        public abstract void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase);

        public abstract void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase);

        public abstract void onCreate(SupportSQLiteDatabase supportSQLiteDatabase);

        public abstract void onOpen(SupportSQLiteDatabase supportSQLiteDatabase);

        public abstract void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase);

        public Delegate(int i) {
            this.version = i;
        }
    }

    public RoomOpenHelper(DatabaseConfiguration databaseConfiguration, Delegate delegate, String str, String str2) {
        super(delegate.version);
        this.mConfiguration = databaseConfiguration;
        this.mDelegate = delegate;
        this.mIdentityHash = str;
        this.mLegacyHash = str2;
    }

    public void onConfigure(SupportSQLiteDatabase supportSQLiteDatabase) {
        super.onConfigure(supportSQLiteDatabase);
    }

    public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
        updateIdentity(supportSQLiteDatabase);
        this.mDelegate.createAllTables(supportSQLiteDatabase);
        this.mDelegate.onCreate(supportSQLiteDatabase);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x002f  */
    public void onUpgrade(android.arch.persistence.p000db.SupportSQLiteDatabase r3, int r4, int r5) {
        /*
        r2 = this;
        r0 = r2.mConfiguration;
        if (r0 == 0) goto L_0x002c;
    L_0x0004:
        r0 = r2.mConfiguration;
        r0 = r0.migrationContainer;
        r0 = r0.findMigrationPath(r4, r5);
        if (r0 == 0) goto L_0x002c;
    L_0x000e:
        r0 = r0.iterator();
    L_0x0012:
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x0022;
    L_0x0018:
        r1 = r0.next();
        r1 = (android.arch.persistence.room.migration.Migration) r1;
        r1.migrate(r3);
        goto L_0x0012;
    L_0x0022:
        r0 = r2.mDelegate;
        r0.validateMigration(r3);
        r2.updateIdentity(r3);
        r0 = 1;
        goto L_0x002d;
    L_0x002c:
        r0 = 0;
    L_0x002d:
        if (r0 != 0) goto L_0x007e;
    L_0x002f:
        r0 = r2.mConfiguration;
        if (r0 == 0) goto L_0x0046;
    L_0x0033:
        r0 = r2.mConfiguration;
        r0 = r0.isMigrationRequiredFrom(r4);
        if (r0 != 0) goto L_0x0046;
    L_0x003b:
        r4 = r2.mDelegate;
        r4.dropAllTables(r3);
        r4 = r2.mDelegate;
        r4.createAllTables(r3);
        goto L_0x007e;
    L_0x0046:
        r3 = new java.lang.IllegalStateException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "A migration from ";
        r0.append(r1);
        r0.append(r4);
        r4 = " to ";
        r0.append(r4);
        r0.append(r5);
        r4 = " was required but not found. Please provide the ";
        r0.append(r4);
        r4 = "necessary Migration path via ";
        r0.append(r4);
        r4 = "RoomDatabase.Builder.addMigration(Migration ...) or allow for ";
        r0.append(r4);
        r4 = "destructive migrations via one of the ";
        r0.append(r4);
        r4 = "RoomDatabase.Builder.fallbackToDestructiveMigration* methods.";
        r0.append(r4);
        r4 = r0.toString();
        r3.<init>(r4);
        throw r3;
    L_0x007e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.arch.persistence.room.RoomOpenHelper.onUpgrade(android.arch.persistence.db.SupportSQLiteDatabase, int, int):void");
    }

    public void onDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
        onUpgrade(supportSQLiteDatabase, i, i2);
    }

    public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
        super.onOpen(supportSQLiteDatabase);
        checkIdentity(supportSQLiteDatabase);
        this.mDelegate.onOpen(supportSQLiteDatabase);
        this.mConfiguration = null;
    }

    private void checkIdentity(SupportSQLiteDatabase supportSQLiteDatabase) {
        Object obj = null;
        if (hasRoomMasterTable(supportSQLiteDatabase)) {
            Cursor query = supportSQLiteDatabase.query(new SimpleSQLiteQuery("SELECT identity_hash FROM room_master_table WHERE id = 42 LIMIT 1"));
            try {
                if (query.moveToFirst()) {
                    obj = query.getString(0);
                }
                query.close();
            } catch (Throwable th) {
                query.close();
            }
        }
        if (!this.mIdentityHash.equals(obj) && !this.mLegacyHash.equals(obj)) {
            throw new IllegalStateException("Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.");
        }
    }

    private void updateIdentity(SupportSQLiteDatabase supportSQLiteDatabase) {
        createMasterTableIfNotExists(supportSQLiteDatabase);
        supportSQLiteDatabase.execSQL(RoomMasterTable.createInsertQuery(this.mIdentityHash));
    }

    private void createMasterTableIfNotExists(SupportSQLiteDatabase supportSQLiteDatabase) {
        supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
    }

    private static boolean hasRoomMasterTable(SupportSQLiteDatabase supportSQLiteDatabase) {
        Cursor query = supportSQLiteDatabase.query("SELECT 1 FROM sqlite_master WHERE type = 'table' AND name='room_master_table'");
        try {
            boolean z = false;
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            query.close();
            return z;
        } catch (Throwable th) {
            query.close();
        }
    }
}
