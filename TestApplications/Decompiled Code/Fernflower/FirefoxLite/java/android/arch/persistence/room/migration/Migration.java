package android.arch.persistence.room.migration;

import android.arch.persistence.db.SupportSQLiteDatabase;

public abstract class Migration {
   public final int endVersion;
   public final int startVersion;

   public Migration(int var1, int var2) {
      this.startVersion = var1;
      this.endVersion = var2;
   }

   public abstract void migrate(SupportSQLiteDatabase var1);
}
