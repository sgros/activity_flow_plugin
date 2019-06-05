package org.mapsforge.android.maps;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import java.util.HashMap;
import java.util.Map;
import menion.android.whereyougo.maps.mapsforge.MapsforgeActivity;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;

public class MapScaleBar {
    private static final int BITMAP_HEIGHT = 50;
    private static final int BITMAP_WIDTH = 150;
    private static final double LATITUDE_REDRAW_THRESHOLD = 0.2d;
    private static final int MARGIN_BOTTOM = 15;
    private static final int MARGIN_LEFT = 5;
    private static final double METER_FOOT_RATIO = 0.3048d;
    private static final int ONE_KILOMETER = 1000;
    private static final int ONE_MILE = 5280;
    private static final Paint SCALE_BAR = new Paint(1);
    private static final Paint SCALE_BAR_STROKE = new Paint(1);
    private static final int[] SCALE_BAR_VALUES_IMPERIAL = new int[]{26400000, 10560000, 5280000, 2640000, 1056000, 528000, 264000, 105600, 52800, 26400, 10560, ONE_MILE, 2000, 1000, MapsforgeActivity.FILE_SYSTEM_CACHE_SIZE_MAX, 200, 100, 50, 20, 10, 5, 2, 1};
    private static final int[] SCALE_BAR_VALUES_METRIC = new int[]{10000000, 5000000, 2000000, 1000000, 500000, 200000, 100000, 50000, 20000, 10000, 5000, 2000, 1000, MapsforgeActivity.FILE_SYSTEM_CACHE_SIZE_MAX, 200, 100, 50, 20, 10, 5, 2, 1};
    private static final Paint SCALE_TEXT = new Paint(1);
    private static final Paint SCALE_TEXT_STROKE = new Paint(1);
    private boolean imperialUnits;
    private MapPosition mapPosition;
    private final Bitmap mapScaleBitmap = Bitmap.createBitmap(BITMAP_WIDTH, 50, Config.ARGB_4444);
    private final Canvas mapScaleCanvas = new Canvas(this.mapScaleBitmap);
    private final MapView mapView;
    private boolean redrawNeeded;
    private boolean showMapScaleBar;
    private final Map<TextField, String> textFields = new HashMap();

    public enum TextField {
        FOOT,
        KILOMETER,
        METER,
        MILE
    }

    private static void configurePaints() {
        SCALE_BAR.setStrokeWidth(2.0f);
        SCALE_BAR.setStrokeCap(Cap.SQUARE);
        SCALE_BAR.setColor(-16777216);
        SCALE_BAR_STROKE.setStrokeWidth(5.0f);
        SCALE_BAR_STROKE.setStrokeCap(Cap.SQUARE);
        SCALE_BAR_STROKE.setColor(-1);
        SCALE_TEXT.setTypeface(Typeface.defaultFromStyle(1));
        SCALE_TEXT.setTextSize(17.0f);
        SCALE_TEXT.setColor(-16777216);
        SCALE_TEXT_STROKE.setTypeface(Typeface.defaultFromStyle(1));
        SCALE_TEXT_STROKE.setStyle(Style.STROKE);
        SCALE_TEXT_STROKE.setColor(-1);
        SCALE_TEXT_STROKE.setStrokeWidth(2.0f);
        SCALE_TEXT_STROKE.setTextSize(17.0f);
    }

    MapScaleBar(MapView mapView) {
        this.mapView = mapView;
        setDefaultTexts();
        configurePaints();
    }

    public boolean isImperialUnits() {
        return this.imperialUnits;
    }

    public boolean isShowMapScaleBar() {
        return this.showMapScaleBar;
    }

    public void setImperialUnits(boolean imperialUnits) {
        this.imperialUnits = imperialUnits;
        this.redrawNeeded = true;
    }

    public void setShowMapScaleBar(boolean showMapScaleBar) {
        this.showMapScaleBar = showMapScaleBar;
    }

    public void setText(TextField textField, String value) {
        this.textFields.put(textField, value);
        this.redrawNeeded = true;
    }

    private void drawScaleBar(float scaleBarLength, Paint paint) {
        this.mapScaleCanvas.drawLine(7.0f, 25.0f, 3.0f + scaleBarLength, 25.0f, paint);
        this.mapScaleCanvas.drawLine(5.0f, 10.0f, 5.0f, 40.0f, paint);
        this.mapScaleCanvas.drawLine(scaleBarLength + 5.0f, 10.0f, scaleBarLength + 5.0f, 40.0f, paint);
    }

    private void drawScaleText(int scaleValue, String unitSymbol, Paint paint) {
        this.mapScaleCanvas.drawText(scaleValue + unitSymbol, 12.0f, 18.0f, paint);
    }

    private boolean isRedrawNecessary() {
        if (this.redrawNeeded || this.mapPosition == null) {
            return true;
        }
        MapPosition currentMapPosition = this.mapView.getMapViewPosition().getMapPosition();
        if (currentMapPosition.zoomLevel != this.mapPosition.zoomLevel || Math.abs(currentMapPosition.geoPoint.latitude - this.mapPosition.geoPoint.latitude) > LATITUDE_REDRAW_THRESHOLD) {
            return true;
        }
        return false;
    }

    private void redrawMapScaleBitmap(float scaleBarLength, int mapScaleValue) {
        int scaleValue;
        String unitSymbol;
        this.mapScaleBitmap.eraseColor(0);
        drawScaleBar(scaleBarLength, SCALE_BAR_STROKE);
        drawScaleBar(scaleBarLength, SCALE_BAR);
        if (this.imperialUnits) {
            if (mapScaleValue < ONE_MILE) {
                scaleValue = mapScaleValue;
                unitSymbol = (String) this.textFields.get(TextField.FOOT);
            } else {
                scaleValue = mapScaleValue / ONE_MILE;
                unitSymbol = (String) this.textFields.get(TextField.MILE);
            }
        } else if (mapScaleValue < 1000) {
            scaleValue = mapScaleValue;
            unitSymbol = (String) this.textFields.get(TextField.METER);
        } else {
            scaleValue = mapScaleValue / 1000;
            unitSymbol = (String) this.textFields.get(TextField.KILOMETER);
        }
        drawScaleText(scaleValue, unitSymbol, SCALE_TEXT_STROKE);
        drawScaleText(scaleValue, unitSymbol, SCALE_TEXT);
    }

    private void setDefaultTexts() {
        this.textFields.put(TextField.FOOT, " ft");
        this.textFields.put(TextField.MILE, " mi");
        this.textFields.put(TextField.METER, " m");
        this.textFields.put(TextField.KILOMETER, " km");
    }

    /* Access modifiers changed, original: 0000 */
    public void destroy() {
        this.mapScaleBitmap.recycle();
    }

    /* Access modifiers changed, original: 0000 */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.mapScaleBitmap, 5.0f, (float) ((this.mapView.getHeight() - 50) - 15), null);
    }

    /* Access modifiers changed, original: 0000 */
    public void redrawScaleBar() {
        if (isRedrawNecessary()) {
            int[] scaleBarValues;
            this.mapPosition = this.mapView.getMapViewPosition().getMapPosition();
            double groundResolution = MercatorProjection.calculateGroundResolution(this.mapPosition.geoPoint.latitude, this.mapPosition.zoomLevel);
            if (this.imperialUnits) {
                groundResolution /= METER_FOOT_RATIO;
                scaleBarValues = SCALE_BAR_VALUES_IMPERIAL;
            } else {
                scaleBarValues = SCALE_BAR_VALUES_METRIC;
            }
            float scaleBarLength = 0.0f;
            int mapScaleValue = 0;
            for (int mapScaleValue2 : scaleBarValues) {
                scaleBarLength = ((float) mapScaleValue2) / ((float) groundResolution);
                if (scaleBarLength < 140.0f) {
                    break;
                }
            }
            redrawMapScaleBitmap(scaleBarLength, mapScaleValue2);
            this.redrawNeeded = false;
        }
    }
}
