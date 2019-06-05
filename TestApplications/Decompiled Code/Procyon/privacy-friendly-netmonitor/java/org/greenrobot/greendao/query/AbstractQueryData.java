// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import java.util.Iterator;
import java.util.HashMap;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.greenrobot.greendao.AbstractDao;

abstract class AbstractQueryData<T, Q extends AbstractQuery<T>>
{
    final AbstractDao<T, ?> dao;
    final String[] initialValues;
    final Map<Long, WeakReference<Q>> queriesForThreads;
    final String sql;
    
    AbstractQueryData(final AbstractDao<T, ?> dao, final String sql, final String[] initialValues) {
        this.dao = dao;
        this.sql = sql;
        this.initialValues = initialValues;
        this.queriesForThreads = new HashMap<Long, WeakReference<Q>>();
    }
    
    protected abstract Q createQuery();
    
    Q forCurrentThread() {
        final long id = Thread.currentThread().getId();
        synchronized (this.queriesForThreads) {
            final WeakReference<Q> weakReference = this.queriesForThreads.get(id);
            AbstractQuery<T> query;
            if (weakReference != null) {
                query = weakReference.get();
            }
            else {
                query = null;
            }
            if (query == null) {
                this.gc();
                query = this.createQuery();
                this.queriesForThreads.put(id, new WeakReference<Q>((Q)query));
            }
            else {
                System.arraycopy(this.initialValues, 0, query.parameters, 0, this.initialValues.length);
            }
            return (Q)query;
        }
    }
    
    Q forCurrentThread(final Q q) {
        if (Thread.currentThread() == q.ownerThread) {
            System.arraycopy(this.initialValues, 0, q.parameters, 0, this.initialValues.length);
            return q;
        }
        return this.forCurrentThread();
    }
    
    void gc() {
        synchronized (this.queriesForThreads) {
            final Iterator<Map.Entry<Long, WeakReference<Q>>> iterator = this.queriesForThreads.entrySet().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getValue().get() == null) {
                    iterator.remove();
                }
            }
        }
    }
}
