package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

public abstract class AbstractCollection implements Collection, KMappedMarker {
   protected AbstractCollection() {
   }

   public boolean add(Object var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public void clear() {
      throw new UnsupportedOperationException("Operation is not supported for read-only collection");
   }

   public boolean contains(Object var1) {
      boolean var2 = this instanceof Collection;
      boolean var3 = false;
      if (var2 && ((Collection)this).isEmpty()) {
         var2 = var3;
      } else {
         Iterator var4 = this.iterator();

         while(true) {
            var2 = var3;
            if (!var4.hasNext()) {
               break;
            }

            if (Intrinsics.areEqual(var4.next(), var1)) {
               var2 = true;
               break;
            }
         }
      }

      return var2;
   }

   public boolean containsAll(Collection var1) {
      Intrinsics.checkParameterIsNotNull(var1, "elements");
      Iterable var4 = (Iterable)var1;
      boolean var2 = ((Collection)var4).isEmpty();
      boolean var3 = true;
      if (var2) {
         var2 = var3;
      } else {
         Iterator var5 = var4.iterator();

         while(true) {
            var2 = var3;
            if (!var5.hasNext()) {
               break;
            }

            if (!this.contains(var5.next())) {
               var2 = false;
               break;
            }
         }
      }

      return var2;
   }

   public abstract int getSize();

   public boolean isEmpty() {
      boolean var1;
      if (this.size() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
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

   public Object[] toArray() {
      return CollectionToArray.toArray((Collection)this);
   }

   public Object[] toArray(Object[] var1) {
      Intrinsics.checkParameterIsNotNull(var1, "array");
      var1 = CollectionToArray.toArray((Collection)this, var1);
      if (var1 != null) {
         return var1;
      } else {
         throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
      }
   }

   public String toString() {
      return CollectionsKt.joinToString$default(this, (CharSequence)", ", (CharSequence)"[", (CharSequence)"]", 0, (CharSequence)null, (Function1)(new Function1() {
         public final CharSequence invoke(Object var1) {
            String var2;
            if (var1 == AbstractCollection.this) {
               var2 = "(this Collection)";
            } else {
               var2 = String.valueOf(var1);
            }

            CharSequence var3 = (CharSequence)var2;
            return var3;
         }
      }), 24, (Object)null);
   }
}
