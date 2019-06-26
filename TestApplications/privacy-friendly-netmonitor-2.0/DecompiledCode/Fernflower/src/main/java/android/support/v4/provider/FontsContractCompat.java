package android.support.v4.provider;

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
import android.provider.BaseColumns;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.util.LruCache;
import android.support.v4.util.Preconditions;
import android.support.v4.util.SimpleArrayMap;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
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
   private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final String PARCEL_FONT_RESULTS = "font_results";
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
   private static final String TAG = "FontsContractCompat";
   private static final SelfDestructiveThread sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
   private static final Comparator sByteArrayComparator = new Comparator() {
      public int compare(byte[] var1, byte[] var2) {
         if (var1.length != var2.length) {
            return var1.length - var2.length;
         } else {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var1[var3] != var2[var3]) {
                  return var1[var3] - var2[var3];
               }
            }

            return 0;
         }
      }
   };
   private static final Object sLock = new Object();
   @GuardedBy("sLock")
   private static final SimpleArrayMap sPendingReplies = new SimpleArrayMap();
   private static final LruCache sTypefaceCache = new LruCache(16);

   private FontsContractCompat() {
   }

   // $FF: synthetic method
   static Object access$200() {
      return sLock;
   }

   // $FF: synthetic method
   static SimpleArrayMap access$300() {
      return sPendingReplies;
   }

   public static Typeface buildTypeface(@NonNull Context var0, @Nullable CancellationSignal var1, @NonNull FontsContractCompat.FontInfo[] var2) {
      return TypefaceCompat.createFromFontInfo(var0, var1, var2, 0);
   }

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

   @NonNull
   public static FontsContractCompat.FontFamilyResult fetchFonts(@NonNull Context var0, @Nullable CancellationSignal var1, @NonNull FontRequest var2) throws NameNotFoundException {
      ProviderInfo var3 = getProvider(var0.getPackageManager(), var2, var0.getResources());
      return var3 == null ? new FontsContractCompat.FontFamilyResult(1, (FontsContractCompat.FontInfo[])null) : new FontsContractCompat.FontFamilyResult(0, getFontFromProvider(var0, var2, var3.authority, var1));
   }

   private static List getCertificates(FontRequest var0, Resources var1) {
      return var0.getCertificates() != null ? var0.getCertificates() : FontResourcesParserCompat.readCerts(var1, var0.getCertificatesArrayResId());
   }

   @NonNull
   @VisibleForTesting
   static FontsContractCompat.FontInfo[] getFontFromProvider(Context var0, FontRequest var1, String var2, CancellationSignal var3) {
      ArrayList var4 = new ArrayList();
      Uri var5 = (new Builder()).scheme("content").authority(var2).build();
      Uri var6 = (new Builder()).scheme("content").authority(var2).appendPath("file").build();
      Object var7 = null;
      Cursor var725 = (Cursor)var7;

      Cursor var721;
      ArrayList var724;
      label5993: {
         ArrayList var727;
         label5997: {
            Throwable var10000;
            label5998: {
               boolean var10001;
               label5999: {
                  ContentResolver var720;
                  String var722;
                  label6000: {
                     try {
                        if (VERSION.SDK_INT > 16) {
                           break label6000;
                        }
                     } catch (Throwable var719) {
                        var10000 = var719;
                        var10001 = false;
                        break label5998;
                     }

                     var725 = (Cursor)var7;

                     try {
                        var720 = var0.getContentResolver();
                     } catch (Throwable var715) {
                        var10000 = var715;
                        var10001 = false;
                        break label5998;
                     }

                     var725 = (Cursor)var7;

                     try {
                        var722 = var1.getQuery();
                     } catch (Throwable var714) {
                        var10000 = var714;
                        var10001 = false;
                        break label5998;
                     }

                     var725 = (Cursor)var7;

                     try {
                        var721 = var720.query(var5, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var722}, (String)null);
                        break label5999;
                     } catch (Throwable var713) {
                        var10000 = var713;
                        var10001 = false;
                        break label5998;
                     }
                  }

                  var725 = (Cursor)var7;

                  try {
                     var720 = var0.getContentResolver();
                  } catch (Throwable var718) {
                     var10000 = var718;
                     var10001 = false;
                     break label5998;
                  }

                  var725 = (Cursor)var7;

                  try {
                     var722 = var1.getQuery();
                  } catch (Throwable var717) {
                     var10000 = var717;
                     var10001 = false;
                     break label5998;
                  }

                  var725 = (Cursor)var7;

                  try {
                     var721 = var720.query(var5, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var722}, (String)null, var3);
                  } catch (Throwable var716) {
                     var10000 = var716;
                     var10001 = false;
                     break label5998;
                  }
               }

               var724 = var4;
               if (var721 == null) {
                  break label5993;
               }

               var724 = var4;
               var725 = var721;

               try {
                  if (var721.getCount() <= 0) {
                     break label5993;
                  }
               } catch (Throwable var712) {
                  var10000 = var712;
                  var10001 = false;
                  break label5998;
               }

               var725 = var721;

               int var8;
               try {
                  var8 = var721.getColumnIndex("result_code");
               } catch (Throwable var711) {
                  var10000 = var711;
                  var10001 = false;
                  break label5998;
               }

               var725 = var721;

               try {
                  var727 = new ArrayList;
               } catch (Throwable var710) {
                  var10000 = var710;
                  var10001 = false;
                  break label5998;
               }

               var725 = var721;

               try {
                  var727.<init>();
               } catch (Throwable var709) {
                  var10000 = var709;
                  var10001 = false;
                  break label5998;
               }

               var725 = var721;

               int var9;
               try {
                  var9 = var721.getColumnIndex("_id");
               } catch (Throwable var708) {
                  var10000 = var708;
                  var10001 = false;
                  break label5998;
               }

               var725 = var721;

               int var10;
               try {
                  var10 = var721.getColumnIndex("file_id");
               } catch (Throwable var707) {
                  var10000 = var707;
                  var10001 = false;
                  break label5998;
               }

               var725 = var721;

               int var11;
               try {
                  var11 = var721.getColumnIndex("font_ttc_index");
               } catch (Throwable var706) {
                  var10000 = var706;
                  var10001 = false;
                  break label5998;
               }

               var725 = var721;

               int var12;
               try {
                  var12 = var721.getColumnIndex("font_weight");
               } catch (Throwable var705) {
                  var10000 = var705;
                  var10001 = false;
                  break label5998;
               }

               var725 = var721;

               int var13;
               try {
                  var13 = var721.getColumnIndex("font_italic");
               } catch (Throwable var704) {
                  var10000 = var704;
                  var10001 = false;
                  break label5998;
               }

               while(true) {
                  var725 = var721;

                  try {
                     if (!var721.moveToNext()) {
                        break label5997;
                     }
                  } catch (Throwable var702) {
                     var10000 = var702;
                     var10001 = false;
                     break;
                  }

                  int var14;
                  if (var8 != -1) {
                     var725 = var721;

                     try {
                        var14 = var721.getInt(var8);
                     } catch (Throwable var701) {
                        var10000 = var701;
                        var10001 = false;
                        break;
                     }
                  } else {
                     var14 = 0;
                  }

                  int var15;
                  if (var11 != -1) {
                     var725 = var721;

                     try {
                        var15 = var721.getInt(var11);
                     } catch (Throwable var700) {
                        var10000 = var700;
                        var10001 = false;
                        break;
                     }
                  } else {
                     var15 = 0;
                  }

                  Uri var726;
                  if (var10 == -1) {
                     var725 = var721;

                     try {
                        var726 = ContentUris.withAppendedId(var5, var721.getLong(var9));
                     } catch (Throwable var699) {
                        var10000 = var699;
                        var10001 = false;
                        break;
                     }
                  } else {
                     var725 = var721;

                     try {
                        var726 = ContentUris.withAppendedId(var6, var721.getLong(var10));
                     } catch (Throwable var698) {
                        var10000 = var698;
                        var10001 = false;
                        break;
                     }
                  }

                  int var16;
                  if (var12 != -1) {
                     var725 = var721;

                     try {
                        var16 = var721.getInt(var12);
                     } catch (Throwable var697) {
                        var10000 = var697;
                        var10001 = false;
                        break;
                     }
                  } else {
                     var16 = 400;
                  }

                  boolean var17;
                  label6004: {
                     if (var13 != -1) {
                        label6003: {
                           var725 = var721;

                           try {
                              if (var721.getInt(var13) != 1) {
                                 break label6003;
                              }
                           } catch (Throwable var703) {
                              var10000 = var703;
                              var10001 = false;
                              break;
                           }

                           var17 = true;
                           break label6004;
                        }
                     }

                     var17 = false;
                  }

                  var725 = var721;

                  FontsContractCompat.FontInfo var728;
                  try {
                     var728 = new FontsContractCompat.FontInfo;
                  } catch (Throwable var696) {
                     var10000 = var696;
                     var10001 = false;
                     break;
                  }

                  var725 = var721;

                  try {
                     var728.<init>(var726, var15, var16, var17, var14);
                  } catch (Throwable var695) {
                     var10000 = var695;
                     var10001 = false;
                     break;
                  }

                  var725 = var721;

                  try {
                     var727.add(var728);
                  } catch (Throwable var694) {
                     var10000 = var694;
                     var10001 = false;
                     break;
                  }
               }
            }

            Throwable var723 = var10000;
            if (var725 != null) {
               var725.close();
            }

            throw var723;
         }

         var724 = var727;
      }

      if (var721 != null) {
         var721.close();
      }

      return (FontsContractCompat.FontInfo[])var724.toArray(new FontsContractCompat.FontInfo[0]);
   }

   private static Typeface getFontInternal(Context var0, FontRequest var1, int var2) {
      FontsContractCompat.FontFamilyResult var4;
      try {
         var4 = fetchFonts(var0, (CancellationSignal)null, var1);
      } catch (NameNotFoundException var3) {
         return null;
      }

      return var4.getStatusCode() == 0 ? TypefaceCompat.createFromFontInfo(var0, (CancellationSignal)null, var4.getFonts(), var2) : null;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static Typeface getFontSync(final Context var0, final FontRequest var1, @Nullable final TextView var2, int var3, int var4, final int var5) {
      StringBuilder var6 = new StringBuilder();
      var6.append(var1.getIdentifier());
      var6.append("-");
      var6.append(var5);
      final String var30 = var6.toString();
      Typeface var7 = (Typeface)sTypefaceCache.get(var30);
      if (var7 != null) {
         return var7;
      } else {
         boolean var29;
         if (var3 == 0) {
            var29 = true;
         } else {
            var29 = false;
         }

         if (var29 && var4 == -1) {
            return getFontInternal(var0, var1, var5);
         } else {
            Callable var24 = new Callable() {
               public Typeface call() throws Exception {
                  Typeface var1x = FontsContractCompat.getFontInternal(var0, var1, var5);
                  if (var1x != null) {
                     FontsContractCompat.sTypefaceCache.put(var30, var1x);
                  }

                  return var1x;
               }
            };
            if (var29) {
               try {
                  Typeface var26 = (Typeface)sBackgroundThread.postAndWait(var24, var4);
                  return var26;
               } catch (InterruptedException var20) {
                  return null;
               }
            } else {
               SelfDestructiveThread.ReplyCallback var28 = new SelfDestructiveThread.ReplyCallback(new WeakReference(var2)) {
                  // $FF: synthetic field
                  final WeakReference val$textViewWeak;

                  {
                     this.val$textViewWeak = var1;
                  }

                  public void onReply(Typeface var1) {
                     if ((TextView)this.val$textViewWeak.get() != null) {
                        var2.setTypeface(var1, var5);
                     }

                  }
               };
               Object var27 = sLock;
               synchronized(var27){}

               Throwable var10000;
               boolean var10001;
               label223: {
                  try {
                     if (sPendingReplies.containsKey(var30)) {
                        ((ArrayList)sPendingReplies.get(var30)).add(var28);
                        return null;
                     }
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label223;
                  }

                  try {
                     ArrayList var31 = new ArrayList();
                     var31.add(var28);
                     sPendingReplies.put(var30, var31);
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label223;
                  }

                  sBackgroundThread.postAndReply(var24, new SelfDestructiveThread.ReplyCallback() {
                     public void onReply(Typeface param1) {
                        // $FF: Couldn't be decompiled
                     }
                  });
                  return null;
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
         }
      }
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   @VisibleForTesting
   public static ProviderInfo getProvider(@NonNull PackageManager var0, @NonNull FontRequest var1, @Nullable Resources var2) throws NameNotFoundException {
      String var3 = var1.getProviderAuthority();
      int var4 = 0;
      ProviderInfo var5 = var0.resolveContentProvider(var3, 0);
      StringBuilder var7;
      if (var5 == null) {
         var7 = new StringBuilder();
         var7.append("No package found for authority: ");
         var7.append(var3);
         throw new NameNotFoundException(var7.toString());
      } else if (!var5.packageName.equals(var1.getProviderPackage())) {
         var7 = new StringBuilder();
         var7.append("Found content provider ");
         var7.append(var3);
         var7.append(", but package was not ");
         var7.append(var1.getProviderPackage());
         throw new NameNotFoundException(var7.toString());
      } else {
         List var6 = convertToByteArrayList(var0.getPackageInfo(var5.packageName, 64).signatures);
         Collections.sort(var6, sByteArrayComparator);

         for(List var8 = getCertificates(var1, var2); var4 < var8.size(); ++var4) {
            ArrayList var9 = new ArrayList((Collection)var8.get(var4));
            Collections.sort(var9, sByteArrayComparator);
            if (equalsByteArrayList(var6, var9)) {
               return var5;
            }
         }

         return null;
      }
   }

   @RequiresApi(19)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static Map prepareFontData(Context var0, FontsContractCompat.FontInfo[] var1, CancellationSignal var2) {
      HashMap var3 = new HashMap();
      int var4 = 0;

      for(int var5 = var1.length; var4 < var5; ++var4) {
         FontsContractCompat.FontInfo var6 = var1[var4];
         if (var6.getResultCode() == 0) {
            Uri var7 = var6.getUri();
            if (!var3.containsKey(var7)) {
               var3.put(var7, TypefaceCompatUtil.mmap(var0, var2, var7));
            }
         }
      }

      return Collections.unmodifiableMap(var3);
   }

   public static void requestFont(@NonNull final Context var0, @NonNull final FontRequest var1, @NonNull final FontsContractCompat.FontRequestCallback var2, @NonNull Handler var3) {
      var3.post(new Runnable(new Handler()) {
         // $FF: synthetic field
         final Handler val$callerThreadHandler;

         {
            this.val$callerThreadHandler = var3;
         }

         public void run() {
            FontsContractCompat.FontFamilyResult var1x;
            try {
               var1x = FontsContractCompat.fetchFonts(var0, (CancellationSignal)null, var1);
            } catch (NameNotFoundException var5) {
               this.val$callerThreadHandler.post(new Runnable() {
                  public void run() {
                     var2.onTypefaceRequestFailed(-1);
                  }
               });
               return;
            }

            if (var1x.getStatusCode() != 0) {
               switch(var1x.getStatusCode()) {
               case 1:
                  this.val$callerThreadHandler.post(new Runnable() {
                     public void run() {
                        var2.onTypefaceRequestFailed(-2);
                     }
                  });
                  return;
               case 2:
                  this.val$callerThreadHandler.post(new Runnable() {
                     public void run() {
                        var2.onTypefaceRequestFailed(-3);
                     }
                  });
                  return;
               default:
                  this.val$callerThreadHandler.post(new Runnable() {
                     public void run() {
                        var2.onTypefaceRequestFailed(-3);
                     }
                  });
               }
            } else {
               FontsContractCompat.FontInfo[] var6 = var1x.getFonts();
               if (var6 != null && var6.length != 0) {
                  int var2x = var6.length;

                  for(final int var3 = 0; var3 < var2x; ++var3) {
                     FontsContractCompat.FontInfo var4 = var6[var3];
                     if (var4.getResultCode() != 0) {
                        var3 = var4.getResultCode();
                        if (var3 < 0) {
                           this.val$callerThreadHandler.post(new Runnable() {
                              public void run() {
                                 var2.onTypefaceRequestFailed(-3);
                              }
                           });
                        } else {
                           this.val$callerThreadHandler.post(new Runnable() {
                              public void run() {
                                 var2.onTypefaceRequestFailed(var3);
                              }
                           });
                        }

                        return;
                     }
                  }

                  final Typeface var7 = FontsContractCompat.buildTypeface(var0, (CancellationSignal)null, var6);
                  if (var7 == null) {
                     this.val$callerThreadHandler.post(new Runnable() {
                        public void run() {
                           var2.onTypefaceRequestFailed(-3);
                        }
                     });
                  } else {
                     this.val$callerThreadHandler.post(new Runnable() {
                        public void run() {
                           var2.onTypefaceRetrieved(var7);
                        }
                     });
                  }
               } else {
                  this.val$callerThreadHandler.post(new Runnable() {
                     public void run() {
                        var2.onTypefaceRequestFailed(1);
                     }
                  });
               }
            }
         }
      });
   }

   public static final class Columns implements BaseColumns {
      public static final String FILE_ID = "file_id";
      public static final String ITALIC = "font_italic";
      public static final String RESULT_CODE = "result_code";
      public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
      public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
      public static final int RESULT_CODE_MALFORMED_QUERY = 3;
      public static final int RESULT_CODE_OK = 0;
      public static final String TTC_INDEX = "font_ttc_index";
      public static final String VARIATION_SETTINGS = "font_variation_settings";
      public static final String WEIGHT = "font_weight";
   }

   public static class FontFamilyResult {
      public static final int STATUS_OK = 0;
      public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
      public static final int STATUS_WRONG_CERTIFICATES = 1;
      private final FontsContractCompat.FontInfo[] mFonts;
      private final int mStatusCode;

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public FontFamilyResult(int var1, @Nullable FontsContractCompat.FontInfo[] var2) {
         this.mStatusCode = var1;
         this.mFonts = var2;
      }

      public FontsContractCompat.FontInfo[] getFonts() {
         return this.mFonts;
      }

      public int getStatusCode() {
         return this.mStatusCode;
      }

      @Retention(RetentionPolicy.SOURCE)
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      @interface FontResultStatus {
      }
   }

   public static class FontInfo {
      private final boolean mItalic;
      private final int mResultCode;
      private final int mTtcIndex;
      private final Uri mUri;
      private final int mWeight;

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public FontInfo(@NonNull Uri var1, @IntRange(from = 0L) int var2, @IntRange(from = 1L,to = 1000L) int var3, boolean var4, int var5) {
         this.mUri = (Uri)Preconditions.checkNotNull(var1);
         this.mTtcIndex = var2;
         this.mWeight = var3;
         this.mItalic = var4;
         this.mResultCode = var5;
      }

      public int getResultCode() {
         return this.mResultCode;
      }

      @IntRange(
         from = 0L
      )
      public int getTtcIndex() {
         return this.mTtcIndex;
      }

      @NonNull
      public Uri getUri() {
         return this.mUri;
      }

      @IntRange(
         from = 1L,
         to = 1000L
      )
      public int getWeight() {
         return this.mWeight;
      }

      public boolean isItalic() {
         return this.mItalic;
      }
   }

   public static class FontRequestCallback {
      public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
      public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
      public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
      public static final int FAIL_REASON_MALFORMED_QUERY = 3;
      public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
      public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;

      public void onTypefaceRequestFailed(int var1) {
      }

      public void onTypefaceRetrieved(Typeface var1) {
      }

      @Retention(RetentionPolicy.SOURCE)
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      @interface FontRequestFailReason {
      }
   }
}
