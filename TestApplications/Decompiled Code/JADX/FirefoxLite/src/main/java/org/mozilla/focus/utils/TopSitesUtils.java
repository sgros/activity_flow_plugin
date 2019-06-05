package org.mozilla.focus.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import com.adjust.sdk.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.focus.history.model.Site;
import org.mozilla.rocket.C0769R;

public class TopSitesUtils {
    public static JSONArray getDefaultSitesJsonArrayFromAssets(Context context) {
        JSONArray jSONArray;
        JSONException e;
        try {
            jSONArray = new JSONArray(loadDefaultSitesFromAssets(context, C0769R.raw.topsites));
            try {
                long currentTimeMillis = System.currentTimeMillis();
                for (int i = 0; i < jSONArray.length(); i++) {
                    ((JSONObject) jSONArray.get(i)).put("lastViewTimestamp", currentTimeMillis);
                }
                saveDefaultSites(context, jSONArray);
            } catch (JSONException e2) {
                e = e2;
                e.printStackTrace();
                return jSONArray;
            }
        } catch (JSONException e3) {
            e = e3;
            jSONArray = null;
            e.printStackTrace();
            return jSONArray;
        }
        return jSONArray;
    }

    public static void clearTopSiteData(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("topsites_pref").apply();
    }

    public static String loadDefaultSitesFromAssets(Context context, int i) {
        String str = "[]";
        try {
            InputStream openRawResource = context.getResources().openRawResource(i);
            byte[] bArr = new byte[openRawResource.available()];
            openRawResource.read(bArr);
            openRawResource.close();
            return new String(bArr, Constants.ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable unused) {
        }
        return str;
    }

    public static void saveDefaultSites(Context context, JSONArray jSONArray) {
        if (context != null) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("topsites_pref", jSONArray.toString()).apply();
        }
    }

    public static List<Site> paresJsonToList(Context context, JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        if (jSONArray != null) {
            int i = 0;
            while (i < jSONArray.length()) {
                try {
                    JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                    long j = jSONObject.getLong("id");
                    String string = jSONObject.getString("title");
                    String string2 = jSONObject.getString("url");
                    long j2 = jSONObject.getLong("viewCount");
                    long j3 = jSONObject.getLong("lastViewTimestamp");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("file:///android_asset/topsites/icon/");
                    stringBuilder.append(jSONObject.getString("favicon"));
                    Site site = new Site(j, string, string2, j2, j3, stringBuilder.toString());
                    site.setDefault(true);
                    arrayList.add(site);
                    i++;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Throwable unused) {
                }
            }
        }
        return arrayList;
    }
}
