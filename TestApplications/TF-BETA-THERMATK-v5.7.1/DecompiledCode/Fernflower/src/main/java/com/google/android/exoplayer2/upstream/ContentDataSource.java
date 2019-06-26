package com.google.android.exoplayer2.upstream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

public final class ContentDataSource extends BaseDataSource {
   private AssetFileDescriptor assetFileDescriptor;
   private long bytesRemaining;
   private FileInputStream inputStream;
   private boolean opened;
   private final ContentResolver resolver;
   private Uri uri;

   public ContentDataSource(Context var1) {
      super(false);
      this.resolver = var1.getContentResolver();
   }

   public void close() throws ContentDataSource.ContentDataSourceException {
      this.uri = null;
      boolean var21 = false;

      ContentDataSource.ContentDataSourceException var2;
      try {
         var21 = true;
         if (this.inputStream != null) {
            this.inputStream.close();
            var21 = false;
         } else {
            var21 = false;
         }
      } catch (IOException var22) {
         var2 = new ContentDataSource.ContentDataSourceException(var22);
         throw var2;
      } finally {
         if (var21) {
            this.inputStream = null;

            try {
               if (this.assetFileDescriptor != null) {
                  this.assetFileDescriptor.close();
               }
            } catch (IOException var23) {
               ContentDataSource.ContentDataSourceException var1 = new ContentDataSource.ContentDataSourceException(var23);
               throw var1;
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
         var2 = new ContentDataSource.ContentDataSourceException(var26);
         throw var2;
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

   public long open(DataSpec var1) throws ContentDataSource.ContentDataSourceException {
      IOException var10000;
      label69: {
         label71: {
            long var3;
            long var5;
            boolean var10001;
            label67: {
               label66: {
                  try {
                     this.uri = var1.uri;
                     this.transferInitializing(var1);
                     this.assetFileDescriptor = this.resolver.openAssetFileDescriptor(this.uri, "r");
                     if (this.assetFileDescriptor != null) {
                        FileInputStream var18 = new FileInputStream(this.assetFileDescriptor.getFileDescriptor());
                        this.inputStream = var18;
                        var3 = this.assetFileDescriptor.getStartOffset();
                        var5 = this.inputStream.skip(var1.position + var3) - var3;
                        if (var5 != var1.position) {
                           break label66;
                        }

                        if (var1.length == -1L) {
                           break label67;
                        }

                        this.bytesRemaining = var1.length;
                        break label71;
                     }
                  } catch (IOException var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label69;
                  }

                  try {
                     StringBuilder var2 = new StringBuilder();
                     var2.append("Could not open file descriptor for: ");
                     var2.append(this.uri);
                     FileNotFoundException var15 = new FileNotFoundException(var2.toString());
                     throw var15;
                  } catch (IOException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label69;
                  }
               }

               try {
                  EOFException var17 = new EOFException();
                  throw var17;
               } catch (IOException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label69;
               }
            }

            try {
               var3 = this.assetFileDescriptor.getLength();
            } catch (IOException var13) {
               var10000 = var13;
               var10001 = false;
               break label69;
            }

            if (var3 == -1L) {
               FileChannel var19;
               try {
                  var19 = this.inputStream.getChannel();
                  var3 = var19.size();
               } catch (IOException var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label69;
               }

               if (var3 == 0L) {
                  var3 = -1L;
               } else {
                  try {
                     var3 -= var19.position();
                  } catch (IOException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label69;
                  }
               }

               try {
                  this.bytesRemaining = var3;
               } catch (IOException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label69;
               }
            } else {
               try {
                  this.bytesRemaining = var3 - var5;
               } catch (IOException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label69;
               }
            }
         }

         this.opened = true;
         this.transferStarted(var1);
         return this.bytesRemaining;
      }

      IOException var16 = var10000;
      throw new ContentDataSource.ContentDataSourceException(var16);
   }

   public int read(byte[] var1, int var2, int var3) throws ContentDataSource.ContentDataSourceException {
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
               throw new ContentDataSource.ContentDataSourceException(var10);
            }

            if (var2 == -1) {
               if (this.bytesRemaining == -1L) {
                  return -1;
               } else {
                  throw new ContentDataSource.ContentDataSourceException(new EOFException());
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

   public static class ContentDataSourceException extends IOException {
      public ContentDataSourceException(IOException var1) {
         super(var1);
      }
   }
}
