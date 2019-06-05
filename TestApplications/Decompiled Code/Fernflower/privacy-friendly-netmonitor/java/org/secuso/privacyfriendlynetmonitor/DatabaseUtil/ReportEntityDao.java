package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class ReportEntityDao extends AbstractDao {
   public static final String TABLENAME = "REPORTS";

   public ReportEntityDao(DaoConfig var1) {
      super(var1);
   }

   public ReportEntityDao(DaoConfig var1, DaoSession var2) {
      super(var1, var2);
   }

   public static void createTable(Database var0, boolean var1) {
      String var2;
      if (var1) {
         var2 = "IF NOT EXISTS ";
      } else {
         var2 = "";
      }

      StringBuilder var3 = new StringBuilder();
      var3.append("CREATE TABLE ");
      var3.append(var2);
      var3.append("\"REPORTS\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ,\"APP_NAME\" TEXT NOT NULL ,\"USER_ID\" TEXT NOT NULL ,\"TIME_STAMP\" TEXT NOT NULL ,\"REMOTE_ADDRESS\" TEXT NOT NULL ,\"REMOTE_HEX\" TEXT NOT NULL ,\"REMOTE_HOST\" TEXT NOT NULL ,\"LOCAL_ADDRESS\" TEXT NOT NULL ,\"LOCAL_HEX\" TEXT NOT NULL ,\"SERVICE_PORT\" TEXT NOT NULL ,\"PAYLOAD_PROTOCOL\" TEXT NOT NULL ,\"TRANSPORT_PROTOCOL\" TEXT NOT NULL ,\"LOCAL_PORT\" TEXT NOT NULL ,\"CONNECTION_INFO\" TEXT NOT NULL );");
      var0.execSQL(var3.toString());
      var3 = new StringBuilder();
      var3.append("CREATE UNIQUE INDEX ");
      var3.append(var2);
      var3.append("IDX_REPORTS__id_DESC ON \"REPORTS\" (\"_id\" DESC);");
      var0.execSQL(var3.toString());
   }

   public static void dropTable(Database var0, boolean var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("DROP TABLE ");
      String var3;
      if (var1) {
         var3 = "IF EXISTS ";
      } else {
         var3 = "";
      }

      var2.append(var3);
      var2.append("\"REPORTS\"");
      var0.execSQL(var2.toString());
   }

   protected final void bindValues(SQLiteStatement var1, ReportEntity var2) {
      var1.clearBindings();
      Long var3 = var2.getId();
      if (var3 != null) {
         var1.bindLong(1, var3);
      }

      var1.bindString(2, var2.getAppName());
      var1.bindString(3, var2.getUserID());
      var1.bindString(4, var2.getTimeStamp());
      var1.bindString(5, var2.getRemoteAddress());
      var1.bindString(6, var2.getRemoteHex());
      var1.bindString(7, var2.getRemoteHost());
      var1.bindString(8, var2.getLocalAddress());
      var1.bindString(9, var2.getLocalHex());
      var1.bindString(10, var2.getServicePort());
      var1.bindString(11, var2.getPayloadProtocol());
      var1.bindString(12, var2.getTransportProtocol());
      var1.bindString(13, var2.getLocalPort());
      var1.bindString(14, var2.getConnectionInfo());
   }

   protected final void bindValues(DatabaseStatement var1, ReportEntity var2) {
      var1.clearBindings();
      Long var3 = var2.getId();
      if (var3 != null) {
         var1.bindLong(1, var3);
      }

      var1.bindString(2, var2.getAppName());
      var1.bindString(3, var2.getUserID());
      var1.bindString(4, var2.getTimeStamp());
      var1.bindString(5, var2.getRemoteAddress());
      var1.bindString(6, var2.getRemoteHex());
      var1.bindString(7, var2.getRemoteHost());
      var1.bindString(8, var2.getLocalAddress());
      var1.bindString(9, var2.getLocalHex());
      var1.bindString(10, var2.getServicePort());
      var1.bindString(11, var2.getPayloadProtocol());
      var1.bindString(12, var2.getTransportProtocol());
      var1.bindString(13, var2.getLocalPort());
      var1.bindString(14, var2.getConnectionInfo());
   }

   public Long getKey(ReportEntity var1) {
      return var1 != null ? var1.getId() : null;
   }

   public boolean hasKey(ReportEntity var1) {
      boolean var2;
      if (var1.getId() != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   protected final boolean isEntityUpdateable() {
      return true;
   }

   public ReportEntity readEntity(Cursor var1, int var2) {
      int var3 = var2 + 0;
      Long var4;
      if (var1.isNull(var3)) {
         var4 = null;
      } else {
         var4 = var1.getLong(var3);
      }

      return new ReportEntity(var4, var1.getString(var2 + 1), var1.getString(var2 + 2), var1.getString(var2 + 3), var1.getString(var2 + 4), var1.getString(var2 + 5), var1.getString(var2 + 6), var1.getString(var2 + 7), var1.getString(var2 + 8), var1.getString(var2 + 9), var1.getString(var2 + 10), var1.getString(var2 + 11), var1.getString(var2 + 12), var1.getString(var2 + 13));
   }

   public void readEntity(Cursor var1, ReportEntity var2, int var3) {
      int var4 = var3 + 0;
      Long var5;
      if (var1.isNull(var4)) {
         var5 = null;
      } else {
         var5 = var1.getLong(var4);
      }

      var2.setId(var5);
      var2.setAppName(var1.getString(var3 + 1));
      var2.setUserID(var1.getString(var3 + 2));
      var2.setTimeStamp(var1.getString(var3 + 3));
      var2.setRemoteAddress(var1.getString(var3 + 4));
      var2.setRemoteHex(var1.getString(var3 + 5));
      var2.setRemoteHost(var1.getString(var3 + 6));
      var2.setLocalAddress(var1.getString(var3 + 7));
      var2.setLocalHex(var1.getString(var3 + 8));
      var2.setServicePort(var1.getString(var3 + 9));
      var2.setPayloadProtocol(var1.getString(var3 + 10));
      var2.setTransportProtocol(var1.getString(var3 + 11));
      var2.setLocalPort(var1.getString(var3 + 12));
      var2.setConnectionInfo(var1.getString(var3 + 13));
   }

   public Long readKey(Cursor var1, int var2) {
      var2 += 0;
      Long var3;
      if (var1.isNull(var2)) {
         var3 = null;
      } else {
         var3 = var1.getLong(var2);
      }

      return var3;
   }

   protected final Long updateKeyAfterInsert(ReportEntity var1, long var2) {
      var1.setId(var2);
      return var2;
   }

   public static class Properties {
      public static final Property AppName = new Property(1, String.class, "appName", false, "APP_NAME");
      public static final Property ConnectionInfo = new Property(13, String.class, "connectionInfo", false, "CONNECTION_INFO");
      public static final Property Id = new Property(0, Long.class, "id", true, "_id");
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
}
