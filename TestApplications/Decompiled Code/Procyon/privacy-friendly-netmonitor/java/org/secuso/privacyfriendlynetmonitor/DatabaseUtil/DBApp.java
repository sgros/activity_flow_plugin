// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import java.util.Map;
import org.greenrobot.greendao.database.Database;
import android.content.Context;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.content.SharedPreferences$Editor;
import android.app.Application;

public class DBApp extends Application
{
    public static final boolean ENCRYPTED = false;
    private static DaoSession daoSession;
    private static final String dbVersion = "1";
    private static SharedPreferences$Editor editor;
    private static DBApp mContext;
    private static SharedPreferences selectedAppsPreferences;
    
    public DaoSession getDaoSession() {
        return DBApp.daoSession;
    }
    
    public void onCreate() {
        super.onCreate();
        DBApp.mContext = this;
        new DBAppAsyncTask().execute(new Object[] { "" });
        DBApp.selectedAppsPreferences = this.getSharedPreferences("DBINFO", 0);
        DBApp.editor = DBApp.selectedAppsPreferences.edit();
    }
    
    static class DBAppAsyncTask extends AsyncTask
    {
        protected Object doInBackground(final Object[] array) {
            System.out.println("Starting Database Async Task");
            final DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper((Context)DBApp.mContext, "reports-db");
            final Database writableDb = devOpenHelper.getWritableDb();
            final Map all = DBApp.selectedAppsPreferences.getAll();
            if (!all.isEmpty() && all.get("Version") != null && !all.get("Version").equals("")) {
                if (!all.get("Version").equals("1") && Integer.parseInt(all.get("Version")) < Integer.parseInt("1")) {
                    devOpenHelper.onUpgrade(writableDb, Integer.parseInt(all.get("Version")), Integer.parseInt("1"));
                    DBApp.editor.putString("Version", "1");
                    DBApp.editor.commit();
                }
            }
            else {
                devOpenHelper.onUpgrade(writableDb, 0, Integer.parseInt("1"));
                DBApp.editor.putString("Version", "1");
                DBApp.editor.commit();
            }
            DBApp.daoSession = new DaoMaster(writableDb).newSession();
            return "";
        }
    }
}
