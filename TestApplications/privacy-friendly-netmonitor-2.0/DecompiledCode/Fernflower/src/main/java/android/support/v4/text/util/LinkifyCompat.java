package android.support.v4.text.util;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.PatternsCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkifyCompat {
   private static final Comparator COMPARATOR = new Comparator() {
      public final int compare(LinkifyCompat.LinkSpec var1, LinkifyCompat.LinkSpec var2) {
         if (var1.start < var2.start) {
            return -1;
         } else if (var1.start > var2.start) {
            return 1;
         } else if (var1.end < var2.end) {
            return 1;
         } else {
            return var1.end > var2.end ? -1 : 0;
         }
      }
   };
   private static final String[] EMPTY_STRING = new String[0];

   private LinkifyCompat() {
   }

   private static void addLinkMovementMethod(@NonNull TextView var0) {
      MovementMethod var1 = var0.getMovementMethod();
      if ((var1 == null || !(var1 instanceof LinkMovementMethod)) && var0.getLinksClickable()) {
         var0.setMovementMethod(LinkMovementMethod.getInstance());
      }

   }

   public static final void addLinks(@NonNull TextView var0, @NonNull Pattern var1, @Nullable String var2) {
      if (VERSION.SDK_INT >= 26) {
         Linkify.addLinks(var0, var1, var2);
      } else {
         addLinks((TextView)var0, var1, var2, (String[])null, (MatchFilter)null, (TransformFilter)null);
      }
   }

   public static final void addLinks(@NonNull TextView var0, @NonNull Pattern var1, @Nullable String var2, @Nullable MatchFilter var3, @Nullable TransformFilter var4) {
      if (VERSION.SDK_INT >= 26) {
         Linkify.addLinks(var0, var1, var2, var3, var4);
      } else {
         addLinks((TextView)var0, var1, var2, (String[])null, var3, var4);
      }
   }

   public static final void addLinks(@NonNull TextView var0, @NonNull Pattern var1, @Nullable String var2, @Nullable String[] var3, @Nullable MatchFilter var4, @Nullable TransformFilter var5) {
      if (VERSION.SDK_INT >= 26) {
         Linkify.addLinks(var0, var1, var2, var3, var4, var5);
      } else {
         SpannableString var6 = SpannableString.valueOf(var0.getText());
         if (addLinks((Spannable)var6, var1, var2, var3, var4, var5)) {
            var0.setText(var6);
            addLinkMovementMethod(var0);
         }

      }
   }

   public static final boolean addLinks(@NonNull Spannable var0, int var1) {
      if (VERSION.SDK_INT >= 26) {
         return Linkify.addLinks(var0, var1);
      } else if (var1 == 0) {
         return false;
      } else {
         URLSpan[] var2 = (URLSpan[])var0.getSpans(0, var0.length(), URLSpan.class);

         for(int var3 = var2.length - 1; var3 >= 0; --var3) {
            var0.removeSpan(var2[var3]);
         }

         if ((var1 & 4) != 0) {
            Linkify.addLinks(var0, 4);
         }

         ArrayList var4 = new ArrayList();
         if ((var1 & 1) != 0) {
            Pattern var5 = PatternsCompat.AUTOLINK_WEB_URL;
            MatchFilter var6 = Linkify.sUrlMatchFilter;
            gatherLinks(var4, var0, var5, new String[]{"http://", "https://", "rtsp://"}, var6, (TransformFilter)null);
         }

         if ((var1 & 2) != 0) {
            gatherLinks(var4, var0, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[]{"mailto:"}, (MatchFilter)null, (TransformFilter)null);
         }

         if ((var1 & 8) != 0) {
            gatherMapLinks(var4, var0);
         }

         pruneOverlaps(var4, var0);
         if (var4.size() == 0) {
            return false;
         } else {
            Iterator var8 = var4.iterator();

            while(var8.hasNext()) {
               LinkifyCompat.LinkSpec var7 = (LinkifyCompat.LinkSpec)var8.next();
               if (var7.frameworkAddedSpan == null) {
                  applyLink(var7.url, var7.start, var7.end, var0);
               }
            }

            return true;
         }
      }
   }

   public static final boolean addLinks(@NonNull Spannable var0, @NonNull Pattern var1, @Nullable String var2) {
      return VERSION.SDK_INT >= 26 ? Linkify.addLinks(var0, var1, var2) : addLinks((Spannable)var0, var1, var2, (String[])null, (MatchFilter)null, (TransformFilter)null);
   }

   public static final boolean addLinks(@NonNull Spannable var0, @NonNull Pattern var1, @Nullable String var2, @Nullable MatchFilter var3, @Nullable TransformFilter var4) {
      return VERSION.SDK_INT >= 26 ? Linkify.addLinks(var0, var1, var2, var3, var4) : addLinks((Spannable)var0, var1, var2, (String[])null, var3, var4);
   }

   public static final boolean addLinks(@NonNull Spannable var0, @NonNull Pattern var1, @Nullable String var2, @Nullable String[] var3, @Nullable MatchFilter var4, @Nullable TransformFilter var5) {
      if (VERSION.SDK_INT >= 26) {
         return Linkify.addLinks(var0, var1, var2, var3, var4, var5);
      } else {
         String var6 = var2;
         if (var2 == null) {
            var6 = "";
         }

         String[] var13;
         label42: {
            if (var3 != null) {
               var13 = var3;
               if (var3.length >= 1) {
                  break label42;
               }
            }

            var13 = EMPTY_STRING;
         }

         String[] var7 = new String[var13.length + 1];
         var7[0] = var6.toLowerCase(Locale.ROOT);

         int var8;
         String var14;
         for(var8 = 0; var8 < var13.length; var7[var8] = var14) {
            var14 = var13[var8];
            ++var8;
            if (var14 == null) {
               var14 = "";
            } else {
               var14 = var14.toLowerCase(Locale.ROOT);
            }
         }

         Matcher var12 = var1.matcher(var0);
         boolean var9 = false;

         while(var12.find()) {
            int var10 = var12.start();
            var8 = var12.end();
            boolean var11;
            if (var4 != null) {
               var11 = var4.acceptMatch(var0, var10, var8);
            } else {
               var11 = true;
            }

            if (var11) {
               applyLink(makeUrl(var12.group(0), var7, var12, var5), var10, var8, var0);
               var9 = true;
            }
         }

         return var9;
      }
   }

   public static final boolean addLinks(@NonNull TextView var0, int var1) {
      if (VERSION.SDK_INT >= 26) {
         return Linkify.addLinks(var0, var1);
      } else if (var1 == 0) {
         return false;
      } else {
         CharSequence var2 = var0.getText();
         if (var2 instanceof Spannable) {
            if (addLinks((Spannable)var2, var1)) {
               addLinkMovementMethod(var0);
               return true;
            } else {
               return false;
            }
         } else {
            SpannableString var3 = SpannableString.valueOf(var2);
            if (addLinks((Spannable)var3, var1)) {
               addLinkMovementMethod(var0);
               var0.setText(var3);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   private static void applyLink(String var0, int var1, int var2, Spannable var3) {
      var3.setSpan(new URLSpan(var0), var1, var2, 33);
   }

   private static void gatherLinks(ArrayList var0, Spannable var1, Pattern var2, String[] var3, MatchFilter var4, TransformFilter var5) {
      Matcher var6 = var2.matcher(var1);

      while(true) {
         int var7;
         int var8;
         do {
            if (!var6.find()) {
               return;
            }

            var7 = var6.start();
            var8 = var6.end();
         } while(var4 != null && !var4.acceptMatch(var1, var7, var8));

         LinkifyCompat.LinkSpec var9 = new LinkifyCompat.LinkSpec();
         var9.url = makeUrl(var6.group(0), var3, var6, var5);
         var9.start = var7;
         var9.end = var8;
         var0.add(var9);
      }
   }

   private static final void gatherMapLinks(ArrayList param0, Spannable param1) {
      // $FF: Couldn't be decompiled
   }

   private static String makeUrl(@NonNull String var0, @NonNull String[] var1, Matcher var2, @Nullable TransformFilter var3) {
      String var4 = var0;
      if (var3 != null) {
         var4 = var3.transformUrl(var2, var0);
      }

      int var5 = 0;

      boolean var7;
      while(true) {
         boolean var6 = true;
         if (var5 >= var1.length) {
            var7 = false;
            var0 = var4;
            break;
         }

         if (var4.regionMatches(true, 0, var1[var5], 0, var1[var5].length())) {
            var7 = var6;
            var0 = var4;
            if (!var4.regionMatches(false, 0, var1[var5], 0, var1[var5].length())) {
               StringBuilder var8 = new StringBuilder();
               var8.append(var1[var5]);
               var8.append(var4.substring(var1[var5].length()));
               var0 = var8.toString();
               var7 = var6;
            }
            break;
         }

         ++var5;
      }

      String var9 = var0;
      if (!var7) {
         var9 = var0;
         if (var1.length > 0) {
            StringBuilder var10 = new StringBuilder();
            var10.append(var1[0]);
            var10.append(var0);
            var9 = var10.toString();
         }
      }

      return var9;
   }

   private static final void pruneOverlaps(ArrayList var0, Spannable var1) {
      int var2 = var1.length();
      byte var3 = 0;
      URLSpan[] var4 = (URLSpan[])var1.getSpans(0, var2, URLSpan.class);

      LinkifyCompat.LinkSpec var5;
      for(var2 = 0; var2 < var4.length; ++var2) {
         var5 = new LinkifyCompat.LinkSpec();
         var5.frameworkAddedSpan = var4[var2];
         var5.start = var1.getSpanStart(var4[var2]);
         var5.end = var1.getSpanEnd(var4[var2]);
         var0.add(var5);
      }

      Collections.sort(var0, COMPARATOR);
      int var6 = var0.size();
      var2 = var3;

      while(true) {
         while(var2 < var6 - 1) {
            LinkifyCompat.LinkSpec var9 = (LinkifyCompat.LinkSpec)var0.get(var2);
            int var7 = var2 + 1;
            var5 = (LinkifyCompat.LinkSpec)var0.get(var7);
            if (var9.start <= var5.start && var9.end > var5.start) {
               int var8;
               if (var5.end > var9.end && var9.end - var9.start <= var5.end - var5.start) {
                  if (var9.end - var9.start < var5.end - var5.start) {
                     var8 = var2;
                  } else {
                     var8 = -1;
                  }
               } else {
                  var8 = var7;
               }

               if (var8 != -1) {
                  URLSpan var10 = ((LinkifyCompat.LinkSpec)var0.get(var8)).frameworkAddedSpan;
                  if (var10 != null) {
                     var1.removeSpan(var10);
                  }

                  var0.remove(var8);
                  --var6;
                  continue;
               }
            }

            var2 = var7;
         }

         return;
      }
   }

   private static class LinkSpec {
      int end;
      URLSpan frameworkAddedSpan;
      int start;
      String url;

      LinkSpec() {
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface LinkifyMask {
   }
}
