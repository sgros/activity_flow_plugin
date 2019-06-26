package com.google.android.exoplayer2.metadata.icy;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IcyDecoder implements MetadataDecoder {
   private static final Pattern METADATA_ELEMENT = Pattern.compile("(.+?)='(.+?)';");

   public Metadata decode(MetadataInputBuffer var1) {
      ByteBuffer var2 = var1.data;
      return this.decode(Util.fromUtf8Bytes(var2.array(), 0, var2.limit()));
   }

   Metadata decode(String var1) {
      Matcher var2 = METADATA_ELEMENT.matcher(var1);
      Object var3 = null;
      String var4 = null;
      var1 = var4;

      for(int var5 = 0; var2.find(var5); var5 = var2.end()) {
         String var6 = Util.toLowerInvariant(var2.group(1));
         String var7 = var2.group(2);
         byte var9 = -1;
         int var8 = var6.hashCode();
         if (var8 != -315603473) {
            if (var8 == 1646559960 && var6.equals("streamtitle")) {
               var9 = 0;
            }
         } else if (var6.equals("streamurl")) {
            var9 = 1;
         }

         if (var9 != 0) {
            if (var9 != 1) {
               StringBuilder var10 = new StringBuilder();
               var10.append("Unrecognized ICY tag: ");
               var10.append(var4);
               Log.w("IcyDecoder", var10.toString());
            } else {
               var1 = var7;
            }
         } else {
            var4 = var7;
         }
      }

      Metadata var11;
      if (var4 == null) {
         var11 = (Metadata)var3;
         if (var1 == null) {
            return var11;
         }
      }

      var11 = new Metadata(new Metadata.Entry[]{new IcyInfo(var4, var1)});
      return var11;
   }
}
