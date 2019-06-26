package org.greenrobot.greendao.identityscope;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.greendao.internal.LongHashMap;

public class IdentityScopeLong implements IdentityScope {
   private final ReentrantLock lock = new ReentrantLock();
   private final LongHashMap map = new LongHashMap();

   public void clear() {
      this.lock.lock();

      try {
         this.map.clear();
      } finally {
         this.lock.unlock();
      }

   }

   public boolean detach(Long var1, Object var2) {
      this.lock.lock();

      boolean var3;
      label89: {
         label88: {
            Throwable var10000;
            label93: {
               boolean var10001;
               try {
                  if (this.get(var1) != var2) {
                     break label88;
                  }
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label93;
               }

               if (var2 == null) {
                  break label88;
               }

               try {
                  this.remove(var1);
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label93;
               }

               var3 = true;
               break label89;
            }

            Throwable var10 = var10000;
            this.lock.unlock();
            throw var10;
         }

         var3 = false;
      }

      this.lock.unlock();
      return var3;
   }

   public Object get(Long var1) {
      return this.get2(var1);
   }

   public Object get2(long var1) {
      this.lock.lock();

      Reference var3;
      try {
         var3 = (Reference)this.map.get(var1);
      } finally {
         this.lock.unlock();
      }

      return var3 != null ? var3.get() : null;
   }

   public Object get2NoLock(long var1) {
      Reference var3 = (Reference)this.map.get(var1);
      return var3 != null ? var3.get() : null;
   }

   public Object getNoLock(Long var1) {
      return this.get2NoLock(var1);
   }

   public void lock() {
      this.lock.lock();
   }

   public void put(Long var1, Object var2) {
      this.put2(var1, var2);
   }

   public void put2(long var1, Object var3) {
      this.lock.lock();

      try {
         LongHashMap var4 = this.map;
         WeakReference var5 = new WeakReference(var3);
         var4.put(var1, var5);
      } finally {
         this.lock.unlock();
      }

   }

   public void put2NoLock(long var1, Object var3) {
      this.map.put(var1, new WeakReference(var3));
   }

   public void putNoLock(Long var1, Object var2) {
      this.put2NoLock(var1, var2);
   }

   public void remove(Iterable var1) {
      this.lock.lock();

      label78: {
         Throwable var10000;
         label77: {
            boolean var10001;
            Iterator var9;
            try {
               var9 = var1.iterator();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label77;
            }

            while(true) {
               try {
                  if (!var9.hasNext()) {
                     break label78;
                  }

                  Long var2 = (Long)var9.next();
                  this.map.remove(var2);
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var10 = var10000;
         this.lock.unlock();
         throw var10;
      }

      this.lock.unlock();
   }

   public void remove(Long var1) {
      this.lock.lock();

      try {
         this.map.remove(var1);
      } finally {
         this.lock.unlock();
      }

   }

   public void reserveRoom(int var1) {
      this.map.reserveRoom(var1);
   }

   public void unlock() {
      this.lock.unlock();
   }
}
