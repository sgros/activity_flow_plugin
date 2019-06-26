// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao;

import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import rx.schedulers.Schedulers;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.HashMap;
import org.greenrobot.greendao.rx.RxTransaction;
import java.util.Map;
import org.greenrobot.greendao.database.Database;

public class AbstractDaoSession
{
    private final Database db;
    private final Map<Class<?>, AbstractDao<?, ?>> entityToDao;
    private volatile RxTransaction rxTxIo;
    private volatile RxTransaction rxTxPlain;
    
    public AbstractDaoSession(final Database db) {
        this.db = db;
        this.entityToDao = new HashMap<Class<?>, AbstractDao<?, ?>>();
    }
    
    public <V> V callInTx(final Callable<V> callable) throws Exception {
        this.db.beginTransaction();
        try {
            final V call = callable.call();
            this.db.setTransactionSuccessful();
            return call;
        }
        finally {
            this.db.endTransaction();
        }
    }
    
    public <V> V callInTxNoException(final Callable<V> callable) {
        this.db.beginTransaction();
        try {
            try {
                final V call = callable.call();
                this.db.setTransactionSuccessful();
                this.db.endTransaction();
                return call;
            }
            finally {}
        }
        catch (Exception ex) {
            throw new DaoException("Callable failed", ex);
        }
        this.db.endTransaction();
    }
    
    public <T> void delete(final T t) {
        this.getDao(t.getClass()).delete(t);
    }
    
    public <T> void deleteAll(final Class<T> clazz) {
        this.getDao(clazz).deleteAll();
    }
    
    public Collection<AbstractDao<?, ?>> getAllDaos() {
        return Collections.unmodifiableCollection((Collection<? extends AbstractDao<?, ?>>)this.entityToDao.values());
    }
    
    public AbstractDao<?, ?> getDao(final Class<?> obj) {
        final AbstractDao<?, ?> abstractDao = this.entityToDao.get(obj);
        if (abstractDao == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("No DAO registered for ");
            sb.append(obj);
            throw new DaoException(sb.toString());
        }
        return abstractDao;
    }
    
    public Database getDatabase() {
        return this.db;
    }
    
    public <T> long insert(final T t) {
        return this.getDao(t.getClass()).insert(t);
    }
    
    public <T> long insertOrReplace(final T t) {
        return this.getDao(t.getClass()).insertOrReplace(t);
    }
    
    public <T, K> T load(final Class<T> clazz, final K k) {
        return (T)this.getDao(clazz).load(k);
    }
    
    public <T, K> List<T> loadAll(final Class<T> clazz) {
        return (List<T>)this.getDao(clazz).loadAll();
    }
    
    public <T> QueryBuilder<T> queryBuilder(final Class<T> clazz) {
        return (QueryBuilder<T>)this.getDao(clazz).queryBuilder();
    }
    
    public <T, K> List<T> queryRaw(final Class<T> clazz, final String s, final String... array) {
        return (List<T>)this.getDao(clazz).queryRaw(s, array);
    }
    
    public <T> void refresh(final T t) {
        this.getDao(t.getClass()).refresh(t);
    }
    
    protected <T> void registerDao(final Class<T> clazz, final AbstractDao<T, ?> abstractDao) {
        this.entityToDao.put(clazz, abstractDao);
    }
    
    public void runInTx(final Runnable runnable) {
        this.db.beginTransaction();
        try {
            runnable.run();
            this.db.setTransactionSuccessful();
        }
        finally {
            this.db.endTransaction();
        }
    }
    
    @Experimental
    public RxTransaction rxTx() {
        if (this.rxTxIo == null) {
            this.rxTxIo = new RxTransaction(this, Schedulers.io());
        }
        return this.rxTxIo;
    }
    
    @Experimental
    public RxTransaction rxTxPlain() {
        if (this.rxTxPlain == null) {
            this.rxTxPlain = new RxTransaction(this);
        }
        return this.rxTxPlain;
    }
    
    public AsyncSession startAsyncSession() {
        return new AsyncSession(this);
    }
    
    public <T> void update(final T t) {
        this.getDao(t.getClass()).update(t);
    }
}
