package locus.api.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.net.Uri;
import android.text.TextUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.android.ActionTools;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.Storable;
import locus.api.objects.extra.Track;
import locus.api.objects.extra.Waypoint;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class LocusUtils {
   private static final String TAG = "LocusUtils";

   public static void addWaypointToIntent(Intent var0, Waypoint var1) {
      var0.putExtra("INTENT_EXTRA_POINT", var1.getAsBytes());
   }

   public static void callInstallLocus(Context var0) {
      Intent var1 = new Intent("android.intent.action.VIEW", Uri.parse("http://market.android.com/details?id=menion.android.locus"));
      var1.addFlags(268435456);
      var0.startActivity(var1);
   }

   public static void callStartLocusMap(Context var0) {
      Intent var1 = new Intent("com.asamm.locus.map.START_APP");
      var1.addFlags(268435456);
      var0.startActivity(var1);
   }

   public static Location convertToA(locus.api.objects.extra.Location var0) {
      Location var1 = new Location(var0.getProvider());
      var1.setLongitude(var0.getLongitude());
      var1.setLatitude(var0.getLatitude());
      var1.setTime(var0.getTime());
      if (var0.hasAccuracy()) {
         var1.setAccuracy(var0.getAccuracy());
      }

      if (var0.hasAltitude()) {
         var1.setAltitude(var0.getAltitude());
      }

      if (var0.hasBearing()) {
         var1.setBearing(var0.getBearing());
      }

      if (var0.hasSpeed()) {
         var1.setSpeed(var0.getSpeed());
      }

      return var1;
   }

   public static locus.api.objects.extra.Location convertToL(Location var0) {
      locus.api.objects.extra.Location var1 = new locus.api.objects.extra.Location(var0.getProvider());
      var1.setLongitude(var0.getLongitude());
      var1.setLatitude(var0.getLatitude());
      var1.setTime(var0.getTime());
      if (var0.hasAccuracy()) {
         var1.setAccuracy(var0.getAccuracy());
      }

      if (var0.hasAltitude()) {
         var1.setAltitude(var0.getAltitude());
      }

      if (var0.hasBearing()) {
         var1.setBearing(var0.getBearing());
      }

      if (var0.hasSpeed()) {
         var1.setSpeed(var0.getSpeed());
      }

      return var1;
   }

   @Deprecated
   public static LocusUtils.LocusVersion createLocusVersion(Context var0) {
      LocusUtils.LocusVersion var5;
      if (var0 == null) {
         var5 = null;
      } else {
         Logger.logW("LocusUtils", "getLocusVersion(" + var0 + "), " + "Warning: old version of Locus: Correct package name is not known!");
         List var1 = getAvailableVersions(var0);
         int var2 = 0;
         int var3 = var1.size();

         while(true) {
            if (var2 >= var3) {
               var5 = null;
               break;
            }

            LocusUtils.LocusVersion var4 = (LocusUtils.LocusVersion)var1.get(var2);
            var5 = var4;
            if (var4.isVersionFree()) {
               break;
            }

            var5 = var4;
            if (var4.isVersionPro()) {
               break;
            }

            ++var2;
         }
      }

      return var5;
   }

   public static LocusUtils.LocusVersion createLocusVersion(Context var0, Intent var1) {
      LocusUtils.LocusVersion var2;
      if (var0 != null && var1 != null) {
         String var3 = var1.getStringExtra("INTENT_EXTRA_PACKAGE_NAME");
         if (var3 != null && var3.length() > 0) {
            var2 = createLocusVersion(var0, var3);
         } else {
            var2 = createLocusVersion(var0);
         }
      } else {
         var2 = null;
      }

      return var2;
   }

   public static LocusUtils.LocusVersion createLocusVersion(Context var0, String var1) {
      Object var2 = null;
      LocusUtils.LocusVersion var3 = (LocusUtils.LocusVersion)var2;
      if (var1 != null) {
         var3 = (LocusUtils.LocusVersion)var2;

         label54: {
            Exception var10000;
            label43: {
               boolean var10001;
               try {
                  if (var1.length() == 0) {
                     return var3;
                  }

                  if (!var1.startsWith("menion.android.locus")) {
                     break label54;
                  }
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label43;
               }

               PackageInfo var4;
               try {
                  var4 = var0.getPackageManager().getPackageInfo(var1, 0);
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label43;
               }

               var3 = (LocusUtils.LocusVersion)var2;
               if (var4 == null) {
                  return var3;
               }

               try {
                  var3 = new LocusUtils.LocusVersion(var1, var4.versionName, var4.versionCode);
                  return var3;
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            Exception var8 = var10000;
            Logger.logE("LocusUtils", "getLocusVersion(" + var0 + ", " + var1 + ")", var8);
            var3 = (LocusUtils.LocusVersion)var2;
            return var3;
         }

         var3 = (LocusUtils.LocusVersion)var2;
      }

      return var3;
   }

   public static LocusUtils.LocusVersion getActiveVersion(Context var0) {
      return getActiveVersion(var0, 0);
   }

   public static LocusUtils.LocusVersion getActiveVersion(Context var0, int var1) {
      List var2 = getAvailableVersions(var0);
      LocusUtils.LocusVersion var14;
      if (var2.size() == 0) {
         var14 = null;
      } else {
         LocusUtils.LocusVersion var3 = null;
         int var4 = 0;

         for(int var5 = var2.size(); var4 < var5; ++var4) {
            LocusUtils.LocusVersion var6 = var3;

            LocusUtils.LocusVersion var7;
            boolean var9;
            label59: {
               RequiredVersionMissingException var10000;
               label70: {
                  boolean var10001;
                  try {
                     var7 = (LocusUtils.LocusVersion)var2.get(var4);
                  } catch (RequiredVersionMissingException var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label70;
                  }

                  var6 = var3;

                  try {
                     if (var7.getVersionCode() < var1) {
                        continue;
                     }
                  } catch (RequiredVersionMissingException var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label70;
                  }

                  var6 = var3;

                  LocusInfo var8;
                  try {
                     var8 = ActionTools.getLocusInfo(var0, var7);
                  } catch (RequiredVersionMissingException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label70;
                  }

                  if (var8 == null) {
                     continue;
                  }

                  var3 = var7;
                  var6 = var7;

                  try {
                     var9 = var8.isRunning();
                     break label59;
                  } catch (RequiredVersionMissingException var10) {
                     var10000 = var10;
                     var10001 = false;
                  }
               }

               RequiredVersionMissingException var15 = var10000;
               Logger.logE("LocusUtils", "prepareActiveLocus()", var15);
               var3 = var6;
               continue;
            }

            if (var9) {
               var14 = var7;
               return var14;
            }
         }

         if (var3 != null) {
            var14 = var3;
         } else {
            var14 = (LocusUtils.LocusVersion)var2.get(0);
         }
      }

      return var14;
   }

   public static List getAvailableVersions(Context var0) {
      ArrayList var1 = new ArrayList();
      List var2 = var0.getPackageManager().getInstalledApplications(0);
      int var3 = 0;

      for(int var4 = var2.size(); var3 < var4; ++var3) {
         ApplicationInfo var5 = (ApplicationInfo)var2.get(var3);
         if (isPackageNameLocus(var5.packageName)) {
            LocusUtils.LocusVersion var6 = createLocusVersion(var0, var5.packageName);
            if (var6 != null) {
               var1.add(var6);
            }
         }
      }

      return var1;
   }

   public static locus.api.objects.extra.Location getLocationFromIntent(Intent var0, String var1) {
      Object var2 = null;

      locus.api.objects.extra.Location var5;
      label31: {
         locus.api.objects.extra.Location var3;
         try {
            if (!var0.hasExtra(var1)) {
               break label31;
            }

            var3 = new locus.api.objects.extra.Location(var0.getByteArrayExtra(var1));
         } catch (Exception var4) {
            Logger.logE("LocusUtils", "getLocationFromIntent(" + var0 + ")", var4);
            var5 = (locus.api.objects.extra.Location)var2;
            return var5;
         }

         var5 = var3;
         return var5;
      }

      var5 = (locus.api.objects.extra.Location)var2;
      return var5;
   }

   public static Waypoint getWaypointFromIntent(Intent var0) {
      Waypoint var1;
      Waypoint var3;
      try {
         var1 = new Waypoint(var0.getByteArrayExtra("INTENT_EXTRA_POINT"));
      } catch (Exception var2) {
         Logger.logE("LocusUtils", "getWaypointFromIntent(" + var0 + ")", var2);
         var3 = null;
         return var3;
      }

      var3 = var1;
      return var3;
   }

   public static void handleIntentGetLocation(Context var0, Intent var1, LocusUtils.OnIntentGetLocation var2) throws NullPointerException {
      if (var1 == null) {
         throw new NullPointerException("Intent cannot be null");
      } else {
         if (!isIntentGetLocation(var1)) {
            var2.onFailed();
         } else {
            var2.onReceived(getLocationFromIntent(var1, "INTENT_EXTRA_LOCATION_GPS"), getLocationFromIntent(var1, "INTENT_EXTRA_LOCATION_MAP_CENTER"));
         }

      }
   }

   public static void handleIntentMainFunction(Context var0, Intent var1, LocusUtils.OnIntentMainFunction var2) throws NullPointerException {
      handleIntentMenuItem(var0, var1, var2, "locus.api.android.INTENT_ITEM_MAIN_FUNCTION");
   }

   public static void handleIntentMainFunctionGc(Context var0, Intent var1, LocusUtils.OnIntentMainFunction var2) throws NullPointerException {
      handleIntentMenuItem(var0, var1, var2, "locus.api.android.INTENT_ITEM_MAIN_FUNCTION_GC");
   }

   private static void handleIntentMenuItem(Context var0, Intent var1, LocusUtils.OnIntentMainFunction var2, String var3) throws NullPointerException {
      if (var1 == null) {
         throw new NullPointerException("Intent cannot be null");
      } else if (var2 == null) {
         throw new NullPointerException("Handler cannot be null");
      } else {
         if (!isRequiredAction(var1, var3)) {
            var2.onFailed();
         } else {
            var2.onReceived(createLocusVersion(var0, var1), getLocationFromIntent(var1, "INTENT_EXTRA_LOCATION_GPS"), getLocationFromIntent(var1, "INTENT_EXTRA_LOCATION_MAP_CENTER"));
         }

      }
   }

   public static Waypoint handleIntentPointTools(Context var0, Intent var1) throws RequiredVersionMissingException {
      long var2 = var1.getLongExtra("INTENT_EXTRA_ITEM_ID", -1L);
      Waypoint var4;
      if (var2 < 0L) {
         var4 = null;
      } else {
         var4 = ActionTools.getLocusWaypoint(var0, createLocusVersion(var0, var1), var2);
      }

      return var4;
   }

   public static long[] handleIntentPointsScreenTools(Intent var0) {
      long[] var1 = null;
      if (var0.hasExtra("INTENT_EXTRA_ITEMS_ID")) {
         var1 = var0.getLongArrayExtra("INTENT_EXTRA_ITEMS_ID");
      }

      return var1;
   }

   public static void handleIntentSearchList(Context var0, Intent var1, LocusUtils.OnIntentMainFunction var2) throws NullPointerException {
      handleIntentMenuItem(var0, var1, var2, "locus.api.android.INTENT_ITEM_SEARCH_LIST");
   }

   public static Track handleIntentTrackTools(Context var0, Intent var1) throws RequiredVersionMissingException {
      long var2 = var1.getLongExtra("INTENT_EXTRA_ITEM_ID", -1L);
      Track var4;
      if (var2 < 0L) {
         var4 = null;
      } else {
         var4 = ActionTools.getLocusTrack(var0, createLocusVersion(var0, var1), var2);
      }

      return var4;
   }

   public static boolean isAppAvailable(Context var0, String var1, int var2) {
      boolean var3 = false;

      boolean var4;
      int var5;
      label39: {
         label32: {
            boolean var10001;
            PackageInfo var8;
            try {
               var8 = var0.getPackageManager().getPackageInfo(var1, 0);
            } catch (NameNotFoundException var7) {
               var10001 = false;
               break label32;
            }

            var4 = var3;
            if (var8 == null) {
               return var4;
            }

            try {
               var5 = var8.versionCode;
               break label39;
            } catch (NameNotFoundException var6) {
               var10001 = false;
            }
         }

         var4 = var3;
         return var4;
      }

      var4 = var3;
      if (var5 >= var2) {
         var4 = true;
      }

      return var4;
   }

   public static boolean isIntentGetLocation(Intent var0) {
      return isRequiredAction(var0, "locus.api.android.INTENT_ITEM_GET_LOCATION");
   }

   public static boolean isIntentMainFunction(Intent var0) {
      return isRequiredAction(var0, "locus.api.android.INTENT_ITEM_MAIN_FUNCTION");
   }

   public static boolean isIntentMainFunctionGc(Intent var0) {
      return isRequiredAction(var0, "locus.api.android.INTENT_ITEM_MAIN_FUNCTION_GC");
   }

   public static boolean isIntentPointTools(Intent var0) {
      return isRequiredAction(var0, "locus.api.android.INTENT_ITEM_POINT_TOOLS");
   }

   public static boolean isIntentPointsScreenTools(Intent var0) {
      return isRequiredAction(var0, "locus.api.android.INTENT_ITEM_POINTS_SCREEN_TOOLS");
   }

   public static boolean isIntentReceiveLocation(Intent var0) {
      return isRequiredAction(var0, "locus.api.android.ACTION_RECEIVE_LOCATION");
   }

   public static boolean isIntentSearchList(Intent var0) {
      return isRequiredAction(var0, "locus.api.android.INTENT_ITEM_SEARCH_LIST");
   }

   public static boolean isIntentTrackTools(Intent var0) {
      return isRequiredAction(var0, "locus.api.android.INTENT_ITEM_TRACK_TOOLS");
   }

   public static boolean isLocusAvailable(Context var0) {
      return isLocusAvailable(var0, LocusUtils.VersionCode.UPDATE_01);
   }

   public static boolean isLocusAvailable(Context var0, int var1, int var2, int var3) {
      boolean var4 = true;
      List var9 = getAvailableVersions(var0);
      int var5 = 0;
      int var6 = var9.size();

      boolean var8;
      while(true) {
         if (var5 >= var6) {
            var8 = false;
            break;
         }

         LocusUtils.LocusVersion var7 = (LocusUtils.LocusVersion)var9.get(var5);
         if (var7.isVersionFree() && var1 > 0 && var7.getVersionCode() >= var1) {
            var8 = var4;
            break;
         }

         if (var7.isVersionPro() && var2 > 0) {
            var8 = var4;
            if (var7.getVersionCode() >= var2) {
               break;
            }
         }

         if (var7.isVersionGis() && var3 > 0) {
            var8 = var4;
            if (var7.getVersionCode() >= var3) {
               break;
            }
         }

         ++var5;
      }

      return var8;
   }

   public static boolean isLocusAvailable(Context var0, LocusUtils.VersionCode var1) {
      return isLocusAvailable(var0, var1.vcFree, var1.vcPro, var1.vcGis);
   }

   public static boolean isLocusFreePro(LocusUtils.LocusVersion var0, int var1) {
      boolean var2 = true;
      boolean var3;
      if (var0 == null) {
         var3 = false;
      } else {
         if (var0.isVersionFree()) {
            var3 = var2;
            if (var0.getVersionCode() >= var1) {
               return var3;
            }
         }

         var3 = var2;
         if (var0.isVersionPro()) {
            var3 = var2;
            if (var0.getVersionCode() >= var1) {
               var3 = var2;
            }
         }
      }

      return var3;
   }

   private static boolean isPackageNameLocus(String var0) {
      boolean var1 = false;
      boolean var2 = var1;
      if (var0 != null) {
         if (var0.length() == 0) {
            var2 = var1;
         } else {
            var2 = var1;
            if (var0.startsWith("menion")) {
               if (!var0.equals("menion.android.locus") && !var0.startsWith("menion.android.locus.free")) {
                  var2 = var1;
                  if (!var0.startsWith("menion.android.locus.pro")) {
                     return var2;
                  }
               }

               var2 = true;
            }
         }
      }

      return var2;
   }

   private static boolean isRequiredAction(Intent var0, String var1) {
      boolean var2;
      if (var0 != null && var0.getAction() != null && var0.getAction().equals(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static Intent prepareResultExtraOnDisplayIntent(Waypoint var0, boolean var1) {
      Intent var2 = new Intent();
      addWaypointToIntent(var2, var0);
      var2.putExtra("INTENT_EXTRA_POINT_OVERWRITE", var1);
      return var2;
   }

   public static boolean sendGetLocationData(Activity var0, String var1, locus.api.objects.extra.Location var2) {
      boolean var3;
      if (var2 == null) {
         var3 = false;
      } else {
         Intent var4 = new Intent();
         if (!TextUtils.isEmpty(var1)) {
            var4.putExtra("INTENT_EXTRA_NAME", var1);
         }

         var4.putExtra("INTENT_EXTRA_LOCATION", var2.getAsBytes());
         var0.setResult(-1, var4);
         var0.finish();
         var3 = true;
      }

      return var3;
   }

   public static class LocusVersion extends Storable {
      private String mPackageName;
      private int mVersionCode;
      private String mVersionName;

      public LocusVersion() {
      }

      private LocusVersion(String var1, String var2, int var3) {
         String var4 = var1;
         if (var1 == null) {
            var4 = "";
         }

         this.mPackageName = var4;
         var1 = var2;
         if (var2 == null) {
            var1 = "";
         }

         this.mVersionName = var1;
         int var5 = var3;
         if (var3 < 0) {
            var5 = 0;
         }

         this.mVersionCode = var5;
      }

      // $FF: synthetic method
      LocusVersion(String var1, String var2, int var3, Object var4) {
         this(var1, var2, var3);
      }

      public String getPackageName() {
         return this.mPackageName;
      }

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
         boolean var1;
         if (!this.isVersionPro() && !this.isVersionGis()) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean isVersionGis() {
         return this.mPackageName.contains(".gis");
      }

      public boolean isVersionPro() {
         return this.mPackageName.contains(".pro");
      }

      public boolean isVersionValid(LocusUtils.VersionCode var1) {
         boolean var2 = true;
         if (this.isVersionFree()) {
            if (var1.vcFree == 0 || this.mVersionCode < var1.vcFree) {
               var2 = false;
            }
         } else if (this.isVersionPro()) {
            if (var1.vcPro == 0 || this.mVersionCode < var1.vcPro) {
               var2 = false;
            }
         } else if (this.isVersionGis()) {
            if (var1.vcGis == 0 || this.mVersionCode < var1.vcGis) {
               var2 = false;
            }
         } else {
            var2 = false;
         }

         return var2;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         this.mPackageName = var2.readString();
         this.mVersionName = var2.readString();
         this.mVersionCode = var2.readInt();
      }

      public void reset() {
         this.mPackageName = "";
         this.mVersionName = "";
         this.mVersionCode = 0;
      }

      public String toString() {
         return locus.api.utils.Utils.toString(this);
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         var1.writeString(this.mPackageName);
         var1.writeString(this.mVersionName);
         var1.writeInt(this.mVersionCode);
      }
   }

   public interface OnIntentGetLocation {
      void onFailed();

      void onReceived(locus.api.objects.extra.Location var1, locus.api.objects.extra.Location var2);
   }

   public interface OnIntentMainFunction {
      void onFailed();

      void onReceived(LocusUtils.LocusVersion var1, locus.api.objects.extra.Location var2, locus.api.objects.extra.Location var3);
   }

   public static enum VersionCode {
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

      private VersionCode(int var3, int var4, int var5) {
         this.vcFree = var3;
         this.vcPro = var4;
         this.vcGis = var5;
      }
   }
}
