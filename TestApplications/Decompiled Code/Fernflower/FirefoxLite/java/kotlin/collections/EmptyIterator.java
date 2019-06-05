package kotlin.collections;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.markers.KMappedMarker;

public final class EmptyIterator implements ListIterator, KMappedMarker {
   public static final EmptyIterator INSTANCE = new EmptyIterator();

   private EmptyIterator() {
   }

   // $FF: synthetic method
   public void add(Object var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public boolean hasNext() {
      return false;
   }

   public boolean hasPrevious() {
      return false;
   }

   public Void next() {
      throw (Throwable)(new NoSuchElementException());
   }

   public int nextIndex() {
      return 0;
   }

   public Void previous() {
      throw (Throwable)(new NoSuchElementException());
   }

   public int previousIndex() {
      return -1;
   }

   public void remove() {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   // $FF: synthetic method
   public void set(Object var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }
}
