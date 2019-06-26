// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.util;

import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import com.stripe.android.model.Card;

public class StripeNetworkUtils
{
    public static Map<String, Object> hashMapFromCard(final Card card) {
        final HashMap<String, HashMap<Object, Object>> hashMap = (HashMap<String, HashMap<Object, Object>>)new HashMap<String, Object>();
        final HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("number", StripeTextUtils.nullIfBlank(card.getNumber()));
        hashMap2.put("cvc", (Integer)StripeTextUtils.nullIfBlank(card.getCVC()));
        hashMap2.put("exp_month", card.getExpMonth());
        hashMap2.put("exp_year", card.getExpYear());
        hashMap2.put("name", (Integer)StripeTextUtils.nullIfBlank(card.getName()));
        hashMap2.put("currency", StripeTextUtils.nullIfBlank(card.getCurrency()));
        hashMap2.put("address_line1", StripeTextUtils.nullIfBlank(card.getAddressLine1()));
        hashMap2.put("address_line2", StripeTextUtils.nullIfBlank(card.getAddressLine2()));
        hashMap2.put("address_city", StripeTextUtils.nullIfBlank(card.getAddressCity()));
        hashMap2.put("address_zip", StripeTextUtils.nullIfBlank(card.getAddressZip()));
        hashMap2.put("address_state", StripeTextUtils.nullIfBlank(card.getAddressState()));
        hashMap2.put("address_country", StripeTextUtils.nullIfBlank(card.getAddressCountry()));
        for (final String s : new HashSet<String>((Collection<? extends String>)hashMap2.keySet())) {
            if (hashMap2.get(s) == null) {
                hashMap2.remove(s);
            }
        }
        hashMap.put("card", (HashMap<Object, Object>)hashMap2);
        return (Map<String, Object>)hashMap;
    }
}
