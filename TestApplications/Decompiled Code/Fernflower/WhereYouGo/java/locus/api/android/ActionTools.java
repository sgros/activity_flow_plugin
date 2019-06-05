package locus.api.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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
import locus.api.android.utils.LocusInfo;
import locus.api.android.utils.LocusUtils;
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

   public static void actionPickDir(Activity var0, int var1) throws ActivityNotFoundException {
      intentPick("org.openintents.action.PICK_DIRECTORY", var0, var1, (String)null, (String[])null);
   }

   public static void actionPickDir(Activity var0, int var1, String var2) throws ActivityNotFoundException {
      intentPick("org.openintents.action.PICK_DIRECTORY", var0, var1, var2, (String[])null);
   }

   public static void actionPickFile(Activity var0, int var1) throws ActivityNotFoundException {
      intentPick("org.openintents.action.PICK_FILE", var0, var1, (String)null, (String[])null);
   }

   public static void actionPickFile(Activity var0, int var1, String var2, String[] var3) throws ActivityNotFoundException {
      intentPick("org.openintents.action.PICK_FILE", var0, var1, var2, var3);
   }

   public static void actionPickLocation(Activity var0) throws RequiredVersionMissingException {
      if (LocusUtils.isLocusAvailable(var0, 235, 235, 0)) {
         var0.startActivity(new Intent("locus.api.android.ACTION_PICK_LOCATION"));
      } else {
         throw new RequiredVersionMissingException(235);
      }
   }

   public static void actionStartGuiding(Activity var0, String var1, double var2, double var4) throws RequiredVersionMissingException {
      if (LocusUtils.isLocusAvailable(var0, 243, 243, 0)) {
         Intent var6 = new Intent("locus.api.android.ACTION_GUIDING_START");
         if (var1 != null) {
            var6.putExtra("INTENT_EXTRA_NAME", var1);
         }

         var6.putExtra("INTENT_EXTRA_LATITUDE", var2);
         var6.putExtra("INTENT_EXTRA_LONGITUDE", var4);
         var0.startActivity(var6);
      } else {
         throw new RequiredVersionMissingException(243);
      }
   }

   public static void actionStartGuiding(Activity var0, Waypoint var1) throws RequiredVersionMissingException {
      if (LocusUtils.isLocusAvailable(var0, 243, 243, 0)) {
         Intent var2 = new Intent("locus.api.android.ACTION_GUIDING_START");
         LocusUtils.addWaypointToIntent(var2, var1);
         var0.startActivity(var2);
      } else {
         throw new RequiredVersionMissingException(243);
      }
   }

   public static void actionStartNavigation(Activity var0, String var1) throws RequiredVersionMissingException {
      if (!LocusUtils.isLocusAvailable(var0, LocusUtils.VersionCode.UPDATE_08)) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_08);
      } else {
         Intent var2 = new Intent("locus.api.android.ACTION_NAVIGATION_START");
         var2.putExtra("INTENT_EXTRA_ADDRESS_TEXT", var1);
         var0.startActivity(var2);
      }
   }

   public static void actionStartNavigation(Activity var0, String var1, double var2, double var4) throws RequiredVersionMissingException {
      if (!LocusUtils.isLocusAvailable(var0, LocusUtils.VersionCode.UPDATE_01)) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
      } else {
         Intent var6 = new Intent("locus.api.android.ACTION_NAVIGATION_START");
         if (var1 != null) {
            var6.putExtra("INTENT_EXTRA_NAME", var1);
         }

         var6.putExtra("INTENT_EXTRA_LATITUDE", var2);
         var6.putExtra("INTENT_EXTRA_LONGITUDE", var4);
         var0.startActivity(var6);
      }
   }

   public static void actionStartNavigation(Activity var0, Waypoint var1) throws RequiredVersionMissingException {
      if (!LocusUtils.isLocusAvailable(var0, LocusUtils.VersionCode.UPDATE_01)) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
      } else {
         Intent var2 = new Intent("locus.api.android.ACTION_NAVIGATION_START");
         LocusUtils.addWaypointToIntent(var2, var1);
         var0.startActivity(var2);
      }
   }

   private static Intent actionTrackRecord(String var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      int var2 = LocusUtils.VersionCode.UPDATE_02.vcFree;
      if (!LocusUtils.isLocusFreePro(var1, var2)) {
         throw new RequiredVersionMissingException(var2);
      } else {
         Intent var3 = new Intent(var0);
         var3.setPackage(var1.getPackageName());
         return var3;
      }
   }

   public static void actionTrackRecordAddWpt(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      actionTrackRecordAddWpt(var0, var1, false);
   }

   public static void actionTrackRecordAddWpt(Context var0, LocusUtils.LocusVersion var1, String var2, String var3) throws RequiredVersionMissingException {
      Intent var4 = actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_ADD_WPT", var1);
      if (var2 != null && var2.length() > 0) {
         var4.putExtra("INTENT_EXTRA_NAME", var2);
      }

      var4.putExtra("INTENT_EXTRA_TRACK_REC_AUTO_SAVE", false);
      var4.putExtra("INTENT_EXTRA_TRACK_REC_ACTION_AFTER", var3);
      var0.sendBroadcast(var4);
   }

   public static void actionTrackRecordAddWpt(Context var0, LocusUtils.LocusVersion var1, String var2, boolean var3) throws RequiredVersionMissingException {
      Intent var4 = actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_ADD_WPT", var1);
      if (var2 != null && var2.length() > 0) {
         var4.putExtra("INTENT_EXTRA_NAME", var2);
      }

      var4.putExtra("INTENT_EXTRA_TRACK_REC_AUTO_SAVE", var3);
      var0.sendBroadcast(var4);
   }

   public static void actionTrackRecordAddWpt(Context var0, LocusUtils.LocusVersion var1, boolean var2) throws RequiredVersionMissingException {
      actionTrackRecordAddWpt(var0, var1, (String)null, var2);
   }

   public static void actionTrackRecordPause(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      var0.sendBroadcast(actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_PAUSE", var1));
   }

   public static void actionTrackRecordStart(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      actionTrackRecordStart(var0, var1, (String)null);
   }

   public static void actionTrackRecordStart(Context var0, LocusUtils.LocusVersion var1, String var2) throws RequiredVersionMissingException {
      Intent var3 = actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_START", var1);
      if (var2 != null && var2.length() > 0) {
         var3.putExtra("INTENT_EXTRA_TRACK_REC_PROFILE", var2);
      }

      var0.sendBroadcast(var3);
   }

   public static void actionTrackRecordStop(Context var0, LocusUtils.LocusVersion var1, boolean var2) throws RequiredVersionMissingException {
      Intent var3 = actionTrackRecord("locus.api.android.ACTION_TRACK_RECORD_STOP", var1);
      var3.putExtra("INTENT_EXTRA_TRACK_REC_AUTO_SAVE", var2);
      var0.sendBroadcast(var3);
   }

   public static void callAddNewWmsMap(Context var0, String var1) throws RequiredVersionMissingException, InvalidObjectException {
      if (!LocusUtils.isLocusAvailable(var0, LocusUtils.VersionCode.UPDATE_01)) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
      } else if (TextUtils.isEmpty(var1)) {
         throw new InvalidObjectException("WMS Url address '" + var1 + "', is not valid!");
      } else {
         Intent var2 = new Intent("locus.api.android.ACTION_ADD_NEW_WMS_MAP");
         var2.putExtra("INTENT_EXTRA_ADD_NEW_WMS_MAP_URL", var1);
         var0.startActivity(var2);
      }
   }

   public static void disablePeriodicUpdatesReceiver(Context var0, LocusUtils.LocusVersion var1, Class var2) throws RequiredVersionMissingException {
      Logger.logD("ActionTools", "disableReceiver(" + var0 + ")");
      var0.getPackageManager().setComponentEnabledSetting(new ComponentName(var0, var2), 2, 1);
      refreshPeriodicUpdateListeners(var0, var1);
   }

   public static void displayLocusStoreItemDetail(Context var0, LocusUtils.LocusVersion var1, long var2) throws RequiredVersionMissingException {
      if (var1 != null && var1.isVersionValid(LocusUtils.VersionCode.UPDATE_12)) {
         Intent var4 = new Intent("locus.api.android.ACTION_DISPLAY_STORE_ITEM");
         var4.putExtra("INTENT_EXTRA_ITEM_ID", var2);
         var0.startActivity(var4);
      } else {
         Logger.logW("ActionTools", "displayLocusStoreItemDetail(), invalid Locus version");
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_12);
      }
   }

   public static void displayWaypointScreen(Context var0, LocusUtils.LocusVersion var1, long var2) throws RequiredVersionMissingException {
      displayWaypointScreen(var0, var1, var2, "");
   }

   private static void displayWaypointScreen(Context var0, LocusUtils.LocusVersion var1, long var2, String var4) throws RequiredVersionMissingException {
      if (!LocusUtils.isLocusFreePro(var1, LocusUtils.VersionCode.UPDATE_07.vcFree)) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_07);
      } else {
         Intent var5 = new Intent("locus.api.android.ACTION_DISPLAY_POINT_SCREEN");
         var5.putExtra("INTENT_EXTRA_ITEM_ID", var2);
         if (var4 != null && var4.length() > 0) {
            var5.putExtra("TAG_EXTRA_CALLBACK", var4);
         }

         var0.startActivity(var5);
      }
   }

   public static void displayWaypointScreen(Context var0, LocusUtils.LocusVersion var1, long var2, String var4, String var5, String var6, String var7) throws RequiredVersionMissingException {
      displayWaypointScreen(var0, var1, var2, ExtraData.generateCallbackString("", var4, var5, var6, var7));
   }

   public static void enablePeriodicUpdatesReceiver(Context var0, LocusUtils.LocusVersion var1, Class var2) throws RequiredVersionMissingException {
      Logger.logD("ActionTools", "enableReceiver(" + var0 + ")");
      var0.getPackageManager().setComponentEnabledSetting(new ComponentName(var0, var2), 1, 1);
      refreshPeriodicUpdateListeners(var0, var1);
   }

   private static Uri getContentProviderData(LocusUtils.LocusVersion var0, LocusUtils.VersionCode var1, String var2) {
      return getContentProviderUri(var0, var1, "LocusDataProvider", var2);
   }

   public static Uri getContentProviderGeocaching(LocusUtils.LocusVersion var0, LocusUtils.VersionCode var1, String var2) {
      return getContentProviderUri(var0, var1, "GeocachingDataProvider", var2);
   }

   private static Uri getContentProviderUri(LocusUtils.LocusVersion var0, LocusUtils.VersionCode var1, String var2, String var3) {
      Object var4 = null;
      Uri var5;
      if (var2 != null && var2.length() != 0 && var3 != null && var3.length() != 0) {
         if (var0 != null && var1 != null && var0.isVersionValid(var1)) {
            StringBuilder var6 = new StringBuilder();
            if (var0.isVersionFree()) {
               var6.append("content://menion.android.locus.free");
            } else if (var0.isVersionPro()) {
               var6.append("content://menion.android.locus.pro");
            } else {
               if (!var0.isVersionGis()) {
                  Logger.logW("ActionTools", "getContentProviderUri(), unknown Locus version:" + var0);
                  var5 = (Uri)var4;
                  return var5;
               }

               var6.append("content://menion.android.locus.gis");
            }

            var5 = Uri.parse(var6.append(".").append(var2).append("/").append(var3).toString());
         } else {
            Logger.logW("ActionTools", "getContentProviderUri(), invalid Locus version");
            var5 = (Uri)var4;
         }
      } else {
         Logger.logW("ActionTools", "getContentProviderUri(), invalid 'authority' or 'path'parameters");
         var5 = (Uri)var4;
      }

      return var5;
   }

   public static int getItemPurchaseState(Context var0, LocusUtils.LocusVersion var1, long var2) throws RequiredVersionMissingException {
      Uri var4 = getContentProviderData(var1, LocusUtils.VersionCode.UPDATE_06, "itemPurchaseState");
      if (var4 == null) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_06);
      } else {
         var4 = ContentUris.withAppendedId(var4, var2);
         Cursor var13 = var0.getContentResolver().query(var4, (String[])null, (String)null, (String[])null, (String)null);
         int var5 = 0;

         while(true) {
            boolean var10 = false;

            label92: {
               label91: {
                  label90: {
                     label89: {
                        try {
                           var10 = true;
                           if (var5 < var13.getCount()) {
                              var13.moveToPosition(var5);
                              if (var13.getString(0).equals("purchaseState")) {
                                 var5 = var13.getInt(1);
                                 var10 = false;
                                 break label92;
                              }

                              var10 = false;
                              break label91;
                           }

                           var10 = false;
                           break label89;
                        } catch (Exception var11) {
                           StringBuilder var7 = new StringBuilder();
                           Logger.logE("ActionTools", var7.append("getItemPurchaseState(").append(var0).append(", ").append(var1).append(")").toString(), var11);
                           var10 = false;
                        } finally {
                           if (var10) {
                              Utils.closeQuietly(var13);
                           }
                        }

                        Utils.closeQuietly(var13);
                        break label90;
                     }

                     Utils.closeQuietly(var13);
                  }

                  var5 = 0;
                  break;
               }

               ++var5;
               continue;
            }

            Utils.closeQuietly(var13);
            break;
         }

         return var5;
      }
   }

   public static LocusInfo getLocusInfo(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      Object var2 = null;
      Uri var3 = getContentProviderData(var1, LocusUtils.VersionCode.UPDATE_01, "info");
      if (var3 == null) {
         Logger.logD("ActionTools", "getLocusInfo(" + var0 + ", " + var1 + "), invalid version");
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
      } else {
         Cursor var4 = var0.getContentResolver().query(var3, (String[])null, (String)null, (String[])null, (String)null);
         boolean var8 = false;

         LocusInfo var11;
         LocusInfo var12;
         label62: {
            try {
               var8 = true;
               var12 = LocusInfo.create(var4);
               var8 = false;
               break label62;
            } catch (Exception var9) {
               StringBuilder var5 = new StringBuilder();
               Logger.logE("ActionTools", var5.append("getLocusInfo(").append(var0).append(", ").append(var1).append(")").toString(), var9);
               var8 = false;
            } finally {
               if (var8) {
                  Utils.closeQuietly(var4);
               }
            }

            Utils.closeQuietly(var4);
            var11 = (LocusInfo)var2;
            return var11;
         }

         var11 = var12;
         Utils.closeQuietly(var4);
         return var11;
      }
   }

   private static LocusInfo getLocusInfoData(Context var0) throws RequiredVersionMissingException {
      return getLocusInfo(var0, LocusUtils.createLocusVersion(var0));
   }

   @Deprecated
   public static String getLocusRootDirectory(Context var0) throws RequiredVersionMissingException {
      LocusInfo var1 = getLocusInfoData(var0);
      String var2;
      if (var1 != null) {
         var2 = var1.getRootDirectory();
      } else {
         var2 = null;
      }

      return var2;
   }

   public static Track getLocusTrack(Context var0, LocusUtils.LocusVersion var1, long var2) throws RequiredVersionMissingException {
      Object var4 = null;
      int var5 = LocusUtils.VersionCode.UPDATE_10.vcFree;
      if (!LocusUtils.isLocusFreePro(var1, var5)) {
         throw new RequiredVersionMissingException(var5);
      } else {
         Uri var13 = getContentProviderData(var1, LocusUtils.VersionCode.UPDATE_10, "track");
         if (var13 == null) {
            throw new RequiredVersionMissingException(var5);
         } else {
            var13 = ContentUris.withAppendedId(var13, var2);
            Cursor var6 = var0.getContentResolver().query(var13, (String[])null, (String)null, (String[])null, (String)null);
            Track var12;
            if (var6 != null && var6.moveToFirst()) {
               Track var15;
               label81: {
                  try {
                     var15 = new Track(var6.getBlob(1));
                     break label81;
                  } catch (Exception var10) {
                     StringBuilder var14 = new StringBuilder();
                     Logger.logE("ActionTools", var14.append("getLocusTrack(").append(var0).append(", ").append(var2).append(")").toString(), var10);
                  } finally {
                     Utils.closeQuietly(var6);
                  }

                  var12 = (Track)var4;
                  return var12;
               }

               var12 = var15;
            } else {
               Logger.logW("ActionTools", "getLocusTrack(" + var0 + ", " + var2 + "), " + "'cursor' in not valid");
               var12 = (Track)var4;
            }

            return var12;
         }
      }
   }

   public static Waypoint getLocusWaypoint(Context var0, LocusUtils.LocusVersion var1, long var2) throws RequiredVersionMissingException {
      Object var4 = null;
      int var5 = LocusUtils.VersionCode.UPDATE_01.vcFree;
      if (!LocusUtils.isLocusFreePro(var1, var5)) {
         throw new RequiredVersionMissingException(var5);
      } else {
         Uri var13 = getContentProviderData(var1, LocusUtils.VersionCode.UPDATE_01, "waypoint");
         if (var13 == null) {
            throw new RequiredVersionMissingException(var5);
         } else {
            var13 = ContentUris.withAppendedId(var13, var2);
            Cursor var6 = var0.getContentResolver().query(var13, (String[])null, (String)null, (String[])null, (String)null);
            Waypoint var12;
            if (var6 != null && var6.moveToFirst()) {
               Waypoint var14;
               label81: {
                  try {
                     var14 = new Waypoint(var6.getBlob(1));
                     break label81;
                  } catch (Exception var10) {
                     StringBuilder var7 = new StringBuilder();
                     Logger.logE("ActionTools", var7.append("getLocusWaypoint(").append(var0).append(", ").append(var2).append(")").toString(), var10);
                  } finally {
                     Utils.closeQuietly(var6);
                  }

                  var12 = (Waypoint)var4;
                  return var12;
               }

               var12 = var14;
            } else {
               Logger.logW("ActionTools", "getLocusWaypoint(" + var0 + ", " + var2 + "), " + "'cursor' in not valid");
               var12 = (Waypoint)var4;
            }

            return var12;
         }
      }
   }

   public static long[] getLocusWaypointId(Context param0, LocusUtils.LocusVersion param1, String param2) throws RequiredVersionMissingException {
      // $FF: Couldn't be decompiled
   }

   public static ActionTools.BitmapLoadResult getMapPreview(Context param0, LocusUtils.LocusVersion param1, Location param2, int param3, int param4, int param5, boolean param6) throws RequiredVersionMissingException {
      // $FF: Couldn't be decompiled
   }

   public static List getTrackRecordingProfiles(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      Uri var2 = getContentProviderData(var1, LocusUtils.VersionCode.UPDATE_09, "trackRecordProfileNames");
      if (var2 == null) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_09);
      } else {
         Cursor var12 = var0.getContentResolver().query(var2, (String[])null, (String)null, (String[])null, (String)null);
         ArrayList var3 = new ArrayList();
         int var4 = 0;

         while(true) {
            boolean var9 = false;

            label71: {
               label70: {
                  try {
                     var9 = true;
                     if (var4 < var12.getCount()) {
                        var12.moveToPosition(var4);
                        ActionTools.TrackRecordProfileSimple var13 = new ActionTools.TrackRecordProfileSimple(var12.getLong(0), var12.getString(1), var12.getString(2), var12.getBlob(3));
                        var3.add(var13);
                        var9 = false;
                        break label71;
                     }

                     var9 = false;
                     break label70;
                  } catch (Exception var10) {
                     StringBuilder var5 = new StringBuilder();
                     Logger.logE("ActionTools", var5.append("getItemPurchaseState(").append(var0).append(", ").append(var1).append(")").toString(), var10);
                     var9 = false;
                  } finally {
                     if (var9) {
                        Utils.closeQuietly(var12);
                     }
                  }

                  Utils.closeQuietly(var12);
                  break;
               }

               Utils.closeQuietly(var12);
               break;
            }

            ++var4;
         }

         return var3;
      }
   }

   private static void intentPick(String var0, Activity var1, int var2, String var3, String[] var4) {
      Intent var5 = new Intent(var0);
      if (var3 != null && var3.length() > 0) {
         var5.putExtra("org.openintents.extra.TITLE", var3);
      }

      if (var4 != null && var4.length > 0) {
         var5.putExtra("org.openintents.extra.FILTER", var4);
      }

      var1.startActivityForResult(var5, var2);
   }

   @Deprecated
   public static boolean isPeriodicUpdatesEnabled(Context var0) throws RequiredVersionMissingException {
      LocusInfo var2 = getLocusInfoData(var0);
      boolean var1;
      if (var2 != null && var2.isPeriodicUpdatesEnabled()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static void refreshPeriodicUpdateListeners(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      if (!LocusUtils.isLocusFreePro(var1, LocusUtils.VersionCode.UPDATE_01.vcFree)) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_01);
      } else {
         Intent var2 = new Intent("com.asamm.locus.ACTION_REFRESH_PERIODIC_UPDATE_LISTENERS");
         var2.setPackage(var1.getPackageName());
         var0.sendBroadcast(var2);
      }
   }

   public static int updateLocusWaypoint(Context var0, LocusUtils.LocusVersion var1, Waypoint var2, boolean var3) throws RequiredVersionMissingException {
      return updateLocusWaypoint(var0, var1, var2, var3, false);
   }

   public static int updateLocusWaypoint(Context var0, LocusUtils.LocusVersion var1, Waypoint var2, boolean var3, boolean var4) throws RequiredVersionMissingException {
      int var5 = LocusUtils.VersionCode.UPDATE_01.vcFree;
      if (!LocusUtils.isLocusFreePro(var1, var5)) {
         throw new RequiredVersionMissingException(var5);
      } else {
         Uri var7 = getContentProviderData(var1, LocusUtils.VersionCode.UPDATE_01, "waypoint");
         if (var7 != null) {
            ContentValues var6 = new ContentValues();
            var6.put("waypoint", var2.getAsBytes());
            var6.put("forceOverwrite", var3);
            var6.put("loadAllGcWaypoints", var4);
            return var0.getContentResolver().update(var7, var6, (String)null, (String[])null);
         } else {
            throw new RequiredVersionMissingException(var5);
         }
      }
   }

   public static class BitmapLoadResult extends Storable {
      private byte[] mImg;
      private int mNotYetLoadedTiles;

      public BitmapLoadResult() {
      }

      private BitmapLoadResult(byte[] var1, int var2) {
         this.mImg = var1;
         this.mNotYetLoadedTiles = var2;
      }

      // $FF: synthetic method
      BitmapLoadResult(byte[] var1, int var2, Object var3) {
         this(var1, var2);
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

      protected int getVersion() {
         return 0;
      }

      public boolean isValid() {
         boolean var1;
         if (this.mImg != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         var1 = var2.readInt();
         if (var1 > 0) {
            this.mImg = new byte[var1];
            var2.readBytes(this.mImg);
            this.mNotYetLoadedTiles = var2.readInt();
         }

      }

      public void reset() {
         this.mImg = null;
         this.mNotYetLoadedTiles = 0;
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         if (this.mImg != null && this.mImg.length != 0) {
            var1.writeInt(this.mImg.length);
            var1.write(this.mImg);
         } else {
            var1.writeInt(0);
         }

         var1.writeInt(this.mNotYetLoadedTiles);
      }
   }

   public static class TrackRecordProfileSimple extends Storable {
      private String mDesc;
      private long mId;
      private byte[] mImg;
      private String mName;

      public TrackRecordProfileSimple() {
      }

      private TrackRecordProfileSimple(long var1, String var3, String var4, byte[] var5) {
         this.mId = var1;
         String var6 = var3;
         if (var3 == null) {
            var6 = "";
         }

         this.mName = var6;
         var3 = var4;
         if (var4 == null) {
            var3 = "";
         }

         this.mDesc = var3;
         this.mImg = var5;
      }

      // $FF: synthetic method
      TrackRecordProfileSimple(long var1, String var3, String var4, byte[] var5, Object var6) {
         this(var1, var3, var4, var5);
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

      protected int getVersion() {
         return 0;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         this.mId = var2.readLong();
         this.mName = var2.readString();
         this.mDesc = var2.readString();
         var1 = var2.readInt();
         if (var1 > 0) {
            this.mImg = new byte[var1];
            var2.readBytes(this.mImg);
         }

      }

      public void reset() {
         this.mId = 0L;
         this.mName = "";
         this.mDesc = "";
         this.mImg = null;
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         var1.writeLong(this.mId);
         var1.writeString(this.mName);
         var1.writeString(this.mDesc);
         int var2;
         if (this.mImg != null) {
            var2 = this.mImg.length;
         } else {
            var2 = 0;
         }

         var1.writeInt(var2);
         if (var2 > 0) {
            var1.write(this.mImg);
         }

      }
   }
}
