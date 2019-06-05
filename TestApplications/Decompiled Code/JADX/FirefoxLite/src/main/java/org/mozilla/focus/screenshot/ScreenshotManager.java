package org.mozilla.focus.screenshot;

import android.content.Context;
import android.net.Uri;
import android.support.p001v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.cachedrequestloader.BackgroundCachedRequestLoader;
import org.mozilla.cachedrequestloader.ResponseData;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.provider.QueryHandler.AsyncDeleteListener;
import org.mozilla.focus.provider.QueryHandler.AsyncDeleteWrapper;
import org.mozilla.focus.provider.QueryHandler.AsyncInsertListener;
import org.mozilla.focus.provider.QueryHandler.AsyncQueryListener;
import org.mozilla.focus.provider.ScreenshotContract;
import org.mozilla.focus.screenshot.model.Screenshot;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.IOUtils;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.urlutils.UrlUtils;

public class ScreenshotManager {
    private static volatile ScreenshotManager sInstance;
    HashMap<String, String> categories = new HashMap();
    private int categoryVersion = 1;
    private QueryHandler mQueryHandler;
    private ResponseData responseData;

    public static ScreenshotManager getInstance() {
        if (sInstance == null) {
            synchronized (ScreenshotManager.class) {
                if (sInstance == null) {
                    sInstance = new ScreenshotManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        this.mQueryHandler = new QueryHandler(context.getContentResolver());
    }

    public void insert(Screenshot screenshot, AsyncInsertListener asyncInsertListener) {
        this.mQueryHandler.startInsert(2, asyncInsertListener, ScreenshotContract.Screenshot.CONTENT_URI, QueryHandler.getContentValuesFromScreenshot(screenshot));
    }

    public void delete(long j, AsyncDeleteListener asyncDeleteListener) {
        this.mQueryHandler.startDelete(2, new AsyncDeleteWrapper(j, asyncDeleteListener), ScreenshotContract.Screenshot.CONTENT_URI, "_id = ?", new String[]{Long.toString(j)});
    }

    public void query(int i, int i2, AsyncQueryListener asyncQueryListener) {
        QueryHandler queryHandler = this.mQueryHandler;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ScreenshotContract.Screenshot.CONTENT_URI.toString());
        stringBuilder.append("?offset=");
        stringBuilder.append(i);
        stringBuilder.append("&limit=");
        stringBuilder.append(i2);
        queryHandler.startQuery(2, asyncQueryListener, Uri.parse(stringBuilder.toString()), null, null, null, "timestamp DESC");
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0013 */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing block: B:9:?, code skipped:
            initFromLocal(r3);
     */
    private void lazyInitCategories(android.content.Context r3) {
        /*
        r2 = this;
        r0 = r2.categories;	 Catch:{ IOException -> 0x0017 }
        r0 = r0.size();	 Catch:{ IOException -> 0x0017 }
        if (r0 == 0) goto L_0x0009;
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.initFromRemote(r3);	 Catch:{ InterruptedException -> 0x0013 }
        if (r0 != 0) goto L_0x001f;
    L_0x000f:
        r2.initFromLocal(r3);	 Catch:{ InterruptedException -> 0x0013 }
        goto L_0x001f;
    L_0x0013:
        r2.initFromLocal(r3);	 Catch:{ IOException -> 0x0017 }
        goto L_0x001f;
    L_0x0017:
        r3 = move-exception;
        r0 = "ScreenshotManager";
        r1 = "ScreenshotManager init error: ";
        android.util.Log.e(r0, r1, r3);
    L_0x001f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.screenshot.ScreenshotManager.lazyInitCategories(android.content.Context):void");
    }

    private void initFromLocal(Context context) throws IOException {
        initWithJson(IOUtils.readAsset(context, "screenshots-mapping.json"));
    }

    private boolean initFromRemote(Context context) throws InterruptedException {
        boolean z = true;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String screenshotCategoryUrl = AppConfigWrapper.getScreenshotCategoryUrl(context);
        if (TextUtils.isEmpty(screenshotCategoryUrl)) {
            return false;
        }
        this.responseData = new BackgroundCachedRequestLoader(context, "screenshot_category", screenshotCategoryUrl, WebViewProvider.getUserAgentString(context), 10003).getStringLiveData();
        this.responseData.observeForever(new C0735-$$Lambda$ScreenshotManager$DuDV0OUCiu80Bc2qNHJMkf-qmMs(this, countDownLatch));
        countDownLatch.await(5, TimeUnit.SECONDS);
        if (countDownLatch.getCount() != 0) {
            z = false;
        }
        return z;
    }

    public static /* synthetic */ void lambda$initFromRemote$0(ScreenshotManager screenshotManager, CountDownLatch countDownLatch, Pair pair) {
        if (pair != null) {
            try {
                String str = (String) pair.second;
                if (!TextUtils.isEmpty(str)) {
                    screenshotManager.initWithJson(new JSONObject(str));
                    countDownLatch.countDown();
                }
            } catch (JSONException e) {
                Log.e("ScreenshotManager", "ScreenshotManager init error with incorrect format: ", e);
            }
        }
    }

    private void initWithJson(JSONObject jSONObject) {
        try {
            this.categories.clear();
            JSONObject jSONObject2 = jSONObject.getJSONObject("mapping");
            Iterator keys = jSONObject2.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                Object obj = jSONObject2.get(str);
                if (obj instanceof JSONArray) {
                    JSONArray jSONArray = (JSONArray) obj;
                    for (int i = 0; i < jSONArray.length(); i++) {
                        Object obj2 = jSONArray.get(i);
                        if (obj2 instanceof String) {
                            this.categories.put((String) obj2, str);
                        }
                    }
                }
            }
            this.categoryVersion = jSONObject.getInt("version");
        } catch (JSONException e) {
            Log.e("ScreenshotManager", "ScreenshotManager init error with incorrect format: ", e);
        }
    }

    public int getCategoryVersion() {
        if (this.categories.size() != 0) {
            return this.categoryVersion;
        }
        throw new IllegalStateException("Screenshot category is not ready! Call init before get Version.");
    }

    public String getCategory(Context context, String str) {
        lazyInitCategories(context);
        try {
            if (this.categories.size() != 0) {
                String stripCommonSubdomains = UrlUtils.stripCommonSubdomains(new URL(str).getAuthority());
                for (Entry entry : this.categories.entrySet()) {
                    if (stripCommonSubdomains.endsWith((String) entry.getKey())) {
                        return (String) entry.getValue();
                    }
                }
                return "Others";
            }
            throw new IllegalStateException("Screenshot category is not ready!");
        } catch (MalformedURLException unused) {
            return "Error";
        }
    }
}
