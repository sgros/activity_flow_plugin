// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.mapProvider;

import android.os.IBinder;
import android.content.Intent;
import locus.api.android.features.mapProvider.data.MapTileRequest;
import locus.api.android.features.mapProvider.data.MapTileResponse;
import android.os.RemoteException;
import java.util.List;
import locus.api.android.features.mapProvider.data.MapConfigLayer;
import java.util.ArrayList;
import locus.api.utils.Logger;
import android.app.Service;

public abstract class MapTileService extends Service
{
    private static final String TAG;
    private final IMapTileService.Stub mBinder;
    
    static {
        TAG = MapTileService.class.getSimpleName();
    }
    
    public MapTileService() {
        this.mBinder = new IMapTileService.Stub() {
            public MapDataContainer getMapConfigs() throws RemoteException {
                final List<MapConfigLayer> mapConfigs = MapTileService.this.getMapConfigs();
                MapDataContainer mapDataContainer;
                if (mapConfigs == null || mapConfigs.size() == 0) {
                    Logger.logW(MapTileService.TAG, "getMapConfigs(), invalid configs");
                    mapDataContainer = new MapDataContainer(new ArrayList<MapConfigLayer>());
                }
                else {
                    mapDataContainer = new MapDataContainer(mapConfigs);
                }
                return mapDataContainer;
            }
            
            public MapDataContainer getMapTile(MapDataContainer mapDataContainer) throws RemoteException {
                if (mapDataContainer == null || !mapDataContainer.isValid(2)) {
                    Logger.logW(MapTileService.TAG, "getMapTile(" + mapDataContainer + "), invalid request");
                    final MapTileResponse mapTileResponse = new MapTileResponse();
                    mapTileResponse.setResultCode(2);
                    mapDataContainer = new MapDataContainer(mapTileResponse);
                }
                else {
                    final MapTileResponse mapTile = MapTileService.this.getMapTile(mapDataContainer.getTileRequest());
                    if (mapTile == null) {
                        Logger.logW(MapTileService.TAG, "getMapTile(" + mapDataContainer + "), invalid response");
                        final MapTileResponse mapTileResponse2 = new MapTileResponse();
                        mapTileResponse2.setResultCode(4);
                        mapDataContainer = new MapDataContainer(mapTileResponse2);
                    }
                    else {
                        mapDataContainer = new MapDataContainer(mapTile);
                    }
                }
                return mapDataContainer;
            }
        };
    }
    
    public abstract List<MapConfigLayer> getMapConfigs();
    
    public abstract MapTileResponse getMapTile(final MapTileRequest p0);
    
    public IBinder onBind(final Intent intent) {
        return (IBinder)this.mBinder;
    }
    
    public void onCreate() {
        super.onCreate();
    }
}
