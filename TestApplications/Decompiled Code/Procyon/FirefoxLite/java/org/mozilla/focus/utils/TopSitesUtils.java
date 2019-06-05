// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import java.util.ArrayList;
import org.mozilla.focus.history.model.Site;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.preference.PreferenceManager;
import android.content.Context;

public class TopSitesUtils
{
    public static void clearTopSiteData(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("topsites_pref").apply();
    }
    
    public static JSONArray getDefaultSitesJsonArrayFromAssets(final Context context) {
        JSONArray jsonArray2;
        try {
            final JSONArray jsonArray = new JSONArray(loadDefaultSitesFromAssets(context, 2131689487));
            try {
                final long currentTimeMillis = System.currentTimeMillis();
                for (int i = 0; i < jsonArray.length(); ++i) {
                    ((JSONObject)jsonArray.get(i)).put("lastViewTimestamp", currentTimeMillis);
                }
                saveDefaultSites(context, jsonArray);
                jsonArray2 = jsonArray;
            }
            catch (JSONException ex) {
                jsonArray2 = jsonArray;
            }
        }
        catch (JSONException ex) {
            jsonArray2 = null;
        }
        final JSONException ex;
        ex.printStackTrace();
        return jsonArray2;
    }
    
    public static String loadDefaultSitesFromAssets(final Context context, final int n) {
        try {
            try {
                final InputStream openRawResource = context.getResources().openRawResource(n);
                final byte[] array = new byte[openRawResource.available()];
                openRawResource.read(array);
                openRawResource.close();
                return new String(array, "UTF-8");
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            return "[]";
        }
        finally {
            return "[]";
        }
    }
    
    public static List<Site> paresJsonToList(Context context, final JSONArray jsonArray) {
        context = (Context)new ArrayList();
        if (jsonArray == null) {
            return (List<Site>)context;
        }
        int i = 0;
        try {
            try {
                while (i < jsonArray.length()) {
                    final JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    final long long1 = jsonObject.getLong("id");
                    final String string = jsonObject.getString("title");
                    final String string2 = jsonObject.getString("url");
                    final long long2 = jsonObject.getLong("viewCount");
                    final long long3 = jsonObject.getLong("lastViewTimestamp");
                    final StringBuilder sb = new StringBuilder();
                    sb.append("file:///android_asset/topsites/icon/");
                    sb.append(jsonObject.getString("favicon"));
                    final Site site = new Site(long1, string, string2, long2, long3, sb.toString());
                    site.setDefault(true);
                    ((List<Site>)context).add(site);
                    ++i;
                }
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
            return (List<Site>)context;
        }
        finally {
            return (List<Site>)context;
        }
    }
    
    public static void saveDefaultSites(final Context context, final JSONArray jsonArray) {
        if (context == null) {
            return;
        }
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("topsites_pref", jsonArray.toString()).apply();
    }
}
