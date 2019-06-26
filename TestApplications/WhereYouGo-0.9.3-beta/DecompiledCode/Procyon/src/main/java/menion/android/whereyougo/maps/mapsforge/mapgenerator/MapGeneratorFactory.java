// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.mapgenerator;

import org.mapsforge.android.maps.mapgenerator.tiledownloader.FormatURLTileDownloader;
import org.mapsforge.android.maps.mapgenerator.blank.Blank;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import android.util.AttributeSet;

public final class MapGeneratorFactory
{
    private static final String MAP_GENERATOR_ATTRIBUTE_NAME = "mapGenerator";
    
    private MapGeneratorFactory() {
        throw new IllegalStateException();
    }
    
    public static MapGenerator createMapGenerator(final AttributeSet set) {
        final String attributeValue = set.getAttributeValue((String)null, "mapGenerator");
        MapGenerator mapGenerator;
        if (attributeValue == null) {
            mapGenerator = new DatabaseRenderer();
        }
        else {
            mapGenerator = createMapGenerator(MapGeneratorInternal.valueOf(attributeValue));
        }
        return mapGenerator;
    }
    
    public static MapGenerator createMapGenerator(final MapGeneratorInternal mapGeneratorInternal) {
        MapGenerator mapGenerator = null;
        switch (mapGeneratorInternal) {
            default: {
                mapGenerator = new Blank();
                break;
            }
            case BLANK: {
                mapGenerator = new Blank();
                break;
            }
            case DATABASE_RENDERER: {
                mapGenerator = new DatabaseRenderer();
                break;
            }
            case OPENSTREETMAP: {
                mapGenerator = new FormatURLTileDownloader(18, "http://a.tile.openstreetmap.org/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
                break;
            }
            case OPENSTREETMAP_DE: {
                mapGenerator = new FormatURLTileDownloader(18, "http://a.tile.openstreetmap.de/tiles/osmde/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
                break;
            }
            case OPENCYCLEMAP_CYCLE: {
                mapGenerator = new FormatURLTileDownloader(18, "http://a.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Tiles courtesy of <a href='http://www.opencyclemap.org'>Andy Allan</a>");
                break;
            }
            case OPENCYCLEMAP_TRANSPORT: {
                mapGenerator = new FormatURLTileDownloader(18, "http://a.tile2.opencyclemap.org/transport/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Tiles courtesy of <a href='http://www.opencyclemap.org'>Andy Allan</a>");
                break;
            }
            case OPENMAPSURFER_ROADS: {
                mapGenerator = new FormatURLTileDownloader(18, "http://openmapsurfer.uni-hd.de/tiles/roads/x={x}&y={y}&z={z}", "Imagery from <a href=\"http://giscience.uni-hd.de/\">GIScience Research Group @ University of Heidelberg</a> &mdash; Map data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
                break;
            }
            case MAPQUEST: {
                mapGenerator = new FormatURLTileDownloader(18, "http://otile1.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png", "© <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Tiles Courtesy of <a href=\"http://www.mapquest.com/\">MapQuest</a>");
                break;
            }
            case MAPQUEST_AERIAL: {
                mapGenerator = new FormatURLTileDownloader(18, "http://otile1.mqcdn.com/tiles/1.0.0/sat/{z}/{x}/{y}.png", "Portions Courtesy NASA/JPL-Caltech and U.S. Depart. of Agriculture, Farm Service Agency, Tiles Courtesy of <a href=\"http://www.mapquest.com/\">MapQuest</a>");
                break;
            }
            case THUNDERFOREST_OPENCYCLEMAP: {
                mapGenerator = new FormatURLTileDownloader(18, "http://a.tile.thunderforest.com/cycle/{z}/{x}/{y}.png", "Maps © <a href=\"http://www.thunderforest.com\">Thunderforest</a>, Data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
                break;
            }
            case THUNDERFOREST_TRANSPORT: {
                mapGenerator = new FormatURLTileDownloader(18, "http://a.tile.opencyclemap.org/transport/{z}/{x}/{y}.png", "Maps © <a href=\"http://www.thunderforest.com\">Thunderforest</a>, Data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
                break;
            }
            case THUNDERFOREST_LANDSCAPE: {
                mapGenerator = new FormatURLTileDownloader(18, "http://a.tile.opencyclemap.org/landscape/{z}/{x}/{y}.png", "Maps © <a href=\"http://www.thunderforest.com\">Thunderforest</a>, Data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
                break;
            }
            case THUNDERFOREST_OUTDOORS: {
                mapGenerator = new FormatURLTileDownloader(18, "http://a.tile.opencyclemap.org/outdoors/{z}/{x}/{y}.png", "Maps © <a href=\"http://www.thunderforest.com\">Thunderforest</a>, Data © <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>");
                break;
            }
            case ESRI_WORLD_STREET_MAP: {
                mapGenerator = new FormatURLTileDownloader(18, "http://server.arcgisonline.com//ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}.png", "© <a href=\"http://www.esri.com/\">Esri</a>, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, Esri China (Hong Kong), Esri (Thailand), TomTom, 2012");
                break;
            }
            case ESRI_WORLD_IMAGERY: {
                mapGenerator = new FormatURLTileDownloader(18, "http://server.arcgisonline.com//ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}.jpg", "© <a href=\"http://www.esri.com/\">Esri</a>, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community");
                break;
            }
        }
        return mapGenerator;
    }
}
