package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.util.Base64;
import android.util.TypedValue;
import android.util.Xml;
import androidx.core.R$styleable;
import androidx.core.provider.FontRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FontResourcesParserCompat {
   private static int getType(TypedArray var0, int var1) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getType(var1);
      } else {
         TypedValue var2 = new TypedValue();
         var0.getValue(var1, var2);
         return var2.type;
      }
   }

   public static FontResourcesParserCompat.FamilyResourceEntry parse(XmlPullParser var0, Resources var1) throws XmlPullParserException, IOException {
      int var2;
      do {
         var2 = var0.next();
      } while(var2 != 2 && var2 != 1);

      if (var2 == 2) {
         return readFamilies(var0, var1);
      } else {
         throw new XmlPullParserException("No start tag found");
      }
   }

   public static List readCerts(Resources var0, int var1) {
      if (var1 == 0) {
         return Collections.emptyList();
      } else {
         TypedArray var2 = var0.obtainTypedArray(var1);

         List var36;
         label357: {
            Throwable var10000;
            label363: {
               boolean var10001;
               try {
                  if (var2.length() == 0) {
                     var36 = Collections.emptyList();
                     break label357;
                  }
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label363;
               }

               ArrayList var3;
               label364: {
                  label347: {
                     try {
                        var3 = new ArrayList();
                        if (getType(var2, 0) == 1) {
                           break label347;
                        }
                     } catch (Throwable var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label363;
                     }

                     try {
                        var3.add(toByteArrayList(var0.getStringArray(var1)));
                        break label364;
                     } catch (Throwable var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label363;
                     }
                  }

                  var1 = 0;

                  while(true) {
                     int var4;
                     try {
                        if (var1 >= var2.length()) {
                           break;
                        }

                        var4 = var2.getResourceId(var1, 0);
                     } catch (Throwable var32) {
                        var10000 = var32;
                        var10001 = false;
                        break label363;
                     }

                     if (var4 != 0) {
                        try {
                           var3.add(toByteArrayList(var0.getStringArray(var4)));
                        } catch (Throwable var31) {
                           var10000 = var31;
                           var10001 = false;
                           break label363;
                        }
                     }

                     ++var1;
                  }
               }

               var2.recycle();
               return var3;
            }

            Throwable var35 = var10000;
            var2.recycle();
            throw var35;
         }

         var2.recycle();
         return var36;
      }
   }

   private static FontResourcesParserCompat.FamilyResourceEntry readFamilies(XmlPullParser var0, Resources var1) throws XmlPullParserException, IOException {
      var0.require(2, (String)null, "font-family");
      if (var0.getName().equals("font-family")) {
         return readFamily(var0, var1);
      } else {
         skip(var0);
         return null;
      }
   }

   private static FontResourcesParserCompat.FamilyResourceEntry readFamily(XmlPullParser var0, Resources var1) throws XmlPullParserException, IOException {
      TypedArray var2 = var1.obtainAttributes(Xml.asAttributeSet(var0), R$styleable.FontFamily);
      String var3 = var2.getString(R$styleable.FontFamily_fontProviderAuthority);
      String var4 = var2.getString(R$styleable.FontFamily_fontProviderPackage);
      String var5 = var2.getString(R$styleable.FontFamily_fontProviderQuery);
      int var6 = var2.getResourceId(R$styleable.FontFamily_fontProviderCerts, 0);
      int var7 = var2.getInteger(R$styleable.FontFamily_fontProviderFetchStrategy, 1);
      int var8 = var2.getInteger(R$styleable.FontFamily_fontProviderFetchTimeout, 500);
      var2.recycle();
      if (var3 != null && var4 != null && var5 != null) {
         while(var0.next() != 3) {
            skip(var0);
         }

         return new FontResourcesParserCompat.ProviderResourceEntry(new FontRequest(var3, var4, var5, readCerts(var1, var6)), var7, var8);
      } else {
         ArrayList var9 = new ArrayList();

         while(var0.next() != 3) {
            if (var0.getEventType() == 2) {
               if (var0.getName().equals("font")) {
                  var9.add(readFont(var0, var1));
               } else {
                  skip(var0);
               }
            }
         }

         if (var9.isEmpty()) {
            return null;
         } else {
            return new FontResourcesParserCompat.FontFamilyFilesResourceEntry((FontResourcesParserCompat.FontFileResourceEntry[])var9.toArray(new FontResourcesParserCompat.FontFileResourceEntry[var9.size()]));
         }
      }
   }

   private static FontResourcesParserCompat.FontFileResourceEntry readFont(XmlPullParser var0, Resources var1) throws XmlPullParserException, IOException {
      TypedArray var2 = var1.obtainAttributes(Xml.asAttributeSet(var0), R$styleable.FontFamilyFont);
      int var3;
      if (var2.hasValue(R$styleable.FontFamilyFont_fontWeight)) {
         var3 = R$styleable.FontFamilyFont_fontWeight;
      } else {
         var3 = R$styleable.FontFamilyFont_android_fontWeight;
      }

      int var4 = var2.getInt(var3, 400);
      if (var2.hasValue(R$styleable.FontFamilyFont_fontStyle)) {
         var3 = R$styleable.FontFamilyFont_fontStyle;
      } else {
         var3 = R$styleable.FontFamilyFont_android_fontStyle;
      }

      boolean var5;
      if (1 == var2.getInt(var3, 0)) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var2.hasValue(R$styleable.FontFamilyFont_ttcIndex)) {
         var3 = R$styleable.FontFamilyFont_ttcIndex;
      } else {
         var3 = R$styleable.FontFamilyFont_android_ttcIndex;
      }

      int var6;
      if (var2.hasValue(R$styleable.FontFamilyFont_fontVariationSettings)) {
         var6 = R$styleable.FontFamilyFont_fontVariationSettings;
      } else {
         var6 = R$styleable.FontFamilyFont_android_fontVariationSettings;
      }

      String var9 = var2.getString(var6);
      var6 = var2.getInt(var3, 0);
      if (var2.hasValue(R$styleable.FontFamilyFont_font)) {
         var3 = R$styleable.FontFamilyFont_font;
      } else {
         var3 = R$styleable.FontFamilyFont_android_font;
      }

      int var7 = var2.getResourceId(var3, 0);
      String var8 = var2.getString(var3);
      var2.recycle();

      while(var0.next() != 3) {
         skip(var0);
      }

      return new FontResourcesParserCompat.FontFileResourceEntry(var8, var4, var5, var9, var6, var7);
   }

   private static void skip(XmlPullParser var0) throws XmlPullParserException, IOException {
      int var1 = 1;

      while(var1 > 0) {
         int var2 = var0.next();
         if (var2 != 2) {
            if (var2 == 3) {
               --var1;
            }
         } else {
            ++var1;
         }
      }

   }

   private static List toByteArrayList(String[] var0) {
      ArrayList var1 = new ArrayList();
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.add(Base64.decode(var0[var3], 0));
      }

      return var1;
   }

   public interface FamilyResourceEntry {
   }

   public static final class FontFamilyFilesResourceEntry implements FontResourcesParserCompat.FamilyResourceEntry {
      private final FontResourcesParserCompat.FontFileResourceEntry[] mEntries;

      public FontFamilyFilesResourceEntry(FontResourcesParserCompat.FontFileResourceEntry[] var1) {
         this.mEntries = var1;
      }

      public FontResourcesParserCompat.FontFileResourceEntry[] getEntries() {
         return this.mEntries;
      }
   }

   public static final class FontFileResourceEntry {
      private final String mFileName;
      private boolean mItalic;
      private int mResourceId;
      private int mTtcIndex;
      private String mVariationSettings;
      private int mWeight;

      public FontFileResourceEntry(String var1, int var2, boolean var3, String var4, int var5, int var6) {
         this.mFileName = var1;
         this.mWeight = var2;
         this.mItalic = var3;
         this.mVariationSettings = var4;
         this.mTtcIndex = var5;
         this.mResourceId = var6;
      }

      public String getFileName() {
         return this.mFileName;
      }

      public int getResourceId() {
         return this.mResourceId;
      }

      public int getTtcIndex() {
         return this.mTtcIndex;
      }

      public String getVariationSettings() {
         return this.mVariationSettings;
      }

      public int getWeight() {
         return this.mWeight;
      }

      public boolean isItalic() {
         return this.mItalic;
      }
   }

   public static final class ProviderResourceEntry implements FontResourcesParserCompat.FamilyResourceEntry {
      private final FontRequest mRequest;
      private final int mStrategy;
      private final int mTimeoutMs;

      public ProviderResourceEntry(FontRequest var1, int var2, int var3) {
         this.mRequest = var1;
         this.mStrategy = var2;
         this.mTimeoutMs = var3;
      }

      public int getFetchStrategy() {
         return this.mStrategy;
      }

      public FontRequest getRequest() {
         return this.mRequest;
      }

      public int getTimeout() {
         return this.mTimeoutMs;
      }
   }
}
