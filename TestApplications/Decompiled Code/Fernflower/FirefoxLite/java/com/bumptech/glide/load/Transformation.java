package com.bumptech.glide.load;

import android.content.Context;
import com.bumptech.glide.load.engine.Resource;

public interface Transformation extends Key {
   boolean equals(Object var1);

   int hashCode();

   Resource transform(Context var1, Resource var2, int var3, int var4);
}
