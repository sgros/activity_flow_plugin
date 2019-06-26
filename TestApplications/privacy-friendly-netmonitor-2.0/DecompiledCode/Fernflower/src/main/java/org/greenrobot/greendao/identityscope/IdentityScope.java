package org.greenrobot.greendao.identityscope;

public interface IdentityScope {
   void clear();

   boolean detach(Object var1, Object var2);

   Object get(Object var1);

   Object getNoLock(Object var1);

   void lock();

   void put(Object var1, Object var2);

   void putNoLock(Object var1, Object var2);

   void remove(Iterable var1);

   void remove(Object var1);

   void reserveRoom(int var1);

   void unlock();
}
