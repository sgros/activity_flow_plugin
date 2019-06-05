package org.greenrobot.greendao.query;

import java.util.Date;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

public class DeleteQuery extends AbstractQuery {
   private final DeleteQuery.QueryData queryData;

   private DeleteQuery(DeleteQuery.QueryData var1, AbstractDao var2, String var3, String[] var4) {
      super(var2, var3, var4);
      this.queryData = var1;
   }

   // $FF: synthetic method
   DeleteQuery(DeleteQuery.QueryData var1, AbstractDao var2, String var3, String[] var4, Object var5) {
      this(var1, var2, var3, var4);
   }

   static DeleteQuery create(AbstractDao var0, String var1, Object[] var2) {
      return (DeleteQuery)(new DeleteQuery.QueryData(var0, var1, toStringArray(var2))).forCurrentThread();
   }

   public void executeDeleteWithoutDetachingEntities() {
      this.checkThread();
      Database var1 = this.dao.getDatabase();
      if (var1.isDbLockedByCurrentThread()) {
         this.dao.getDatabase().execSQL(this.sql, this.parameters);
      } else {
         var1.beginTransaction();

         try {
            this.dao.getDatabase().execSQL(this.sql, this.parameters);
            var1.setTransactionSuccessful();
         } finally {
            var1.endTransaction();
         }
      }

   }

   public DeleteQuery forCurrentThread() {
      return (DeleteQuery)this.queryData.forCurrentThread(this);
   }

   public DeleteQuery setParameter(int var1, Boolean var2) {
      return (DeleteQuery)super.setParameter(var1, var2);
   }

   public DeleteQuery setParameter(int var1, Object var2) {
      return (DeleteQuery)super.setParameter(var1, var2);
   }

   public DeleteQuery setParameter(int var1, Date var2) {
      return (DeleteQuery)super.setParameter(var1, var2);
   }

   private static final class QueryData extends AbstractQueryData {
      private QueryData(AbstractDao var1, String var2, String[] var3) {
         super(var1, var2, var3);
      }

      // $FF: synthetic method
      QueryData(AbstractDao var1, String var2, String[] var3, Object var4) {
         this(var1, var2, var3);
      }

      protected DeleteQuery createQuery() {
         return new DeleteQuery(this, this.dao, this.sql, (String[])this.initialValues.clone());
      }
   }
}
