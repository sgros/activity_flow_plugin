// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import org.mapsforge.core.util.MercatorProjection;
import android.graphics.Paint$Style;
import android.graphics.Typeface;
import android.graphics.Paint$Cap;
import java.util.HashMap;
import android.graphics.Bitmap$Config;
import java.util.Map;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import org.mapsforge.core.model.MapPosition;
import android.graphics.Paint;

public class MapScaleBar
{
    private static final int BITMAP_HEIGHT = 50;
    private static final int BITMAP_WIDTH = 150;
    private static final double LATITUDE_REDRAW_THRESHOLD = 0.2;
    private static final int MARGIN_BOTTOM = 15;
    private static final int MARGIN_LEFT = 5;
    private static final double METER_FOOT_RATIO = 0.3048;
    private static final int ONE_KILOMETER = 1000;
    private static final int ONE_MILE = 5280;
    private static final Paint SCALE_BAR;
    private static final Paint SCALE_BAR_STROKE;
    private static final int[] SCALE_BAR_VALUES_IMPERIAL;
    private static final int[] SCALE_BAR_VALUES_METRIC;
    private static final Paint SCALE_TEXT;
    private static final Paint SCALE_TEXT_STROKE;
    private boolean imperialUnits;
    private MapPosition mapPosition;
    private final Bitmap mapScaleBitmap;
    private final Canvas mapScaleCanvas;
    private final MapView mapView;
    private boolean redrawNeeded;
    private boolean showMapScaleBar;
    private final Map<TextField, String> textFields;
    
    static {
        SCALE_BAR = new Paint(1);
        SCALE_BAR_STROKE = new Paint(1);
        SCALE_BAR_VALUES_IMPERIAL = new int[] { 26400000, 10560000, 5280000, 2640000, 1056000, 528000, 264000, 105600, 52800, 26400, 10560, 5280, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1 };
        SCALE_BAR_VALUES_METRIC = new int[] { 10000000, 5000000, 2000000, 1000000, 500000, 200000, 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1 };
        SCALE_TEXT = new Paint(1);
        SCALE_TEXT_STROKE = new Paint(1);
    }
    
    MapScaleBar(final MapView mapView) {
        this.mapView = mapView;
        this.mapScaleBitmap = Bitmap.createBitmap(150, 50, Bitmap$Config.ARGB_4444);
        this.mapScaleCanvas = new Canvas(this.mapScaleBitmap);
        this.textFields = new HashMap<TextField, String>();
        this.setDefaultTexts();
        configurePaints();
    }
    
    private static void configurePaints() {
        MapScaleBar.SCALE_BAR.setStrokeWidth(2.0f);
        MapScaleBar.SCALE_BAR.setStrokeCap(Paint$Cap.SQUARE);
        MapScaleBar.SCALE_BAR.setColor(-16777216);
        MapScaleBar.SCALE_BAR_STROKE.setStrokeWidth(5.0f);
        MapScaleBar.SCALE_BAR_STROKE.setStrokeCap(Paint$Cap.SQUARE);
        MapScaleBar.SCALE_BAR_STROKE.setColor(-1);
        MapScaleBar.SCALE_TEXT.setTypeface(Typeface.defaultFromStyle(1));
        MapScaleBar.SCALE_TEXT.setTextSize(17.0f);
        MapScaleBar.SCALE_TEXT.setColor(-16777216);
        MapScaleBar.SCALE_TEXT_STROKE.setTypeface(Typeface.defaultFromStyle(1));
        MapScaleBar.SCALE_TEXT_STROKE.setStyle(Paint$Style.STROKE);
        MapScaleBar.SCALE_TEXT_STROKE.setColor(-1);
        MapScaleBar.SCALE_TEXT_STROKE.setStrokeWidth(2.0f);
        MapScaleBar.SCALE_TEXT_STROKE.setTextSize(17.0f);
    }
    
    private void drawScaleBar(final float n, final Paint paint) {
        this.mapScaleCanvas.drawLine(7.0f, 25.0f, 3.0f + n, 25.0f, paint);
        this.mapScaleCanvas.drawLine(5.0f, 10.0f, 5.0f, 40.0f, paint);
        this.mapScaleCanvas.drawLine(n + 5.0f, 10.0f, n + 5.0f, 40.0f, paint);
    }
    
    private void drawScaleText(final int i, final String str, final Paint paint) {
        this.mapScaleCanvas.drawText(i + str, 12.0f, 18.0f, paint);
    }
    
    private boolean isRedrawNecessary() {
        boolean b2;
        final boolean b = b2 = true;
        if (!this.redrawNeeded) {
            if (this.mapPosition == null) {
                b2 = b;
            }
            else {
                final MapPosition mapPosition = this.mapView.getMapViewPosition().getMapPosition();
                b2 = b;
                if (mapPosition.zoomLevel == this.mapPosition.zoomLevel) {
                    b2 = b;
                    if (Math.abs(mapPosition.geoPoint.latitude - this.mapPosition.geoPoint.latitude) <= 0.2) {
                        b2 = false;
                    }
                }
            }
        }
        return b2;
    }
    
    private void redrawMapScaleBitmap(final float n, int n2) {
        this.mapScaleBitmap.eraseColor(0);
        this.drawScaleBar(n, MapScaleBar.SCALE_BAR_STROKE);
        this.drawScaleBar(n, MapScaleBar.SCALE_BAR);
        String s;
        if (this.imperialUnits) {
            if (n2 < 5280) {
                s = this.textFields.get(TextField.FOOT);
            }
            else {
                n2 /= 5280;
                s = this.textFields.get(TextField.MILE);
            }
        }
        else if (n2 < 1000) {
            s = this.textFields.get(TextField.METER);
        }
        else {
            n2 /= 1000;
            s = this.textFields.get(TextField.KILOMETER);
        }
        this.drawScaleText(n2, s, MapScaleBar.SCALE_TEXT_STROKE);
        this.drawScaleText(n2, s, MapScaleBar.SCALE_TEXT);
    }
    
    private void setDefaultTexts() {
        this.textFields.put(TextField.FOOT, " ft");
        this.textFields.put(TextField.MILE, " mi");
        this.textFields.put(TextField.METER, " m");
        this.textFields.put(TextField.KILOMETER, " km");
    }
    
    void destroy() {
        this.mapScaleBitmap.recycle();
    }
    
    void draw(final Canvas canvas) {
        canvas.drawBitmap(this.mapScaleBitmap, 5.0f, (float)(this.mapView.getHeight() - 50 - 15), (Paint)null);
    }
    
    public boolean isImperialUnits() {
        return this.imperialUnits;
    }
    
    public boolean isShowMapScaleBar() {
        return this.showMapScaleBar;
    }
    
    void redrawScaleBar() {
        if (this.isRedrawNecessary()) {
            this.mapPosition = this.mapView.getMapViewPosition().getMapPosition();
            double calculateGroundResolution = MercatorProjection.calculateGroundResolution(this.mapPosition.geoPoint.latitude, this.mapPosition.zoomLevel);
            int[] array;
            if (this.imperialUnits) {
                calculateGroundResolution /= 0.3048;
                array = MapScaleBar.SCALE_BAR_VALUES_IMPERIAL;
            }
            else {
                array = MapScaleBar.SCALE_BAR_VALUES_METRIC;
            }
            float n = 0.0f;
            int n2 = 0;
            for (int i = 0; i < array.length; ++i) {
                n2 = array[i];
                n = n2 / (float)calculateGroundResolution;
                if (n < 140.0f) {
                    break;
                }
            }
            this.redrawMapScaleBitmap(n, n2);
            this.redrawNeeded = false;
        }
    }
    
    public void setImperialUnits(final boolean imperialUnits) {
        this.imperialUnits = imperialUnits;
        this.redrawNeeded = true;
    }
    
    public void setShowMapScaleBar(final boolean showMapScaleBar) {
        this.showMapScaleBar = showMapScaleBar;
    }
    
    public void setText(final TextField textField, final String s) {
        this.textFields.put(textField, s);
        this.redrawNeeded = true;
    }
    
    public enum TextField
    {
        FOOT, 
        KILOMETER, 
        METER, 
        MILE;
    }
}
