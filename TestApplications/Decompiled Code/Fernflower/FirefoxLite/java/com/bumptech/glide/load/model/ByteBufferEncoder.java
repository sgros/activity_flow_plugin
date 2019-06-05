package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferEncoder implements Encoder {
   public boolean encode(ByteBuffer var1, File var2, Options var3) {
      boolean var4;
      try {
         ByteBufferUtil.toFile(var1, var2);
      } catch (IOException var5) {
         if (Log.isLoggable("ByteBufferEncoder", 3)) {
            Log.d("ByteBufferEncoder", "Failed to write data", var5);
         }

         var4 = false;
         return var4;
      }

      var4 = true;
      return var4;
   }
}
