// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.home.pinsite;

import kotlin.jvm.functions.Function1;
import java.util.Iterator;
import kotlin.collections.CollectionsKt;
import android.annotation.SuppressLint;
import org.json.JSONException;
import java.util.Collection;
import android.preference.PreferenceManager;
import org.json.JSONArray;
import org.mozilla.focus.utils.TopSitesUtils;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;
import android.content.SharedPreferences;
import org.mozilla.focus.history.model.Site;
import java.util.List;
import android.content.Context;

public final class SharedPreferencePinSiteDelegate implements PinSiteDelegate
{
    public static final Companion Companion;
    private final Context context;
    private boolean isEnabled;
    private final List<Site> partnerList;
    private final SharedPreferences pref;
    private final JSONObject rootNode;
    private final List<Site> sites;
    
    static {
        Companion = new Companion(null);
    }
    
    public SharedPreferencePinSiteDelegate(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.context = context;
        this.pref = this.context.getSharedPreferences("pin_sites", 0);
        this.sites = new ArrayList<Site>();
        this.partnerList = new ArrayList<Site>();
        this.rootNode = new JSONObject(TopSitesUtils.loadDefaultSitesFromAssets(this.context, 2131689481));
        this.isEnabled = this.isEnabled(this.rootNode);
        final StringBuilder sb = new StringBuilder();
        sb.append("isEnable: ");
        sb.append(this.isEnabled);
        this.log(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("isFirstInit: ");
        sb2.append(this.isFirstInit());
        this.log(sb2.toString());
        if (this.isEnabled) {
            final List<Site> partnerList = this.getPartnerList(this.rootNode);
            if (this.hasTopSiteRecord()) {
                this.log("init for update user");
                this.initForUpdateUser(this.partnerList, partnerList);
            }
            else {
                this.log("init for new user");
                this.initForNewUser(this.partnerList, partnerList);
            }
        }
        else {
            this.log("no initialization needed");
        }
    }
    
    private final String getFaviconUrl(final JSONObject jsonObject) {
        final String optString = jsonObject.optString("favicon");
        Intrinsics.checkExpressionValueIsNotNull(optString, "json.optString(TopSitesUtils.KEY_FAVICON)");
        return optString;
    }
    
    private final List<Site> getPartnerList(final JSONObject jsonObject) {
        final JSONArray jsonArray = jsonObject.getJSONArray("partner");
        Intrinsics.checkExpressionValueIsNotNull(jsonArray, "rootNode.getJSONArray(JSON_KEY_STRING_PARTNER)");
        return this.jsonToSites(jsonArray, true);
    }
    
    private final long getViewCountForPinSiteAt(final int n) {
        return Long.MAX_VALUE - n * 100L;
    }
    
    private final boolean hasTopSiteRecord() {
        final String string = PreferenceManager.getDefaultSharedPreferences(this.context).getString("topsites_pref", "");
        boolean b = false;
        if (string != null) {
            b = b;
            if (string.length() > 0) {
                b = true;
            }
        }
        return b;
    }
    
    private final void initForNewUser(final List<Site> list, final List<? extends Site> list2) {
        list.addAll(list2);
        final List<Site> mutableList = CollectionsKt___CollectionsKt.toMutableList((Collection<? extends Site>)this.jsonToSites(new JSONArray(TopSitesUtils.loadDefaultSitesFromAssets(this.context, 2131689487)), true));
        for (int n = 2 - list2.size(); n > 0 && (mutableList.isEmpty() ^ true); --n) {
            list.add(mutableList.remove(0));
        }
    }
    
    private final void initForUpdateUser(final List<Site> list, final List<? extends Site> list2) {
        list.addAll(list2);
    }
    
    private final boolean isEnabled(final JSONObject jsonObject) {
        return jsonObject.getBoolean("isEnabled");
    }
    
    private final boolean isFirstInit() {
        return this.pref.getBoolean("first_init", true);
    }
    
    private final List<Site> jsonToSites(final JSONArray jsonArray, final boolean b) {
        final ArrayList<Site> list = new ArrayList<Site>();
        String str;
        if (b) {
            str = "file:///android_asset/topsites/icon/";
        }
        else {
            str = "";
        }
        int i = 0;
        try {
            while (i < jsonArray.length()) {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                final long long1 = jsonObject.getLong("id");
                final String string = jsonObject.getString("title");
                final String string2 = jsonObject.getString("url");
                final long long2 = jsonObject.getLong("viewCount");
                final StringBuilder sb = new StringBuilder();
                sb.append(str);
                Intrinsics.checkExpressionValueIsNotNull(jsonObject, "obj");
                sb.append(this.getFaviconUrl(jsonObject));
                list.add(new Site(long1, string, string2, long2, 0L, sb.toString()));
                ++i;
            }
            return list;
        }
        catch (JSONException ex) {
            return list;
        }
    }
    
    private final void load(final List<Site> list) {
        if (!this.isEnabled) {
            this.log("load - not enabled");
            return;
        }
        this.log("load - enabled");
        list.clear();
        final boolean firstInit = this.isFirstInit();
        if (firstInit && (this.partnerList.isEmpty() ^ true)) {
            list.addAll(0, this.partnerList);
            this.log("load partner list");
            this.save(list);
        }
        else {
            this.log("load saved pin site pref");
            this.loadSavedPinnedSite(list);
        }
        if (firstInit) {
            this.log("init finished");
            this.onFirstInitComplete();
        }
    }
    
    private final void loadSavedPinnedSite(final List<Site> list) {
        final String string = this.pref.getString("json", "");
        try {
            list.addAll(this.jsonToSites(new JSONArray(string), false));
        }
        catch (JSONException ex) {}
    }
    
    @SuppressLint({ "LogUsage" })
    private final void log(final String s) {
    }
    
    private final void onFirstInitComplete() {
        this.pref.edit().putBoolean("first_init", false).apply();
    }
    
    private final void save(final List<? extends Site> list) {
        final Iterator<Site> iterator = list.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final Site next = iterator.next();
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            next.setViewCount(this.getViewCountForPinSiteAt(n));
            ++n;
        }
        this.pref.edit().putString("json", this.sitesToJson(list).toString()).apply();
        this.log("save");
    }
    
    private final JSONObject siteToJson(final Site site) {
        JSONObject put;
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", site.getId());
            jsonObject.put("url", (Object)site.getUrl());
            jsonObject.put("title", (Object)site.getTitle());
            jsonObject.put("favicon", (Object)site.getFavIconUri());
            put = jsonObject.put("viewCount", site.getViewCount());
        }
        catch (JSONException ex) {
            put = null;
        }
        return put;
    }
    
    private final JSONArray sitesToJson(final List<? extends Site> list) {
        final JSONArray jsonArray = new JSONArray();
        for (int size = list.size(), i = 0; i < size; ++i) {
            final JSONObject siteToJson = this.siteToJson((Site)list.get(i));
            if (siteToJson != null) {
                jsonArray.put((Object)siteToJson);
            }
        }
        return jsonArray;
    }
    
    @Override
    public List<Site> getPinSites() {
        this.load(this.sites);
        return this.sites;
    }
    
    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
    
    @Override
    public boolean isFirstTimeEnable() {
        return this.isFirstInit();
    }
    
    @Override
    public boolean isPinned(final Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        final List<Site> list = this.sites;
        final boolean b = list instanceof Collection;
        final boolean b2 = false;
        boolean b3;
        if (b && list.isEmpty()) {
            b3 = b2;
        }
        else {
            final Iterator<Object> iterator = list.iterator();
            do {
                b3 = b2;
                if (iterator.hasNext()) {
                    continue;
                }
                return b3;
            } while (iterator.next().getId() != site.getId());
            b3 = true;
        }
        return b3;
    }
    
    @Override
    public void pin(final Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        this.sites.add(0, new Site(site.getId(), site.getTitle(), site.getUrl(), site.getViewCount(), site.getLastViewTimestamp(), site.getFavIconUri()));
        this.save(this.sites);
    }
    
    @Override
    public void unpinned(final Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        CollectionsKt__MutableCollectionsKt.removeAll(this.sites, (Function1<? super Site, Boolean>)new SharedPreferencePinSiteDelegate$unpinned.SharedPreferencePinSiteDelegate$unpinned$1(site));
        this.save(this.sites);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final void resetPinSiteData(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            context.getSharedPreferences("pin_sites", 0).edit().putBoolean("first_init", true).putString("json", "").apply();
        }
    }
}
