package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.Typeface;
import java.util.List;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.graphics.android.AndroidGraphics;
import org.mapsforge.map.rendertheme.GraphicAdapter.Color;

class CanvasRasterer {
    private static final Paint PAINT_BITMAP_FILTER = createAndroidPaint();
    private static final Paint PAINT_TILE_COORDINATES = createAndroidPaint();
    private static final Paint PAINT_TILE_COORDINATES_STROKE = createAndroidPaint();
    private static final Paint PAINT_TILE_FRAME = createAndroidPaint();
    private static final float[] TILE_FRAME = new float[]{0.0f, 0.0f, 0.0f, 256.0f, 0.0f, 256.0f, 256.0f, 256.0f, 256.0f, 256.0f, 256.0f, 0.0f, 256.0f, 0.0f, 0.0f, 0.0f};
    private final Canvas canvas = new Canvas();
    private final Path path = new Path();
    private final Matrix symbolMatrix = new Matrix();

    private static Paint createAndroidPaint() {
        return new Paint(1);
    }

    private static void configurePaints() {
        PAINT_TILE_COORDINATES.setTypeface(Typeface.defaultFromStyle(1));
        PAINT_TILE_COORDINATES.setTextSize(20.0f);
        PAINT_TILE_COORDINATES.setTypeface(Typeface.defaultFromStyle(1));
        PAINT_TILE_COORDINATES_STROKE.setStyle(Style.STROKE);
        PAINT_TILE_COORDINATES_STROKE.setStrokeWidth(5.0f);
        PAINT_TILE_COORDINATES_STROKE.setTextSize(20.0f);
        PAINT_TILE_COORDINATES_STROKE.setColor(AndroidGraphics.INSTANCE.getColor(Color.WHITE));
    }

    CanvasRasterer() {
        this.path.setFillType(FillType.EVEN_ODD);
        configurePaints();
    }

    private void drawTileCoordinate(String string, int offsetY) {
        this.canvas.drawText(string, 20.0f, (float) offsetY, PAINT_TILE_COORDINATES_STROKE);
        this.canvas.drawText(string, 20.0f, (float) offsetY, PAINT_TILE_COORDINATES);
    }

    /* Access modifiers changed, original: 0000 */
    public void drawNodes(List<PointTextContainer> pointTextContainers) {
        for (int index = pointTextContainers.size() - 1; index >= 0; index--) {
            PointTextContainer pointTextContainer = (PointTextContainer) pointTextContainers.get(index);
            if (pointTextContainer.paintBack != null) {
                this.canvas.drawText(pointTextContainer.text, (float) pointTextContainer.f66x, (float) pointTextContainer.f67y, AndroidGraphics.getAndroidPaint(pointTextContainer.paintBack));
            }
            this.canvas.drawText(pointTextContainer.text, (float) pointTextContainer.f66x, (float) pointTextContainer.f67y, AndroidGraphics.getAndroidPaint(pointTextContainer.paintFront));
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawSymbols(List<SymbolContainer> symbolContainers) {
        for (int index = symbolContainers.size() - 1; index >= 0; index--) {
            SymbolContainer symbolContainer = (SymbolContainer) symbolContainers.get(index);
            Point point = symbolContainer.point;
            if (symbolContainer.alignCenter) {
                int pivotX = symbolContainer.symbol.getWidth() / 2;
                int pivotY = symbolContainer.symbol.getHeight() / 2;
                this.symbolMatrix.setRotate(symbolContainer.rotation, (float) pivotX, (float) pivotY);
                this.symbolMatrix.postTranslate((float) (point.f68x - ((double) pivotX)), (float) (point.f69y - ((double) pivotY)));
            } else {
                this.symbolMatrix.setRotate(symbolContainer.rotation);
                this.symbolMatrix.postTranslate((float) point.f68x, (float) point.f69y);
            }
            this.canvas.drawBitmap(AndroidGraphics.getAndroidBitmap(symbolContainer.symbol), this.symbolMatrix, PAINT_BITMAP_FILTER);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawTileCoordinates(Tile tile) {
        drawTileCoordinate("X: " + tile.tileX, 30);
        drawTileCoordinate("Y: " + tile.tileY, 60);
        drawTileCoordinate("Z: " + tile.zoomLevel, 90);
    }

    /* Access modifiers changed, original: 0000 */
    public void drawTileFrame() {
        this.canvas.drawLines(TILE_FRAME, PAINT_TILE_FRAME);
    }

    /* Access modifiers changed, original: 0000 */
    public void drawWayNames(List<WayTextContainer> wayTextContainers) {
        for (int index = wayTextContainers.size() - 1; index >= 0; index--) {
            WayTextContainer wayTextContainer = (WayTextContainer) wayTextContainers.get(index);
            this.path.rewind();
            double[] textCoordinates = wayTextContainer.coordinates;
            this.path.moveTo((float) textCoordinates[0], (float) textCoordinates[1]);
            for (int i = 2; i < textCoordinates.length; i += 2) {
                this.path.lineTo((float) textCoordinates[i], (float) textCoordinates[i + 1]);
            }
            this.canvas.drawTextOnPath(wayTextContainer.text, this.path, 0.0f, 3.0f, AndroidGraphics.getAndroidPaint(wayTextContainer.paint));
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawWays(List<List<List<ShapePaintContainer>>> drawWays) {
        int levelsPerLayer = ((List) drawWays.get(0)).size();
        int layers = drawWays.size();
        for (int layer = 0; layer < layers; layer++) {
            List<List<ShapePaintContainer>> shapePaintContainers = (List) drawWays.get(layer);
            for (int level = 0; level < levelsPerLayer; level++) {
                List<ShapePaintContainer> wayList = (List) shapePaintContainers.get(level);
                for (int index = wayList.size() - 1; index >= 0; index--) {
                    ShapePaintContainer shapePaintContainer = (ShapePaintContainer) wayList.get(index);
                    this.path.rewind();
                    switch (shapePaintContainer.shapeContainer.getShapeType()) {
                        case CIRCLE:
                            CircleContainer circleContainer = shapePaintContainer.shapeContainer;
                            Point point = circleContainer.point;
                            this.path.addCircle((float) point.f68x, (float) point.f69y, circleContainer.radius, Direction.CCW);
                            break;
                        case WAY:
                            Point[][] coordinates = ((WayContainer) shapePaintContainer.shapeContainer).coordinates;
                            for (Point[] points : coordinates) {
                                if (points.length >= 2) {
                                    Point immutablePoint = points[0];
                                    this.path.moveTo((float) immutablePoint.f68x, (float) immutablePoint.f69y);
                                    for (int i = 1; i < points.length; i++) {
                                        immutablePoint = points[i];
                                        this.path.lineTo((float) immutablePoint.f68x, (float) immutablePoint.f69y);
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    this.canvas.drawPath(this.path, AndroidGraphics.getAndroidPaint(shapePaintContainer.paint));
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void fill(int color) {
        this.canvas.drawColor(color);
    }

    /* Access modifiers changed, original: 0000 */
    public void setCanvasBitmap(Bitmap bitmap) {
        this.canvas.setBitmap(bitmap);
    }
}
