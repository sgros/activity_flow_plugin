// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.lite.newspoint;

import org.json.JSONException;
import org.json.JSONArray;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Arrays;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import org.mozilla.lite.partner.Repository;

public class RepositoryNewsPoint extends Repository<NewsPointItem>
{
    static Parser<NewsPointItem> PARSER;
    
    static {
        RepositoryNewsPoint.PARSER = (Parser<NewsPointItem>)_$$Lambda$RepositoryNewsPoint$ZvYynr31y46fQc7qiAJk6VMEhFM.INSTANCE;
    }
    
    public RepositoryNewsPoint(final Context context, final String s) {
        super(context, null, 3, null, null, "newspoint", s, 1, RepositoryNewsPoint.PARSER, true);
    }
    
    private static JSONArray safeGetArray(final JSONObject jsonObject, final String s) {
        try {
            return jsonObject.getJSONArray(s);
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    private static long safeGetLong(final JSONObject jsonObject, final String s) {
        try {
            return jsonObject.getLong(s);
        }
        catch (JSONException ex) {
            return -1L;
        }
    }
    
    private static String safeGetString(final JSONObject jsonObject, final String s) {
        try {
            return jsonObject.getString(s);
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    protected String getSubscriptionUrl(final int i) {
        return String.format(Locale.US, this.subscriptionUrl, i, 30);
    }
}
