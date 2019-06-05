// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.android.camera;

import com.journeyapps.barcodescanner.camera.CameraSettings;
import java.util.Iterator;
import android.util.Log;
import java.util.Collection;
import java.util.Arrays;
import android.os.Build$VERSION;
import android.os.Build;
import android.hardware.Camera$Parameters;
import android.annotation.TargetApi;
import java.util.Collections;
import android.graphics.Rect;
import android.hardware.Camera$Area;
import java.util.List;
import java.util.regex.Pattern;

public final class CameraConfigurationUtils
{
    private static final int AREA_PER_1000 = 400;
    private static final float MAX_EXPOSURE_COMPENSATION = 1.5f;
    private static final int MAX_FPS = 20;
    private static final float MIN_EXPOSURE_COMPENSATION = 0.0f;
    private static final int MIN_FPS = 10;
    private static final Pattern SEMICOLON;
    private static final String TAG = "CameraConfiguration";
    
    static {
        SEMICOLON = Pattern.compile(";");
    }
    
    private CameraConfigurationUtils() {
    }
    
    @TargetApi(15)
    private static List<Camera$Area> buildMiddleArea(final int n) {
        return Collections.singletonList(new Camera$Area(new Rect(-n, -n, n, n), 1));
    }
    
    public static String collectStats(final Camera$Parameters camera$Parameters) {
        return collectStats(camera$Parameters.flatten());
    }
    
    public static String collectStats(final CharSequence input) {
        final StringBuilder sb = new StringBuilder(1000);
        sb.append("BOARD=").append(Build.BOARD).append('\n');
        sb.append("BRAND=").append(Build.BRAND).append('\n');
        sb.append("CPU_ABI=").append(Build.CPU_ABI).append('\n');
        sb.append("DEVICE=").append(Build.DEVICE).append('\n');
        sb.append("DISPLAY=").append(Build.DISPLAY).append('\n');
        sb.append("FINGERPRINT=").append(Build.FINGERPRINT).append('\n');
        sb.append("HOST=").append(Build.HOST).append('\n');
        sb.append("ID=").append(Build.ID).append('\n');
        sb.append("MANUFACTURER=").append(Build.MANUFACTURER).append('\n');
        sb.append("MODEL=").append(Build.MODEL).append('\n');
        sb.append("PRODUCT=").append(Build.PRODUCT).append('\n');
        sb.append("TAGS=").append(Build.TAGS).append('\n');
        sb.append("TIME=").append(Build.TIME).append('\n');
        sb.append("TYPE=").append(Build.TYPE).append('\n');
        sb.append("USER=").append(Build.USER).append('\n');
        sb.append("VERSION.CODENAME=").append(Build$VERSION.CODENAME).append('\n');
        sb.append("VERSION.INCREMENTAL=").append(Build$VERSION.INCREMENTAL).append('\n');
        sb.append("VERSION.RELEASE=").append(Build$VERSION.RELEASE).append('\n');
        sb.append("VERSION.SDK_INT=").append(Build$VERSION.SDK_INT).append('\n');
        if (input != null) {
            final String[] split = CameraConfigurationUtils.SEMICOLON.split(input);
            Arrays.sort(split);
            for (int length = split.length, i = 0; i < length; ++i) {
                sb.append(split[i]).append('\n');
            }
        }
        return sb.toString();
    }
    
    private static String findSettableValue(String str, final Collection<String> obj, final String... a) {
        Log.i("CameraConfiguration", "Requesting " + str + " value from among: " + Arrays.toString(a));
        Log.i("CameraConfiguration", "Supported " + str + " values: " + obj);
        if (obj != null) {
            for (final String str2 : a) {
                if (obj.contains(str2)) {
                    Log.i("CameraConfiguration", "Can set " + str + " to: " + str2);
                    str = str2;
                    return str;
                }
            }
        }
        Label_0150: {
            break Label_0150;
        }
        Log.i("CameraConfiguration", "No supported values match");
        str = null;
        return str;
    }
    
    private static Integer indexOfClosestZoom(final Camera$Parameters camera$Parameters, final double n) {
        final List zoomRatios = camera$Parameters.getZoomRatios();
        Log.i("CameraConfiguration", "Zoom ratios: " + zoomRatios);
        final int maxZoom = camera$Parameters.getMaxZoom();
        Integer value;
        if (zoomRatios == null || zoomRatios.isEmpty() || zoomRatios.size() != maxZoom + 1) {
            Log.w("CameraConfiguration", "Invalid zoom ratios!");
            value = null;
        }
        else {
            double n2 = Double.POSITIVE_INFINITY;
            int i = 0;
            double n3;
            for (int j = 0; j < zoomRatios.size(); ++j, n2 = n3) {
                final double abs = Math.abs(zoomRatios.get(j) - 100.0 * n);
                n3 = n2;
                if (abs < n2) {
                    n3 = abs;
                    i = j;
                }
            }
            Log.i("CameraConfiguration", "Chose zoom ratio of " + zoomRatios.get(i) / 100.0);
            value = i;
        }
        return value;
    }
    
    public static void setBarcodeSceneMode(final Camera$Parameters camera$Parameters) {
        if ("barcode".equals(camera$Parameters.getSceneMode())) {
            Log.i("CameraConfiguration", "Barcode scene mode already set");
        }
        else {
            final String settableValue = findSettableValue("scene mode", camera$Parameters.getSupportedSceneModes(), "barcode");
            if (settableValue != null) {
                camera$Parameters.setSceneMode(settableValue);
            }
        }
    }
    
    public static void setBestExposure(final Camera$Parameters camera$Parameters, final boolean b) {
        float n = 0.0f;
        final int minExposureCompensation = camera$Parameters.getMinExposureCompensation();
        final int maxExposureCompensation = camera$Parameters.getMaxExposureCompensation();
        final float exposureCompensationStep = camera$Parameters.getExposureCompensationStep();
        if ((minExposureCompensation != 0 || maxExposureCompensation != 0) && exposureCompensationStep > 0.0f) {
            if (!b) {
                n = 1.5f;
            }
            final int round = Math.round(n / exposureCompensationStep);
            final float n2 = exposureCompensationStep * round;
            final int max = Math.max(Math.min(round, maxExposureCompensation), minExposureCompensation);
            if (camera$Parameters.getExposureCompensation() == max) {
                Log.i("CameraConfiguration", "Exposure compensation already set to " + max + " / " + n2);
            }
            else {
                Log.i("CameraConfiguration", "Setting exposure compensation to " + max + " / " + n2);
                camera$Parameters.setExposureCompensation(max);
            }
        }
        else {
            Log.i("CameraConfiguration", "Camera does not support exposure compensation");
        }
    }
    
    public static void setBestPreviewFPS(final Camera$Parameters camera$Parameters) {
        setBestPreviewFPS(camera$Parameters, 10, 20);
    }
    
    public static void setBestPreviewFPS(final Camera$Parameters camera$Parameters, final int n, final int n2) {
        final List supportedPreviewFpsRange = camera$Parameters.getSupportedPreviewFpsRange();
        Log.i("CameraConfiguration", "Supported FPS ranges: " + toString((Collection<int[]>)supportedPreviewFpsRange));
        if (supportedPreviewFpsRange != null && !supportedPreviewFpsRange.isEmpty()) {
            final int[] array = null;
            final Iterator<int[]> iterator = supportedPreviewFpsRange.iterator();
            int n3;
            int n4;
            int[] a;
            do {
                a = array;
                if (!iterator.hasNext()) {
                    break;
                }
                a = iterator.next();
                n3 = a[0];
                n4 = a[1];
            } while (n3 < n * 1000 || n4 > n2 * 1000);
            if (a == null) {
                Log.i("CameraConfiguration", "No suitable FPS range?");
            }
            else {
                final int[] a2 = new int[2];
                camera$Parameters.getPreviewFpsRange(a2);
                if (Arrays.equals(a2, a)) {
                    Log.i("CameraConfiguration", "FPS range already set to " + Arrays.toString(a));
                }
                else {
                    Log.i("CameraConfiguration", "Setting FPS range to " + Arrays.toString(a));
                    camera$Parameters.setPreviewFpsRange(a[0], a[1]);
                }
            }
        }
    }
    
    public static void setFocus(final Camera$Parameters camera$Parameters, final CameraSettings.FocusMode focusMode, final boolean b) {
        final List supportedFocusModes = camera$Parameters.getSupportedFocusModes();
        String s = null;
        if (b || focusMode == CameraSettings.FocusMode.AUTO) {
            s = findSettableValue("focus mode", supportedFocusModes, "auto");
        }
        else if (focusMode == CameraSettings.FocusMode.CONTINUOUS) {
            s = findSettableValue("focus mode", supportedFocusModes, "continuous-picture", "continuous-video", "auto");
        }
        else if (focusMode == CameraSettings.FocusMode.INFINITY) {
            s = findSettableValue("focus mode", supportedFocusModes, "infinity");
        }
        else if (focusMode == CameraSettings.FocusMode.MACRO) {
            s = findSettableValue("focus mode", supportedFocusModes, "macro");
        }
        String settableValue = s;
        if (!b && (settableValue = s) == null) {
            settableValue = findSettableValue("focus mode", supportedFocusModes, "macro", "edof");
        }
        if (settableValue != null) {
            if (settableValue.equals(camera$Parameters.getFocusMode())) {
                Log.i("CameraConfiguration", "Focus mode already set to " + settableValue);
            }
            else {
                camera$Parameters.setFocusMode(settableValue);
            }
        }
    }
    
    @TargetApi(15)
    public static void setFocusArea(final Camera$Parameters camera$Parameters) {
        if (camera$Parameters.getMaxNumFocusAreas() > 0) {
            Log.i("CameraConfiguration", "Old focus areas: " + toString((Iterable<Camera$Area>)camera$Parameters.getFocusAreas()));
            final List<Camera$Area> buildMiddleArea = buildMiddleArea(400);
            Log.i("CameraConfiguration", "Setting focus area to : " + toString(buildMiddleArea));
            camera$Parameters.setFocusAreas((List)buildMiddleArea);
        }
        else {
            Log.i("CameraConfiguration", "Device does not support focus areas");
        }
    }
    
    public static void setInvertColor(final Camera$Parameters camera$Parameters) {
        if ("negative".equals(camera$Parameters.getColorEffect())) {
            Log.i("CameraConfiguration", "Negative effect already set");
        }
        else {
            final String settableValue = findSettableValue("color effect", camera$Parameters.getSupportedColorEffects(), "negative");
            if (settableValue != null) {
                camera$Parameters.setColorEffect(settableValue);
            }
        }
    }
    
    @TargetApi(15)
    public static void setMetering(final Camera$Parameters camera$Parameters) {
        if (camera$Parameters.getMaxNumMeteringAreas() > 0) {
            Log.i("CameraConfiguration", "Old metering areas: " + camera$Parameters.getMeteringAreas());
            final List<Camera$Area> buildMiddleArea = buildMiddleArea(400);
            Log.i("CameraConfiguration", "Setting metering area to : " + toString(buildMiddleArea));
            camera$Parameters.setMeteringAreas((List)buildMiddleArea);
        }
        else {
            Log.i("CameraConfiguration", "Device does not support metering areas");
        }
    }
    
    public static void setTorch(final Camera$Parameters camera$Parameters, final boolean b) {
        final List supportedFlashModes = camera$Parameters.getSupportedFlashModes();
        String flashMode;
        if (b) {
            flashMode = findSettableValue("flash mode", supportedFlashModes, "torch", "on");
        }
        else {
            flashMode = findSettableValue("flash mode", supportedFlashModes, "off");
        }
        if (flashMode != null) {
            if (flashMode.equals(camera$Parameters.getFlashMode())) {
                Log.i("CameraConfiguration", "Flash mode already set to " + flashMode);
            }
            else {
                Log.i("CameraConfiguration", "Setting flash mode to " + flashMode);
                camera$Parameters.setFlashMode(flashMode);
            }
        }
    }
    
    @TargetApi(15)
    public static void setVideoStabilization(final Camera$Parameters camera$Parameters) {
        if (camera$Parameters.isVideoStabilizationSupported()) {
            if (camera$Parameters.getVideoStabilization()) {
                Log.i("CameraConfiguration", "Video stabilization already enabled");
            }
            else {
                Log.i("CameraConfiguration", "Enabling video stabilization...");
                camera$Parameters.setVideoStabilization(true);
            }
        }
        else {
            Log.i("CameraConfiguration", "This device does not support video stabilization");
        }
    }
    
    public static void setZoom(final Camera$Parameters camera$Parameters, final double n) {
        if (camera$Parameters.isZoomSupported()) {
            final Integer indexOfClosestZoom = indexOfClosestZoom(camera$Parameters, n);
            if (indexOfClosestZoom != null) {
                if (camera$Parameters.getZoom() == indexOfClosestZoom) {
                    Log.i("CameraConfiguration", "Zoom is already set to " + indexOfClosestZoom);
                }
                else {
                    Log.i("CameraConfiguration", "Setting zoom to " + indexOfClosestZoom);
                    camera$Parameters.setZoom((int)indexOfClosestZoom);
                }
            }
        }
        else {
            Log.i("CameraConfiguration", "Zoom is not supported");
        }
    }
    
    @TargetApi(15)
    private static String toString(final Iterable<Camera$Area> iterable) {
        String string;
        if (iterable == null) {
            string = null;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            for (final Camera$Area camera$Area : iterable) {
                sb.append(camera$Area.rect).append(':').append(camera$Area.weight).append(' ');
            }
            string = sb.toString();
        }
        return string;
    }
    
    private static String toString(final Collection<int[]> collection) {
        String string;
        if (collection == null || collection.isEmpty()) {
            string = "[]";
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            final Iterator<int[]> iterator = collection.iterator();
            while (iterator.hasNext()) {
                sb.append(Arrays.toString(iterator.next()));
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append(']');
            string = sb.toString();
        }
        return string;
    }
}
