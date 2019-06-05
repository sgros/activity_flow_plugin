package android.support.v4.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.compat.R;
import android.support.v4.provider.FontRequest;
import android.util.Base64;
import android.util.Xml;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class FontResourcesParserCompat {
   private static final int DEFAULT_TIMEOUT_MILLIS = 500;
   public static final int FETCH_STRATEGY_ASYNC = 1;
   public static final int FETCH_STRATEGY_BLOCKING = 0;
   public static final int INFINITE_TIMEOUT_VALUE = -1;
   private static final int ITALIC = 1;
   private static final int NORMAL_WEIGHT = 400;

   @Nullable
   public static FontResourcesParserCompat.FamilyResourceEntry parse(XmlPullParser var0, Resources var1) throws XmlPullParserException, IOException {
      int var2;
      do {
         var2 = var0.next();
      } while(var2 != 2 && var2 != 1);

      if (var2 != 2) {
         throw new XmlPullParserException("No start tag found");
      } else {
         return readFamilies(var0, var1);
      }
   }

   public static List readCerts(Resources var0, @ArrayRes int var1) {
      Object var2 = null;
      ArrayList var3 = null;
      if (var1 != 0) {
         TypedArray var4 = var0.obtainTypedArray(var1);
         var2 = var3;
         if (var4.length() > 0) {
            var3 = new ArrayList();
            boolean var5;
            if (var4.getResourceId(0, 0) != 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            if (var5) {
               var1 = 0;

               while(true) {
                  var2 = var3;
                  if (var1 >= var4.length()) {
                     break;
                  }

                  var3.add(toByteArrayList(var0.getStringArray(var4.getResourceId(var1, 0))));
                  ++var1;
               }
            } else {
               var3.add(toByteArrayList(var0.getStringArray(var1)));
               var2 = var3;
            }
         }

         var4.recycle();
      }

      if (var2 == null) {
         var2 = Collections.emptyList();
      }

      return (List)var2;
   }

   @Nullable
   private static FontResourcesParserCompat.FamilyResourceEntry readFamilies(XmlPullParser var0, Resources var1) throws XmlPullParserException, IOException {
      var0.require(2, (String)null, "font-family");
      if (var0.getName().equals("font-family")) {
         return readFamily(var0, var1);
      } else {
         skip(var0);
         return null;
      }
   }

   @Nullable
   private static FontResourcesParserCompat.FamilyResourceEntry readFamily(XmlPullParser var0, Resources var1) throws XmlPullParserException, IOException {
      TypedArray var2 = var1.obtainAttributes(Xml.asAttributeSet(var0), R.styleable.FontFamily);
      String var3 = var2.getString(R.styleable.FontFamily_fontProviderAuthority);
      String var4 = var2.getString(R.styleable.FontFamily_fontProviderPackage);
      String var5 = var2.getString(R.styleable.FontFamily_fontProviderQuery);
      int var6 = var2.getResourceId(R.styleable.FontFamily_fontProviderCerts, 0);
      int var7 = var2.getInteger(R.styleable.FontFamily_fontProviderFetchStrategy, 1);
      int var8 = var2.getInteger(R.styleable.FontFamily_fontProviderFetchTimeout, 500);
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
      TypedArray var6 = var1.obtainAttributes(Xml.asAttributeSet(var0), R.styleable.FontFamilyFont);
      int var2 = var6.getInt(R.styleable.FontFamilyFont_fontWeight, 400);
      int var3 = var6.getInt(R.styleable.FontFamilyFont_fontStyle, 0);
      boolean var4 = true;
      if (1 != var3) {
         var4 = false;
      }

      var3 = var6.getResourceId(R.styleable.FontFamilyFont_font, 0);
      String var5 = var6.getString(R.styleable.FontFamilyFont_font);
      var6.recycle();

      while(var0.next() != 3) {
         skip(var0);
      }

      return new FontResourcesParserCompat.FontFileResourceEntry(var5, var2, var4, var3);
   }

   private static void skip(XmlPullParser var0) throws XmlPullParserException, IOException {
      int var1 = 1;

      while(var1 > 0) {
         switch(var0.next()) {
         case 2:
            ++var1;
            break;
         case 3:
            --var1;
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

   @Retention(RetentionPolicy.SOURCE)
   public @interface FetchStrategy {
   }

   public static final class FontFamilyFilesResourceEntry implements FontResourcesParserCompat.FamilyResourceEntry {
      @NonNull
      private final FontResourcesParserCompat.FontFileResourceEntry[] mEntries;

      public FontFamilyFilesResourceEntry(@NonNull FontResourcesParserCompat.FontFileResourceEntry[] var1) {
         this.mEntries = var1;
      }

      @NonNull
      public FontResourcesParserCompat.FontFileResourceEntry[] getEntries() {
         return this.mEntries;
      }
   }

   public static final class FontFileResourceEntry {
      @NonNull
      private final String mFileName;
      private boolean mItalic;
      private int mResourceId;
      private int mWeight;

      public FontFileResourceEntry(@NonNull String var1, int var2, boolean var3, int var4) {
         this.mFileName = var1;
         this.mWeight = var2;
         this.mItalic = var3;
         this.mResourceId = var4;
      }

      @NonNull
      public String getFileName() {
         return this.mFileName;
      }

      public int getResourceId() {
         return this.mResourceId;
      }

      public int getWeight() {
         return this.mWeight;
      }

      public boolean isItalic() {
         return this.mItalic;
      }
   }

   public static final class ProviderResourceEntry implements FontResourcesParserCompat.FamilyResourceEntry {
      @NonNull
      private final FontRequest mRequest;
      private final int mStrategy;
      private final int mTimeoutMs;

      public ProviderResourceEntry(@NonNull FontRequest var1, int var2, int var3) {
         this.mRequest = var1;
         this.mStrategy = var2;
         this.mTimeoutMs = var3;
      }

      public int getFetchStrategy() {
         return this.mStrategy;
      }

      @NonNull
      public FontRequest getRequest() {
         return this.mRequest;
      }

      public int getTimeout() {
         return this.mTimeoutMs;
      }
   }
}
