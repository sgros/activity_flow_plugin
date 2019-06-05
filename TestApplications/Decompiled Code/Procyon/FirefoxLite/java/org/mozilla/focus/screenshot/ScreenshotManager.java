// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot;

import android.net.Uri;
import org.mozilla.focus.screenshot.model.Screenshot;
import java.net.MalformedURLException;
import java.util.Map;
import org.mozilla.urlutils.UrlUtils;
import java.net.URL;
import org.mozilla.focus.provider.ScreenshotContract;
import java.util.Iterator;
import org.json.JSONException;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;
import android.support.v4.util.Pair;
import android.arch.lifecycle.Observer;
import org.mozilla.cachedrequestloader.BackgroundCachedRequestLoader;
import org.mozilla.focus.web.WebViewProvider;
import android.text.TextUtils;
import org.mozilla.focus.utils.AppConfigWrapper;
import java.util.concurrent.CountDownLatch;
import java.io.IOException;
import org.mozilla.focus.utils.IOUtils;
import android.content.Context;
import org.mozilla.cachedrequestloader.ResponseData;
import org.mozilla.focus.provider.QueryHandler;
import java.util.HashMap;

public class ScreenshotManager
{
    private static volatile ScreenshotManager sInstance;
    HashMap<String, String> categories;
    private int categoryVersion;
    private QueryHandler mQueryHandler;
    private ResponseData responseData;
    
    public ScreenshotManager() {
        this.categories = new HashMap<String, String>();
        this.categoryVersion = 1;
    }
    
    public static ScreenshotManager getInstance() {
        if (ScreenshotManager.sInstance == null) {
            synchronized (ScreenshotManager.class) {
                if (ScreenshotManager.sInstance == null) {
                    ScreenshotManager.sInstance = new ScreenshotManager();
                }
            }
        }
        return ScreenshotManager.sInstance;
    }
    
    private void initFromLocal(final Context context) throws IOException {
        this.initWithJson(IOUtils.readAsset(context, "screenshots-mapping.json"));
    }
    
    private boolean initFromRemote(final Context context) throws InterruptedException {
        boolean b = true;
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final String screenshotCategoryUrl = AppConfigWrapper.getScreenshotCategoryUrl(context);
        if (TextUtils.isEmpty((CharSequence)screenshotCategoryUrl)) {
            return false;
        }
        (this.responseData = new BackgroundCachedRequestLoader(context, "screenshot_category", screenshotCategoryUrl, WebViewProvider.getUserAgentString(context), 10003).getStringLiveData()).observeForever(new _$$Lambda$ScreenshotManager$DuDV0OUCiu80Bc2qNHJMkf_qmMs(this, countDownLatch));
        countDownLatch.await(5L, TimeUnit.SECONDS);
        if (countDownLatch.getCount() != 0L) {
            b = false;
        }
        return b;
    }
    
    private void initWithJson(final JSONObject jsonObject) {
        try {
            this.categories.clear();
            final JSONObject jsonObject2 = jsonObject.getJSONObject("mapping");
            final Iterator keys = jsonObject2.keys();
            while (keys.hasNext()) {
                final String value = keys.next();
                final Object value2 = jsonObject2.get(value);
                if (value2 instanceof JSONArray) {
                    final JSONArray jsonArray = (JSONArray)value2;
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        final Object value3 = jsonArray.get(i);
                        if (value3 instanceof String) {
                            this.categories.put((String)value3, value);
                        }
                    }
                }
            }
            this.categoryVersion = jsonObject.getInt("version");
        }
        catch (JSONException ex) {
            Log.e("ScreenshotManager", "ScreenshotManager init error with incorrect format: ", (Throwable)ex);
        }
    }
    
    private void lazyInitCategories(final Context context) {
        try {
            if (this.categories.size() != 0) {
                return;
            }
            try {
                if (!this.initFromRemote(context)) {
                    this.initFromLocal(context);
                }
            }
            catch (InterruptedException ex2) {
                this.initFromLocal(context);
            }
        }
        catch (IOException ex) {
            Log.e("ScreenshotManager", "ScreenshotManager init error: ", (Throwable)ex);
        }
    }
    
    public void delete(final long i, final QueryHandler.AsyncDeleteListener asyncDeleteListener) {
        this.mQueryHandler.startDelete(2, (Object)new QueryHandler.AsyncDeleteWrapper(i, asyncDeleteListener), ScreenshotContract.Screenshot.CONTENT_URI, "_id = ?", new String[] { Long.toString(i) });
    }
    
    public String getCategory(final Context context, final String spec) {
        this.lazyInitCategories(context);
        try {
            if (this.categories.size() != 0) {
                final String stripCommonSubdomains = UrlUtils.stripCommonSubdomains(new URL(spec).getAuthority());
                for (final Map.Entry<String, String> entry : this.categories.entrySet()) {
                    if (stripCommonSubdomains.endsWith(entry.getKey())) {
                        return entry.getValue();
                    }
                }
                return "Others";
            }
            throw new IllegalStateException("Screenshot category is not ready!");
        }
        catch (MalformedURLException ex) {
            return "Error";
        }
    }
    
    public int getCategoryVersion() {
        if (this.categories.size() != 0) {
            return this.categoryVersion;
        }
        throw new IllegalStateException("Screenshot category is not ready! Call init before get Version.");
    }
    
    public void init(final Context context) {
        this.mQueryHandler = new QueryHandler(context.getContentResolver());
    }
    
    public void insert(final Screenshot screenshot, final QueryHandler.AsyncInsertListener asyncInsertListener) {
        this.mQueryHandler.startInsert(2, (Object)asyncInsertListener, ScreenshotContract.Screenshot.CONTENT_URI, QueryHandler.getContentValuesFromScreenshot(screenshot));
    }
    
    public void query(final int i, final int j, final QueryHandler.AsyncQueryListener asyncQueryListener) {
        final QueryHandler mQueryHandler = this.mQueryHandler;
        final StringBuilder sb = new StringBuilder();
        sb.append(ScreenshotContract.Screenshot.CONTENT_URI.toString());
        sb.append("?offset=");
        sb.append(i);
        sb.append("&limit=");
        sb.append(j);
        mQueryHandler.startQuery(2, (Object)asyncQueryListener, Uri.parse(sb.toString()), (String[])null, (String)null, (String[])null, "timestamp DESC");
    }
}
