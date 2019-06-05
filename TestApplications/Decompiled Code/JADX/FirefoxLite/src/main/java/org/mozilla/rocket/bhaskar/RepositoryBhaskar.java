package org.mozilla.rocket.bhaskar;

import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.lite.partner.Repository;
import org.mozilla.lite.partner.Repository.Parser;

public class RepositoryBhaskar extends Repository<BhaskarItem> {
    static Parser<BhaskarItem> PARSER = C0750-$$Lambda$RepositoryBhaskar$6W7SyoJU81_IckXVButtHQnV0FA.INSTANCE;

    static /* synthetic */ List lambda$static$0(String str) throws JSONException {
        ArrayList arrayList = new ArrayList();
        JSONArray jSONArray = new JSONObject(str).getJSONObject("data").getJSONArray("rows");
        for (int i = 0; i < jSONArray.length(); i++) {
            String string;
            String optString;
            String optString2;
            String optString3;
            String optString4;
            String optString5;
            long optLong;
            String optString6;
            String optString7;
            List asList;
            String string2;
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            String optString8 = jSONObject.optString("id", null);
            String optString9 = jSONObject.optString("articleFrom", null);
            String optString10 = jSONObject.optString("category", null);
            String optString11 = jSONObject.optString("city", null);
            String optString12 = jSONObject.optString("coverPic", null);
            if (optString12 != null) {
                JSONArray jSONArray2 = new JSONArray(optString12);
                if (jSONArray2.length() > 0) {
                    string = jSONArray2.getString(0);
                    optString = jSONObject.optString("description", null);
                    optString2 = jSONObject.optString("detailUrl", null);
                    optString3 = jSONObject.optString("keywords", null);
                    optString4 = jSONObject.optString("language", null);
                    optString5 = jSONObject.optString("province", null);
                    optLong = jSONObject.optLong("publishTime", -1);
                    optString6 = jSONObject.optString("subcategory", null);
                    optString7 = jSONObject.optString("summary", null);
                    optString12 = "\u0000";
                    asList = Arrays.asList(jSONObject.getJSONArray("tags").join(optString12).split(optString12));
                    string2 = jSONObject.getString("title");
                    if (!(optString8 == null || string2 == null || optString2 == null || optLong == -1)) {
                        arrayList.add(new BhaskarItem(optString8, string, string2, optString2, optLong, optString7, optString4, optString10, optString6, optString3, optString, asList, optString9, optString5, optString11));
                    }
                }
            }
            string = null;
            optString = jSONObject.optString("description", null);
            optString2 = jSONObject.optString("detailUrl", null);
            optString3 = jSONObject.optString("keywords", null);
            optString4 = jSONObject.optString("language", null);
            optString5 = jSONObject.optString("province", null);
            optLong = jSONObject.optLong("publishTime", -1);
            optString6 = jSONObject.optString("subcategory", null);
            optString7 = jSONObject.optString("summary", null);
            optString12 = "\u0000";
            asList = Arrays.asList(jSONObject.getJSONArray("tags").join(optString12).split(optString12));
            string2 = jSONObject.getString("title");
            arrayList.add(new BhaskarItem(optString8, string, string2, optString2, optLong, optString7, optString4, optString10, optString6, optString3, optString, asList, optString9, optString5, optString11));
        }
        return arrayList;
    }

    public RepositoryBhaskar(Context context, String str) {
        super(context, null, 3, null, null, "bhaskar", str, 1, PARSER, true);
    }

    /* Access modifiers changed, original: protected */
    public String getSubscriptionUrl(int i) {
        return String.format(Locale.US, this.subscriptionUrl, new Object[]{Integer.valueOf(30), Integer.valueOf(521), Integer.valueOf(i)});
    }
}
