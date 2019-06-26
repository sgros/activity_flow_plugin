package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.util.Date;
import org.greenrobot.greendao.AbstractDao;

public class CursorQuery extends AbstractQueryWithLimit {
   private final CursorQuery.QueryData queryData;

   private CursorQuery(CursorQuery.QueryData var1, AbstractDao var2, String var3, String[] var4, int var5, int var6) {
      super(var2, var3, var4, var5, var6);
      this.queryData = var1;
   }

   // $FF: synthetic method
   CursorQuery(CursorQuery.QueryData var1, AbstractDao var2, String var3, String[] var4, int var5, int var6, Object var7) {
      this(var1, var2, var3, var4, var5, var6);
   }

   static CursorQuery create(AbstractDao var0, String var1, Object[] var2, int var3, int var4) {
      return (CursorQuery)(new CursorQuery.QueryData(var0, var1, toStringArray(var2), var3, var4)).forCurrentThread();
   }

   public static CursorQuery internalCreate(AbstractDao var0, String var1, Object[] var2) {
      return create(var0, var1, var2, -1, -1);
   }

   public CursorQuery forCurrentThread() {
      return (CursorQuery)this.queryData.forCurrentThread(this);
   }

   public Cursor query() {
      this.checkThread();
      return this.dao.getDatabase().rawQuery(this.sql, this.parameters);
   }

   public CursorQuery setParameter(int var1, Boolean var2) {
      return (CursorQuery)super.setParameter(var1, (Boolean)var2);
   }

   public CursorQuery setParameter(int var1, Object var2) {
      return (CursorQuery)super.setParameter(var1, var2);
   }

   public CursorQuery setParameter(int var1, Date var2) {
      return (CursorQuery)super.setParameter(var1, (Date)var2);
   }

   private static final class QueryData extends AbstractQueryData {
      private final int limitPosition;
      private final int offsetPosition;

      QueryData(AbstractDao var1, String var2, String[] var3, int var4, int var5) {
         super(var1, var2, var3);
         this.limitPosition = var4;
         this.offsetPosition = var5;
      }

      protected CursorQuery createQuery() {
         return new CursorQuery(this, this.dao, this.sql, (String[])this.initialValues.clone(), this.limitPosition, this.offsetPosition);
      }
   }
}
