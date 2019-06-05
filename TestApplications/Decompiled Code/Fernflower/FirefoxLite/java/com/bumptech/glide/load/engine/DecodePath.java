package com.bumptech.glide.load.engine;

import android.support.v4.util.Pools;
import android.util.Log;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DecodePath {
   private final Class dataClass;
   private final List decoders;
   private final String failureMessage;
   private final Pools.Pool listPool;
   private final ResourceTranscoder transcoder;

   public DecodePath(Class var1, Class var2, Class var3, List var4, ResourceTranscoder var5, Pools.Pool var6) {
      this.dataClass = var1;
      this.decoders = var4;
      this.transcoder = var5;
      this.listPool = var6;
      StringBuilder var7 = new StringBuilder();
      var7.append("Failed DecodePath{");
      var7.append(var1.getSimpleName());
      var7.append("->");
      var7.append(var2.getSimpleName());
      var7.append("->");
      var7.append(var3.getSimpleName());
      var7.append("}");
      this.failureMessage = var7.toString();
   }

   private Resource decodeResource(DataRewinder var1, int var2, int var3, Options var4) throws GlideException {
      List var5 = (List)this.listPool.acquire();

      Resource var8;
      try {
         var8 = this.decodeResourceWithList(var1, var2, var3, var4, var5);
      } finally {
         this.listPool.release(var5);
      }

      return var8;
   }

   private Resource decodeResourceWithList(DataRewinder var1, int var2, int var3, Options var4, List var5) throws GlideException {
      int var6 = this.decoders.size();
      Resource var7 = null;
      int var8 = 0;

      Resource var9;
      while(true) {
         var9 = var7;
         if (var8 >= var6) {
            break;
         }

         ResourceDecoder var10 = (ResourceDecoder)this.decoders.get(var8);
         var9 = var7;

         try {
            if (var10.handles(var1.rewindAndGet(), var4)) {
               var9 = var10.decode(var1.rewindAndGet(), var2, var3, var4);
            }
         } catch (RuntimeException | IOException var12) {
            if (Log.isLoggable("DecodePath", 2)) {
               StringBuilder var11 = new StringBuilder();
               var11.append("Failed to decode data for ");
               var11.append(var10);
               Log.v("DecodePath", var11.toString(), var12);
            }

            var5.add(var12);
            var9 = var7;
         }

         if (var9 != null) {
            break;
         }

         ++var8;
         var7 = var9;
      }

      if (var9 != null) {
         return var9;
      } else {
         throw new GlideException(this.failureMessage, new ArrayList(var5));
      }
   }

   public Resource decode(DataRewinder var1, int var2, int var3, Options var4, DecodePath.DecodeCallback var5) throws GlideException {
      Resource var6 = var5.onResourceDecoded(this.decodeResource(var1, var2, var3, var4));
      return this.transcoder.transcode(var6, var4);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DecodePath{ dataClass=");
      var1.append(this.dataClass);
      var1.append(", decoders=");
      var1.append(this.decoders);
      var1.append(", transcoder=");
      var1.append(this.transcoder);
      var1.append('}');
      return var1.toString();
   }

   interface DecodeCallback {
      Resource onResourceDecoded(Resource var1);
   }
}
