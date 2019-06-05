// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db.framework;

import android.arch.persistence.db.SupportSQLiteOpenHelper;

public final class FrameworkSQLiteOpenHelperFactory implements Factory
{
    @Override
    public SupportSQLiteOpenHelper create(final Configuration configuration) {
        return new FrameworkSQLiteOpenHelper(configuration.context, configuration.name, configuration.callback);
    }
}
