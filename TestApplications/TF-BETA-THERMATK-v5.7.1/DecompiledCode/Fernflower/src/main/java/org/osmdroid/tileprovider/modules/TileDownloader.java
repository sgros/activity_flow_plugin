package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

public class TileDownloader {
   public long computeExpirationTime(String var1, String var2, long var3) {
      Long var5 = Configuration.getInstance().getExpirationOverrideDuration();
      if (var5 != null) {
         return var3 + var5;
      } else {
         long var6 = Configuration.getInstance().getExpirationExtendedDuration();
         Long var9 = this.getHttpCacheControlDuration(var2);
         if (var9 != null) {
            return var3 + var9 * 1000L + var6;
         } else {
            Long var8 = this.getHttpExpiresTime(var1);
            return var8 != null ? var8 + var6 : var3 + 604800000L + var6;
         }
      }
   }

   public Drawable downloadTile(long param1, int param3, String param4, IFilesystemCache param5, OnlineTileSourceBase param6) throws CantContinueException {
      // $FF: Couldn't be decompiled
   }

   public Long getHttpCacheControlDuration(String var1) {
      if (var1 != null && var1.length() > 0) {
         Exception var10000;
         label47: {
            boolean var10001;
            String[] var2;
            int var3;
            try {
               var2 = var1.split(", ");
               var3 = var2.length;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label47;
            }

            int var4 = 0;

            while(true) {
               if (var4 >= var3) {
                  return null;
               }

               String var5 = var2[var4];

               try {
                  if (var5.indexOf("max-age=") == 0) {
                     Long var9 = Long.valueOf(var5.substring(8));
                     return var9;
                  }
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break;
               }

               ++var4;
            }
         }

         Exception var8 = var10000;
         if (Configuration.getInstance().isDebugMapTileDownloader()) {
            StringBuilder var10 = new StringBuilder();
            var10.append("Unable to parse cache control tag for tile, server returned ");
            var10.append(var1);
            Log.d("OsmDroid", var10.toString(), var8);
         }
      }

      return null;
   }

   public Long getHttpExpiresTime(String var1) {
      if (var1 != null && var1.length() > 0) {
         long var2;
         try {
            var2 = Configuration.getInstance().getHttpHeaderDateTimeFormat().parse(var1).getTime();
         } catch (Exception var6) {
            if (Configuration.getInstance().isDebugMapTileDownloader()) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Unable to parse expiration tag for tile, server returned ");
               var5.append(var1);
               Log.d("OsmDroid", var5.toString(), var6);
            }

            return null;
         }

         return var2;
      } else {
         return null;
      }
   }
}
