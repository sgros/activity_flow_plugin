// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.os.Bundle;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.SQLite.SQLiteDatabase;
import java.util.Collection;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.SQLite.SQLiteCursor;
import android.text.TextUtils;
import org.telegram.tgnet.AbstractSerializedData;
import android.content.Intent;
import android.location.LocationListener;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.util.SparseIntArray;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import android.location.LocationManager;
import android.location.Location;
import android.util.LongSparseArray;

public class LocationController implements NotificationCenterDelegate
{
    private static final int BACKGROUD_UPDATE_TIME = 90000;
    private static final int FOREGROUND_UPDATE_TIME = 20000;
    private static volatile LocationController[] Instance;
    private static final int LOCATION_ACQUIRE_TIME = 10000;
    private static final double eps = 1.0E-4;
    private LongSparseArray<Boolean> cacheRequests;
    private int currentAccount;
    private GpsLocationListener gpsLocationListener;
    private Location lastKnownLocation;
    private boolean lastLocationByGoogleMaps;
    private long lastLocationSendTime;
    private long lastLocationStartTime;
    private LocationManager locationManager;
    private boolean locationSentSinceLastGoogleMapUpdate;
    public LongSparseArray<ArrayList<TLRPC.Message>> locationsCache;
    private GpsLocationListener networkLocationListener;
    private GpsLocationListener passiveLocationListener;
    private SparseIntArray requests;
    private ArrayList<SharingLocationInfo> sharingLocations;
    private LongSparseArray<SharingLocationInfo> sharingLocationsMap;
    private LongSparseArray<SharingLocationInfo> sharingLocationsMapUI;
    public ArrayList<SharingLocationInfo> sharingLocationsUI;
    private boolean started;
    
    static {
        LocationController.Instance = new LocationController[3];
    }
    
    public LocationController(final int currentAccount) {
        this.sharingLocationsMap = (LongSparseArray<SharingLocationInfo>)new LongSparseArray();
        this.sharingLocations = new ArrayList<SharingLocationInfo>();
        this.locationsCache = (LongSparseArray<ArrayList<TLRPC.Message>>)new LongSparseArray();
        this.gpsLocationListener = new GpsLocationListener();
        this.networkLocationListener = new GpsLocationListener();
        this.passiveLocationListener = new GpsLocationListener();
        this.locationSentSinceLastGoogleMapUpdate = true;
        this.requests = new SparseIntArray();
        this.cacheRequests = (LongSparseArray<Boolean>)new LongSparseArray();
        this.sharingLocationsUI = new ArrayList<SharingLocationInfo>();
        this.sharingLocationsMapUI = (LongSparseArray<SharingLocationInfo>)new LongSparseArray();
        this.currentAccount = currentAccount;
        this.locationManager = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
        AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$jwDhs2Wxth9unque4gfUrSG9YJ8(this));
        this.loadSharingLocations();
    }
    
    private void broadcastLastKnownLocation() {
        if (this.lastKnownLocation == null) {
            return;
        }
        if (this.requests.size() != 0) {
            for (int i = 0; i < this.requests.size(); ++i) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.requests.keyAt(i), false);
            }
            this.requests.clear();
        }
        final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        for (int j = 0; j < this.sharingLocations.size(); ++j) {
            final SharingLocationInfo sharingLocationInfo = this.sharingLocations.get(j);
            final TLRPC.Message messageOwner = sharingLocationInfo.messageObject.messageOwner;
            final TLRPC.MessageMedia media = messageOwner.media;
            if (media != null && media.geo != null) {
                int n = messageOwner.edit_date;
                if (n == 0) {
                    n = messageOwner.date;
                }
                final TLRPC.GeoPoint geo = sharingLocationInfo.messageObject.messageOwner.media.geo;
                if (Math.abs(currentTime - n) < 30 && Math.abs(geo.lat - this.lastKnownLocation.getLatitude()) <= 1.0E-4 && Math.abs(geo._long - this.lastKnownLocation.getLongitude()) <= 1.0E-4) {
                    continue;
                }
            }
            final TLRPC.TL_messages_editMessage tl_messages_editMessage = new TLRPC.TL_messages_editMessage();
            tl_messages_editMessage.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)sharingLocationInfo.did);
            tl_messages_editMessage.id = sharingLocationInfo.mid;
            tl_messages_editMessage.flags |= 0x4000;
            tl_messages_editMessage.media = new TLRPC.TL_inputMediaGeoLive();
            final TLRPC.InputMedia media2 = tl_messages_editMessage.media;
            media2.stopped = false;
            media2.geo_point = new TLRPC.TL_inputGeoPoint();
            tl_messages_editMessage.media.geo_point.lat = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLatitude());
            tl_messages_editMessage.media.geo_point._long = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLongitude());
            final int[] array = { 0 };
            array[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_editMessage, new _$$Lambda$LocationController$GgGvUosGIle_dcnPqBDWFGDVM7A(this, sharingLocationInfo, array));
            this.requests.put(array[0], 0);
        }
        ConnectionsManager.getInstance(this.currentAccount).resumeNetworkMaybe();
        this.stop(false);
    }
    
    public static LocationController getInstance(final int n) {
        final LocationController locationController;
        if ((locationController = LocationController.Instance[n]) == null) {
            synchronized (LocationController.class) {
                if (LocationController.Instance[n] == null) {
                    LocationController.Instance[n] = new LocationController(n);
                }
            }
        }
        return locationController;
    }
    
    public static int getLocationsCount() {
        int i = 0;
        int n = 0;
        while (i < 3) {
            n += getInstance(i).sharingLocationsUI.size();
            ++i;
        }
        return n;
    }
    
    private void loadSharingLocations() {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$LocationController$CedxaXVEsRh3MfbiwLY0cCEiBNY(this));
    }
    
    private void saveSharingLocation(final SharingLocationInfo sharingLocationInfo, final int n) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$LocationController$yQY9F3qag_AziWeeanO81WIpkbw(this, n, sharingLocationInfo));
    }
    
    private void start() {
        if (this.started) {
            return;
        }
        this.lastLocationStartTime = System.currentTimeMillis();
        this.started = true;
        try {
            this.locationManager.requestLocationUpdates("gps", 1L, 0.0f, (LocationListener)this.gpsLocationListener);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            this.locationManager.requestLocationUpdates("network", 1L, 0.0f, (LocationListener)this.networkLocationListener);
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        try {
            this.locationManager.requestLocationUpdates("passive", 1L, 0.0f, (LocationListener)this.passiveLocationListener);
        }
        catch (Exception ex3) {
            FileLog.e(ex3);
        }
        if (this.lastKnownLocation == null) {
            try {
                this.lastKnownLocation = this.locationManager.getLastKnownLocation("gps");
                if (this.lastKnownLocation == null) {
                    this.lastKnownLocation = this.locationManager.getLastKnownLocation("network");
                }
            }
            catch (Exception ex4) {
                FileLog.e(ex4);
            }
        }
    }
    
    private void startService() {
        try {
            ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class)LocationSharingService.class));
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
    }
    
    private void stop(final boolean b) {
        this.started = false;
        this.locationManager.removeUpdates((LocationListener)this.gpsLocationListener);
        if (b) {
            this.locationManager.removeUpdates((LocationListener)this.networkLocationListener);
            this.locationManager.removeUpdates((LocationListener)this.passiveLocationListener);
        }
    }
    
    private void stopService() {
        ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class)LocationSharingService.class));
    }
    
    protected void addSharingLocation(final long did, final int mid, final int period, final TLRPC.Message message) {
        final SharingLocationInfo e = new SharingLocationInfo();
        e.did = did;
        e.mid = mid;
        e.period = period;
        e.messageObject = new MessageObject(this.currentAccount, message, false);
        e.stopTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + period;
        final SharingLocationInfo o = (SharingLocationInfo)this.sharingLocationsMap.get(did);
        this.sharingLocationsMap.put(did, (Object)e);
        if (o != null) {
            this.sharingLocations.remove(o);
        }
        this.sharingLocations.add(e);
        this.saveSharingLocation(e, 0);
        this.lastLocationSendTime = System.currentTimeMillis() - 90000L + 5000L;
        AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$zAJ9cmnQja1jAmGuw23_E99HA_0(this, o, e));
    }
    
    public void cleanup() {
        this.sharingLocationsUI.clear();
        this.sharingLocationsMapUI.clear();
        this.locationsCache.clear();
        this.cacheRequests.clear();
        this.stopService();
        Utilities.stageQueue.postRunnable(new _$$Lambda$LocationController$CqwMtWkVqaOCz_mvGUr_tzsuYCI(this));
    }
    
    @Override
    public void didReceivedNotification(int i, int j, final Object... array) {
        j = NotificationCenter.didReceiveNewMessages;
        final int n = 0;
        if (i == j) {
            final long longValue = (long)array[0];
            if (!this.isSharingLocation(longValue)) {
                return;
            }
            final ArrayList list = (ArrayList)this.locationsCache.get(longValue);
            if (list == null) {
                return;
            }
            final ArrayList list2 = (ArrayList)array[1];
            i = 0;
            j = 0;
            while (i < list2.size()) {
                final MessageObject messageObject = list2.get(i);
                Label_0171: {
                    if (messageObject.isLiveLocation()) {
                        while (true) {
                            int from_id;
                            TLRPC.Message messageOwner;
                            for (j = 0; j < list.size(); ++j) {
                                from_id = list.get(j).from_id;
                                messageOwner = messageObject.messageOwner;
                                if (from_id == messageOwner.from_id) {
                                    list.set(j, messageOwner);
                                    j = 1;
                                    if (j == 0) {
                                        list.add(messageObject.messageOwner);
                                    }
                                    j = 1;
                                    break Label_0171;
                                }
                            }
                            j = 0;
                            continue;
                        }
                    }
                }
                ++i;
            }
            if (j != 0) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsCacheChanged, longValue, this.currentAccount);
            }
        }
        else if (i == NotificationCenter.messagesDeleted) {
            if (!this.sharingLocationsUI.isEmpty()) {
                final ArrayList list3 = (ArrayList)array[0];
                final int intValue = (int)array[1];
                ArrayList<Long> list4 = null;
                SharingLocationInfo sharingLocationInfo;
                MessageObject messageObject2;
                ArrayList<Long> list5;
                for (i = 0; i < this.sharingLocationsUI.size(); ++i, list4 = list5) {
                    sharingLocationInfo = this.sharingLocationsUI.get(i);
                    messageObject2 = sharingLocationInfo.messageObject;
                    if (messageObject2 != null) {
                        j = messageObject2.getChannelId();
                    }
                    else {
                        j = 0;
                    }
                    if (intValue != j) {
                        list5 = list4;
                    }
                    else {
                        list5 = list4;
                        if (list3.contains(sharingLocationInfo.mid)) {
                            if ((list5 = list4) == null) {
                                list5 = new ArrayList<Long>();
                            }
                            list5.add(sharingLocationInfo.did);
                        }
                    }
                }
                if (list4 != null) {
                    for (i = n; i < list4.size(); ++i) {
                        this.removeSharingLocation(list4.get(i));
                    }
                }
            }
        }
        else if (i == NotificationCenter.replaceMessagesObjects) {
            final long longValue2 = (long)array[0];
            if (!this.isSharingLocation(longValue2)) {
                return;
            }
            final ArrayList list6 = (ArrayList)this.locationsCache.get(longValue2);
            if (list6 == null) {
                return;
            }
            final ArrayList list7 = (ArrayList)array[1];
            i = 0;
            j = 0;
            while (i < list7.size()) {
                final MessageObject messageObject3 = list7.get(i);
                int index = 0;
                int n2;
                while (true) {
                    n2 = j;
                    if (index >= list6.size()) {
                        break;
                    }
                    if (list6.get(index).from_id == messageObject3.messageOwner.from_id) {
                        if (!messageObject3.isLiveLocation()) {
                            list6.remove(index);
                        }
                        else {
                            list6.set(index, messageObject3.messageOwner);
                        }
                        n2 = 1;
                        break;
                    }
                    ++index;
                }
                ++i;
                j = n2;
            }
            if (j != 0) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsCacheChanged, longValue2, this.currentAccount);
            }
        }
    }
    
    public SharingLocationInfo getSharingLocationInfo(final long n) {
        return (SharingLocationInfo)this.sharingLocationsMapUI.get(n);
    }
    
    public boolean isSharingLocation(final long n) {
        return this.sharingLocationsMapUI.indexOfKey(n) >= 0;
    }
    
    public void loadLiveLocations(final long n) {
        if (this.cacheRequests.indexOfKey(n) >= 0) {
            return;
        }
        this.cacheRequests.put(n, (Object)true);
        final TLRPC.TL_messages_getRecentLocations tl_messages_getRecentLocations = new TLRPC.TL_messages_getRecentLocations();
        tl_messages_getRecentLocations.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)n);
        tl_messages_getRecentLocations.limit = 100;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getRecentLocations, new _$$Lambda$LocationController$9e6Jr8NCwhd7x0jAE3itGEDvCZ8(this, n));
    }
    
    public void removeAllLocationSharings() {
        Utilities.stageQueue.postRunnable(new _$$Lambda$LocationController$unkueEB9icgZn5pQTky3flbGZuo(this));
    }
    
    public void removeSharingLocation(final long n) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$LocationController$__2b05V3C269L8B6e6NaqY7nkE0(this, n));
    }
    
    public void setGoogleMapLocation(final Location lastKnownLocation, final boolean b) {
        if (lastKnownLocation == null) {
            return;
        }
        this.lastLocationByGoogleMaps = true;
        Label_0084: {
            if (!b) {
                final Location lastKnownLocation2 = this.lastKnownLocation;
                if (lastKnownLocation2 == null || lastKnownLocation2.distanceTo(lastKnownLocation) < 20.0f) {
                    if (this.locationSentSinceLastGoogleMapUpdate) {
                        this.lastLocationSendTime = System.currentTimeMillis() - 90000L + 20000L;
                        this.locationSentSinceLastGoogleMapUpdate = false;
                    }
                    break Label_0084;
                }
            }
            this.lastLocationSendTime = System.currentTimeMillis() - 90000L;
            this.locationSentSinceLastGoogleMapUpdate = false;
        }
        this.lastKnownLocation = lastKnownLocation;
    }
    
    protected void update() {
        if (this.sharingLocations.isEmpty()) {
            return;
        }
        int n;
        for (int i = 0; i < this.sharingLocations.size(); i = n + 1) {
            final SharingLocationInfo sharingLocationInfo = this.sharingLocations.get(i);
            final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            n = i;
            if (sharingLocationInfo.stopTime <= currentTime) {
                this.sharingLocations.remove(i);
                this.sharingLocationsMap.remove(sharingLocationInfo.did);
                this.saveSharingLocation(sharingLocationInfo, 1);
                AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$PRv_6ObbwXmRgZBGVbIVGOjYQD8(this, sharingLocationInfo));
                n = i - 1;
            }
        }
        if (!this.started) {
            if (Math.abs(this.lastLocationSendTime - System.currentTimeMillis()) > 90000L) {
                this.lastLocationStartTime = System.currentTimeMillis();
                this.start();
            }
        }
        else if (this.lastLocationByGoogleMaps || Math.abs(this.lastLocationStartTime - System.currentTimeMillis()) > 10000L) {
            this.lastLocationByGoogleMaps = false;
            this.locationSentSinceLastGoogleMapUpdate = true;
            this.lastLocationSendTime = System.currentTimeMillis();
            this.broadcastLastKnownLocation();
        }
    }
    
    private class GpsLocationListener implements LocationListener
    {
        public void onLocationChanged(final Location location) {
            if (location == null) {
                return;
            }
            if (LocationController.this.lastKnownLocation != null && (this == LocationController.this.networkLocationListener || this == LocationController.this.passiveLocationListener)) {
                if (!LocationController.this.started && location.distanceTo(LocationController.this.lastKnownLocation) > 20.0f) {
                    LocationController.this.lastKnownLocation = location;
                    LocationController.this.lastLocationSendTime = System.currentTimeMillis() - 90000L + 5000L;
                }
            }
            else {
                LocationController.this.lastKnownLocation = location;
            }
        }
        
        public void onProviderDisabled(final String s) {
        }
        
        public void onProviderEnabled(final String s) {
        }
        
        public void onStatusChanged(final String s, final int n, final Bundle bundle) {
        }
    }
    
    public static class SharingLocationInfo
    {
        public long did;
        public MessageObject messageObject;
        public int mid;
        public int period;
        public int stopTime;
    }
}
