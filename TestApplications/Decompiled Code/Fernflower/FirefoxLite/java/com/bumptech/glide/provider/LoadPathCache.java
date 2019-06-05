package com.bumptech.glide.provider;

import android.support.v4.util.ArrayMap;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.util.MultiClassKey;
import java.util.concurrent.atomic.AtomicReference;

public class LoadPathCache {
   private final ArrayMap cache = new ArrayMap();
   private final AtomicReference keyRef = new AtomicReference();

   private MultiClassKey getKey(Class var1, Class var2, Class var3) {
      MultiClassKey var4 = (MultiClassKey)this.keyRef.getAndSet((Object)null);
      MultiClassKey var5 = var4;
      if (var4 == null) {
         var5 = new MultiClassKey();
      }

      var5.set(var1, var2, var3);
      return var5;
   }

   public boolean contains(Class param1, Class param2, Class param3) {
      // $FF: Couldn't be decompiled
   }

   public LoadPath get(Class param1, Class param2, Class param3) {
      // $FF: Couldn't be decompiled
   }

   public void put(Class param1, Class param2, Class param3, LoadPath param4) {
      // $FF: Couldn't be decompiled
   }
}
