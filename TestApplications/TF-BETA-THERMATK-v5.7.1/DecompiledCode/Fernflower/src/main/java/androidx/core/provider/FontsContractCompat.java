package androidx.core.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Build.VERSION;
import androidx.collection.LruCache;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FontsContractCompat {
   private static final SelfDestructiveThread sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
   private static final Comparator sByteArrayComparator = new Comparator() {
      public int compare(byte[] var1, byte[] var2) {
         int var3;
         int var4;
         if (var1.length != var2.length) {
            var3 = var1.length;
            var4 = var2.length;
         } else {
            var3 = 0;

            while(true) {
               if (var3 >= var1.length) {
                  return 0;
               }

               if (var1[var3] != var2[var3]) {
                  byte var6 = var1[var3];
                  byte var5 = var2[var3];
                  var3 = var6;
                  var4 = var5;
                  break;
               }

               ++var3;
            }
         }

         return var3 - var4;
      }
   };
   static final Object sLock = new Object();
   static final SimpleArrayMap sPendingReplies = new SimpleArrayMap();
   static final LruCache sTypefaceCache = new LruCache(16);

   private static List convertToByteArrayList(Signature[] var0) {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(var0[var2].toByteArray());
      }

      return var1;
   }

   private static boolean equalsByteArrayList(List var0, List var1) {
      if (var0.size() != var1.size()) {
         return false;
      } else {
         for(int var2 = 0; var2 < var0.size(); ++var2) {
            if (!Arrays.equals((byte[])var0.get(var2), (byte[])var1.get(var2))) {
               return false;
            }
         }

         return true;
      }
   }

   public static FontsContractCompat.FontFamilyResult fetchFonts(Context var0, CancellationSignal var1, FontRequest var2) throws NameNotFoundException {
      ProviderInfo var3 = getProvider(var0.getPackageManager(), var2, var0.getResources());
      return var3 == null ? new FontsContractCompat.FontFamilyResult(1, (FontsContractCompat.FontInfo[])null) : new FontsContractCompat.FontFamilyResult(0, getFontFromProvider(var0, var2, var3.authority, var1));
   }

   private static List getCertificates(FontRequest var0, Resources var1) {
      return var0.getCertificates() != null ? var0.getCertificates() : FontResourcesParserCompat.readCerts(var1, var0.getCertificatesArrayResId());
   }

   static FontsContractCompat.FontInfo[] getFontFromProvider(Context var0, FontRequest var1, String var2, CancellationSignal var3) {
      ArrayList var4 = new ArrayList();
      Uri var5 = (new Builder()).scheme("content").authority(var2).build();
      Uri var6 = (new Builder()).scheme("content").authority(var2).appendPath("file").build();

      Cursor var174;
      ArrayList var177;
      label1486: {
         label1490: {
            Throwable var176;
            label1491: {
               int var7;
               Throwable var10000;
               boolean var10001;
               label1483: {
                  label1482: {
                     try {
                        var7 = VERSION.SDK_INT;
                     } catch (Throwable var172) {
                        var10000 = var172;
                        var10001 = false;
                        break label1482;
                     }

                     ContentResolver var173;
                     String var175;
                     if (var7 > 16) {
                        label1476:
                        try {
                           var173 = var0.getContentResolver();
                           var175 = var1.getQuery();
                           var174 = var173.query(var5, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var175}, (String)null, var3);
                        } catch (Throwable var170) {
                           var10000 = var170;
                           var10001 = false;
                           break label1476;
                        }
                     } else {
                        label1478:
                        try {
                           var173 = var0.getContentResolver();
                           var175 = var1.getQuery();
                           var174 = var173.query(var5, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var175}, (String)null);
                        } catch (Throwable var171) {
                           var10000 = var171;
                           var10001 = false;
                           break label1478;
                        }
                     }
                     break label1483;
                  }

                  var176 = var10000;
                  var174 = null;
                  break label1491;
               }

               if (var174 == null) {
                  break label1490;
               }

               label1470: {
                  int var8;
                  int var9;
                  int var10;
                  int var11;
                  int var12;
                  int var13;
                  ArrayList var178;
                  try {
                     if (var174.getCount() <= 0) {
                        break label1490;
                     }

                     var8 = var174.getColumnIndex("result_code");
                     var178 = new ArrayList();
                     var9 = var174.getColumnIndex("_id");
                     var10 = var174.getColumnIndex("file_id");
                     var11 = var174.getColumnIndex("font_ttc_index");
                     var12 = var174.getColumnIndex("font_weight");
                     var13 = var174.getColumnIndex("font_italic");
                  } catch (Throwable var169) {
                     var10000 = var169;
                     var10001 = false;
                     break label1470;
                  }

                  while(true) {
                     var177 = var178;

                     try {
                        if (!var174.moveToNext()) {
                           break label1486;
                        }
                     } catch (Throwable var167) {
                        var10000 = var167;
                        var10001 = false;
                        break;
                     }

                     if (var8 != -1) {
                        try {
                           var7 = var174.getInt(var8);
                        } catch (Throwable var166) {
                           var10000 = var166;
                           var10001 = false;
                           break;
                        }
                     } else {
                        var7 = 0;
                     }

                     int var14;
                     if (var11 != -1) {
                        try {
                           var14 = var174.getInt(var11);
                        } catch (Throwable var165) {
                           var10000 = var165;
                           var10001 = false;
                           break;
                        }
                     } else {
                        var14 = 0;
                     }

                     Uri var179;
                     if (var10 == -1) {
                        try {
                           var179 = ContentUris.withAppendedId(var5, var174.getLong(var9));
                        } catch (Throwable var164) {
                           var10000 = var164;
                           var10001 = false;
                           break;
                        }
                     } else {
                        try {
                           var179 = ContentUris.withAppendedId(var6, var174.getLong(var10));
                        } catch (Throwable var163) {
                           var10000 = var163;
                           var10001 = false;
                           break;
                        }
                     }

                     int var15;
                     if (var12 != -1) {
                        try {
                           var15 = var174.getInt(var12);
                        } catch (Throwable var162) {
                           var10000 = var162;
                           var10001 = false;
                           break;
                        }
                     } else {
                        var15 = 400;
                     }

                     boolean var16;
                     label1460: {
                        label1459: {
                           if (var13 != -1) {
                              try {
                                 if (var174.getInt(var13) == 1) {
                                    break label1459;
                                 }
                              } catch (Throwable var168) {
                                 var10000 = var168;
                                 var10001 = false;
                                 break;
                              }
                           }

                           var16 = false;
                           break label1460;
                        }

                        var16 = true;
                     }

                     try {
                        FontsContractCompat.FontInfo var180 = new FontsContractCompat.FontInfo(var179, var14, var15, var16, var7);
                        var178.add(var180);
                     } catch (Throwable var161) {
                        var10000 = var161;
                        var10001 = false;
                        break;
                     }
                  }
               }

               var176 = var10000;
            }

            if (var174 != null) {
               var174.close();
            }

            throw var176;
         }

         var177 = var4;
      }

      if (var174 != null) {
         var174.close();
      }

      return (FontsContractCompat.FontInfo[])var177.toArray(new FontsContractCompat.FontInfo[0]);
   }

   static FontsContractCompat.TypefaceResult getFontInternal(Context var0, FontRequest var1, int var2) {
      FontsContractCompat.FontFamilyResult var7;
      try {
         var7 = fetchFonts(var0, (CancellationSignal)null, var1);
      } catch (NameNotFoundException var5) {
         return new FontsContractCompat.TypefaceResult((Typeface)null, -1);
      }

      int var3 = var7.getStatusCode();
      byte var4 = -3;
      if (var3 == 0) {
         Typeface var6 = TypefaceCompat.createFromFontInfo(var0, (CancellationSignal)null, var7.getFonts(), var2);
         if (var6 != null) {
            var4 = 0;
         }

         return new FontsContractCompat.TypefaceResult(var6, var4);
      } else {
         if (var7.getStatusCode() == 1) {
            var4 = -2;
         }

         return new FontsContractCompat.TypefaceResult((Typeface)null, var4);
      }
   }

   public static Typeface getFontSync(final Context var0, final FontRequest var1, final ResourcesCompat.FontCallback var2, final Handler var3, boolean var4, int var5, final int var6) {
      StringBuilder var7 = new StringBuilder();
      var7.append(var1.getIdentifier());
      var7.append("-");
      var7.append(var6);
      final String var66 = var7.toString();
      Typeface var8 = (Typeface)sTypefaceCache.get(var66);
      if (var8 != null) {
         if (var2 != null) {
            var2.onFontRetrieved(var8);
         }

         return var8;
      } else if (var4 && var5 == -1) {
         FontsContractCompat.TypefaceResult var64 = getFontInternal(var0, var1, var6);
         if (var2 != null) {
            var5 = var64.mResult;
            if (var5 == 0) {
               var2.callbackSuccessAsync(var64.mTypeface, var3);
            } else {
               var2.callbackFailAsync(var5, var3);
            }
         }

         return var64.mTypeface;
      } else {
         Callable var60 = new Callable() {
            public FontsContractCompat.TypefaceResult call() throws Exception {
               FontsContractCompat.TypefaceResult var1x = FontsContractCompat.getFontInternal(var0, var1, var6);
               Typeface var2 = var1x.mTypeface;
               if (var2 != null) {
                  FontsContractCompat.sTypefaceCache.put(var66, var2);
               }

               return var1x;
            }
         };
         Typeface var58 = null;
         if (var4) {
            Typeface var61;
            try {
               var61 = ((FontsContractCompat.TypefaceResult)sBackgroundThread.postAndWait(var60, var5)).mTypeface;
            } catch (InterruptedException var52) {
               return var58;
            }

            var58 = var61;
            return var58;
         } else {
            SelfDestructiveThread.ReplyCallback var59;
            if (var2 == null) {
               var59 = null;
            } else {
               var59 = new SelfDestructiveThread.ReplyCallback() {
                  public void onReply(FontsContractCompat.TypefaceResult var1) {
                     if (var1 == null) {
                        var2.callbackFailAsync(1, var3);
                     } else {
                        int var2x = var1.mResult;
                        if (var2x == 0) {
                           var2.callbackSuccessAsync(var1.mTypeface, var3);
                        } else {
                           var2.callbackFailAsync(var2x, var3);
                        }
                     }

                  }
               };
            }

            Object var63 = sLock;
            synchronized(var63){}

            Throwable var10000;
            boolean var10001;
            label601: {
               ArrayList var65;
               try {
                  var65 = (ArrayList)sPendingReplies.get(var66);
               } catch (Throwable var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label601;
               }

               if (var65 != null) {
                  label590: {
                     if (var59 != null) {
                        try {
                           var65.add(var59);
                        } catch (Throwable var54) {
                           var10000 = var54;
                           var10001 = false;
                           break label590;
                        }
                     }

                     label586:
                     try {
                        return null;
                     } catch (Throwable var53) {
                        var10000 = var53;
                        var10001 = false;
                        break label586;
                     }
                  }
               } else {
                  label611: {
                     if (var59 != null) {
                        try {
                           var65 = new ArrayList();
                           var65.add(var59);
                           sPendingReplies.put(var66, var65);
                        } catch (Throwable var56) {
                           var10000 = var56;
                           var10001 = false;
                           break label611;
                        }
                     }

                     try {
                        ;
                     } catch (Throwable var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label611;
                     }

                     sBackgroundThread.postAndReply(var60, new SelfDestructiveThread.ReplyCallback() {
                        public void onReply(FontsContractCompat.TypefaceResult var1) {
                           Object var2 = FontsContractCompat.sLock;
                           synchronized(var2){}

                           Throwable var10000;
                           boolean var10001;
                           label222: {
                              ArrayList var3;
                              try {
                                 var3 = (ArrayList)FontsContractCompat.sPendingReplies.get(var66);
                              } catch (Throwable var24) {
                                 var10000 = var24;
                                 var10001 = false;
                                 break label222;
                              }

                              if (var3 == null) {
                                 label215:
                                 try {
                                    return;
                                 } catch (Throwable var22) {
                                    var10000 = var22;
                                    var10001 = false;
                                    break label215;
                                 }
                              } else {
                                 label226: {
                                    try {
                                       FontsContractCompat.sPendingReplies.remove(var66);
                                    } catch (Throwable var23) {
                                       var10000 = var23;
                                       var10001 = false;
                                       break label226;
                                    }

                                    for(int var4 = 0; var4 < var3.size(); ++var4) {
                                       ((SelfDestructiveThread.ReplyCallback)var3.get(var4)).onReply(var1);
                                    }

                                    return;
                                 }
                              }
                           }

                           while(true) {
                              Throwable var25 = var10000;

                              try {
                                 throw var25;
                              } catch (Throwable var21) {
                                 var10000 = var21;
                                 var10001 = false;
                                 continue;
                              }
                           }
                        }
                     });
                     return null;
                  }
               }
            }

            while(true) {
               Throwable var62 = var10000;

               try {
                  throw var62;
               } catch (Throwable var51) {
                  var10000 = var51;
                  var10001 = false;
                  continue;
               }
            }
         }
      }
   }

   public static ProviderInfo getProvider(PackageManager var0, FontRequest var1, Resources var2) throws NameNotFoundException {
      String var3 = var1.getProviderAuthority();
      int var4 = 0;
      ProviderInfo var5 = var0.resolveContentProvider(var3, 0);
      StringBuilder var6;
      if (var5 != null) {
         if (var5.packageName.equals(var1.getProviderPackage())) {
            List var7 = convertToByteArrayList(var0.getPackageInfo(var5.packageName, 64).signatures);
            Collections.sort(var7, sByteArrayComparator);

            for(List var9 = getCertificates(var1, var2); var4 < var9.size(); ++var4) {
               ArrayList var8 = new ArrayList((Collection)var9.get(var4));
               Collections.sort(var8, sByteArrayComparator);
               if (equalsByteArrayList(var7, var8)) {
                  return var5;
               }
            }

            return null;
         } else {
            var6 = new StringBuilder();
            var6.append("Found content provider ");
            var6.append(var3);
            var6.append(", but package was not ");
            var6.append(var1.getProviderPackage());
            throw new NameNotFoundException(var6.toString());
         }
      } else {
         var6 = new StringBuilder();
         var6.append("No package found for authority: ");
         var6.append(var3);
         throw new NameNotFoundException(var6.toString());
      }
   }

   public static Map prepareFontData(Context var0, FontsContractCompat.FontInfo[] var1, CancellationSignal var2) {
      HashMap var3 = new HashMap();
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         FontsContractCompat.FontInfo var6 = var1[var5];
         if (var6.getResultCode() == 0) {
            Uri var7 = var6.getUri();
            if (!var3.containsKey(var7)) {
               var3.put(var7, TypefaceCompatUtil.mmap(var0, var2, var7));
            }
         }
      }

      return Collections.unmodifiableMap(var3);
   }

   public static class FontFamilyResult {
      private final FontsContractCompat.FontInfo[] mFonts;
      private final int mStatusCode;

      public FontFamilyResult(int var1, FontsContractCompat.FontInfo[] var2) {
         this.mStatusCode = var1;
         this.mFonts = var2;
      }

      public FontsContractCompat.FontInfo[] getFonts() {
         return this.mFonts;
      }

      public int getStatusCode() {
         return this.mStatusCode;
      }
   }

   public static class FontInfo {
      private final boolean mItalic;
      private final int mResultCode;
      private final int mTtcIndex;
      private final Uri mUri;
      private final int mWeight;

      public FontInfo(Uri var1, int var2, int var3, boolean var4, int var5) {
         Preconditions.checkNotNull(var1);
         this.mUri = (Uri)var1;
         this.mTtcIndex = var2;
         this.mWeight = var3;
         this.mItalic = var4;
         this.mResultCode = var5;
      }

      public int getResultCode() {
         return this.mResultCode;
      }

      public int getTtcIndex() {
         return this.mTtcIndex;
      }

      public Uri getUri() {
         return this.mUri;
      }

      public int getWeight() {
         return this.mWeight;
      }

      public boolean isItalic() {
         return this.mItalic;
      }
   }

   private static final class TypefaceResult {
      final int mResult;
      final Typeface mTypeface;

      TypefaceResult(Typeface var1, int var2) {
         this.mTypeface = var1;
         this.mResult = var2;
      }
   }
}
