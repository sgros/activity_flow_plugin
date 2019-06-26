package com.googlecode.mp4parser.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class LazyList extends AbstractList {
   private static final Logger LOG = Logger.getLogger(LazyList.class);
   Iterator elementSource;
   List underlying;

   public LazyList(List var1, Iterator var2) {
      this.underlying = var1;
      this.elementSource = var2;
   }

   private void blowup() {
      LOG.logDebug("blowup running");

      while(this.elementSource.hasNext()) {
         this.underlying.add(this.elementSource.next());
      }

   }

   public Object get(int var1) {
      if (this.underlying.size() > var1) {
         return this.underlying.get(var1);
      } else if (this.elementSource.hasNext()) {
         this.underlying.add(this.elementSource.next());
         return this.get(var1);
      } else {
         throw new NoSuchElementException();
      }
   }

   public Iterator iterator() {
      return new Iterator() {
         int pos = 0;

         public boolean hasNext() {
            return this.pos < LazyList.this.underlying.size() || LazyList.this.elementSource.hasNext();
         }

         public Object next() {
            if (this.pos < LazyList.this.underlying.size()) {
               List var3 = LazyList.this.underlying;
               int var2 = this.pos++;
               return var3.get(var2);
            } else {
               LazyList var1 = LazyList.this;
               var1.underlying.add(var1.elementSource.next());
               return this.next();
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int size() {
      LOG.logDebug("potentially expensive size() call");
      this.blowup();
      return this.underlying.size();
   }
}
