package locus.api.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.android.ActionTools;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.Storable;
import locus.api.objects.extra.Location;
import locus.api.objects.extra.Track;
import locus.api.objects.extra.Waypoint;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import locus.api.utils.Utils;

public class LocusUtils {
    private static final String TAG = "LocusUtils";

    public interface OnIntentGetLocation {
        void onFailed();

        void onReceived(Location location, Location location2);
    }

    public interface OnIntentMainFunction {
        void onFailed();

        void onReceived(LocusVersion locusVersion, Location location, Location location2);
    }

    public enum VersionCode {
        UPDATE_01(235, 235, 0),
        UPDATE_02(242, 242, 0),
        UPDATE_03(269, 269, 0),
        UPDATE_04(278, 278, 0),
        UPDATE_05(296, 296, 0),
        UPDATE_06(311, 311, 5),
        UPDATE_07(317, 317, 0),
        UPDATE_08(343, 343, 0),
        UPDATE_09(357, 357, 0),
        UPDATE_10(370, 370, 0),
        UPDATE_11(380, 380, 0),
        UPDATE_12(421, 421, 0);
        
        public final int vcFree;
        public final int vcGis;
        public final int vcPro;

        private VersionCode(int vcFree, int vcPro, int vcGis) {
            this.vcFree = vcFree;
            this.vcPro = vcPro;
            this.vcGis = vcGis;
        }
    }

    public static class LocusVersion extends Storable {
        private String mPackageName;
        private int mVersionCode;
        private String mVersionName;

        private LocusVersion(String packageName, String versionName, int versionCode) {
            if (packageName == null) {
                packageName = "";
            }
            this.mPackageName = packageName;
            if (versionName == null) {
                versionName = "";
            }
            this.mVersionName = versionName;
            if (versionCode < 0) {
                versionCode = 0;
            }
            this.mVersionCode = versionCode;
        }

        public boolean isVersionFree() {
            return (isVersionPro() || isVersionGis()) ? false : true;
        }

        public boolean isVersionPro() {
            return this.mPackageName.contains(".pro");
        }

        public boolean isVersionGis() {
            return this.mPackageName.contains(".gis");
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public String getVersionName() {
            return this.mVersionName;
        }

        public int getVersionCode() {
            return this.mVersionCode;
        }

        public boolean isVersionValid(VersionCode code) {
            if (isVersionFree()) {
                if (code.vcFree == 0 || this.mVersionCode < code.vcFree) {
                    return false;
                }
                return true;
            } else if (isVersionPro()) {
                if (code.vcPro == 0 || this.mVersionCode < code.vcPro) {
                    return false;
                }
                return true;
            } else if (!isVersionGis()) {
                return false;
            } else {
                if (code.vcGis == 0 || this.mVersionCode < code.vcGis) {
                    return false;
                }
                return true;
            }
        }

        public String toString() {
            return Utils.toString(this);
        }

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 0;
        }

        public void reset() {
            this.mPackageName = "";
            this.mVersionName = "";
            this.mVersionCode = 0;
        }

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dr) throws IOException {
            this.mPackageName = dr.readString();
            this.mVersionName = dr.readString();
            this.mVersionCode = dr.readInt();
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeString(this.mPackageName);
            dw.writeString(this.mVersionName);
            dw.writeInt(this.mVersionCode);
        }
    }

    public static LocusVersion getActiveVersion(Context ctx) {
        return getActiveVersion(ctx, 0);
    }

    public static LocusVersion getActiveVersion(Context ctx, int minVersionCode) {
        List<LocusVersion> versions = getAvailableVersions(ctx);
        if (versions.size() == 0) {
            return null;
        }
        LocusVersion backupVersion = null;
        int m = versions.size();
        for (int i = 0; i < m; i++) {
            try {
                LocusVersion lv = (LocusVersion) versions.get(i);
                if (lv.getVersionCode() < minVersionCode) {
                    continue;
                } else {
                    LocusInfo li = ActionTools.getLocusInfo(ctx, lv);
                    if (li != null) {
                        backupVersion = lv;
                        if (li.isRunning()) {
                            return lv;
                        }
                    } else {
                        continue;
                    }
                }
            } catch (RequiredVersionMissingException e) {
                Logger.logE(TAG, "prepareActiveLocus()", e);
            }
        }
        if (backupVersion != null) {
            return backupVersion;
        }
        return (LocusVersion) versions.get(0);
    }

    public static List<LocusVersion> getAvailableVersions(Context ctx) {
        List<LocusVersion> versions = new ArrayList();
        List<ApplicationInfo> appInfos = ctx.getPackageManager().getInstalledApplications(0);
        int m = appInfos.size();
        for (int i = 0; i < m; i++) {
            ApplicationInfo appInfo = (ApplicationInfo) appInfos.get(i);
            if (isPackageNameLocus(appInfo.packageName)) {
                LocusVersion lv = createLocusVersion(ctx, appInfo.packageName);
                if (lv != null) {
                    versions.add(lv);
                }
            }
        }
        return versions;
    }

    private static boolean isPackageNameLocus(String packageName) {
        if (packageName == null || packageName.length() == 0 || !packageName.startsWith("menion")) {
            return false;
        }
        if (packageName.equals("menion.android.locus") || packageName.startsWith("menion.android.locus.free") || packageName.startsWith("menion.android.locus.pro")) {
            return true;
        }
        return false;
    }

    public static LocusVersion createLocusVersion(Context ctx, String packageName) {
        if (packageName == null) {
            return null;
        }
        try {
            if (packageName.length() == 0 || !packageName.startsWith("menion.android.locus")) {
                return null;
            }
            PackageInfo info = ctx.getPackageManager().getPackageInfo(packageName, 0);
            if (info != null) {
                return new LocusVersion(packageName, info.versionName, info.versionCode);
            }
            return null;
        } catch (Exception e) {
            Logger.logE(TAG, "getLocusVersion(" + ctx + ", " + packageName + ")", e);
            return null;
        }
    }

    public static LocusVersion createLocusVersion(Context ctx, Intent intent) {
        if (ctx == null || intent == null) {
            return null;
        }
        String packageName = intent.getStringExtra(LocusConst.INTENT_EXTRA_PACKAGE_NAME);
        if (packageName == null || packageName.length() <= 0) {
            return createLocusVersion(ctx);
        }
        return createLocusVersion(ctx, packageName);
    }

    @Deprecated
    public static LocusVersion createLocusVersion(Context ctx) {
        if (ctx == null) {
            return null;
        }
        Logger.logW(TAG, "getLocusVersion(" + ctx + "), " + "Warning: old version of Locus: Correct package name is not known!");
        List<LocusVersion> versions = getAvailableVersions(ctx);
        int m = versions.size();
        for (int i = 0; i < m; i++) {
            LocusVersion lv = (LocusVersion) versions.get(i);
            if (lv.isVersionFree() || lv.isVersionPro()) {
                return lv;
            }
        }
        return null;
    }

    public static boolean isLocusAvailable(Context ctx) {
        return isLocusAvailable(ctx, VersionCode.UPDATE_01);
    }

    public static boolean isLocusAvailable(Context ctx, VersionCode vc) {
        return isLocusAvailable(ctx, vc.vcFree, vc.vcPro, vc.vcGis);
    }

    public static boolean isLocusAvailable(Context ctx, int versionFree, int versionPro, int versionGis) {
        List<LocusVersion> versions = getAvailableVersions(ctx);
        int m = versions.size();
        for (int i = 0; i < m; i++) {
            LocusVersion lv = (LocusVersion) versions.get(i);
            if (lv.isVersionFree() && versionFree > 0 && lv.getVersionCode() >= versionFree) {
                return true;
            }
            if (lv.isVersionPro() && versionPro > 0 && lv.getVersionCode() >= versionPro) {
                return true;
            }
            if (lv.isVersionGis() && versionGis > 0 && lv.getVersionCode() >= versionGis) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLocusFreePro(LocusVersion lv, int minVersion) {
        if (lv == null) {
            return false;
        }
        if ((!lv.isVersionFree() || lv.getVersionCode() < minVersion) && lv.isVersionPro() && lv.getVersionCode() >= minVersion) {
        }
        return true;
    }

    public static void callInstallLocus(Context ctx) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://market.android.com/details?id=menion.android.locus"));
        intent.addFlags(268435456);
        ctx.startActivity(intent);
    }

    public static void callStartLocusMap(Context ctx) {
        Intent intent = new Intent("com.asamm.locus.map.START_APP");
        intent.addFlags(268435456);
        ctx.startActivity(intent);
    }

    public static boolean isIntentGetLocation(Intent intent) {
        return isRequiredAction(intent, LocusConst.INTENT_ITEM_GET_LOCATION);
    }

    public static void handleIntentGetLocation(Context context, Intent intent, OnIntentGetLocation handler) throws NullPointerException {
        if (intent == null) {
            throw new NullPointerException("Intent cannot be null");
        } else if (isIntentGetLocation(intent)) {
            handler.onReceived(getLocationFromIntent(intent, LocusConst.INTENT_EXTRA_LOCATION_GPS), getLocationFromIntent(intent, LocusConst.INTENT_EXTRA_LOCATION_MAP_CENTER));
        } else {
            handler.onFailed();
        }
    }

    public static boolean sendGetLocationData(Activity activity, String name, Location loc) {
        if (loc == null) {
            return false;
        }
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(name)) {
            intent.putExtra(LocusConst.INTENT_EXTRA_NAME, name);
        }
        intent.putExtra(LocusConst.INTENT_EXTRA_LOCATION, loc.getAsBytes());
        activity.setResult(-1, intent);
        activity.finish();
        return true;
    }

    public static boolean isIntentPointTools(Intent intent) {
        return isRequiredAction(intent, LocusConst.INTENT_ITEM_POINT_TOOLS);
    }

    public static Waypoint handleIntentPointTools(Context ctx, Intent intent) throws RequiredVersionMissingException {
        long wptId = intent.getLongExtra(LocusConst.INTENT_EXTRA_ITEM_ID, -1);
        if (wptId < 0) {
            return null;
        }
        return ActionTools.getLocusWaypoint(ctx, createLocusVersion(ctx, intent), wptId);
    }

    public static boolean isIntentTrackTools(Intent intent) {
        return isRequiredAction(intent, LocusConst.INTENT_ITEM_TRACK_TOOLS);
    }

    public static Track handleIntentTrackTools(Context ctx, Intent intent) throws RequiredVersionMissingException {
        long trackId = intent.getLongExtra(LocusConst.INTENT_EXTRA_ITEM_ID, -1);
        if (trackId < 0) {
            return null;
        }
        return ActionTools.getLocusTrack(ctx, createLocusVersion(ctx, intent), trackId);
    }

    public static boolean isIntentMainFunction(Intent intent) {
        return isRequiredAction(intent, LocusConst.INTENT_ITEM_MAIN_FUNCTION);
    }

    public static void handleIntentMainFunction(Context ctx, Intent intent, OnIntentMainFunction handler) throws NullPointerException {
        handleIntentMenuItem(ctx, intent, handler, LocusConst.INTENT_ITEM_MAIN_FUNCTION);
    }

    public static boolean isIntentMainFunctionGc(Intent intent) {
        return isRequiredAction(intent, LocusConst.INTENT_ITEM_MAIN_FUNCTION_GC);
    }

    public static void handleIntentMainFunctionGc(Context ctx, Intent intent, OnIntentMainFunction handler) throws NullPointerException {
        handleIntentMenuItem(ctx, intent, handler, LocusConst.INTENT_ITEM_MAIN_FUNCTION_GC);
    }

    public static boolean isIntentSearchList(Intent intent) {
        return isRequiredAction(intent, LocusConst.INTENT_ITEM_SEARCH_LIST);
    }

    public static void handleIntentSearchList(Context ctx, Intent intent, OnIntentMainFunction handler) throws NullPointerException {
        handleIntentMenuItem(ctx, intent, handler, LocusConst.INTENT_ITEM_SEARCH_LIST);
    }

    private static void handleIntentMenuItem(Context ctx, Intent intent, OnIntentMainFunction handler, String item) throws NullPointerException {
        if (intent == null) {
            throw new NullPointerException("Intent cannot be null");
        } else if (handler == null) {
            throw new NullPointerException("Handler cannot be null");
        } else if (isRequiredAction(intent, item)) {
            handler.onReceived(createLocusVersion(ctx, intent), getLocationFromIntent(intent, LocusConst.INTENT_EXTRA_LOCATION_GPS), getLocationFromIntent(intent, LocusConst.INTENT_EXTRA_LOCATION_MAP_CENTER));
        } else {
            handler.onFailed();
        }
    }

    public static boolean isIntentPointsScreenTools(Intent intent) {
        return isRequiredAction(intent, LocusConst.INTENT_ITEM_POINTS_SCREEN_TOOLS);
    }

    public static long[] handleIntentPointsScreenTools(Intent intent) {
        if (intent.hasExtra(LocusConst.INTENT_EXTRA_ITEMS_ID)) {
            return intent.getLongArrayExtra(LocusConst.INTENT_EXTRA_ITEMS_ID);
        }
        return null;
    }

    public static boolean isIntentReceiveLocation(Intent intent) {
        return isRequiredAction(intent, LocusConst.ACTION_RECEIVE_LOCATION);
    }

    private static boolean isRequiredAction(Intent intent, String action) {
        return (intent == null || intent.getAction() == null || !intent.getAction().equals(action)) ? false : true;
    }

    public static Intent prepareResultExtraOnDisplayIntent(Waypoint wpt, boolean overridePoint) {
        Intent intent = new Intent();
        addWaypointToIntent(intent, wpt);
        intent.putExtra(LocusConst.INTENT_EXTRA_POINT_OVERWRITE, overridePoint);
        return intent;
    }

    public static void addWaypointToIntent(Intent intent, Waypoint wpt) {
        intent.putExtra(LocusConst.INTENT_EXTRA_POINT, wpt.getAsBytes());
    }

    public static Waypoint getWaypointFromIntent(Intent intent) {
        try {
            return new Waypoint(intent.getByteArrayExtra(LocusConst.INTENT_EXTRA_POINT));
        } catch (Exception e) {
            Logger.logE(TAG, "getWaypointFromIntent(" + intent + ")", e);
            return null;
        }
    }

    public static Location getLocationFromIntent(Intent intent, String extraName) {
        try {
            if (intent.hasExtra(extraName)) {
                return new Location(intent.getByteArrayExtra(extraName));
            }
            return null;
        } catch (Exception e) {
            Logger.logE(TAG, "getLocationFromIntent(" + intent + ")", e);
            return null;
        }
    }

    public static Location convertToL(android.location.Location oldLoc) {
        Location loc = new Location(oldLoc.getProvider());
        loc.setLongitude(oldLoc.getLongitude());
        loc.setLatitude(oldLoc.getLatitude());
        loc.setTime(oldLoc.getTime());
        if (oldLoc.hasAccuracy()) {
            loc.setAccuracy(oldLoc.getAccuracy());
        }
        if (oldLoc.hasAltitude()) {
            loc.setAltitude(oldLoc.getAltitude());
        }
        if (oldLoc.hasBearing()) {
            loc.setBearing(oldLoc.getBearing());
        }
        if (oldLoc.hasSpeed()) {
            loc.setSpeed(oldLoc.getSpeed());
        }
        return loc;
    }

    public static android.location.Location convertToA(Location oldLoc) {
        android.location.Location loc = new android.location.Location(oldLoc.getProvider());
        loc.setLongitude(oldLoc.getLongitude());
        loc.setLatitude(oldLoc.getLatitude());
        loc.setTime(oldLoc.getTime());
        if (oldLoc.hasAccuracy()) {
            loc.setAccuracy(oldLoc.getAccuracy());
        }
        if (oldLoc.hasAltitude()) {
            loc.setAltitude(oldLoc.getAltitude());
        }
        if (oldLoc.hasBearing()) {
            loc.setBearing(oldLoc.getBearing());
        }
        if (oldLoc.hasSpeed()) {
            loc.setSpeed(oldLoc.getSpeed());
        }
        return loc;
    }

    public static boolean isAppAvailable(Context ctx, String packageName, int version) {
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(packageName, 0);
            if (info == null || info.versionCode < version) {
                return false;
            }
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
