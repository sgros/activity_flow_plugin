package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.io.File;

public interface DiskCache {
   File get(Key var1);

   void put(Key var1, DiskCache.Writer var2);

   public interface Factory {
      DiskCache build();
   }

   public interface Writer {
      boolean write(File var1);
   }
}
