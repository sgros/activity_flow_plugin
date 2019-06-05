package locus.api.android.features.periodicUpdates;

import android.content.Intent;
import java.io.IOException;
import locus.api.android.utils.LocusUtils;
import locus.api.objects.extra.TrackStats;

public class PeriodicUpdatesFiller {
    public static UpdateContainer intentToUpdate(Intent i, PeriodicUpdatesHandler pu) {
        UpdateContainer update = new UpdateContainer();
        update.enabledMyLocation = i.getBooleanExtra("1000", false);
        update.newMyLocation = false;
        update.locMyLocation = LocusUtils.getLocationFromIntent(i, "1001");
        if (update.enabledMyLocation && (pu.mLastGps == null || ((double) pu.mLastGps.distanceTo(update.locMyLocation)) > pu.mLocMinDistance)) {
            pu.mLastGps = update.locMyLocation;
            update.newMyLocation = true;
        }
        update.gpsSatsUsed = i.getIntExtra("1005", 0);
        update.gpsSatsAll = i.getIntExtra("1006", 0);
        update.declination = i.getFloatExtra("1007", 0.0f);
        update.speedVertical = i.getFloatExtra("1014", 0.0f);
        update.slope = i.getFloatExtra("1015", 0.0f);
        update.orientGpsAngle = i.getFloatExtra("1013", 0.0f);
        update.orientHeading = i.getFloatExtra("1008", 0.0f);
        update.orientHeadingOpposit = i.getFloatExtra("1009", 0.0f);
        update.orientCourse = i.getFloatExtra("1016", 0.0f);
        update.orientPitch = i.getFloatExtra("1010", 0.0f);
        update.orientRoll = i.getFloatExtra("1011", 0.0f);
        update.mapVisible = i.getBooleanExtra("1300", false);
        update.newMapCenter = false;
        update.locMapCenter = LocusUtils.getLocationFromIntent(i, "1302");
        if (pu.mLastMapCenter == null || ((double) pu.mLastMapCenter.distanceTo(update.locMapCenter)) > pu.mLocMinDistance) {
            pu.mLastMapCenter = update.locMapCenter;
            update.newMapCenter = true;
        }
        update.mapTopLeft = LocusUtils.getLocationFromIntent(i, "1303");
        update.mapBottomRight = LocusUtils.getLocationFromIntent(i, "1304");
        update.mapZoomLevel = i.getIntExtra("1305", 0);
        update.newZoomLevel = update.mapZoomLevel != pu.mLastZoomLevel;
        pu.mLastZoomLevel = update.mapZoomLevel;
        update.mapRotate = i.getFloatExtra("1301", 0.0f);
        update.isUserTouching = i.getBooleanExtra("1306", false);
        update.trackRecRecording = i.getBooleanExtra("1200", false);
        if (update.trackRecRecording) {
            update.trackRecPaused = i.getBooleanExtra("1201", false);
            update.trackRecProfileName = i.getStringExtra("1216");
            try {
                byte[] data = i.getByteArrayExtra("1217");
                if (data != null && data.length > 0) {
                    update.trackStats = new TrackStats();
                    update.trackStats.read(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (update.trackStats == null) {
                update.trackStats = new TrackStats();
                update.trackStats.setTotalLength((float) i.getDoubleExtra("1202", 0.0d));
                update.trackStats.setEleNegativeDistance((float) i.getDoubleExtra("1203", 0.0d));
                update.trackStats.setElePositiveDistance((float) i.getDoubleExtra("1204", 0.0d));
                update.trackStats.setAltitudeMin(i.getFloatExtra("1205", 0.0f));
                update.trackStats.setAltitudeMax(i.getFloatExtra("1206", 0.0f));
                update.trackStats.setEleNegativeHeight(i.getFloatExtra("1207", 0.0f));
                update.trackStats.setElePositiveHeight(i.getFloatExtra("1208", 0.0f));
                update.trackStats.setEleTotalAbsHeight(i.getFloatExtra("1209", 0.0f));
                update.trackStats.setTotalTime(i.getLongExtra("1210", 0));
                update.trackStats.setTotalTimeMove(i.getLongExtra("1211", 0));
                update.trackStats.setSpeedMax(i.getFloatExtra("1214", 0.0f));
                update.trackStats.setNumOfPoints(i.getIntExtra("1215", 0));
            }
        }
        update.guideType = i.getIntExtra("1410", -1);
        if (update.guideType != -1) {
            update.guideWptName = i.getStringExtra("1401");
            update.guideWptLoc = LocusUtils.getLocationFromIntent(i, "1402");
            update.guideWptDist = i.getDoubleExtra("1403", 0.0d);
            update.guideWptAzim = i.getFloatExtra("1405", 0.0f);
            update.guideWptAngle = i.getFloatExtra("1406", 0.0f);
            update.guideWptTime = i.getLongExtra("1407", 0);
            update.guideDistFromStart = i.getDoubleExtra("1409", 0.0d);
            update.guideDistToFinish = i.getDoubleExtra("1404", 0.0d);
            update.guideTimeToFinish = i.getLongExtra("1408", 0);
            update.guideNavPoint1Loc = LocusUtils.getLocationFromIntent(i, "1412");
            if (update.guideNavPoint1Loc != null) {
                update.guideNavPoint1Name = i.getStringExtra("1411");
                update.guideNavPoint1Dist = i.getDoubleExtra("1413", 0.0d);
                update.guideNavPoint1Time = i.getLongExtra("1414", 0);
                update.guideNavPoint1Action = i.getIntExtra("1419", update.guideNavPoint1Action);
            }
            update.guideNavPoint2Loc = LocusUtils.getLocationFromIntent(i, "1416");
            if (update.guideNavPoint2Loc != null) {
                update.guideNavPoint2Name = i.getStringExtra("1415");
                update.guideNavPoint2Dist = i.getDoubleExtra("1417", 0.0d);
                update.guideNavPoint2Time = i.getLongExtra("1418", 0);
                update.guideNavPoint2Action = i.getIntExtra("1420", update.guideNavPoint2Action);
            }
        }
        update.deviceBatteryValue = i.getIntExtra("1500", 0);
        update.deviceBatteryTemperature = i.getFloatExtra("1501", 0.0f);
        return update;
    }

    public static Intent updateToIntent(String action, UpdateContainer cont) {
        Intent i = new Intent(action);
        addValuesBasicLocation(cont, i);
        addValuesMap(cont, i);
        addValuesTrackRecording(cont, i);
        addValuesGuiding(cont, i);
        addValuesVarious(cont, i);
        return i;
    }

    private static void addValuesBasicLocation(UpdateContainer cont, Intent i) {
        i.putExtra("1000", cont.enabledMyLocation);
        i.putExtra("1001", cont.locMyLocation.getAsBytes());
        i.putExtra("1005", cont.gpsSatsUsed);
        i.putExtra("1006", cont.gpsSatsAll);
        i.putExtra("1007", cont.declination);
        i.putExtra("1014", cont.speedVertical);
        i.putExtra("1015", cont.slope);
        i.putExtra("1013", cont.orientGpsAngle);
        i.putExtra("1008", cont.orientHeading);
        i.putExtra("1009", cont.orientHeadingOpposit);
        i.putExtra("1016", cont.orientCourse);
        i.putExtra("1010", cont.orientPitch);
        i.putExtra("1011", cont.orientRoll);
    }

    private static void addValuesMap(UpdateContainer cont, Intent i) {
        i.putExtra("1300", cont.mapVisible);
        i.putExtra("1302", cont.locMapCenter.getAsBytes());
        i.putExtra("1303", cont.mapTopLeft.getAsBytes());
        i.putExtra("1304", cont.mapBottomRight.getAsBytes());
        i.putExtra("1305", cont.mapZoomLevel);
        i.putExtra("1301", cont.mapRotate);
        i.putExtra("1306", cont.isUserTouching);
    }

    private static void addValuesTrackRecording(UpdateContainer cont, Intent i) {
        i.putExtra("1200", cont.trackRecRecording);
        i.putExtra("1201", cont.trackRecPaused);
        if (cont.trackRecRecording) {
            i.putExtra("1216", cont.trackRecProfileName);
            i.putExtra("1217", cont.trackStats.getAsBytes());
            i.putExtra("1202", (double) cont.trackStats.getTotalLength());
            i.putExtra("1203", (double) cont.trackStats.getEleNegativeDistance());
            i.putExtra("1204", (double) cont.trackStats.getElePositiveDistance());
            i.putExtra("1205", cont.trackStats.getAltitudeMin());
            i.putExtra("1206", cont.trackStats.getAltitudeMax());
            i.putExtra("1207", cont.trackStats.getEleNegativeHeight());
            i.putExtra("1208", cont.trackStats.getElePositiveHeight());
            i.putExtra("1209", cont.trackStats.getEleTotalAbsHeight());
            i.putExtra("1210", cont.trackStats.getTotalTime());
            i.putExtra("1211", cont.trackStats.getTotalTimeMove());
            i.putExtra("1212", cont.trackStats.getSpeedAverage(false));
            i.putExtra("1213", cont.trackStats.getSpeedAverage(true));
            i.putExtra("1214", cont.trackStats.getSpeedMax());
            i.putExtra("1215", cont.trackStats.getNumOfPoints());
        }
    }

    private static void addValuesGuiding(UpdateContainer cont, Intent i) {
        i.putExtra("1410", cont.guideType);
        if (cont.guideType != -1) {
            i.putExtra("1401", cont.guideWptName);
            i.putExtra("1402", cont.guideWptLoc.getAsBytes());
            i.putExtra("1403", cont.guideWptDist);
            i.putExtra("1405", cont.guideWptAzim);
            i.putExtra("1406", cont.guideWptAngle);
            i.putExtra("1407", cont.guideWptTime);
            i.putExtra("1409", cont.guideDistFromStart);
            i.putExtra("1404", cont.guideDistToFinish);
            i.putExtra("1408", cont.guideTimeToFinish);
            if (cont.guideNavPoint1Loc != null) {
                i.putExtra("1412", cont.guideNavPoint1Loc.getAsBytes());
                i.putExtra("1411", cont.guideNavPoint1Name);
                i.putExtra("1413", cont.guideNavPoint1Dist);
                i.putExtra("1414", cont.guideNavPoint1Time);
                i.putExtra("1419", cont.guideNavPoint1Action);
            }
            if (cont.guideNavPoint2Loc != null) {
                i.putExtra("1416", cont.guideNavPoint2Loc.getAsBytes());
                i.putExtra("1415", cont.guideNavPoint2Name);
                i.putExtra("1417", cont.guideNavPoint2Dist);
                i.putExtra("1418", cont.guideNavPoint2Time);
                i.putExtra("1420", cont.guideNavPoint2Action);
            }
        }
    }

    private static void addValuesVarious(UpdateContainer cont, Intent i) {
        i.putExtra("1500", cont.deviceBatteryValue);
        i.putExtra("1501", cont.deviceBatteryTemperature);
    }
}
