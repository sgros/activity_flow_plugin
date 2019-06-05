// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.query;

import java.util.NoSuchElementException;
import java.util.ListIterator;
import java.util.Iterator;
import org.greenrobot.greendao.DaoException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.greendao.InternalQueryDaoAccess;
import android.database.Cursor;
import java.io.Closeable;
import java.util.List;

public class LazyList<E> implements List<E>, Closeable
{
    private final Cursor cursor;
    private final InternalQueryDaoAccess<E> daoAccess;
    private final List<E> entities;
    private volatile int loadedCount;
    private final ReentrantLock lock;
    private final int size;
    
    LazyList(final InternalQueryDaoAccess<E> daoAccess, final Cursor cursor, final boolean b) {
        this.cursor = cursor;
        this.daoAccess = daoAccess;
        this.size = cursor.getCount();
        if (b) {
            this.entities = new ArrayList<E>(this.size);
            for (int i = 0; i < this.size; ++i) {
                this.entities.add(null);
            }
        }
        else {
            this.entities = null;
        }
        if (this.size == 0) {
            cursor.close();
        }
        this.lock = new ReentrantLock();
    }
    
    @Override
    public void add(final int n, final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final int n, final Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }
    
    protected void checkCached() {
        if (this.entities == null) {
            throw new DaoException("This operation only works with cached lazy lists");
        }
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void close() {
        this.cursor.close();
    }
    
    @Override
    public boolean contains(final Object o) {
        this.loadRemaining();
        return this.entities.contains(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        this.loadRemaining();
        return this.entities.containsAll(collection);
    }
    
    @Override
    public E get(final int n) {
        if (this.entities != null) {
            final E value;
            if ((value = this.entities.get(n)) == null) {
                this.lock.lock();
                try {
                    if (this.entities.get(n) == null) {
                        this.entities.set(n, this.loadEntity(n));
                        ++this.loadedCount;
                        if (this.loadedCount == this.size) {
                            this.cursor.close();
                        }
                    }
                }
                finally {
                    this.lock.unlock();
                }
            }
            return value;
        }
        this.lock.lock();
        try {
            return this.loadEntity(n);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public int getLoadedCount() {
        return this.loadedCount;
    }
    
    @Override
    public int indexOf(final Object o) {
        this.loadRemaining();
        return this.entities.indexOf(o);
    }
    
    public boolean isClosed() {
        return this.cursor.isClosed();
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public boolean isLoadedCompletely() {
        return this.loadedCount == this.size;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new LazyIterator(0, false);
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        this.loadRemaining();
        return this.entities.lastIndexOf(o);
    }
    
    @Override
    public ListIterator<E> listIterator(final int n) {
        return new LazyIterator(n, false);
    }
    
    @Override
    public CloseableListIterator<E> listIterator() {
        return new LazyIterator(0, false);
    }
    
    public CloseableListIterator<E> listIteratorAutoClose() {
        return new LazyIterator(0, true);
    }
    
    protected E loadEntity(final int n) {
        if (!this.cursor.moveToPosition(n)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not move to cursor location ");
            sb.append(n);
            throw new DaoException(sb.toString());
        }
        final E loadCurrent = this.daoAccess.loadCurrent(this.cursor, 0, true);
        if (loadCurrent == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Loading of entity failed (null) at position ");
            sb2.append(n);
            throw new DaoException(sb2.toString());
        }
        return loadCurrent;
    }
    
    public void loadRemaining() {
        this.checkCached();
        for (int size = this.entities.size(), i = 0; i < size; ++i) {
            this.get(i);
        }
    }
    
    public E peek(final int n) {
        if (this.entities != null) {
            return this.entities.get(n);
        }
        return null;
    }
    
    @Override
    public E remove(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public E set(final int n, final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public List<E> subList(final int n, final int n2) {
        this.checkCached();
        for (int i = n; i < n2; ++i) {
            this.get(i);
        }
        return this.entities.subList(n, n2);
    }
    
    @Override
    public Object[] toArray() {
        this.loadRemaining();
        return this.entities.toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        this.loadRemaining();
        return this.entities.toArray(array);
    }
    
    protected class LazyIterator implements CloseableListIterator<E>
    {
        private final boolean closeWhenDone;
        private int index;
        
        public LazyIterator(final int index, final boolean closeWhenDone) {
            this.index = index;
            this.closeWhenDone = closeWhenDone;
        }
        
        @Override
        public void add(final E e) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void close() {
            LazyList.this.close();
        }
        
        @Override
        public boolean hasNext() {
            return this.index < LazyList.this.size;
        }
        
        @Override
        public boolean hasPrevious() {
            return this.index > 0;
        }
        
        @Override
        public E next() {
            if (this.index >= LazyList.this.size) {
                throw new NoSuchElementException();
            }
            final E value = LazyList.this.get(this.index);
            ++this.index;
            if (this.index == LazyList.this.size && this.closeWhenDone) {
                this.close();
            }
            return value;
        }
        
        @Override
        public int nextIndex() {
            return this.index;
        }
        
        @Override
        public E previous() {
            if (this.index <= 0) {
                throw new NoSuchElementException();
            }
            --this.index;
            return LazyList.this.get(this.index);
        }
        
        @Override
        public int previousIndex() {
            return this.index - 1;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void set(final E e) {
            throw new UnsupportedOperationException();
        }
    }
}
