// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import java.util.Set;
import android.content.Context;
import java.util.List;

public class DatabaseConfiguration
{
    public final boolean allowMainThreadQueries;
    public final List<RoomDatabase.Callback> callbacks;
    public final Context context;
    public final RoomDatabase.JournalMode journalMode;
    private final Set<Integer> mMigrationNotRequiredFrom;
    public final RoomDatabase.MigrationContainer migrationContainer;
    public final String name;
    public final boolean requireMigration;
    public final SupportSQLiteOpenHelper.Factory sqliteOpenHelperFactory;
    
    public DatabaseConfiguration(final Context context, final String name, final SupportSQLiteOpenHelper.Factory sqliteOpenHelperFactory, final RoomDatabase.MigrationContainer migrationContainer, final List<RoomDatabase.Callback> callbacks, final boolean allowMainThreadQueries, final RoomDatabase.JournalMode journalMode, final boolean requireMigration, final Set<Integer> mMigrationNotRequiredFrom) {
        this.sqliteOpenHelperFactory = sqliteOpenHelperFactory;
        this.context = context;
        this.name = name;
        this.migrationContainer = migrationContainer;
        this.callbacks = callbacks;
        this.allowMainThreadQueries = allowMainThreadQueries;
        this.journalMode = journalMode;
        this.requireMigration = requireMigration;
        this.mMigrationNotRequiredFrom = mMigrationNotRequiredFrom;
    }
    
    public boolean isMigrationRequiredFrom(final int i) {
        return this.requireMigration && (this.mMigrationNotRequiredFrom == null || !this.mMigrationNotRequiredFrom.contains(i));
    }
}
