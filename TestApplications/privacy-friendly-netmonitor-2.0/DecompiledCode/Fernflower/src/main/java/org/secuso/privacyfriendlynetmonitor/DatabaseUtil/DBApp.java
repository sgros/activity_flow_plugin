package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import java.util.Map;
import org.greenrobot.greendao.database.Database;

public class DBApp extends Application {
   public static final boolean ENCRYPTED = false;
   private static DaoSession daoSession;
   private static final String dbVersion = "1";
   private static Editor editor;
   private static DBApp mContext;
   private static SharedPreferences selectedAppsPreferences;

   public DaoSession getDaoSession() {
      return daoSession;
   }

   public void onCreate() {
      super.onCreate();
      mContext = this;
      (new DBApp.DBAppAsyncTask()).execute(new Object[]{""});
      selectedAppsPreferences = this.getSharedPreferences("DBINFO", 0);
      editor = selectedAppsPreferences.edit();
   }

   static class DBAppAsyncTask extends AsyncTask {
      protected Object doInBackground(Object[] var1) {
         System.out.println("Starting Database Async Task");
         DaoMaster.DevOpenHelper var4 = new DaoMaster.DevOpenHelper(DBApp.mContext, "reports-db");
         Database var2 = var4.getWritableDb();
         Map var3 = DBApp.selectedAppsPreferences.getAll();
         if (!var3.isEmpty() && var3.get("Version") != null && !var3.get("Version").equals("")) {
            if (!var3.get("Version").equals("1") && Integer.parseInt((String)var3.get("Version")) < Integer.parseInt("1")) {
               var4.onUpgrade(var2, Integer.parseInt((String)var3.get("Version")), Integer.parseInt("1"));
               DBApp.editor.putString("Version", "1");
               DBApp.editor.commit();
            }
         } else {
            var4.onUpgrade(var2, 0, Integer.parseInt("1"));
            DBApp.editor.putString("Version", "1");
            DBApp.editor.commit();
         }

         DBApp.daoSession = (new DaoMaster(var2)).newSession();
         return "";
      }
   }
}
