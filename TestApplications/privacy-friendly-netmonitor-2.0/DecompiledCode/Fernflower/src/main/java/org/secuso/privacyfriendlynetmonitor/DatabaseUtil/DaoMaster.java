package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

public class DaoMaster extends AbstractDaoMaster {
   public static final int SCHEMA_VERSION = 1;

   public DaoMaster(SQLiteDatabase var1) {
      this((Database)(new StandardDatabase(var1)));
   }

   public DaoMaster(Database var1) {
      super(var1, 1);
      this.registerDaoClass(ReportEntityDao.class);
   }

   public static void createAllTables(Database var0, boolean var1) {
      ReportEntityDao.createTable(var0, var1);
   }

   public static void dropAllTables(Database var0, boolean var1) {
      ReportEntityDao.dropTable(var0, var1);
   }

   public static DaoSession newDevSession(Context var0, String var1) {
      return (new DaoMaster((new DaoMaster.DevOpenHelper(var0, var1)).getWritableDb())).newSession();
   }

   public DaoSession newSession() {
      return new DaoSession(this.db, IdentityScopeType.Session, this.daoConfigMap);
   }

   public DaoSession newSession(IdentityScopeType var1) {
      return new DaoSession(this.db, var1, this.daoConfigMap);
   }

   public static class DevOpenHelper extends DaoMaster.OpenHelper {
      public DevOpenHelper(Context var1, String var2) {
         super(var1, var2);
      }

      public DevOpenHelper(Context var1, String var2, CursorFactory var3) {
         super(var1, var2, var3);
      }

      public void onUpgrade(Database var1, int var2, int var3) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Upgrading schema from version ");
         var4.append(var2);
         var4.append(" to ");
         var4.append(var3);
         var4.append(" by dropping all tables");
         Log.i("greenDAO", var4.toString());
         DaoMaster.dropAllTables(var1, true);
         this.onCreate(var1);
      }
   }

   public abstract static class OpenHelper extends DatabaseOpenHelper {
      public OpenHelper(Context var1, String var2) {
         super(var1, var2, 1);
      }

      public OpenHelper(Context var1, String var2, CursorFactory var3) {
         super(var1, var2, var3, 1);
      }

      public void onCreate(Database var1) {
         Log.i("greenDAO", "Creating tables for schema version 1");
         DaoMaster.createAllTables(var1, false);
      }
   }
}
