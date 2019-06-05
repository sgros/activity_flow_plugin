// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import org.greenrobot.greendao.database.DatabaseOpenHelper;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.AbstractDaoSession;
import android.content.Context;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import android.database.sqlite.SQLiteDatabase;
import org.greenrobot.greendao.AbstractDaoMaster;

public class DaoMaster extends AbstractDaoMaster
{
    public static final int SCHEMA_VERSION = 1;
    
    public DaoMaster(final SQLiteDatabase sqLiteDatabase) {
        this(new StandardDatabase(sqLiteDatabase));
    }
    
    public DaoMaster(final Database database) {
        super(database, 1);
        this.registerDaoClass(ReportEntityDao.class);
    }
    
    public static void createAllTables(final Database database, final boolean b) {
        ReportEntityDao.createTable(database, b);
    }
    
    public static void dropAllTables(final Database database, final boolean b) {
        ReportEntityDao.dropTable(database, b);
    }
    
    public static DaoSession newDevSession(final Context context, final String s) {
        return new DaoMaster(new DevOpenHelper(context, s).getWritableDb()).newSession();
    }
    
    @Override
    public DaoSession newSession() {
        return new DaoSession(this.db, IdentityScopeType.Session, this.daoConfigMap);
    }
    
    @Override
    public DaoSession newSession(final IdentityScopeType identityScopeType) {
        return new DaoSession(this.db, identityScopeType, this.daoConfigMap);
    }
    
    public static class DevOpenHelper extends OpenHelper
    {
        public DevOpenHelper(final Context context, final String s) {
            super(context, s);
        }
        
        public DevOpenHelper(final Context context, final String s, final SQLiteDatabase$CursorFactory sqLiteDatabase$CursorFactory) {
            super(context, s, sqLiteDatabase$CursorFactory);
        }
        
        @Override
        public void onUpgrade(final Database database, final int i, final int j) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Upgrading schema from version ");
            sb.append(i);
            sb.append(" to ");
            sb.append(j);
            sb.append(" by dropping all tables");
            Log.i("greenDAO", sb.toString());
            DaoMaster.dropAllTables(database, true);
            ((OpenHelper)this).onCreate(database);
        }
    }
    
    public abstract static class OpenHelper extends DatabaseOpenHelper
    {
        public OpenHelper(final Context context, final String s) {
            super(context, s, 1);
        }
        
        public OpenHelper(final Context context, final String s, final SQLiteDatabase$CursorFactory sqLiteDatabase$CursorFactory) {
            super(context, s, sqLiteDatabase$CursorFactory, 1);
        }
        
        @Override
        public void onCreate(final Database database) {
            Log.i("greenDAO", "Creating tables for schema version 1");
            DaoMaster.createAllTables(database, false);
        }
    }
}
