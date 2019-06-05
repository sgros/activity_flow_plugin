package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.InternalQueryDaoAccess;

public class LazyList implements List, Closeable {
   private final Cursor cursor;
   private final InternalQueryDaoAccess daoAccess;
   private final List entities;
   private volatile int loadedCount;
   private final ReentrantLock lock;
   private final int size;

   LazyList(InternalQueryDaoAccess var1, Cursor var2, boolean var3) {
      this.cursor = var2;
      this.daoAccess = var1;
      this.size = var2.getCount();
      if (var3) {
         this.entities = new ArrayList(this.size);

         for(int var4 = 0; var4 < this.size; ++var4) {
            this.entities.add((Object)null);
         }
      } else {
         this.entities = null;
      }

      if (this.size == 0) {
         var2.close();
      }

      this.lock = new ReentrantLock();
   }

   public void add(int var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public boolean add(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(int var1, Collection var2) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   protected void checkCached() {
      if (this.entities == null) {
         throw new DaoException("This operation only works with cached lazy lists");
      }
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public void close() {
      this.cursor.close();
   }

   public boolean contains(Object var1) {
      this.loadRemaining();
      return this.entities.contains(var1);
   }

   public boolean containsAll(Collection var1) {
      this.loadRemaining();
      return this.entities.containsAll(var1);
   }

   public Object get(int var1) {
      Object var3;
      if (this.entities == null) {
         this.lock.lock();

         try {
            var3 = this.loadEntity(var1);
         } finally {
            this.lock.unlock();
         }

         return var3;
      } else {
         Object var2 = this.entities.get(var1);
         var3 = var2;
         if (var2 == null) {
            this.lock.lock();

            label272: {
               label271: {
                  Throwable var10000;
                  label280: {
                     boolean var10001;
                     try {
                        var2 = this.entities.get(var1);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label280;
                     }

                     var3 = var2;
                     if (var2 != null) {
                        break label272;
                     }

                     try {
                        var2 = this.loadEntity(var1);
                        this.entities.set(var1, var2);
                        ++this.loadedCount;
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label280;
                     }

                     var3 = var2;

                     label262:
                     try {
                        if (this.loadedCount != this.size) {
                           break label272;
                        }

                        this.cursor.close();
                        break label271;
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label262;
                     }
                  }

                  Throwable var24 = var10000;
                  this.lock.unlock();
                  throw var24;
               }

               var3 = var2;
            }

            this.lock.unlock();
         }

         return var3;
      }
   }

   public int getLoadedCount() {
      return this.loadedCount;
   }

   public int indexOf(Object var1) {
      this.loadRemaining();
      return this.entities.indexOf(var1);
   }

   public boolean isClosed() {
      return this.cursor.isClosed();
   }

   public boolean isEmpty() {
      boolean var1;
      if (this.size == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isLoadedCompletely() {
      boolean var1;
      if (this.loadedCount == this.size) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public Iterator iterator() {
      return new LazyList.LazyIterator(0, false);
   }

   public int lastIndexOf(Object var1) {
      this.loadRemaining();
      return this.entities.lastIndexOf(var1);
   }

   public ListIterator listIterator(int var1) {
      return new LazyList.LazyIterator(var1, false);
   }

   public CloseableListIterator listIterator() {
      return new LazyList.LazyIterator(0, false);
   }

   public CloseableListIterator listIteratorAutoClose() {
      return new LazyList.LazyIterator(0, true);
   }

   protected Object loadEntity(int var1) {
      StringBuilder var3;
      if (!this.cursor.moveToPosition(var1)) {
         var3 = new StringBuilder();
         var3.append("Could not move to cursor location ");
         var3.append(var1);
         throw new DaoException(var3.toString());
      } else {
         Object var2 = this.daoAccess.loadCurrent(this.cursor, 0, true);
         if (var2 == null) {
            var3 = new StringBuilder();
            var3.append("Loading of entity failed (null) at position ");
            var3.append(var1);
            throw new DaoException(var3.toString());
         } else {
            return var2;
         }
      }
   }

   public void loadRemaining() {
      this.checkCached();
      int var1 = this.entities.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         this.get(var2);
      }

   }

   public Object peek(int var1) {
      return this.entities != null ? this.entities.get(var1) : null;
   }

   public Object remove(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public Object set(int var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public int size() {
      return this.size;
   }

   public List subList(int var1, int var2) {
      this.checkCached();

      for(int var3 = var1; var3 < var2; ++var3) {
         this.get(var3);
      }

      return this.entities.subList(var1, var2);
   }

   public Object[] toArray() {
      this.loadRemaining();
      return this.entities.toArray();
   }

   public Object[] toArray(Object[] var1) {
      this.loadRemaining();
      return this.entities.toArray(var1);
   }

   protected class LazyIterator implements CloseableListIterator {
      private final boolean closeWhenDone;
      private int index;

      public LazyIterator(int var2, boolean var3) {
         this.index = var2;
         this.closeWhenDone = var3;
      }

      public void add(Object var1) {
         throw new UnsupportedOperationException();
      }

      public void close() {
         LazyList.this.close();
      }

      public boolean hasNext() {
         boolean var1;
         if (this.index < LazyList.this.size) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean hasPrevious() {
         boolean var1;
         if (this.index > 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public Object next() {
         if (this.index >= LazyList.this.size) {
            throw new NoSuchElementException();
         } else {
            Object var1 = LazyList.this.get(this.index);
            ++this.index;
            if (this.index == LazyList.this.size && this.closeWhenDone) {
               this.close();
            }

            return var1;
         }
      }

      public int nextIndex() {
         return this.index;
      }

      public Object previous() {
         if (this.index <= 0) {
            throw new NoSuchElementException();
         } else {
            --this.index;
            return LazyList.this.get(this.index);
         }
      }

      public int previousIndex() {
         return this.index - 1;
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public void set(Object var1) {
         throw new UnsupportedOperationException();
      }
   }
}
