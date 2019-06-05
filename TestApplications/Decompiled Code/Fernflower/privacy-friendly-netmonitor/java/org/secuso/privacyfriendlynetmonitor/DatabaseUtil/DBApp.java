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
         DaoMaster.DevOpenHelper var2 = new DaoMaster.DevOpenHelper(DBApp.mContext, "reports-db");
         Database var3 = var2.getWritableDb();
         Map var4 = DBApp.selectedAppsPreferences.getAll();
         if (!var4.isEmpty() && var4.get("Version") != null && !var4.get("Version").equals("")) {
            if (!var4.get("Version").equals("1") && Integer.parseInt((String)var4.get("Version")) < Integer.parseInt("1")) {
               var2.onUpgrade(var3, Integer.parseInt((String)var4.get("Version")), Integer.parseInt("1"));
               DBApp.editor.putString("Version", "1");
               DBApp.editor.commit();
            }
         } else {
            var2.onUpgrade(var3, 0, Integer.parseInt("1"));
            DBApp.editor.putString("Version", "1");
            DBApp.editor.commit();
         }

         DBApp.daoSession = (new DaoMaster(var3)).newSession();
         return "";
      }
   }
}
