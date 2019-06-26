package com.stripe.android.util;

import com.stripe.android.model.Card;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class StripeNetworkUtils {
   public static Map hashMapFromCard(Card var0) {
      HashMap var1 = new HashMap();
      HashMap var2 = new HashMap();
      var2.put("number", StripeTextUtils.nullIfBlank(var0.getNumber()));
      var2.put("cvc", StripeTextUtils.nullIfBlank(var0.getCVC()));
      var2.put("exp_month", var0.getExpMonth());
      var2.put("exp_year", var0.getExpYear());
      var2.put("name", StripeTextUtils.nullIfBlank(var0.getName()));
      var2.put("currency", StripeTextUtils.nullIfBlank(var0.getCurrency()));
      var2.put("address_line1", StripeTextUtils.nullIfBlank(var0.getAddressLine1()));
      var2.put("address_line2", StripeTextUtils.nullIfBlank(var0.getAddressLine2()));
      var2.put("address_city", StripeTextUtils.nullIfBlank(var0.getAddressCity()));
      var2.put("address_zip", StripeTextUtils.nullIfBlank(var0.getAddressZip()));
      var2.put("address_state", StripeTextUtils.nullIfBlank(var0.getAddressState()));
      var2.put("address_country", StripeTextUtils.nullIfBlank(var0.getAddressCountry()));
      Iterator var3 = (new HashSet(var2.keySet())).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (var2.get(var4) == null) {
            var2.remove(var4);
         }
      }

      var1.put("card", var2);
      return var1;
   }
}
