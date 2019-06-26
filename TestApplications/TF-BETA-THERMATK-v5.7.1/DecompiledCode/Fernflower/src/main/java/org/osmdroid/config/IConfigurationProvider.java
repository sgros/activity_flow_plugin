package org.osmdroid.config;

import java.io.File;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Map;

public interface IConfigurationProvider {
   Map getAdditionalHttpRequestProperties();

   int getAnimationSpeedDefault();

   int getAnimationSpeedShort();

   short getCacheMapTileCount();

   short getCacheMapTileOvershoot();

   long getExpirationExtendedDuration();

   Long getExpirationOverrideDuration();

   long getGpsWaitTime();

   SimpleDateFormat getHttpHeaderDateTimeFormat();

   Proxy getHttpProxy();

   String getNormalizedUserAgent();

   File getOsmdroidBasePath();

   File getOsmdroidTileCache();

   short getTileDownloadMaxQueueSize();

   short getTileDownloadThreads();

   long getTileFileSystemCacheMaxBytes();

   long getTileFileSystemCacheTrimBytes();

   short getTileFileSystemMaxQueueSize();

   short getTileFileSystemThreads();

   long getTileGCBulkPauseInMillis();

   int getTileGCBulkSize();

   long getTileGCFrequencyInMillis();

   String getUserAgentHttpHeader();

   String getUserAgentValue();

   boolean isDebugMapTileDownloader();

   boolean isDebugMapView();

   boolean isDebugMode();

   boolean isDebugTileProviders();

   boolean isMapTileDownloaderFollowRedirects();

   boolean isMapViewHardwareAccelerated();

   boolean isMapViewRecyclerFriendly();

   void setUserAgentValue(String var1);
}
