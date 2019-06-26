// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.config;

import android.util.Log;
import org.osmdroid.tileprovider.util.StorageUtils;
import java.util.Locale;
import java.util.HashMap;
import java.io.File;
import java.util.Map;
import java.net.Proxy;
import java.text.SimpleDateFormat;

public class DefaultConfigurationProvider implements IConfigurationProvider
{
    protected int animationSpeedDefault;
    protected int animationSpeedShort;
    protected short cacheMapTileCount;
    protected short cacheTileOvershoot;
    protected boolean debugMapTileDownloader;
    protected boolean debugMapView;
    protected boolean debugMode;
    protected boolean debugTileProviders;
    protected long expirationAdder;
    protected Long expirationOverride;
    protected long gpsWaitTime;
    protected SimpleDateFormat httpHeaderDateTimeFormat;
    protected Proxy httpProxy;
    protected boolean isMapViewHardwareAccelerated;
    private final Map<String, String> mAdditionalHttpRequestProperties;
    private String mNormalizedUserAgent;
    protected boolean mTileDownloaderFollowRedirects;
    protected long mTileGCBulkPauseInMillis;
    protected int mTileGCBulkSize;
    protected long mTileGCFrequencyInMillis;
    protected boolean mapViewRecycler;
    protected File osmdroidBasePath;
    protected File osmdroidTileCache;
    protected short tileDownloadMaxQueueSize;
    protected short tileDownloadThreads;
    protected long tileFileSystemCacheMaxBytes;
    protected long tileFileSystemCacheTrimBytes;
    protected short tileFileSystemMaxQueueSize;
    protected short tileFileSystemThreads;
    protected String userAgentHttpHeader;
    protected String userAgentValue;
    
    public DefaultConfigurationProvider() {
        this.gpsWaitTime = 20000L;
        this.debugMode = false;
        this.debugMapView = false;
        this.debugTileProviders = false;
        this.debugMapTileDownloader = false;
        this.isMapViewHardwareAccelerated = true;
        this.userAgentValue = "osmdroid";
        this.userAgentHttpHeader = "User-Agent";
        this.mAdditionalHttpRequestProperties = new HashMap<String, String>();
        this.cacheMapTileCount = 9;
        this.tileDownloadThreads = 2;
        this.tileFileSystemThreads = 8;
        this.tileDownloadMaxQueueSize = 40;
        this.tileFileSystemMaxQueueSize = 40;
        this.tileFileSystemCacheMaxBytes = 629145600L;
        this.tileFileSystemCacheTrimBytes = 524288000L;
        this.httpHeaderDateTimeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        this.expirationAdder = 0L;
        this.expirationOverride = null;
        this.httpProxy = null;
        this.animationSpeedDefault = 1000;
        this.animationSpeedShort = 500;
        this.mapViewRecycler = true;
        this.cacheTileOvershoot = 0;
        this.mTileGCFrequencyInMillis = 300000L;
        this.mTileGCBulkSize = 20;
        this.mTileGCBulkPauseInMillis = 500L;
        this.mTileDownloaderFollowRedirects = true;
    }
    
    @Override
    public Map<String, String> getAdditionalHttpRequestProperties() {
        return this.mAdditionalHttpRequestProperties;
    }
    
    @Override
    public int getAnimationSpeedDefault() {
        return this.animationSpeedDefault;
    }
    
    @Override
    public int getAnimationSpeedShort() {
        return this.animationSpeedShort;
    }
    
    @Override
    public short getCacheMapTileCount() {
        return this.cacheMapTileCount;
    }
    
    @Override
    public short getCacheMapTileOvershoot() {
        return this.cacheTileOvershoot;
    }
    
    @Override
    public long getExpirationExtendedDuration() {
        return this.expirationAdder;
    }
    
    @Override
    public Long getExpirationOverrideDuration() {
        return this.expirationOverride;
    }
    
    @Override
    public long getGpsWaitTime() {
        return this.gpsWaitTime;
    }
    
    @Override
    public SimpleDateFormat getHttpHeaderDateTimeFormat() {
        return this.httpHeaderDateTimeFormat;
    }
    
    @Override
    public Proxy getHttpProxy() {
        return this.httpProxy;
    }
    
    @Override
    public String getNormalizedUserAgent() {
        return this.mNormalizedUserAgent;
    }
    
    @Override
    public File getOsmdroidBasePath() {
        if (this.osmdroidBasePath == null) {
            this.osmdroidBasePath = new File(StorageUtils.getStorage().getAbsolutePath(), "osmdroid");
        }
        try {
            this.osmdroidBasePath.mkdirs();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to create base path at ");
            sb.append(this.osmdroidBasePath.getAbsolutePath());
            Log.d("OsmDroid", sb.toString(), (Throwable)ex);
        }
        return this.osmdroidBasePath;
    }
    
    @Override
    public File getOsmdroidTileCache() {
        if (this.osmdroidTileCache == null) {
            this.osmdroidTileCache = new File(this.getOsmdroidBasePath(), "tiles");
        }
        try {
            this.osmdroidTileCache.mkdirs();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to create tile cache path at ");
            sb.append(this.osmdroidTileCache.getAbsolutePath());
            Log.d("OsmDroid", sb.toString(), (Throwable)ex);
        }
        return this.osmdroidTileCache;
    }
    
    @Override
    public short getTileDownloadMaxQueueSize() {
        return this.tileDownloadMaxQueueSize;
    }
    
    @Override
    public short getTileDownloadThreads() {
        return this.tileDownloadThreads;
    }
    
    @Override
    public long getTileFileSystemCacheMaxBytes() {
        return this.tileFileSystemCacheMaxBytes;
    }
    
    @Override
    public long getTileFileSystemCacheTrimBytes() {
        return this.tileFileSystemCacheTrimBytes;
    }
    
    @Override
    public short getTileFileSystemMaxQueueSize() {
        return this.tileFileSystemMaxQueueSize;
    }
    
    @Override
    public short getTileFileSystemThreads() {
        return this.tileFileSystemThreads;
    }
    
    @Override
    public long getTileGCBulkPauseInMillis() {
        return this.mTileGCBulkPauseInMillis;
    }
    
    @Override
    public int getTileGCBulkSize() {
        return this.mTileGCBulkSize;
    }
    
    @Override
    public long getTileGCFrequencyInMillis() {
        return this.mTileGCFrequencyInMillis;
    }
    
    @Override
    public String getUserAgentHttpHeader() {
        return this.userAgentHttpHeader;
    }
    
    @Override
    public String getUserAgentValue() {
        return this.userAgentValue;
    }
    
    @Override
    public boolean isDebugMapTileDownloader() {
        return this.debugMapTileDownloader;
    }
    
    @Override
    public boolean isDebugMapView() {
        return this.debugMapView;
    }
    
    @Override
    public boolean isDebugMode() {
        return this.debugMode;
    }
    
    @Override
    public boolean isDebugTileProviders() {
        return this.debugTileProviders;
    }
    
    @Override
    public boolean isMapTileDownloaderFollowRedirects() {
        return this.mTileDownloaderFollowRedirects;
    }
    
    @Override
    public boolean isMapViewHardwareAccelerated() {
        return this.isMapViewHardwareAccelerated;
    }
    
    @Override
    public boolean isMapViewRecyclerFriendly() {
        return this.mapViewRecycler;
    }
    
    @Override
    public void setUserAgentValue(final String userAgentValue) {
        this.userAgentValue = userAgentValue;
    }
}
