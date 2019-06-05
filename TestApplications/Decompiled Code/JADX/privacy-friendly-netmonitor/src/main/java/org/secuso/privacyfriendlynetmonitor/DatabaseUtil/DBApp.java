package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import java.util.Map;
import org.greenrobot.greendao.database.Database;
import org.secuso.privacyfriendlynetmonitor.DatabaseUtil.DaoMaster.DevOpenHelper;

public class DBApp extends Application {
    public static final boolean ENCRYPTED = false;
    private static DaoSession daoSession = null;
    private static final String dbVersion = "1";
    private static Editor editor;
    private static DBApp mContext;
    private static SharedPreferences selectedAppsPreferences;

    static class DBAppAsyncTask extends AsyncTask {
        DBAppAsyncTask() {
        }

        /* Access modifiers changed, original: protected */
        public Object doInBackground(Object[] objArr) {
            System.out.println("Starting Database Async Task");
            DevOpenHelper devOpenHelper = new DevOpenHelper(DBApp.mContext, "reports-db");
            Database writableDb = devOpenHelper.getWritableDb();
            Map all = DBApp.selectedAppsPreferences.getAll();
            if (all.isEmpty() || all.get("Version") == null || all.get("Version").equals("")) {
                devOpenHelper.onUpgrade(writableDb, 0, Integer.parseInt(DBApp.dbVersion));
                DBApp.editor.putString("Version", DBApp.dbVersion);
                DBApp.editor.commit();
            } else if (!all.get("Version").equals(DBApp.dbVersion) && Integer.parseInt((String) all.get("Version")) < Integer.parseInt(DBApp.dbVersion)) {
                devOpenHelper.onUpgrade(writableDb, Integer.parseInt((String) all.get("Version")), Integer.parseInt(DBApp.dbVersion));
                DBApp.editor.putString("Version", DBApp.dbVersion);
                DBApp.editor.commit();
            }
            DBApp.daoSession = new DaoMaster(writableDb).newSession();
            return "";
        }
    }

    public void onCreate() {
        super.onCreate();
        mContext = this;
        new DBAppAsyncTask().execute(new Object[]{""});
        selectedAppsPreferences = getSharedPreferences("DBINFO", 0);
        editor = selectedAppsPreferences.edit();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
