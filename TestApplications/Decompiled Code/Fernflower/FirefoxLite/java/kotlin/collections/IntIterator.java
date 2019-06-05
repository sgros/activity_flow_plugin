package kotlin.collections;

import java.util.Iterator;
import kotlin.jvm.internal.markers.KMappedMarker;

public abstract class IntIterator implements Iterator, KMappedMarker {
   public final Integer next() {
      return this.nextInt();
   }

   public abstract int nextInt();

   public void remove() {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }
}
