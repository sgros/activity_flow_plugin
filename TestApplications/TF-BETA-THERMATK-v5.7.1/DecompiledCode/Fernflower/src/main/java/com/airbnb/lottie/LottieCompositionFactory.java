package com.airbnb.lottie;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import com.airbnb.lottie.model.LottieCompositionCache;
import com.airbnb.lottie.network.NetworkFetcher;
import com.airbnb.lottie.parser.LottieCompositionParser;
import com.airbnb.lottie.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LottieCompositionFactory {
   private static final Map taskCache = new HashMap();

   private static LottieTask cache(final String var0, Callable var1) {
      final LottieComposition var2;
      if (var0 == null) {
         var2 = null;
      } else {
         var2 = LottieCompositionCache.getInstance().get(var0);
      }

      if (var2 != null) {
         return new LottieTask(new Callable() {
            public LottieResult call() {
               return new LottieResult(var2);
            }
         });
      } else if (var0 != null && taskCache.containsKey(var0)) {
         return (LottieTask)taskCache.get(var0);
      } else {
         LottieTask var3 = new LottieTask(var1);
         var3.addListener(new LottieListener() {
            public void onResult(LottieComposition var1) {
               if (var0 != null) {
                  LottieCompositionCache.getInstance().put(var0, var1);
               }

               LottieCompositionFactory.taskCache.remove(var0);
            }
         });
         var3.addFailureListener(new LottieListener() {
            public void onResult(Throwable var1) {
               LottieCompositionFactory.taskCache.remove(var0);
            }
         });
         taskCache.put(var0, var3);
         return var3;
      }
   }

   private static LottieImageAsset findImageAssetForFileName(LottieComposition var0, String var1) {
      Iterator var2 = var0.getImages().values().iterator();

      LottieImageAsset var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (LottieImageAsset)var2.next();
      } while(!var3.getFileName().equals(var1));

      return var3;
   }

   public static LottieTask fromAsset(Context var0, final String var1) {
      return cache(var1, new Callable(var0.getApplicationContext()) {
         // $FF: synthetic field
         final Context val$appContext;

         {
            this.val$appContext = var1x;
         }

         public LottieResult call() {
            return LottieCompositionFactory.fromAssetSync(this.val$appContext, var1);
         }
      });
   }

   public static LottieResult fromAssetSync(Context var0, String var1) {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append("asset_");
         var2.append(var1);
         String var6 = var2.toString();
         if (var1.endsWith(".zip")) {
            ZipInputStream var3 = new ZipInputStream(var0.getAssets().open(var1));
            return fromZipStreamSync(var3, var6);
         } else {
            LottieResult var5 = fromJsonInputStreamSync(var0.getAssets().open(var1), var6);
            return var5;
         }
      } catch (IOException var4) {
         return new LottieResult(var4);
      }
   }

   public static LottieResult fromJsonInputStreamSync(InputStream var0, String var1) {
      return fromJsonInputStreamSync(var0, var1, true);
   }

   private static LottieResult fromJsonInputStreamSync(InputStream var0, String var1, boolean var2) {
      LottieResult var7;
      try {
         InputStreamReader var4 = new InputStreamReader(var0);
         JsonReader var3 = new JsonReader(var4);
         var7 = fromJsonReaderSync(var3, var1);
      } finally {
         if (var2) {
            Utils.closeQuietly(var0);
         }

      }

      return var7;
   }

   public static LottieTask fromJsonReader(final JsonReader var0, final String var1) {
      return cache(var1, new Callable() {
         public LottieResult call() {
            return LottieCompositionFactory.fromJsonReaderSync(var0, var1);
         }
      });
   }

   public static LottieResult fromJsonReaderSync(JsonReader var0, String var1) {
      return fromJsonReaderSyncInternal(var0, var1, true);
   }

   private static LottieResult fromJsonReaderSyncInternal(JsonReader var0, String var1, boolean var2) {
      LottieResult var8;
      try {
         LottieComposition var3 = LottieCompositionParser.parse(var0);
         LottieCompositionCache.getInstance().put(var1, var3);
         var8 = new LottieResult(var3);
         return var8;
      } catch (Exception var6) {
         var8 = new LottieResult(var6);
      } finally {
         if (var2) {
            Utils.closeQuietly(var0);
         }

      }

      return var8;
   }

   public static LottieTask fromRawRes(final Context var0, final int var1) {
      var0 = var0.getApplicationContext();
      return cache(rawResCacheKey(var1), new Callable() {
         public LottieResult call() {
            return LottieCompositionFactory.fromRawResSync(var0, var1);
         }
      });
   }

   public static LottieResult fromRawResSync(Context var0, int var1) {
      try {
         LottieResult var3 = fromJsonInputStreamSync(var0.getResources().openRawResource(var1), rawResCacheKey(var1));
         return var3;
      } catch (NotFoundException var2) {
         return new LottieResult(var2);
      }
   }

   public static LottieTask fromUrl(final Context var0, final String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("url_");
      var2.append(var1);
      return cache(var2.toString(), new Callable() {
         public LottieResult call() {
            return NetworkFetcher.fetchSync(var0, var1);
         }
      });
   }

   public static LottieResult fromZipStreamSync(ZipInputStream var0, String var1) {
      LottieResult var4;
      try {
         var4 = fromZipStreamSyncInternal(var0, var1);
      } finally {
         Utils.closeQuietly(var0);
      }

      return var4;
   }

   private static LottieResult fromZipStreamSyncInternal(ZipInputStream var0, String var1) {
      HashMap var2 = new HashMap();

      IOException var10000;
      label103: {
         ZipEntry var3;
         boolean var10001;
         try {
            var3 = var0.getNextEntry();
         } catch (IOException var11) {
            var10000 = var11;
            var10001 = false;
            break label103;
         }

         LottieComposition var4 = null;

         while(true) {
            if (var3 == null) {
               if (var4 == null) {
                  return new LottieResult(new IllegalArgumentException("Unable to parse composition"));
               }

               Iterator var13 = var2.entrySet().iterator();

               while(var13.hasNext()) {
                  Entry var21 = (Entry)var13.next();
                  LottieImageAsset var16 = findImageAssetForFileName(var4, (String)var21.getKey());
                  if (var16 != null) {
                     var16.setBitmap(Utils.resizeBitmapIfNeeded((Bitmap)var21.getValue(), var16.getWidth(), var16.getHeight()));
                  }
               }

               Iterator var22 = var4.getImages().entrySet().iterator();

               Entry var14;
               do {
                  if (!var22.hasNext()) {
                     LottieCompositionCache.getInstance().put(var1, var4);
                     return new LottieResult(var4);
                  }

                  var14 = (Entry)var22.next();
               } while(((LottieImageAsset)var14.getValue()).getBitmap() != null);

               StringBuilder var15 = new StringBuilder();
               var15.append("There is no image for ");
               var15.append(((LottieImageAsset)var14.getValue()).getFileName());
               return new LottieResult(new IllegalStateException(var15.toString()));
            }

            label110: {
               String var17;
               try {
                  var17 = var3.getName();
                  if (var17.contains("__MACOSX")) {
                     var0.closeEntry();
                     break label110;
                  }
               } catch (IOException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break;
               }

               try {
                  if (var17.contains(".json")) {
                     InputStreamReader var20 = new InputStreamReader(var0);
                     JsonReader var19 = new JsonReader(var20);
                     var4 = (LottieComposition)fromJsonReaderSyncInternal(var19, (String)null, false).getValue();
                     break label110;
                  }
               } catch (IOException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }

               label111: {
                  label74:
                  try {
                     if (!var17.contains(".png") && !var17.contains(".webp")) {
                        break label74;
                     }
                     break label111;
                  } catch (IOException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break;
                  }

                  try {
                     var0.closeEntry();
                     break label110;
                  } catch (IOException var7) {
                     var10000 = var7;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  String[] var18 = var17.split("/");
                  var2.put(var18[var18.length - 1], BitmapFactory.decodeStream(var0));
               } catch (IOException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break;
               }
            }

            try {
               var3 = var0.getNextEntry();
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break;
            }
         }
      }

      IOException var12 = var10000;
      return new LottieResult(var12);
   }

   private static String rawResCacheKey(int var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("rawRes_");
      var1.append(var0);
      return var1.toString();
   }
}
