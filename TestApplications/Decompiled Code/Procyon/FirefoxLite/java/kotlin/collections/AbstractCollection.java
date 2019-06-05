// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.collections;

import kotlin.jvm.functions.Function1;
import kotlin.TypeCastException;
import kotlin.jvm.internal.CollectionToArray;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Collection;

public abstract class AbstractCollection<E> implements Collection<E>, KMappedMarker
{
    protected AbstractCollection() {
    }
    
    @Override
    public boolean add(final E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean contains(final Object o) {
        final boolean b = this instanceof Collection;
        final boolean b2 = false;
        boolean b3;
        if (b && this.isEmpty()) {
            b3 = b2;
        }
        else {
            final Iterator<E> iterator = this.iterator();
            do {
                b3 = b2;
                if (iterator.hasNext()) {
                    continue;
                }
                return b3;
            } while (!Intrinsics.areEqual(iterator.next(), o));
            b3 = true;
        }
        return b3;
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        final Collection<?> collection2 = collection;
        final boolean empty = ((Collection<Object>)collection2).isEmpty();
        final boolean b = true;
        boolean b2;
        if (empty) {
            b2 = b;
        }
        else {
            final Iterator<Object> iterator = collection2.iterator();
            do {
                b2 = b;
                if (iterator.hasNext()) {
                    continue;
                }
                return b2;
            } while (this.contains(iterator.next()));
            b2 = false;
        }
        return b2;
    }
    
    public abstract int getSize();
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final /* bridge */ int size() {
        return this.getSize();
    }
    
    @Override
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        final Object[] array2 = CollectionToArray.toArray(this, array);
        if (array2 != null) {
            return (T[])array2;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
    }
    
    @Override
    public String toString() {
        return CollectionsKt___CollectionsKt.joinToString$default(this, ", ", "[", "]", 0, null, (Function1)new AbstractCollection$toString.AbstractCollection$toString$1(this), 24, null);
    }
}
