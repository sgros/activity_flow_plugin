package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Bitmap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.graphics.android.AndroidGraphics;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.MapReadResult;
import org.mapsforge.map.reader.PointOfInterest;
import org.mapsforge.map.reader.Way;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.mapsforge.map.rendertheme.GraphicAdapter.Color;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeHandler;
import org.xml.sax.SAXException;

public class DatabaseRenderer implements MapGenerator, RenderCallback {
    private static final Byte DEFAULT_START_ZOOM_LEVEL = Byte.valueOf(STROKE_MIN_ZOOM_LEVEL);
    private static final byte LAYERS = (byte) 11;
    private static final Logger LOGGER = Logger.getLogger(DatabaseRenderer.class.getName());
    private static final Paint PAINT_WATER_TILE_HIGHTLIGHT = AndroidGraphics.INSTANCE.getPaint();
    private static final double STROKE_INCREASE = 1.5d;
    private static final byte STROKE_MIN_ZOOM_LEVEL = (byte) 12;
    private static final Tag TAG_NATURAL_WATER = new Tag("natural", "water");
    private static final Point[][] WATER_TILE_COORDINATES = getTilePixelCoordinates();
    private static final byte ZOOM_MAX = (byte) 22;
    private final List<PointTextContainer> areaLabels = new ArrayList(64);
    private final CanvasRasterer canvasRasterer = new CanvasRasterer();
    private Point[][] coordinates;
    private Tile currentTile;
    private List<List<ShapePaintContainer>> drawingLayers;
    private final LabelPlacement labelPlacement = new LabelPlacement();
    private MapDatabase mapDatabase;
    private List<PointTextContainer> nodes = new ArrayList(64);
    private Point poiPosition;
    private final List<SymbolContainer> pointSymbols = new ArrayList(64);
    private XmlRenderTheme previousJobTheme;
    private float previousTextScale;
    private byte previousZoomLevel;
    private RenderTheme renderTheme;
    private ShapeContainer shapeContainer;
    private final List<WayTextContainer> wayNames = new ArrayList(64);
    private final List<SymbolContainer> waySymbols = new ArrayList(64);
    private final List<List<List<ShapePaintContainer>>> ways = new ArrayList(11);

    private static RenderTheme getRenderTheme(XmlRenderTheme jobTheme) {
        RenderTheme renderTheme = null;
        try {
            return RenderThemeHandler.getRenderTheme(AndroidGraphics.INSTANCE, jobTheme);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.SEVERE, renderTheme, e);
            return renderTheme;
        } catch (SAXException e2) {
            LOGGER.log(Level.SEVERE, renderTheme, e2);
            return renderTheme;
        } catch (IOException e3) {
            LOGGER.log(Level.SEVERE, renderTheme, e3);
            return renderTheme;
        }
    }

    private static Point[][] getTilePixelCoordinates() {
        Point point1 = new Point(0.0d, 0.0d);
        Point point2 = new Point(256.0d, 0.0d);
        Point point3 = new Point(256.0d, 256.0d);
        Point point4 = new Point(0.0d, 256.0d);
        Point[][] pointArr = new Point[1][];
        pointArr[0] = new Point[]{point1, point2, point3, point4, point1};
        return pointArr;
    }

    private static byte getValidLayer(byte layer) {
        if (layer < (byte) 0) {
            return (byte) 0;
        }
        if (layer >= LAYERS) {
            return (byte) 10;
        }
        return layer;
    }

    public DatabaseRenderer() {
        PAINT_WATER_TILE_HIGHTLIGHT.setStyle(Style.FILL);
        PAINT_WATER_TILE_HIGHTLIGHT.setColor(AndroidGraphics.INSTANCE.getColor(Color.CYAN));
    }

    public DatabaseRenderer(MapDatabase mapDatabase) {
        this.mapDatabase = mapDatabase;
        PAINT_WATER_TILE_HIGHTLIGHT.setStyle(Style.FILL);
        PAINT_WATER_TILE_HIGHTLIGHT.setColor(AndroidGraphics.INSTANCE.getColor(Color.CYAN));
    }

    public boolean executeJob(MapGeneratorJob mapGeneratorJob, Bitmap bitmap) {
        if (mapGeneratorJob == null) {
            return false;
        }
        this.currentTile = mapGeneratorJob.tile;
        XmlRenderTheme jobTheme = mapGeneratorJob.jobParameters.jobTheme;
        if (!jobTheme.equals(this.previousJobTheme)) {
            if (this.renderTheme != null) {
                this.renderTheme.destroy();
            }
            this.renderTheme = getRenderTheme(jobTheme);
            if (this.renderTheme == null) {
                this.previousJobTheme = null;
                return false;
            }
            createWayLists();
            this.previousJobTheme = jobTheme;
            this.previousZoomLevel = Byte.MIN_VALUE;
            this.previousTextScale = -1.0f;
        }
        byte zoomLevel = this.currentTile.zoomLevel;
        if (zoomLevel != this.previousZoomLevel) {
            setScaleStrokeWidth(zoomLevel);
            this.previousZoomLevel = zoomLevel;
        }
        float textScale = mapGeneratorJob.jobParameters.textScale;
        if (Float.compare(textScale, this.previousTextScale) != 0) {
            this.renderTheme.scaleTextSize(textScale);
            this.previousTextScale = textScale;
        }
        if (this.mapDatabase != null) {
            processReadMapData(this.mapDatabase.readMapData(this.currentTile));
        }
        this.nodes = this.labelPlacement.placeLabels(this.nodes, this.pointSymbols, this.areaLabels, this.currentTile);
        this.canvasRasterer.setCanvasBitmap(bitmap);
        this.canvasRasterer.fill(this.renderTheme.getMapBackground());
        this.canvasRasterer.drawWays(this.ways);
        this.canvasRasterer.drawSymbols(this.waySymbols);
        this.canvasRasterer.drawSymbols(this.pointSymbols);
        this.canvasRasterer.drawWayNames(this.wayNames);
        this.canvasRasterer.drawNodes(this.nodes);
        this.canvasRasterer.drawNodes(this.areaLabels);
        if (mapGeneratorJob.debugSettings.drawTileFrames) {
            this.canvasRasterer.drawTileFrame();
        }
        if (mapGeneratorJob.debugSettings.drawTileCoordinates) {
            this.canvasRasterer.drawTileCoordinates(this.currentTile);
        }
        clearLists();
        return true;
    }

    public GeoPoint getStartPoint() {
        if (this.mapDatabase == null || !this.mapDatabase.hasOpenFile()) {
            return null;
        }
        MapFileInfo mapFileInfo = this.mapDatabase.getMapFileInfo();
        if (mapFileInfo.startPosition != null) {
            return mapFileInfo.startPosition;
        }
        return mapFileInfo.boundingBox.getCenterPoint();
    }

    public Byte getStartZoomLevel() {
        if (this.mapDatabase != null && this.mapDatabase.hasOpenFile()) {
            MapFileInfo mapFileInfo = this.mapDatabase.getMapFileInfo();
            if (mapFileInfo.startZoomLevel != null) {
                return mapFileInfo.startZoomLevel;
            }
        }
        return DEFAULT_START_ZOOM_LEVEL;
    }

    public byte getZoomLevelMax() {
        return ZOOM_MAX;
    }

    public void renderArea(Paint fill, Paint stroke, int level) {
        List<ShapePaintContainer> list = (List) this.drawingLayers.get(level);
        list.add(new ShapePaintContainer(this.shapeContainer, fill));
        list.add(new ShapePaintContainer(this.shapeContainer, stroke));
    }

    public void renderAreaCaption(String caption, float verticalOffset, Paint fill, Paint stroke) {
        Point centerPosition = GeometryUtils.calculateCenterOfBoundingBox(this.coordinates[0]);
        this.areaLabels.add(new PointTextContainer(caption, centerPosition.f68x, centerPosition.f69y, fill, stroke));
    }

    public void renderAreaSymbol(org.mapsforge.map.graphics.Bitmap symbol) {
        Point centerPosition = GeometryUtils.calculateCenterOfBoundingBox(this.coordinates[0]);
        this.pointSymbols.add(new SymbolContainer(symbol, new Point(centerPosition.f68x - ((double) (symbol.getWidth() / 2)), centerPosition.f69y - ((double) (symbol.getHeight() / 2)))));
    }

    public void renderPointOfInterestCaption(String caption, float verticalOffset, Paint fill, Paint stroke) {
        this.nodes.add(new PointTextContainer(caption, this.poiPosition.f68x, this.poiPosition.f69y + ((double) verticalOffset), fill, stroke));
    }

    public void renderPointOfInterestCircle(float radius, Paint fill, Paint stroke, int level) {
        List<ShapePaintContainer> list = (List) this.drawingLayers.get(level);
        list.add(new ShapePaintContainer(new CircleContainer(this.poiPosition, radius), fill));
        list.add(new ShapePaintContainer(new CircleContainer(this.poiPosition, radius), stroke));
    }

    public void renderPointOfInterestSymbol(org.mapsforge.map.graphics.Bitmap symbol) {
        this.pointSymbols.add(new SymbolContainer(symbol, new Point(this.poiPosition.f68x - ((double) (symbol.getWidth() / 2)), this.poiPosition.f69y - ((double) (symbol.getHeight() / 2)))));
    }

    public void renderWay(Paint stroke, int level) {
        ((List) this.drawingLayers.get(level)).add(new ShapePaintContainer(this.shapeContainer, stroke));
    }

    public void renderWaySymbol(org.mapsforge.map.graphics.Bitmap symbolBitmap, boolean alignCenter, boolean repeatSymbol) {
        WayDecorator.renderSymbol(symbolBitmap, alignCenter, repeatSymbol, this.coordinates, this.waySymbols);
    }

    public void renderWayText(String textKey, Paint fill, Paint stroke) {
        WayDecorator.renderText(textKey, fill, stroke, this.coordinates, this.wayNames);
    }

    public boolean requiresInternetConnection() {
        return false;
    }

    public void setMapDatabase(MapDatabase mapDatabase) {
        this.mapDatabase = mapDatabase;
    }

    private void clearLists() {
        for (int i = this.ways.size() - 1; i >= 0; i--) {
            List<List<ShapePaintContainer>> innerWayList = (List) this.ways.get(i);
            for (int j = innerWayList.size() - 1; j >= 0; j--) {
                ((List) innerWayList.get(j)).clear();
            }
        }
        this.areaLabels.clear();
        this.nodes.clear();
        this.pointSymbols.clear();
        this.wayNames.clear();
        this.waySymbols.clear();
    }

    private void createWayLists() {
        int levels = this.renderTheme.getLevels();
        this.ways.clear();
        for (byte i = (byte) 10; i >= (byte) 0; i = (byte) (i - 1)) {
            List<List<ShapePaintContainer>> innerWayList = new ArrayList(levels);
            for (int j = levels - 1; j >= 0; j--) {
                innerWayList.add(new ArrayList(0));
            }
            this.ways.add(innerWayList);
        }
    }

    private void processReadMapData(MapReadResult mapReadResult) {
        if (mapReadResult != null) {
            for (PointOfInterest pointOfInterest : mapReadResult.pointOfInterests) {
                renderPointOfInterest(pointOfInterest);
            }
            for (Way way : mapReadResult.ways) {
                renderWay(way);
            }
            if (mapReadResult.isWater) {
                renderWaterBackground();
            }
        }
    }

    private void renderPointOfInterest(PointOfInterest pointOfInterest) {
        this.drawingLayers = (List) this.ways.get(getValidLayer(pointOfInterest.layer));
        this.poiPosition = scaleGeoPoint(pointOfInterest.position);
        this.renderTheme.matchNode(this, pointOfInterest.tags, this.currentTile.zoomLevel);
    }

    private void renderWaterBackground() {
        this.drawingLayers = (List) this.ways.get(0);
        this.coordinates = WATER_TILE_COORDINATES;
        this.shapeContainer = new WayContainer(this.coordinates);
        this.renderTheme.matchClosedWay(this, Arrays.asList(new Tag[]{TAG_NATURAL_WATER}), this.currentTile.zoomLevel);
    }

    private void renderWay(Way way) {
        this.drawingLayers = (List) this.ways.get(getValidLayer(way.layer));
        GeoPoint[][] geoPoints = way.geoPoints;
        this.coordinates = new Point[geoPoints.length][];
        for (int i = 0; i < this.coordinates.length; i++) {
            this.coordinates[i] = new Point[geoPoints[i].length];
            for (int j = 0; j < this.coordinates[i].length; j++) {
                this.coordinates[i][j] = scaleGeoPoint(geoPoints[i][j]);
            }
        }
        this.shapeContainer = new WayContainer(this.coordinates);
        if (GeometryUtils.isClosedWay(this.coordinates[0])) {
            this.renderTheme.matchClosedWay(this, way.tags, this.currentTile.zoomLevel);
        } else {
            this.renderTheme.matchLinearWay(this, way.tags, this.currentTile.zoomLevel);
        }
    }

    private Point scaleGeoPoint(GeoPoint geoPoint) {
        return new Point((double) ((float) (MercatorProjection.longitudeToPixelX(geoPoint.longitude, this.currentTile.zoomLevel) - ((double) this.currentTile.getPixelX()))), (double) ((float) (MercatorProjection.latitudeToPixelY(geoPoint.latitude, this.currentTile.zoomLevel) - ((double) this.currentTile.getPixelY()))));
    }

    private void setScaleStrokeWidth(byte zoomLevel) {
        this.renderTheme.scaleStrokeWidth((float) Math.pow(STROKE_INCREASE, (double) Math.max(zoomLevel - 12, 0)));
    }

    public void destroy() {
        if (this.renderTheme != null) {
            this.renderTheme.destroy();
            this.renderTheme = null;
        }
    }
}
