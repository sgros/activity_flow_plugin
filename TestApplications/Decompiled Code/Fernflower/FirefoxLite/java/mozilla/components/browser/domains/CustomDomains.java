package mozilla.components.browser.domains;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public final class CustomDomains {
   public static final CustomDomains INSTANCE = new CustomDomains();

   private CustomDomains() {
   }

   private final SharedPreferences preferences(Context var1) {
      SharedPreferences var2 = var1.getSharedPreferences("custom_autocomplete", 0);
      Intrinsics.checkExpressionValueIsNotNull(var2, "context.getSharedPrefere…ME, Context.MODE_PRIVATE)");
      return var2;
   }

   public final List load(Context var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      String var5 = this.preferences(var1).getString("custom_domains", "");
      Intrinsics.checkExpressionValueIsNotNull(var5, "preferences(context).getString(KEY_DOMAINS, \"\")");
      Iterable var2 = (Iterable)StringsKt.split$default((CharSequence)var5, new String[]{"@<;>@"}, false, 0, 6, (Object)null);
      Collection var6 = (Collection)(new ArrayList());
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var7 = var3.next();
         boolean var4;
         if (((CharSequence)((String)var7)).length() == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         if (var4 ^ true) {
            var6.add(var7);
         }
      }

      return (List)var6;
   }
}
