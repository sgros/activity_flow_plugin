package org.greenrobot.greendao;

import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.internal.FastCursor;
import org.greenrobot.greendao.internal.TableStatements;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxDao;
import rx.schedulers.Schedulers;

public abstract class AbstractDao {
   protected final DaoConfig config;
   protected final Database db;
   protected final IdentityScope identityScope;
   protected final IdentityScopeLong identityScopeLong;
   protected final boolean isStandardSQLite;
   protected final int pkOrdinal;
   private volatile RxDao rxDao;
   private volatile RxDao rxDaoPlain;
   protected final AbstractDaoSession session;
   protected final TableStatements statements;

   public AbstractDao(DaoConfig var1) {
      this(var1, (AbstractDaoSession)null);
   }

   public AbstractDao(DaoConfig var1, AbstractDaoSession var2) {
      this.config = var1;
      this.session = var2;
      this.db = var1.db;
      this.isStandardSQLite = this.db.getRawDatabase() instanceof SQLiteDatabase;
      this.identityScope = var1.getIdentityScope();
      if (this.identityScope instanceof IdentityScopeLong) {
         this.identityScopeLong = (IdentityScopeLong)this.identityScope;
      } else {
         this.identityScopeLong = null;
      }

      this.statements = var1.statements;
      int var3;
      if (var1.pkProperty != null) {
         var3 = var1.pkProperty.ordinal;
      } else {
         var3 = -1;
      }

      this.pkOrdinal = var3;
   }

   private void deleteByKeyInsideSynchronized(Object var1, DatabaseStatement var2) {
      if (var1 instanceof Long) {
         var2.bindLong(1, (Long)var1);
      } else {
         if (var1 == null) {
            throw new DaoException("Cannot delete entity, key is null");
         }

         var2.bindString(1, var1.toString());
      }

      var2.execute();
   }

   private void deleteInTxInternal(Iterable param1, Iterable param2) {
      // $FF: Couldn't be decompiled
   }

   private long executeInsert(Object var1, DatabaseStatement var2, boolean var3) {
      long var4;
      if (this.db.isDbLockedByCurrentThread()) {
         var4 = this.insertInsideTx(var1, var2);
      } else {
         this.db.beginTransaction();

         try {
            var4 = this.insertInsideTx(var1, var2);
            this.db.setTransactionSuccessful();
         } finally {
            this.db.endTransaction();
         }
      }

      if (var3) {
         this.updateKeyAfterInsertAndAttach(var1, var4, true);
      }

      return var4;
   }

   private void executeInsertInTx(DatabaseStatement param1, Iterable param2, boolean param3) {
      // $FF: Couldn't be decompiled
   }

   private long insertInsideTx(Object var1, DatabaseStatement var2) {
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label123: {
         long var4;
         try {
            if (this.isStandardSQLite) {
               SQLiteStatement var3 = (SQLiteStatement)var2.getRawStatement();
               this.bindValues(var3, var1);
               var4 = var3.executeInsert();
               return var4;
            }
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            this.bindValues(var2, var1);
            var4 = var2.executeInsert();
            return var4;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var18 = var10000;

         try {
            throw var18;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            continue;
         }
      }
   }

   private void loadAllUnlockOnWindowBounds(Cursor var1, CursorWindow var2, List var3) {
      int var4 = var2.getStartPosition() + var2.getNumRows();
      int var5 = 0;

      while(true) {
         var3.add(this.loadCurrent(var1, 0, false));
         ++var5;
         if (var5 >= var4) {
            var2 = this.moveToNextUnlocked(var1);
            if (var2 == null) {
               break;
            }

            var4 = var2.getStartPosition() + var2.getNumRows();
         } else if (!var1.moveToNext()) {
            break;
         }

         ++var5;
      }

   }

   private CursorWindow moveToNextUnlocked(Cursor var1) {
      this.identityScope.unlock();
      boolean var3 = false;

      CursorWindow var5;
      label36: {
         try {
            var3 = true;
            if (var1.moveToNext()) {
               var5 = ((CrossProcessCursor)var1).getWindow();
               var3 = false;
               break label36;
            }

            var3 = false;
         } finally {
            if (var3) {
               this.identityScope.lock();
            }
         }

         var5 = null;
      }

      this.identityScope.lock();
      return var5;
   }

   protected void assertSinglePk() {
      if (this.config.pkColumns.length != 1) {
         StringBuilder var1 = new StringBuilder();
         var1.append(this);
         var1.append(" (");
         var1.append(this.config.tablename);
         var1.append(") does not have a single-column primary key");
         throw new DaoException(var1.toString());
      }
   }

   protected void attachEntity(Object var1) {
   }

   protected final void attachEntity(Object var1, Object var2, boolean var3) {
      this.attachEntity(var2);
      if (this.identityScope != null && var1 != null) {
         if (var3) {
            this.identityScope.put(var1, var2);
         } else {
            this.identityScope.putNoLock(var1, var2);
         }
      }

   }

   protected abstract void bindValues(SQLiteStatement var1, Object var2);

   protected abstract void bindValues(DatabaseStatement var1, Object var2);

   public long count() {
      return this.statements.getCountStatement().simpleQueryForLong();
   }

   public void delete(Object var1) {
      this.assertSinglePk();
      this.deleteByKey(this.getKeyVerified(var1));
   }

   public void deleteAll() {
      Database var1 = this.db;
      StringBuilder var2 = new StringBuilder();
      var2.append("DELETE FROM '");
      var2.append(this.config.tablename);
      var2.append("'");
      var1.execSQL(var2.toString());
      if (this.identityScope != null) {
         this.identityScope.clear();
      }

   }

   public void deleteByKey(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public void deleteByKeyInTx(Iterable var1) {
      this.deleteInTxInternal((Iterable)null, var1);
   }

   public void deleteByKeyInTx(Object... var1) {
      this.deleteInTxInternal((Iterable)null, Arrays.asList(var1));
   }

   public void deleteInTx(Iterable var1) {
      this.deleteInTxInternal(var1, (Iterable)null);
   }

   public void deleteInTx(Object... var1) {
      this.deleteInTxInternal(Arrays.asList(var1), (Iterable)null);
   }

   public boolean detach(Object var1) {
      if (this.identityScope != null) {
         Object var2 = this.getKeyVerified(var1);
         return this.identityScope.detach(var2, var1);
      } else {
         return false;
      }
   }

   public void detachAll() {
      if (this.identityScope != null) {
         this.identityScope.clear();
      }

   }

   public String[] getAllColumns() {
      return this.config.allColumns;
   }

   public Database getDatabase() {
      return this.db;
   }

   protected abstract Object getKey(Object var1);

   protected Object getKeyVerified(Object var1) {
      Object var2 = this.getKey(var1);
      if (var2 == null) {
         if (var1 == null) {
            throw new NullPointerException("Entity may not be null");
         } else {
            throw new DaoException("Entity has no key");
         }
      } else {
         return var2;
      }
   }

   public String[] getNonPkColumns() {
      return this.config.nonPkColumns;
   }

   public String[] getPkColumns() {
      return this.config.pkColumns;
   }

   public Property getPkProperty() {
      return this.config.pkProperty;
   }

   public Property[] getProperties() {
      return this.config.properties;
   }

   public AbstractDaoSession getSession() {
      return this.session;
   }

   TableStatements getStatements() {
      return this.config.statements;
   }

   public String getTablename() {
      return this.config.tablename;
   }

   protected abstract boolean hasKey(Object var1);

   public long insert(Object var1) {
      return this.executeInsert(var1, this.statements.getInsertStatement(), true);
   }

   public void insertInTx(Iterable var1) {
      this.insertInTx(var1, this.isEntityUpdateable());
   }

   public void insertInTx(Iterable var1, boolean var2) {
      this.executeInsertInTx(this.statements.getInsertStatement(), var1, var2);
   }

   public void insertInTx(Object... var1) {
      this.insertInTx(Arrays.asList(var1), this.isEntityUpdateable());
   }

   public long insertOrReplace(Object var1) {
      return this.executeInsert(var1, this.statements.getInsertOrReplaceStatement(), true);
   }

   public void insertOrReplaceInTx(Iterable var1) {
      this.insertOrReplaceInTx(var1, this.isEntityUpdateable());
   }

   public void insertOrReplaceInTx(Iterable var1, boolean var2) {
      this.executeInsertInTx(this.statements.getInsertOrReplaceStatement(), var1, var2);
   }

   public void insertOrReplaceInTx(Object... var1) {
      this.insertOrReplaceInTx(Arrays.asList(var1), this.isEntityUpdateable());
   }

   public long insertWithoutSettingPk(Object var1) {
      return this.executeInsert(var1, this.statements.getInsertOrReplaceStatement(), false);
   }

   protected abstract boolean isEntityUpdateable();

   public Object load(Object var1) {
      this.assertSinglePk();
      if (var1 == null) {
         return null;
      } else {
         if (this.identityScope != null) {
            Object var2 = this.identityScope.get(var1);
            if (var2 != null) {
               return var2;
            }
         }

         String var4 = this.statements.getSelectByKey();
         String var3 = var1.toString();
         return this.loadUniqueAndCloseCursor(this.db.rawQuery(var4, new String[]{var3}));
      }
   }

   public List loadAll() {
      return this.loadAllAndCloseCursor(this.db.rawQuery(this.statements.getSelectAll(), (String[])null));
   }

   protected List loadAllAndCloseCursor(Cursor var1) {
      List var2;
      try {
         var2 = this.loadAllFromCursor(var1);
      } finally {
         var1.close();
      }

      return var2;
   }

   protected List loadAllFromCursor(Cursor var1) {
      int var2 = ((Cursor)var1).getCount();
      if (var2 == 0) {
         return new ArrayList();
      } else {
         ArrayList var3;
         CursorWindow var4;
         boolean var6;
         label188: {
            var3 = new ArrayList(var2);
            var4 = null;
            if (var1 instanceof CrossProcessCursor) {
               CursorWindow var5 = ((CrossProcessCursor)var1).getWindow();
               var4 = var5;
               if (var5 != null) {
                  if (var5.getNumRows() == var2) {
                     var1 = new FastCursor(var5);
                     var6 = true;
                     var4 = var5;
                     break label188;
                  }

                  StringBuilder var15 = new StringBuilder();
                  var15.append("Window vs. result size: ");
                  var15.append(var5.getNumRows());
                  var15.append("/");
                  var15.append(var2);
                  DaoLog.d(var15.toString());
                  var4 = var5;
               }
            }

            var6 = false;
         }

         if (((Cursor)var1).moveToFirst()) {
            if (this.identityScope != null) {
               this.identityScope.lock();
               this.identityScope.reserveRoom(var2);
            }

            label193: {
               Throwable var10000;
               label178: {
                  boolean var10001;
                  if (!var6 && var4 != null) {
                     try {
                        if (this.identityScope != null) {
                           this.loadAllUnlockOnWindowBounds((Cursor)var1, var4, var3);
                           break label193;
                        }
                     } catch (Throwable var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label178;
                     }
                  }

                  while(true) {
                     boolean var7;
                     try {
                        var3.add(this.loadCurrent((Cursor)var1, 0, false));
                        var7 = ((Cursor)var1).moveToNext();
                     } catch (Throwable var12) {
                        var10000 = var12;
                        var10001 = false;
                        break;
                     }

                     if (!var7) {
                        break label193;
                     }
                  }
               }

               Throwable var14 = var10000;
               if (this.identityScope != null) {
                  this.identityScope.unlock();
               }

               throw var14;
            }

            if (this.identityScope != null) {
               this.identityScope.unlock();
            }
         }

         return var3;
      }
   }

   public Object loadByRowId(long var1) {
      String var3 = Long.toString(var1);
      return this.loadUniqueAndCloseCursor(this.db.rawQuery(this.statements.getSelectByRowId(), new String[]{var3}));
   }

   protected final Object loadCurrent(Cursor var1, int var2, boolean var3) {
      Object var6;
      Object var8;
      if (this.identityScopeLong != null) {
         if (var2 != 0 && var1.isNull(this.pkOrdinal + var2)) {
            return null;
         } else {
            long var4 = var1.getLong(this.pkOrdinal + var2);
            if (var3) {
               var6 = this.identityScopeLong.get2(var4);
            } else {
               var6 = this.identityScopeLong.get2NoLock(var4);
            }

            if (var6 != null) {
               return var6;
            } else {
               var8 = this.readEntity(var1, var2);
               this.attachEntity(var8);
               if (var3) {
                  this.identityScopeLong.put2(var4, var8);
               } else {
                  this.identityScopeLong.put2NoLock(var4, var8);
               }

               return var8;
            }
         }
      } else if (this.identityScope != null) {
         Object var7 = this.readKey(var1, var2);
         if (var2 != 0 && var7 == null) {
            return null;
         } else {
            if (var3) {
               var6 = this.identityScope.get(var7);
            } else {
               var6 = this.identityScope.getNoLock(var7);
            }

            if (var6 != null) {
               return var6;
            } else {
               var8 = this.readEntity(var1, var2);
               this.attachEntity(var7, var8, var3);
               return var8;
            }
         }
      } else if (var2 != 0 && this.readKey(var1, var2) == null) {
         return null;
      } else {
         var8 = this.readEntity(var1, var2);
         this.attachEntity(var8);
         return var8;
      }
   }

   protected final Object loadCurrentOther(AbstractDao var1, Cursor var2, int var3) {
      return var1.loadCurrent(var2, var3, true);
   }

   protected Object loadUnique(Cursor var1) {
      if (!var1.moveToFirst()) {
         return null;
      } else if (!var1.isLast()) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Expected unique result, but count was ");
         var2.append(var1.getCount());
         throw new DaoException(var2.toString());
      } else {
         return this.loadCurrent(var1, 0, true);
      }
   }

   protected Object loadUniqueAndCloseCursor(Cursor var1) {
      Object var2;
      try {
         var2 = this.loadUnique(var1);
      } finally {
         var1.close();
      }

      return var2;
   }

   public QueryBuilder queryBuilder() {
      return QueryBuilder.internalCreate(this);
   }

   public List queryRaw(String var1, String... var2) {
      Database var3 = this.db;
      StringBuilder var4 = new StringBuilder();
      var4.append(this.statements.getSelectAll());
      var4.append(var1);
      return this.loadAllAndCloseCursor(var3.rawQuery(var4.toString(), var2));
   }

   public Query queryRawCreate(String var1, Object... var2) {
      return this.queryRawCreateListArgs(var1, Arrays.asList(var2));
   }

   public Query queryRawCreateListArgs(String var1, Collection var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(this.statements.getSelectAll());
      var3.append(var1);
      return Query.internalCreate(this, var3.toString(), var2.toArray());
   }

   protected abstract Object readEntity(Cursor var1, int var2);

   protected abstract void readEntity(Cursor var1, Object var2, int var3);

   protected abstract Object readKey(Cursor var1, int var2);

   public void refresh(Object var1) {
      this.assertSinglePk();
      Object var2 = this.getKeyVerified(var1);
      String var3 = this.statements.getSelectByKey();
      String var4 = var2.toString();
      Cursor var10 = this.db.rawQuery(var3, new String[]{var4});

      try {
         if (!var10.moveToFirst()) {
            StringBuilder var11 = new StringBuilder();
            var11.append("Entity does not exist in the database anymore: ");
            var11.append(var1.getClass());
            var11.append(" with key ");
            var11.append(var2);
            DaoException var5 = new DaoException(var11.toString());
            throw var5;
         }

         if (!var10.isLast()) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Expected unique result, but count was ");
            var8.append(var10.getCount());
            DaoException var9 = new DaoException(var8.toString());
            throw var9;
         }

         this.readEntity(var10, var1, 0);
         this.attachEntity(var2, var1, true);
      } finally {
         var10.close();
      }

   }

   @Experimental
   public RxDao rx() {
      if (this.rxDao == null) {
         this.rxDao = new RxDao(this, Schedulers.io());
      }

      return this.rxDao;
   }

   @Experimental
   public RxDao rxPlain() {
      if (this.rxDaoPlain == null) {
         this.rxDaoPlain = new RxDao(this);
      }

      return this.rxDaoPlain;
   }

   public void save(Object var1) {
      if (this.hasKey(var1)) {
         this.update(var1);
      } else {
         this.insert(var1);
      }

   }

   public void saveInTx(Iterable var1) {
      Iterator var2 = var1.iterator();
      int var3 = 0;
      int var4 = 0;

      while(var2.hasNext()) {
         if (this.hasKey(var2.next())) {
            ++var3;
         } else {
            ++var4;
         }
      }

      if (var3 > 0 && var4 > 0) {
         ArrayList var5 = new ArrayList(var3);
         ArrayList var10 = new ArrayList(var4);
         Iterator var9 = var1.iterator();

         while(var9.hasNext()) {
            Object var6 = var9.next();
            if (this.hasKey(var6)) {
               var5.add(var6);
            } else {
               var10.add(var6);
            }
         }

         this.db.beginTransaction();

         try {
            this.updateInTx((Iterable)var5);
            this.insertInTx((Iterable)var10);
            this.db.setTransactionSuccessful();
         } finally {
            this.db.endTransaction();
         }
      } else if (var4 > 0) {
         this.insertInTx(var1);
      } else if (var3 > 0) {
         this.updateInTx(var1);
      }

   }

   public void saveInTx(Object... var1) {
      this.saveInTx((Iterable)Arrays.asList(var1));
   }

   public void update(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public void updateInTx(Iterable param1) {
      // $FF: Couldn't be decompiled
   }

   public void updateInTx(Object... var1) {
      this.updateInTx((Iterable)Arrays.asList(var1));
   }

   protected void updateInsideSynchronized(Object var1, SQLiteStatement var2, boolean var3) {
      this.bindValues(var2, var1);
      int var4 = this.config.allColumns.length + 1;
      Object var5 = this.getKey(var1);
      if (var5 instanceof Long) {
         var2.bindLong(var4, (Long)var5);
      } else {
         if (var5 == null) {
            throw new DaoException("Cannot update entity without key - was it inserted before?");
         }

         var2.bindString(var4, var5.toString());
      }

      var2.execute();
      this.attachEntity(var5, var1, var3);
   }

   protected void updateInsideSynchronized(Object var1, DatabaseStatement var2, boolean var3) {
      this.bindValues(var2, var1);
      int var4 = this.config.allColumns.length + 1;
      Object var5 = this.getKey(var1);
      if (var5 instanceof Long) {
         var2.bindLong(var4, (Long)var5);
      } else {
         if (var5 == null) {
            throw new DaoException("Cannot update entity without key - was it inserted before?");
         }

         var2.bindString(var4, var5.toString());
      }

      var2.execute();
      this.attachEntity(var5, var1, var3);
   }

   protected abstract Object updateKeyAfterInsert(Object var1, long var2);

   protected void updateKeyAfterInsertAndAttach(Object var1, long var2, boolean var4) {
      if (var2 != -1L) {
         this.attachEntity(this.updateKeyAfterInsert(var1, var2), var1, var4);
      } else {
         DaoLog.w("Could not insert row (executeInsert returned -1)");
      }

   }
}
