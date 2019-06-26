package androidx.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ArrayMap extends SimpleArrayMap implements Map {
   MapCollections mCollections;

   public ArrayMap() {
   }

   public ArrayMap(int var1) {
      super(var1);
   }

   private MapCollections getCollection() {
      if (this.mCollections == null) {
         this.mCollections = new MapCollections() {
            protected void colClear() {
               ArrayMap.this.clear();
            }

            protected Object colGetEntry(int var1, int var2) {
               return ArrayMap.super.mArray[(var1 << 1) + var2];
            }

            protected Map colGetMap() {
               return ArrayMap.this;
            }

            protected int colGetSize() {
               return ArrayMap.super.mSize;
            }

            protected int colIndexOfKey(Object var1) {
               return ArrayMap.this.indexOfKey(var1);
            }

            protected int colIndexOfValue(Object var1) {
               return ArrayMap.this.indexOfValue(var1);
            }

            protected void colPut(Object var1, Object var2) {
               ArrayMap.this.put(var1, var2);
            }

            protected void colRemoveAt(int var1) {
               ArrayMap.this.removeAt(var1);
            }

            protected Object colSetValue(int var1, Object var2) {
               return ArrayMap.this.setValueAt(var1, var2);
            }
         };
      }

      return this.mCollections;
   }

   public Set entrySet() {
      return this.getCollection().getEntrySet();
   }

   public Set keySet() {
      return this.getCollection().getKeySet();
   }

   public void putAll(Map var1) {
      this.ensureCapacity(super.mSize + var1.size());
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public Collection values() {
      return this.getCollection().getValues();
   }
}
