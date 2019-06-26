package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Bitmap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeHandler;
import org.xml.sax.SAXException;

public class DatabaseRenderer implements MapGenerator, RenderCallback {
   private static final Byte DEFAULT_START_ZOOM_LEVEL = 12;
   private static final byte LAYERS = 11;
   private static final Logger LOGGER = Logger.getLogger(DatabaseRenderer.class.getName());
   private static final Paint PAINT_WATER_TILE_HIGHTLIGHT;
   private static final double STROKE_INCREASE = 1.5D;
   private static final byte STROKE_MIN_ZOOM_LEVEL = 12;
   private static final Tag TAG_NATURAL_WATER;
   private static final Point[][] WATER_TILE_COORDINATES;
   private static final byte ZOOM_MAX = 22;
   private final List areaLabels;
   private final CanvasRasterer canvasRasterer;
   private Point[][] coordinates;
   private Tile currentTile;
   private List drawingLayers;
   private final LabelPlacement labelPlacement;
   private MapDatabase mapDatabase;
   private List nodes;
   private Point poiPosition;
   private final List pointSymbols;
   private XmlRenderTheme previousJobTheme;
   private float previousTextScale;
   private byte previousZoomLevel;
   private RenderTheme renderTheme;
   private ShapeContainer shapeContainer;
   private final List wayNames;
   private final List waySymbols;
   private final List ways;

   static {
      PAINT_WATER_TILE_HIGHTLIGHT = AndroidGraphics.INSTANCE.getPaint();
      TAG_NATURAL_WATER = new Tag("natural", "water");
      WATER_TILE_COORDINATES = getTilePixelCoordinates();
   }

   public DatabaseRenderer() {
      this.canvasRasterer = new CanvasRasterer();
      this.labelPlacement = new LabelPlacement();
      this.ways = new ArrayList(11);
      this.wayNames = new ArrayList(64);
      this.nodes = new ArrayList(64);
      this.areaLabels = new ArrayList(64);
      this.waySymbols = new ArrayList(64);
      this.pointSymbols = new ArrayList(64);
      PAINT_WATER_TILE_HIGHTLIGHT.setStyle(Style.FILL);
      PAINT_WATER_TILE_HIGHTLIGHT.setColor(AndroidGraphics.INSTANCE.getColor(GraphicAdapter.Color.CYAN));
   }

   public DatabaseRenderer(MapDatabase var1) {
      this.mapDatabase = var1;
      this.canvasRasterer = new CanvasRasterer();
      this.labelPlacement = new LabelPlacement();
      this.ways = new ArrayList(11);
      this.wayNames = new ArrayList(64);
      this.nodes = new ArrayList(64);
      this.areaLabels = new ArrayList(64);
      this.waySymbols = new ArrayList(64);
      this.pointSymbols = new ArrayList(64);
      PAINT_WATER_TILE_HIGHTLIGHT.setStyle(Style.FILL);
      PAINT_WATER_TILE_HIGHTLIGHT.setColor(AndroidGraphics.INSTANCE.getColor(GraphicAdapter.Color.CYAN));
   }

   private void clearLists() {
      for(int var1 = this.ways.size() - 1; var1 >= 0; --var1) {
         List var2 = (List)this.ways.get(var1);

         for(int var3 = var2.size() - 1; var3 >= 0; --var3) {
            ((List)var2.get(var3)).clear();
         }
      }

      this.areaLabels.clear();
      this.nodes.clear();
      this.pointSymbols.clear();
      this.wayNames.clear();
      this.waySymbols.clear();
   }

   private void createWayLists() {
      int var1 = this.renderTheme.getLevels();
      this.ways.clear();

      for(byte var2 = 10; var2 >= 0; --var2) {
         ArrayList var3 = new ArrayList(var1);

         for(int var4 = var1 - 1; var4 >= 0; --var4) {
            var3.add(new ArrayList(0));
         }

         this.ways.add(var3);
      }

   }

   private static RenderTheme getRenderTheme(XmlRenderTheme var0) {
      Object var1 = null;

      RenderTheme var5;
      try {
         var5 = RenderThemeHandler.getRenderTheme(AndroidGraphics.INSTANCE, var0);
      } catch (ParserConfigurationException var2) {
         LOGGER.log(Level.SEVERE, (String)null, var2);
         var5 = (RenderTheme)var1;
      } catch (SAXException var3) {
         LOGGER.log(Level.SEVERE, (String)null, var3);
         var5 = (RenderTheme)var1;
      } catch (IOException var4) {
         LOGGER.log(Level.SEVERE, (String)null, var4);
         var5 = (RenderTheme)var1;
      }

      return var5;
   }

   private static Point[][] getTilePixelCoordinates() {
      Point var0 = new Point(0.0D, 0.0D);
      return new Point[][]{{var0, new Point(256.0D, 0.0D), new Point(256.0D, 256.0D), new Point(0.0D, 256.0D), var0}};
   }

   private static byte getValidLayer(byte var0) {
      byte var1;
      byte var2;
      if (var0 < 0) {
         var2 = 0;
         var1 = var2;
      } else {
         var1 = var0;
         if (var0 >= 11) {
            var2 = 10;
            var1 = var2;
         }
      }

      return var1;
   }

   private void processReadMapData(MapReadResult var1) {
      if (var1 != null) {
         Iterator var2 = var1.pointOfInterests.iterator();

         while(var2.hasNext()) {
            this.renderPointOfInterest((PointOfInterest)var2.next());
         }

         var2 = var1.ways.iterator();

         while(var2.hasNext()) {
            this.renderWay((Way)var2.next());
         }

         if (var1.isWater) {
            this.renderWaterBackground();
         }
      }

   }

   private void renderPointOfInterest(PointOfInterest var1) {
      this.drawingLayers = (List)this.ways.get(getValidLayer(var1.layer));
      this.poiPosition = this.scaleGeoPoint(var1.position);
      this.renderTheme.matchNode(this, var1.tags, this.currentTile.zoomLevel);
   }

   private void renderWaterBackground() {
      this.drawingLayers = (List)this.ways.get(0);
      this.coordinates = WATER_TILE_COORDINATES;
      this.shapeContainer = new WayContainer(this.coordinates);
      this.renderTheme.matchClosedWay(this, Arrays.asList(TAG_NATURAL_WATER), this.currentTile.zoomLevel);
   }

   private void renderWay(Way var1) {
      this.drawingLayers = (List)this.ways.get(getValidLayer(var1.layer));
      GeoPoint[][] var2 = var1.geoPoints;
      this.coordinates = new Point[var2.length][];

      for(int var3 = 0; var3 < this.coordinates.length; ++var3) {
         this.coordinates[var3] = new Point[var2[var3].length];

         for(int var4 = 0; var4 < this.coordinates[var3].length; ++var4) {
            this.coordinates[var3][var4] = this.scaleGeoPoint(var2[var3][var4]);
         }
      }

      this.shapeContainer = new WayContainer(this.coordinates);
      if (GeometryUtils.isClosedWay(this.coordinates[0])) {
         this.renderTheme.matchClosedWay(this, var1.tags, this.currentTile.zoomLevel);
      } else {
         this.renderTheme.matchLinearWay(this, var1.tags, this.currentTile.zoomLevel);
      }

   }

   private Point scaleGeoPoint(GeoPoint var1) {
      double var2 = MercatorProjection.longitudeToPixelX(var1.longitude, this.currentTile.zoomLevel);
      double var4 = (double)this.currentTile.getPixelX();
      double var6 = MercatorProjection.latitudeToPixelY(var1.latitude, this.currentTile.zoomLevel);
      double var8 = (double)this.currentTile.getPixelY();
      return new Point((double)((float)(var2 - var4)), (double)((float)(var6 - var8)));
   }

   private void setScaleStrokeWidth(byte var1) {
      int var2 = Math.max(var1 - 12, 0);
      this.renderTheme.scaleStrokeWidth((float)Math.pow(1.5D, (double)var2));
   }

   public void destroy() {
      if (this.renderTheme != null) {
         this.renderTheme.destroy();
         this.renderTheme = null;
      }

   }

   public boolean executeJob(MapGeneratorJob var1, Bitmap var2) {
      boolean var3 = false;
      if (var1 != null) {
         this.currentTile = var1.tile;
         XmlRenderTheme var4 = var1.jobParameters.jobTheme;
         if (!var4.equals(this.previousJobTheme)) {
            if (this.renderTheme != null) {
               this.renderTheme.destroy();
            }

            this.renderTheme = getRenderTheme(var4);
            if (this.renderTheme == null) {
               this.previousJobTheme = null;
               return var3;
            }

            this.createWayLists();
            this.previousJobTheme = var4;
            this.previousZoomLevel = (byte)-128;
            this.previousTextScale = -1.0F;
         }

         byte var5 = this.currentTile.zoomLevel;
         if (var5 != this.previousZoomLevel) {
            this.setScaleStrokeWidth(var5);
            this.previousZoomLevel = var5;
         }

         float var6 = var1.jobParameters.textScale;
         if (Float.compare(var6, this.previousTextScale) != 0) {
            this.renderTheme.scaleTextSize(var6);
            this.previousTextScale = var6;
         }

         if (this.mapDatabase != null) {
            this.processReadMapData(this.mapDatabase.readMapData(this.currentTile));
         }

         this.nodes = this.labelPlacement.placeLabels(this.nodes, this.pointSymbols, this.areaLabels, this.currentTile);
         this.canvasRasterer.setCanvasBitmap(var2);
         this.canvasRasterer.fill(this.renderTheme.getMapBackground());
         this.canvasRasterer.drawWays(this.ways);
         this.canvasRasterer.drawSymbols(this.waySymbols);
         this.canvasRasterer.drawSymbols(this.pointSymbols);
         this.canvasRasterer.drawWayNames(this.wayNames);
         this.canvasRasterer.drawNodes(this.nodes);
         this.canvasRasterer.drawNodes(this.areaLabels);
         if (var1.debugSettings.drawTileFrames) {
            this.canvasRasterer.drawTileFrame();
         }

         if (var1.debugSettings.drawTileCoordinates) {
            this.canvasRasterer.drawTileCoordinates(this.currentTile);
         }

         this.clearLists();
         var3 = true;
      }

      return var3;
   }

   public GeoPoint getStartPoint() {
      GeoPoint var1;
      if (this.mapDatabase != null && this.mapDatabase.hasOpenFile()) {
         MapFileInfo var2 = this.mapDatabase.getMapFileInfo();
         if (var2.startPosition != null) {
            var1 = var2.startPosition;
         } else {
            var1 = var2.boundingBox.getCenterPoint();
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public Byte getStartZoomLevel() {
      Byte var2;
      if (this.mapDatabase != null && this.mapDatabase.hasOpenFile()) {
         MapFileInfo var1 = this.mapDatabase.getMapFileInfo();
         if (var1.startZoomLevel != null) {
            var2 = var1.startZoomLevel;
            return var2;
         }
      }

      var2 = DEFAULT_START_ZOOM_LEVEL;
      return var2;
   }

   public byte getZoomLevelMax() {
      return 22;
   }

   public void renderArea(Paint var1, Paint var2, int var3) {
      List var4 = (List)this.drawingLayers.get(var3);
      var4.add(new ShapePaintContainer(this.shapeContainer, var1));
      var4.add(new ShapePaintContainer(this.shapeContainer, var2));
   }

   public void renderAreaCaption(String var1, float var2, Paint var3, Paint var4) {
      Point var5 = GeometryUtils.calculateCenterOfBoundingBox(this.coordinates[0]);
      this.areaLabels.add(new PointTextContainer(var1, var5.x, var5.y, var3, var4));
   }

   public void renderAreaSymbol(org.mapsforge.map.graphics.Bitmap var1) {
      Point var2 = GeometryUtils.calculateCenterOfBoundingBox(this.coordinates[0]);
      int var3 = var1.getWidth() / 2;
      int var4 = var1.getHeight() / 2;
      var2 = new Point(var2.x - (double)var3, var2.y - (double)var4);
      this.pointSymbols.add(new SymbolContainer(var1, var2));
   }

   public void renderPointOfInterestCaption(String var1, float var2, Paint var3, Paint var4) {
      this.nodes.add(new PointTextContainer(var1, this.poiPosition.x, this.poiPosition.y + (double)var2, var3, var4));
   }

   public void renderPointOfInterestCircle(float var1, Paint var2, Paint var3, int var4) {
      List var5 = (List)this.drawingLayers.get(var4);
      var5.add(new ShapePaintContainer(new CircleContainer(this.poiPosition, var1), var2));
      var5.add(new ShapePaintContainer(new CircleContainer(this.poiPosition, var1), var3));
   }

   public void renderPointOfInterestSymbol(org.mapsforge.map.graphics.Bitmap var1) {
      int var2 = var1.getWidth() / 2;
      int var3 = var1.getHeight() / 2;
      Point var4 = new Point(this.poiPosition.x - (double)var2, this.poiPosition.y - (double)var3);
      this.pointSymbols.add(new SymbolContainer(var1, var4));
   }

   public void renderWay(Paint var1, int var2) {
      ((List)this.drawingLayers.get(var2)).add(new ShapePaintContainer(this.shapeContainer, var1));
   }

   public void renderWaySymbol(org.mapsforge.map.graphics.Bitmap var1, boolean var2, boolean var3) {
      WayDecorator.renderSymbol(var1, var2, var3, this.coordinates, this.waySymbols);
   }

   public void renderWayText(String var1, Paint var2, Paint var3) {
      WayDecorator.renderText(var1, var2, var3, this.coordinates, this.wayNames);
   }

   public boolean requiresInternetConnection() {
      return false;
   }

   public void setMapDatabase(MapDatabase var1) {
      this.mapDatabase = var1;
   }
}
