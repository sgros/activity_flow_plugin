// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.test;

import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.DbUtils;
import org.greenrobot.greendao.database.StandardDatabase;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.app.Instrumentation;
import java.util.Random;
import org.greenrobot.greendao.database.Database;
import android.app.Application;
import android.test.AndroidTestCase;

public abstract class DbTest extends AndroidTestCase
{
    public static final String DB_NAME = "greendao-unittest-db.temp";
    private Application application;
    protected Database db;
    protected final boolean inMemory;
    protected final Random random;
    
    public DbTest() {
        this(true);
    }
    
    public DbTest(final boolean inMemory) {
        this.inMemory = inMemory;
        this.random = new Random();
    }
    
    public <T extends Application> T createApplication(final Class<T> obj) {
        assertNull("Application already created", (Object)this.application);
        try {
            final Application application = Instrumentation.newApplication((Class)obj, this.getContext());
            application.onCreate();
            return (T)(this.application = application);
        }
        catch (Exception cause) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not create application ");
            sb.append(obj);
            throw new RuntimeException(sb.toString(), cause);
        }
    }
    
    protected Database createDatabase() {
        SQLiteDatabase sqLiteDatabase;
        if (this.inMemory) {
            sqLiteDatabase = SQLiteDatabase.create((SQLiteDatabase$CursorFactory)null);
        }
        else {
            this.getContext().deleteDatabase("greendao-unittest-db.temp");
            sqLiteDatabase = this.getContext().openOrCreateDatabase("greendao-unittest-db.temp", 0, (SQLiteDatabase$CursorFactory)null);
        }
        return new StandardDatabase(sqLiteDatabase);
    }
    
    public <T extends Application> T getApplication() {
        assertNotNull("Application not yet created", (Object)this.application);
        return (T)this.application;
    }
    
    protected void logTableDump(final String s) {
        if (this.db instanceof StandardDatabase) {
            DbUtils.logTableDump(((StandardDatabase)this.db).getSQLiteDatabase(), s);
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("Table dump unsupported for ");
            sb.append(this.db);
            DaoLog.w(sb.toString());
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
        assertNotNull("Application not yet created", (Object)this.application);
        this.application.onTerminate();
        this.application = null;
    }
}
