package locus.api.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.LocusInfo;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.LocusUtils.LocusVersion;
import locus.api.android.utils.LocusUtils.VersionCode;
import locus.api.android.utils.Utils;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.Storable;
import locus.api.objects.extra.ExtraData;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Track;
import locus.api.objects.extra.Waypoint;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class ActionTools {
    private static final String TAG = "ActionTools";

    public static class BitmapLoadResult extends Storable {
        private byte[] mImg;
        private int mNotYetLoadedTiles;

        private BitmapLoadResult(byte[] img, int notYetLoadedTiles) {
            this.mImg = img;
            this.mNotYetLoadedTiles = notYetLoadedTiles;
        }

        public boolean isValid() {
            return this.mImg != null;
        }

        public byte[] getImageB() {
            return this.mImg;
        }

        public Bitmap getImage() {
            return BitmapFactory.decodeByteArray(this.mImg, 0, this.mImg.length);
        }

        public int getNumOfNotYetLoadedTiles() {
            return this.mNotYetLoadedTiles;
        }

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 0;
        }

        public void reset() {
            this.mImg = null;
            this.mNotYetLoadedTiles = 0;
        }

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dr) throws IOException {
            int size = dr.readInt();
            if (size > 0) {
                this.mImg = new byte[size];
                dr.readBytes(this.mImg);
                this.mNotYetLoadedTiles = dr.readInt();
            }
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            if (this.mImg == null || this.mImg.length == 0) {
                dw.writeInt(0);
            } else {
                dw.writeInt(this.mImg.length);
                dw.write(this.mImg);
            }
            dw.writeInt(this.mNotYetLoadedTiles);
        }
    }

    public static class TrackRecordProfileSimple extends Storable {
        private String mDesc;
        private long mId;
        private byte[] mImg;
        private String mName;

        private TrackRecordProfileSimple(long id, String name, String desc, byte[] img) {
            this.mId = id;
            if (name == null) {
                name = "";
            }
            this.mName = name;
            if (desc == null) {
                desc = "";
            }
            this.mDesc = desc;
            this.mImg = img;
        }

        public long getId() {
            return this.mId;
        }

        public String getName() {
            return this.mName;
        }

        public String getDesc() {
            return this.mDesc;
        }

        public byte[] getIcon() {
            return this.mImg;
        }

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 0;
        }

        public void reset() {
            this.mId = 0;
            this.mName = "";
            this.mDesc = "";
            this.mImg = null;
        }

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dr) throws IOException {
            this.mId = dr.readLong();
            this.mName = dr.readString();
            this.mDesc = dr.readString();
            int imgSize = dr.readInt();
            if (imgSize > 0) {
                this.mImg = new byte[imgSize];
                dr.readBytes(this.mImg);
            }
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeLong(this.mId);
            dw.writeString(this.mName);
            dw.writeString(this.mDesc);
            int imgSize = this.mImg != null ? this.mImg.length : 0;
            dw.writeInt(imgSize);
            if (imgSize > 0) {
                dw.write(this.mImg);
            }
        }
    }

    public static void actionPickFile(Activity activity, int requestCode) throws ActivityNotFoundException {
        intentPick("org.openintents.action.PICK_FILE", activity, requestCode, null, null);
    }

    public static void actionPickFile(Activity activity, int requestCode, String title, String[] filter) throws ActivityNotFoundException {
        intentPick("org.openintents.action.PICK_FILE", activity, requestCode, title, filter);
    }

    public static void actionPickDir(Activity activity, int requestCode) throws ActivityNotFoundException {
        intentPick("org.openintents.action.PICK_DIRECTORY", activity, requestCode, null, null);
    }

    public static void actionPickDir(Activity activity, int requestCode, String title) throws ActivityNotFoundException {
        intentPick("org.openintents.action.PICK_DIRECTORY", activity, requestCode, title, null);
    }

    private static void intentPick(String action, Activity activity, int requestCode, String title, String[] filter) {
        Intent intent = new Intent(action);
        if (title != null && title.length() > 0) {
            intent.putExtra("org.openintents.extra.TITLE", title);
        }
        if (filter != null && filter.length > 0) {
            intent.putExtra("org.openintents.extra.FILTER", filter);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void actionPickLocation(Activity act) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusAvailable(act, 235, 235, 0)) {
            act.startActivity(new Intent(LocusConst.ACTION_PICK_LOCATION));
            return;
        }
        throw new RequiredVersionMissingException(235);
    }

    public static void actionStartNavigation(Activity act, String name, double latitude, double longitude) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusAvailable(act, VersionCode.UPDATE_01)) {
            Intent intent = new Intent(LocusConst.ACTION_NAVIGATION_START);
            if (name != null) {
                intent.putExtra(LocusConst.INTENT_EXTRA_NAME, name);
            }
            intent.putExtra(LocusConst.INTENT_EXTRA_LATITUDE, latitude);
            intent.putExtra(LocusConst.INTENT_EXTRA_LONGITUDE, longitude);
            act.startActivity(intent);
            return;
        }
        throw new RequiredVersionMissingException(VersionCode.UPDATE_01);
    }

    public static void actionStartNavigation(Activity act, Waypoint wpt) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusAvailable(act, VersionCode.UPDATE_01)) {
            Intent intent = new Intent(LocusConst.ACTION_NAVIGATION_START);
            LocusUtils.addWaypointToIntent(intent, wpt);
            act.startActivity(intent);
            return;
        }
        throw new RequiredVersionMissingException(VersionCode.UPDATE_01);
    }

    public static void actionStartNavigation(Activity act, String address) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusAvailable(act, VersionCode.UPDATE_08)) {
            Intent intent = new Intent(LocusConst.ACTION_NAVIGATION_START);
            intent.putExtra(LocusConst.INTENT_EXTRA_ADDRESS_TEXT, address);
            act.startActivity(intent);
            return;
        }
        throw new RequiredVersionMissingException(VersionCode.UPDATE_08);
    }

    public static void actionStartGuiding(Activity act, String name, double latitude, double longitude) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusAvailable(act, 243, 243, 0)) {
            Intent intent = new Intent(LocusConst.ACTION_GUIDING_START);
            if (name != null) {
                intent.putExtra(LocusConst.INTENT_EXTRA_NAME, name);
            }
            intent.putExtra(LocusConst.INTENT_EXTRA_LATITUDE, latitude);
            intent.putExtra(LocusConst.INTENT_EXTRA_LONGITUDE, longitude);
            act.startActivity(intent);
            return;
        }
        throw new RequiredVersionMissingException(243);
    }

    public static void actionStartGuiding(Activity act, Waypoint wpt) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusAvailable(act, 243, 243, 0)) {
            Intent intent = new Intent(LocusConst.ACTION_GUIDING_START);
            LocusUtils.addWaypointToIntent(intent, wpt);
            act.startActivity(intent);
            return;
        }
        throw new RequiredVersionMissingException(243);
    }

    public static Waypoint getLocusWaypoint(Context ctx, LocusVersion lv, long wptId) throws RequiredVersionMissingException {
        int minVersion = VersionCode.UPDATE_01.vcFree;
        if (LocusUtils.isLocusFreePro(lv, minVersion)) {
            Uri scheme = getContentProviderData(lv, VersionCode.UPDATE_01, LocusConst.CONTENT_PROVIDER_PATH_WAYPOINT);
            if (scheme != null) {
                Cursor cursor = ctx.getContentResolver().query(ContentUris.withAppendedId(scheme, wptId), null, null, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    Logger.logW(TAG, "getLocusWaypoint(" + ctx + ", " + wptId + "), " + "'cursor' in not valid");
                    return null;
                }
                Waypoint waypoint;
                try {
                    waypoint = new Waypoint(cursor.getBlob(1));
                    return waypoint;
                } catch (Exception e) {
                    waypoint = TAG;
                    Logger.logE(waypoint, "getLocusWaypoint(" + ctx + ", " + wptId + ")", e);
                    return null;
                } finally {
                    Utils.closeQuietly(cursor);
                }
            } else {
                throw new RequiredVersionMissingException(minVersion);
            }
        }
        throw new RequiredVersionMissingException(minVersion);
    }

    public static long[] getLocusWaypointId(Context ctx, LocusVersion lv, String wptName) throws RequiredVersionMissingException {
        int minVersion = VersionCode.UPDATE_03.vcFree;
        if (LocusUtils.isLocusFreePro(lv, minVersion)) {
            Uri scheme = getContentProviderData(lv, VersionCode.UPDATE_03, LocusConst.CONTENT_PROVIDER_PATH_WAYPOINT);
            if (scheme != null) {
                Cursor cursor = ctx.getContentResolver().query(scheme, null, "getWaypointId", new String[]{wptName}, null);
                long[] result = null;
                try {
                    result = new long[cursor.getCount()];
                    int m = result.length;
                    for (int i = 0; i < m; i++) {
                        cursor.moveToPosition(i);
                        result[i] = cursor.getLong(0);
                    }
                } catch (Exception e) {
                    Logger.logE(TAG, "getLocusWaypointId(" + ctx + ", " + wptName + ")", e);
                } finally {
                    Utils.closeQuietly(cursor);
                }
                return result;
            }
            throw new RequiredVersionMissingException(minVersion);
        }
        throw new RequiredVersionMissingException(minVersion);
    }

    public static int updateLocusWaypoint(Context context, LocusVersion lv, Waypoint wpt, boolean forceOverwrite) throws RequiredVersionMissingException {
        return updateLocusWaypoint(context, lv, wpt, forceOverwrite, false);
    }

    public static int updateLocusWaypoint(Context ctx, LocusVersion lv, Waypoint wpt, boolean forceOverwrite, boolean loadAllGcWaypoints) throws RequiredVersionMissingException {
        int minVersion = VersionCode.UPDATE_01.vcFree;
        if (LocusUtils.isLocusFreePro(lv, minVersion)) {
            Uri scheme = getContentProviderData(lv, VersionCode.UPDATE_01, LocusConst.CONTENT_PROVIDER_PATH_WAYPOINT);
            if (scheme != null) {
                ContentValues cv = new ContentValues();
                cv.put(LocusConst.CONTENT_PROVIDER_PATH_WAYPOINT, wpt.getAsBytes());
                cv.put("forceOverwrite", Boolean.valueOf(forceOverwrite));
                cv.put("loadAllGcWaypoints", Boolean.valueOf(loadAllGcWaypoints));
                return ctx.getContentResolver().update(scheme, cv, null, null);
            }
            throw new RequiredVersionMissingException(minVersion);
        }
        throw new RequiredVersionMissingException(minVersion);
    }

    public static void displayWaypointScreen(Context ctx, LocusVersion lv, long wptId) throws RequiredVersionMissingException {
        displayWaypointScreen(ctx, lv, wptId, "");
    }

    public static void displayWaypointScreen(Context ctx, LocusVersion lv, long wptId, String packageName, String className, String returnDataName, String returnDataValue) throws RequiredVersionMissingException {
        displayWaypointScreen(ctx, lv, wptId, ExtraData.generateCallbackString("", packageName, className, returnDataName, returnDataValue));
    }

    private static void displayWaypointScreen(Context ctx, LocusVersion lv, long wptId, String callback) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusFreePro(lv, VersionCode.UPDATE_07.vcFree)) {
            Intent intent = new Intent(LocusConst.ACTION_DISPLAY_POINT_SCREEN);
            intent.putExtra(LocusConst.INTENT_EXTRA_ITEM_ID, wptId);
            if (callback != null && callback.length() > 0) {
                intent.putExtra(Waypoint.TAG_EXTRA_CALLBACK, callback);
            }
            ctx.startActivity(intent);
            return;
        }
        throw new RequiredVersionMissingException(VersionCode.UPDATE_07);
    }

    public static Track getLocusTrack(Context ctx, LocusVersion lv, long trackId) throws RequiredVersionMissingException {
        int minVersion = VersionCode.UPDATE_10.vcFree;
        if (LocusUtils.isLocusFreePro(lv, minVersion)) {
            Uri scheme = getContentProviderData(lv, VersionCode.UPDATE_10, LocusConst.CONTENT_PROVIDER_PATH_TRACK);
            if (scheme != null) {
                Cursor cursor = ctx.getContentResolver().query(ContentUris.withAppendedId(scheme, trackId), null, null, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    Logger.logW(TAG, "getLocusTrack(" + ctx + ", " + trackId + "), " + "'cursor' in not valid");
                    return null;
                }
                Track track;
                try {
                    track = new Track(cursor.getBlob(1));
                    return track;
                } catch (Exception e) {
                    track = TAG;
                    Logger.logE(track, "getLocusTrack(" + ctx + ", " + trackId + ")", e);
                    return null;
                } finally {
                    Utils.closeQuietly(cursor);
                }
            } else {
                throw new RequiredVersionMissingException(minVersion);
            }
        }
        throw new RequiredVersionMissingException(minVersion);
    }

    public static void actionTrackRecordStart(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        actionTrackRecordStart(ctx, lv, null);
    }

    public static void actionTrackRecordStart(Context ctx, LocusVersion lv, String profileName) throws RequiredVersionMissingException {
        Intent intent = actionTrackRecord(LocusConst.ACTION_TRACK_RECORD_START, lv);
        if (profileName != null && profileName.length() > 0) {
            intent.putExtra(LocusConst.INTENT_EXTRA_TRACK_REC_PROFILE, profileName);
        }
        ctx.sendBroadcast(intent);
    }

    public static void actionTrackRecordPause(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        ctx.sendBroadcast(actionTrackRecord(LocusConst.ACTION_TRACK_RECORD_PAUSE, lv));
    }

    public static void actionTrackRecordStop(Context ctx, LocusVersion lv, boolean autoSave) throws RequiredVersionMissingException {
        Intent intent = actionTrackRecord(LocusConst.ACTION_TRACK_RECORD_STOP, lv);
        intent.putExtra(LocusConst.INTENT_EXTRA_TRACK_REC_AUTO_SAVE, autoSave);
        ctx.sendBroadcast(intent);
    }

    public static void actionTrackRecordAddWpt(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        actionTrackRecordAddWpt(ctx, lv, false);
    }

    public static void actionTrackRecordAddWpt(Context ctx, LocusVersion lv, boolean autoSave) throws RequiredVersionMissingException {
        actionTrackRecordAddWpt(ctx, lv, null, autoSave);
    }

    public static void actionTrackRecordAddWpt(Context ctx, LocusVersion lv, String wptName, boolean autoSave) throws RequiredVersionMissingException {
        Intent intent = actionTrackRecord(LocusConst.ACTION_TRACK_RECORD_ADD_WPT, lv);
        if (wptName != null && wptName.length() > 0) {
            intent.putExtra(LocusConst.INTENT_EXTRA_NAME, wptName);
        }
        intent.putExtra(LocusConst.INTENT_EXTRA_TRACK_REC_AUTO_SAVE, autoSave);
        ctx.sendBroadcast(intent);
    }

    public static void actionTrackRecordAddWpt(Context ctx, LocusVersion lv, String wptName, String actionAfter) throws RequiredVersionMissingException {
        Intent intent = actionTrackRecord(LocusConst.ACTION_TRACK_RECORD_ADD_WPT, lv);
        if (wptName != null && wptName.length() > 0) {
            intent.putExtra(LocusConst.INTENT_EXTRA_NAME, wptName);
        }
        intent.putExtra(LocusConst.INTENT_EXTRA_TRACK_REC_AUTO_SAVE, false);
        intent.putExtra(LocusConst.INTENT_EXTRA_TRACK_REC_ACTION_AFTER, actionAfter);
        ctx.sendBroadcast(intent);
    }

    private static Intent actionTrackRecord(String action, LocusVersion lv) throws RequiredVersionMissingException {
        int minVersion = VersionCode.UPDATE_02.vcFree;
        if (LocusUtils.isLocusFreePro(lv, minVersion)) {
            Intent intent = new Intent(action);
            intent.setPackage(lv.getPackageName());
            return intent;
        }
        throw new RequiredVersionMissingException(minVersion);
    }

    public static List<TrackRecordProfileSimple> getTrackRecordingProfiles(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        Uri scheme = getContentProviderData(lv, VersionCode.UPDATE_09, LocusConst.CONTENT_PROVIDER_PATH_TRACK_RECORD_PROFILE_NAMES);
        if (scheme != null) {
            Cursor cursor = ctx.getContentResolver().query(scheme, null, null, null, null);
            List<TrackRecordProfileSimple> profiles = new ArrayList();
            int i = 0;
            while (i < cursor.getCount()) {
                try {
                    cursor.moveToPosition(i);
                    profiles.add(new TrackRecordProfileSimple(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3)));
                    i++;
                } catch (Exception e) {
                    Logger.logE(TAG, "getItemPurchaseState(" + ctx + ", " + lv + ")", e);
                } finally {
                    Utils.closeQuietly(cursor);
                }
            }
            return profiles;
        }
        throw new RequiredVersionMissingException(VersionCode.UPDATE_09);
    }

    public static void callAddNewWmsMap(Context context, String wmsUrl) throws RequiredVersionMissingException, InvalidObjectException {
        if (!LocusUtils.isLocusAvailable(context, VersionCode.UPDATE_01)) {
            throw new RequiredVersionMissingException(VersionCode.UPDATE_01);
        } else if (TextUtils.isEmpty(wmsUrl)) {
            throw new InvalidObjectException("WMS Url address '" + wmsUrl + "', is not valid!");
        } else {
            Intent intent = new Intent(LocusConst.ACTION_ADD_NEW_WMS_MAP);
            intent.putExtra(LocusConst.INTENT_EXTRA_ADD_NEW_WMS_MAP_URL, wmsUrl);
            context.startActivity(intent);
        }
    }

    @Deprecated
    public static String getLocusRootDirectory(Context context) throws RequiredVersionMissingException {
        LocusInfo locusInfo = getLocusInfoData(context);
        if (locusInfo != null) {
            return locusInfo.getRootDirectory();
        }
        return null;
    }

    @Deprecated
    public static boolean isPeriodicUpdatesEnabled(Context context) throws RequiredVersionMissingException {
        LocusInfo locusInfo = getLocusInfoData(context);
        return locusInfo != null && locusInfo.isPeriodicUpdatesEnabled();
    }

    private static LocusInfo getLocusInfoData(Context ctx) throws RequiredVersionMissingException {
        return getLocusInfo(ctx, LocusUtils.createLocusVersion(ctx));
    }

    public static LocusInfo getLocusInfo(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        LocusInfo locusInfo = null;
        Uri scheme = getContentProviderData(lv, VersionCode.UPDATE_01, LocusConst.CONTENT_PROVIDER_PATH_INFO);
        if (scheme == null) {
            Logger.logD(TAG, "getLocusInfo(" + ctx + ", " + lv + "), invalid version");
            throw new RequiredVersionMissingException(VersionCode.UPDATE_01);
        }
        Cursor cursor = ctx.getContentResolver().query(scheme, locusInfo, locusInfo, locusInfo, locusInfo);
        try {
            locusInfo = LocusInfo.create(cursor);
        } catch (Exception e) {
            Logger.logE(TAG, "getLocusInfo(" + ctx + ", " + lv + ")", e);
        } finally {
            Utils.closeQuietly(cursor);
        }
        return locusInfo;
    }

    public static int getItemPurchaseState(Context ctx, LocusVersion lv, long itemId) throws RequiredVersionMissingException {
        Uri scheme = getContentProviderData(lv, VersionCode.UPDATE_06, LocusConst.CONTENT_PROVIDER_PATH_ITEM_PURCHASE_STATE);
        if (scheme != null) {
            Cursor cursor = ctx.getContentResolver().query(ContentUris.withAppendedId(scheme, itemId), null, null, null, null);
            int i = 0;
            while (i < cursor.getCount()) {
                int i2;
                try {
                    cursor.moveToPosition(i);
                    if (cursor.getString(0).equals("purchaseState")) {
                        i2 = cursor.getInt(1);
                        return i2;
                    }
                    i++;
                } catch (Exception e) {
                    i2 = TAG;
                    Logger.logE(i2, "getItemPurchaseState(" + ctx + ", " + lv + ")", e);
                } finally {
                    Utils.closeQuietly(cursor);
                }
            }
            Utils.closeQuietly(cursor);
            return 0;
        }
        throw new RequiredVersionMissingException(VersionCode.UPDATE_06);
    }

    public static void displayLocusStoreItemDetail(Context ctx, LocusVersion lv, long itemId) throws RequiredVersionMissingException {
        if (lv == null || !lv.isVersionValid(VersionCode.UPDATE_12)) {
            Logger.logW(TAG, "displayLocusStoreItemDetail(), invalid Locus version");
            throw new RequiredVersionMissingException(VersionCode.UPDATE_12);
        }
        Intent intent = new Intent(LocusConst.ACTION_DISPLAY_STORE_ITEM);
        intent.putExtra(LocusConst.INTENT_EXTRA_ITEM_ID, itemId);
        ctx.startActivity(intent);
    }

    public static BitmapLoadResult getMapPreview(Context ctx, LocusVersion lv, Location locCenter, int zoomValue, int widthPx, int heightPx, boolean tinyMode) throws RequiredVersionMissingException {
        Uri scheme = getContentProviderData(lv, VersionCode.UPDATE_04, "mapPreview");
        if (scheme == null) {
            throw new RequiredVersionMissingException(VersionCode.UPDATE_04);
        }
        BitmapLoadResult bitmapLoadResult;
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("lon=").append(locCenter.getLongitude()).append(",");
        sbQuery.append("lat=").append(locCenter.getLatitude()).append(",");
        sbQuery.append("zoom=").append(zoomValue).append(",");
        sbQuery.append("width=").append(widthPx).append(",");
        sbQuery.append("height=").append(heightPx).append(",");
        sbQuery.append("tinyMode=").append(tinyMode ? 1 : 0);
        Cursor cursor = ctx.getContentResolver().query(scheme, null, sbQuery.toString(), null, null);
        byte[] img = null;
        int notYetLoadedTiles = 0;
        int i = 0;
        while (i < cursor.getCount()) {
            try {
                cursor.moveToPosition(i);
                String key = new String(cursor.getBlob(0));
                byte[] data = cursor.getBlob(1);
                if (key.equals("mapPreview")) {
                    img = data;
                } else if (key.equals(LocusConst.VALUE_MAP_PREVIEW_MISSING_TILES)) {
                    notYetLoadedTiles = locus.api.utils.Utils.parseInt(new String(data));
                }
                i++;
            } catch (Exception e) {
                Logger.logE(TAG, "getMapPreview()", e);
                bitmapLoadResult = new BitmapLoadResult();
            } finally {
                Utils.closeQuietly(cursor);
            }
        }
        bitmapLoadResult = new BitmapLoadResult(img, notYetLoadedTiles);
        return bitmapLoadResult;
    }

    public static void enablePeriodicUpdatesReceiver(Context ctx, LocusVersion lv, Class<? extends BroadcastReceiver> receiver) throws RequiredVersionMissingException {
        Logger.logD(TAG, "enableReceiver(" + ctx + ")");
        ctx.getPackageManager().setComponentEnabledSetting(new ComponentName(ctx, receiver), 1, 1);
        refreshPeriodicUpdateListeners(ctx, lv);
    }

    public static void disablePeriodicUpdatesReceiver(Context ctx, LocusVersion lv, Class<? extends BroadcastReceiver> receiver) throws RequiredVersionMissingException {
        Logger.logD(TAG, "disableReceiver(" + ctx + ")");
        ctx.getPackageManager().setComponentEnabledSetting(new ComponentName(ctx, receiver), 2, 1);
        refreshPeriodicUpdateListeners(ctx, lv);
    }

    private static void refreshPeriodicUpdateListeners(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusFreePro(lv, VersionCode.UPDATE_01.vcFree)) {
            Intent intent = new Intent(LocusConst.ACTION_REFRESH_PERIODIC_UPDATE_LISTENERS);
            intent.setPackage(lv.getPackageName());
            ctx.sendBroadcast(intent);
            return;
        }
        throw new RequiredVersionMissingException(VersionCode.UPDATE_01);
    }

    private static Uri getContentProviderData(LocusVersion lv, VersionCode vc, String path) {
        return getContentProviderUri(lv, vc, LocusConst.CONTENT_PROVIDER_AUTHORITY_DATA, path);
    }

    public static Uri getContentProviderGeocaching(LocusVersion lv, VersionCode vc, String path) {
        return getContentProviderUri(lv, vc, LocusConst.CONTENT_PROVIDER_AUTHORITY_GEOCACHING, path);
    }

    private static Uri getContentProviderUri(LocusVersion lv, VersionCode vc, String provider, String path) {
        if (provider == null || provider.length() == 0 || path == null || path.length() == 0) {
            Logger.logW(TAG, "getContentProviderUri(), invalid 'authority' or 'path'parameters");
            return null;
        } else if (lv == null || vc == null || !lv.isVersionValid(vc)) {
            Logger.logW(TAG, "getContentProviderUri(), invalid Locus version");
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            if (lv.isVersionFree()) {
                sb.append("content://menion.android.locus.free");
            } else if (lv.isVersionPro()) {
                sb.append("content://menion.android.locus.pro");
            } else if (lv.isVersionGis()) {
                sb.append("content://menion.android.locus.gis");
            } else {
                Logger.logW(TAG, "getContentProviderUri(), unknown Locus version:" + lv);
                return null;
            }
            return Uri.parse(sb.append(".").append(provider).append("/").append(path).toString());
        }
    }
}
