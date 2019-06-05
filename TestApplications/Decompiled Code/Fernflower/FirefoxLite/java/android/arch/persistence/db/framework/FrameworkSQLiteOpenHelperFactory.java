package android.arch.persistence.db.framework;

import android.arch.persistence.db.SupportSQLiteOpenHelper;

public final class FrameworkSQLiteOpenHelperFactory implements SupportSQLiteOpenHelper.Factory {
   public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration var1) {
      return new FrameworkSQLiteOpenHelper(var1.context, var1.name, var1.callback);
   }
}
