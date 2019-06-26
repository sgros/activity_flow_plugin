// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.periodicUpdates;

import java.io.IOException;
import locus.api.objects.extra.TrackStats;
import locus.api.android.utils.LocusUtils;
import android.content.Intent;

public class PeriodicUpdatesFiller
{
    private static void addValuesBasicLocation(final UpdateContainer updateContainer, final Intent intent) {
        intent.putExtra("1000", updateContainer.enabledMyLocation);
        intent.putExtra("1001", updateContainer.locMyLocation.getAsBytes());
        intent.putExtra("1005", updateContainer.gpsSatsUsed);
        intent.putExtra("1006", updateContainer.gpsSatsAll);
        intent.putExtra("1007", updateContainer.declination);
        intent.putExtra("1014", updateContainer.speedVertical);
        intent.putExtra("1015", updateContainer.slope);
        intent.putExtra("1013", updateContainer.orientGpsAngle);
        intent.putExtra("1008", updateContainer.orientHeading);
        intent.putExtra("1009", updateContainer.orientHeadingOpposit);
        intent.putExtra("1016", updateContainer.orientCourse);
        intent.putExtra("1010", updateContainer.orientPitch);
        intent.putExtra("1011", updateContainer.orientRoll);
    }
    
    private static void addValuesGuiding(final UpdateContainer updateContainer, final Intent intent) {
        intent.putExtra("1410", updateContainer.guideType);
        if (updateContainer.guideType != -1) {
            intent.putExtra("1401", updateContainer.guideWptName);
            intent.putExtra("1402", updateContainer.guideWptLoc.getAsBytes());
            intent.putExtra("1403", updateContainer.guideWptDist);
            intent.putExtra("1405", updateContainer.guideWptAzim);
            intent.putExtra("1406", updateContainer.guideWptAngle);
            intent.putExtra("1407", updateContainer.guideWptTime);
            intent.putExtra("1409", updateContainer.guideDistFromStart);
            intent.putExtra("1404", updateContainer.guideDistToFinish);
            intent.putExtra("1408", updateContainer.guideTimeToFinish);
            if (updateContainer.guideNavPoint1Loc != null) {
                intent.putExtra("1412", updateContainer.guideNavPoint1Loc.getAsBytes());
                intent.putExtra("1411", updateContainer.guideNavPoint1Name);
                intent.putExtra("1413", updateContainer.guideNavPoint1Dist);
                intent.putExtra("1414", updateContainer.guideNavPoint1Time);
                intent.putExtra("1419", updateContainer.guideNavPoint1Action);
            }
            if (updateContainer.guideNavPoint2Loc != null) {
                intent.putExtra("1416", updateContainer.guideNavPoint2Loc.getAsBytes());
                intent.putExtra("1415", updateContainer.guideNavPoint2Name);
                intent.putExtra("1417", updateContainer.guideNavPoint2Dist);
                intent.putExtra("1418", updateContainer.guideNavPoint2Time);
                intent.putExtra("1420", updateContainer.guideNavPoint2Action);
            }
        }
    }
    
    private static void addValuesMap(final UpdateContainer updateContainer, final Intent intent) {
        intent.putExtra("1300", updateContainer.mapVisible);
        intent.putExtra("1302", updateContainer.locMapCenter.getAsBytes());
        intent.putExtra("1303", updateContainer.mapTopLeft.getAsBytes());
        intent.putExtra("1304", updateContainer.mapBottomRight.getAsBytes());
        intent.putExtra("1305", updateContainer.mapZoomLevel);
        intent.putExtra("1301", updateContainer.mapRotate);
        intent.putExtra("1306", updateContainer.isUserTouching);
    }
    
    private static void addValuesTrackRecording(final UpdateContainer updateContainer, final Intent intent) {
        intent.putExtra("1200", updateContainer.trackRecRecording);
        intent.putExtra("1201", updateContainer.trackRecPaused);
        if (updateContainer.trackRecRecording) {
            intent.putExtra("1216", updateContainer.trackRecProfileName);
            intent.putExtra("1217", updateContainer.trackStats.getAsBytes());
            intent.putExtra("1202", (double)updateContainer.trackStats.getTotalLength());
            intent.putExtra("1203", (double)updateContainer.trackStats.getEleNegativeDistance());
            intent.putExtra("1204", (double)updateContainer.trackStats.getElePositiveDistance());
            intent.putExtra("1205", updateContainer.trackStats.getAltitudeMin());
            intent.putExtra("1206", updateContainer.trackStats.getAltitudeMax());
            intent.putExtra("1207", updateContainer.trackStats.getEleNegativeHeight());
            intent.putExtra("1208", updateContainer.trackStats.getElePositiveHeight());
            intent.putExtra("1209", updateContainer.trackStats.getEleTotalAbsHeight());
            intent.putExtra("1210", updateContainer.trackStats.getTotalTime());
            intent.putExtra("1211", updateContainer.trackStats.getTotalTimeMove());
            intent.putExtra("1212", updateContainer.trackStats.getSpeedAverage(false));
            intent.putExtra("1213", updateContainer.trackStats.getSpeedAverage(true));
            intent.putExtra("1214", updateContainer.trackStats.getSpeedMax());
            intent.putExtra("1215", updateContainer.trackStats.getNumOfPoints());
        }
    }
    
    private static void addValuesVarious(final UpdateContainer updateContainer, final Intent intent) {
        intent.putExtra("1500", updateContainer.deviceBatteryValue);
        intent.putExtra("1501", updateContainer.deviceBatteryTemperature);
    }
    
    public static UpdateContainer intentToUpdate(final Intent intent, final PeriodicUpdatesHandler periodicUpdatesHandler) {
        final UpdateContainer updateContainer = new UpdateContainer();
        updateContainer.enabledMyLocation = intent.getBooleanExtra("1000", false);
        updateContainer.newMyLocation = false;
        updateContainer.locMyLocation = LocusUtils.getLocationFromIntent(intent, "1001");
        if (updateContainer.enabledMyLocation && (periodicUpdatesHandler.mLastGps == null || periodicUpdatesHandler.mLastGps.distanceTo(updateContainer.locMyLocation) > periodicUpdatesHandler.mLocMinDistance)) {
            periodicUpdatesHandler.mLastGps = updateContainer.locMyLocation;
            updateContainer.newMyLocation = true;
        }
        updateContainer.gpsSatsUsed = intent.getIntExtra("1005", 0);
        updateContainer.gpsSatsAll = intent.getIntExtra("1006", 0);
        updateContainer.declination = intent.getFloatExtra("1007", 0.0f);
        updateContainer.speedVertical = intent.getFloatExtra("1014", 0.0f);
        updateContainer.slope = intent.getFloatExtra("1015", 0.0f);
        updateContainer.orientGpsAngle = intent.getFloatExtra("1013", 0.0f);
        updateContainer.orientHeading = intent.getFloatExtra("1008", 0.0f);
        updateContainer.orientHeadingOpposit = intent.getFloatExtra("1009", 0.0f);
        updateContainer.orientCourse = intent.getFloatExtra("1016", 0.0f);
        updateContainer.orientPitch = intent.getFloatExtra("1010", 0.0f);
        updateContainer.orientRoll = intent.getFloatExtra("1011", 0.0f);
        updateContainer.mapVisible = intent.getBooleanExtra("1300", false);
        updateContainer.newMapCenter = false;
        updateContainer.locMapCenter = LocusUtils.getLocationFromIntent(intent, "1302");
        if (periodicUpdatesHandler.mLastMapCenter == null || periodicUpdatesHandler.mLastMapCenter.distanceTo(updateContainer.locMapCenter) > periodicUpdatesHandler.mLocMinDistance) {
            periodicUpdatesHandler.mLastMapCenter = updateContainer.locMapCenter;
            updateContainer.newMapCenter = true;
        }
        updateContainer.mapTopLeft = LocusUtils.getLocationFromIntent(intent, "1303");
        updateContainer.mapBottomRight = LocusUtils.getLocationFromIntent(intent, "1304");
        updateContainer.mapZoomLevel = intent.getIntExtra("1305", 0);
        Label_0898: {
            if (updateContainer.mapZoomLevel == periodicUpdatesHandler.mLastZoomLevel) {
                break Label_0898;
            }
            boolean newZoomLevel = true;
        Label_0429_Outer:
            while (true) {
                updateContainer.newZoomLevel = newZoomLevel;
                periodicUpdatesHandler.mLastZoomLevel = updateContainer.mapZoomLevel;
                updateContainer.mapRotate = intent.getFloatExtra("1301", 0.0f);
                updateContainer.isUserTouching = intent.getBooleanExtra("1306", false);
                updateContainer.trackRecRecording = intent.getBooleanExtra("1200", false);
                Label_0630: {
                    if (!updateContainer.trackRecRecording) {
                        break Label_0630;
                    }
                    updateContainer.trackRecPaused = intent.getBooleanExtra("1201", false);
                    updateContainer.trackRecProfileName = intent.getStringExtra("1216");
                    while (true) {
                        try {
                            final byte[] byteArrayExtra = intent.getByteArrayExtra("1217");
                            if (byteArrayExtra != null && byteArrayExtra.length > 0) {
                                (updateContainer.trackStats = new TrackStats()).read(byteArrayExtra);
                            }
                            if (updateContainer.trackStats == null) {
                                (updateContainer.trackStats = new TrackStats()).setTotalLength((float)intent.getDoubleExtra("1202", 0.0));
                                updateContainer.trackStats.setEleNegativeDistance((float)intent.getDoubleExtra("1203", 0.0));
                                updateContainer.trackStats.setElePositiveDistance((float)intent.getDoubleExtra("1204", 0.0));
                                updateContainer.trackStats.setAltitudeMin(intent.getFloatExtra("1205", 0.0f));
                                updateContainer.trackStats.setAltitudeMax(intent.getFloatExtra("1206", 0.0f));
                                updateContainer.trackStats.setEleNegativeHeight(intent.getFloatExtra("1207", 0.0f));
                                updateContainer.trackStats.setElePositiveHeight(intent.getFloatExtra("1208", 0.0f));
                                updateContainer.trackStats.setEleTotalAbsHeight(intent.getFloatExtra("1209", 0.0f));
                                updateContainer.trackStats.setTotalTime(intent.getLongExtra("1210", 0L));
                                updateContainer.trackStats.setTotalTimeMove(intent.getLongExtra("1211", 0L));
                                updateContainer.trackStats.setSpeedMax(intent.getFloatExtra("1214", 0.0f));
                                updateContainer.trackStats.setNumOfPoints(intent.getIntExtra("1215", 0));
                            }
                            updateContainer.guideType = intent.getIntExtra("1410", -1);
                            if (updateContainer.guideType != -1) {
                                updateContainer.guideWptName = intent.getStringExtra("1401");
                                updateContainer.guideWptLoc = LocusUtils.getLocationFromIntent(intent, "1402");
                                updateContainer.guideWptDist = intent.getDoubleExtra("1403", 0.0);
                                updateContainer.guideWptAzim = intent.getFloatExtra("1405", 0.0f);
                                updateContainer.guideWptAngle = intent.getFloatExtra("1406", 0.0f);
                                updateContainer.guideWptTime = intent.getLongExtra("1407", 0L);
                                updateContainer.guideDistFromStart = intent.getDoubleExtra("1409", 0.0);
                                updateContainer.guideDistToFinish = intent.getDoubleExtra("1404", 0.0);
                                updateContainer.guideTimeToFinish = intent.getLongExtra("1408", 0L);
                                updateContainer.guideNavPoint1Loc = LocusUtils.getLocationFromIntent(intent, "1412");
                                if (updateContainer.guideNavPoint1Loc != null) {
                                    updateContainer.guideNavPoint1Name = intent.getStringExtra("1411");
                                    updateContainer.guideNavPoint1Dist = intent.getDoubleExtra("1413", 0.0);
                                    updateContainer.guideNavPoint1Time = intent.getLongExtra("1414", 0L);
                                    updateContainer.guideNavPoint1Action = intent.getIntExtra("1419", updateContainer.guideNavPoint1Action);
                                }
                                updateContainer.guideNavPoint2Loc = LocusUtils.getLocationFromIntent(intent, "1416");
                                if (updateContainer.guideNavPoint2Loc != null) {
                                    updateContainer.guideNavPoint2Name = intent.getStringExtra("1415");
                                    updateContainer.guideNavPoint2Dist = intent.getDoubleExtra("1417", 0.0);
                                    updateContainer.guideNavPoint2Time = intent.getLongExtra("1418", 0L);
                                    updateContainer.guideNavPoint2Action = intent.getIntExtra("1420", updateContainer.guideNavPoint2Action);
                                }
                            }
                            updateContainer.deviceBatteryValue = intent.getIntExtra("1500", 0);
                            updateContainer.deviceBatteryTemperature = intent.getFloatExtra("1501", 0.0f);
                            return updateContainer;
                            newZoomLevel = false;
                            continue Label_0429_Outer;
                        }
                        catch (IOException ex) {
                            ex.printStackTrace();
                            continue;
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    
    public static Intent updateToIntent(final String s, final UpdateContainer updateContainer) {
        final Intent intent = new Intent(s);
        addValuesBasicLocation(updateContainer, intent);
        addValuesMap(updateContainer, intent);
        addValuesTrackRecording(updateContainer, intent);
        addValuesGuiding(updateContainer, intent);
        addValuesVarious(updateContainer, intent);
        return intent;
    }
}
