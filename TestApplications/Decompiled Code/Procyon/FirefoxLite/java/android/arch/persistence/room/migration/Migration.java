// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room.migration;

import android.arch.persistence.db.SupportSQLiteDatabase;

public abstract class Migration
{
    public final int endVersion;
    public final int startVersion;
    
    public Migration(final int startVersion, final int endVersion) {
        this.startVersion = startVersion;
        this.endVersion = endVersion;
    }
    
    public abstract void migrate(final SupportSQLiteDatabase p0);
}
