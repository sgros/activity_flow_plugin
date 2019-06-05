package org.mozilla.lite.newspoint;

import android.content.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.lite.partner.Repository;
import org.mozilla.lite.partner.Repository.Parser;

public class RepositoryNewsPoint extends Repository<NewsPointItem> {
    static Parser<NewsPointItem> PARSER = C0749-$$Lambda$RepositoryNewsPoint$ZvYynr31y46fQc7qiAJk6VMEhFM.INSTANCE;

    static /* synthetic */ List lambda$static$0(String str) throws JSONException {
        JSONArray jSONArray;
        ParseException e;
        ArrayList arrayList = new ArrayList();
        JSONArray jSONArray2 = new JSONObject(str).getJSONArray("items");
        int i = 0;
        int i2 = 0;
        while (i2 < jSONArray2.length()) {
            String str2;
            JSONObject jSONObject = jSONArray2.getJSONObject(i2);
            String safeGetString = safeGetString(jSONObject, "id");
            String safeGetString2 = safeGetString(jSONObject, "hl");
            String safeGetString3 = safeGetString(jSONObject, "imageid");
            JSONArray safeGetArray = safeGetArray(jSONObject, "images");
            if (safeGetArray == null) {
                str2 = null;
            } else {
                str2 = safeGetArray.getString(i);
            }
            String str3 = str2;
            String safeGetString4 = safeGetString(jSONObject, "pn");
            str2 = safeGetString(jSONObject, "dl");
            String safeGetString5 = safeGetString(jSONObject, "dm");
            long safeGetLong = safeGetLong(jSONObject, "pid");
            long safeGetLong2 = safeGetLong(jSONObject, "lid");
            String safeGetString6 = safeGetString(jSONObject, "lang");
            String safeGetString7 = safeGetString(jSONObject, "tn");
            String safeGetString8 = safeGetString(jSONObject, "wu");
            String safeGetString9 = safeGetString(jSONObject, "pnu");
            String safeGetString10 = safeGetString(jSONObject, "fu");
            String safeGetString11 = safeGetString(jSONObject, "sec");
            String safeGetString12 = safeGetString(jSONObject, "mwu");
            String safeGetString13 = safeGetString(jSONObject, "m");
            String str4 = "\u0000";
            List asList = Arrays.asList(jSONObject.getJSONArray("tags").join(str4).split(str4));
            if (safeGetString == null || safeGetString2 == null || safeGetString12 == null || str2 == null) {
                jSONArray = jSONArray2;
            } else {
                try {
                    jSONArray = jSONArray2;
                    try {
                        arrayList.add(new NewsPointItem(safeGetString, str3, safeGetString2, safeGetString12, new SimpleDateFormat("EEE MMM dd HH:mm:ss 'IST' yyyy", Locale.US).parse(str2).getTime(), safeGetString3, safeGetString4, safeGetString5, Long.valueOf(safeGetLong), Long.valueOf(safeGetLong2), safeGetString6, safeGetString7, safeGetString8, safeGetString9, safeGetString10, safeGetString11, safeGetString13, asList));
                    } catch (ParseException e2) {
                        e = e2;
                        e.printStackTrace();
                        i2++;
                        jSONArray2 = jSONArray;
                        i = 0;
                    }
                } catch (ParseException e3) {
                    e = e3;
                    jSONArray = jSONArray2;
                    e.printStackTrace();
                    i2++;
                    jSONArray2 = jSONArray;
                    i = 0;
                }
            }
            i2++;
            jSONArray2 = jSONArray;
            i = 0;
        }
        return arrayList;
    }

    private static JSONArray safeGetArray(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getJSONArray(str);
        } catch (JSONException unused) {
            return null;
        }
    }

    private static String safeGetString(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getString(str);
        } catch (JSONException unused) {
            return null;
        }
    }

    private static long safeGetLong(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getLong(str);
        } catch (JSONException unused) {
            return -1;
        }
    }

    public RepositoryNewsPoint(Context context, String str) {
        super(context, null, 3, null, null, "newspoint", str, 1, PARSER, true);
    }

    /* Access modifiers changed, original: protected */
    public String getSubscriptionUrl(int i) {
        return String.format(Locale.US, this.subscriptionUrl, new Object[]{Integer.valueOf(i), Integer.valueOf(30)});
    }
}
