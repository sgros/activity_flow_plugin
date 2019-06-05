package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class ThumbnailStreamOpener {
   private static final FileService DEFAULT_SERVICE = new FileService();
   private final ArrayPool byteArrayPool;
   private final ContentResolver contentResolver;
   private final List parsers;
   private final ThumbnailQuery query;
   private final FileService service;

   public ThumbnailStreamOpener(List var1, FileService var2, ThumbnailQuery var3, ArrayPool var4, ContentResolver var5) {
      this.service = var2;
      this.query = var3;
      this.byteArrayPool = var4;
      this.contentResolver = var5;
      this.parsers = var1;
   }

   public ThumbnailStreamOpener(List var1, ThumbnailQuery var2, ArrayPool var3, ContentResolver var4) {
      this(var1, DEFAULT_SERVICE, var2, var3, var4);
   }

   public int getOrientation(Uri var1) {
      InputStream var2;
      int var4;
      label865: {
         InputStream var3;
         Throwable var10000;
         label866: {
            IOException var5;
            boolean var10001;
            label867: {
               label859: {
                  try {
                     var2 = this.contentResolver.openInputStream(var1);
                     break label859;
                  } catch (NullPointerException | IOException var122) {
                     var5 = var122;
                  } finally {
                     ;
                  }

                  var2 = null;
                  break label867;
               }

               var3 = var2;

               try {
                  try {
                     var4 = ImageHeaderParserUtils.getOrientation(this.parsers, var2, this.byteArrayPool);
                     break label865;
                  } catch (NullPointerException | IOException var120) {
                     var5 = var120;
                  }
               } catch (Throwable var121) {
                  var10000 = var121;
                  var10001 = false;
                  break label866;
               }
            }

            var3 = var2;

            label868: {
               try {
                  if (!Log.isLoggable("ThumbStreamOpener", 3)) {
                     break label868;
                  }
               } catch (Throwable var119) {
                  var10000 = var119;
                  var10001 = false;
                  break label866;
               }

               var3 = var2;

               StringBuilder var6;
               try {
                  var6 = new StringBuilder;
               } catch (Throwable var118) {
                  var10000 = var118;
                  var10001 = false;
                  break label866;
               }

               var3 = var2;

               try {
                  var6.<init>();
               } catch (Throwable var117) {
                  var10000 = var117;
                  var10001 = false;
                  break label866;
               }

               var3 = var2;

               try {
                  var6.append("Failed to open uri: ");
               } catch (Throwable var116) {
                  var10000 = var116;
                  var10001 = false;
                  break label866;
               }

               var3 = var2;

               try {
                  var6.append(var1);
               } catch (Throwable var115) {
                  var10000 = var115;
                  var10001 = false;
                  break label866;
               }

               var3 = var2;

               try {
                  Log.d("ThumbStreamOpener", var6.toString(), var5);
               } catch (Throwable var114) {
                  var10000 = var114;
                  var10001 = false;
                  break label866;
               }
            }

            if (var2 != null) {
               try {
                  var2.close();
               } catch (IOException var111) {
               }
            }

            return -1;
         }

         Throwable var124 = var10000;
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var112) {
            }
         }

         throw var124;
      }

      if (var2 != null) {
         try {
            var2.close();
         } catch (IOException var113) {
         }
      }

      return var4;
   }

   public InputStream open(Uri var1) throws FileNotFoundException {
      Cursor var2 = this.query.query(var1);
      InputStream var3 = null;
      if (var2 != null) {
         label273: {
            Throwable var10000;
            label272: {
               boolean var10001;
               try {
                  if (!var2.moveToFirst()) {
                     break label273;
                  }
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label272;
               }

               boolean var4;
               String var21;
               try {
                  var21 = var2.getString(0);
                  var4 = TextUtils.isEmpty(var21);
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label272;
               }

               if (var4) {
                  if (var2 != null) {
                     var2.close();
                  }

                  return null;
               }

               label255: {
                  try {
                     File var23 = this.service.get(var21);
                     if (this.service.exists(var23) && this.service.length(var23) > 0L) {
                        var1 = Uri.fromFile(var23);
                        break label255;
                     }
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label272;
                  }

                  var1 = null;
               }

               if (var2 != null) {
                  var2.close();
               }

               if (var1 != null) {
                  try {
                     var3 = this.contentResolver.openInputStream(var1);
                  } catch (NullPointerException var17) {
                     StringBuilder var24 = new StringBuilder();
                     var24.append("NPE opening uri: ");
                     var24.append(var1);
                     throw (FileNotFoundException)(new FileNotFoundException(var24.toString())).initCause(var17);
                  }
               }

               return var3;
            }

            Throwable var22 = var10000;
            if (var2 != null) {
               var2.close();
            }

            throw var22;
         }
      }

      if (var2 != null) {
         var2.close();
      }

      return null;
   }
}
