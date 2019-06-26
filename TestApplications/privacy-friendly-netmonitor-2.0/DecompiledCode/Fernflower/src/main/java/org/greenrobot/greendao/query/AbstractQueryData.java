package org.greenrobot.greendao.query;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.greenrobot.greendao.AbstractDao;

abstract class AbstractQueryData {
   final AbstractDao dao;
   final String[] initialValues;
   final Map queriesForThreads;
   final String sql;

   AbstractQueryData(AbstractDao var1, String var2, String[] var3) {
      this.dao = var1;
      this.sql = var2;
      this.initialValues = var3;
      this.queriesForThreads = new HashMap();
   }

   protected abstract AbstractQuery createQuery();

   AbstractQuery forCurrentThread() {
      long var1 = Thread.currentThread().getId();
      Map var3 = this.queriesForThreads;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label338: {
         WeakReference var4;
         try {
            var4 = (WeakReference)this.queriesForThreads.get(var1);
         } catch (Throwable var48) {
            var10000 = var48;
            var10001 = false;
            break label338;
         }

         AbstractQuery var49;
         if (var4 != null) {
            try {
               var49 = (AbstractQuery)var4.get();
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label338;
            }
         } else {
            var49 = null;
         }

         if (var49 == null) {
            try {
               this.gc();
               var49 = this.createQuery();
               Map var5 = this.queriesForThreads;
               WeakReference var6 = new WeakReference(var49);
               var5.put(var1, var6);
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label338;
            }
         } else {
            try {
               System.arraycopy(this.initialValues, 0, var49.parameters, 0, this.initialValues.length);
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label338;
            }
         }

         label321:
         try {
            return var49;
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label321;
         }
      }

      while(true) {
         Throwable var50 = var10000;

         try {
            throw var50;
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            continue;
         }
      }
   }

   AbstractQuery forCurrentThread(AbstractQuery var1) {
      if (Thread.currentThread() == var1.ownerThread) {
         System.arraycopy(this.initialValues, 0, var1.parameters, 0, this.initialValues.length);
         return var1;
      } else {
         return this.forCurrentThread();
      }
   }

   void gc() {
      Map var1 = this.queriesForThreads;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label221: {
         Iterator var2;
         try {
            var2 = this.queriesForThreads.entrySet().iterator();
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label221;
         }

         while(true) {
            try {
               while(var2.hasNext()) {
                  if (((WeakReference)((Entry)var2.next()).getValue()).get() == null) {
                     var2.remove();
                  }
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }
}
