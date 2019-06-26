// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import org.greenrobot.greendao.Property;
import android.database.Cursor;
import org.greenrobot.greendao.database.DatabaseStatement;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.AbstractDao;

public class ReportEntityDao extends AbstractDao<ReportEntity, Long>
{
    public static final String TABLENAME = "REPORTS";
    
    public ReportEntityDao(final DaoConfig daoConfig) {
        super(daoConfig);
    }
    
    public ReportEntityDao(final DaoConfig daoConfig, final DaoSession daoSession) {
        super(daoConfig, daoSession);
    }
    
    public static void createTable(final Database database, final boolean b) {
        String s;
        if (b) {
            s = "IF NOT EXISTS ";
        }
        else {
            s = "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(s);
        sb.append("\"REPORTS\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ,\"APP_NAME\" TEXT NOT NULL ,\"USER_ID\" TEXT NOT NULL ,\"TIME_STAMP\" TEXT NOT NULL ,\"REMOTE_ADDRESS\" TEXT NOT NULL ,\"REMOTE_HEX\" TEXT NOT NULL ,\"REMOTE_HOST\" TEXT NOT NULL ,\"LOCAL_ADDRESS\" TEXT NOT NULL ,\"LOCAL_HEX\" TEXT NOT NULL ,\"SERVICE_PORT\" TEXT NOT NULL ,\"PAYLOAD_PROTOCOL\" TEXT NOT NULL ,\"TRANSPORT_PROTOCOL\" TEXT NOT NULL ,\"LOCAL_PORT\" TEXT NOT NULL ,\"CONNECTION_INFO\" TEXT NOT NULL );");
        database.execSQL(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("CREATE UNIQUE INDEX ");
        sb2.append(s);
        sb2.append("IDX_REPORTS__id_DESC ON \"REPORTS\" (\"_id\" DESC);");
        database.execSQL(sb2.toString());
    }
    
    public static void dropTable(final Database database, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        String str;
        if (b) {
            str = "IF EXISTS ";
        }
        else {
            str = "";
        }
        sb.append(str);
        sb.append("\"REPORTS\"");
        database.execSQL(sb.toString());
    }
    
    @Override
    protected final void bindValues(final SQLiteStatement sqLiteStatement, final ReportEntity reportEntity) {
        sqLiteStatement.clearBindings();
        final Long id = reportEntity.getId();
        if (id != null) {
            sqLiteStatement.bindLong(1, (long)id);
        }
        sqLiteStatement.bindString(2, reportEntity.getAppName());
        sqLiteStatement.bindString(3, reportEntity.getUserID());
        sqLiteStatement.bindString(4, reportEntity.getTimeStamp());
        sqLiteStatement.bindString(5, reportEntity.getRemoteAddress());
        sqLiteStatement.bindString(6, reportEntity.getRemoteHex());
        sqLiteStatement.bindString(7, reportEntity.getRemoteHost());
        sqLiteStatement.bindString(8, reportEntity.getLocalAddress());
        sqLiteStatement.bindString(9, reportEntity.getLocalHex());
        sqLiteStatement.bindString(10, reportEntity.getServicePort());
        sqLiteStatement.bindString(11, reportEntity.getPayloadProtocol());
        sqLiteStatement.bindString(12, reportEntity.getTransportProtocol());
        sqLiteStatement.bindString(13, reportEntity.getLocalPort());
        sqLiteStatement.bindString(14, reportEntity.getConnectionInfo());
    }
    
    @Override
    protected final void bindValues(final DatabaseStatement databaseStatement, final ReportEntity reportEntity) {
        databaseStatement.clearBindings();
        final Long id = reportEntity.getId();
        if (id != null) {
            databaseStatement.bindLong(1, id);
        }
        databaseStatement.bindString(2, reportEntity.getAppName());
        databaseStatement.bindString(3, reportEntity.getUserID());
        databaseStatement.bindString(4, reportEntity.getTimeStamp());
        databaseStatement.bindString(5, reportEntity.getRemoteAddress());
        databaseStatement.bindString(6, reportEntity.getRemoteHex());
        databaseStatement.bindString(7, reportEntity.getRemoteHost());
        databaseStatement.bindString(8, reportEntity.getLocalAddress());
        databaseStatement.bindString(9, reportEntity.getLocalHex());
        databaseStatement.bindString(10, reportEntity.getServicePort());
        databaseStatement.bindString(11, reportEntity.getPayloadProtocol());
        databaseStatement.bindString(12, reportEntity.getTransportProtocol());
        databaseStatement.bindString(13, reportEntity.getLocalPort());
        databaseStatement.bindString(14, reportEntity.getConnectionInfo());
    }
    
    public Long getKey(final ReportEntity reportEntity) {
        if (reportEntity != null) {
            return reportEntity.getId();
        }
        return null;
    }
    
    public boolean hasKey(final ReportEntity reportEntity) {
        return reportEntity.getId() != null;
    }
    
    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    public ReportEntity readEntity(final Cursor cursor, final int n) {
        final int n2 = n + 0;
        Long value;
        if (cursor.isNull(n2)) {
            value = null;
        }
        else {
            value = cursor.getLong(n2);
        }
        return new ReportEntity(value, cursor.getString(n + 1), cursor.getString(n + 2), cursor.getString(n + 3), cursor.getString(n + 4), cursor.getString(n + 5), cursor.getString(n + 6), cursor.getString(n + 7), cursor.getString(n + 8), cursor.getString(n + 9), cursor.getString(n + 10), cursor.getString(n + 11), cursor.getString(n + 12), cursor.getString(n + 13));
    }
    
    public void readEntity(final Cursor cursor, final ReportEntity reportEntity, final int n) {
        final int n2 = n + 0;
        Long value;
        if (cursor.isNull(n2)) {
            value = null;
        }
        else {
            value = cursor.getLong(n2);
        }
        reportEntity.setId(value);
        reportEntity.setAppName(cursor.getString(n + 1));
        reportEntity.setUserID(cursor.getString(n + 2));
        reportEntity.setTimeStamp(cursor.getString(n + 3));
        reportEntity.setRemoteAddress(cursor.getString(n + 4));
        reportEntity.setRemoteHex(cursor.getString(n + 5));
        reportEntity.setRemoteHost(cursor.getString(n + 6));
        reportEntity.setLocalAddress(cursor.getString(n + 7));
        reportEntity.setLocalHex(cursor.getString(n + 8));
        reportEntity.setServicePort(cursor.getString(n + 9));
        reportEntity.setPayloadProtocol(cursor.getString(n + 10));
        reportEntity.setTransportProtocol(cursor.getString(n + 11));
        reportEntity.setLocalPort(cursor.getString(n + 12));
        reportEntity.setConnectionInfo(cursor.getString(n + 13));
    }
    
    public Long readKey(final Cursor cursor, int n) {
        n += 0;
        Long value;
        if (cursor.isNull(n)) {
            value = null;
        }
        else {
            value = cursor.getLong(n);
        }
        return value;
    }
    
    @Override
    protected final Long updateKeyAfterInsert(final ReportEntity reportEntity, final long n) {
        reportEntity.setId(n);
        return n;
    }
    
    public static class Properties
    {
        public static final Property AppName;
        public static final Property ConnectionInfo;
        public static final Property Id;
        public static final Property LocalAddress;
        public static final Property LocalHex;
        public static final Property LocalPort;
        public static final Property PayloadProtocol;
        public static final Property RemoteAddress;
        public static final Property RemoteHex;
        public static final Property RemoteHost;
        public static final Property ServicePort;
        public static final Property TimeStamp;
        public static final Property TransportProtocol;
        public static final Property UserID;
        
        static {
            Id = new Property(0, Long.class, "id", true, "_id");
            AppName = new Property(1, String.class, "appName", false, "APP_NAME");
            UserID = new Property(2, String.class, "userID", false, "USER_ID");
            TimeStamp = new Property(3, String.class, "timeStamp", false, "TIME_STAMP");
            RemoteAddress = new Property(4, String.class, "remoteAddress", false, "REMOTE_ADDRESS");
            RemoteHex = new Property(5, String.class, "remoteHex", false, "REMOTE_HEX");
            RemoteHost = new Property(6, String.class, "remoteHost", false, "REMOTE_HOST");
            LocalAddress = new Property(7, String.class, "localAddress", false, "LOCAL_ADDRESS");
            LocalHex = new Property(8, String.class, "localHex", false, "LOCAL_HEX");
            ServicePort = new Property(9, String.class, "servicePort", false, "SERVICE_PORT");
            PayloadProtocol = new Property(10, String.class, "payloadProtocol", false, "PAYLOAD_PROTOCOL");
            TransportProtocol = new Property(11, String.class, "transportProtocol", false, "TRANSPORT_PROTOCOL");
            LocalPort = new Property(12, String.class, "localPort", false, "LOCAL_PORT");
            ConnectionInfo = new Property(13, String.class, "connectionInfo", false, "CONNECTION_INFO");
        }
    }
}
