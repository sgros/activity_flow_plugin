package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class ReportEntityDao extends AbstractDao<ReportEntity, Long> {
    public static final String TABLENAME = "REPORTS";

    public static class Properties {
        public static final Property AppName = new Property(1, String.class, "appName", false, "APP_NAME");
        public static final Property ConnectionInfo = new Property(13, String.class, "connectionInfo", false, "CONNECTION_INFO");
        /* renamed from: Id */
        public static final Property f79Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property LocalAddress = new Property(7, String.class, "localAddress", false, "LOCAL_ADDRESS");
        public static final Property LocalHex = new Property(8, String.class, "localHex", false, "LOCAL_HEX");
        public static final Property LocalPort = new Property(12, String.class, "localPort", false, "LOCAL_PORT");
        public static final Property PayloadProtocol = new Property(10, String.class, "payloadProtocol", false, "PAYLOAD_PROTOCOL");
        public static final Property RemoteAddress = new Property(4, String.class, "remoteAddress", false, "REMOTE_ADDRESS");
        public static final Property RemoteHex = new Property(5, String.class, "remoteHex", false, "REMOTE_HEX");
        public static final Property RemoteHost = new Property(6, String.class, "remoteHost", false, "REMOTE_HOST");
        public static final Property ServicePort = new Property(9, String.class, "servicePort", false, "SERVICE_PORT");
        public static final Property TimeStamp = new Property(3, String.class, "timeStamp", false, "TIME_STAMP");
        public static final Property TransportProtocol = new Property(11, String.class, "transportProtocol", false, "TRANSPORT_PROTOCOL");
        public static final Property UserID = new Property(2, String.class, "userID", false, "USER_ID");
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isEntityUpdateable() {
        return true;
    }

    public ReportEntityDao(DaoConfig daoConfig) {
        super(daoConfig);
    }

    public ReportEntityDao(DaoConfig daoConfig, DaoSession daoSession) {
        super(daoConfig, daoSession);
    }

    public static void createTable(Database database, boolean z) {
        String str = z ? "IF NOT EXISTS " : "";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE ");
        stringBuilder.append(str);
        stringBuilder.append("\"REPORTS\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ,\"APP_NAME\" TEXT NOT NULL ,\"USER_ID\" TEXT NOT NULL ,\"TIME_STAMP\" TEXT NOT NULL ,\"REMOTE_ADDRESS\" TEXT NOT NULL ,\"REMOTE_HEX\" TEXT NOT NULL ,\"REMOTE_HOST\" TEXT NOT NULL ,\"LOCAL_ADDRESS\" TEXT NOT NULL ,\"LOCAL_HEX\" TEXT NOT NULL ,\"SERVICE_PORT\" TEXT NOT NULL ,\"PAYLOAD_PROTOCOL\" TEXT NOT NULL ,\"TRANSPORT_PROTOCOL\" TEXT NOT NULL ,\"LOCAL_PORT\" TEXT NOT NULL ,\"CONNECTION_INFO\" TEXT NOT NULL );");
        database.execSQL(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE UNIQUE INDEX ");
        stringBuilder.append(str);
        stringBuilder.append("IDX_REPORTS__id_DESC ON \"REPORTS\" (\"_id\" DESC);");
        database.execSQL(stringBuilder.toString());
    }

    public static void dropTable(Database database, boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DROP TABLE ");
        stringBuilder.append(z ? "IF EXISTS " : "");
        stringBuilder.append("\"REPORTS\"");
        database.execSQL(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(DatabaseStatement databaseStatement, ReportEntity reportEntity) {
        databaseStatement.clearBindings();
        Long id = reportEntity.getId();
        if (id != null) {
            databaseStatement.bindLong(1, id.longValue());
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

    /* Access modifiers changed, original: protected|final */
    public final void bindValues(SQLiteStatement sQLiteStatement, ReportEntity reportEntity) {
        sQLiteStatement.clearBindings();
        Long id = reportEntity.getId();
        if (id != null) {
            sQLiteStatement.bindLong(1, id.longValue());
        }
        sQLiteStatement.bindString(2, reportEntity.getAppName());
        sQLiteStatement.bindString(3, reportEntity.getUserID());
        sQLiteStatement.bindString(4, reportEntity.getTimeStamp());
        sQLiteStatement.bindString(5, reportEntity.getRemoteAddress());
        sQLiteStatement.bindString(6, reportEntity.getRemoteHex());
        sQLiteStatement.bindString(7, reportEntity.getRemoteHost());
        sQLiteStatement.bindString(8, reportEntity.getLocalAddress());
        sQLiteStatement.bindString(9, reportEntity.getLocalHex());
        sQLiteStatement.bindString(10, reportEntity.getServicePort());
        sQLiteStatement.bindString(11, reportEntity.getPayloadProtocol());
        sQLiteStatement.bindString(12, reportEntity.getTransportProtocol());
        sQLiteStatement.bindString(13, reportEntity.getLocalPort());
        sQLiteStatement.bindString(14, reportEntity.getConnectionInfo());
    }

    public Long readKey(Cursor cursor, int i) {
        i += 0;
        return cursor.isNull(i) ? null : Long.valueOf(cursor.getLong(i));
    }

    public ReportEntity readEntity(Cursor cursor, int i) {
        Cursor cursor2 = cursor;
        int i2 = i + 0;
        return new ReportEntity(cursor2.isNull(i2) ? null : Long.valueOf(cursor2.getLong(i2)), cursor2.getString(i + 1), cursor2.getString(i + 2), cursor2.getString(i + 3), cursor2.getString(i + 4), cursor2.getString(i + 5), cursor2.getString(i + 6), cursor2.getString(i + 7), cursor2.getString(i + 8), cursor2.getString(i + 9), cursor2.getString(i + 10), cursor2.getString(i + 11), cursor2.getString(i + 12), cursor2.getString(i + 13));
    }

    public void readEntity(Cursor cursor, ReportEntity reportEntity, int i) {
        int i2 = i + 0;
        reportEntity.setId(cursor.isNull(i2) ? null : Long.valueOf(cursor.getLong(i2)));
        reportEntity.setAppName(cursor.getString(i + 1));
        reportEntity.setUserID(cursor.getString(i + 2));
        reportEntity.setTimeStamp(cursor.getString(i + 3));
        reportEntity.setRemoteAddress(cursor.getString(i + 4));
        reportEntity.setRemoteHex(cursor.getString(i + 5));
        reportEntity.setRemoteHost(cursor.getString(i + 6));
        reportEntity.setLocalAddress(cursor.getString(i + 7));
        reportEntity.setLocalHex(cursor.getString(i + 8));
        reportEntity.setServicePort(cursor.getString(i + 9));
        reportEntity.setPayloadProtocol(cursor.getString(i + 10));
        reportEntity.setTransportProtocol(cursor.getString(i + 11));
        reportEntity.setLocalPort(cursor.getString(i + 12));
        reportEntity.setConnectionInfo(cursor.getString(i + 13));
    }

    /* Access modifiers changed, original: protected|final */
    public final Long updateKeyAfterInsert(ReportEntity reportEntity, long j) {
        reportEntity.setId(Long.valueOf(j));
        return Long.valueOf(j);
    }

    public Long getKey(ReportEntity reportEntity) {
        return reportEntity != null ? reportEntity.getId() : null;
    }

    public boolean hasKey(ReportEntity reportEntity) {
        return reportEntity.getId() != null;
    }
}
