package org.osmdroid.config;

import android.util.Log;
import java.io.File;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.osmdroid.tileprovider.util.StorageUtils;

public class DefaultConfigurationProvider implements IConfigurationProvider {
    protected int animationSpeedDefault = 1000;
    protected int animationSpeedShort = 500;
    protected short cacheMapTileCount = (short) 9;
    protected short cacheTileOvershoot = (short) 0;
    protected boolean debugMapTileDownloader = false;
    protected boolean debugMapView = false;
    protected boolean debugMode = false;
    protected boolean debugTileProviders = false;
    protected long expirationAdder = 0;
    protected Long expirationOverride = null;
    protected long gpsWaitTime = 20000;
    protected SimpleDateFormat httpHeaderDateTimeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    protected Proxy httpProxy = null;
    protected boolean isMapViewHardwareAccelerated = true;
    private final Map<String, String> mAdditionalHttpRequestProperties = new HashMap();
    private String mNormalizedUserAgent;
    protected boolean mTileDownloaderFollowRedirects = true;
    protected long mTileGCBulkPauseInMillis = 500;
    protected int mTileGCBulkSize = 20;
    protected long mTileGCFrequencyInMillis = 300000;
    protected boolean mapViewRecycler = true;
    protected File osmdroidBasePath;
    protected File osmdroidTileCache;
    protected short tileDownloadMaxQueueSize = (short) 40;
    protected short tileDownloadThreads = (short) 2;
    protected long tileFileSystemCacheMaxBytes = 629145600;
    protected long tileFileSystemCacheTrimBytes = 524288000;
    protected short tileFileSystemMaxQueueSize = (short) 40;
    protected short tileFileSystemThreads = (short) 8;
    protected String userAgentHttpHeader = "User-Agent";
    protected String userAgentValue = "osmdroid";

    public long getGpsWaitTime() {
        return this.gpsWaitTime;
    }

    public boolean isDebugMode() {
        return this.debugMode;
    }

    public boolean isDebugMapView() {
        return this.debugMapView;
    }

    public boolean isDebugTileProviders() {
        return this.debugTileProviders;
    }

    public boolean isDebugMapTileDownloader() {
        return this.debugMapTileDownloader;
    }

    public boolean isMapViewHardwareAccelerated() {
        return this.isMapViewHardwareAccelerated;
    }

    public String getUserAgentValue() {
        return this.userAgentValue;
    }

    public void setUserAgentValue(String str) {
        this.userAgentValue = str;
    }

    public Map<String, String> getAdditionalHttpRequestProperties() {
        return this.mAdditionalHttpRequestProperties;
    }

    public short getCacheMapTileCount() {
        return this.cacheMapTileCount;
    }

    public short getTileDownloadThreads() {
        return this.tileDownloadThreads;
    }

    public short getTileFileSystemThreads() {
        return this.tileFileSystemThreads;
    }

    public short getTileDownloadMaxQueueSize() {
        return this.tileDownloadMaxQueueSize;
    }

    public short getTileFileSystemMaxQueueSize() {
        return this.tileFileSystemMaxQueueSize;
    }

    public long getTileFileSystemCacheMaxBytes() {
        return this.tileFileSystemCacheMaxBytes;
    }

    public long getTileFileSystemCacheTrimBytes() {
        return this.tileFileSystemCacheTrimBytes;
    }

    public SimpleDateFormat getHttpHeaderDateTimeFormat() {
        return this.httpHeaderDateTimeFormat;
    }

    public Proxy getHttpProxy() {
        return this.httpProxy;
    }

    public File getOsmdroidBasePath() {
        if (this.osmdroidBasePath == null) {
            this.osmdroidBasePath = new File(StorageUtils.getStorage().getAbsolutePath(), "osmdroid");
        }
        try {
            this.osmdroidBasePath.mkdirs();
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to create base path at ");
            stringBuilder.append(this.osmdroidBasePath.getAbsolutePath());
            Log.d("OsmDroid", stringBuilder.toString(), e);
        }
        return this.osmdroidBasePath;
    }

    public File getOsmdroidTileCache() {
        if (this.osmdroidTileCache == null) {
            this.osmdroidTileCache = new File(getOsmdroidBasePath(), "tiles");
        }
        try {
            this.osmdroidTileCache.mkdirs();
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to create tile cache path at ");
            stringBuilder.append(this.osmdroidTileCache.getAbsolutePath());
            Log.d("OsmDroid", stringBuilder.toString(), e);
        }
        return this.osmdroidTileCache;
    }

    public String getUserAgentHttpHeader() {
        return this.userAgentHttpHeader;
    }

    public long getExpirationExtendedDuration() {
        return this.expirationAdder;
    }

    public Long getExpirationOverrideDuration() {
        return this.expirationOverride;
    }

    public int getAnimationSpeedDefault() {
        return this.animationSpeedDefault;
    }

    public int getAnimationSpeedShort() {
        return this.animationSpeedShort;
    }

    public boolean isMapViewRecyclerFriendly() {
        return this.mapViewRecycler;
    }

    public short getCacheMapTileOvershoot() {
        return this.cacheTileOvershoot;
    }

    public long getTileGCFrequencyInMillis() {
        return this.mTileGCFrequencyInMillis;
    }

    public int getTileGCBulkSize() {
        return this.mTileGCBulkSize;
    }

    public long getTileGCBulkPauseInMillis() {
        return this.mTileGCBulkPauseInMillis;
    }

    public boolean isMapTileDownloaderFollowRedirects() {
        return this.mTileDownloaderFollowRedirects;
    }

    public String getNormalizedUserAgent() {
        return this.mNormalizedUserAgent;
    }
}
