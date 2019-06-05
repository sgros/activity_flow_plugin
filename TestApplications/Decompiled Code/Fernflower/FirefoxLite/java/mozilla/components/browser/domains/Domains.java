package mozilla.components.browser.domains;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.LocaleList;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import kotlin.collections.CollectionsKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

public final class Domains {
   public static final Domains INSTANCE = new Domains();

   private Domains() {
   }

   private final Set getAvailableDomainLists(Context var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      AssetManager var5 = var1.getAssets();

      String[] var6;
      try {
         var6 = var5.list("domains");
      } catch (IOException var4) {
         var6 = new String[0];
      }

      Collection var3 = (Collection)var2;
      Intrinsics.checkExpressionValueIsNotNull(var6, "domains");
      CollectionsKt.addAll(var3, var6);
      return (Set)var2;
   }

   private final Set getCountriesInDefaultLocaleList() {
      final LinkedHashSet var1 = new LinkedHashSet();
      Function1 var2 = (Function1)(new Function1() {
         public final void invoke(String var1x) {
            Intrinsics.checkParameterIsNotNull(var1x, "c");
            if (!TextUtils.isEmpty((CharSequence)var1x)) {
               LinkedHashSet var2 = var1;
               Locale var3 = Locale.US;
               Intrinsics.checkExpressionValueIsNotNull(var3, "Locale.US");
               var1x = var1x.toLowerCase(var3);
               Intrinsics.checkExpressionValueIsNotNull(var1x, "(this as java.lang.String).toLowerCase(locale)");
               var2.add(var1x);
            }

         }
      });
      if (VERSION.SDK_INT >= 24) {
         LocaleList var3 = LocaleList.getDefault();
         int var4 = 0;

         for(int var5 = var3.size(); var4 < var5; ++var4) {
            Locale var6 = var3.get(var4);
            Intrinsics.checkExpressionValueIsNotNull(var6, "list.get(i)");
            String var9 = var6.getCountry();
            Intrinsics.checkExpressionValueIsNotNull(var9, "list.get(i).country");
            var2.invoke(var9);
         }
      } else {
         Locale var7 = Locale.getDefault();
         Intrinsics.checkExpressionValueIsNotNull(var7, "Locale.getDefault()");
         String var8 = var7.getCountry();
         Intrinsics.checkExpressionValueIsNotNull(var8, "Locale.getDefault().country");
         var2.invoke(var8);
      }

      return (Set)var1;
   }

   private final void loadDomainsForLanguage(Context var1, Set var2, String var3) {
      AssetManager var8 = var1.getAssets();

      List var12;
      label34: {
         label38: {
            boolean var10001;
            BufferedReader var11;
            label31: {
               Reader var10;
               try {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("domains/");
                  var4.append(var3);
                  InputStream var9 = var8.open(var4.toString());
                  Intrinsics.checkExpressionValueIsNotNull(var9, "assetManager.open(\"domains/\" + country)");
                  Charset var13 = Charsets.UTF_8;
                  InputStreamReader var14 = new InputStreamReader(var9, var13);
                  var10 = (Reader)var14;
                  if (var10 instanceof BufferedReader) {
                     var11 = (BufferedReader)var10;
                     break label31;
                  }
               } catch (IOException var7) {
                  var10001 = false;
                  break label38;
               }

               try {
                  var11 = new BufferedReader(var10, 8192);
               } catch (IOException var6) {
                  var10001 = false;
                  break label38;
               }
            }

            try {
               var12 = TextStreamsKt.readLines((Reader)var11);
               break label34;
            } catch (IOException var5) {
               var10001 = false;
            }
         }

         var12 = CollectionsKt.emptyList();
      }

      var2.addAll((Collection)var12);
   }

   public final List load(Context var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      return this.load$domains_release(var1, this.getCountriesInDefaultLocaleList());
   }

   public final List load$domains_release(Context var1, Set var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "countries");
      LinkedHashSet var3 = new LinkedHashSet();
      Set var4 = this.getAvailableDomainLists(var1);
      Iterable var5 = (Iterable)var2;
      Collection var7 = (Collection)(new ArrayList());
      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         Object var10 = var6.next();
         if (var4.contains((String)var10)) {
            var7.add(var10);
         }
      }

      Iterator var9 = ((Iterable)((List)var7)).iterator();

      while(var9.hasNext()) {
         String var8 = (String)var9.next();
         INSTANCE.loadDomainsForLanguage(var1, (Set)var3, var8);
      }

      this.loadDomainsForLanguage(var1, (Set)var3, "global");
      return CollectionsKt.toList((Iterable)var3);
   }
}
