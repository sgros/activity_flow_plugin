// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android;

import locus.api.utils.DataWriterBigEndian;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import locus.api.objects.Storable;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Track;
import locus.api.android.utils.LocusInfo;
import android.database.Cursor;
import locus.api.android.utils.Utils;
import android.content.ContentUris;
import android.net.Uri;
import locus.api.objects.extra.ExtraData;
import android.content.ComponentName;
import locus.api.utils.Logger;
import android.content.BroadcastReceiver;
import java.io.InvalidObjectException;
import android.text.TextUtils;
import locus.api.objects.extra.Waypoint;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import android.content.Intent;
import android.content.Context;
import locus.api.android.utils.LocusUtils;
import android.content.ActivityNotFoundException;
import android.app.Activity;

public class ActionTools
{
    private static final String TAG = "ActionTools";
    
    public static void actionPickDir(final Activity activity, final int n) throws ActivityNotFoundException {
        intentPick("org.openintents.action.PICK_DIRECTORY", activity, n, null, null);
    }
    
    public static void actionPickDir(final Activity activity, final int n, final String s) throws ActivityNotFoundException {
        intentPick("org.openintents.action.PICK_DIRECTORY", activity, n, s, null);
    }
    
    public static void actionPickFile(final Activity activity, final int n) throws ActivityNotFoundException {
        intentPick("org.openintents.action.PICK_FILE", activity, n, null, null);
    }
    
    public static void actionPickFile(final Activity activity, final int n, final String s, final String[] array) throws ActivityNotFoundException {
        intentPick("org.openintents.action.PICK_FILE", activity, n, s, array);
    }
    
    public static void actionPickLocation(final Activity activity) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusAvailable((Context)activity, 235, 235, 0)) {
            activity.startActivity(new Intent("locus.api.android.ACTION_PICK_LOCATION"));
            return;
        }
        throw new RequiredVersionMissingException(235);
    }
    
    public static void actionStartGuiding(final Activity activity, final String s, final double n, final double n2) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusAvailable((Context)activity, 243, 243, 0)) {
            final Intent intent = new Intent("locus.api.android.ACTION_GUIDING_START");
            if (s != null) {
                intent.putExtra("INTENT_EXTRA_NAME", s);
            }
            intent.putExtra("INTENT_EXTRA_LATITUDE", n);
            intent.putExtra("INTENT_EXTRA_LONGITUDE", n2);
            activity.startActivity(intent);
            return;
        }
        throw new RequiredVersionMissingException(243);
    }
    
    public static void actionStartGuiding(final Activity activity, final Waypoint waypoint) throws RequiredVersionMissingException {
        if (LocusUtils.isLocusAvailable((Context)activity, 243, 243, 0)) {
            final Intent intent = new Intent("locus.api.android.ACTION_GUIDING_START");
            LocusUtils.addWaypointToIntent(intent, waypoint);
            activity.startActivity(intent);
            return;
        }
        throw new RequiredVersionMissingException(243);
    }
    
    public static void actionStartNavigation(final Activity activity, final String s) throws RequiredVersionMissingException {
        if (!LocusUtils.isLocusAvailable((Context)activity, LocusUtils.VersionCode.UPDATE_08)) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_08);
        }
        final Intent intent = new Intent("locus.api.android.ACTION_NAVIGATION_START");
        intent.putExtra("INTENT_EXTRA_ADDRESS_TEXT", s);
        activity.startActivity(intent);
    }
    
    public static void actionStartNavigation(final Activity activity, final String s, final double n, final double n2) throws RequiredVersionMissingException {
        if (!LocusUtils.isLocusAvailable((Context)activity, LocusUtils.VersionCode.UPDATE_01)) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
        }
        final Intent intent = new Intent("locus.api.android.ACTION_NAVIGATION_START");
        if (s != null) {
            intent.putExtra("INTENT_EXTRA_NAME", s);
        }
        intent.putExtra("INTENT_EXTRA_LATITUDE", n);
        intent.putExtra("INTENT_EXTRA_LONGITUDE", n2);
        activity.startActivity(intent);
    }
    
    public static void actionStartNavigation(final Activity activity, final Waypoint waypoint) throws RequiredVersionMissingException {
        if (!LocusUtils.isLocusAvailable((Context)activity, LocusUtils.VersionCode.UPDATE_01)) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
        }
        final Intent intent = new Intent("locus.api.android.ACTION_NAVIGATION_START");
        LocusUtils.addWaypointToIntent(intent, waypoint);
        activity.startActivity(intent);
    }
    
    private static Intent actionTrackRecord(final String s, final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        final int vcFree = LocusUtils.VersionCode.UPDATE_02.vcFree;
        if (!LocusUtils.isLocusFreePro(locusVersion, vcFree)) {
            throw new RequiredVersionMissingException(vcFree);
        }
        final Intent intent = new Intent(s);
        intent.setPackage(locusVersion.getPackageName());
        return intent;
    }
    
    public static void actionTrackRecordAddWpt(final Context context, final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        actionTrackRecordAddWpt(context, locusVersion, false);
    }
    
    public static void actionTrackRecordAddWpt(final Context context, final LocusUtils.LocusVersion locusVersion, final String s, final String s2) throws RequiredVersionMissingException {
        final Intent actionTrackRecord = actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_ADD_WPT", locusVersion);
        if (s != null && s.length() > 0) {
            actionTrackRecord.putExtra("INTENT_EXTRA_NAME", s);
        }
        actionTrackRecord.putExtra("INTENT_EXTRA_TRACK_REC_AUTO_SAVE", false);
        actionTrackRecord.putExtra("INTENT_EXTRA_TRACK_REC_ACTION_AFTER", s2);
        context.sendBroadcast(actionTrackRecord);
    }
    
    public static void actionTrackRecordAddWpt(final Context context, final LocusUtils.LocusVersion locusVersion, final String s, final boolean b) throws RequiredVersionMissingException {
        final Intent actionTrackRecord = actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_ADD_WPT", locusVersion);
        if (s != null && s.length() > 0) {
            actionTrackRecord.putExtra("INTENT_EXTRA_NAME", s);
        }
        actionTrackRecord.putExtra("INTENT_EXTRA_TRACK_REC_AUTO_SAVE", b);
        context.sendBroadcast(actionTrackRecord);
    }
    
    public static void actionTrackRecordAddWpt(final Context context, final LocusUtils.LocusVersion locusVersion, final boolean b) throws RequiredVersionMissingException {
        actionTrackRecordAddWpt(context, locusVersion, null, b);
    }
    
    public static void actionTrackRecordPause(final Context context, final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        context.sendBroadcast(actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_PAUSE", locusVersion));
    }
    
    public static void actionTrackRecordStart(final Context context, final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        actionTrackRecordStart(context, locusVersion, null);
    }
    
    public static void actionTrackRecordStart(final Context context, final LocusUtils.LocusVersion locusVersion, final String s) throws RequiredVersionMissingException {
        final Intent actionTrackRecord = actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_START", locusVersion);
        if (s != null && s.length() > 0) {
            actionTrackRecord.putExtra("INTENT_EXTRA_TRACK_REC_PROFILE", s);
        }
        context.sendBroadcast(actionTrackRecord);
    }
    
    public static void actionTrackRecordStop(final Context context, final LocusUtils.LocusVersion locusVersion, final boolean b) throws RequiredVersionMissingException {
        final Intent actionTrackRecord = actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_STOP", locusVersion);
        actionTrackRecord.putExtra("INTENT_EXTRA_TRACK_REC_AUTO_SAVE", b);
        context.sendBroadcast(actionTrackRecord);
    }
    
    public static void callAddNewWmsMap(final Context context, final String str) throws RequiredVersionMissingException, InvalidObjectException {
        if (!LocusUtils.isLocusAvailable(context, LocusUtils.VersionCode.UPDATE_01)) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
        }
        if (TextUtils.isEmpty((CharSequence)str)) {
            throw new InvalidObjectException("WMS Url address '" + str + "', is not valid!");
        }
        final Intent intent = new Intent("locus.api.android.ACTION_ADD_NEW_WMS_MAP");
        intent.putExtra("INTENT_EXTRA_ADD_NEW_WMS_MAP_URL", str);
        context.startActivity(intent);
    }
    
    public static void disablePeriodicUpdatesReceiver(final Context obj, final LocusUtils.LocusVersion locusVersion, final Class<? extends BroadcastReceiver> clazz) throws RequiredVersionMissingException {
        Logger.logD("ActionTools", "disableReceiver(" + obj + ")");
        obj.getPackageManager().setComponentEnabledSetting(new ComponentName(obj, (Class)clazz), 2, 1);
        refreshPeriodicUpdateListeners(obj, locusVersion);
    }
    
    public static void displayLocusStoreItemDetail(final Context context, final LocusUtils.LocusVersion locusVersion, final long n) throws RequiredVersionMissingException {
        if (locusVersion == null || !locusVersion.isVersionValid(LocusUtils.VersionCode.UPDATE_12)) {
            Logger.logW("ActionTools", "displayLocusStoreItemDetail(), invalid Locus version");
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_12);
        }
        final Intent intent = new Intent("locus.api.android.ACTION_DISPLAY_STORE_ITEM");
        intent.putExtra("INTENT_EXTRA_ITEM_ID", n);
        context.startActivity(intent);
    }
    
    public static void displayWaypointScreen(final Context context, final LocusUtils.LocusVersion locusVersion, final long n) throws RequiredVersionMissingException {
        displayWaypointScreen(context, locusVersion, n, "");
    }
    
    private static void displayWaypointScreen(final Context context, final LocusUtils.LocusVersion locusVersion, final long n, final String s) throws RequiredVersionMissingException {
        if (!LocusUtils.isLocusFreePro(locusVersion, LocusUtils.VersionCode.UPDATE_07.vcFree)) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_07);
        }
        final Intent intent = new Intent("locus.api.android.ACTION_DISPLAY_POINT_SCREEN");
        intent.putExtra("INTENT_EXTRA_ITEM_ID", n);
        if (s != null && s.length() > 0) {
            intent.putExtra("TAG_EXTRA_CALLBACK", s);
        }
        context.startActivity(intent);
    }
    
    public static void displayWaypointScreen(final Context context, final LocusUtils.LocusVersion locusVersion, final long n, final String s, final String s2, final String s3, final String s4) throws RequiredVersionMissingException {
        displayWaypointScreen(context, locusVersion, n, ExtraData.generateCallbackString("", s, s2, s3, s4));
    }
    
    public static void enablePeriodicUpdatesReceiver(final Context obj, final LocusUtils.LocusVersion locusVersion, final Class<? extends BroadcastReceiver> clazz) throws RequiredVersionMissingException {
        Logger.logD("ActionTools", "enableReceiver(" + obj + ")");
        obj.getPackageManager().setComponentEnabledSetting(new ComponentName(obj, (Class)clazz), 1, 1);
        refreshPeriodicUpdateListeners(obj, locusVersion);
    }
    
    private static Uri getContentProviderData(final LocusUtils.LocusVersion locusVersion, final LocusUtils.VersionCode versionCode, final String s) {
        return getContentProviderUri(locusVersion, versionCode, "LocusDataProvider", s);
    }
    
    public static Uri getContentProviderGeocaching(final LocusUtils.LocusVersion locusVersion, final LocusUtils.VersionCode versionCode, final String s) {
        return getContentProviderUri(locusVersion, versionCode, "GeocachingDataProvider", s);
    }
    
    private static Uri getContentProviderUri(final LocusUtils.LocusVersion obj, final LocusUtils.VersionCode versionCode, final String str, final String str2) {
        final Uri uri = null;
        Uri parse;
        if (str == null || str.length() == 0 || str2 == null || str2.length() == 0) {
            Logger.logW("ActionTools", "getContentProviderUri(), invalid 'authority' or 'path'parameters");
            parse = uri;
        }
        else if (obj == null || versionCode == null || !obj.isVersionValid(versionCode)) {
            Logger.logW("ActionTools", "getContentProviderUri(), invalid Locus version");
            parse = uri;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            if (obj.isVersionFree()) {
                sb.append("content://menion.android.locus.free");
            }
            else if (obj.isVersionPro()) {
                sb.append("content://menion.android.locus.pro");
            }
            else {
                if (!obj.isVersionGis()) {
                    Logger.logW("ActionTools", "getContentProviderUri(), unknown Locus version:" + obj);
                    parse = uri;
                    return parse;
                }
                sb.append("content://menion.android.locus.gis");
            }
            parse = Uri.parse(sb.append(".").append(str).append("/").append(str2).toString());
        }
        return parse;
    }
    
    public static int getItemPurchaseState(final Context obj, final LocusUtils.LocusVersion obj2, final long n) throws RequiredVersionMissingException {
        Object o = getContentProviderData(obj2, LocusUtils.VersionCode.UPDATE_06, "itemPurchaseState");
        Label_0100: {
            if (o == null) {
                break Label_0100;
            }
            o = obj.getContentResolver().query(ContentUris.withAppendedId((Uri)o, n), (String[])null, (String)null, (String[])null, (String)null);
            int i = 0;
            try {
                while (i < ((Cursor)o).getCount()) {
                    ((Cursor)o).moveToPosition(i);
                    if (((Cursor)o).getString(0).equals("purchaseState")) {
                        return ((Cursor)o).getInt(1);
                    }
                    ++i;
                }
                Utils.closeQuietly((Cursor)o);
                return 0;
                throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_06);
            }
            catch (Exception ex) {
                Logger.logE("ActionTools", "getItemPurchaseState(" + obj + ", " + obj2 + ")", ex);
                Utils.closeQuietly((Cursor)o);
                return 0;
            }
            finally {
                Utils.closeQuietly((Cursor)o);
            }
        }
    }
    
    public static LocusInfo getLocusInfo(Context create, final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        final Object o = null;
        final Uri contentProviderData = getContentProviderData(locusVersion, LocusUtils.VersionCode.UPDATE_01, "info");
        if (contentProviderData == null) {
            Logger.logD("ActionTools", "getLocusInfo(" + create + ", " + locusVersion + "), invalid version");
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
        }
        final Cursor query = ((Context)create).getContentResolver().query(contentProviderData, (String[])null, (String)null, (String[])null, (String)null);
        try {
            create = LocusInfo.create(query);
            return (LocusInfo)create;
        }
        catch (Exception ex) {
            Logger.logE("ActionTools", "getLocusInfo(" + create + ", " + locusVersion + ")", ex);
            Utils.closeQuietly(query);
            create = o;
            return (LocusInfo)create;
        }
        finally {
            Utils.closeQuietly(query);
        }
    }
    
    private static LocusInfo getLocusInfoData(final Context context) throws RequiredVersionMissingException {
        return getLocusInfo(context, LocusUtils.createLocusVersion(context));
    }
    
    @Deprecated
    public static String getLocusRootDirectory(final Context context) throws RequiredVersionMissingException {
        final LocusInfo locusInfoData = getLocusInfoData(context);
        String rootDirectory;
        if (locusInfoData != null) {
            rootDirectory = locusInfoData.getRootDirectory();
        }
        else {
            rootDirectory = null;
        }
        return rootDirectory;
    }
    
    public static Track getLocusTrack(Context context, final LocusUtils.LocusVersion locusVersion, final long n) throws RequiredVersionMissingException {
        final Track track = null;
        final int vcFree = LocusUtils.VersionCode.UPDATE_10.vcFree;
        if (!LocusUtils.isLocusFreePro(locusVersion, vcFree)) {
            throw new RequiredVersionMissingException(vcFree);
        }
        final Uri contentProviderData = getContentProviderData(locusVersion, LocusUtils.VersionCode.UPDATE_10, "track");
        if (contentProviderData != null) {
            final Cursor query = context.getContentResolver().query(ContentUris.withAppendedId(contentProviderData, n), (String[])null, (String)null, (String[])null, (String)null);
            Track track2 = null;
            if (query == null || !query.moveToFirst()) {
                Logger.logW("ActionTools", "getLocusTrack(" + context + ", " + n + "), " + "'cursor' in not valid");
                track2 = track;
            }
            else {
                try {
                    final Object o = new Track(query.getBlob(1));
                    Utils.closeQuietly(query);
                    context = (Context)o;
                }
                catch (Exception ex) {
                    Logger.logE("ActionTools", "getLocusTrack(" + context + ", " + n + ")", ex);
                    Utils.closeQuietly(query);
                }
                finally {
                    Utils.closeQuietly(query);
                }
            }
            return track2;
        }
        throw new RequiredVersionMissingException(vcFree);
    }
    
    public static Waypoint getLocusWaypoint(Context context, final LocusUtils.LocusVersion locusVersion, final long n) throws RequiredVersionMissingException {
        final Waypoint waypoint = null;
        final int vcFree = LocusUtils.VersionCode.UPDATE_01.vcFree;
        if (!LocusUtils.isLocusFreePro(locusVersion, vcFree)) {
            throw new RequiredVersionMissingException(vcFree);
        }
        final Uri contentProviderData = getContentProviderData(locusVersion, LocusUtils.VersionCode.UPDATE_01, "waypoint");
        if (contentProviderData != null) {
            final Cursor query = context.getContentResolver().query(ContentUris.withAppendedId(contentProviderData, n), (String[])null, (String)null, (String[])null, (String)null);
            Waypoint waypoint2 = null;
            if (query == null || !query.moveToFirst()) {
                Logger.logW("ActionTools", "getLocusWaypoint(" + context + ", " + n + "), " + "'cursor' in not valid");
                waypoint2 = waypoint;
            }
            else {
                try {
                    final Object o = new Waypoint(query.getBlob(1));
                    Utils.closeQuietly(query);
                    context = (Context)o;
                }
                catch (Exception ex) {
                    Logger.logE("ActionTools", "getLocusWaypoint(" + context + ", " + n + ")", ex);
                    Utils.closeQuietly(query);
                }
                finally {
                    Utils.closeQuietly(query);
                }
            }
            return waypoint2;
        }
        throw new RequiredVersionMissingException(vcFree);
    }
    
    public static long[] getLocusWaypointId(final Context obj, LocusUtils.LocusVersion contentProviderData, final String str) throws RequiredVersionMissingException {
        final int vcFree = LocusUtils.VersionCode.UPDATE_03.vcFree;
        if (!LocusUtils.isLocusFreePro(contentProviderData, vcFree)) {
            throw new RequiredVersionMissingException(vcFree);
        }
        contentProviderData = (LocusUtils.LocusVersion)getContentProviderData(contentProviderData, LocusUtils.VersionCode.UPDATE_03, "waypoint");
        Label_0124: {
            if (contentProviderData == null) {
                break Label_0124;
            }
            final Cursor query = obj.getContentResolver().query((Uri)contentProviderData, (String[])null, "getWaypointId", new String[] { str }, (String)null);
            contentProviderData = null;
            try {
                final Object o = new long[query.getCount()];
                int i = 0;
                contentProviderData = (LocusUtils.LocusVersion)o;
                while (i < o.length) {
                    contentProviderData = (LocusUtils.LocusVersion)o;
                    query.moveToPosition(i);
                    contentProviderData = (LocusUtils.LocusVersion)o;
                    o[i] = query.getLong(0);
                    ++i;
                }
                Utils.closeQuietly(query);
                contentProviderData = (LocusUtils.LocusVersion)o;
                return (long[])(Object)contentProviderData;
                throw new RequiredVersionMissingException(vcFree);
            }
            catch (Exception ex) {
                Logger.logE("ActionTools", "getLocusWaypointId(" + obj + ", " + str + ")", ex);
                Utils.closeQuietly(query);
                return (long[])(Object)contentProviderData;
            }
            finally {
                Utils.closeQuietly(query);
            }
        }
    }
    
    public static BitmapLoadResult getMapPreview(final Context context, LocusUtils.LocusVersion contentProviderData, Location bytes, int i, int int1, final int j, final boolean b) throws RequiredVersionMissingException {
        contentProviderData = (LocusUtils.LocusVersion)getContentProviderData(contentProviderData, LocusUtils.VersionCode.UPDATE_04, "mapPreview");
        if (contentProviderData == null) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_04);
        }
        Object query = new StringBuilder();
        ((StringBuilder)query).append("lon=").append(bytes.getLongitude()).append(",");
        ((StringBuilder)query).append("lat=").append(bytes.getLatitude()).append(",");
        ((StringBuilder)query).append("zoom=").append(i).append(",");
        ((StringBuilder)query).append("width=").append(int1).append(",");
        ((StringBuilder)query).append("height=").append(j).append(",");
        bytes = (Location)((StringBuilder)query).append("tinyMode=");
        Label_0253: {
            if (!b) {
                break Label_0253;
            }
            i = 1;
            while (true) {
                ((StringBuilder)bytes).append(i);
                query = context.getContentResolver().query((Uri)contentProviderData, (String[])null, ((StringBuilder)query).toString(), (String[])null, (String)null);
                Object o = null;
                int1 = 0;
                i = 0;
                try {
                    while (i < ((Cursor)query).getCount()) {
                        ((Cursor)query).moveToPosition(i);
                        final String s = new String(((Cursor)query).getBlob(0));
                        bytes = (Location)(Object)((Cursor)query).getBlob(1);
                        if (s.equals("mapPreview")) {
                            contentProviderData = (LocusUtils.LocusVersion)bytes;
                        }
                        else {
                            contentProviderData = (LocusUtils.LocusVersion)o;
                            if (s.equals("mapPreviewMissingTiles")) {
                                contentProviderData = (LocusUtils.LocusVersion)new String((byte[])(Object)bytes);
                                int1 = locus.api.utils.Utils.parseInt((String)contentProviderData);
                                contentProviderData = (LocusUtils.LocusVersion)o;
                            }
                        }
                        ++i;
                        o = contentProviderData;
                    }
                    return new BitmapLoadResult((byte[])o, int1);
                    i = 0;
                    continue;
                }
                catch (Exception ex) {
                    Logger.logE("ActionTools", "getMapPreview()", ex);
                    final BitmapLoadResult bitmapLoadResult = new BitmapLoadResult((byte[])null, 0);
                    Utils.closeQuietly((Cursor)query);
                    return bitmapLoadResult;
                }
                finally {
                    Utils.closeQuietly((Cursor)query);
                }
                break;
            }
        }
    }
    
    public static List<TrackRecordProfileSimple> getTrackRecordingProfiles(final Context obj, final LocusUtils.LocusVersion obj2) throws RequiredVersionMissingException {
        Object o = getContentProviderData(obj2, LocusUtils.VersionCode.UPDATE_09, "trackRecordProfileNames");
        Label_0113: {
            if (o == null) {
                break Label_0113;
            }
            o = obj.getContentResolver().query((Uri)o, (String[])null, (String)null, (String[])null, (String)null);
            final ArrayList<TrackRecordProfileSimple> list = new ArrayList<TrackRecordProfileSimple>();
            int i = 0;
            try {
                while (i < ((Cursor)o).getCount()) {
                    ((Cursor)o).moveToPosition(i);
                    list.add(new TrackRecordProfileSimple(((Cursor)o).getLong(0), ((Cursor)o).getString(1), ((Cursor)o).getString(2), ((Cursor)o).getBlob(3)));
                    ++i;
                }
                return list;
                throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_09);
            }
            catch (Exception ex) {
                Logger.logE("ActionTools", "getItemPurchaseState(" + obj + ", " + obj2 + ")", ex);
                Utils.closeQuietly((Cursor)o);
                return list;
            }
            finally {
                Utils.closeQuietly((Cursor)o);
            }
        }
    }
    
    private static void intentPick(final String s, final Activity activity, final int n, final String s2, final String[] array) {
        final Intent intent = new Intent(s);
        if (s2 != null && s2.length() > 0) {
            intent.putExtra("org.openintents.extra.TITLE", s2);
        }
        if (array != null && array.length > 0) {
            intent.putExtra("org.openintents.extra.FILTER", array);
        }
        activity.startActivityForResult(intent, n);
    }
    
    @Deprecated
    public static boolean isPeriodicUpdatesEnabled(final Context context) throws RequiredVersionMissingException {
        final LocusInfo locusInfoData = getLocusInfoData(context);
        return locusInfoData != null && locusInfoData.isPeriodicUpdatesEnabled();
    }
    
    private static void refreshPeriodicUpdateListeners(final Context context, final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        if (!LocusUtils.isLocusFreePro(locusVersion, LocusUtils.VersionCode.UPDATE_01.vcFree)) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
        }
        final Intent intent = new Intent("com.asamm.locus.ACTION_REFRESH_PERIODIC_UPDATE_LISTENERS");
        intent.setPackage(locusVersion.getPackageName());
        context.sendBroadcast(intent);
    }
    
    public static int updateLocusWaypoint(final Context context, final LocusUtils.LocusVersion locusVersion, final Waypoint waypoint, final boolean b) throws RequiredVersionMissingException {
        return updateLocusWaypoint(context, locusVersion, waypoint, b, false);
    }
    
    public static int updateLocusWaypoint(final Context context, final LocusUtils.LocusVersion locusVersion, final Waypoint waypoint, final boolean b, final boolean b2) throws RequiredVersionMissingException {
        final int vcFree = LocusUtils.VersionCode.UPDATE_01.vcFree;
        if (!LocusUtils.isLocusFreePro(locusVersion, vcFree)) {
            throw new RequiredVersionMissingException(vcFree);
        }
        final Uri contentProviderData = getContentProviderData(locusVersion, LocusUtils.VersionCode.UPDATE_01, "waypoint");
        if (contentProviderData != null) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put("waypoint", waypoint.getAsBytes());
            contentValues.put("forceOverwrite", Boolean.valueOf(b));
            contentValues.put("loadAllGcWaypoints", Boolean.valueOf(b2));
            return context.getContentResolver().update(contentProviderData, contentValues, (String)null, (String[])null);
        }
        throw new RequiredVersionMissingException(vcFree);
    }
    
    public static class BitmapLoadResult extends Storable
    {
        private byte[] mImg;
        private int mNotYetLoadedTiles;
        
        public BitmapLoadResult() {
        }
        
        private BitmapLoadResult(final byte[] mImg, final int mNotYetLoadedTiles) {
            this.mImg = mImg;
            this.mNotYetLoadedTiles = mNotYetLoadedTiles;
        }
        
        public Bitmap getImage() {
            return BitmapFactory.decodeByteArray(this.mImg, 0, this.mImg.length);
        }
        
        public byte[] getImageB() {
            return this.mImg;
        }
        
        public int getNumOfNotYetLoadedTiles() {
            return this.mNotYetLoadedTiles;
        }
        
        @Override
        protected int getVersion() {
            return 0;
        }
        
        public boolean isValid() {
            return this.mImg != null;
        }
        
        @Override
        protected void readObject(int int1, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            int1 = dataReaderBigEndian.readInt();
            if (int1 > 0) {
                dataReaderBigEndian.readBytes(this.mImg = new byte[int1]);
                this.mNotYetLoadedTiles = dataReaderBigEndian.readInt();
            }
        }
        
        @Override
        public void reset() {
            this.mImg = null;
            this.mNotYetLoadedTiles = 0;
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            if (this.mImg == null || this.mImg.length == 0) {
                dataWriterBigEndian.writeInt(0);
            }
            else {
                dataWriterBigEndian.writeInt(this.mImg.length);
                dataWriterBigEndian.write(this.mImg);
            }
            dataWriterBigEndian.writeInt(this.mNotYetLoadedTiles);
        }
    }
    
    public static class TrackRecordProfileSimple extends Storable
    {
        private String mDesc;
        private long mId;
        private byte[] mImg;
        private String mName;
        
        public TrackRecordProfileSimple() {
        }
        
        private TrackRecordProfileSimple(final long mId, String mDesc, final String s, final byte[] mImg) {
            this.mId = mId;
            String mName = mDesc;
            if (mDesc == null) {
                mName = "";
            }
            this.mName = mName;
            if ((mDesc = s) == null) {
                mDesc = "";
            }
            this.mDesc = mDesc;
            this.mImg = mImg;
        }
        
        public String getDesc() {
            return this.mDesc;
        }
        
        public byte[] getIcon() {
            return this.mImg;
        }
        
        public long getId() {
            return this.mId;
        }
        
        public String getName() {
            return this.mName;
        }
        
        @Override
        protected int getVersion() {
            return 0;
        }
        
        @Override
        protected void readObject(int int1, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            this.mId = dataReaderBigEndian.readLong();
            this.mName = dataReaderBigEndian.readString();
            this.mDesc = dataReaderBigEndian.readString();
            int1 = dataReaderBigEndian.readInt();
            if (int1 > 0) {
                dataReaderBigEndian.readBytes(this.mImg = new byte[int1]);
            }
        }
        
        @Override
        public void reset() {
            this.mId = 0L;
            this.mName = "";
            this.mDesc = "";
            this.mImg = null;
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            dataWriterBigEndian.writeLong(this.mId);
            dataWriterBigEndian.writeString(this.mName);
            dataWriterBigEndian.writeString(this.mDesc);
            int length;
            if (this.mImg != null) {
                length = this.mImg.length;
            }
            else {
                length = 0;
            }
            dataWriterBigEndian.writeInt(length);
            if (length > 0) {
                dataWriterBigEndian.write(this.mImg);
            }
        }
    }
}
