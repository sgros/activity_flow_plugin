package com.bumptech.glide.load.engine.bitmap_recycle;

public interface ArrayPool {
   void clearMemory();

   Object get(int var1, Class var2);

   void put(Object var1, Class var2);

   void trimMemory(int var1);
}
