package org.osmdroid.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapTileAreaList implements MapTileContainer, IterableWithSize {
   private final List mList = new ArrayList();

   public boolean contains(long var1) {
      Iterator var3 = this.mList.iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!((MapTileArea)var3.next()).contains(var1));

      return true;
   }

   public List getList() {
      return this.mList;
   }

   public Iterator iterator() {
      return new Iterator() {
         private Iterator mCurrent;
         private int mIndex;

         private Iterator getCurrent() {
            Iterator var1 = this.mCurrent;
            if (var1 != null) {
               return var1;
            } else if (this.mIndex < MapTileAreaList.this.mList.size()) {
               List var3 = MapTileAreaList.this.mList;
               int var2 = this.mIndex++;
               var1 = ((MapTileArea)var3.get(var2)).iterator();
               this.mCurrent = var1;
               return var1;
            } else {
               return null;
            }
         }

         public boolean hasNext() {
            Iterator var1 = this.getCurrent();
            boolean var2;
            if (var1 != null && var1.hasNext()) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }

         public Long next() {
            long var1 = (Long)this.getCurrent().next();
            if (!this.getCurrent().hasNext()) {
               this.mCurrent = null;
            }

            return var1;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int size() {
      Iterator var1 = this.mList.iterator();

      int var2;
      for(var2 = 0; var1.hasNext(); var2 += ((MapTileArea)var1.next()).size()) {
      }

      return var2;
   }
}
