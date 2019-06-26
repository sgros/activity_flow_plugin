package menion.android.whereyougo.maps.mapsforge.mapgenerator;

import android.util.AttributeSet;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.android.maps.mapgenerator.blank.Blank;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;
import org.mapsforge.android.maps.mapgenerator.tiledownloader.FormatURLTileDownloader;

public final class MapGeneratorFactory {
   private static final String MAP_GENERATOR_ATTRIBUTE_NAME = "mapGenerator";

   private MapGeneratorFactory() {
      throw new IllegalStateException();
   }

   public static MapGenerator createMapGenerator(AttributeSet var0) {
      String var1 = var0.getAttributeValue((String)null, "mapGenerator");
      Object var2;
      if (var1 == null) {
         var2 = new DatabaseRenderer();
      } else {
         var2 = createMapGenerator(MapGeneratorInternal.valueOf(var1));
      }

      return (MapGenerator)var2;
   }

   public static MapGenerator createMapGenerator(MapGeneratorInternal var0) {
      Object var1;
      switch(var0) {
      case BLANK:
         var1 = new Blank();
         break;
      case DATABASE_RENDERER:
         var1 = new DatabaseRenderer();
         break;
      case OPENSTREETMAP:
         var1 = new FormatURLTileDownloader(18, "http://a.tile.openstreetmap.org/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
         break;
      case OPENSTREETMAP_DE:
         var1 = new FormatURLTileDownloader(18, "http://a.tile.openstreetmap.de/tiles/osmde/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
         break;
      case OPENCYCLEMAP_CYCLE:
         var1 = new FormatURLTileDownloader(18, "http://a.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Tiles courtesy of <a href='http://www.opencyclemap.org'>Andy Allan</a>");
         break;
      case OPENCYCLEMAP_TRANSPORT:
         var1 = new FormatURLTileDownloader(18, "http://a.tile2.opencyclemap.org/transport/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Tiles courtesy of <a href='http://www.opencyclemap.org'>Andy Allan</a>");
         break;
      case OPENMAPSURFER_ROADS:
         var1 = new FormatURLTileDownloader(18, "http://openmapsurfer.uni-hd.de/tiles/roads/x={x}&y={y}&z={z}", "Imagery from <a href=\"http://giscience.uni-hd.de/\">GIScience Research Group @ University of Heidelberg</a> &mdash; Map data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
         break;
      case MAPQUEST:
         var1 = new FormatURLTileDownloader(18, "http://otile1.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Tiles Courtesy of <a href=\"http://www.mapquest.com/\">MapQuest</a>");
         break;
      case MAPQUEST_AERIAL:
         var1 = new FormatURLTileDownloader(18, "http://otile1.mqcdn.com/tiles/1.0.0/sat/{z}/{x}/{y}.png", "Portions Courtesy NASA/JPL-Caltech and U.S. Depart. of Agriculture, Farm Service Agency, Tiles Courtesy of <a href=\"http://www.mapquest.com/\">MapQuest</a>");
         break;
      case THUNDERFOREST_OPENCYCLEMAP:
         var1 = new FormatURLTileDownloader(18, "http://a.tile.thunderforest.com/cycle/{z}/{x}/{y}.png", "Maps © <a href=\"http://www.thunderforest.com\">Thunderforest</a>, Data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
         break;
      case THUNDERFOREST_TRANSPORT:
         var1 = new FormatURLTileDownloader(18, "http://a.tile.opencyclemap.org/transport/{z}/{x}/{y}.png", "Maps © <a href=\"http://www.thunderforest.com\">Thunderforest</a>, Data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
         break;
      case THUNDERFOREST_LANDSCAPE:
         var1 = new FormatURLTileDownloader(18, "http://a.tile.opencyclemap.org/landscape/{z}/{x}/{y}.png", "Maps © <a href=\"http://www.thunderforest.com\">Thunderforest</a>, Data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
         break;
      case THUNDERFOREST_OUTDOORS:
         var1 = new FormatURLTileDownloader(18, "http://a.tile.opencyclemap.org/outdoors/{z}/{x}/{y}.png", "Maps © <a href=\"http://www.thunderforest.com\">Thunderforest</a>, Data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
         break;
      case ESRI_WORLD_STREET_MAP:
         var1 = new FormatURLTileDownloader(18, "http://server.arcgisonline.com//ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}.png", "© <a href=\"http://www.esri.com/\">Esri</a>, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, Esri China (Hong Kong), Esri (Thailand), TomTom, 2012");
         break;
      case ESRI_WORLD_IMAGERY:
         var1 = new FormatURLTileDownloader(18, "http://server.arcgisonline.com//ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}.jpg", "© <a href=\"http://www.esri.com/\">Esri</a>, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community");
         break;
      default:
         var1 = new Blank();
      }

      return (MapGenerator)var1;
   }
}
