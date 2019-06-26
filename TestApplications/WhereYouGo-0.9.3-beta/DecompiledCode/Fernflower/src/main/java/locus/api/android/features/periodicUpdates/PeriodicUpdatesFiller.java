package locus.api.android.features.periodicUpdates;

import android.content.Intent;
import java.io.IOException;
import locus.api.android.utils.LocusUtils;
import locus.api.objects.extra.TrackStats;

public class PeriodicUpdatesFiller {
   private static void addValuesBasicLocation(UpdateContainer var0, Intent var1) {
      var1.putExtra("1000", var0.enabledMyLocation);
      var1.putExtra("1001", var0.locMyLocation.getAsBytes());
      var1.putExtra("1005", var0.gpsSatsUsed);
      var1.putExtra("1006", var0.gpsSatsAll);
      var1.putExtra("1007", var0.declination);
      var1.putExtra("1014", var0.speedVertical);
      var1.putExtra("1015", var0.slope);
      var1.putExtra("1013", var0.orientGpsAngle);
      var1.putExtra("1008", var0.orientHeading);
      var1.putExtra("1009", var0.orientHeadingOpposit);
      var1.putExtra("1016", var0.orientCourse);
      var1.putExtra("1010", var0.orientPitch);
      var1.putExtra("1011", var0.orientRoll);
   }

   private static void addValuesGuiding(UpdateContainer var0, Intent var1) {
      var1.putExtra("1410", var0.guideType);
      if (var0.guideType != -1) {
         var1.putExtra("1401", var0.guideWptName);
         var1.putExtra("1402", var0.guideWptLoc.getAsBytes());
         var1.putExtra("1403", var0.guideWptDist);
         var1.putExtra("1405", var0.guideWptAzim);
         var1.putExtra("1406", var0.guideWptAngle);
         var1.putExtra("1407", var0.guideWptTime);
         var1.putExtra("1409", var0.guideDistFromStart);
         var1.putExtra("1404", var0.guideDistToFinish);
         var1.putExtra("1408", var0.guideTimeToFinish);
         if (var0.guideNavPoint1Loc != null) {
            var1.putExtra("1412", var0.guideNavPoint1Loc.getAsBytes());
            var1.putExtra("1411", var0.guideNavPoint1Name);
            var1.putExtra("1413", var0.guideNavPoint1Dist);
            var1.putExtra("1414", var0.guideNavPoint1Time);
            var1.putExtra("1419", var0.guideNavPoint1Action);
         }

         if (var0.guideNavPoint2Loc != null) {
            var1.putExtra("1416", var0.guideNavPoint2Loc.getAsBytes());
            var1.putExtra("1415", var0.guideNavPoint2Name);
            var1.putExtra("1417", var0.guideNavPoint2Dist);
            var1.putExtra("1418", var0.guideNavPoint2Time);
            var1.putExtra("1420", var0.guideNavPoint2Action);
         }
      }

   }

   private static void addValuesMap(UpdateContainer var0, Intent var1) {
      var1.putExtra("1300", var0.mapVisible);
      var1.putExtra("1302", var0.locMapCenter.getAsBytes());
      var1.putExtra("1303", var0.mapTopLeft.getAsBytes());
      var1.putExtra("1304", var0.mapBottomRight.getAsBytes());
      var1.putExtra("1305", var0.mapZoomLevel);
      var1.putExtra("1301", var0.mapRotate);
      var1.putExtra("1306", var0.isUserTouching);
   }

   private static void addValuesTrackRecording(UpdateContainer var0, Intent var1) {
      var1.putExtra("1200", var0.trackRecRecording);
      var1.putExtra("1201", var0.trackRecPaused);
      if (var0.trackRecRecording) {
         var1.putExtra("1216", var0.trackRecProfileName);
         var1.putExtra("1217", var0.trackStats.getAsBytes());
         var1.putExtra("1202", (double)var0.trackStats.getTotalLength());
         var1.putExtra("1203", (double)var0.trackStats.getEleNegativeDistance());
         var1.putExtra("1204", (double)var0.trackStats.getElePositiveDistance());
         var1.putExtra("1205", var0.trackStats.getAltitudeMin());
         var1.putExtra("1206", var0.trackStats.getAltitudeMax());
         var1.putExtra("1207", var0.trackStats.getEleNegativeHeight());
         var1.putExtra("1208", var0.trackStats.getElePositiveHeight());
         var1.putExtra("1209", var0.trackStats.getEleTotalAbsHeight());
         var1.putExtra("1210", var0.trackStats.getTotalTime());
         var1.putExtra("1211", var0.trackStats.getTotalTimeMove());
         var1.putExtra("1212", var0.trackStats.getSpeedAverage(false));
         var1.putExtra("1213", var0.trackStats.getSpeedAverage(true));
         var1.putExtra("1214", var0.trackStats.getSpeedMax());
         var1.putExtra("1215", var0.trackStats.getNumOfPoints());
      }

   }

   private static void addValuesVarious(UpdateContainer var0, Intent var1) {
      var1.putExtra("1500", var0.deviceBatteryValue);
      var1.putExtra("1501", var0.deviceBatteryTemperature);
   }

   public static UpdateContainer intentToUpdate(Intent var0, PeriodicUpdatesHandler var1) {
      UpdateContainer var2 = new UpdateContainer();
      var2.enabledMyLocation = var0.getBooleanExtra("1000", false);
      var2.newMyLocation = false;
      var2.locMyLocation = LocusUtils.getLocationFromIntent(var0, "1001");
      if (var2.enabledMyLocation && (var1.mLastGps == null || (double)var1.mLastGps.distanceTo(var2.locMyLocation) > var1.mLocMinDistance)) {
         var1.mLastGps = var2.locMyLocation;
         var2.newMyLocation = true;
      }

      var2.gpsSatsUsed = var0.getIntExtra("1005", 0);
      var2.gpsSatsAll = var0.getIntExtra("1006", 0);
      var2.declination = var0.getFloatExtra("1007", 0.0F);
      var2.speedVertical = var0.getFloatExtra("1014", 0.0F);
      var2.slope = var0.getFloatExtra("1015", 0.0F);
      var2.orientGpsAngle = var0.getFloatExtra("1013", 0.0F);
      var2.orientHeading = var0.getFloatExtra("1008", 0.0F);
      var2.orientHeadingOpposit = var0.getFloatExtra("1009", 0.0F);
      var2.orientCourse = var0.getFloatExtra("1016", 0.0F);
      var2.orientPitch = var0.getFloatExtra("1010", 0.0F);
      var2.orientRoll = var0.getFloatExtra("1011", 0.0F);
      var2.mapVisible = var0.getBooleanExtra("1300", false);
      var2.newMapCenter = false;
      var2.locMapCenter = LocusUtils.getLocationFromIntent(var0, "1302");
      if (var1.mLastMapCenter == null || (double)var1.mLastMapCenter.distanceTo(var2.locMapCenter) > var1.mLocMinDistance) {
         var1.mLastMapCenter = var2.locMapCenter;
         var2.newMapCenter = true;
      }

      var2.mapTopLeft = LocusUtils.getLocationFromIntent(var0, "1303");
      var2.mapBottomRight = LocusUtils.getLocationFromIntent(var0, "1304");
      var2.mapZoomLevel = var0.getIntExtra("1305", 0);
      boolean var3;
      if (var2.mapZoomLevel != var1.mLastZoomLevel) {
         var3 = true;
      } else {
         var3 = false;
      }

      var2.newZoomLevel = var3;
      var1.mLastZoomLevel = var2.mapZoomLevel;
      var2.mapRotate = var0.getFloatExtra("1301", 0.0F);
      var2.isUserTouching = var0.getBooleanExtra("1306", false);
      var2.trackRecRecording = var0.getBooleanExtra("1200", false);
      if (var2.trackRecRecording) {
         var2.trackRecPaused = var0.getBooleanExtra("1201", false);
         var2.trackRecProfileName = var0.getStringExtra("1216");

         label50: {
            IOException var10000;
            label69: {
               boolean var10001;
               byte[] var7;
               try {
                  var7 = var0.getByteArrayExtra("1217");
               } catch (IOException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label69;
               }

               if (var7 == null) {
                  break label50;
               }

               try {
                  if (var7.length > 0) {
                     TrackStats var4 = new TrackStats();
                     var2.trackStats = var4;
                     var2.trackStats.read(var7);
                  }
                  break label50;
               } catch (IOException var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            IOException var8 = var10000;
            var8.printStackTrace();
         }

         if (var2.trackStats == null) {
            var2.trackStats = new TrackStats();
            var2.trackStats.setTotalLength((float)var0.getDoubleExtra("1202", 0.0D));
            var2.trackStats.setEleNegativeDistance((float)var0.getDoubleExtra("1203", 0.0D));
            var2.trackStats.setElePositiveDistance((float)var0.getDoubleExtra("1204", 0.0D));
            var2.trackStats.setAltitudeMin(var0.getFloatExtra("1205", 0.0F));
            var2.trackStats.setAltitudeMax(var0.getFloatExtra("1206", 0.0F));
            var2.trackStats.setEleNegativeHeight(var0.getFloatExtra("1207", 0.0F));
            var2.trackStats.setElePositiveHeight(var0.getFloatExtra("1208", 0.0F));
            var2.trackStats.setEleTotalAbsHeight(var0.getFloatExtra("1209", 0.0F));
            var2.trackStats.setTotalTime(var0.getLongExtra("1210", 0L));
            var2.trackStats.setTotalTimeMove(var0.getLongExtra("1211", 0L));
            var2.trackStats.setSpeedMax(var0.getFloatExtra("1214", 0.0F));
            var2.trackStats.setNumOfPoints(var0.getIntExtra("1215", 0));
         }
      }

      var2.guideType = var0.getIntExtra("1410", -1);
      if (var2.guideType != -1) {
         var2.guideWptName = var0.getStringExtra("1401");
         var2.guideWptLoc = LocusUtils.getLocationFromIntent(var0, "1402");
         var2.guideWptDist = var0.getDoubleExtra("1403", 0.0D);
         var2.guideWptAzim = var0.getFloatExtra("1405", 0.0F);
         var2.guideWptAngle = var0.getFloatExtra("1406", 0.0F);
         var2.guideWptTime = var0.getLongExtra("1407", 0L);
         var2.guideDistFromStart = var0.getDoubleExtra("1409", 0.0D);
         var2.guideDistToFinish = var0.getDoubleExtra("1404", 0.0D);
         var2.guideTimeToFinish = var0.getLongExtra("1408", 0L);
         var2.guideNavPoint1Loc = LocusUtils.getLocationFromIntent(var0, "1412");
         if (var2.guideNavPoint1Loc != null) {
            var2.guideNavPoint1Name = var0.getStringExtra("1411");
            var2.guideNavPoint1Dist = var0.getDoubleExtra("1413", 0.0D);
            var2.guideNavPoint1Time = var0.getLongExtra("1414", 0L);
            var2.guideNavPoint1Action = var0.getIntExtra("1419", var2.guideNavPoint1Action);
         }

         var2.guideNavPoint2Loc = LocusUtils.getLocationFromIntent(var0, "1416");
         if (var2.guideNavPoint2Loc != null) {
            var2.guideNavPoint2Name = var0.getStringExtra("1415");
            var2.guideNavPoint2Dist = var0.getDoubleExtra("1417", 0.0D);
            var2.guideNavPoint2Time = var0.getLongExtra("1418", 0L);
            var2.guideNavPoint2Action = var0.getIntExtra("1420", var2.guideNavPoint2Action);
         }
      }

      var2.deviceBatteryValue = var0.getIntExtra("1500", 0);
      var2.deviceBatteryTemperature = var0.getFloatExtra("1501", 0.0F);
      return var2;
   }

   public static Intent updateToIntent(String var0, UpdateContainer var1) {
      Intent var2 = new Intent(var0);
      addValuesBasicLocation(var1, var2);
      addValuesMap(var1, var2);
      addValuesTrackRecording(var1, var2);
      addValuesGuiding(var1, var2);
      addValuesVarious(var1, var2);
      return var2;
   }
}
