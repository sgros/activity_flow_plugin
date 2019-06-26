// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao;

import org.greenrobot.greendao.annotation.apihint.Experimental;
import rx.schedulers.Schedulers;
import java.util.Collection;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.internal.FastCursor;
import java.util.Arrays;
import android.database.CrossProcessCursor;
import android.database.CursorWindow;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.greenrobot.greendao.database.DatabaseStatement;
import android.database.sqlite.SQLiteDatabase;
import org.greenrobot.greendao.internal.TableStatements;
import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

public abstract class AbstractDao<T, K>
{
    protected final DaoConfig config;
    protected final Database db;
    protected final IdentityScope<K, T> identityScope;
    protected final IdentityScopeLong<T> identityScopeLong;
    protected final boolean isStandardSQLite;
    protected final int pkOrdinal;
    private volatile RxDao<T, K> rxDao;
    private volatile RxDao<T, K> rxDaoPlain;
    protected final AbstractDaoSession session;
    protected final TableStatements statements;
    
    public AbstractDao(final DaoConfig daoConfig) {
        this(daoConfig, null);
    }
    
    public AbstractDao(final DaoConfig config, final AbstractDaoSession session) {
        this.config = config;
        this.session = session;
        this.db = config.db;
        this.isStandardSQLite = (this.db.getRawDatabase() instanceof SQLiteDatabase);
        this.identityScope = (IdentityScope<K, T>)config.getIdentityScope();
        if (this.identityScope instanceof IdentityScopeLong) {
            this.identityScopeLong = (IdentityScopeLong<T>)(IdentityScopeLong)this.identityScope;
        }
        else {
            this.identityScopeLong = null;
        }
        this.statements = config.statements;
        int ordinal;
        if (config.pkProperty != null) {
            ordinal = config.pkProperty.ordinal;
        }
        else {
            ordinal = -1;
        }
        this.pkOrdinal = ordinal;
    }
    
    private void deleteByKeyInsideSynchronized(final K k, final DatabaseStatement databaseStatement) {
        if (k instanceof Long) {
            databaseStatement.bindLong(1, (long)k);
        }
        else {
            if (k == null) {
                throw new DaoException("Cannot delete entity, key is null");
            }
            databaseStatement.bindString(1, k.toString());
        }
        databaseStatement.execute();
    }
    
    private void deleteInTxInternal(final Iterable<T> iterable, final Iterable<K> iterable2) {
        this.assertSinglePk();
        final DatabaseStatement deleteStatement = this.statements.getDeleteStatement();
        this.db.beginTransaction();
        try {
            synchronized (deleteStatement) {
                List<K> list;
                if (this.identityScope != null) {
                    this.identityScope.lock();
                    list = new ArrayList<K>();
                }
                else {
                    list = null;
                }
            Block_15_Outer:
                while (true) {
                    if (iterable != null) {
                        try {
                            final Iterator<T> iterator = iterable.iterator();
                            while (iterator.hasNext()) {
                                final K keyVerified = this.getKeyVerified(iterator.next());
                                this.deleteByKeyInsideSynchronized(keyVerified, deleteStatement);
                                if (list != null) {
                                    list.add(keyVerified);
                                }
                            }
                            break Label_0116;
                        }
                        finally {
                            if (this.identityScope != null) {
                                this.identityScope.unlock();
                            }
                            // iftrue(Label_0184:, iterable2 == null)
                            // iftrue(Label_0127:, list == null)
                            while (true) {
                                Label_0127: {
                                    Iterator<K> iterator2;
                                    while (true) {
                                        iterator2 = iterable2.iterator();
                                        break Label_0127;
                                        continue Block_15_Outer;
                                    }
                                    final K next = iterator2.next();
                                    this.deleteByKeyInsideSynchronized(next, deleteStatement);
                                    list.add(next);
                                }
                                continue;
                            }
                        }
                        // iftrue(Label_0184:, !iterator2.hasNext())
                        Label_0184: {
                            if (this.identityScope != null) {
                                this.identityScope.unlock();
                            }
                        }
                        // monitorexit(deleteStatement)
                        this.db.setTransactionSuccessful();
                        if (list != null && this.identityScope != null) {
                            this.identityScope.remove(list);
                        }
                        return;
                    }
                    continue;
                }
            }
        }
        finally {
            this.db.endTransaction();
        }
    }
    
    private long executeInsert(final T t, final DatabaseStatement databaseStatement, final boolean b) {
        Label_0058: {
            if (this.db.isDbLockedByCurrentThread()) {
                final long n = this.insertInsideTx(t, databaseStatement);
                break Label_0058;
            }
            this.db.beginTransaction();
            try {
                final long n = this.insertInsideTx(t, databaseStatement);
                this.db.setTransactionSuccessful();
                this.db.endTransaction();
                if (b) {
                    this.updateKeyAfterInsertAndAttach(t, n, true);
                }
                return n;
            }
            finally {
                this.db.endTransaction();
            }
        }
    }
    
    private void executeInsertInTx(final DatabaseStatement databaseStatement, final Iterable<T> iterable, final boolean b) {
        this.db.beginTransaction();
        try {
            synchronized (databaseStatement) {
                if (this.identityScope != null) {
                    this.identityScope.lock();
                }
                try {
                    if (this.isStandardSQLite) {
                        final SQLiteStatement sqLiteStatement = (SQLiteStatement)databaseStatement.getRawStatement();
                        for (final T next : iterable) {
                            this.bindValues(sqLiteStatement, next);
                            if (b) {
                                this.updateKeyAfterInsertAndAttach(next, sqLiteStatement.executeInsert(), false);
                            }
                            else {
                                sqLiteStatement.execute();
                            }
                        }
                    }
                    else {
                        for (final T next2 : iterable) {
                            this.bindValues(databaseStatement, next2);
                            if (b) {
                                this.updateKeyAfterInsertAndAttach(next2, databaseStatement.executeInsert(), false);
                            }
                            else {
                                databaseStatement.execute();
                            }
                        }
                    }
                    if (this.identityScope != null) {
                        this.identityScope.unlock();
                    }
                    // monitorexit(databaseStatement)
                    this.db.setTransactionSuccessful();
                }
                finally {
                    if (this.identityScope != null) {
                        this.identityScope.unlock();
                    }
                }
            }
        }
        finally {
            this.db.endTransaction();
        }
    }
    
    private long insertInsideTx(final T t, final DatabaseStatement databaseStatement) {
        synchronized (databaseStatement) {
            if (this.isStandardSQLite) {
                final SQLiteStatement sqLiteStatement = (SQLiteStatement)databaseStatement.getRawStatement();
                this.bindValues(sqLiteStatement, t);
                return sqLiteStatement.executeInsert();
            }
            this.bindValues(databaseStatement, t);
            return databaseStatement.executeInsert();
        }
    }
    
    private void loadAllUnlockOnWindowBounds(final Cursor cursor, CursorWindow moveToNextUnlocked, final List<T> list) {
        int n = moveToNextUnlocked.getStartPosition() + moveToNextUnlocked.getNumRows();
        int n2 = 0;
        while (true) {
            list.add(this.loadCurrent(cursor, 0, false));
            if (++n2 >= n) {
                moveToNextUnlocked = this.moveToNextUnlocked(cursor);
                if (moveToNextUnlocked == null) {
                    break;
                }
                n = moveToNextUnlocked.getStartPosition() + moveToNextUnlocked.getNumRows();
            }
            else if (!cursor.moveToNext()) {
                break;
            }
            ++n2;
        }
    }
    
    private CursorWindow moveToNextUnlocked(final Cursor cursor) {
        this.identityScope.unlock();
        try {
            CursorWindow window;
            if (cursor.moveToNext()) {
                window = ((CrossProcessCursor)cursor).getWindow();
            }
            else {
                window = null;
            }
            return window;
        }
        finally {
            this.identityScope.lock();
        }
    }
    
    protected void assertSinglePk() {
        if (this.config.pkColumns.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this);
            sb.append(" (");
            sb.append(this.config.tablename);
            sb.append(") does not have a single-column primary key");
            throw new DaoException(sb.toString());
        }
    }
    
    protected void attachEntity(final T t) {
    }
    
    protected final void attachEntity(final K k, final T t, final boolean b) {
        this.attachEntity(t);
        if (this.identityScope != null && k != null) {
            if (b) {
                this.identityScope.put(k, t);
            }
            else {
                this.identityScope.putNoLock(k, t);
            }
        }
    }
    
    protected abstract void bindValues(final SQLiteStatement p0, final T p1);
    
    protected abstract void bindValues(final DatabaseStatement p0, final T p1);
    
    public long count() {
        return this.statements.getCountStatement().simpleQueryForLong();
    }
    
    public void delete(final T t) {
        this.assertSinglePk();
        this.deleteByKey(this.getKeyVerified(t));
    }
    
    public void deleteAll() {
        final Database db = this.db;
        final StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM '");
        sb.append(this.config.tablename);
        sb.append("'");
        db.execSQL(sb.toString());
        if (this.identityScope != null) {
            this.identityScope.clear();
        }
    }
    
    public void deleteByKey(final K k) {
        this.assertSinglePk();
        final DatabaseStatement deleteStatement = this.statements.getDeleteStatement();
        Label_0079: {
            if (this.db.isDbLockedByCurrentThread()) {
                synchronized (deleteStatement) {
                    this.deleteByKeyInsideSynchronized(k, deleteStatement);
                    break Label_0079;
                }
            }
            this.db.beginTransaction();
            try {
                synchronized (deleteStatement) {
                    this.deleteByKeyInsideSynchronized(k, deleteStatement);
                    // monitorexit(deleteStatement)
                    this.db.setTransactionSuccessful();
                    this.db.endTransaction();
                    if (this.identityScope != null) {
                        this.identityScope.remove(k);
                    }
                }
            }
            finally {
                this.db.endTransaction();
            }
        }
    }
    
    public void deleteByKeyInTx(final Iterable<K> iterable) {
        this.deleteInTxInternal(null, iterable);
    }
    
    public void deleteByKeyInTx(final K... a) {
        this.deleteInTxInternal(null, Arrays.asList(a));
    }
    
    public void deleteInTx(final Iterable<T> iterable) {
        this.deleteInTxInternal(iterable, null);
    }
    
    public void deleteInTx(final T... a) {
        this.deleteInTxInternal(Arrays.asList(a), null);
    }
    
    public boolean detach(final T t) {
        return this.identityScope != null && this.identityScope.detach(this.getKeyVerified(t), t);
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
    
    protected abstract K getKey(final T p0);
    
    protected K getKeyVerified(final T t) {
        final K key = this.getKey(t);
        if (key != null) {
            return key;
        }
        if (t == null) {
            throw new NullPointerException("Entity may not be null");
        }
        throw new DaoException("Entity has no key");
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
    
    protected abstract boolean hasKey(final T p0);
    
    public long insert(final T t) {
        return this.executeInsert(t, this.statements.getInsertStatement(), true);
    }
    
    public void insertInTx(final Iterable<T> iterable) {
        this.insertInTx(iterable, this.isEntityUpdateable());
    }
    
    public void insertInTx(final Iterable<T> iterable, final boolean b) {
        this.executeInsertInTx(this.statements.getInsertStatement(), iterable, b);
    }
    
    public void insertInTx(final T... a) {
        this.insertInTx(Arrays.asList(a), this.isEntityUpdateable());
    }
    
    public long insertOrReplace(final T t) {
        return this.executeInsert(t, this.statements.getInsertOrReplaceStatement(), true);
    }
    
    public void insertOrReplaceInTx(final Iterable<T> iterable) {
        this.insertOrReplaceInTx(iterable, this.isEntityUpdateable());
    }
    
    public void insertOrReplaceInTx(final Iterable<T> iterable, final boolean b) {
        this.executeInsertInTx(this.statements.getInsertOrReplaceStatement(), iterable, b);
    }
    
    public void insertOrReplaceInTx(final T... a) {
        this.insertOrReplaceInTx(Arrays.asList(a), this.isEntityUpdateable());
    }
    
    public long insertWithoutSettingPk(final T t) {
        return this.executeInsert(t, this.statements.getInsertOrReplaceStatement(), false);
    }
    
    protected abstract boolean isEntityUpdateable();
    
    public T load(final K k) {
        this.assertSinglePk();
        if (k == null) {
            return null;
        }
        if (this.identityScope != null) {
            final T value = this.identityScope.get(k);
            if (value != null) {
                return value;
            }
        }
        return this.loadUniqueAndCloseCursor(this.db.rawQuery(this.statements.getSelectByKey(), new String[] { k.toString() }));
    }
    
    public List<T> loadAll() {
        return this.loadAllAndCloseCursor(this.db.rawQuery(this.statements.getSelectAll(), null));
    }
    
    protected List<T> loadAllAndCloseCursor(final Cursor cursor) {
        try {
            return this.loadAllFromCursor(cursor);
        }
        finally {
            cursor.close();
        }
    }
    
    protected List<T> loadAllFromCursor(Cursor cursor) {
        final int count = cursor.getCount();
        if (count == 0) {
            return new ArrayList<T>();
        }
        final ArrayList list = new ArrayList<T>(count);
        CursorWindow cursorWindow = null;
        boolean b = false;
        Label_0148: {
            if (cursor instanceof CrossProcessCursor) {
                final CursorWindow window = ((CrossProcessCursor)cursor).getWindow();
                if ((cursorWindow = window) != null) {
                    if (window.getNumRows() == count) {
                        cursor = (Cursor)new FastCursor(window);
                        b = true;
                        cursorWindow = window;
                        break Label_0148;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Window vs. result size: ");
                    sb.append(window.getNumRows());
                    sb.append("/");
                    sb.append(count);
                    DaoLog.d(sb.toString());
                    cursorWindow = window;
                }
            }
            b = false;
        }
        if (cursor.moveToFirst()) {
            if (this.identityScope != null) {
                this.identityScope.lock();
                this.identityScope.reserveRoom(count);
            }
            Label_0211: {
                if (b || cursorWindow == null) {
                    break Label_0211;
                }
                try {
                    if (this.identityScope != null) {
                        this.loadAllUnlockOnWindowBounds(cursor, cursorWindow, (List<T>)list);
                    }
                    else {
                        do {
                            list.add(this.loadCurrent(cursor, 0, false));
                        } while (cursor.moveToNext());
                    }
                }
                finally {
                    if (this.identityScope != null) {
                        this.identityScope.unlock();
                    }
                }
            }
        }
        return (List<T>)list;
    }
    
    public T loadByRowId(final long i) {
        return this.loadUniqueAndCloseCursor(this.db.rawQuery(this.statements.getSelectByRowId(), new String[] { Long.toString(i) }));
    }
    
    protected final T loadCurrent(final Cursor cursor, final int n, final boolean b) {
        if (this.identityScopeLong != null) {
            if (n != 0 && cursor.isNull(this.pkOrdinal + n)) {
                return null;
            }
            final long long1 = cursor.getLong(this.pkOrdinal + n);
            T t;
            if (b) {
                t = this.identityScopeLong.get2(long1);
            }
            else {
                t = this.identityScopeLong.get2NoLock(long1);
            }
            if (t != null) {
                return t;
            }
            final T entity = this.readEntity(cursor, n);
            this.attachEntity(entity);
            if (b) {
                this.identityScopeLong.put2(long1, entity);
            }
            else {
                this.identityScopeLong.put2NoLock(long1, entity);
            }
            return entity;
        }
        else if (this.identityScope != null) {
            final K key = this.readKey(cursor, n);
            if (n != 0 && key == null) {
                return null;
            }
            T t2;
            if (b) {
                t2 = this.identityScope.get(key);
            }
            else {
                t2 = this.identityScope.getNoLock(key);
            }
            if (t2 != null) {
                return t2;
            }
            final T entity2 = this.readEntity(cursor, n);
            this.attachEntity(key, entity2, b);
            return entity2;
        }
        else {
            if (n != 0 && this.readKey(cursor, n) == null) {
                return null;
            }
            final T entity3 = this.readEntity(cursor, n);
            this.attachEntity(entity3);
            return entity3;
        }
    }
    
    protected final <O> O loadCurrentOther(final AbstractDao<O, ?> abstractDao, final Cursor cursor, final int n) {
        return abstractDao.loadCurrent(cursor, n, true);
    }
    
    protected T loadUnique(final Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }
        if (!cursor.isLast()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected unique result, but count was ");
            sb.append(cursor.getCount());
            throw new DaoException(sb.toString());
        }
        return this.loadCurrent(cursor, 0, true);
    }
    
    protected T loadUniqueAndCloseCursor(final Cursor cursor) {
        try {
            return this.loadUnique(cursor);
        }
        finally {
            cursor.close();
        }
    }
    
    public QueryBuilder<T> queryBuilder() {
        return QueryBuilder.internalCreate((AbstractDao<T, ?>)this);
    }
    
    public List<T> queryRaw(final String str, final String... array) {
        final Database db = this.db;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.statements.getSelectAll());
        sb.append(str);
        return this.loadAllAndCloseCursor(db.rawQuery(sb.toString(), array));
    }
    
    public Query<T> queryRawCreate(final String s, final Object... a) {
        return this.queryRawCreateListArgs(s, Arrays.asList(a));
    }
    
    public Query<T> queryRawCreateListArgs(final String str, final Collection<Object> collection) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.statements.getSelectAll());
        sb.append(str);
        return Query.internalCreate((AbstractDao<T, ?>)this, sb.toString(), collection.toArray());
    }
    
    protected abstract T readEntity(final Cursor p0, final int p1);
    
    protected abstract void readEntity(final Cursor p0, final T p1, final int p2);
    
    protected abstract K readKey(final Cursor p0, final int p1);
    
    public void refresh(final T t) {
        this.assertSinglePk();
        final K keyVerified = this.getKeyVerified(t);
        final Cursor rawQuery = this.db.rawQuery(this.statements.getSelectByKey(), new String[] { keyVerified.toString() });
        try {
            if (!rawQuery.moveToFirst()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Entity does not exist in the database anymore: ");
                sb.append(t.getClass());
                sb.append(" with key ");
                sb.append(keyVerified);
                throw new DaoException(sb.toString());
            }
            if (!rawQuery.isLast()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Expected unique result, but count was ");
                sb2.append(rawQuery.getCount());
                throw new DaoException(sb2.toString());
            }
            this.readEntity(rawQuery, t, 0);
            this.attachEntity(keyVerified, t, true);
        }
        finally {
            rawQuery.close();
        }
    }
    
    @Experimental
    public RxDao<T, K> rx() {
        if (this.rxDao == null) {
            this.rxDao = new RxDao<T, K>(this, Schedulers.io());
        }
        return this.rxDao;
    }
    
    @Experimental
    public RxDao<T, K> rxPlain() {
        if (this.rxDaoPlain == null) {
            this.rxDaoPlain = new RxDao<T, K>(this);
        }
        return this.rxDaoPlain;
    }
    
    public void save(final T t) {
        if (this.hasKey(t)) {
            this.update(t);
        }
        else {
            this.insert(t);
        }
    }
    
    public void saveInTx(final Iterable<T> iterable) {
        final Iterator<T> iterator = iterable.iterator();
        int initialCapacity = 0;
        int initialCapacity2 = 0;
        while (iterator.hasNext()) {
            if (this.hasKey(iterator.next())) {
                ++initialCapacity;
            }
            else {
                ++initialCapacity2;
            }
        }
        if (initialCapacity > 0 && initialCapacity2 > 0) {
            final ArrayList list = new ArrayList<T>(initialCapacity);
            final ArrayList list2 = new ArrayList<T>(initialCapacity2);
            for (final T next : iterable) {
                if (this.hasKey(next)) {
                    list.add(next);
                }
                else {
                    list2.add(next);
                }
            }
            this.db.beginTransaction();
            try {
                this.updateInTx((Iterable<T>)list);
                this.insertInTx((Iterable<T>)list2);
                this.db.setTransactionSuccessful();
                return;
            }
            finally {
                this.db.endTransaction();
            }
        }
        if (initialCapacity2 > 0) {
            this.insertInTx(iterable);
        }
        else if (initialCapacity > 0) {
            this.updateInTx(iterable);
        }
    }
    
    public void saveInTx(final T... a) {
        this.saveInTx(Arrays.asList(a));
    }
    
    public void update(final T t) {
        this.assertSinglePk();
        final DatabaseStatement updateStatement = this.statements.getUpdateStatement();
        if (this.db.isDbLockedByCurrentThread()) {
            synchronized (updateStatement) {
                if (this.isStandardSQLite) {
                    this.updateInsideSynchronized(t, (SQLiteStatement)updateStatement.getRawStatement(), true);
                    return;
                }
                this.updateInsideSynchronized(t, updateStatement, true);
                return;
            }
        }
        this.db.beginTransaction();
        try {
            synchronized (updateStatement) {
                this.updateInsideSynchronized(t, updateStatement, true);
                // monitorexit(updateStatement)
                this.db.setTransactionSuccessful();
            }
        }
        finally {
            this.db.endTransaction();
        }
    }
    
    public void updateInTx(final Iterable<T> o) {
        final DatabaseStatement updateStatement = this.statements.getUpdateStatement();
        this.db.beginTransaction();
        try {
            synchronized (updateStatement) {
                if (this.identityScope != null) {
                    this.identityScope.lock();
                }
                try {
                    if (this.isStandardSQLite) {
                        final SQLiteStatement sqLiteStatement = (SQLiteStatement)updateStatement.getRawStatement();
                        final Iterator<T> iterator = ((Iterable<T>)o).iterator();
                        while (iterator.hasNext()) {
                            this.updateInsideSynchronized(iterator.next(), sqLiteStatement, false);
                        }
                    }
                    else {
                        final Iterator<T> iterator2 = ((Iterable<T>)o).iterator();
                        while (iterator2.hasNext()) {
                            this.updateInsideSynchronized(iterator2.next(), updateStatement, false);
                        }
                    }
                    if (this.identityScope != null) {
                        this.identityScope.unlock();
                    }
                    // monitorexit(updateStatement)
                    this.db.setTransactionSuccessful();
                    try {
                        this.db.endTransaction();
                    }
                    catch (RuntimeException ex) {
                        throw ex;
                    }
                }
                finally {
                    if (this.identityScope != null) {
                        this.identityScope.unlock();
                    }
                }
            }
        }
        catch (RuntimeException o) {
            try {
                this.db.endTransaction();
                if (o != null) {
                    throw o;
                }
            }
            catch (RuntimeException ex2) {
                if (o != null) {
                    DaoLog.w("Could not end transaction (rethrowing initial exception)", ex2);
                    throw o;
                }
                throw ex2;
            }
        }
        finally {
            try {
                this.db.endTransaction();
            }
            catch (RuntimeException o) {}
        }
    }
    
    public void updateInTx(final T... a) {
        this.updateInTx(Arrays.asList(a));
    }
    
    protected void updateInsideSynchronized(final T t, final SQLiteStatement sqLiteStatement, final boolean b) {
        this.bindValues(sqLiteStatement, t);
        final int n = this.config.allColumns.length + 1;
        final K key = this.getKey(t);
        if (key instanceof Long) {
            sqLiteStatement.bindLong(n, (long)key);
        }
        else {
            if (key == null) {
                throw new DaoException("Cannot update entity without key - was it inserted before?");
            }
            sqLiteStatement.bindString(n, key.toString());
        }
        sqLiteStatement.execute();
        this.attachEntity(key, t, b);
    }
    
    protected void updateInsideSynchronized(final T t, final DatabaseStatement databaseStatement, final boolean b) {
        this.bindValues(databaseStatement, t);
        final int n = this.config.allColumns.length + 1;
        final K key = this.getKey(t);
        if (key instanceof Long) {
            databaseStatement.bindLong(n, (long)key);
        }
        else {
            if (key == null) {
                throw new DaoException("Cannot update entity without key - was it inserted before?");
            }
            databaseStatement.bindString(n, key.toString());
        }
        databaseStatement.execute();
        this.attachEntity(key, t, b);
    }
    
    protected abstract K updateKeyAfterInsert(final T p0, final long p1);
    
    protected void updateKeyAfterInsertAndAttach(final T t, final long n, final boolean b) {
        if (n != -1L) {
            this.attachEntity(this.updateKeyAfterInsert(t, n), t, b);
        }
        else {
            DaoLog.w("Could not insert row (executeInsert returned -1)");
        }
    }
}
