package android.support.v4.text.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
         byte var3 = -1;
         if (var1.start >= var2.start) {
            if (var1.start > var2.start) {
               var3 = 1;
            } else if (var1.end < var2.end) {
               var3 = 1;
            } else if (var1.end <= var2.end) {
               var3 = 0;
            }
         }

         return var3;
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
      addLinks((TextView)var0, var1, var2, (String[])null, (MatchFilter)null, (TransformFilter)null);
   }

   public static final void addLinks(@NonNull TextView var0, @NonNull Pattern var1, @Nullable String var2, @Nullable MatchFilter var3, @Nullable TransformFilter var4) {
      addLinks((TextView)var0, var1, var2, (String[])null, var3, var4);
   }

   public static final void addLinks(@NonNull TextView var0, @NonNull Pattern var1, @Nullable String var2, @Nullable String[] var3, @Nullable MatchFilter var4, @Nullable TransformFilter var5) {
      SpannableString var6 = SpannableString.valueOf(var0.getText());
      if (addLinks((Spannable)var6, var1, var2, var3, var4, var5)) {
         var0.setText(var6);
         addLinkMovementMethod(var0);
      }

   }

   public static final boolean addLinks(@NonNull Spannable var0, int var1) {
      boolean var2;
      if (var1 == 0) {
         var2 = false;
      } else {
         URLSpan[] var3 = (URLSpan[])var0.getSpans(0, var0.length(), URLSpan.class);

         for(int var4 = var3.length - 1; var4 >= 0; --var4) {
            var0.removeSpan(var3[var4]);
         }

         if ((var1 & 4) != 0) {
            Linkify.addLinks(var0, 4);
         }

         ArrayList var5 = new ArrayList();
         if ((var1 & 1) != 0) {
            Pattern var7 = PatternsCompat.AUTOLINK_WEB_URL;
            MatchFilter var6 = Linkify.sUrlMatchFilter;
            gatherLinks(var5, var0, var7, new String[]{"http://", "https://", "rtsp://"}, var6, (TransformFilter)null);
         }

         if ((var1 & 2) != 0) {
            gatherLinks(var5, var0, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[]{"mailto:"}, (MatchFilter)null, (TransformFilter)null);
         }

         if ((var1 & 8) != 0) {
            gatherMapLinks(var5, var0);
         }

         pruneOverlaps(var5, var0);
         if (var5.size() == 0) {
            var2 = false;
         } else {
            Iterator var9 = var5.iterator();

            while(var9.hasNext()) {
               LinkifyCompat.LinkSpec var8 = (LinkifyCompat.LinkSpec)var9.next();
               if (var8.frameworkAddedSpan == null) {
                  applyLink(var8.url, var8.start, var8.end, var0);
               }
            }

            var2 = true;
         }
      }

      return var2;
   }

   public static final boolean addLinks(@NonNull Spannable var0, @NonNull Pattern var1, @Nullable String var2) {
      return addLinks((Spannable)var0, var1, var2, (String[])null, (MatchFilter)null, (TransformFilter)null);
   }

   public static final boolean addLinks(@NonNull Spannable var0, @NonNull Pattern var1, @Nullable String var2, @Nullable MatchFilter var3, @Nullable TransformFilter var4) {
      return addLinks((Spannable)var0, var1, var2, (String[])null, var3, var4);
   }

   public static final boolean addLinks(@NonNull Spannable var0, @NonNull Pattern var1, @Nullable String var2, @Nullable String[] var3, @Nullable MatchFilter var4, @Nullable TransformFilter var5) {
      String var6 = var2;
      if (var2 == null) {
         var6 = "";
      }

      String[] var13;
      label39: {
         if (var3 != null) {
            var13 = var3;
            if (var3.length >= 1) {
               break label39;
            }
         }

         var13 = EMPTY_STRING;
      }

      String[] var7 = new String[var13.length + 1];
      var7[0] = var6.toLowerCase(Locale.ROOT);

      int var8;
      for(var8 = 0; var8 < var13.length; ++var8) {
         String var14 = var13[var8];
         if (var14 == null) {
            var14 = "";
         } else {
            var14 = var14.toLowerCase(Locale.ROOT);
         }

         var7[var8 + 1] = var14;
      }

      boolean var9 = false;
      Matcher var12 = var1.matcher(var0);

      while(var12.find()) {
         int var10 = var12.start();
         var8 = var12.end();
         boolean var11 = true;
         if (var4 != null) {
            var11 = var4.acceptMatch(var0, var10, var8);
         }

         if (var11) {
            applyLink(makeUrl(var12.group(0), var7, var12, var5), var10, var8, var0);
            var9 = true;
         }
      }

      return var9;
   }

   public static final boolean addLinks(@NonNull TextView var0, int var1) {
      boolean var2 = false;
      if (var1 != 0) {
         CharSequence var3 = var0.getText();
         if (var3 instanceof Spannable) {
            if (addLinks((Spannable)var3, var1)) {
               addLinkMovementMethod(var0);
               var2 = true;
            }
         } else {
            SpannableString var4 = SpannableString.valueOf(var3);
            if (addLinks((Spannable)var4, var1)) {
               addLinkMovementMethod(var0);
               var0.setText(var4);
               var2 = true;
            }
         }
      }

      return var2;
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

      boolean var5 = false;
      int var6 = 0;

      boolean var7;
      while(true) {
         var7 = var5;
         var0 = var4;
         if (var6 >= var1.length) {
            break;
         }

         if (var4.regionMatches(true, 0, var1[var6], 0, var1[var6].length())) {
            var5 = true;
            var7 = var5;
            var0 = var4;
            if (!var4.regionMatches(false, 0, var1[var6], 0, var1[var6].length())) {
               var0 = var1[var6] + var4.substring(var1[var6].length());
               var7 = var5;
            }
            break;
         }

         ++var6;
      }

      String var8 = var0;
      if (!var7) {
         var8 = var0;
         if (var1.length > 0) {
            var8 = var1[0] + var0;
         }
      }

      return var8;
   }

   private static final void pruneOverlaps(ArrayList var0, Spannable var1) {
      URLSpan[] var2 = (URLSpan[])var1.getSpans(0, var1.length(), URLSpan.class);

      int var3;
      LinkifyCompat.LinkSpec var4;
      for(var3 = 0; var3 < var2.length; ++var3) {
         var4 = new LinkifyCompat.LinkSpec();
         var4.frameworkAddedSpan = var2[var3];
         var4.start = var1.getSpanStart(var2[var3]);
         var4.end = var1.getSpanEnd(var2[var3]);
         var0.add(var4);
      }

      Collections.sort(var0, COMPARATOR);
      int var5 = var0.size();
      int var6 = 0;

      while(true) {
         while(var6 < var5 - 1) {
            var4 = (LinkifyCompat.LinkSpec)var0.get(var6);
            LinkifyCompat.LinkSpec var7 = (LinkifyCompat.LinkSpec)var0.get(var6 + 1);
            var3 = -1;
            if (var4.start <= var7.start && var4.end > var7.start) {
               if (var7.end <= var4.end) {
                  var3 = var6 + 1;
               } else if (var4.end - var4.start > var7.end - var7.start) {
                  var3 = var6 + 1;
               } else if (var4.end - var4.start < var7.end - var7.start) {
                  var3 = var6;
               }

               if (var3 != -1) {
                  URLSpan var8 = ((LinkifyCompat.LinkSpec)var0.get(var3)).frameworkAddedSpan;
                  if (var8 != null) {
                     var1.removeSpan(var8);
                  }

                  var0.remove(var3);
                  --var5;
                  continue;
               }
            }

            ++var6;
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
   public @interface LinkifyMask {
   }
}
