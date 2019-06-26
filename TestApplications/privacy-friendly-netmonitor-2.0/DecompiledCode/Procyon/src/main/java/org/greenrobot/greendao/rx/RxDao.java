// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.rx;

import java.util.List;
import java.util.concurrent.Callable;
import rx.Observable;
import rx.Scheduler;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.annotation.apihint.Experimental;

@Experimental
public class RxDao<T, K> extends RxBase
{
    private final AbstractDao<T, K> dao;
    
    @Experimental
    public RxDao(final AbstractDao<T, K> abstractDao) {
        this(abstractDao, null);
    }
    
    @Experimental
    public RxDao(final AbstractDao<T, K> dao, final Scheduler scheduler) {
        super(scheduler);
        this.dao = dao;
    }
    
    @Experimental
    public Observable<Long> count() {
        return this.wrap((Callable<Long>)new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return RxDao.this.dao.count();
            }
        });
    }
    
    @Experimental
    public Observable<Void> delete(final T t) {
        return this.wrap((Callable<Void>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                RxDao.this.dao.delete(t);
                return null;
            }
        });
    }
    
    @Experimental
    public Observable<Void> deleteAll() {
        return this.wrap((Callable<Void>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                RxDao.this.dao.deleteAll();
                return null;
            }
        });
    }
    
    @Experimental
    public Observable<Void> deleteByKey(final K k) {
        return this.wrap((Callable<Void>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                RxDao.this.dao.deleteByKey(k);
                return null;
            }
        });
    }
    
    @Experimental
    public Observable<Void> deleteByKeyInTx(final Iterable<K> iterable) {
        return this.wrap((Callable<Void>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                RxDao.this.dao.deleteByKeyInTx(iterable);
                return null;
            }
        });
    }
    
    @Experimental
    public Observable<Void> deleteByKeyInTx(final K... array) {
        return this.wrap((Callable<Void>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                RxDao.this.dao.deleteByKeyInTx(array);
                return null;
            }
        });
    }
    
    @Experimental
    public Observable<Void> deleteInTx(final Iterable<T> iterable) {
        return this.wrap((Callable<Void>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                RxDao.this.dao.deleteInTx(iterable);
                return null;
            }
        });
    }
    
    @Experimental
    public Observable<Void> deleteInTx(final T... array) {
        return this.wrap((Callable<Void>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                RxDao.this.dao.deleteInTx(array);
                return null;
            }
        });
    }
    
    @Experimental
    public AbstractDao<T, K> getDao() {
        return this.dao;
    }
    
    @Experimental
    public Observable<T> insert(final T t) {
        return this.wrap((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                RxDao.this.dao.insert(t);
                return t;
            }
        });
    }
    
    @Experimental
    public Observable<Iterable<T>> insertInTx(final Iterable<T> iterable) {
        return this.wrap((Callable<Iterable<T>>)new Callable<Iterable<T>>() {
            @Override
            public Iterable<T> call() throws Exception {
                RxDao.this.dao.insertInTx(iterable);
                return iterable;
            }
        });
    }
    
    @Experimental
    public Observable<Object[]> insertInTx(final T... array) {
        return this.wrap((Callable<Object[]>)new Callable<Object[]>() {
            @Override
            public Object[] call() throws Exception {
                RxDao.this.dao.insertInTx(array);
                return array;
            }
        });
    }
    
    @Experimental
    public Observable<T> insertOrReplace(final T t) {
        return this.wrap((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                RxDao.this.dao.insertOrReplace(t);
                return t;
            }
        });
    }
    
    @Experimental
    public Observable<Iterable<T>> insertOrReplaceInTx(final Iterable<T> iterable) {
        return this.wrap((Callable<Iterable<T>>)new Callable<Iterable<T>>() {
            @Override
            public Iterable<T> call() throws Exception {
                RxDao.this.dao.insertOrReplaceInTx(iterable);
                return iterable;
            }
        });
    }
    
    @Experimental
    public Observable<Object[]> insertOrReplaceInTx(final T... array) {
        return this.wrap((Callable<Object[]>)new Callable<Object[]>() {
            @Override
            public Object[] call() throws Exception {
                RxDao.this.dao.insertOrReplaceInTx(array);
                return array;
            }
        });
    }
    
    @Experimental
    public Observable<T> load(final K k) {
        return this.wrap((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                return RxDao.this.dao.load(k);
            }
        });
    }
    
    @Experimental
    public Observable<List<T>> loadAll() {
        return this.wrap((Callable<List<T>>)new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                return RxDao.this.dao.loadAll();
            }
        });
    }
    
    @Experimental
    public Observable<T> refresh(final T t) {
        return this.wrap((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                RxDao.this.dao.refresh(t);
                return t;
            }
        });
    }
    
    @Experimental
    public Observable<T> save(final T t) {
        return this.wrap((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                RxDao.this.dao.save(t);
                return t;
            }
        });
    }
    
    @Experimental
    public Observable<Iterable<T>> saveInTx(final Iterable<T> iterable) {
        return this.wrap((Callable<Iterable<T>>)new Callable<Iterable<T>>() {
            @Override
            public Iterable<T> call() throws Exception {
                RxDao.this.dao.saveInTx(iterable);
                return iterable;
            }
        });
    }
    
    @Experimental
    public Observable<Object[]> saveInTx(final T... array) {
        return this.wrap((Callable<Object[]>)new Callable<Object[]>() {
            @Override
            public Object[] call() throws Exception {
                RxDao.this.dao.saveInTx(array);
                return array;
            }
        });
    }
    
    @Experimental
    public Observable<T> update(final T t) {
        return this.wrap((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                RxDao.this.dao.update(t);
                return t;
            }
        });
    }
    
    @Experimental
    public Observable<Iterable<T>> updateInTx(final Iterable<T> iterable) {
        return this.wrap((Callable<Iterable<T>>)new Callable<Iterable<T>>() {
            @Override
            public Iterable<T> call() throws Exception {
                RxDao.this.dao.updateInTx(iterable);
                return iterable;
            }
        });
    }
    
    @Experimental
    public Observable<Object[]> updateInTx(final T... array) {
        return this.wrap((Callable<Object[]>)new Callable<Object[]>() {
            @Override
            public Object[] call() throws Exception {
                RxDao.this.dao.updateInTx(array);
                return array;
            }
        });
    }
}
