package org.greenrobot.greendao.test;

import android.app.Application;
import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.test.AndroidTestCase;
import java.util.Random;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.DbUtils;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

public abstract class DbTest extends AndroidTestCase {
   public static final String DB_NAME = "greendao-unittest-db.temp";
   private Application application;
   protected Database db;
   protected final boolean inMemory;
   protected final Random random;

   public DbTest() {
      this(true);
   }

   public DbTest(boolean var1) {
      this.inMemory = var1;
      this.random = new Random();
   }

   public Application createApplication(Class var1) {
      assertNull("Application already created", this.application);

      Application var5;
      try {
         var5 = Instrumentation.newApplication(var1, this.getContext());
      } catch (Exception var4) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Could not create application ");
         var2.append(var1);
         throw new RuntimeException(var2.toString(), var4);
      }

      var5.onCreate();
      this.application = var5;
      return var5;
   }

   protected Database createDatabase() {
      SQLiteDatabase var1;
      if (this.inMemory) {
         var1 = SQLiteDatabase.create((CursorFactory)null);
      } else {
         this.getContext().deleteDatabase("greendao-unittest-db.temp");
         var1 = this.getContext().openOrCreateDatabase("greendao-unittest-db.temp", 0, (CursorFactory)null);
      }

      return new StandardDatabase(var1);
   }

   public Application getApplication() {
      assertNotNull("Application not yet created", this.application);
      return this.application;
   }

   protected void logTableDump(String var1) {
      if (this.db instanceof StandardDatabase) {
         DbUtils.logTableDump(((StandardDatabase)this.db).getSQLiteDatabase(), var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Table dump unsupported for ");
         var2.append(this.db);
         DaoLog.w(var2.toString());
      }

   }

   protected void setUp() throws Exception {
      super.setUp();
      this.db = this.createDatabase();
   }

   protected void tearDown() throws Exception {
      if (this.application != null) {
         this.terminateApplication();
      }

      this.db.close();
      if (!this.inMemory) {
         this.getContext().deleteDatabase("greendao-unittest-db.temp");
      }

      super.tearDown();
   }

   public void terminateApplication() {
      assertNotNull("Application not yet created", this.application);
      this.application.onTerminate();
      this.application = null;
   }
}
