// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room;

import java.util.Iterator;
import java.util.List;
import android.arch.persistence.room.migration.Migration;
import android.database.Cursor;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;

public class RoomOpenHelper extends Callback
{
    private DatabaseConfiguration mConfiguration;
    private final Delegate mDelegate;
    private final String mIdentityHash;
    private final String mLegacyHash;
    
    public RoomOpenHelper(final DatabaseConfiguration mConfiguration, final Delegate mDelegate, final String mIdentityHash, final String mLegacyHash) {
        super(mDelegate.version);
        this.mConfiguration = mConfiguration;
        this.mDelegate = mDelegate;
        this.mIdentityHash = mIdentityHash;
        this.mLegacyHash = mLegacyHash;
    }
    
    private void checkIdentity(final SupportSQLiteDatabase supportSQLiteDatabase) {
        final boolean hasRoomMasterTable = hasRoomMasterTable(supportSQLiteDatabase);
        Cursor query = null;
        final Cursor cursor = null;
        if (hasRoomMasterTable) {
            query = supportSQLiteDatabase.query(new SimpleSQLiteQuery("SELECT identity_hash FROM room_master_table WHERE id = 42 LIMIT 1"));
            Object string = cursor;
            try {
                if (query.moveToFirst()) {
                    string = query.getString(0);
                }
                query.close();
                query = (Cursor)string;
            }
            finally {
                query.close();
            }
        }
        if (!this.mIdentityHash.equals(query) && !this.mLegacyHash.equals(query)) {
            throw new IllegalStateException("Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.");
        }
    }
    
    private void createMasterTableIfNotExists(final SupportSQLiteDatabase supportSQLiteDatabase) {
        supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
    }
    
    private static boolean hasRoomMasterTable(final SupportSQLiteDatabase supportSQLiteDatabase) {
        final Cursor query = supportSQLiteDatabase.query("SELECT 1 FROM sqlite_master WHERE type = 'table' AND name='room_master_table'");
        try {
            final boolean moveToFirst = query.moveToFirst();
            boolean b = false;
            if (moveToFirst) {
                final int int1 = query.getInt(0);
                b = b;
                if (int1 != 0) {
                    b = true;
                }
            }
            return b;
        }
        finally {
            query.close();
        }
    }
    
    private void updateIdentity(final SupportSQLiteDatabase supportSQLiteDatabase) {
        this.createMasterTableIfNotExists(supportSQLiteDatabase);
        supportSQLiteDatabase.execSQL(RoomMasterTable.createInsertQuery(this.mIdentityHash));
    }
    
    @Override
    public void onConfigure(final SupportSQLiteDatabase supportSQLiteDatabase) {
        super.onConfigure(supportSQLiteDatabase);
    }
    
    @Override
    public void onCreate(final SupportSQLiteDatabase supportSQLiteDatabase) {
        this.updateIdentity(supportSQLiteDatabase);
        this.mDelegate.createAllTables(supportSQLiteDatabase);
        this.mDelegate.onCreate(supportSQLiteDatabase);
    }
    
    @Override
    public void onDowngrade(final SupportSQLiteDatabase supportSQLiteDatabase, final int n, final int n2) {
        this.onUpgrade(supportSQLiteDatabase, n, n2);
    }
    
    @Override
    public void onOpen(final SupportSQLiteDatabase supportSQLiteDatabase) {
        super.onOpen(supportSQLiteDatabase);
        this.checkIdentity(supportSQLiteDatabase);
        this.mDelegate.onOpen(supportSQLiteDatabase);
        this.mConfiguration = null;
    }
    
    @Override
    public void onUpgrade(final SupportSQLiteDatabase supportSQLiteDatabase, final int i, final int j) {
        boolean b = false;
        Label_0084: {
            if (this.mConfiguration != null) {
                final List<Migration> migrationPath = this.mConfiguration.migrationContainer.findMigrationPath(i, j);
                if (migrationPath != null) {
                    final Iterator<Migration> iterator = migrationPath.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().migrate(supportSQLiteDatabase);
                    }
                    this.mDelegate.validateMigration(supportSQLiteDatabase);
                    this.updateIdentity(supportSQLiteDatabase);
                    b = true;
                    break Label_0084;
                }
            }
            b = false;
        }
        if (!b) {
            if (this.mConfiguration == null || this.mConfiguration.isMigrationRequiredFrom(i)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("A migration from ");
                sb.append(i);
                sb.append(" to ");
                sb.append(j);
                sb.append(" was required but not found. Please provide the ");
                sb.append("necessary Migration path via ");
                sb.append("RoomDatabase.Builder.addMigration(Migration ...) or allow for ");
                sb.append("destructive migrations via one of the ");
                sb.append("RoomDatabase.Builder.fallbackToDestructiveMigration* methods.");
                throw new IllegalStateException(sb.toString());
            }
            this.mDelegate.dropAllTables(supportSQLiteDatabase);
            this.mDelegate.createAllTables(supportSQLiteDatabase);
        }
    }
    
    public abstract static class Delegate
    {
        public final int version;
        
        public Delegate(final int version) {
            this.version = version;
        }
        
        protected abstract void createAllTables(final SupportSQLiteDatabase p0);
        
        protected abstract void dropAllTables(final SupportSQLiteDatabase p0);
        
        protected abstract void onCreate(final SupportSQLiteDatabase p0);
        
        protected abstract void onOpen(final SupportSQLiteDatabase p0);
        
        protected abstract void validateMigration(final SupportSQLiteDatabase p0);
    }
}
