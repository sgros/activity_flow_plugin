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
   protected int animationSpeedDefault;
   protected int animationSpeedShort;
   protected short cacheMapTileCount = (short)9;
   protected short cacheTileOvershoot;
   protected boolean debugMapTileDownloader = false;
   protected boolean debugMapView = false;
   protected boolean debugMode = false;
   protected boolean debugTileProviders = false;
   protected long expirationAdder;
   protected Long expirationOverride;
   protected long gpsWaitTime = 20000L;
   protected SimpleDateFormat httpHeaderDateTimeFormat;
   protected Proxy httpProxy;
   protected boolean isMapViewHardwareAccelerated = true;
   private final Map mAdditionalHttpRequestProperties = new HashMap();
   private String mNormalizedUserAgent;
   protected boolean mTileDownloaderFollowRedirects;
   protected long mTileGCBulkPauseInMillis;
   protected int mTileGCBulkSize;
   protected long mTileGCFrequencyInMillis;
   protected boolean mapViewRecycler;
   protected File osmdroidBasePath;
   protected File osmdroidTileCache;
   protected short tileDownloadMaxQueueSize = (short)40;
   protected short tileDownloadThreads = (short)2;
   protected long tileFileSystemCacheMaxBytes = 629145600L;
   protected long tileFileSystemCacheTrimBytes = 524288000L;
   protected short tileFileSystemMaxQueueSize = (short)40;
   protected short tileFileSystemThreads = (short)8;
   protected String userAgentHttpHeader = "User-Agent";
   protected String userAgentValue = "osmdroid";

   public DefaultConfigurationProvider() {
      this.httpHeaderDateTimeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
      this.expirationAdder = 0L;
      this.expirationOverride = null;
      this.httpProxy = null;
      this.animationSpeedDefault = 1000;
      this.animationSpeedShort = 500;
      this.mapViewRecycler = true;
      this.cacheTileOvershoot = (short)0;
      this.mTileGCFrequencyInMillis = 300000L;
      this.mTileGCBulkSize = 20;
      this.mTileGCBulkPauseInMillis = 500L;
      this.mTileDownloaderFollowRedirects = true;
   }

   public Map getAdditionalHttpRequestProperties() {
      return this.mAdditionalHttpRequestProperties;
   }

   public int getAnimationSpeedDefault() {
      return this.animationSpeedDefault;
   }

   public int getAnimationSpeedShort() {
      return this.animationSpeedShort;
   }

   public short getCacheMapTileCount() {
      return this.cacheMapTileCount;
   }

   public short getCacheMapTileOvershoot() {
      return this.cacheTileOvershoot;
   }

   public long getExpirationExtendedDuration() {
      return this.expirationAdder;
   }

   public Long getExpirationOverrideDuration() {
      return this.expirationOverride;
   }

   public long getGpsWaitTime() {
      return this.gpsWaitTime;
   }

   public SimpleDateFormat getHttpHeaderDateTimeFormat() {
      return this.httpHeaderDateTimeFormat;
   }

   public Proxy getHttpProxy() {
      return this.httpProxy;
   }

   public String getNormalizedUserAgent() {
      return this.mNormalizedUserAgent;
   }

   public File getOsmdroidBasePath() {
      if (this.osmdroidBasePath == null) {
         this.osmdroidBasePath = new File(StorageUtils.getStorage().getAbsolutePath(), "osmdroid");
      }

      try {
         this.osmdroidBasePath.mkdirs();
      } catch (Exception var3) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unable to create base path at ");
         var2.append(this.osmdroidBasePath.getAbsolutePath());
         Log.d("OsmDroid", var2.toString(), var3);
      }

      return this.osmdroidBasePath;
   }

   public File getOsmdroidTileCache() {
      if (this.osmdroidTileCache == null) {
         this.osmdroidTileCache = new File(this.getOsmdroidBasePath(), "tiles");
      }

      try {
         this.osmdroidTileCache.mkdirs();
      } catch (Exception var3) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unable to create tile cache path at ");
         var2.append(this.osmdroidTileCache.getAbsolutePath());
         Log.d("OsmDroid", var2.toString(), var3);
      }

      return this.osmdroidTileCache;
   }

   public short getTileDownloadMaxQueueSize() {
      return this.tileDownloadMaxQueueSize;
   }

   public short getTileDownloadThreads() {
      return this.tileDownloadThreads;
   }

   public long getTileFileSystemCacheMaxBytes() {
      return this.tileFileSystemCacheMaxBytes;
   }

   public long getTileFileSystemCacheTrimBytes() {
      return this.tileFileSystemCacheTrimBytes;
   }

   public short getTileFileSystemMaxQueueSize() {
      return this.tileFileSystemMaxQueueSize;
   }

   public short getTileFileSystemThreads() {
      return this.tileFileSystemThreads;
   }

   public long getTileGCBulkPauseInMillis() {
      return this.mTileGCBulkPauseInMillis;
   }

   public int getTileGCBulkSize() {
      return this.mTileGCBulkSize;
   }

   public long getTileGCFrequencyInMillis() {
      return this.mTileGCFrequencyInMillis;
   }

   public String getUserAgentHttpHeader() {
      return this.userAgentHttpHeader;
   }

   public String getUserAgentValue() {
      return this.userAgentValue;
   }

   public boolean isDebugMapTileDownloader() {
      return this.debugMapTileDownloader;
   }

   public boolean isDebugMapView() {
      return this.debugMapView;
   }

   public boolean isDebugMode() {
      return this.debugMode;
   }

   public boolean isDebugTileProviders() {
      return this.debugTileProviders;
   }

   public boolean isMapTileDownloaderFollowRedirects() {
      return this.mTileDownloaderFollowRedirects;
   }

   public boolean isMapViewHardwareAccelerated() {
      return this.isMapViewHardwareAccelerated;
   }

   public boolean isMapViewRecyclerFriendly() {
      return this.mapViewRecycler;
   }

   public void setUserAgentValue(String var1) {
      this.userAgentValue = var1;
   }
}
