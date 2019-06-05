// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.utils;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Utils;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import android.text.TextUtils;
import android.app.Activity;
import android.content.pm.PackageManager$NameNotFoundException;
import locus.api.objects.extra.Track;
import android.content.pm.ApplicationInfo;
import java.util.ArrayList;
import locus.api.objects.Storable;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.android.ActionTools;
import android.content.pm.PackageInfo;
import java.util.List;
import locus.api.utils.Logger;
import locus.api.objects.extra.Location;
import android.net.Uri;
import android.content.Context;
import locus.api.objects.extra.Waypoint;
import android.content.Intent;

public class LocusUtils
{
    private static final String TAG = "LocusUtils";
    
    public static void addWaypointToIntent(final Intent intent, final Waypoint waypoint) {
        intent.putExtra("INTENT_EXTRA_POINT", waypoint.getAsBytes());
    }
    
    public static void callInstallLocus(final Context context) {
        final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://market.android.com/details?id=menion.android.locus"));
        intent.addFlags(268435456);
        context.startActivity(intent);
    }
    
    public static void callStartLocusMap(final Context context) {
        final Intent intent = new Intent("com.asamm.locus.map.START_APP");
        intent.addFlags(268435456);
        context.startActivity(intent);
    }
    
    public static android.location.Location convertToA(final Location location) {
        final android.location.Location location2 = new android.location.Location(location.getProvider());
        location2.setLongitude(location.getLongitude());
        location2.setLatitude(location.getLatitude());
        location2.setTime(location.getTime());
        if (location.hasAccuracy()) {
            location2.setAccuracy(location.getAccuracy());
        }
        if (location.hasAltitude()) {
            location2.setAltitude(location.getAltitude());
        }
        if (location.hasBearing()) {
            location2.setBearing(location.getBearing());
        }
        if (location.hasSpeed()) {
            location2.setSpeed(location.getSpeed());
        }
        return location2;
    }
    
    public static Location convertToL(final android.location.Location location) {
        final Location location2 = new Location(location.getProvider());
        location2.setLongitude(location.getLongitude());
        location2.setLatitude(location.getLatitude());
        location2.setTime(location.getTime());
        if (location.hasAccuracy()) {
            location2.setAccuracy(location.getAccuracy());
        }
        if (location.hasAltitude()) {
            location2.setAltitude(location.getAltitude());
        }
        if (location.hasBearing()) {
            location2.setBearing(location.getBearing());
        }
        if (location.hasSpeed()) {
            location2.setSpeed(location.getSpeed());
        }
        return location2;
    }
    
    @Deprecated
    public static LocusVersion createLocusVersion(final Context obj) {
        LocusVersion locusVersion;
        if (obj == null) {
            locusVersion = null;
        }
        else {
            Logger.logW("LocusUtils", "getLocusVersion(" + obj + "), " + "Warning: old version of Locus: Correct package name is not known!");
            final List<LocusVersion> availableVersions = getAvailableVersions(obj);
            for (int i = 0; i < availableVersions.size(); ++i) {
                final LocusVersion locusVersion2 = locusVersion = availableVersions.get(i);
                if (locusVersion2.isVersionFree()) {
                    return locusVersion;
                }
                locusVersion = locusVersion2;
                if (locusVersion2.isVersionPro()) {
                    return locusVersion;
                }
            }
            locusVersion = null;
        }
        return locusVersion;
    }
    
    public static LocusVersion createLocusVersion(final Context context, final Intent intent) {
        LocusVersion locusVersion;
        if (context == null || intent == null) {
            locusVersion = null;
        }
        else {
            final String stringExtra = intent.getStringExtra("INTENT_EXTRA_PACKAGE_NAME");
            if (stringExtra != null && stringExtra.length() > 0) {
                locusVersion = createLocusVersion(context, stringExtra);
            }
            else {
                locusVersion = createLocusVersion(context);
            }
        }
        return locusVersion;
    }
    
    public static LocusVersion createLocusVersion(final Context obj, final String str) {
        LocusVersion locusVersion2;
        final LocusVersion locusVersion = locusVersion2 = null;
        if (str != null) {
            locusVersion2 = locusVersion;
            try {
                if (str.length() != 0) {
                    if (!str.startsWith("menion.android.locus")) {
                        locusVersion2 = locusVersion;
                    }
                    else {
                        final PackageInfo packageInfo = obj.getPackageManager().getPackageInfo(str, 0);
                        locusVersion2 = locusVersion;
                        if (packageInfo != null) {
                            locusVersion2 = new LocusVersion(str, packageInfo.versionName, packageInfo.versionCode);
                        }
                    }
                }
                return locusVersion2;
            }
            catch (Exception ex) {
                Logger.logE("LocusUtils", "getLocusVersion(" + obj + ", " + str + ")", ex);
                locusVersion2 = locusVersion;
                return locusVersion2;
            }
            return locusVersion2;
        }
        return locusVersion2;
    }
    
    public static LocusVersion getActiveVersion(final Context context) {
        return getActiveVersion(context, 0);
    }
    
    public static LocusVersion getActiveVersion(Context o, final int n) {
        final List<LocusVersion> availableVersions = getAvailableVersions((Context)o);
        if (availableVersions.size() == 0) {
            o = null;
        }
        else {
            LocusVersion locusVersion = null;
            int i = 0;
        Label_0066_Outer:
            while (i < availableVersions.size()) {
                LocusVersion locusVersion2 = locusVersion;
                while (true) {
                    try {
                        final Storable storable = availableVersions.get(i);
                        locusVersion2 = locusVersion;
                        if (((LocusVersion)storable).getVersionCode() >= n) {
                            locusVersion2 = locusVersion;
                            final LocusInfo locusInfo = ActionTools.getLocusInfo((Context)o, (LocusVersion)storable);
                            if (locusInfo != null) {
                                locusVersion = (locusVersion2 = (LocusVersion)storable);
                                if (locusInfo.isRunning()) {
                                    o = storable;
                                    return (LocusVersion)o;
                                }
                            }
                        }
                        ++i;
                        continue Label_0066_Outer;
                    }
                    catch (RequiredVersionMissingException ex) {
                        Logger.logE("LocusUtils", "prepareActiveLocus()", ex);
                        locusVersion = locusVersion2;
                        continue;
                    }
                    break;
                }
                break;
            }
            if (locusVersion != null) {
                o = locusVersion;
            }
            else {
                o = availableVersions.get(0);
            }
        }
        return (LocusVersion)o;
    }
    
    public static List<LocusVersion> getAvailableVersions(final Context context) {
        final ArrayList<LocusVersion> list = new ArrayList<LocusVersion>();
        final List installedApplications = context.getPackageManager().getInstalledApplications(0);
        for (int i = 0; i < installedApplications.size(); ++i) {
            final ApplicationInfo applicationInfo = installedApplications.get(i);
            if (isPackageNameLocus(applicationInfo.packageName)) {
                final LocusVersion locusVersion = createLocusVersion(context, applicationInfo.packageName);
                if (locusVersion != null) {
                    list.add(locusVersion);
                }
            }
        }
        return list;
    }
    
    public static Location getLocationFromIntent(Intent obj, final String s) {
        final Object o = null;
        try {
            if (!((Intent)obj).hasExtra(s)) {
                obj = o;
            }
            else {
                obj = new Location(((Intent)obj).getByteArrayExtra(s));
            }
            return (Location)obj;
        }
        catch (Exception ex) {
            Logger.logE("LocusUtils", "getLocationFromIntent(" + obj + ")", ex);
            obj = o;
            return (Location)obj;
        }
        return (Location)obj;
    }
    
    public static Waypoint getWaypointFromIntent(Intent obj) {
        try {
            obj = new Waypoint(((Intent)obj).getByteArrayExtra("INTENT_EXTRA_POINT"));
            return (Waypoint)obj;
        }
        catch (Exception ex) {
            Logger.logE("LocusUtils", "getWaypointFromIntent(" + obj + ")", ex);
            obj = null;
            return (Waypoint)obj;
        }
    }
    
    public static void handleIntentGetLocation(final Context context, final Intent intent, final OnIntentGetLocation onIntentGetLocation) throws NullPointerException {
        if (intent == null) {
            throw new NullPointerException("Intent cannot be null");
        }
        if (!isIntentGetLocation(intent)) {
            onIntentGetLocation.onFailed();
        }
        else {
            onIntentGetLocation.onReceived(getLocationFromIntent(intent, "INTENT_EXTRA_LOCATION_GPS"), getLocationFromIntent(intent, "INTENT_EXTRA_LOCATION_MAP_CENTER"));
        }
    }
    
    public static void handleIntentMainFunction(final Context context, final Intent intent, final OnIntentMainFunction onIntentMainFunction) throws NullPointerException {
        handleIntentMenuItem(context, intent, onIntentMainFunction, "locus.api.android.INTENT_ITEM_MAIN_FUNCTION");
    }
    
    public static void handleIntentMainFunctionGc(final Context context, final Intent intent, final OnIntentMainFunction onIntentMainFunction) throws NullPointerException {
        handleIntentMenuItem(context, intent, onIntentMainFunction, "locus.api.android.INTENT_ITEM_MAIN_FUNCTION_GC");
    }
    
    private static void handleIntentMenuItem(final Context context, final Intent intent, final OnIntentMainFunction onIntentMainFunction, final String s) throws NullPointerException {
        if (intent == null) {
            throw new NullPointerException("Intent cannot be null");
        }
        if (onIntentMainFunction == null) {
            throw new NullPointerException("Handler cannot be null");
        }
        if (!isRequiredAction(intent, s)) {
            onIntentMainFunction.onFailed();
        }
        else {
            onIntentMainFunction.onReceived(createLocusVersion(context, intent), getLocationFromIntent(intent, "INTENT_EXTRA_LOCATION_GPS"), getLocationFromIntent(intent, "INTENT_EXTRA_LOCATION_MAP_CENTER"));
        }
    }
    
    public static Waypoint handleIntentPointTools(final Context context, final Intent intent) throws RequiredVersionMissingException {
        final long longExtra = intent.getLongExtra("INTENT_EXTRA_ITEM_ID", -1L);
        Waypoint locusWaypoint;
        if (longExtra < 0L) {
            locusWaypoint = null;
        }
        else {
            locusWaypoint = ActionTools.getLocusWaypoint(context, createLocusVersion(context, intent), longExtra);
        }
        return locusWaypoint;
    }
    
    public static long[] handleIntentPointsScreenTools(final Intent intent) {
        long[] longArrayExtra = null;
        if (intent.hasExtra("INTENT_EXTRA_ITEMS_ID")) {
            longArrayExtra = intent.getLongArrayExtra("INTENT_EXTRA_ITEMS_ID");
        }
        return longArrayExtra;
    }
    
    public static void handleIntentSearchList(final Context context, final Intent intent, final OnIntentMainFunction onIntentMainFunction) throws NullPointerException {
        handleIntentMenuItem(context, intent, onIntentMainFunction, "locus.api.android.INTENT_ITEM_SEARCH_LIST");
    }
    
    public static Track handleIntentTrackTools(final Context context, final Intent intent) throws RequiredVersionMissingException {
        final long longExtra = intent.getLongExtra("INTENT_EXTRA_ITEM_ID", -1L);
        Track locusTrack;
        if (longExtra < 0L) {
            locusTrack = null;
        }
        else {
            locusTrack = ActionTools.getLocusTrack(context, createLocusVersion(context, intent), longExtra);
        }
        return locusTrack;
    }
    
    public static boolean isAppAvailable(final Context context, final String s, final int n) {
        final boolean b = false;
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(s, 0);
            boolean b2 = b;
            if (packageInfo != null) {
                final int versionCode = packageInfo.versionCode;
                b2 = b;
                if (versionCode >= n) {
                    b2 = true;
                }
            }
            return b2;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return b;
        }
    }
    
    public static boolean isIntentGetLocation(final Intent intent) {
        return isRequiredAction(intent, "locus.api.android.INTENT_ITEM_GET_LOCATION");
    }
    
    public static boolean isIntentMainFunction(final Intent intent) {
        return isRequiredAction(intent, "locus.api.android.INTENT_ITEM_MAIN_FUNCTION");
    }
    
    public static boolean isIntentMainFunctionGc(final Intent intent) {
        return isRequiredAction(intent, "locus.api.android.INTENT_ITEM_MAIN_FUNCTION_GC");
    }
    
    public static boolean isIntentPointTools(final Intent intent) {
        return isRequiredAction(intent, "locus.api.android.INTENT_ITEM_POINT_TOOLS");
    }
    
    public static boolean isIntentPointsScreenTools(final Intent intent) {
        return isRequiredAction(intent, "locus.api.android.INTENT_ITEM_POINTS_SCREEN_TOOLS");
    }
    
    public static boolean isIntentReceiveLocation(final Intent intent) {
        return isRequiredAction(intent, "locus.api.android.ACTION_RECEIVE_LOCATION");
    }
    
    public static boolean isIntentSearchList(final Intent intent) {
        return isRequiredAction(intent, "locus.api.android.INTENT_ITEM_SEARCH_LIST");
    }
    
    public static boolean isIntentTrackTools(final Intent intent) {
        return isRequiredAction(intent, "locus.api.android.INTENT_ITEM_TRACK_TOOLS");
    }
    
    public static boolean isLocusAvailable(final Context context) {
        return isLocusAvailable(context, VersionCode.UPDATE_01);
    }
    
    public static boolean isLocusAvailable(final Context context, final int n, final int n2, final int n3) {
        final boolean b = true;
        final List<LocusVersion> availableVersions = getAvailableVersions(context);
        int i = 0;
        while (i < availableVersions.size()) {
            final LocusVersion locusVersion = availableVersions.get(i);
            if (!locusVersion.isVersionFree() || n <= 0 || locusVersion.getVersionCode() < n) {
                if (locusVersion.isVersionPro() && n2 > 0) {
                    final boolean b2 = b;
                    if (locusVersion.getVersionCode() >= n2) {
                        return b2;
                    }
                }
                if (locusVersion.isVersionGis() && n3 > 0) {
                    final boolean b2 = b;
                    if (locusVersion.getVersionCode() >= n3) {
                        return b2;
                    }
                }
                ++i;
                continue;
            }
            return b;
        }
        return false;
    }
    
    public static boolean isLocusAvailable(final Context context, final VersionCode versionCode) {
        return isLocusAvailable(context, versionCode.vcFree, versionCode.vcPro, versionCode.vcGis);
    }
    
    public static boolean isLocusFreePro(final LocusVersion locusVersion, final int n) {
        final boolean b = true;
        boolean b2;
        if (locusVersion == null) {
            b2 = false;
        }
        else {
            if (locusVersion.isVersionFree()) {
                b2 = b;
                if (locusVersion.getVersionCode() >= n) {
                    return b2;
                }
            }
            b2 = b;
            if (locusVersion.isVersionPro()) {
                b2 = b;
                if (locusVersion.getVersionCode() >= n) {
                    b2 = b;
                }
            }
        }
        return b2;
    }
    
    private static boolean isPackageNameLocus(final String s) {
        boolean b2;
        final boolean b = b2 = false;
        if (s != null) {
            if (s.length() == 0) {
                b2 = b;
            }
            else {
                b2 = b;
                if (s.startsWith("menion")) {
                    if (!s.equals("menion.android.locus") && !s.startsWith("menion.android.locus.free")) {
                        b2 = b;
                        if (!s.startsWith("menion.android.locus.pro")) {
                            return b2;
                        }
                    }
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    private static boolean isRequiredAction(final Intent intent, final String anObject) {
        return intent != null && intent.getAction() != null && intent.getAction().equals(anObject);
    }
    
    public static Intent prepareResultExtraOnDisplayIntent(final Waypoint waypoint, final boolean b) {
        final Intent intent = new Intent();
        addWaypointToIntent(intent, waypoint);
        intent.putExtra("INTENT_EXTRA_POINT_OVERWRITE", b);
        return intent;
    }
    
    public static boolean sendGetLocationData(final Activity activity, final String s, final Location location) {
        boolean b;
        if (location == null) {
            b = false;
        }
        else {
            final Intent intent = new Intent();
            if (!TextUtils.isEmpty((CharSequence)s)) {
                intent.putExtra("INTENT_EXTRA_NAME", s);
            }
            intent.putExtra("INTENT_EXTRA_LOCATION", location.getAsBytes());
            activity.setResult(-1, intent);
            activity.finish();
            b = true;
        }
        return b;
    }
    
    public static class LocusVersion extends Storable
    {
        private String mPackageName;
        private int mVersionCode;
        private String mVersionName;
        
        public LocusVersion() {
        }
        
        private LocusVersion(String mVersionName, final String s, final int n) {
            String mPackageName = mVersionName;
            if (mVersionName == null) {
                mPackageName = "";
            }
            this.mPackageName = mPackageName;
            if ((mVersionName = s) == null) {
                mVersionName = "";
            }
            this.mVersionName = mVersionName;
            int mVersionCode;
            if ((mVersionCode = n) < 0) {
                mVersionCode = 0;
            }
            this.mVersionCode = mVersionCode;
        }
        
        public String getPackageName() {
            return this.mPackageName;
        }
        
        @Override
        protected int getVersion() {
            return 0;
        }
        
        public int getVersionCode() {
            return this.mVersionCode;
        }
        
        public String getVersionName() {
            return this.mVersionName;
        }
        
        public boolean isVersionFree() {
            return !this.isVersionPro() && !this.isVersionGis();
        }
        
        public boolean isVersionGis() {
            return this.mPackageName.contains(".gis");
        }
        
        public boolean isVersionPro() {
            return this.mPackageName.contains(".pro");
        }
        
        public boolean isVersionValid(final VersionCode versionCode) {
            boolean b = true;
            if (this.isVersionFree()) {
                if (versionCode.vcFree == 0 || this.mVersionCode < versionCode.vcFree) {
                    b = false;
                }
            }
            else if (this.isVersionPro()) {
                if (versionCode.vcPro == 0 || this.mVersionCode < versionCode.vcPro) {
                    b = false;
                }
            }
            else if (this.isVersionGis()) {
                if (versionCode.vcGis == 0 || this.mVersionCode < versionCode.vcGis) {
                    b = false;
                }
            }
            else {
                b = false;
            }
            return b;
        }
        
        @Override
        protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            this.mPackageName = dataReaderBigEndian.readString();
            this.mVersionName = dataReaderBigEndian.readString();
            this.mVersionCode = dataReaderBigEndian.readInt();
        }
        
        @Override
        public void reset() {
            this.mPackageName = "";
            this.mVersionName = "";
            this.mVersionCode = 0;
        }
        
        @Override
        public String toString() {
            return Utils.toString(this);
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            dataWriterBigEndian.writeString(this.mPackageName);
            dataWriterBigEndian.writeString(this.mVersionName);
            dataWriterBigEndian.writeInt(this.mVersionCode);
        }
    }
    
    public interface OnIntentGetLocation
    {
        void onFailed();
        
        void onReceived(final Location p0, final Location p1);
    }
    
    public interface OnIntentMainFunction
    {
        void onFailed();
        
        void onReceived(final LocusVersion p0, final Location p1, final Location p2);
    }
    
    public enum VersionCode
    {
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
        
        private VersionCode(final int vcFree, final int vcPro, final int vcGis) {
            this.vcFree = vcFree;
            this.vcPro = vcPro;
            this.vcGis = vcGis;
        }
    }
}
