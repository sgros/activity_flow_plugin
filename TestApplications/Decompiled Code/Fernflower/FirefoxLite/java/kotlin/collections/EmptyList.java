package kotlin.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

public final class EmptyList implements Serializable, List, RandomAccess, KMappedMarker {
   public static final EmptyList INSTANCE = new EmptyList();

   private EmptyList() {
   }

   // $FF: synthetic method
   public void add(int var1, Object var2) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   // $FF: synthetic method
   public boolean add(Object var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public boolean addAll(int var1, Collection var2) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public void clear() {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public boolean contains(Void var1) {
      Intrinsics.checkParameterIsNotNull(var1, "element");
      return false;
   }

   public boolean containsAll(Collection var1) {
      Intrinsics.checkParameterIsNotNull(var1, "elements");
      return var1.isEmpty();
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof List && ((List)var1).isEmpty()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Void get(int var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("Empty list doesn't contain element at index ");
      var2.append(var1);
      var2.append('.');
      throw (Throwable)(new IndexOutOfBoundsException(var2.toString()));
   }

   public int getSize() {
      return 0;
   }

   public int hashCode() {
      return 1;
   }

   public int indexOf(Void var1) {
      Intrinsics.checkParameterIsNotNull(var1, "element");
      return -1;
   }

   public boolean isEmpty() {
      return true;
   }

   public Iterator iterator() {
      return (Iterator)EmptyIterator.INSTANCE;
   }

   public int lastIndexOf(Void var1) {
      Intrinsics.checkParameterIsNotNull(var1, "element");
      return -1;
   }

   public ListIterator listIterator() {
      return (ListIterator)EmptyIterator.INSTANCE;
   }

   public ListIterator listIterator(int var1) {
      if (var1 == 0) {
         return (ListIterator)EmptyIterator.INSTANCE;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Index: ");
         var2.append(var1);
         throw (Throwable)(new IndexOutOfBoundsException(var2.toString()));
      }
   }

   // $FF: synthetic method
   public Object remove(int var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public boolean remove(Object var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   // $FF: synthetic method
   public Object set(int var1, Object var2) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public List subList(int var1, int var2) {
      if (var1 == 0 && var2 == 0) {
         return (List)this;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("fromIndex: ");
         var3.append(var1);
         var3.append(", toIndex: ");
         var3.append(var2);
         throw (Throwable)(new IndexOutOfBoundsException(var3.toString()));
      }
   }

   public Object[] toArray() {
      return CollectionToArray.toArray(this);
   }

   public Object[] toArray(Object[] var1) {
      return CollectionToArray.toArray(this, var1);
   }

   public String toString() {
      return "[]";
   }
}
