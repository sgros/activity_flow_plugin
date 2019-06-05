package org.greenrobot.greendao;

import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.internal.TableStatements;
import org.greenrobot.greendao.p005rx.RxDao;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import rx.schedulers.Schedulers;

public abstract class AbstractDao<T, K> {
    protected final DaoConfig config;
    /* renamed from: db */
    protected final Database f69db;
    protected final IdentityScope<K, T> identityScope;
    protected final IdentityScopeLong<T> identityScopeLong;
    protected final boolean isStandardSQLite;
    protected final int pkOrdinal;
    private volatile RxDao<T, K> rxDao;
    private volatile RxDao<T, K> rxDaoPlain;
    protected final AbstractDaoSession session;
    protected final TableStatements statements;

    /* Access modifiers changed, original: protected */
    public void attachEntity(T t) {
    }

    public abstract void bindValues(SQLiteStatement sQLiteStatement, T t);

    public abstract void bindValues(DatabaseStatement databaseStatement, T t);

    public abstract K getKey(T t);

    public abstract boolean hasKey(T t);

    public abstract boolean isEntityUpdateable();

    public abstract T readEntity(Cursor cursor, int i);

    public abstract void readEntity(Cursor cursor, T t, int i);

    public abstract K readKey(Cursor cursor, int i);

    public abstract K updateKeyAfterInsert(T t, long j);

    public AbstractDao(DaoConfig daoConfig) {
        this(daoConfig, null);
    }

    public AbstractDao(DaoConfig daoConfig, AbstractDaoSession abstractDaoSession) {
        this.config = daoConfig;
        this.session = abstractDaoSession;
        this.f69db = daoConfig.f73db;
        this.isStandardSQLite = this.f69db.getRawDatabase() instanceof SQLiteDatabase;
        this.identityScope = daoConfig.getIdentityScope();
        if (this.identityScope instanceof IdentityScopeLong) {
            this.identityScopeLong = (IdentityScopeLong) this.identityScope;
        } else {
            this.identityScopeLong = null;
        }
        this.statements = daoConfig.statements;
        this.pkOrdinal = daoConfig.pkProperty != null ? daoConfig.pkProperty.ordinal : -1;
    }

    public AbstractDaoSession getSession() {
        return this.session;
    }

    /* Access modifiers changed, original: 0000 */
    public TableStatements getStatements() {
        return this.config.statements;
    }

    public String getTablename() {
        return this.config.tablename;
    }

    public Property[] getProperties() {
        return this.config.properties;
    }

    public Property getPkProperty() {
        return this.config.pkProperty;
    }

    public String[] getAllColumns() {
        return this.config.allColumns;
    }

    public String[] getPkColumns() {
        return this.config.pkColumns;
    }

    public String[] getNonPkColumns() {
        return this.config.nonPkColumns;
    }

    public T load(K k) {
        assertSinglePk();
        if (k == null) {
            return null;
        }
        if (this.identityScope != null) {
            Object obj = this.identityScope.get(k);
            if (obj != null) {
                return obj;
            }
        }
        return loadUniqueAndCloseCursor(this.f69db.rawQuery(this.statements.getSelectByKey(), new String[]{k.toString()}));
    }

    public T loadByRowId(long j) {
        return loadUniqueAndCloseCursor(this.f69db.rawQuery(this.statements.getSelectByRowId(), new String[]{Long.toString(j)}));
    }

    /* Access modifiers changed, original: protected */
    public T loadUniqueAndCloseCursor(Cursor cursor) {
        try {
            T loadUnique = loadUnique(cursor);
            return loadUnique;
        } finally {
            cursor.close();
        }
    }

    /* Access modifiers changed, original: protected */
    public T loadUnique(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }
        if (cursor.isLast()) {
            return loadCurrent(cursor, 0, true);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected unique result, but count was ");
        stringBuilder.append(cursor.getCount());
        throw new DaoException(stringBuilder.toString());
    }

    public List<T> loadAll() {
        return loadAllAndCloseCursor(this.f69db.rawQuery(this.statements.getSelectAll(), null));
    }

    public boolean detach(T t) {
        if (this.identityScope == null) {
            return false;
        }
        return this.identityScope.detach(getKeyVerified(t), t);
    }

    public void detachAll() {
        if (this.identityScope != null) {
            this.identityScope.clear();
        }
    }

    /* Access modifiers changed, original: protected */
    public List<T> loadAllAndCloseCursor(Cursor cursor) {
        try {
            List<T> loadAllFromCursor = loadAllFromCursor(cursor);
            return loadAllFromCursor;
        } finally {
            cursor.close();
        }
    }

    public void insertInTx(Iterable<T> iterable) {
        insertInTx(iterable, isEntityUpdateable());
    }

    public void insertInTx(T... tArr) {
        insertInTx(Arrays.asList(tArr), isEntityUpdateable());
    }

    public void insertInTx(Iterable<T> iterable, boolean z) {
        executeInsertInTx(this.statements.getInsertStatement(), iterable, z);
    }

    public void insertOrReplaceInTx(Iterable<T> iterable, boolean z) {
        executeInsertInTx(this.statements.getInsertOrReplaceStatement(), iterable, z);
    }

    public void insertOrReplaceInTx(Iterable<T> iterable) {
        insertOrReplaceInTx(iterable, isEntityUpdateable());
    }

    public void insertOrReplaceInTx(T... tArr) {
        insertOrReplaceInTx(Arrays.asList(tArr), isEntityUpdateable());
    }

    /* JADX WARNING: Missing block: B:35:0x007c, code skipped:
            r5.f69db.endTransaction();
     */
    private void executeInsertInTx(org.greenrobot.greendao.database.DatabaseStatement r6, java.lang.Iterable<T> r7, boolean r8) {
        /*
        r5 = this;
        r0 = r5.f69db;
        r0.beginTransaction();
        monitor-enter(r6);	 Catch:{ all -> 0x007b }
        r0 = r5.identityScope;	 Catch:{ all -> 0x0078 }
        if (r0 == 0) goto L_0x000f;
    L_0x000a:
        r0 = r5.identityScope;	 Catch:{ all -> 0x0078 }
        r0.lock();	 Catch:{ all -> 0x0078 }
    L_0x000f:
        r0 = r5.isStandardSQLite;	 Catch:{ all -> 0x006d }
        r1 = 0;
        if (r0 == 0) goto L_0x0039;
    L_0x0014:
        r0 = r6.getRawStatement();	 Catch:{ all -> 0x006d }
        r0 = (android.database.sqlite.SQLiteStatement) r0;	 Catch:{ all -> 0x006d }
        r7 = r7.iterator();	 Catch:{ all -> 0x006d }
    L_0x001e:
        r2 = r7.hasNext();	 Catch:{ all -> 0x006d }
        if (r2 == 0) goto L_0x0058;
    L_0x0024:
        r2 = r7.next();	 Catch:{ all -> 0x006d }
        r5.bindValues(r0, r2);	 Catch:{ all -> 0x006d }
        if (r8 == 0) goto L_0x0035;
    L_0x002d:
        r3 = r0.executeInsert();	 Catch:{ all -> 0x006d }
        r5.updateKeyAfterInsertAndAttach(r2, r3, r1);	 Catch:{ all -> 0x006d }
        goto L_0x001e;
    L_0x0035:
        r0.execute();	 Catch:{ all -> 0x006d }
        goto L_0x001e;
    L_0x0039:
        r7 = r7.iterator();	 Catch:{ all -> 0x006d }
    L_0x003d:
        r0 = r7.hasNext();	 Catch:{ all -> 0x006d }
        if (r0 == 0) goto L_0x0058;
    L_0x0043:
        r0 = r7.next();	 Catch:{ all -> 0x006d }
        r5.bindValues(r6, r0);	 Catch:{ all -> 0x006d }
        if (r8 == 0) goto L_0x0054;
    L_0x004c:
        r2 = r6.executeInsert();	 Catch:{ all -> 0x006d }
        r5.updateKeyAfterInsertAndAttach(r0, r2, r1);	 Catch:{ all -> 0x006d }
        goto L_0x003d;
    L_0x0054:
        r6.execute();	 Catch:{ all -> 0x006d }
        goto L_0x003d;
    L_0x0058:
        r7 = r5.identityScope;	 Catch:{ all -> 0x0078 }
        if (r7 == 0) goto L_0x0061;
    L_0x005c:
        r7 = r5.identityScope;	 Catch:{ all -> 0x0078 }
        r7.unlock();	 Catch:{ all -> 0x0078 }
    L_0x0061:
        monitor-exit(r6);	 Catch:{ all -> 0x0078 }
        r6 = r5.f69db;	 Catch:{ all -> 0x007b }
        r6.setTransactionSuccessful();	 Catch:{ all -> 0x007b }
        r6 = r5.f69db;
        r6.endTransaction();
        return;
    L_0x006d:
        r7 = move-exception;
        r8 = r5.identityScope;	 Catch:{ all -> 0x0078 }
        if (r8 == 0) goto L_0x0077;
    L_0x0072:
        r8 = r5.identityScope;	 Catch:{ all -> 0x0078 }
        r8.unlock();	 Catch:{ all -> 0x0078 }
    L_0x0077:
        throw r7;	 Catch:{ all -> 0x0078 }
    L_0x0078:
        r7 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0078 }
        throw r7;	 Catch:{ all -> 0x007b }
    L_0x007b:
        r6 = move-exception;
        r7 = r5.f69db;
        r7.endTransaction();
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.greenrobot.greendao.AbstractDao.executeInsertInTx(org.greenrobot.greendao.database.DatabaseStatement, java.lang.Iterable, boolean):void");
    }

    public long insert(T t) {
        return executeInsert(t, this.statements.getInsertStatement(), true);
    }

    public long insertWithoutSettingPk(T t) {
        return executeInsert(t, this.statements.getInsertOrReplaceStatement(), false);
    }

    public long insertOrReplace(T t) {
        return executeInsert(t, this.statements.getInsertOrReplaceStatement(), true);
    }

    private long executeInsert(T t, DatabaseStatement databaseStatement, boolean z) {
        long insertInsideTx;
        if (this.f69db.isDbLockedByCurrentThread()) {
            insertInsideTx = insertInsideTx(t, databaseStatement);
        } else {
            insertInsideTx = this.f69db;
            insertInsideTx.beginTransaction();
            try {
                insertInsideTx = insertInsideTx(t, databaseStatement);
                this.f69db.setTransactionSuccessful();
            } finally {
                this.f69db.endTransaction();
            }
        }
        if (z) {
            updateKeyAfterInsertAndAttach(t, insertInsideTx, true);
        }
        return insertInsideTx;
    }

    private long insertInsideTx(T t, DatabaseStatement databaseStatement) {
        synchronized (databaseStatement) {
            long executeInsert;
            if (this.isStandardSQLite) {
                SQLiteStatement sQLiteStatement = (SQLiteStatement) databaseStatement.getRawStatement();
                bindValues(sQLiteStatement, (Object) t);
                executeInsert = sQLiteStatement.executeInsert();
                return executeInsert;
            }
            bindValues(databaseStatement, (Object) t);
            executeInsert = databaseStatement.executeInsert();
            return executeInsert;
        }
    }

    /* Access modifiers changed, original: protected */
    public void updateKeyAfterInsertAndAttach(T t, long j, boolean z) {
        if (j != -1) {
            attachEntity(updateKeyAfterInsert(t, j), t, z);
        } else {
            DaoLog.m19w("Could not insert row (executeInsert returned -1)");
        }
    }

    public void save(T t) {
        if (hasKey(t)) {
            update(t);
        } else {
            insert(t);
        }
    }

    public void saveInTx(T... tArr) {
        saveInTx(Arrays.asList(tArr));
    }

    public void saveInTx(Iterable<T> iterable) {
        int i = 0;
        int i2 = 0;
        for (T hasKey : iterable) {
            if (hasKey(hasKey)) {
                i++;
            } else {
                i2++;
            }
        }
        if (i > 0 && i2 > 0) {
            Iterable arrayList = new ArrayList(i);
            Iterable arrayList2 = new ArrayList(i2);
            for (Object next : iterable) {
                if (hasKey(next)) {
                    arrayList.add(next);
                } else {
                    arrayList2.add(next);
                }
            }
            this.f69db.beginTransaction();
            try {
                updateInTx(arrayList);
                insertInTx(arrayList2);
                this.f69db.setTransactionSuccessful();
            } finally {
                this.f69db.endTransaction();
            }
        } else if (i2 > 0) {
            insertInTx((Iterable) iterable);
        } else if (i > 0) {
            updateInTx((Iterable) iterable);
        }
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0054  */
    public java.util.List<T> loadAllFromCursor(android.database.Cursor r7) {
        /*
        r6 = this;
        r0 = r7.getCount();
        if (r0 != 0) goto L_0x000c;
    L_0x0006:
        r7 = new java.util.ArrayList;
        r7.<init>();
        return r7;
    L_0x000c:
        r1 = new java.util.ArrayList;
        r1.<init>(r0);
        r2 = 0;
        r3 = r7 instanceof android.database.CrossProcessCursor;
        r4 = 0;
        if (r3 == 0) goto L_0x004d;
    L_0x0017:
        r2 = r7;
        r2 = (android.database.CrossProcessCursor) r2;
        r2 = r2.getWindow();
        if (r2 == 0) goto L_0x004d;
    L_0x0020:
        r3 = r2.getNumRows();
        if (r3 != r0) goto L_0x002d;
    L_0x0026:
        r7 = new org.greenrobot.greendao.internal.FastCursor;
        r7.<init>(r2);
        r3 = 1;
        goto L_0x004e;
    L_0x002d:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Window vs. result size: ";
        r3.append(r5);
        r5 = r2.getNumRows();
        r3.append(r5);
        r5 = "/";
        r3.append(r5);
        r3.append(r0);
        r3 = r3.toString();
        org.greenrobot.greendao.DaoLog.m11d(r3);
    L_0x004d:
        r3 = r4;
    L_0x004e:
        r5 = r7.moveToFirst();
        if (r5 == 0) goto L_0x0090;
    L_0x0054:
        r5 = r6.identityScope;
        if (r5 == 0) goto L_0x0062;
    L_0x0058:
        r5 = r6.identityScope;
        r5.lock();
        r5 = r6.identityScope;
        r5.reserveRoom(r0);
    L_0x0062:
        if (r3 != 0) goto L_0x006e;
    L_0x0064:
        if (r2 == 0) goto L_0x006e;
    L_0x0066:
        r0 = r6.identityScope;	 Catch:{ all -> 0x0085 }
        if (r0 == 0) goto L_0x006e;
    L_0x006a:
        r6.loadAllUnlockOnWindowBounds(r7, r2, r1);	 Catch:{ all -> 0x0085 }
        goto L_0x007b;
    L_0x006e:
        r0 = r6.loadCurrent(r7, r4, r4);	 Catch:{ all -> 0x0085 }
        r1.add(r0);	 Catch:{ all -> 0x0085 }
        r0 = r7.moveToNext();	 Catch:{ all -> 0x0085 }
        if (r0 != 0) goto L_0x006e;
    L_0x007b:
        r7 = r6.identityScope;
        if (r7 == 0) goto L_0x0090;
    L_0x007f:
        r7 = r6.identityScope;
        r7.unlock();
        goto L_0x0090;
    L_0x0085:
        r7 = move-exception;
        r0 = r6.identityScope;
        if (r0 == 0) goto L_0x008f;
    L_0x008a:
        r0 = r6.identityScope;
        r0.unlock();
    L_0x008f:
        throw r7;
    L_0x0090:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.greenrobot.greendao.AbstractDao.loadAllFromCursor(android.database.Cursor):java.util.List");
    }

    private void loadAllUnlockOnWindowBounds(Cursor cursor, CursorWindow cursorWindow, List<T> list) {
        int startPosition = cursorWindow.getStartPosition() + cursorWindow.getNumRows();
        int i = 0;
        while (true) {
            list.add(loadCurrent(cursor, 0, false));
            i++;
            if (i >= startPosition) {
                CursorWindow moveToNextUnlocked = moveToNextUnlocked(cursor);
                if (moveToNextUnlocked != null) {
                    startPosition = moveToNextUnlocked.getStartPosition() + moveToNextUnlocked.getNumRows();
                } else {
                    return;
                }
            } else if (!cursor.moveToNext()) {
                return;
            }
            i++;
        }
    }

    private CursorWindow moveToNextUnlocked(Cursor cursor) {
        this.identityScope.unlock();
        try {
            CursorWindow window;
            if (cursor.moveToNext()) {
                window = ((CrossProcessCursor) cursor).getWindow();
            } else {
                window = null;
            }
            this.identityScope.lock();
            return window;
        } catch (Throwable th) {
            this.identityScope.lock();
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final T loadCurrent(Cursor cursor, int i, boolean z) {
        Object readEntity;
        if (this.identityScopeLong != null) {
            if (i != 0 && cursor.isNull(this.pkOrdinal + i)) {
                return null;
            }
            long j = cursor.getLong(this.pkOrdinal + i);
            T 2 = z ? this.identityScopeLong.get2(j) : this.identityScopeLong.get2NoLock(j);
            if (2 != null) {
                return 2;
            }
            readEntity = readEntity(cursor, i);
            attachEntity(readEntity);
            if (z) {
                this.identityScopeLong.put2(j, readEntity);
            } else {
                this.identityScopeLong.put2NoLock(j, readEntity);
            }
            return readEntity;
        } else if (this.identityScope != null) {
            Object readKey = readKey(cursor, i);
            if (i != 0 && readKey == null) {
                return null;
            }
            T noLock = z ? this.identityScope.get(readKey) : this.identityScope.getNoLock(readKey);
            if (noLock != null) {
                return noLock;
            }
            readEntity = readEntity(cursor, i);
            attachEntity(readKey, readEntity, z);
            return readEntity;
        } else if (i != 0 && readKey(cursor, i) == null) {
            return null;
        } else {
            readEntity = readEntity(cursor, i);
            attachEntity(readEntity);
            return readEntity;
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final <O> O loadCurrentOther(AbstractDao<O, ?> abstractDao, Cursor cursor, int i) {
        return abstractDao.loadCurrent(cursor, i, true);
    }

    public List<T> queryRaw(String str, String... strArr) {
        Database database = this.f69db;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.statements.getSelectAll());
        stringBuilder.append(str);
        return loadAllAndCloseCursor(database.rawQuery(stringBuilder.toString(), strArr));
    }

    public Query<T> queryRawCreate(String str, Object... objArr) {
        return queryRawCreateListArgs(str, Arrays.asList(objArr));
    }

    public Query<T> queryRawCreateListArgs(String str, Collection<Object> collection) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.statements.getSelectAll());
        stringBuilder.append(str);
        return Query.internalCreate(this, stringBuilder.toString(), collection.toArray());
    }

    public void deleteAll() {
        Database database = this.f69db;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM '");
        stringBuilder.append(this.config.tablename);
        stringBuilder.append("'");
        database.execSQL(stringBuilder.toString());
        if (this.identityScope != null) {
            this.identityScope.clear();
        }
    }

    public void delete(T t) {
        assertSinglePk();
        deleteByKey(getKeyVerified(t));
    }

    public void deleteByKey(K k) {
        assertSinglePk();
        DatabaseStatement deleteStatement = this.statements.getDeleteStatement();
        if (this.f69db.isDbLockedByCurrentThread()) {
            synchronized (deleteStatement) {
                deleteByKeyInsideSynchronized(k, deleteStatement);
            }
        } else {
            this.f69db.beginTransaction();
            try {
                synchronized (deleteStatement) {
                    deleteByKeyInsideSynchronized(k, deleteStatement);
                }
                this.f69db.setTransactionSuccessful();
                this.f69db.endTransaction();
            } catch (Throwable th) {
                this.f69db.endTransaction();
            }
        }
        if (this.identityScope != null) {
            this.identityScope.remove((Object) k);
        }
    }

    private void deleteByKeyInsideSynchronized(K k, DatabaseStatement databaseStatement) {
        if (k instanceof Long) {
            databaseStatement.bindLong(1, ((Long) k).longValue());
        } else if (k == null) {
            throw new DaoException("Cannot delete entity, key is null");
        } else {
            databaseStatement.bindString(1, k.toString());
        }
        databaseStatement.execute();
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002b A:{Catch:{ all -> 0x003c }} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002b A:{Catch:{ all -> 0x003c }} */
    private void deleteInTxInternal(java.lang.Iterable<T> r4, java.lang.Iterable<K> r5) {
        /*
        r3 = this;
        r3.assertSinglePk();
        r0 = r3.statements;
        r0 = r0.getDeleteStatement();
        r1 = r3.f69db;
        r1.beginTransaction();
        monitor-enter(r0);	 Catch:{ all -> 0x0084 }
        r1 = r3.identityScope;	 Catch:{ all -> 0x0081 }
        if (r1 == 0) goto L_0x001e;
    L_0x0013:
        r1 = r3.identityScope;	 Catch:{ all -> 0x0081 }
        r1.lock();	 Catch:{ all -> 0x0081 }
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x0081 }
        r1.<init>();	 Catch:{ all -> 0x0081 }
        goto L_0x001f;
    L_0x001e:
        r1 = 0;
    L_0x001f:
        if (r4 == 0) goto L_0x003e;
    L_0x0021:
        r4 = r4.iterator();	 Catch:{ all -> 0x003c }
    L_0x0025:
        r2 = r4.hasNext();	 Catch:{ all -> 0x003c }
        if (r2 == 0) goto L_0x003e;
    L_0x002b:
        r2 = r4.next();	 Catch:{ all -> 0x003c }
        r2 = r3.getKeyVerified(r2);	 Catch:{ all -> 0x003c }
        r3.deleteByKeyInsideSynchronized(r2, r0);	 Catch:{ all -> 0x003c }
        if (r1 == 0) goto L_0x0025;
    L_0x0038:
        r1.add(r2);	 Catch:{ all -> 0x003c }
        goto L_0x0025;
    L_0x003c:
        r4 = move-exception;
        goto L_0x0057;
    L_0x003e:
        if (r5 == 0) goto L_0x0061;
    L_0x0040:
        r4 = r5.iterator();	 Catch:{ all -> 0x003c }
    L_0x0044:
        r5 = r4.hasNext();	 Catch:{ all -> 0x003c }
        if (r5 == 0) goto L_0x0061;
    L_0x004a:
        r5 = r4.next();	 Catch:{ all -> 0x003c }
        r3.deleteByKeyInsideSynchronized(r5, r0);	 Catch:{ all -> 0x003c }
        if (r1 == 0) goto L_0x0044;
    L_0x0053:
        r1.add(r5);	 Catch:{ all -> 0x003c }
        goto L_0x0044;
    L_0x0057:
        r5 = r3.identityScope;	 Catch:{ all -> 0x0081 }
        if (r5 == 0) goto L_0x0060;
    L_0x005b:
        r5 = r3.identityScope;	 Catch:{ all -> 0x0081 }
        r5.unlock();	 Catch:{ all -> 0x0081 }
    L_0x0060:
        throw r4;	 Catch:{ all -> 0x0081 }
    L_0x0061:
        r4 = r3.identityScope;	 Catch:{ all -> 0x0081 }
        if (r4 == 0) goto L_0x006a;
    L_0x0065:
        r4 = r3.identityScope;	 Catch:{ all -> 0x0081 }
        r4.unlock();	 Catch:{ all -> 0x0081 }
    L_0x006a:
        monitor-exit(r0);	 Catch:{ all -> 0x0081 }
        r4 = r3.f69db;	 Catch:{ all -> 0x0084 }
        r4.setTransactionSuccessful();	 Catch:{ all -> 0x0084 }
        if (r1 == 0) goto L_0x007b;
    L_0x0072:
        r4 = r3.identityScope;	 Catch:{ all -> 0x0084 }
        if (r4 == 0) goto L_0x007b;
    L_0x0076:
        r4 = r3.identityScope;	 Catch:{ all -> 0x0084 }
        r4.remove(r1);	 Catch:{ all -> 0x0084 }
    L_0x007b:
        r4 = r3.f69db;
        r4.endTransaction();
        return;
    L_0x0081:
        r4 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0081 }
        throw r4;	 Catch:{ all -> 0x0084 }
    L_0x0084:
        r4 = move-exception;
        r5 = r3.f69db;
        r5.endTransaction();
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.greenrobot.greendao.AbstractDao.deleteInTxInternal(java.lang.Iterable, java.lang.Iterable):void");
    }

    public void deleteInTx(Iterable<T> iterable) {
        deleteInTxInternal(iterable, null);
    }

    public void deleteInTx(T... tArr) {
        deleteInTxInternal(Arrays.asList(tArr), null);
    }

    public void deleteByKeyInTx(Iterable<K> iterable) {
        deleteInTxInternal(null, iterable);
    }

    public void deleteByKeyInTx(K... kArr) {
        deleteInTxInternal(null, Arrays.asList(kArr));
    }

    public void refresh(T t) {
        assertSinglePk();
        Object keyVerified = getKeyVerified(t);
        Cursor rawQuery = this.f69db.rawQuery(this.statements.getSelectByKey(), new String[]{keyVerified.toString()});
        try {
            if (!rawQuery.moveToFirst()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Entity does not exist in the database anymore: ");
                stringBuilder.append(t.getClass());
                stringBuilder.append(" with key ");
                stringBuilder.append(keyVerified);
                throw new DaoException(stringBuilder.toString());
            } else if (rawQuery.isLast()) {
                readEntity(rawQuery, t, 0);
                attachEntity(keyVerified, t, true);
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Expected unique result, but count was ");
                stringBuilder2.append(rawQuery.getCount());
                throw new DaoException(stringBuilder2.toString());
            }
        } finally {
            rawQuery.close();
        }
    }

    public void update(T t) {
        assertSinglePk();
        DatabaseStatement updateStatement = this.statements.getUpdateStatement();
        if (this.f69db.isDbLockedByCurrentThread()) {
            synchronized (updateStatement) {
                if (this.isStandardSQLite) {
                    updateInsideSynchronized((Object) t, (SQLiteStatement) updateStatement.getRawStatement(), true);
                } else {
                    updateInsideSynchronized((Object) t, updateStatement, true);
                }
            }
            return;
        }
        this.f69db.beginTransaction();
        try {
            synchronized (updateStatement) {
                updateInsideSynchronized((Object) t, updateStatement, true);
            }
            this.f69db.setTransactionSuccessful();
            this.f69db.endTransaction();
        } catch (Throwable th) {
            this.f69db.endTransaction();
        }
    }

    public QueryBuilder<T> queryBuilder() {
        return QueryBuilder.internalCreate(this);
    }

    /* Access modifiers changed, original: protected */
    public void updateInsideSynchronized(T t, DatabaseStatement databaseStatement, boolean z) {
        bindValues(databaseStatement, (Object) t);
        int length = this.config.allColumns.length + 1;
        Object key = getKey(t);
        if (key instanceof Long) {
            databaseStatement.bindLong(length, ((Long) key).longValue());
        } else if (key == null) {
            throw new DaoException("Cannot update entity without key - was it inserted before?");
        } else {
            databaseStatement.bindString(length, key.toString());
        }
        databaseStatement.execute();
        attachEntity(key, t, z);
    }

    /* Access modifiers changed, original: protected */
    public void updateInsideSynchronized(T t, SQLiteStatement sQLiteStatement, boolean z) {
        bindValues(sQLiteStatement, (Object) t);
        int length = this.config.allColumns.length + 1;
        Object key = getKey(t);
        if (key instanceof Long) {
            sQLiteStatement.bindLong(length, ((Long) key).longValue());
        } else if (key == null) {
            throw new DaoException("Cannot update entity without key - was it inserted before?");
        } else {
            sQLiteStatement.bindString(length, key.toString());
        }
        sQLiteStatement.execute();
        attachEntity(key, t, z);
    }

    /* Access modifiers changed, original: protected|final */
    public final void attachEntity(K k, T t, boolean z) {
        attachEntity(t);
        if (this.identityScope != null && k != null) {
            if (z) {
                this.identityScope.put(k, t);
            } else {
                this.identityScope.putNoLock(k, t);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x007c A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x007b  */
    public void updateInTx(java.lang.Iterable<T> r5) {
        /*
        r4 = this;
        r0 = r4.statements;
        r0 = r0.getUpdateStatement();
        r1 = r4.f69db;
        r1.beginTransaction();
        monitor-enter(r0);	 Catch:{ RuntimeException -> 0x0073, all -> 0x006a }
        r1 = r4.identityScope;	 Catch:{ all -> 0x0067 }
        if (r1 == 0) goto L_0x0015;
    L_0x0010:
        r1 = r4.identityScope;	 Catch:{ all -> 0x0067 }
        r1.lock();	 Catch:{ all -> 0x0067 }
    L_0x0015:
        r1 = r4.isStandardSQLite;	 Catch:{ all -> 0x005c }
        r2 = 0;
        if (r1 == 0) goto L_0x0032;
    L_0x001a:
        r1 = r0.getRawStatement();	 Catch:{ all -> 0x005c }
        r1 = (android.database.sqlite.SQLiteStatement) r1;	 Catch:{ all -> 0x005c }
        r5 = r5.iterator();	 Catch:{ all -> 0x005c }
    L_0x0024:
        r3 = r5.hasNext();	 Catch:{ all -> 0x005c }
        if (r3 == 0) goto L_0x0044;
    L_0x002a:
        r3 = r5.next();	 Catch:{ all -> 0x005c }
        r4.updateInsideSynchronized(r3, r1, r2);	 Catch:{ all -> 0x005c }
        goto L_0x0024;
    L_0x0032:
        r5 = r5.iterator();	 Catch:{ all -> 0x005c }
    L_0x0036:
        r1 = r5.hasNext();	 Catch:{ all -> 0x005c }
        if (r1 == 0) goto L_0x0044;
    L_0x003c:
        r1 = r5.next();	 Catch:{ all -> 0x005c }
        r4.updateInsideSynchronized(r1, r0, r2);	 Catch:{ all -> 0x005c }
        goto L_0x0036;
    L_0x0044:
        r5 = r4.identityScope;	 Catch:{ all -> 0x0067 }
        if (r5 == 0) goto L_0x004d;
    L_0x0048:
        r5 = r4.identityScope;	 Catch:{ all -> 0x0067 }
        r5.unlock();	 Catch:{ all -> 0x0067 }
    L_0x004d:
        monitor-exit(r0);	 Catch:{ all -> 0x0067 }
        r5 = r4.f69db;	 Catch:{ RuntimeException -> 0x0073, all -> 0x006a }
        r5.setTransactionSuccessful();	 Catch:{ RuntimeException -> 0x0073, all -> 0x006a }
        r5 = r4.f69db;	 Catch:{ RuntimeException -> 0x005a }
        r5.endTransaction();	 Catch:{ RuntimeException -> 0x005a }
        r5 = 0;
        goto L_0x0079;
    L_0x005a:
        r5 = move-exception;
        throw r5;
    L_0x005c:
        r5 = move-exception;
        r1 = r4.identityScope;	 Catch:{ all -> 0x0067 }
        if (r1 == 0) goto L_0x0066;
    L_0x0061:
        r1 = r4.identityScope;	 Catch:{ all -> 0x0067 }
        r1.unlock();	 Catch:{ all -> 0x0067 }
    L_0x0066:
        throw r5;	 Catch:{ all -> 0x0067 }
    L_0x0067:
        r5 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0067 }
        throw r5;	 Catch:{ RuntimeException -> 0x0073, all -> 0x006a }
    L_0x006a:
        r5 = move-exception;
        r0 = r4.f69db;	 Catch:{ RuntimeException -> 0x0071 }
        r0.endTransaction();	 Catch:{ RuntimeException -> 0x0071 }
        throw r5;
    L_0x0071:
        r5 = move-exception;
        throw r5;
    L_0x0073:
        r5 = move-exception;
        r0 = r4.f69db;	 Catch:{ RuntimeException -> 0x007d }
        r0.endTransaction();	 Catch:{ RuntimeException -> 0x007d }
    L_0x0079:
        if (r5 == 0) goto L_0x007c;
    L_0x007b:
        throw r5;
    L_0x007c:
        return;
    L_0x007d:
        r0 = move-exception;
        if (r5 == 0) goto L_0x0086;
    L_0x0080:
        r1 = "Could not end transaction (rethrowing initial exception)";
        org.greenrobot.greendao.DaoLog.m20w(r1, r0);
        throw r5;
    L_0x0086:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.greenrobot.greendao.AbstractDao.updateInTx(java.lang.Iterable):void");
    }

    public void updateInTx(T... tArr) {
        updateInTx(Arrays.asList(tArr));
    }

    /* Access modifiers changed, original: protected */
    public void assertSinglePk() {
        if (this.config.pkColumns.length != 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this);
            stringBuilder.append(" (");
            stringBuilder.append(this.config.tablename);
            stringBuilder.append(") does not have a single-column primary key");
            throw new DaoException(stringBuilder.toString());
        }
    }

    public long count() {
        return this.statements.getCountStatement().simpleQueryForLong();
    }

    /* Access modifiers changed, original: protected */
    public K getKeyVerified(T t) {
        Object key = getKey(t);
        if (key != null) {
            return key;
        }
        if (t == null) {
            throw new NullPointerException("Entity may not be null");
        }
        throw new DaoException("Entity has no key");
    }

    @Experimental
    public RxDao<T, K> rxPlain() {
        if (this.rxDaoPlain == null) {
            this.rxDaoPlain = new RxDao(this);
        }
        return this.rxDaoPlain;
    }

    @Experimental
    /* renamed from: rx */
    public RxDao<T, K> mo6922rx() {
        if (this.rxDao == null) {
            this.rxDao = new RxDao(this, Schedulers.io());
        }
        return this.rxDao;
    }

    public Database getDatabase() {
        return this.f69db;
    }
}
