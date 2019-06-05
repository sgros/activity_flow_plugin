package org.greenrobot.greendao.identityscope;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

public class IdentityScopeObject implements IdentityScope {
   private final ReentrantLock lock = new ReentrantLock();
   private final HashMap map = new HashMap();

   public void clear() {
      this.lock.lock();

      try {
         this.map.clear();
      } finally {
         this.lock.unlock();
      }

   }

   public boolean detach(Object var1, Object var2) {
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

   public Object get(Object var1) {
      this.lock.lock();

      Reference var4;
      try {
         var4 = (Reference)this.map.get(var1);
      } finally {
         this.lock.unlock();
      }

      return var4 != null ? var4.get() : null;
   }

   public Object getNoLock(Object var1) {
      Reference var2 = (Reference)this.map.get(var1);
      return var2 != null ? var2.get() : null;
   }

   public void lock() {
      this.lock.lock();
   }

   public void put(Object var1, Object var2) {
      this.lock.lock();

      try {
         HashMap var3 = this.map;
         WeakReference var4 = new WeakReference(var2);
         var3.put(var1, var4);
      } finally {
         this.lock.unlock();
      }

   }

   public void putNoLock(Object var1, Object var2) {
      this.map.put(var1, new WeakReference(var2));
   }

   public void remove(Iterable var1) {
      this.lock.lock();

      label78: {
         Throwable var10000;
         label77: {
            boolean var10001;
            Iterator var2;
            try {
               var2 = var1.iterator();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label77;
            }

            while(true) {
               try {
                  if (!var2.hasNext()) {
                     break label78;
                  }

                  Object var10 = var2.next();
                  this.map.remove(var10);
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var9 = var10000;
         this.lock.unlock();
         throw var9;
      }

      this.lock.unlock();
   }

   public void remove(Object var1) {
      this.lock.lock();

      try {
         this.map.remove(var1);
      } finally {
         this.lock.unlock();
      }

   }

   public void reserveRoom(int var1) {
   }

   public void unlock() {
      this.lock.unlock();
   }
}
