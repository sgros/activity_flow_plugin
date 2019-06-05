package android.arch.persistence.room;

import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Factory;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.arch.persistence.room.RoomDatabase.JournalMode;
import android.arch.persistence.room.RoomDatabase.MigrationContainer;
import android.content.Context;
import java.util.List;
import java.util.Set;

public class DatabaseConfiguration {
    public final boolean allowMainThreadQueries;
    public final List<Callback> callbacks;
    public final Context context;
    public final JournalMode journalMode;
    private final Set<Integer> mMigrationNotRequiredFrom;
    public final MigrationContainer migrationContainer;
    public final String name;
    public final boolean requireMigration;
    public final Factory sqliteOpenHelperFactory;

    public DatabaseConfiguration(Context context, String str, Factory factory, MigrationContainer migrationContainer, List<Callback> list, boolean z, JournalMode journalMode, boolean z2, Set<Integer> set) {
        this.sqliteOpenHelperFactory = factory;
        this.context = context;
        this.name = str;
        this.migrationContainer = migrationContainer;
        this.callbacks = list;
        this.allowMainThreadQueries = z;
        this.journalMode = journalMode;
        this.requireMigration = z2;
        this.mMigrationNotRequiredFrom = set;
    }

    public boolean isMigrationRequiredFrom(int i) {
        return this.requireMigration && (this.mMigrationNotRequiredFrom == null || !this.mMigrationNotRequiredFrom.contains(Integer.valueOf(i)));
    }
}
