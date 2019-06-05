package org.mozilla.rocket.home.pinsite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.utils.TopSitesUtils;
import org.mozilla.rocket.C0769R;

/* compiled from: PinSiteManager.kt */
public final class SharedPreferencePinSiteDelegate implements PinSiteDelegate {
    public static final Companion Companion = new Companion();
    private final Context context;
    private boolean isEnabled = isEnabled(this.rootNode);
    private final List<Site> partnerList = new ArrayList();
    private final SharedPreferences pref = this.context.getSharedPreferences("pin_sites", 0);
    private final JSONObject rootNode = new JSONObject(TopSitesUtils.loadDefaultSitesFromAssets(this.context, C0769R.raw.pin_sites));
    private final List<Site> sites = new ArrayList();

    /* compiled from: PinSiteManager.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void resetPinSiteData(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            context.getSharedPreferences("pin_sites", 0).edit().putBoolean("first_init", true).putString("json", "").apply();
        }
    }

    private final long getViewCountForPinSiteAt(int i) {
        return Long.MAX_VALUE - (((long) i) * 100);
    }

    @SuppressLint({"LogUsage"})
    private final void log(String str) {
    }

    public SharedPreferencePinSiteDelegate(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.context = context;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isEnable: ");
        stringBuilder.append(this.isEnabled);
        log(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("isFirstInit: ");
        stringBuilder.append(isFirstInit());
        log(stringBuilder.toString());
        if (this.isEnabled) {
            List partnerList = getPartnerList(this.rootNode);
            if (hasTopSiteRecord()) {
                log("init for update user");
                initForUpdateUser(this.partnerList, partnerList);
                return;
            }
            log("init for new user");
            initForNewUser(this.partnerList, partnerList);
            return;
        }
        log("no initialization needed");
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public boolean isPinned(Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        Iterable<Site> iterable = this.sites;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (Site id : iterable) {
            Object obj;
            if (id.getId() == site.getId()) {
                obj = 1;
                continue;
            } else {
                obj = null;
                continue;
            }
            if (obj != null) {
                return true;
            }
        }
        return false;
    }

    public void pin(Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        this.sites.add(0, new Site(site.getId(), site.getTitle(), site.getUrl(), site.getViewCount(), site.getLastViewTimestamp(), site.getFavIconUri()));
        save(this.sites);
    }

    public void unpinned(Site site) {
        Intrinsics.checkParameterIsNotNull(site, "site");
        CollectionsKt__MutableCollectionsKt.removeAll(this.sites, new SharedPreferencePinSiteDelegate$unpinned$1(site));
        save(this.sites);
    }

    public List<Site> getPinSites() {
        load(this.sites);
        return this.sites;
    }

    public boolean isFirstTimeEnable() {
        return isFirstInit();
    }

    private final void save(List<? extends Site> list) {
        int i = 0;
        for (Object next : list) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            ((Site) next).setViewCount(getViewCountForPinSiteAt(i));
            i = i2;
        }
        this.pref.edit().putString("json", sitesToJson(list).toString()).apply();
        log("save");
    }

    private final void load(List<Site> list) {
        if (this.isEnabled) {
            log("load - enabled");
            list.clear();
            boolean isFirstInit = isFirstInit();
            if (!isFirstInit || (this.partnerList.isEmpty() ^ 1) == 0) {
                log("load saved pin site pref");
                loadSavedPinnedSite(list);
            } else {
                list.addAll(0, this.partnerList);
                log("load partner list");
                save(list);
            }
            if (isFirstInit) {
                log("init finished");
                onFirstInitComplete();
            }
            return;
        }
        log("load - not enabled");
    }

    private final void initForUpdateUser(List<Site> list, List<? extends Site> list2) {
        list.addAll(list2);
    }

    private final void initForNewUser(List<Site> list, List<? extends Site> list2) {
        list.addAll(list2);
        List toMutableList = CollectionsKt___CollectionsKt.toMutableList((Collection) jsonToSites(new JSONArray(TopSitesUtils.loadDefaultSitesFromAssets(this.context, C0769R.raw.topsites)), true));
        int size = 2 - list2.size();
        while (true) {
            int i = size - 1;
            if (size > 0 && (toMutableList.isEmpty() ^ 1) != 0) {
                list.add(toMutableList.remove(0));
                size = i;
            } else {
                return;
            }
        }
    }

    private final void loadSavedPinnedSite(List<Site> list) {
        try {
            list.addAll(jsonToSites(new JSONArray(this.pref.getString("json", "")), false));
        } catch (JSONException unused) {
        }
    }

    private final boolean isFirstInit() {
        return this.pref.getBoolean("first_init", true);
    }

    private final void onFirstInitComplete() {
        this.pref.edit().putBoolean("first_init", false).apply();
    }

    private final JSONArray sitesToJson(List<? extends Site> list) {
        JSONArray jSONArray = new JSONArray();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            JSONObject siteToJson = siteToJson((Site) list.get(i));
            if (siteToJson != null) {
                jSONArray.put(siteToJson);
            }
        }
        return jSONArray;
    }

    private final List<Site> jsonToSites(JSONArray jSONArray, boolean z) {
        ArrayList arrayList = new ArrayList();
        String str = z ? "file:///android_asset/topsites/icon/" : "";
        int i = 0;
        try {
            int length = jSONArray.length();
            while (i < length) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                long j = jSONObject.getLong("id");
                String string = jSONObject.getString("title");
                String string2 = jSONObject.getString("url");
                long j2 = jSONObject.getLong("viewCount");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                Intrinsics.checkExpressionValueIsNotNull(jSONObject, "obj");
                stringBuilder.append(getFaviconUrl(jSONObject));
                String str2 = str;
                Site site = r6;
                Site site2 = new Site(j, string, string2, j2, 0, stringBuilder.toString());
                arrayList.add(site);
                i++;
                str = str2;
            }
        } catch (JSONException unused) {
        }
        return arrayList;
    }

    private final String getFaviconUrl(JSONObject jSONObject) {
        String optString = jSONObject.optString("favicon");
        Intrinsics.checkExpressionValueIsNotNull(optString, "json.optString(TopSitesUtils.KEY_FAVICON)");
        return optString;
    }

    private final JSONObject siteToJson(Site site) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", site.getId());
            jSONObject.put("url", site.getUrl());
            jSONObject.put("title", site.getTitle());
            jSONObject.put("favicon", site.getFavIconUri());
            return jSONObject.put("viewCount", site.getViewCount());
        } catch (JSONException unused) {
            return null;
        }
    }

    private final boolean hasTopSiteRecord() {
        String string = PreferenceManager.getDefaultSharedPreferences(this.context).getString("topsites_pref", "");
        return string != null && string.length() > 0;
    }

    private final boolean isEnabled(JSONObject jSONObject) {
        return jSONObject.getBoolean("isEnabled");
    }

    private final List<Site> getPartnerList(JSONObject jSONObject) {
        JSONArray jSONArray = jSONObject.getJSONArray("partner");
        Intrinsics.checkExpressionValueIsNotNull(jSONArray, "rootNode.getJSONArray(JSON_KEY_STRING_PARTNER)");
        return jsonToSites(jSONArray, true);
    }
}
