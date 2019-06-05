// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Bitmap;
import android.graphics.Path$Direction;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.model.Point;
import java.util.List;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.graphics.android.AndroidGraphics;
import android.graphics.Paint$Style;
import android.graphics.Typeface;
import android.graphics.Path$FillType;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Canvas;
import android.graphics.Paint;

class CanvasRasterer
{
    private static final Paint PAINT_BITMAP_FILTER;
    private static final Paint PAINT_TILE_COORDINATES;
    private static final Paint PAINT_TILE_COORDINATES_STROKE;
    private static final Paint PAINT_TILE_FRAME;
    private static final float[] TILE_FRAME;
    private final Canvas canvas;
    private final Path path;
    private final Matrix symbolMatrix;
    
    static {
        PAINT_BITMAP_FILTER = createAndroidPaint();
        PAINT_TILE_COORDINATES = createAndroidPaint();
        PAINT_TILE_COORDINATES_STROKE = createAndroidPaint();
        PAINT_TILE_FRAME = createAndroidPaint();
        TILE_FRAME = new float[] { 0.0f, 0.0f, 0.0f, 256.0f, 0.0f, 256.0f, 256.0f, 256.0f, 256.0f, 256.0f, 256.0f, 0.0f, 256.0f, 0.0f, 0.0f, 0.0f };
    }
    
    CanvasRasterer() {
        this.canvas = new Canvas();
        this.symbolMatrix = new Matrix();
        (this.path = new Path()).setFillType(Path$FillType.EVEN_ODD);
        configurePaints();
    }
    
    private static void configurePaints() {
        CanvasRasterer.PAINT_TILE_COORDINATES.setTypeface(Typeface.defaultFromStyle(1));
        CanvasRasterer.PAINT_TILE_COORDINATES.setTextSize(20.0f);
        CanvasRasterer.PAINT_TILE_COORDINATES.setTypeface(Typeface.defaultFromStyle(1));
        CanvasRasterer.PAINT_TILE_COORDINATES_STROKE.setStyle(Paint$Style.STROKE);
        CanvasRasterer.PAINT_TILE_COORDINATES_STROKE.setStrokeWidth(5.0f);
        CanvasRasterer.PAINT_TILE_COORDINATES_STROKE.setTextSize(20.0f);
        CanvasRasterer.PAINT_TILE_COORDINATES_STROKE.setColor(AndroidGraphics.INSTANCE.getColor(GraphicAdapter.Color.WHITE));
    }
    
    private static Paint createAndroidPaint() {
        return new Paint(1);
    }
    
    private void drawTileCoordinate(final String s, final int n) {
        this.canvas.drawText(s, 20.0f, (float)n, CanvasRasterer.PAINT_TILE_COORDINATES_STROKE);
        this.canvas.drawText(s, 20.0f, (float)n, CanvasRasterer.PAINT_TILE_COORDINATES);
    }
    
    void drawNodes(final List<PointTextContainer> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            final PointTextContainer pointTextContainer = list.get(i);
            if (pointTextContainer.paintBack != null) {
                this.canvas.drawText(pointTextContainer.text, (float)pointTextContainer.x, (float)pointTextContainer.y, AndroidGraphics.getAndroidPaint(pointTextContainer.paintBack));
            }
            this.canvas.drawText(pointTextContainer.text, (float)pointTextContainer.x, (float)pointTextContainer.y, AndroidGraphics.getAndroidPaint(pointTextContainer.paintFront));
        }
    }
    
    void drawSymbols(final List<SymbolContainer> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            final SymbolContainer symbolContainer = list.get(i);
            final Point point = symbolContainer.point;
            if (symbolContainer.alignCenter) {
                final int n = symbolContainer.symbol.getWidth() / 2;
                final int n2 = symbolContainer.symbol.getHeight() / 2;
                this.symbolMatrix.setRotate(symbolContainer.rotation, (float)n, (float)n2);
                this.symbolMatrix.postTranslate((float)(point.x - n), (float)(point.y - n2));
            }
            else {
                this.symbolMatrix.setRotate(symbolContainer.rotation);
                this.symbolMatrix.postTranslate((float)point.x, (float)point.y);
            }
            this.canvas.drawBitmap(AndroidGraphics.getAndroidBitmap(symbolContainer.symbol), this.symbolMatrix, CanvasRasterer.PAINT_BITMAP_FILTER);
        }
    }
    
    void drawTileCoordinates(final Tile tile) {
        this.drawTileCoordinate("X: " + tile.tileX, 30);
        this.drawTileCoordinate("Y: " + tile.tileY, 60);
        this.drawTileCoordinate("Z: " + tile.zoomLevel, 90);
    }
    
    void drawTileFrame() {
        this.canvas.drawLines(CanvasRasterer.TILE_FRAME, CanvasRasterer.PAINT_TILE_FRAME);
    }
    
    void drawWayNames(final List<WayTextContainer> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            final WayTextContainer wayTextContainer = list.get(i);
            this.path.rewind();
            final double[] coordinates = wayTextContainer.coordinates;
            this.path.moveTo((float)coordinates[0], (float)coordinates[1]);
            for (int j = 2; j < coordinates.length; j += 2) {
                this.path.lineTo((float)coordinates[j], (float)coordinates[j + 1]);
            }
            this.canvas.drawTextOnPath(wayTextContainer.text, this.path, 0.0f, 3.0f, AndroidGraphics.getAndroidPaint(wayTextContainer.paint));
        }
    }
    
    void drawWays(final List<List<List<ShapePaintContainer>>> list) {
        final int size = list.get(0).size();
        for (int i = 0; i < list.size(); ++i) {
            final List<List<ShapePaintContainer>> list2 = list.get(i);
            for (int j = 0; j < size; ++j) {
                final List<ShapePaintContainer> list3 = list2.get(j);
                for (int k = list3.size() - 1; k >= 0; --k) {
                    final ShapePaintContainer shapePaintContainer = list3.get(k);
                    this.path.rewind();
                    switch (shapePaintContainer.shapeContainer.getShapeType()) {
                        case CIRCLE: {
                            final CircleContainer circleContainer = (CircleContainer)shapePaintContainer.shapeContainer;
                            final Point point = circleContainer.point;
                            this.path.addCircle((float)point.x, (float)point.y, circleContainer.radius, Path$Direction.CCW);
                            break;
                        }
                        case WAY: {
                            final Point[][] coordinates = ((WayContainer)shapePaintContainer.shapeContainer).coordinates;
                            for (int l = 0; l < coordinates.length; ++l) {
                                final Point[] array = coordinates[l];
                                if (array.length >= 2) {
                                    final Point point2 = array[0];
                                    this.path.moveTo((float)point2.x, (float)point2.y);
                                    for (int n = 1; n < array.length; ++n) {
                                        final Point point3 = array[n];
                                        this.path.lineTo((float)point3.x, (float)point3.y);
                                    }
                                }
                            }
                            break;
                        }
                    }
                    this.canvas.drawPath(this.path, AndroidGraphics.getAndroidPaint(shapePaintContainer.paint));
                }
            }
        }
    }
    
    void fill(final int n) {
        this.canvas.drawColor(n);
    }
    
    void setCanvasBitmap(final Bitmap bitmap) {
        this.canvas.setBitmap(bitmap);
    }
}
