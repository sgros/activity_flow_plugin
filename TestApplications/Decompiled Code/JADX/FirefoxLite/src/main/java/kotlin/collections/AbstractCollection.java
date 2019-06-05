package kotlin.collections;

import java.util.Collection;
import kotlin.TypeCastException;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* compiled from: AbstractCollection.kt */
public abstract class AbstractCollection<E> implements Collection<E>, KMappedMarker {
    public boolean add(E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public abstract int getSize();

    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    protected AbstractCollection() {
    }

    public final /* bridge */ int size() {
        return getSize();
    }

    public boolean containsAll(Collection<? extends Object> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        Iterable<Object> iterable = collection;
        if (((Collection) iterable).isEmpty()) {
            return true;
        }
        for (Object contains : iterable) {
            if (!contains(contains)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public String toString() {
        return CollectionsKt___CollectionsKt.joinToString$default(this, ", ", "[", "]", 0, null, new AbstractCollection$toString$1(this), 24, null);
    }

    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    public <T> T[] toArray(T[] tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "array");
        Object[] toArray = CollectionToArray.toArray(this, tArr);
        if (toArray != null) {
            return toArray;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    public boolean contains(Object obj) {
        if ((this instanceof Collection) && isEmpty()) {
            return false;
        }
        for (Object areEqual : this) {
            if (Intrinsics.areEqual(areEqual, obj)) {
                return true;
            }
        }
        return false;
    }
}
