package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.util.Date;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;

public class CountQuery extends AbstractQuery {
   private final CountQuery.QueryData queryData;

   private CountQuery(CountQuery.QueryData var1, AbstractDao var2, String var3, String[] var4) {
      super(var2, var3, var4);
      this.queryData = var1;
   }

   // $FF: synthetic method
   CountQuery(CountQuery.QueryData var1, AbstractDao var2, String var3, String[] var4, Object var5) {
      this(var1, var2, var3, var4);
   }

   static CountQuery create(AbstractDao var0, String var1, Object[] var2) {
      return (CountQuery)(new CountQuery.QueryData(var0, var1, toStringArray(var2))).forCurrentThread();
   }

   public long count() {
      this.checkThread();
      Cursor var1 = this.dao.getDatabase().rawQuery(this.sql, this.parameters);

      long var4;
      try {
         DaoException var8;
         if (!var1.moveToNext()) {
            var8 = new DaoException("No result for count");
            throw var8;
         }

         if (!var1.isLast()) {
            StringBuilder var9 = new StringBuilder();
            var9.append("Unexpected row count: ");
            var9.append(var1.getCount());
            var8 = new DaoException(var9.toString());
            throw var8;
         }

         if (var1.getColumnCount() != 1) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Unexpected column count: ");
            var2.append(var1.getColumnCount());
            DaoException var3 = new DaoException(var2.toString());
            throw var3;
         }

         var4 = var1.getLong(0);
      } finally {
         var1.close();
      }

      return var4;
   }

   public CountQuery forCurrentThread() {
      return (CountQuery)this.queryData.forCurrentThread(this);
   }

   public CountQuery setParameter(int var1, Boolean var2) {
      return (CountQuery)super.setParameter(var1, var2);
   }

   public CountQuery setParameter(int var1, Object var2) {
      return (CountQuery)super.setParameter(var1, var2);
   }

   public CountQuery setParameter(int var1, Date var2) {
      return (CountQuery)super.setParameter(var1, var2);
   }

   private static final class QueryData extends AbstractQueryData {
      private QueryData(AbstractDao var1, String var2, String[] var3) {
         super(var1, var2, var3);
      }

      // $FF: synthetic method
      QueryData(AbstractDao var1, String var2, String[] var3, Object var4) {
         this(var1, var2, var3);
      }

      protected CountQuery createQuery() {
         return new CountQuery(this, this.dao, this.sql, (String[])this.initialValues.clone());
      }
   }
}
