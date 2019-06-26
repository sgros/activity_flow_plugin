package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.apihint.Internal;
import org.greenrobot.greendao.rx.RxQuery;
import rx.schedulers.Schedulers;

public class Query extends AbstractQueryWithLimit {
   private final Query.QueryData queryData;
   private volatile RxQuery rxTxIo;
   private volatile RxQuery rxTxPlain;

   private Query(Query.QueryData var1, AbstractDao var2, String var3, String[] var4, int var5, int var6) {
      super(var2, var3, var4, var5, var6);
      this.queryData = var1;
   }

   // $FF: synthetic method
   Query(Query.QueryData var1, AbstractDao var2, String var3, String[] var4, int var5, int var6, Object var7) {
      this(var1, var2, var3, var4, var5, var6);
   }

   static Query create(AbstractDao var0, String var1, Object[] var2, int var3, int var4) {
      return (Query)(new Query.QueryData(var0, var1, toStringArray(var2), var3, var4)).forCurrentThread();
   }

   public static Query internalCreate(AbstractDao var0, String var1, Object[] var2) {
      return create(var0, var1, var2, -1, -1);
   }

   @Internal
   public RxQuery __InternalRx() {
      if (this.rxTxIo == null) {
         this.rxTxIo = new RxQuery(this, Schedulers.io());
      }

      return this.rxTxIo;
   }

   @Internal
   public RxQuery __internalRxPlain() {
      if (this.rxTxPlain == null) {
         this.rxTxPlain = new RxQuery(this);
      }

      return this.rxTxPlain;
   }

   public Query forCurrentThread() {
      return (Query)this.queryData.forCurrentThread(this);
   }

   public List list() {
      this.checkThread();
      Cursor var1 = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
      return this.daoAccess.loadAllAndCloseCursor(var1);
   }

   public CloseableListIterator listIterator() {
      return this.listLazyUncached().listIteratorAutoClose();
   }

   public LazyList listLazy() {
      this.checkThread();
      Cursor var1 = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
      return new LazyList(this.daoAccess, var1, true);
   }

   public LazyList listLazyUncached() {
      this.checkThread();
      Cursor var1 = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
      return new LazyList(this.daoAccess, var1, false);
   }

   public Query setParameter(int var1, Boolean var2) {
      return (Query)super.setParameter(var1, (Boolean)var2);
   }

   public Query setParameter(int var1, Object var2) {
      return (Query)super.setParameter(var1, var2);
   }

   public Query setParameter(int var1, Date var2) {
      return (Query)super.setParameter(var1, (Date)var2);
   }

   public Object unique() {
      this.checkThread();
      Cursor var1 = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
      return this.daoAccess.loadUniqueAndCloseCursor(var1);
   }

   public Object uniqueOrThrow() {
      Object var1 = this.unique();
      if (var1 == null) {
         throw new DaoException("No entity found for query");
      } else {
         return var1;
      }
   }

   private static final class QueryData extends AbstractQueryData {
      private final int limitPosition;
      private final int offsetPosition;

      QueryData(AbstractDao var1, String var2, String[] var3, int var4, int var5) {
         super(var1, var2, var3);
         this.limitPosition = var4;
         this.offsetPosition = var5;
      }

      protected Query createQuery() {
         return new Query(this, this.dao, this.sql, (String[])this.initialValues.clone(), this.limitPosition, this.offsetPosition);
      }
   }
}
