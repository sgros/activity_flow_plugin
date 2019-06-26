package com.google.android.exoplayer2.upstream;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public final class AssetDataSource extends BaseDataSource {
   private final AssetManager assetManager;
   private long bytesRemaining;
   private InputStream inputStream;
   private boolean opened;
   private Uri uri;

   public AssetDataSource(Context var1) {
      super(false);
      this.assetManager = var1.getAssets();
   }

   public void close() throws AssetDataSource.AssetDataSourceException {
      this.uri = null;

      try {
         if (this.inputStream != null) {
            this.inputStream.close();
         }
      } catch (IOException var5) {
         AssetDataSource.AssetDataSourceException var1 = new AssetDataSource.AssetDataSourceException(var5);
         throw var1;
      } finally {
         this.inputStream = null;
         if (this.opened) {
            this.opened = false;
            this.transferEnded();
         }

      }

   }

   public Uri getUri() {
      return this.uri;
   }

   public long open(DataSpec var1) throws AssetDataSource.AssetDataSourceException {
      IOException var10000;
      label63: {
         String var3;
         boolean var10001;
         label61: {
            String var2;
            try {
               this.uri = var1.uri;
               var2 = this.uri.getPath();
               if (var2.startsWith("/android_asset/")) {
                  var3 = var2.substring(15);
                  break label61;
               }
            } catch (IOException var8) {
               var10000 = var8;
               var10001 = false;
               break label63;
            }

            var3 = var2;

            try {
               if (var2.startsWith("/")) {
                  var3 = var2.substring(1);
               }
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
               break label63;
            }
         }

         label48: {
            label47: {
               try {
                  this.transferInitializing(var1);
                  this.inputStream = this.assetManager.open(var3, 1);
                  if (this.inputStream.skip(var1.position) < var1.position) {
                     break label48;
                  }

                  if (var1.length != -1L) {
                     this.bytesRemaining = var1.length;
                     break label47;
                  }
               } catch (IOException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label63;
               }

               try {
                  this.bytesRemaining = (long)this.inputStream.available();
                  if (this.bytesRemaining == 2147483647L) {
                     this.bytesRemaining = -1L;
                  }
               } catch (IOException var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label63;
               }
            }

            this.opened = true;
            this.transferStarted(var1);
            return this.bytesRemaining;
         }

         try {
            EOFException var10 = new EOFException();
            throw var10;
         } catch (IOException var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      IOException var9 = var10000;
      throw new AssetDataSource.AssetDataSourceException(var9);
   }

   public int read(byte[] var1, int var2, int var3) throws AssetDataSource.AssetDataSourceException {
      if (var3 == 0) {
         return 0;
      } else {
         long var4 = this.bytesRemaining;
         if (var4 == 0L) {
            return -1;
         } else {
            long var6;
            label41: {
               IOException var10000;
               label40: {
                  boolean var10001;
                  if (var4 != -1L) {
                     var6 = (long)var3;

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
               throw new AssetDataSource.AssetDataSourceException(var10);
            }

            if (var2 == -1) {
               if (this.bytesRemaining == -1L) {
                  return -1;
               } else {
                  throw new AssetDataSource.AssetDataSourceException(new EOFException());
               }
            } else {
               var6 = this.bytesRemaining;
               if (var6 != -1L) {
                  this.bytesRemaining = var6 - (long)var2;
               }

               this.bytesTransferred(var2);
               return var2;
            }
         }
      }
   }

   public static final class AssetDataSourceException extends IOException {
      public AssetDataSourceException(IOException var1) {
         super(var1);
      }
   }
}
