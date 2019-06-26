// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.util;

import java.util.NoSuchElementException;
import java.util.List;
import java.util.Iterator;
import java.util.AbstractList;

public class LazyList<E> extends AbstractList<E>
{
    private static final Logger LOG;
    Iterator<E> elementSource;
    List<E> underlying;
    
    static {
        LOG = Logger.getLogger(LazyList.class);
    }
    
    public LazyList(final List<E> underlying, final Iterator<E> elementSource) {
        this.underlying = underlying;
        this.elementSource = elementSource;
    }
    
    private void blowup() {
        LazyList.LOG.logDebug("blowup running");
        while (this.elementSource.hasNext()) {
            this.underlying.add(this.elementSource.next());
        }
    }
    
    @Override
    public E get(final int n) {
        if (this.underlying.size() > n) {
            return this.underlying.get(n);
        }
        if (this.elementSource.hasNext()) {
            this.underlying.add(this.elementSource.next());
            return this.get(n);
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int pos = 0;
            
            @Override
            public boolean hasNext() {
                return this.pos < LazyList.this.underlying.size() || LazyList.this.elementSource.hasNext();
            }
            
            @Override
            public E next() {
                if (this.pos < LazyList.this.underlying.size()) {
                    return LazyList.this.underlying.get(this.pos++);
                }
                final LazyList this$0 = LazyList.this;
                this$0.underlying.add(this$0.elementSource.next());
                return this.next();
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    @Override
    public int size() {
        LazyList.LOG.logDebug("potentially expensive size() call");
        this.blowup();
        return this.underlying.size();
    }
}
