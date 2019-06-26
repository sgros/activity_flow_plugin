package com.google.android.exoplayer2.upstream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public final class RawResourceDataSource extends BaseDataSource {
   private AssetFileDescriptor assetFileDescriptor;
   private long bytesRemaining;
   private InputStream inputStream;
   private boolean opened;
   private final Resources resources;
   private Uri uri;

   public RawResourceDataSource(Context var1) {
      super(false);
      this.resources = var1.getResources();
   }

   public void close() throws RawResourceDataSource.RawResourceDataSourceException {
      this.uri = null;
      boolean var21 = false;

      RawResourceDataSource.RawResourceDataSourceException var1;
      try {
         var21 = true;
         if (this.inputStream != null) {
            this.inputStream.close();
            var21 = false;
         } else {
            var21 = false;
         }
      } catch (IOException var22) {
         var1 = new RawResourceDataSource.RawResourceDataSourceException(var22);
         throw var1;
      } finally {
         if (var21) {
            this.inputStream = null;

            try {
               if (this.assetFileDescriptor != null) {
                  this.assetFileDescriptor.close();
               }
            } catch (IOException var23) {
               RawResourceDataSource.RawResourceDataSourceException var2 = new RawResourceDataSource.RawResourceDataSourceException(var23);
               throw var2;
            } finally {
               this.assetFileDescriptor = null;
               if (this.opened) {
                  this.opened = false;
                  this.transferEnded();
               }

            }

         }
      }

      this.inputStream = null;

      try {
         if (this.assetFileDescriptor != null) {
            this.assetFileDescriptor.close();
         }
      } catch (IOException var26) {
         var1 = new RawResourceDataSource.RawResourceDataSourceException(var26);
         throw var1;
      } finally {
         this.assetFileDescriptor = null;
         if (this.opened) {
            this.opened = false;
            this.transferEnded();
         }

      }

   }

   public Uri getUri() {
      return this.uri;
   }

   public long open(DataSpec param1) throws RawResourceDataSource.RawResourceDataSourceException {
      // $FF: Couldn't be decompiled
   }

   public int read(byte[] var1, int var2, int var3) throws RawResourceDataSource.RawResourceDataSourceException {
      if (var3 == 0) {
         return 0;
      } else {
         long var4 = this.bytesRemaining;
         if (var4 == 0L) {
            return -1;
         } else {
            label41: {
               IOException var10000;
               label40: {
                  boolean var10001;
                  if (var4 != -1L) {
                     long var6 = (long)var3;

                     try {
                        var3 = (int)Math.min(var4, var6);
                     } catch (IOException var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label40;
                     }
                  }

                  try {
                     var2 = this.inputStream.read(var1, var2, var3);
                     break label41;
                  } catch (IOException var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               }

               IOException var10 = var10000;
               throw new RawResourceDataSource.RawResourceDataSourceException(var10);
            }

            if (var2 == -1) {
               if (this.bytesRemaining == -1L) {
                  return -1;
               } else {
                  throw new RawResourceDataSource.RawResourceDataSourceException(new EOFException());
               }
            } else {
               var4 = this.bytesRemaining;
               if (var4 != -1L) {
                  this.bytesRemaining = var4 - (long)var2;
               }

               this.bytesTransferred(var2);
               return var2;
            }
         }
      }
   }

   public static class RawResourceDataSourceException extends IOException {
      public RawResourceDataSourceException(IOException var1) {
         super(var1);
      }

      public RawResourceDataSourceException(String var1) {
         super(var1);
      }
   }
}
